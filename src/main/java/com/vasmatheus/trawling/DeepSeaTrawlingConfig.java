package com.vasmatheus.trawling;

import java.awt.*;
import net.runelite.client.config.Alpha;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup(DeepSeaTrawlingConfig.GROUP)
public interface DeepSeaTrawlingConfig extends Config {
  String GROUP = "vmdeepseatrawling";

  @ConfigItem(
      position = 1,
      keyName = "indexes",
      name = "Indexes to hide",
      description = "Comma separated list")
  default String indexesToHide() {
    return "";
  }

  @ConfigItem(
      position = 2,
      keyName = "clicklayerIndexes",
      name = "Clicklayer Indexes to hide",
      description = "Comma separated list")
  default String clicklayerIndexesToHide() {
    return "";
  }

  @ConfigItem(
      position = 3,
      keyName = "yOffsetPushDown",
      name = "yOffsetPushDown",
      description = "Y offset for the pushed down UI elements")
  default int yOffsetPushDown() {
    return 66;
  }

  @ConfigItem(
      position = 4,
      keyName = "yOffsetPullUp",
      name = "yOffsetPullUp",
      description = "Y offset for the pulled up UI elements")
  default int yOffsetPullUp() {
    return 102;
  }

  @ConfigItem(
      position = 5,
      keyName = "shoalOutlineColor",
      name = "Shoal color",
      description = "Color to use to highlight shoals (outline)")
  @Alpha
  default Color shoalOutlineColor() {
    return Color.green;
  }

  @ConfigItem(
      position = 6,
      keyName = "shoalAreaColor",
      name = "Shoal color",
      description = "Color to use to highlight shoals (area)")
  @Alpha
  default Color shoalAreaColor() {
    return new Color(0, 0, 0, 50);
  }

  @ConfigItem(
      position = 7,
      keyName = "shoalAreaSize",
      name = "Shoal area size",
      description = "Size of the area to highlight shoals with")
  default int shoalSize() {
    return 7;
  }

  @ConfigItem(
      position = 8,
      keyName = "shoalStrokeSize",
      name = "Shoal outline stroke size",
      description = "Stroke width to outline shoals with")
  default int shoalStrokeSize() {
    return 2;
  }

  @ConfigItem(
      position = 9,
      keyName = "debugTileCenterOffsetDirection",
      name = "Center offset modulo",
      description =
          "Offsets the center tile of a shoal to better match for visuals (using mod to check which direction to offset to,"
              + " 0 = no offset)")
  default int debugTileCenterOffsetDirection() {
    return 2;
  }

  @ConfigItem(
      position = 10,
      keyName = "shoalOutlineMaxDistance",
      name = "Shoal outline max distance",
      description = "Max distance to outline shoals")
  default int shoalOutlineMaxDistance() {
    return 30;
  }
}
