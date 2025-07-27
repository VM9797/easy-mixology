package com.vasmatheus.seupulchrestrangetile;

import net.runelite.client.config.*;

import java.awt.*;

@ConfigGroup(SepulchreStrangeTileMarkerConfig.GROUP)
public interface SepulchreStrangeTileMarkerConfig extends Config {
    String GROUP = "sepulchrestrangetilemarker";



    @ConfigSection(
            name = "Sepulchre",
            description = "Hallowed sepulchre config",
            position = 9
    )
    String sepulchreSection = "sepulchreSection";

    @Alpha
    @ConfigItem(
            position = 1,
            keyName = "strangeTileActiveOutline",
            name = "Active teleporter color",
            description = "Color to use to outline active blue teleporters in HS",
            section = sepulchreSection
    )
    default Color strangeTileActiveOutline() {
        return new Color(0, 237, 103, 200);
    }

    @Alpha
    @ConfigItem(
            position = 2,
            keyName = "strangeTileFadingOutline",
            name = "Fading teleporter color",
            description = "Color to use to outline fading blue teleporters in HS",
            section = sepulchreSection
    )
    default Color strangeTileFadingOutline() {
        return new Color(255, 241, 0, 200);
    }

    @Alpha
    @ConfigItem(
            position = 3,
            keyName = "strangeTileDyingOutline",
            name = "Dying teleporter color",
            description = "Color to use to outline dying blue teleporters in HS",
            section = sepulchreSection
    )
    default Color strangeTileDyingOutline() {
        return new Color(255, 115, 0, 150);
    }

    @ConfigItem(
            keyName = "strangeTileOutlineDistance",
            name = "Outline distance",
            description = "Maximum distance to outline teleporters",
            section = sepulchreSection,
            position = 4
    )
    @Range(min = 0, max = 20)
    default int strangeTileOutlineDistance() {
        return 12;
    }
}
