package com.vasmatheus.mokhaiotlhelper;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;

@ConfigGroup(MokhaiotlHelperConfig.GROUP)
public interface MokhaiotlHelperConfig extends Config {
  String GROUP = "mokhaiotllhelper";

  @ConfigSection(name = "Modules", description = "Helper modules active", position = 1)
  String moduleSection = "moduleSection";

  @ConfigItem(
      position = 1,
      keyName = "isDelveGrubHiderModuleEnabled",
      name = "Grub hider module",
      description = "Hide grubs on XP drop",
      section = moduleSection)
  default boolean isDelveGrubHiderModuleEnabled() {
    return true;
  }

  @ConfigItem(
      position = 2,
      keyName = "isVolatileEarthPathModuleEnabled",
      name = "Volatile earth path module",
      description = "Draw volatile earth path",
      section = moduleSection)
  default boolean isVolatileEarthPathModuleEnabled() {
    return true;
  }
}
