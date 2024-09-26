package com.vasmatheus.easymixology;

import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.MenuEntryAdded;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
@Slf4j
public class DebugUtil {
    @Inject
    private Client client;

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

        log("Object details", name + " - " + object.getId() + " / " + objectDefinition.getId());

//        markObject(objectDefinition, name, object);
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

    @Nullable
    private ObjectComposition getObjectComposition(int id) {
        ObjectComposition objectComposition = client.getObjectDefinition(id);
        return objectComposition.getImpostorIds() == null ? objectComposition : objectComposition.getImpostor();
    }

    public void log(String eventType, String message) {
        client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "[" + eventType + "]" + message, null);
        log.info("[" + eventType + "]" + message);
    }
}
