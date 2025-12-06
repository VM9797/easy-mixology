package com.vasmatheus.mokhaiotlhelper.volatilepath;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.Perspective;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayUtil;

@Slf4j
public class VolatileEarthPathOverlay extends Overlay {
  // TODO: Make these configurable
  private static final Color TILE_COLOR = Color.red;
  private static final Color TILE_FILL_COLOR = new Color(0, 0, 0, 50);

  @Inject private Client client;

  private List<WorldPoint> volatileEarthPath = new ArrayList<>();

  public VolatileEarthPathOverlay() {
    setPosition(OverlayPosition.DYNAMIC);
    setLayer(OverlayLayer.ABOVE_SCENE);
  }

  public void addToPath(WorldPoint point) {
    volatileEarthPath.add(point);
  }

  public void clearPath() {
    volatileEarthPath.clear();
  }

  @Override
  public Dimension render(Graphics2D graphics) {

    for (WorldPoint point : volatileEarthPath) {
      if (point.getPlane() != client.getPlane()) {
        log.info(
            "Point plane {} does not match client plane {}", point.getPlane(), client.getPlane());
        continue;
      }

      drawTile(graphics, point, TILE_COLOR, new BasicStroke((float) 2));
    }

    return null;
  }

  private void drawTile(Graphics2D graphics, WorldPoint point, Color color, Stroke borderStroke) {

    LocalPoint lp = LocalPoint.fromWorld(client, point);
    if (lp == null) {
      return;
    }

    Polygon poly = Perspective.getCanvasTilePoly(client, lp);
    if (poly != null) {
      OverlayUtil.renderPolygon(graphics, poly, color, new Color(0, 0, 0, 50), borderStroke);
    }
  }
}
