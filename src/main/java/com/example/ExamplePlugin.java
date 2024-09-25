package com.example;

import com.google.common.base.Strings;
import com.google.inject.Provides;

import javax.annotation.Nullable;
import javax.inject.Inject;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.*;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

@Slf4j
@PluginDescriptor(
        name = "Example"
)
public class ExamplePlugin extends Plugin {
    @Inject
    private Client client;

    @Inject
    private ExampleConfig config;

    private HelperObject helperObject;

    private MixologyStateMachine machine = new MixologyStateMachine();
    private MixologyState lastState = machine.getState();

    @Override
    protected void startUp() throws Exception {
        log.info("Example started!");
    }

    @Override
    protected void shutDown() throws Exception {
        log.info("Example stopped!");
    }

    @Subscribe
    public void onGameStateChanged(GameStateChanged gameStateChanged) {
        if (gameStateChanged.getGameState() == GameState.LOGGED_IN) {
            client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Example says " + config.greeting(), null);
            log("VARBITS", "MIXER LEFT: " + client.getVarbitValue(MixologyVarbits.MIXER_LEFT));
            log("VARBITS", "MIXER RIGHT: " + client.getVarbitValue(MixologyVarbits.MIXER_MIDDLE));
            log("VARBITS", "MIXER MIDDLE: " + client.getVarbitValue(MixologyVarbits.MIXER_RIGHT));
            log("VARBITS", "POTION ORDER THIRD POT: " + client.getVarbitValue(MixologyVarbits.ORDER_THIRD_POTION));
        }
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
    public void onGameObjectSpawned(GameObjectSpawned event) {
        GameObject gameObject = event.getGameObject();

        if (gameObject.getId() == 55395 && helperObject == null) {
            ObjectComposition objectDefinition = getObjectComposition(gameObject.getId());
            helperObject = new HelperObject(gameObject.getId(), objectDefinition);
            log("DEBUG", "Vessel helper set");
        } else if (gameObject.getId() == 55395 && helperObject != null) {
            log("DEBUG", "DUPE VESSEL SPAWN?");
        }

//		log("Game object spawned", Integer.valueOf(gameObject.getId()).toString());
//		log("Game object spawned at", event.getTile().toString());
//
//        if (gameObject.getId() == 55391) {
//            log("Boldogsag", "Alembic");
//        }
    }

    @Subscribe
    public void onGameObjectDespawned(GameObjectDespawned event) {
        if (event.getGameObject().getId() == 55395 && helperObject != null) {
            helperObject = null;
            log("DEBUG", "Vessel helper unset");
        }
    }

    @Subscribe
    public void onGameTick(GameTick event) {
        if (helperObject != null) {
            helperObject.composition = getObjectComposition(helperObject.objectId);
//            log("DEBUG", helperObject.composition.getName());
        }

        machine.onTickStart(client);

        if (lastState != machine.getState()) {
            lastState = machine.getState();

            if (machine.getState() != MixologyState.WAITING_TO_START) {
                log("MIXOLOGY", machine.getNextStep());
            }
        }
    }

    @Subscribe
    public void onWidgetLoaded(WidgetLoaded event) {
//        log("DEBUG", event.toString());
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
    public void onVarbitChanged(VarbitChanged event)
    {
        if (MixologyVarbits.isRelevantVarbit(event.getVarbitId())) {
            machine.onVarbitsUpdated(client);

            if (lastState != machine.getState()) {
                lastState = machine.getState();

                if (machine.getState() != MixologyState.WAITING_TO_START) {
                    log("MIXOLOGY", machine.getNextStep());
                }
            }
//            log("EASY MIXOLOGY", machine.getState().toString());
        } else {
//            log("DEBUG - IRRELEVANT VARBIT", "" + event.getVarbitId());
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
    private ObjectComposition getObjectComposition(int id)
    {
        ObjectComposition objectComposition = client.getObjectDefinition(id);
        return objectComposition.getImpostorIds() == null ? objectComposition : objectComposition.getImpostor();
    }

    private TileObject findTileObject(int z, int x, int y, int id)
    {
        Scene scene = client.getScene();
        Tile[][][] tiles = scene.getTiles();
        final Tile tile = tiles[z][x][y];
        if (tile == null)
        {
            return null;
        }

        final GameObject[] tileGameObjects = tile.getGameObjects();
        final DecorativeObject tileDecorativeObject = tile.getDecorativeObject();
        final WallObject tileWallObject = tile.getWallObject();
        final GroundObject groundObject = tile.getGroundObject();

        if (objectIdEquals(tileWallObject, id))
        {
            if (id == 54867) {
                log("MATEDEBUG", "AGA lever is a tile wall object");
            }
            return tileWallObject;
        }

        if (objectIdEquals(tileDecorativeObject, id))
        {
            if (id == 54867) {
                log("MATEDEBUG", "AGA lever is a decorative object");
            }
            return tileDecorativeObject;
        }

        if (objectIdEquals(groundObject, id))
        {
            if (id == 54867) {
                log("MATEDEBUG", "AGA lever is a ground object");
            }
            return groundObject;
        }

        for (GameObject object : tileGameObjects)
        {
            if (objectIdEquals(object, id))
            {
                if (id == 54867) {
                    log("MATEDEBUG", "AGA lever is a game object");
                }
                return object;
            }
        }

        return null;
    }

    private boolean objectIdEquals(TileObject tileObject, int id)
    {
        if (tileObject == null)
        {
            return false;
        }

        if (tileObject.getId() == id)
        {
            return true;
        }

        // Menu action EXAMINE_OBJECT sends the transformed object id, not the base id, unlike
        // all of the GAME_OBJECT_OPTION actions, so check the id against the impostor ids
        final ObjectComposition comp = client.getObjectDefinition(tileObject.getId());

        if (comp.getImpostorIds() != null)
        {
            for (int impostorId : comp.getImpostorIds())
            {
                if (impostorId == id)
                {
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
    public void onMenuEntryAdded(MenuEntryAdded event)
    {
        if (event.getType() != MenuAction.EXAMINE_OBJECT.getId() || !client.isKeyPressed(KeyCode.KC_SHIFT))
        {
            return;
        }

        final TileObject tileObject = findTileObject(client.getPlane(), event.getActionParam0(), event.getActionParam1(), event.getIdentifier());
        if (tileObject == null)
        {
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

    private void logObjectInfo(MenuEntry entry)
    {
        TileObject object = findTileObject(client.getPlane(), entry.getParam0(), entry.getParam1(), entry.getIdentifier());
        if (object == null)
        {
            return;
        }

        // object.getId() is always the base object id, getObjectComposition transforms it to
        // the correct object we see
        ObjectComposition objectDefinition = getObjectComposition(object.getId());
        String name = objectDefinition.getName();
        // Name is probably never "null" - however prevent adding it if it is, as it will
        // become ambiguous as objects with no name are assigned name "null"
        if (Strings.isNullOrEmpty(name) || name.equals("null"))
        {
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
    ExampleConfig provideConfig(ConfigManager configManager) {
        return configManager.getConfig(ExampleConfig.class);
    }
}
