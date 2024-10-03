package com.vasmatheus.easymixology;

import com.vasmatheus.easymixology.model.enums.PotionSelectionStrategy;
import net.runelite.client.config.*;

import java.awt.*;

@ConfigGroup(EasyMixologyConfig.GROUP)
public interface EasyMixologyConfig extends Config {
    String GROUP = "easymixology";

    @ConfigSection(
            name = "Options",
            description = "Configuration for plugin behavior",
            position = 0
    )
    String optionsSection = "optionsSection";

    @ConfigSection(
            name = "Highlight colors",
            description = "Colors for highlighting various mixology objects",
            position = 1
    )
    String highlightColorSection = "highlightColorSection";


    @ConfigSection(
            name = "Outline and text configuration",
            description = "Configuration for highlights and texts",
            position = 2
    )
    String outlineAndTextConfiguration = "outlineAndTextConfiguration";

    @ConfigSection(
            name = "Target rewards",
            description = "Target rewards toggles section",
            position = 2
    )
    String targetRewardsSection = "targetRewardsSection";

    @ConfigItem(
            position = 0,
            keyName = "potionSelectionStrategy",
            name = "Potion strategy",
            description = "Which strategy should the plugin use to select the target potion to make. For Prefer Retort, XP is used as a " +
                    "fallback value to select the best retort option, or the next best option if no retort is available",
            section = optionsSection,
            hidden = true
    )
    default PotionSelectionStrategy potionSelectionStrategy()
    {
        return PotionSelectionStrategy.HIGHEST_XP;
    }

    @ConfigItem(
            position = 1,
            keyName = "isOverlayEnabled",
            name = "Infobox enabled",
            description = "Toggles to display the minigame infobox or not",
            section = optionsSection
    )
    default boolean isOverlayEnabled()
    {
        return true;
    }

    @ConfigItem(
            position = 2,
            keyName = "isLeverHighlightEnabled",
            name = "Lever highlight",
            description = "Toggles to highlight the lever or not",
            section = optionsSection
    )
    default boolean isLeverHighlightEnabled()
    {
        return true;
    }

    @ConfigItem(
            position = 3,
            keyName = "isVesselHighlightEnabled",
            name = "Vessel highlight",
            description = "Toggles to highlight the vessel or not",
            section = optionsSection
    )
    default boolean isVesselHighlightEnabled()
    {
        return true;
    }

    @ConfigItem(
            position = 4,
            keyName = "isStationHighlightEnabled",
            name = "Refinery highlight",
            description = "Toggles to highlight the refinery stations or not. Speedup highlight has a different toggle!",
            section = optionsSection
    )
    default boolean isStationHighlightEnabled()
    {
        return true;
    }

    @ConfigItem(
            position = 5,
            keyName = "isStationSpeedupHighlightEnabled",
            name = "Refinery speedup highlight",
            description = "Toggles to highlight the refinery stations or not when speedup is possible",
            section = optionsSection
    )
    default boolean isStationSpeedupHighlightEnabled()
    {
        return true;
    }

    @ConfigItem(
            position = 6,
            keyName = "isStationSpeedupInfoboxHighlightEnabled",
            name = "Refinery speedup infobox highlight",
            description = "Toggles to highlight the infobox or not when refinery speedup is possible",
            section = optionsSection
    )
    default boolean isStationSpeedupInfoboxHighlightEnabled()
    {
        return true;
    }

    @ConfigItem(
            position = 7,
            keyName = "isConveyorBeltHighlightEnabled",
            name = "Conveyor belt highlight",
            description = "Toggles to highlight the conveyor belts or not",
            section = optionsSection
    )
    default boolean isConveyorBeltHighlightEnabled()
    {
        return true;
    }

    @ConfigItem(
            position = 8,
            keyName = "isEmptyHopperHighlightEnabled",
            name = "Low hopper highlight",
            description = "Toggles to highlight the low level hopper or not",
            section = optionsSection
    )
    default boolean isEmptyHopperHighlightEnabled()
    {
        return true;
    }

    @ConfigItem(
            position = 9,
            keyName = "isLeverPullCountTextEnabled",
            name = "Lever pull text",
            description = "Toggles to display the amount of pulls required on a lever or not",
            section = optionsSection
    )
    default boolean isLeverPullCountTextEnabled()
    {
        return true;
    }

    @ConfigItem(
            position = 10,
            keyName = "isDigweedHighlightEnabled",
            name = "Digweed highlight",
            description = "Toggles to highlight the mature digweed herb",
            section = optionsSection
    )
    default boolean isDigweedHighlightEnabled()
    {
        return true;
    }

    @ConfigItem(
            position = 11,
            keyName = "isDigweedInfoboxHighlightEnabled",
            name = "Digweed infobox highlight",
            description = "Toggles to highlight the infobox when mature digweed herb spawns",
            section = optionsSection
    )
    default boolean isDigweedInfoboxHighlightEnabled()
    {
        return true;
    }

    @ConfigItem(
            position = 12,
            keyName = "isStationProcessCountEnabled",
            name = "Display station process count",
            description = "Toggles to display the number of actions left at a refinery station",
            section = optionsSection
    )
    default boolean isStationProcessCountEnabled()
    {
        return true;
    }


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
        return new Color(255, 255, 0, 90);
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

    @Alpha
    @ConfigItem(
            position = 7,
            keyName = "hopperDepositNeededOutline",
            name = "Hopper deposit outline",
            description = "Color to use to outline the hopper when it's contents are insufficient for the mixed potion",
            section = highlightColorSection
    )
    default Color hopperDepositNeededOutline() {
        return Color.RED;
    }

    @Alpha
    @ConfigItem(
            position = 8,
            keyName = "refinerySpeedupOutline",
            name = "Refinery speedup outline",
            description = "Color to use to outline the agitator or alembic station when speedup action is possible",
            section = highlightColorSection
    )
    default Color refinerySpeedupOutline() {
        return Color.CYAN;
    }

    @Alpha
    @ConfigItem(
            position = 9,
            keyName = "refinerySpeedupInfoboxHighlight",
            name = "Refinery speedup infobox highlight",
            description = "Color to use to highlight the infobox when speedup action is possible",
            section = highlightColorSection
    )
    default Color refinerySpeedupInfoboxHighlight() {
        return new Color(20, 131, 137, 108);
    }

    @Alpha
    @ConfigItem(
            position = 10,
            keyName = "digweedOutline",
            name = "Digweed outline",
            description = "Color to use to outline the the mature digweed",
            section = highlightColorSection
    )
    default Color digweedOutline() {
        return new Color(50, 205, 50);
    }

    @Alpha
    @ConfigItem(
            position = 11,
            keyName = "digweedInfoboxHighlight",
            name = "Digweed infobox highlight",
            description = "Color to use to highlight the infobox when mature digweed spawns",
            section = highlightColorSection
    )
    default Color digweedInfoboxHighlight() {
        return new Color(32, 125, 32, 108);
    }

    @Alpha
    @ConfigItem(
            position = 12,
            keyName = "moxLeverPreOutline",
            name = "Mox lever pre outline",
            description = "Color to use to pre outline the Mox lever",
            section = highlightColorSection
    )
    default Color moxLeverPreOutline() {
        return new Color(0, 0, 255, 100);
    }

    @Alpha
    @ConfigItem(
            position = 13,
            keyName = "agaLeverPreOutline",
            name = "Aga lever pre outline",
            description = "Color to use to pre outline the Aga lever",
            section = highlightColorSection
    )
    default Color agaLeverPreOutline() {
        return new Color(0, 255, 0, 100);
    }

    @Alpha
    @ConfigItem(
            position = 14,
            keyName = "lyeLeverPreOutline",
            name = "Lye lever pre outline",
            description = "Color to use to pre outline the Lye lever",
            section = highlightColorSection
    )
    default Color lyeLeverPreOutline() {
        return new Color(255, 0, 0, 100);
    }

    @Alpha
    @ConfigItem(
            position = 15,
            keyName = "conveyorBeltPreOutline",
            name = "Conveyor belt pre outline",
            description = "Color to use to pre outline the conveyor belts",
            section = highlightColorSection
    )
    default Color conveyorBeltPreOutline() {
        return new Color(255, 255, 0, 90);
    }

    @ConfigItem(
            keyName = "outlineFeather",
            name = "Outline feather",
            description = "Specify between 0-4 how much of the model outline should be faded",
            section = outlineAndTextConfiguration
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
            keyName = "borderWidth",
            name = "Border width",
            description = "Width of the object outline border",
            section = outlineAndTextConfiguration
    )
    @Range()
    default int borderWidth()
    {
        return 2;
    }

    @ConfigItem(
            keyName = "stationTextOffset",
            name = "Station text offset",
            description = "Offset value on the primary axis for refinement station text",
            section = outlineAndTextConfiguration
    )
    @Range(min = 0, max = 300)
    default int stationTextOffset()
    {
        return 100;
    }

    @ConfigItem(
            keyName = "textSize",
            name = "Text size",
            description = "Text size increase for various drawn texts (excluding the overlay)",
            section = outlineAndTextConfiguration
    )
    @Range(min = 0, max = 20)
    default int textSize()
    {
        return 6;
    }

    @ConfigItem(
            keyName = "prescriptionGoggles",
            name = "Prescription goggles",
            description = "Include Prescription goggles",
            section = targetRewardsSection
    )
    default boolean prescriptionGoggles()
    {
        return false;
    }

    @ConfigItem(
            keyName = "alchemistLabcoat",
            name = "Alchemist labcoat",
            description = "Include Alchemist labcoat",
            section = targetRewardsSection
    )
    default boolean alchemistLabcoat()
    {
        return false;
    }

    @ConfigItem(
            keyName = "alchemistPants",
            name = "Alchemist pants",
            description = "Include Alchemist pants",
            section = targetRewardsSection
    )
    default boolean alchemistPants()
    {
        return false;
    }

    @ConfigItem(
            keyName = "alchemistGloves",
            name = "Alchemist gloves",
            description = "Include Alchemist gloves",
            section = targetRewardsSection
    )
    default boolean alchemistGloves()
    {
        return false;
    }

    @ConfigItem(
            keyName = "Reagent pouch",
            name = "Reagent pouch",
            description = "Include Reagent pouch",
            section = targetRewardsSection
    )
    default boolean reagentPouch()
    {
        return false;
    }

    @ConfigItem(
            keyName = "potionStorage",
            name = "Potion storage",
            description = "Include Potion storage",
            section = targetRewardsSection
    )
    default boolean potionStorage()
    {
        return false;
    }

    @ConfigItem(
            keyName = "chuggingBarrel",
            name = "Chugging barrel",
            description = "Include Chugging barrel",
            section = targetRewardsSection
    )
    default boolean chuggingBarrel()
    {
        return false;
    }

    @ConfigItem(
            keyName = "alchemistsAmulet",
            name = "Alchemist's amulet",
            description = "Include Alchemist's amulet",
            section = targetRewardsSection
    )
    default boolean alchemistsAmulet()
    {
        return false;
    }
}
