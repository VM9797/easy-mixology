package com.vasmatheus.mokhaiotlhelper.common;

import java.util.Set;
import javax.inject.Inject;
import lombok.Getter;
import net.runelite.api.Client;
import net.runelite.api.WorldView;
import net.runelite.client.eventbus.EventBus;

public abstract class DelveService {
  private static final Set<Integer> DELVE_REGION_IDS = Set.of(5269, 13668, 14180);

  @Inject private Client client;
  @Inject private EventBus eventBus;

  @Getter private boolean isServiceEnabled = false;

  public void enableService() {
    eventBus.register(this);
    isServiceEnabled = true;
    startUp();
  }

  public void disableService() {
    eventBus.unregister(this);
    isServiceEnabled = false;
    shutDown();
  }

  protected void startUp() {}

  protected void shutDown() {}

  protected boolean isInDelveRegion() {
    WorldView wv = client.getTopLevelWorldView();
    if (wv == null) {
      return false;
    }

    int[] regions = wv.getMapRegions();
    if (regions == null) {
      return false;
    }

    for (int region : regions) {
      if (DELVE_REGION_IDS.contains(region)) {
        return true;
      }
    }

    return false;
  }
}
