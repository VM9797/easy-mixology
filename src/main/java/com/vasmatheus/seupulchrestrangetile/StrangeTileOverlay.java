package com.vasmatheus.seupulchrestrangetile;

import java.awt.*;
import java.util.*;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.GraphicsObject;
import net.runelite.api.GroundObject;
import net.runelite.api.coords.LocalPoint;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayUtil;
import net.runelite.client.ui.overlay.outline.ModelOutlineRenderer;

@Slf4j
public class StrangeTileOverlay extends Overlay {
  private static final int NEW_ACTIVE_TELEPORTER_TTL = 6;
  private static final int FADING_TELEPORTER_THRESHOLD = 2;
  private static final int DYING_TELEPORTER_THRESHOLD = 1;

  @Inject private Client client;

  @Inject private ModelOutlineRenderer modelOutlineRenderer;

  @Inject private SepulchreStrangeTileMarkerConfig config;

  private final Set<GroundObject> strangeTiles = new HashSet<>();
  private final Map<GroundObject, Integer> teleportTickTimeToLiveMap = new HashMap<>();

  public void addStrangeTile(GroundObject groundObject) {
    strangeTiles.add(groundObject);
    teleportTickTimeToLiveMap.put(groundObject, 0);
  }

  public void removeStrangeTile(GroundObject groundObject) {
    strangeTiles.remove(groundObject);
    teleportTickTimeToLiveMap.remove(groundObject);
  }

  public void onTick() {
    for (GroundObject groundObject : teleportTickTimeToLiveMap.keySet()) {
      teleportTickTimeToLiveMap.compute(
          groundObject,
          (k, currentValue) ->
              currentValue == null || currentValue <= 0
                  ? 0
                  : teleportTickTimeToLiveMap.get(groundObject) - 1);
    }
  }

  @Override
  public Dimension render(Graphics2D graphics2D) {
    var activeColor = config.strangeTileActiveOutline();
    var fadingColor = config.strangeTileFadingOutline();
    var dyingColor = config.strangeTileDyingOutline();
    var renderDistance = config.strangeTileOutlineDistance();

    for (GraphicsObject graphicsObject : client.getGraphicsObjects()) {
      var strangeTileAtGraphicsObject = getStrangeTileAtLocation(graphicsObject.getLocation());

      if (strangeTileAtGraphicsObject.isPresent()
          && (graphicsObject.getId() == 1799 || graphicsObject.getId() == 1815)) {
        Color targetColor = activeColor;
        var strangeTile = strangeTileAtGraphicsObject.get();
        boolean isDying = false;

        if (teleportTickTimeToLiveMap.containsKey(strangeTile)) {
          var ttl = teleportTickTimeToLiveMap.get(strangeTile);

          if (ttl == null || ttl == 0) {
            teleportTickTimeToLiveMap.put(strangeTile, NEW_ACTIVE_TELEPORTER_TTL);
          }
          if (ttl <= FADING_TELEPORTER_THRESHOLD) {
            targetColor = fadingColor;
          }
          if (ttl <= DYING_TELEPORTER_THRESHOLD) {
            targetColor = dyingColor;
            isDying = true;
          }

          if (distanceFromPlayer(strangeTile) > renderDistance || isDying) {
            continue;
          }
          modelOutlineRenderer.drawOutline(strangeTile, 2, targetColor, 2);
        }

        //                    log.info("GRAPHICS OBJECT ID: " + graphicsObject.getId());
        //                    if (graphicsObject.getAnimation() != null) {
        //                        log.info("GRAPHICS OBJECT ANIMATION ID: " +
        // graphicsObject.getAnimation().getId());
        //                        log.info("GRAPHICS OBJECT ANIMATION DURATION: " +
        // graphicsObject.getAnimation().getDuration());
        //                        log.info("GRAPHICS OBJECT ANIMATION FRAME STEP: " +
        // graphicsObject.getAnimation().getFrameStep());
        //                        log.info("GRAPHICS OBJECT ANIMATION NUM FRAMES: " +
        // graphicsObject.getAnimation().getNumFrames());
        //                    }
      }
    }

    return null;
  }

  private Optional<GroundObject> getStrangeTileAtLocation(LocalPoint location) {
    return strangeTiles.stream()
        .filter(it -> it.getLocalLocation().equals(location))
        .min((a, b) -> Integer.compare(distanceFromPlayer(a), distanceFromPlayer(b)));
  }

  private int distanceFromPlayer(GroundObject object) {
    return object.getWorldLocation().distanceTo(client.getLocalPlayer().getWorldLocation());
  }

  private void renderTile(
      final Graphics2D graphics, Polygon polygon, final Color color, final double borderWidth) {
    if (polygon == null) {
      return;
    }

    OverlayUtil.renderPolygon(graphics, polygon, color, new BasicStroke((float) borderWidth));
  }
}
