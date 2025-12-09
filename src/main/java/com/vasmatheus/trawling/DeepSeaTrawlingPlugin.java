package com.vasmatheus.trawling;

import com.google.common.base.Strings;
import com.google.inject.Provides;
import java.util.*;
import javax.annotation.Nullable;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.*;
import net.runelite.api.gameval.InterfaceID;
import net.runelite.api.widgets.Widget;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;
import org.apache.commons.lang3.tuple.Pair;

@Slf4j
@PluginDescriptor(
    name = "Deep sea trawling utilities",
    description = "",
    tags = {"sailing", "fishing", "trawling"})
public class DeepSeaTrawlingPlugin extends Plugin {
  @Inject private OverlayManager overlayManager;

  @Inject private Client client;

  @Inject private ClientThread clientThread;

  @Inject private DeepSeaTrawlingConfig config;

  @Inject private ShoalTileDrawOverlay overlay;

  private static final int SCRIPT_LOGOUT_LAYOUT_UPDATE = 2243;
  private static final int SCRIPT_WORLD_SWITCHER_DRAW = 892;

  private final Set<Integer> childrenToHide = new HashSet<>();
  private final Set<Widget> childrenToHideWidgets = new HashSet<>();
  private final Set<Integer> clicklayerChildrenToHide = new HashSet<>();
  private final Set<Widget> clicklayerChildrenToHideWidgets = new HashSet<>();

  private final Map<Integer, Integer> originalYMap = new HashMap<>();
  private final Map<Integer, Integer> originalInteractYMap = new HashMap<>();

  @Override
  protected void startUp() throws Exception {

    if (client.getGameState() == GameState.LOGGED_IN) {
      clientThread.invokeLater(this::modifySailingUi);
    }

    overlayManager.add(overlay);
  }

  //  @Subscribe
  //  public void onChatMessage(ChatMessage event) {
  //    final var message = event.getMessage();
  //
  //    if (message != null) {
  //      final var split = message.split(":");
  //
  //      if (split.length == 2 && isStringInteger(split[1])) {
  //        final var id = Integer.parseInt(split[1]);
  //
  //        if (split[0].equalsIgnoreCase("hide")) {
  //          childrenToHide.add(id);
  //        } else if (split[0].equalsIgnoreCase("show")) {
  //          childrenToHide.remove(id);
  //        }
  //      }
  //    }
  //  }

  private boolean isStringInteger(String s) {
    try {
      Integer.parseInt(s);
      return true;
    } catch (NumberFormatException e) {
      return false;
    }
  }

  private boolean isRange(String s) {
    final var split = s.split("-");

    if (split.length != 2) {
      return false;
    }

    return isStringInteger(split[0])
        && isStringInteger(split[1])
        && Integer.parseInt(split[0]) <= Integer.parseInt(split[1]);
  }

  private Pair<Integer, Integer> getRange(String s) {
    final var split = s.split("-");
    if (split.length != 2) {
      throw new RuntimeException("Invalid range format");
    }

    if (!isStringInteger(split[0]) || !isStringInteger(split[1])) {
      throw new RuntimeException("Invalid range format, range parts are not integers");
    }

    final var first = Integer.parseInt(split[0]);
    final var second = Integer.parseInt(split[1]);

    if (first > second) {
      throw new RuntimeException(
          "Invalid range format, left side of range must be smaller than right side of range!");
    }

    return Pair.of(Integer.parseInt(split[0]), Integer.parseInt(split[1]));
  }

  private boolean isInRange(Pair<Integer, Integer> range, int i) {
    return range.getLeft() <= i && i <= range.getRight();
  }

  private void modifySailingUi() {
    final var displayWidgets = client.getWidget(InterfaceID.SailingSidepanel.FACILITIES_ROWS);
    final var clicklayerWidgets =
        client.getWidget(InterfaceID.SailingSidepanel.FACILITIES_CONTENT_CLICKLAYER);
    childrenToHideWidgets.clear();
    clicklayerChildrenToHideWidgets.clear();

    // indexes 96-163 range is the trawling stuff
    // indexes 46-95 range the repair, wind mote and chum stuff

    // indexes 40-47 is the interaction range for the trawling stuff
    // indexes 10-40 is the interaction for the repair, mote and chum

    // pushdown range should be 66
    // pullup range should be 102

    // TODO: This is dodgy AF
    final var trawlingIndexRange = Pair.of(96, 163);
    final var trawlingInteractIndexRange = Pair.of(40, 47);

    final var repairWindMoteAndChumIndexRange = Pair.of(46, 95);
    final var repairWindMoteAndChumInteractIndexRange = Pair.of(10, 40);

    offsetWidgets(
        displayWidgets,
        trawlingIndexRange,
        repairWindMoteAndChumIndexRange,
        originalYMap,
        childrenToHide,
        childrenToHideWidgets);

    offsetWidgets(
        clicklayerWidgets,
        trawlingInteractIndexRange,
        repairWindMoteAndChumInteractIndexRange,
        originalInteractYMap,
        clicklayerChildrenToHide,
        clicklayerChildrenToHideWidgets);
  }

  // TODO: This is dodgy AF
  private void offsetWidgets(
      Widget rootWidget,
      Pair<Integer, Integer> pullWidgetsUpIndexRange,
      Pair<Integer, Integer> pushWidgetsDownIndexRange,
      Map<Integer, Integer> originalYMap,
      Set<Integer> additionalChildrenToHideFromConfig,
      Set<Widget> widgetsToPrintIdsForForDebugging) {
    if (rootWidget != null && rootWidget.getChildren() != null) {
      final var children = rootWidget.getChildren();

      for (int i = 0; i < children.length; i++) {
        originalYMap.putIfAbsent(i, children[i].getOriginalY());

        if (isInRange(pullWidgetsUpIndexRange, i)) {
          children[i].setOriginalY(originalYMap.get(i) - config.yOffsetPullUp());
          children[i].revalidate();
        }

        if (isInRange(pushWidgetsDownIndexRange, i)) {
          children[i].setOriginalY(originalYMap.get(i) + config.yOffsetPushDown());
          children[i].revalidate();
        }

        children[i].setHidden(additionalChildrenToHideFromConfig.contains(i));

        if (additionalChildrenToHideFromConfig.contains(i)) {
          widgetsToPrintIdsForForDebugging.add(children[i]);
        }
      }
    }
  }

  @Override
  protected void shutDown() throws Exception {
    overlay.clearShoalList();
    overlayManager.remove(overlay);
  }

  @Subscribe
  public void onScriptPostFired(ScriptPostFired event) {
    // TODO: Should find out the proper event ID here
    modifySailingUi();
  }

  @Subscribe
  public void onGroundObjectSpawned(GroundObjectSpawned event) {}

  private GameObject shoalObject;

  // 59737 = halibut shoal
  // 59741 = Glistening shoal
  // 59738 = Bluefin shoal
  // 59742 = Vibrant shoal
  private final Set<Integer> shoalObjectIds = Set.of(59737, 59741, 59738, 59742);

  @Subscribe
  public void onGameObjectSpawned(GameObjectSpawned event) {
    if (event.getGameObject() == null) {
      return;
    }

    final var id = event.getGameObject().getId();

    if (shoalObjectIds.contains(event.getGameObject().getId())) {
      log.info("SPAWNED {}", event.getGameObject().getId());
      overlay.addShoalGameObject(event.getGameObject());
    }
  }

  @Subscribe
  public void onGameObjectDespawned(GameObjectDespawned event) {
    if (event.getGameObject() == null) {
      return;
    }

    final var id = event.getGameObject().getId();

    if (shoalObjectIds.contains(event.getGameObject().getId())) {
      log.info("DESPAWNED {}", event.getGameObject().getId());
      overlay.removeShoalGameObject(event.getGameObject());
    }
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

  private TileObject findTileObject(WorldView wv, int x, int y, int id) {
    int level = wv.getPlane();
    Scene scene = wv.getScene();
    Tile[][][] tiles = scene.getTiles();
    final Tile tile = tiles[level][x][y];
    if (tile == null) {
      return null;
    }

    final GameObject[] tileGameObjects = tile.getGameObjects();
    final DecorativeObject tileDecorativeObject = tile.getDecorativeObject();
    final WallObject tileWallObject = tile.getWallObject();
    final GroundObject groundObject = tile.getGroundObject();

    if (objectIdEquals(tileWallObject, id)) {
      return tileWallObject;
    }

    if (objectIdEquals(tileDecorativeObject, id)) {
      return tileDecorativeObject;
    }

    if (objectIdEquals(groundObject, id)) {
      return groundObject;
    }

    for (GameObject object : tileGameObjects) {
      if (objectIdEquals(object, id)) {
        return object;
      }
    }

    return null;
  }

  @Subscribe
  public void onMenuEntryAdded(MenuEntryAdded event) {
    if (event.getType() != MenuAction.EXAMINE_OBJECT.getId()
        || !client.isKeyPressed(KeyCode.KC_SHIFT)) {
      return;
    }

    int worldId = event.getMenuEntry().getWorldViewId();
    WorldView wv = client.getWorldView(worldId);
    if (wv == null) {
      return;
    }

    final TileObject tileObject =
        findTileObject(wv, event.getActionParam0(), event.getActionParam1(), event.getIdentifier());
    if (tileObject == null) {
      return;
    }

    int idx = -1;
    client
        .createMenuEntry(idx--)
        .setOption("DEBUG")
        .setTarget(event.getTarget())
        .setWorldViewId(worldId)
        .setParam0(event.getActionParam0())
        .setParam1(event.getActionParam1())
        .setIdentifier(event.getIdentifier())
        .setType(MenuAction.RUNELITE)
        .onClick(this::debugObject);
  }

  @Nullable
  private ObjectComposition getObjectComposition(int id) {
    ObjectComposition objectComposition = client.getObjectDefinition(id);
    return objectComposition.getImpostorIds() == null
        ? objectComposition
        : objectComposition.getImpostor();
  }

  private void debugObject(MenuEntry entry) {
    WorldView wv = client.getWorldView(entry.getWorldViewId());
    if (wv == null) {
      return;
    }

    TileObject object =
        findTileObject(wv, entry.getParam0(), entry.getParam1(), entry.getIdentifier());
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

    final WorldPoint worldPoint = WorldPoint.fromLocalInstance(client, object.getLocalLocation());
    final int regionId = worldPoint.getRegionID();

    log.info(
        "DEBUG OBJECT ID: {}, OBJECT DEF ID: {}, NAME: {}",
        object.getId(),
        objectDefinition.getId(),
        name);
  }

  @Subscribe
  public void onGroundObjectDespawned(GroundObjectDespawned event) {}

  @Subscribe
  public void onGameTick(GameTick event) {
    modifySailingUi();
  }

  @Provides
  DeepSeaTrawlingConfig provideConfig(ConfigManager configManager) {
    return configManager.getConfig(DeepSeaTrawlingConfig.class);
  }

  private void refreshIdsToHide(Set<Integer> idsToHide, String content) {
    idsToHide.clear();

    if (content == null || content.isEmpty()) return;

    final var split = content.split(",");

    Arrays.stream(split)
        .filter(this::isStringInteger)
        .map(Integer::parseInt)
        .forEach(idsToHide::add);
    Arrays.stream(split)
        .filter(this::isRange)
        .map(this::getRange)
        .forEach(
            range -> {
              for (int i = range.getLeft(); i <= range.getRight(); i++) {
                idsToHide.add(i);
              }
            });
  }

  private void printWidgetIds() {
    log.info("DRAWN WIDGET IDS HIDDEN");
    childrenToHideWidgets.forEach(
        it -> {
          log.info("ID: {}", it.getId());
        });

    log.info("INTERACT WIDGET IDS HIDDEN");
    clicklayerChildrenToHideWidgets.forEach(
        it -> {
          log.info("ID: {}", it.getId());
        });
  }

  // indexes 96-163 range is the trawling stuff
  // indexes 46-95 range the repair, wind mote and chum stuff

  // indexes 40-47 is the interaction range for the trawling stuff
  // indexes 10-40 is the interaction for the repair, mote and chum
  @Subscribe
  public void onConfigChanged(ConfigChanged configChanged) {
    if (configChanged.getGroup().equals(DeepSeaTrawlingConfig.GROUP)) {
      refreshIdsToHide(childrenToHide, config.indexesToHide());
      refreshIdsToHide(clicklayerChildrenToHide, config.clicklayerIndexesToHide());
      printWidgetIds();
    }
  }
}
