package com.vasmatheus.seupulchrestrangetile;

import net.runelite.api.events.GameTick;
import net.runelite.api.events.GroundObjectDespawned;
import net.runelite.api.events.GroundObjectSpawned;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.ui.overlay.OverlayManager;

import javax.inject.Inject;

public class SepulchreStrangeTileMarkerPlugin extends Plugin {
    @Inject
    private OverlayManager overlayManager;

    @Inject
    private StrangeTileOverlay strangeTileOverlay;

    @Override
    protected void startUp() throws Exception {
        overlayManager.add(strangeTileOverlay);
    }

    @Override
    protected void shutDown() throws Exception {
        overlayManager.remove(strangeTileOverlay);
    }

    @Subscribe
    public void onGroundObjectSpawned(GroundObjectSpawned event) {
        var object = event.getGroundObject();

        // Strange tile (BLUE)
        if (object.getId() == 38448) {
            strangeTileOverlay.addStrangeTile(object);
        }
    }

    @Subscribe
    public void onGroundObjectDespawned(GroundObjectDespawned event) {
        var object = event.getGroundObject();

        // Strange tile (BLUE)
        if (object.getId() == 38448) {
            strangeTileOverlay.removeStrangeTile(object);
        }
    }

    @Subscribe
    public void onGameTick(GameTick event) {
        strangeTileOverlay.onTick();
    }
}
