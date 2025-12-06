package com.vasmatheus.mokhaiotlhelper.volatilepath;

import com.vasmatheus.mokhaiotlhelper.common.DelveService;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Actor;
import net.runelite.api.NPC;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.GameObjectSpawned;
import net.runelite.api.events.HitsplatApplied;
import net.runelite.api.events.NpcDespawned;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.ui.overlay.OverlayManager;

@Slf4j
@Singleton
public class VolatileEarthPathService extends DelveService {
  private static final int VOLATILE_EARTH_ID = 14714;
  private static final int VOLATILE_EARTH_ORB_ID = 14715;
  private static final int BURROW_HOLE_ID = 57285;

  @Inject private OverlayManager overlayManager;
  @Inject private VolatileEarthPathOverlay volatileEarthPathOverlay;

  private WorldPoint destination = null;
  private WorldPoint source = null;

  private void drawPath() {
    if (source == null || destination == null) {
      log.error("Cannot construct path: Invalid state, source and destination NPCs are null");
      return;
    }

    WorldPoint currentPoint = source;
    volatileEarthPathOverlay.addToPath(currentPoint);

    while (currentPoint.getX() != destination.getX() || currentPoint.getY() != destination.getY()) {
      int xOffset = 0;
      int yOffset = 0;

      if (destination.getX() < currentPoint.getX()) {
        xOffset = -1;
      } else if (destination.getX() > currentPoint.getX()) {
        xOffset = 1;
      }

      if (destination.getY() < currentPoint.getY()) {
        yOffset = -1;
      } else if (destination.getY() > currentPoint.getY()) {
        yOffset = 1;
      }

      currentPoint = currentPoint.dx(xOffset).dy(yOffset);
      volatileEarthPathOverlay.addToPath(currentPoint);
    }
  }

  private void clearPath() {
    volatileEarthPathOverlay.clearPath();
    destination = null;
    source = null;
  }

  private boolean isVolatileEarth(int npcId) {
    return npcId == VOLATILE_EARTH_ID;
  }

  @Override
  protected void startUp() {
    overlayManager.add(volatileEarthPathOverlay);
  }

  @Override
  protected void shutDown() {
    super.shutDown();
    overlayManager.remove(volatileEarthPathOverlay);
    clearPath();
  }

  @Subscribe
  protected void onHitsplatApplied(HitsplatApplied event) {
    if (!isInDelveRegion()) {
      return;
    }

    Actor actor = event.getActor();
    if (actor instanceof NPC) {
      final int npcId = ((NPC) actor).getId();
      final boolean isVolatileEarth = isVolatileEarth(npcId);

      if (!isVolatileEarth) {
        return;
      }

      final NPC volatileEarth = (NPC) actor;

      if (destination == null) {
        log.info("Destination volatile earth detected");
        destination = volatileEarth.getWorldLocation();
      } else if (source == null) {
        log.info("Source volatile earth detected");
        source = volatileEarth.getWorldLocation();
        drawPath();
      } else {
        log.info("Subsequent volatile earth detected");
      }
    }
  }

  @Subscribe
  protected void onNpcDespawned(NpcDespawned event) {
    if (!isInDelveRegion()) {
      return;
    }

    if (event.getNpc().getId() == VOLATILE_EARTH_ORB_ID) {
      clearPath();
    }
  }

  @Subscribe
  void onGameObjectSpawned(GameObjectSpawned event) {
    if (!isInDelveRegion()) {
      return;
    }

    if (event.getGameObject().getId() == BURROW_HOLE_ID) {
      clearPath();
    }
  }
}
