package com.vasmatheus.mokhaiotlhelper;

import com.google.inject.Provides;
import com.vasmatheus.mokhaiotlhelper.grubhider.DelveGrubHiderService;
import com.vasmatheus.mokhaiotlhelper.volatilepath.VolatileEarthPathService;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

@Slf4j
@PluginDescriptor(
    name = "Doom of Mokhaiotl",
    description = "Plugin providing numerous utilities for the delve boss",
    tags = {"doom", "mokhaiotl", "delve", "enrage"})
public class MokhaiotlHelperPlugin extends Plugin {

  @Inject private DelveGrubHiderService delveGrubHiderService;
  @Inject private VolatileEarthPathService volatileEarthPathService;
  @Inject private MokhaiotlHelperConfig config;
  @Inject private ClientThread clientThread;

  @Override
  protected void startUp() {
    if (config.isDelveGrubHiderModuleEnabled()) {
      delveGrubHiderService.enableService();
    }

    if (config.isVolatileEarthPathModuleEnabled()) {
      volatileEarthPathService.enableService();
    }
  }

  @Override
  protected void shutDown() {
    delveGrubHiderService.disableService();
    volatileEarthPathService.disableService();
  }

  @Subscribe
  public void onConfigChanged(ConfigChanged configChanged) {
    if (configChanged.getGroup().equals(MokhaiotlHelperConfig.GROUP)) {
      clientThread.invoke(
          () -> {
            if (delveGrubHiderService.isServiceEnabled()
                && !config.isDelveGrubHiderModuleEnabled()) {
              delveGrubHiderService.disableService();
            } else if (!delveGrubHiderService.isServiceEnabled()
                && config.isDelveGrubHiderModuleEnabled()) {
              delveGrubHiderService.enableService();
            }

            if (volatileEarthPathService.isServiceEnabled()
                && !config.isVolatileEarthPathModuleEnabled()) {
              volatileEarthPathService.disableService();
            } else if (!volatileEarthPathService.isServiceEnabled()
                && config.isVolatileEarthPathModuleEnabled()) {
              volatileEarthPathService.enableService();
            }
          });
    }
  }

  @Provides
  MokhaiotlHelperConfig provideConfig(ConfigManager configManager) {
    return configManager.getConfig(MokhaiotlHelperConfig.class);
  }
}
