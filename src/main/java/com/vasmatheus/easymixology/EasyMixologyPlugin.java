package com.vasmatheus.easymixology;

import com.google.common.base.Strings;
import com.google.inject.Provides;
import com.vasmatheus.easymixology.constants.MixologyIDs;
import com.vasmatheus.easymixology.constants.MixologyVarbits;
import com.vasmatheus.easymixology.model.MixologyStateMachine;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.*;
import net.runelite.api.widgets.Widget;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

import javax.annotation.Nullable;
import javax.inject.Inject;

@Slf4j
@PluginDescriptor(
        name = "Easy Mixology",
        description = "Helper plugin to improve experience for Mastering Mixology minigame",
        tags = {"mastering", "mixology", "minigame", "herblore", "alchemy", "lab", "herb", "paste", "mox", "lye", "aga", "potion"}
)
public class EasyMixologyPlugin extends Plugin {
    private static final int ARE_BOOTSTRAP_TICK_COUNTER_START = 4;

    @Inject
    private Client client;

    @Inject
    private MixologyStateMachine mixologyStateMachine;

    @Inject
    private EasyMixologyOverlay3D overlay3D;

    @Inject
    private OverlayManager overlayManager;

    private int areaBootstrapTickCounter = ARE_BOOTSTRAP_TICK_COUNTER_START;
    private boolean inArea = false;

    @Override
    protected void startUp() throws Exception {
        overlayManager.add(overlay3D);
    }

    @Override
    protected void shutDown() throws Exception {
        overlayManager.remove(overlay3D);

    }

    @Subscribe
    public void onGameStateChanged(GameStateChanged gameStateChanged) {
    }

    @Subscribe
    public void onNpcSpawned(NpcSpawned event) {
//		if (event.getNpc().getId() == KOVAC_NPC)
//		{
//			overlay3d.kovac = event.getNpc();
//		}
//		log("NPC spawned", event.getNpc().getName());
    }

    @Subscribe
    public void onNpcDespawned(NpcDespawned event) {
    }

    @Subscribe
    public void onDecorativeObjectSpawned(DecorativeObjectSpawned event) {
        var object = event.getDecorativeObject();

        if (object.getId() == MixologyIDs.AGA_LEVER) {
            overlay3D.agaLever = object;
        }
    }

    @Subscribe
    public void onDecorativeObjectDespawned(DecorativeObjectDespawned event) {
        var object = event.getDecorativeObject();

        if (object.getId() == MixologyIDs.AGA_LEVER) {
            overlay3D.agaLever = null;
        }
    }

    @Subscribe
    public void onGameObjectSpawned(GameObjectSpawned event) {
        var object = event.getGameObject();

        switch (object.getId()) {
            case MixologyIDs.CONVEYOR_BELT:
                overlay3D.conveyorBelts.add(object);
                break;
            case MixologyIDs.LYE_LEVER:
                overlay3D.lyeLever = object;
                break;
            case MixologyIDs.MOX_LEVER:
                overlay3D.moxLever = object;
                break;
            case MixologyIDs.ALEMBIC:
                overlay3D.alembic = object;
                break;
            case MixologyIDs.AGITATOR:
                overlay3D.agitator = object;
                break;
            case MixologyIDs.RETORT:
                overlay3D.retort = object;
                break;
            case MixologyIDs.VESSEL:
                overlay3D.vessel = object;
                break;
        }

//        if (gameObject.getId() == 54917) {
//            if (overlay3D.conveyorBeltOne == null) {
//                overlay3D.conveyorBeltOne = gameObject;
//            } else if (overlay3D.conveyorBeltTwo == null) {
//                overlay3D.conveyorBeltTwo = gameObject;
//            }
//        }
//
//        if (gameObject.getId() == 55395 && helperObject == null) {
//            ObjectComposition objectDefinition = getObjectComposition(gameObject.getId());
//            helperObject = new HelperObject(gameObject.getId(), objectDefinition);
//            log("DEBUG", "Vessel helper set");
//        } else if (gameObject.getId() == 55395 && helperObject != null) {
//            log("DEBUG", "DUPE VESSEL SPAWN?");
//        }

//		log("Game object spawned", Integer.valueOf(gameObject.getId()).toString());
//		log("Game object spawned at", event.getTile().toString());
//
//        if (gameObject.getId() == 55391) {
//            log("Boldogsag", "Alembic");
//        }
    }

    @Subscribe
    public void onGameObjectDespawned(GameObjectDespawned event) {
        var object = event.getGameObject();

        switch (object.getId()) {
            case MixologyIDs.CONVEYOR_BELT:
                overlay3D.conveyorBelts.remove(object);
                break;
            case MixologyIDs.LYE_LEVER:
                overlay3D.lyeLever = null;
                break;
            case MixologyIDs.MOX_LEVER:
                overlay3D.moxLever = null;
                break;
            case MixologyIDs.ALEMBIC:
                overlay3D.alembic = null;
                break;
            case MixologyIDs.AGITATOR:
                overlay3D.agitator = null;
                break;
            case MixologyIDs.RETORT:
                overlay3D.retort = null;
                break;
            case MixologyIDs.VESSEL:
                overlay3D.vessel = null;
                break;
        }
    }

    @Subscribe
    public void onWidgetLoaded(WidgetLoaded event) {
    }

    @Subscribe
    public void onGameTick(GameTick event) {
        Widget mixologyWidget = client.getWidget(MixologyIDs.MIXOLOGY_WIDGET_ID);

        if (mixologyWidget != null) {
            inArea = true;

            if (areaBootstrapTickCounter >= 0) {
                areaBootstrapTickCounter--;

                if (areaBootstrapTickCounter < 0) {
                    mixologyStateMachine.start();
                }
            }
        } else {
            if (inArea) {
                inArea = false;
                areaBootstrapTickCounter = ARE_BOOTSTRAP_TICK_COUNTER_START;
                mixologyStateMachine.stop();
            }
        }
    }

    @Subscribe
    public void onMenuOpened(MenuOpened event) {

//        log("Menu opened", event.toString());
//
//        log("Menu opened", firstEntry.getTarget());
//        log("Menu opened", firstEntry.getOption());
//        log("Menu opened", firstEntry.getItemId() + "");
//        log("Menu opened", firstEntry.getItemOp() + "");
    }

    @Subscribe
    public void onVarbitChanged(VarbitChanged event) {
        if (MixologyVarbits.isRelevantVarbit(event.getVarbitId())) {
            mixologyStateMachine.onVarbitsUpdated();
        }
    }


    //    @Subscribe
//    public void onVarbitChanged(VarbitChanged event)
//    {
//        if (event.getVarpId() == REPUTATION_VARBIT)
//        {
//            reputation = client.getVarpValue(REPUTATION_VARBIT);
//        }
//
//        // start the heating state-machine when the varbit updates
//        // if heat varbit updated and the user clicked, start the state-machine
//        if (event.getVarbitId() == VARBIT_HEAT && state.heatingCoolingState.getActionName() != null)
//        {
//            // ignore passive heat decay, one heat per two ticks
//            if (event.getValue() - previousHeat != -1)
//            {
//                // if the state-machine is idle, start it
//                if (state.heatingCoolingState.isIdle())
//                {
//                    state.heatingCoolingState.start(state, config, state.getHeatAmount());
//                }
//
//                state.heatingCoolingState.onTick();
//            }
//            previousHeat = event.getValue();
//        }
//    }
//
    @Nullable
    private ObjectComposition getObjectComposition(int id) {
        ObjectComposition objectComposition = client.getObjectDefinition(id);
        return objectComposition.getImpostorIds() == null ? objectComposition : objectComposition.getImpostor();
    }

    private TileObject findTileObject(int z, int x, int y, int id) {
        Scene scene = client.getScene();
        Tile[][][] tiles = scene.getTiles();
        final Tile tile = tiles[z][x][y];
        if (tile == null) {
            return null;
        }

        final GameObject[] tileGameObjects = tile.getGameObjects();
        final DecorativeObject tileDecorativeObject = tile.getDecorativeObject();
        final WallObject tileWallObject = tile.getWallObject();
        final GroundObject groundObject = tile.getGroundObject();

        if (objectIdEquals(tileWallObject, id)) {
            if (id == 54867) {
                log("MATEDEBUG", "AGA lever is a tile wall object");
            }
            return tileWallObject;
        }

        if (objectIdEquals(tileDecorativeObject, id)) {
            if (id == 54867) {
                log("MATEDEBUG", "AGA lever is a decorative object");
            }
            return tileDecorativeObject;
        }

        if (objectIdEquals(groundObject, id)) {
            if (id == 54867) {
                log("MATEDEBUG", "AGA lever is a ground object");
            }
            return groundObject;
        }

        for (GameObject object : tileGameObjects) {
            if (objectIdEquals(object, id)) {
                if (id == 54867) {
                    log("MATEDEBUG", "AGA lever is a game object");
                }
                return object;
            }
        }

        return null;
    }

    private boolean objectIdEquals(TileObject tileObject, int id) {
        if (tileObject == null) {
            return false;
        }

        if (tileObject.getId() == id) {
            return true;
        }

        // Menu action EXAMINE_OBJECT sends the transformed object id, not the base id, unlike
        // all of the GAME_OBJECT_OPTION actions, so check the id against the impostor ids
        final ObjectComposition comp = client.getObjectDefinition(tileObject.getId());

        if (comp.getImpostorIds() != null) {
            for (int impostorId : comp.getImpostorIds()) {
                if (impostorId == id) {
                    return true;
                }
            }
        }

        return false;
    }

    private void log(String eventType, String message) {
        client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "[" + eventType + "]" + message, null);
        log.info("[" + eventType + "]" + message);
    }

    @Subscribe
    public void onMenuEntryAdded(MenuEntryAdded event) {
        if (event.getType() != MenuAction.EXAMINE_OBJECT.getId() || !client.isKeyPressed(KeyCode.KC_SHIFT)) {
            return;
        }

        final TileObject tileObject = findTileObject(client.getPlane(), event.getActionParam0(), event.getActionParam1(), event.getIdentifier());
        if (tileObject == null) {
            return;
        }

        int idx = -1;
        client.createMenuEntry(idx--)
                .setOption("LOG INFO")
                .setTarget(event.getTarget())
                .setParam0(event.getActionParam0())
                .setParam1(event.getActionParam1())
                .setIdentifier(event.getIdentifier())
                .setType(MenuAction.RUNELITE)
                .onClick(this::logObjectInfo);
    }

    private void logObjectInfo(MenuEntry entry) {
        TileObject object = findTileObject(client.getPlane(), entry.getParam0(), entry.getParam1(), entry.getIdentifier());
        if (object == null) {
            return;
        }

        // object.getId() is always the base object id, getObjectComposition transforms it to
        // the correct object we see
        ObjectComposition objectDefinition = getObjectComposition(object.getId());
        String name = objectDefinition.getName();
        // Name is probably never "null" - however prevent adding it if it is, as it will
        // become ambiguous as objects with no name are assigned name "null"
        if (Strings.isNullOrEmpty(name) || name.equals("null")) {
            return;
        }

        log("Menu opened", name + " - " + object.getId() + " / " + objectDefinition.getId());

//        markObject(objectDefinition, name, object);
    }

//    /** mark or unmark an object
//     *
//     * @param objectComposition transformed composition of object based on vars
//     * @param name name of objectComposition
//     * @param object tile object, for multilocs object.getId() is the base id
//     */
//    private void markObject(ObjectComposition objectComposition, String name, final TileObject object)
//    {
//        final WorldPoint worldPoint = WorldPoint.fromLocalInstance(client, object.getLocalLocation());
//        final int regionId = worldPoint.getRegionID();
//        final Color borderColor = config.markerColor();
//        final Color fillColor = config.fillColor();
//        final ObjectPoint point = new ObjectPoint(
//                object.getId(),
//                name,
//                regionId,
//                worldPoint.getRegionX(),
//                worldPoint.getRegionY(),
//                worldPoint.getPlane(),
//                borderColor,
//                fillColor,
//                // use the default config values
//                null, null, null, null);
//
//        Set<ObjectPoint> objectPoints = points.computeIfAbsent(regionId, k -> new HashSet<>());
//
//        if (objects.removeIf(o -> o.getTileObject() == object))
//        {
//            if (!objectPoints.removeIf(findObjectPredicate(objectComposition, object, worldPoint)))
//            {
//                log.warn("unable to find object point for unmarked object {}", object.getId());
//            }
//
//            log.debug("Unmarking object: {}", point);
//        }
//        else
//        {
//            objectPoints.add(point);
//            objects.add(new ColorTileObject(object,
//                    client.getObjectDefinition(object.getId()),
//                    name,
//                    borderColor,
//                    fillColor,
//                    (byte) 0));
//            log.debug("Marking object: {}", point);
//        }
//
//        savePoints(regionId, objectPoints);
//    }


    @Provides
    EasyMixologyConfig provideConfig(ConfigManager configManager) {
        return configManager.getConfig(EasyMixologyConfig.class);
    }
}
