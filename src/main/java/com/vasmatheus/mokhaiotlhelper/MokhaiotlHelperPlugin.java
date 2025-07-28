package com.vasmatheus.mokhaiotlhelper;

import com.google.common.annotations.VisibleForTesting;
import com.google.inject.Provides;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.*;
import net.runelite.api.gameval.VarPlayerID;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.callback.Hooks;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

import javax.inject.Inject;
import java.util.*;

@Slf4j
@PluginDescriptor(
        name = "Doom of Mokhaiotl",
        description = "Plugin providing numerous utilities for the delve boss",
        tags = {"doom", "mokhaiotl", "delve", "enrage"}
)
public class MokhaiotlHelperPlugin extends Plugin {
    private boolean isInDelveRegion = false;
    private final List<DelveGrub> grubs = new ArrayList<>();
    private final List<DelveGrub> deadGrubs = new ArrayList<>();
    private final Map<Skill, Integer> fakeXpMap = new EnumMap<>(Skill.class);
    private final Map<Skill, Integer> previousXpMap = new EnumMap<>(Skill.class);

    private static final Set<Integer> DELVE_REGION_IDS = Set.of(5269, 13668, 14180);
    private static final Set<Integer> DELVE_GRUB_IDS = Set.of(14710, 14711, 14712, 14713);

    private final Hooks.RenderableDrawListener drawListener = this::shouldDraw;

    @Inject
    private Client client;

    @Inject
    private ClientThread clientThread;

    @Inject
    private Hooks hooks;

    @Override
    protected void startUp() {
        clientThread.invoke(this::initializePreviousXpMap);

        hooks.registerRenderableDrawListener(drawListener);
    }

    @Override
    protected void shutDown() {
        hooks.unregisterRenderableDrawListener(drawListener);
    }

    private void initializePreviousXpMap() {
        if (client.getGameState() != GameState.LOGGED_IN) {
            previousXpMap.clear();
        } else {
            for (final Skill skill : Skill.values()) {
                previousXpMap.put(skill, client.getSkillExperience(skill));
            }
        }
    }

    @Subscribe
    protected void onGameStateChanged(GameStateChanged event) {
        if (event.getGameState() == GameState.LOGGED_IN) {
            if (!isInDelveRegion) {
                isInDelveRegion = isInDelveRegion();
            } else {
                isInDelveRegion = isInDelveRegion();
                if (!isInDelveRegion) {
                    grubs.clear();
                    deadGrubs.clear();
                }
            }
        }
    }

    @Subscribe
    protected void onGameTick(GameTick event) {
        // Group FakeXP drops and process them every game tick
        for (Map.Entry<Skill, Integer> xp : fakeXpMap.entrySet()) {
            processXpDrop(xp.getKey(), xp.getValue());
        }
        fakeXpMap.clear();

        Iterator<DelveGrub> grubIterator = deadGrubs.iterator();
        while (grubIterator.hasNext()) {
            DelveGrub grub = grubIterator.next();
            grub.setHidden(grub.getHidden() + 1);

            final boolean isDead = grub.getNpc().getHealthRatio() == 0;
            // What is this magic 5 here?
            if (grub.getHidden() > 5 && !isDead) {
                grub.setHidden(0);
                grubIterator.remove();
            }
        }
    }


    @Subscribe
    protected void onNpcSpawned(NpcSpawned event) {
        if (!isInDelveRegion) {
            return;
        }



        final NPC npc = event.getNpc();
        final boolean isGrub = isNpcGrub(npc.getId());

        if (!isGrub) {
            return;
        }

        grubs.add(new DelveGrub(npc, npc.getIndex()));
    }

    private boolean isNpcGrub(int npcId) {
        return DELVE_GRUB_IDS.contains(npcId);
    }

    @Subscribe
    protected void onNpcDespawned(NpcDespawned event) {
        if (!isInDelveRegion) {
            return;
        }

        grubs.removeIf((grub) -> grub.getNpcIndex() == event.getNpc().getIndex());
        deadGrubs.removeIf((grub) -> grub.getNpcIndex() == event.getNpc().getIndex());
    }

    @Subscribe
    protected void onHitsplatApplied(HitsplatApplied event) {
        if (!isInDelveRegion) {
            return;
        }

        Actor actor = event.getActor();
        if (actor instanceof NPC) {
            final int npcIndex = ((NPC) actor).getIndex();
            final int damage = event.getHitsplat().getAmount();

            DelveGrub grub = grubs.stream()
                    .filter(n -> n.getNpcIndex() == npcIndex)
                    .findFirst().orElse(null);
            if (grub == null) {
                return;
            }

            if (event.getHitsplat().getHitsplatType() == HitsplatID.HEAL) {
                grub.setHp(grub.getHp() + damage);
            } else {
                grub.setHp(grub.getHp() - damage);
            }
            grub.setQueuedDamage(Math.max(0, grub.getQueuedDamage() - damage));
        }
    }

    @Subscribe
    protected void onNpcDamaged(NpcDamaged event) {
        if (!isInDelveRegion) {
            return;
        }

        final int npcIndex = event.getNpcIndex();
        final int damage = event.getDamage();

        DelveGrub grub = grubs.stream()
                .filter(n -> n.getNpcIndex() == npcIndex)
                .findFirst().orElse(null);

        if (grub == null) {
            return;
        }

        grub.setQueuedDamage(grub.getQueuedDamage() + damage);

        if (grub.getHp() - grub.getQueuedDamage() <= 0) {
            if (deadGrubs.stream().noneMatch(deadGrub -> deadGrub.getNpcIndex() == npcIndex)) {
                deadGrubs.add(grub);
                grub.getNpc().setDead(true);
            }
        }
    }

    @Subscribe
    protected void onFakeXpDrop(FakeXpDrop event) {
        final int currentXp = fakeXpMap.getOrDefault(event.getSkill(), 0);
        fakeXpMap.put(event.getSkill(), currentXp + event.getXp());
    }

    @Subscribe
    protected void onStatChanged(StatChanged event) {
        preProcessXpDrop(event.getSkill(), event.getXp());
    }

    private void preProcessXpDrop(Skill skill, int xp) {
        final int xpAfter = client.getSkillExperience(skill);
        final int xpBefore = previousXpMap.getOrDefault(skill, -1);

        previousXpMap.put(skill, xpAfter);

        if (xpBefore == -1 || xpAfter <= xpBefore) {
            return;
        }

        processXpDrop(skill, xpAfter - xpBefore);
    }

    private void processXpDrop(Skill skill, final int xp) {
        if (!isInDelveRegion()) {
            return;
        }

        int damage = 0;

        Player player = client.getLocalPlayer();
        if (player == null) {
            return;
        }

        PlayerComposition playerComposition = player.getPlayerComposition();
        if (playerComposition == null) {
            return;
        }

        final int attackStyle = client.getVarpValue(VarPlayerID.COM_MODE);


        switch (skill) {
            case MAGIC:
                // TODO this is only for superior demonbane right now
                damage = (xp - 36) / 2;
                break;
            case ATTACK:
            case STRENGTH:
            case DEFENCE:
                damage = (int) ((double) xp / 4.0D);
                break;
            case RANGED:
                if (attackStyle == 3) {
                    // Defensive: Ranged XP = 2x damage dealt
                    damage = (int) ((double) xp / 2.0D);
                } else {
                    // Accurate/Rapid: Ranged XP = 4x damage dealt
                    damage = (int) ((double) xp / 4.0D);
                }
                break;
            default:
                return;
        }

        if (damage > 0) {
            sendDamage(player, damage);
        }
    }

    private void sendDamage(Player player, int damage) {
        if (damage <= 0) {
            return;
        }

        Actor interacted = player.getInteracting();
        if (interacted instanceof NPC) {
            NPC interactedNPC = (NPC) interacted;
            final int npcIndex = interactedNPC.getIndex();
            final NpcDamaged npcDamaged = new NpcDamaged(npcIndex, damage);
            onNpcDamaged(npcDamaged);
        }
    }


    private boolean isInDelveRegion() {
        WorldView wv = client.getTopLevelWorldView();
        if (wv == null) {
            return false;
        }

        int[] regions = wv.getMapRegions();
        if (regions == null) {
            return false;
        }


        for (int region : regions) {
            if (DELVE_REGION_IDS.contains(region)) {
                return true;
            }
        }

        return false;
    }

    @VisibleForTesting
    boolean shouldDraw(Renderable renderable, boolean drawingUI) {
        if (renderable instanceof NPC) {
            return deadGrubs.stream()
                    .noneMatch(grub -> grub.getNpcIndex() == ((NPC) renderable).getIndex());
        }

        return true;
    }


    @Provides
    MokhaiotlHelperConfig provideConfig(ConfigManager configManager) {
        return configManager.getConfig(MokhaiotlHelperConfig.class);
    }
}
