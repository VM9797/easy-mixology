package com.vasmatheus.easymixology;

import com.google.inject.Provides;
import com.vasmatheus.easymixology.constants.MixologyIDs;
import com.vasmatheus.easymixology.constants.MixologyVarbits;
import com.vasmatheus.easymixology.model.MixologyStateMachine;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.events.*;
import net.runelite.api.widgets.Widget;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

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
    public void onVarbitChanged(VarbitChanged event) {
        if (MixologyVarbits.isRelevantVarbit(event.getVarbitId())) {
            mixologyStateMachine.onVarbitsUpdated();
        }
    }

    @Provides
    EasyMixologyConfig provideConfig(ConfigManager configManager) {
        return configManager.getConfig(EasyMixologyConfig.class);
    }
}
