package com.vasmatheus.easymixology;

import net.runelite.client.config.*;

import java.awt.*;

@ConfigGroup("easymixology")
public interface EasyMixologyConfig extends Config {

    @ConfigSection(
            name = "Highlight colors",
            description = "Colors for highlighting various mixology objects",
            position = 0
    )
    String highlightColorSection = "highlightColorSection";


    @ConfigSection(
            name = "Highlight color style",
            description = "Styles for highlighting colors of mixology objects",
            position = 1
    )
    String highlightStyleSection = "highlightStyleSection";

    @Alpha
    @ConfigItem(
            position = 0,
            keyName = "moxLeverOutline",
            name = "Mox lever outline",
            description = "Color to use to outline the Mox lever",
            section = highlightColorSection
    )
    default Color moxLeverOutline() {
        return Color.BLUE;
    }

    @Alpha
    @ConfigItem(
            position = 1,
            keyName = "agaLeverOutline",
            name = "Aga lever outline",
            description = "Color to use to outline the Aga lever",
            section = highlightColorSection
    )
    default Color agaLeverOutline() {
        return Color.GREEN;
    }

    @Alpha
    @ConfigItem(
            position = 2,
            keyName = "lyeLeverOutline",
            name = "Lye lever outline",
            description = "Color to use to outline the Lye lever",
            section = highlightColorSection
    )
    default Color lyeLeverOutline() {
        return Color.RED;
    }

    @Alpha
    @ConfigItem(
            position = 3,
            keyName = "vesselOutline",
            name = "Vessel outline",
            description = "Color to use to outline the vessel",
            section = highlightColorSection
    )
    default Color vesselOutline() {
        return Color.YELLOW;
    }

    @Alpha
    @ConfigItem(
            position = 4,
            keyName = "refineryOutline",
            name = "Refinery outline",
            description = "Color to use to outline the refinery station",
            section = highlightColorSection
    )
    default Color refineryOutline() {
        return Color.YELLOW;
    }

    @Alpha
    @ConfigItem(
            position = 5,
            keyName = "refineryPreOutline",
            name = "Refinery pre outline",
            description = "Color to use to outline the refinery station when picking up the potion to pre-indicate which station to go to",
            section = highlightColorSection
    )
    default Color refineryPreOutline() {
        return new Color(255, 255, 0, 120);
    }

    @Alpha
    @ConfigItem(
            position = 6,
            keyName = "conveyorBeltOutline",
            name = "Conveyor belt outline",
            description = "Color to use to outline the conveyor belts",
            section = highlightColorSection
    )
    default Color conveyorBeltOutline() {
        return Color.YELLOW;
    }

    @ConfigItem(
            position = 0,
            keyName = "outlineFeather",
            name = "Outline feather",
            description = "Specify between 0-4 how much of the model outline should be faded",
            section = highlightStyleSection
    )
    @Range(
            min = 0,
            max = 4
    )
    default int outlineFeather()
    {
        return 0;
    }

    @ConfigItem(
            position = 1,
            keyName = "borderWidth",
            name = "Border width",
            description = "Width of the object outline border",
            section = highlightStyleSection
    )
    @Range()
    default int borderWidth()
    {
        return 2;
    }
}
