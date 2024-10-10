package com.vasmatheus.easymixology;

import com.vasmatheus.easymixology.model.enums.PotionSelectionStrategy;
import net.runelite.client.config.*;

import java.awt.*;

@ConfigGroup(EasyMixologyConfig.GROUP)
public interface EasyMixologyConfig extends Config {
    String GROUP = "easymixology";
    int MULTI_REWARD_UPPER_LIMIT = 10000;

    @ConfigSection(
            name = "General settings",
            description = "General behavior settings",
            position = 0
    )
    String generalSection = "generalSection";

    @ConfigItem(
            position = 0,
            keyName = "potionSelectionStrategyV2",
            name = "Potion strategy",
            description = "Strategy to use to filter potions from order",
            section = generalSection
    )
    default PotionSelectionStrategy potionSelectionStrategy() {
        return PotionSelectionStrategy.NO_AGA_TRIPLES_UNLESS_MIXALOT;
    }

    @ConfigItem(
            position = 1,
            keyName = "isEmptyHopperOutlineEnabled",
            name = "Low hopper outline",
            description = "Toggles to outline hopper when contents are low (<100)",
            section = generalSection
    )
    default boolean isEmptyHopperOutlineEnabled() {
        return true;
    }

    @Alpha
    @ConfigItem(
            position = 2,
            keyName = "hopperDepositNeededOutline",
            name = "Low hopper outline color",
            description = "Color to use to outline the hopper when it's contents are low",
            section = generalSection
    )
    default Color hopperDepositNeededOutline() {
        return new Color(255, 0, 0);
    }


    @ConfigSection(
            name = "Reward targets",
            description = "Configure rewards targets for goal tracking",
            position = 1
    )
    String targetRewardsSection = "targetRewardsSection";

    @ConfigItem(
            keyName = "prescriptionGoggles",
            name = "Prescription goggles",
            description = "Include Prescription goggles in target rewards",
            section = targetRewardsSection,
            position = 0
    )
    default boolean prescriptionGoggles() {
        return false;
    }

    @ConfigItem(
            keyName = "alchemistLabcoat",
            name = "Alchemist labcoat",
            description = "Include Alchemist labcoat in target rewards",
            section = targetRewardsSection,
            position = 1
    )
    default boolean alchemistLabcoat() {
        return false;
    }

    @ConfigItem(
            keyName = "alchemistPants",
            name = "Alchemist pants",
            description = "Include Alchemist pants in target rewards",
            section = targetRewardsSection,
            position = 2
    )
    default boolean alchemistPants() {
        return false;
    }

    @ConfigItem(
            keyName = "alchemistGloves",
            name = "Alchemist gloves",
            description = "Include Alchemist gloves in target rewards",
            section = targetRewardsSection,
            position = 3
    )
    default boolean alchemistGloves() {
        return false;
    }

    @ConfigItem(
            keyName = "Reagent pouch",
            name = "Reagent pouch",
            description = "Include Reagent pouch in target rewards",
            section = targetRewardsSection,
            position = 4
    )
    default boolean reagentPouch() {
        return false;
    }

    @ConfigItem(
            keyName = "potionStorage",
            name = "Potion storage",
            description = "Include Potion storage in target rewards",
            section = targetRewardsSection,
            position = 5
    )
    default boolean potionStorage() {
        return false;
    }

    @ConfigItem(
            keyName = "chuggingBarrel",
            name = "Chugging barrel",
            description = "Include Chugging barrel in target rewards",
            section = targetRewardsSection,
            position = 6
    )
    default boolean chuggingBarrel() {
        return false;
    }

    @ConfigItem(
            keyName = "alchemistsAmulet",
            name = "Alchemist's amulet",
            description = "Include Alchemist's amulet in target rewards",
            section = targetRewardsSection,
            position = 7
    )
    default boolean alchemistsAmulet() {
        return false;
    }

    @ConfigItem(
            keyName = "apprenticePotionPackCount",
            name = "Apprentice potion pack",
            description = "Number of apprentice potion packs to include in target rewards",
            section = targetRewardsSection,
            position = 8
    )
    @Range(max = EasyMixologyConfig.MULTI_REWARD_UPPER_LIMIT)
    default int apprenticePotionPackCount() {
        return 0;
    }

    @ConfigItem(
            keyName = "adeptPotionPackCount",
            name = "Adept potion pack",
            description = "Number of adept potion packs to include in target rewards",
            section = targetRewardsSection,
            position = 9
    )
    @Range(max = EasyMixologyConfig.MULTI_REWARD_UPPER_LIMIT)
    default int adeptPotionPackCount() {
        return 0;
    }

    @ConfigItem(
            keyName = "expertPotionPackCount",
            name = "Expert potion pack",
            description = "Number of expert potion packs to include in target rewards",
            section = targetRewardsSection,
            position = 10
    )
    @Range(max = EasyMixologyConfig.MULTI_REWARD_UPPER_LIMIT)
    default int expertPotionPackCount() {
        return 0;
    }

    @ConfigItem(
            keyName = "aldariumCount",
            name = "Aldarium",
            description = "Number of aldariums to include in target rewards",
            section = targetRewardsSection,
            position = 11
    )
    @Range(max = EasyMixologyConfig.MULTI_REWARD_UPPER_LIMIT)
    default int aldariumCount() {
        return 0;
    }


    @ConfigSection(
            name = "Overlay",
            description = "Overlay configuration",
            position = 2
    )
    String overlaySection = "overlaySection";

    @ConfigItem(
            position = 0,
            keyName = "isOverlayEnabled",
            name = "Overlay enabled",
            description = "Display an informative overlay containing a multitude of information related to the minigame",
            section = overlaySection
    )
    default boolean isOverlayEnabled() {
        return true;
    }

    @ConfigItem(
            position = 1,
            keyName = "isRefiningOverlayHighlightEnabled",
            name = "Refining highlight",
            description = "Enables the highlighting of the overlay while refinining",
            section = overlaySection
    )
    default boolean isRefiningOverlayHighlightEnabled() {
        return true;
    }

    @Alpha
    @ConfigItem(
            position = 2,
            keyName = "overlayRefiningHighlight",
            name = "Refining highlight color",
            description = "Color to use to highlight the overlay when refining is in progress",
            section = overlaySection
    )
    default Color overlayRefiningHighlight() {
        return new Color(219, 152, 4, 55);
    }

    @ConfigItem(
            position = 3,
            keyName = "isStationSpeedupOverlayHighlightEnabled",
            name = "Speedup highlight",
            description = "Toggles to highlight the overlay or not when refinery speedup is possible",
            section = overlaySection
    )
    default boolean isStationSpeedupOverlayHighlightEnabled() {
        return true;
    }

    @Alpha
    @ConfigItem(
            position = 4,
            keyName = "refinerySpeedupOverlayHighlight",
            name = "Speedup highlight color",
            description = "Color to use to highlight the overlay when speedup action is possible",
            section = overlaySection
    )
    default Color refinerySpeedupOverlayHighlight() {
        return new Color(20, 131, 137, 108);
    }

    @ConfigItem(
            position = 5,
            keyName = "isDigweedOverlayHighlightEnabled",
            name = "Digweed highlight",
            description = "Toggles to highlight the overlay when mature digweed herb spawns",
            section = overlaySection
    )
    default boolean isDigweedOverlayHighlightEnabled() {
        return true;
    }

    @Alpha
    @ConfigItem(
            position = 6,
            keyName = "digweedOverlayHighlight",
            name = "Digweed highlight color",
            description = "Color to use to highlight the overlay when mature digweed spawns",
            section = overlaySection
    )
    default Color digweedOverlayHighlight() {
        return new Color(32, 125, 32, 108);
    }

    @ConfigItem(
            position = 7,
            keyName = "shouldDisplayPlayerPoints",
            name = "Player points",
            description = "Display player points",
            section = overlaySection
    )
    default boolean shouldDisplayPlayerPoints() {
        return true;
    }

    @ConfigItem(
            position = 8,
            keyName = "shouldDisplayTarget",
            name = "Display target",
            description = "Display reward targets",
            section = overlaySection
    )
    default boolean shouldDisplayTarget() {
        return true;
    }


    @ConfigItem(
            position = 9,
            keyName = "shouldDisplayRewardPercentage",
            name = "Progress percentage",
            description = "Display reward progress percentages",
            section = overlaySection
    )
    default boolean shouldDisplayRewardPercentage() {
        return true;
    }

    @ConfigItem(
            position = 10,
            keyName = "shouldDisplaySessionPoints",
            name = "Display session points",
            description = "Display session reward points",
            section = overlaySection
    )
    default boolean shouldDisplaySessionPoints() {
        return true;
    }

    @ConfigItem(
            position = 11,
            keyName = "shouldDisplayOrderInfo",
            name = "Display order info",
            description = "Display order related info in overlay",
            section = overlaySection
    )
    default boolean shouldDisplayOrderInfo() {
        return true;
    }


    @ConfigSection(
            name = "Digweed",
            description = "Digweed outlining configuration",
            position = 3
    )
    String digweedSection = "digweedSection";

    @ConfigItem(
            position = 0,
            keyName = "isDigweedOutlineEnabled",
            name = "Outline",
            description = "Toggles to outline the mature digweed herb",
            section = digweedSection
    )
    default boolean isDigweedOutlineEnabled() {
        return true;
    }

    @Alpha
    @ConfigItem(
            position = 1,
            keyName = "digweedOutline",
            name = "Color",
            description = "Color to use to outline the the mature digweed",
            section = digweedSection
    )
    default Color digweedOutline() {
        return new Color(50, 205, 50);
    }


    @ConfigSection(
            name = "Levers",
            description = "Lever outlining configuration",
            position = 4
    )
    String leverSection = "leverSection";

    @ConfigItem(
            position = 0,
            keyName = "isLeverOutlineEnabled",
            name = "Outline",
            description = "Toggles to outline the lever or not",
            section = leverSection
    )
    default boolean isLeverOutlineEnabled() {
        return true;
    }

    @ConfigItem(
            position = 1,
            keyName = "isLeverPullCountTextEnabled",
            name = "Pull text",
            description = "Toggles to display the amount of pulls required on a lever or not",
            section = leverSection
    )
    default boolean isLeverPullCountTextEnabled() {
        return true;
    }

    @Alpha
    @ConfigItem(
            position = 2,
            keyName = "moxLeverOutline",
            name = "Mox color",
            description = "Color to use to outline the Mox lever",
            section = leverSection
    )
    default Color moxLeverOutline() {
        return new Color(98, 98, 255);
    }

    @Alpha
    @ConfigItem(
            position = 3,
            keyName = "agaLeverOutline",
            name = "Aga color",
            description = "Color to use to outline the Aga lever",
            section = leverSection
    )
    default Color agaLeverOutline() {
        return new Color(77, 255, 71);
    }

    @Alpha
    @ConfigItem(
            position = 4,
            keyName = "lyeLeverOutline",
            name = "Lye color",
            description = "Color to use to outline the Lye lever",
            section = leverSection
    )
    default Color lyeLeverOutline() {
        return new Color(255, 72, 68);
    }

    @ConfigItem(
            position = 5,
            keyName = "isLeverPreOutlineEnabled",
            name = "Pre-outline",
            description = "Toggles to pre-outline the levers when taking potions from the vessel",
            section = leverSection
    )
    default boolean isLeverPreOutlineEnabled() {
        return true;
    }


    @Alpha
    @ConfigItem(
            position = 6,
            keyName = "moxLeverPreOutline",
            name = "Mox pre-outline",
            description = "Color to use to pre outline the Mox lever",
            section = leverSection
    )
    default Color moxLeverPreOutline() {
        return new Color(98, 98, 255, 135);
    }

    @Alpha
    @ConfigItem(
            position = 7,
            keyName = "agaLeverPreOutline",
            name = "Aga pre-outline",
            description = "Color to use to pre outline the Aga lever",
            section = leverSection
    )
    default Color agaLeverPreOutline() {
        return new Color(77, 255, 71, 135);
    }

    @Alpha
    @ConfigItem(
            position = 8,
            keyName = "lyeLeverPreOutline",
            name = "Lye pre-outline",
            description = "Color to use to pre outline the Lye lever",
            section = leverSection
    )
    default Color lyeLeverPreOutline() {
        return new Color(255, 72, 68, 135);
    }


    @ConfigSection(
            name = "Vessel",
            description = "Vessel outlining configuration",
            position = 5
    )
    String vesselSection = "vesselSection";

    @ConfigItem(
            position = 0,
            keyName = "isVesselOutlineEnabled",
            name = "Outline",
            description = "Toggles to outline the vessel or not",
            section = vesselSection
    )
    default boolean isVesselOutlineEnabled() {
        return true;
    }

    @Alpha
    @ConfigItem(
            position = 1,
            keyName = "vesselOutline",
            name = "Color",
            description = "Color to use to outline the vessel",
            section = vesselSection
    )
    default Color vesselOutline() {
        return new Color(255, 255, 0);
    }


    @ConfigSection(
            name = "Refinery",
            description = "Refinery outlining configuration",
            position = 6
    )
    String refinerySection = "refinerySection";


    @ConfigItem(
            position = 0,
            keyName = "isRefineryOutlineEnabled",
            name = "Outline",
            description = "Toggles to outline the refinery stations or not. Speedup outline has a different toggle!",
            section = refinerySection
    )
    default boolean isRefineryOutlineEnabled() {
        return true;
    }

    @ConfigItem(
            position = 1,
            keyName = "isRefineryProcessCountEnabled",
            name = "Display station process count",
            description = "Toggles to display the number of actions left at a refinery station",
            section = refinerySection
    )
    default boolean isRefineryProcessCountEnabled() {
        return true;
    }

    @Alpha
    @ConfigItem(
            position = 2,
            keyName = "refineryOutline",
            name = "Refinery outline",
            description = "Color to use to outline the refinery station",
            section = refinerySection
    )
    default Color refineryOutline() {
        return new Color(255, 255, 0);
    }

    @ConfigItem(
            position = 3,
            keyName = "isRefineryPreOutlineEnabled",
            name = "Pre-outline",
            description = "Toggles to pre outline the refinery stations or not",
            section = refinerySection
    )
    default boolean isRefineryPreOutlineEnabled() {
        return true;
    }

    @Alpha
    @ConfigItem(
            position = 4,
            keyName = "refineryPreOutline",
            name = "Pre-outline color",
            description = "Color to use to outline the refinery station when picking up the potion to pre-indicate which station to go to",
            section = refinerySection
    )
    default Color refineryPreOutline() {
        return new Color(255, 255, 0, 65);
    }

    @ConfigItem(
            position = 5,
            keyName = "isRefinerySpeedupOutlineEnabled",
            name = "Refinery speedup outline",
            description = "Toggles to outline the refinery stations or not when speedup is possible",
            section = refinerySection
    )
    default boolean isRefinerySpeedupOutlineEnabled() {
        return true;
    }

    @Alpha
    @ConfigItem(
            position = 6,
            keyName = "refinerySpeedupOutline",
            name = "Refinery speedup outline",
            description = "Color to use to outline the agitator or alembic station when speedup action is possible",
            section = refinerySection
    )
    default Color refinerySpeedupOutline() {
        return new Color(0, 255, 255);
    }


    @ConfigSection(
            name = "Deposit",
            description = "Deposit outlining configuration",
            position = 7
    )
    String depositSection = "depositSection";

    @ConfigItem(
            position = 1,
            keyName = "isLeftClickSwapForAgitatorWhenDepositEnabled",
            name = "Walk through agitator",
            description = "Switch the left click option on agitator to walk through it when ready to deposit the order (useful when " +
                    "facing north)",
            section = depositSection
    )
    default boolean isLeftClickSwapForAgitatorWhenDepositEnabled() {
        return true;
    }

    @ConfigItem(
            position = 2,
            keyName = "isConveyorBeltOutlineEnabled",
            name = "Outline",
            description = "Toggles to outline the conveyor belts or not",
            section = depositSection
    )
    default boolean isConveyorBeltOutlineEnabled() {
        return true;
    }


    @Alpha
    @ConfigItem(
            position = 3,
            keyName = "conveyorBeltOutline",
            name = "Outline color",
            description = "Color to use to outline the conveyor belts",
            section = depositSection
    )
    default Color conveyorBeltOutline() {
        return new Color(255, 255, 0);
    }

    @ConfigItem(
            position = 4,
            keyName = "isConveyorBeltPreOutlineEnabled",
            name = "Pre-outline",
            description = "Toggles to pre outline the conveyor belts or not",
            section = depositSection
    )
    default boolean isConveyorBeltPreOutlineEnabled() {
        return true;
    }

    @Alpha
    @ConfigItem(
            position = 5,
            keyName = "conveyorBeltPreOutline",
            name = "Pre-outline color",
            description = "Color to use to pre outline the conveyor belts",
            section = depositSection
    )
    default Color conveyorBeltPreOutline() {
        return new Color(255, 255, 0, 65);
    }


    @ConfigSection(
            name = "Graphics",
            description = "Text and outlining graphics settings",
            position = 8
    )
    String graphicsSection = "graphicsSection";


    @ConfigItem(
            keyName = "outlineFeather",
            name = "Outline feather",
            description = "Specify between 0-4 how much of the model outline should be faded",
            section = graphicsSection,
            position = 1
    )
    @Range(
            min = 0,
            max = 4
    )
    default int outlineFeather() {
        return 0;
    }

    @ConfigItem(
            keyName = "borderWidth",
            name = "Border width",
            description = "Width of the object outline border",
            section = graphicsSection,
            position = 2
    )
    @Range()
    default int borderWidth() {
        return 2;
    }

    @ConfigItem(
            keyName = "stationTextOffset",
            name = "Station text offset",
            description = "Offset value on the primary axis for refinement station text",
            section = graphicsSection,
            position = 3
    )
    @Range(min = 0, max = 300)
    default int stationTextOffset() {
        return 100;
    }

    @ConfigItem(
            keyName = "textSize",
            name = "Text size",
            description = "Text size increase for various drawn texts (excluding the overlay)",
            section = graphicsSection,
            position = 4
    )
    @Range(min = 0, max = 20)
    default int textSize() {
        return 6;
    }

    @ConfigItem(
            keyName = "boldText",
            name = "Bold text",
            description = "Use bold text for various drawn texts (excluding overlay)",
            section = graphicsSection,
            position = 5
    )
    default boolean boldText() {
        return false;
    }
}
