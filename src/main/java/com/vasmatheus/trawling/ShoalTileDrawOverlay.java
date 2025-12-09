package com.vasmatheus.trawling;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.GameObject;
import net.runelite.api.Perspective;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayUtil;

@Slf4j
public class ShoalTileDrawOverlay extends Overlay {
  // TODO: Make these configurable
  private static final Color TILE_COLOR = Color.red;
  private static final Color TILE_FILL_COLOR = new Color(0, 0, 0, 50);

  @Inject private Client client;
  @Inject private DeepSeaTrawlingConfig config;

  private final Set<GameObject> shoalObjects = new HashSet<>();

  public ShoalTileDrawOverlay() {
    setPosition(OverlayPosition.DYNAMIC);
    setLayer(OverlayLayer.ABOVE_SCENE);
    setPriority(Overlay.PRIORITY_LOW);
  }

  public void addShoalGameObject(GameObject gameObject) {
    shoalObjects.add(gameObject);
  }

  public void removeShoalGameObject(GameObject gameObject) {
    shoalObjects.remove(gameObject);
  }

  public void clearShoalList() {
    shoalObjects.clear();
  }

  @Override
  public Dimension render(Graphics2D graphics) {
    final var outlineColor = config.shoalOutlineColor();
    final var areaColor = config.shoalAreaColor();
    final var shoalAreaSize = config.shoalSize();
    final var stroke = new BasicStroke((float) config.shoalStrokeSize());

    final var uniqueShoalObjectsByCoordinates =
        new ArrayList<>(
            shoalObjects.stream()
                .collect(
                    Collectors.toMap(
                        o ->
                            o.getWorldLocation().getX()
                                + ","
                                + o.getWorldLocation().getY(), // key extractor
                        o -> o, // value
                        (o1, o2) -> o1 // merge function (keep first)
                        ))
                .values());

    for (GameObject gameObject : uniqueShoalObjectsByCoordinates) {
      final var point = gameObject.getWorldLocation();
      if (point.getPlane() != client.getPlane()) {
        log.info(
            "Point plane {} does not match client plane {}", point.getPlane(), client.getPlane());
        continue;
      }

      drawTile(graphics, point, outlineColor, areaColor, shoalAreaSize, stroke);
    }

    return null;
  }

  private void drawTile(
      Graphics2D graphics,
      WorldPoint point,
      Color outlineColor,
      Color areaColor,
      int shoalAreaSize,
      Stroke borderStroke) {

    LocalPoint lp = LocalPoint.fromWorld(client, point);
    if (lp == null) {
      return;
    }

    final var playerLp = client.getLocalPlayer().getLocalLocation();

    if (playerLp == null) {
      log.info("Player LP is null");
      return;
    }

    //    if (((int) Math.hypot(lp.getX() - playerLp.getX(), lp.getY() - playerLp.getY())) >
    // config.shoalOutlineMaxDistance()) {
    //      log.info("Distance is {}", ((int) Math.hypot(lp.getX() - playerLp.getX(), lp.getY() -
    // playerLp.getY())));
    //      log.info("Distance is using method {}", playerLp.distanceTo(lp));
    //      return;
    //    }

    final var debugOffset = config.debugTileCenterOffsetDirection();

    if (debugOffset > 0) {
      final var mod = debugOffset % 4;

      if (mod == 0) {
        lp = lp.dx(1).dy(1);
      } else if (mod == 1) {
        lp = lp.dx(1).dy(-1);
      } else if (mod == 2) {
        lp = lp.dx(-1).dy(1);
      } else {
        lp = lp.dx(-1).dy(-1);
      }
    }

    Polygon poly = Perspective.getCanvasTileAreaPoly(client, lp, shoalAreaSize);
    if (poly != null) {
      OverlayUtil.renderPolygon(graphics, poly, outlineColor, areaColor, borderStroke);
    }
  }

  //  private void drawTileSmall(Graphics2D graphics, WorldPoint point, Color color, Stroke
  // borderStroke) {
  //
  //    LocalPoint lp = LocalPoint.fromWorld(client, point);
  //    if (lp == null) {
  //      return;
  //    }
  //
  //    Polygon poly = Perspective.getCanvasTilePoly(client, lp);
  //    if (poly != null) {
  //      OverlayUtil.renderPolygon(graphics, poly, color, new Color(0, 0, 0, 50), borderStroke);
  //    }
  //  }
}
