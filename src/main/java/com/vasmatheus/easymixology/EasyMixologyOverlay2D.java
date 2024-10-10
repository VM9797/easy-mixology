package com.vasmatheus.easymixology;

import com.vasmatheus.easymixology.model.MixologyStateMachine;
import com.vasmatheus.easymixology.model.MixologyStats;
import com.vasmatheus.easymixology.model.enums.MixologyState;
import com.vasmatheus.easymixology.model.enums.PotionComponent;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.ComponentConstants;
import net.runelite.client.ui.overlay.components.LineComponent;
import net.runelite.client.ui.overlay.components.TitleComponent;

import javax.inject.Inject;
import java.awt.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class EasyMixologyOverlay2D extends OverlayPanel {
    private static final int PREFERRED_WIDTH = 375;

    @Inject
    private MixologyStateMachine state;

    @Inject
    private MixologyStats stats;

    @Inject
    private EasyMixologyConfig config;

    @Inject
    private UiHelper uiHelper;

    public EasyMixologyOverlay2D() {
        super();
        setPosition(OverlayPosition.TOP_CENTER);
        setPreferredSize(new Dimension(PREFERRED_WIDTH, 0));
    }

    @Override
    public Dimension render(Graphics2D graphics) {
        if (!state.isStarted() || !config.isOverlayEnabled()) {
            return super.render(graphics);
        }

        panelComponent.setBackgroundColor(ComponentConstants.STANDARD_BACKGROUND_COLOR);

        panelComponent.getChildren().add(TitleComponent.builder()
                .text("Easy Mixology")
                .color(Color.GREEN)
                .build());

        if (config.shouldDisplayPlayerPoints()) {
            panelComponent.getChildren().add(LineComponent.builder()
                    .left("Player points")
                    .right(colorCodeString(stats.getPlayerMoxCount() == -1 ? "?" : formatInt(stats.getPlayerMoxCount()), getMoxColor()) + " " +
                            "/ " +
                            colorCodeString(stats.getPlayerAgaCount() == -1 ? "?" : formatInt(stats.getPlayerAgaCount()), getAgaColor()) +
                            " / " +
                            colorCodeString(stats.getPlayerLyeCount() == -1 ? "?" : formatInt(stats.getPlayerLyeCount()), getLyeColor()))
                    .build());
        }

        if (config.shouldDisplayTarget()) {
            panelComponent.getChildren().add(LineComponent.builder()
                    .left("Target points")
                    .right(colorCodeString(formatInt(stats.getTargetMox()), getMoxColor()) + " / " +
                            colorCodeString(formatInt(stats.getTargetAga()), getAgaColor()) + " / " +
                            colorCodeString(formatInt(stats.getTargetLye()), getLyeColor()))
                    .build());
        }

        if (config.shouldDisplayRewardPercentage()) {
            if (stats.isArePlayerCountsLoaded()) {
                panelComponent.getChildren().add(LineComponent.builder()
                        .left("Target %")
                        .right(colorCodeString(formatInt(stats.getTargetMoxPercent()), getMoxColor()) + "% / " +
                                colorCodeString(formatInt(stats.getTargetAgaPercent()), getAgaColor()) + "% / " +
                                colorCodeString(formatInt(stats.getTargetLyePercent()), getLyeColor()) + "%")
                        .build());
            }
        }

        if (config.shouldDisplaySessionPoints()) {
            panelComponent.getChildren().add(LineComponent.builder()
                    .left("Session points")
                    .right(colorCodeString(formatInt(stats.getSessionMoxCount()), getMoxColor()) + " / " +
                            colorCodeString(formatInt(stats.getSessionAgaCount()), getAgaColor()) + " / " +
                            colorCodeString(formatInt(stats.getSessionLyeCount()), getLyeColor()))
                    .build());
        }

        if (config.shouldDisplayOrderInfo()) {
            displayOrderInfo();
        }

        if (config.isRefiningOverlayHighlightEnabled() && state.getState() == MixologyState.REFINING) {
            panelComponent.setBackgroundColor(config.overlayRefiningHighlight());
        }

        if (config.isStationSpeedupOverlayHighlightEnabled() && state.getState() == MixologyState.REFINING && (uiHelper.isAgitatorSpeedupObjectPresent()) || uiHelper.isAlembicSpeedupObjectPresent()) {
            panelComponent.setBackgroundColor(config.refinerySpeedupOverlayHighlight());
        }

        if (config.isDigweedOverlayHighlightEnabled() && uiHelper.isMatureDigweedPresent()) {
            panelComponent.setBackgroundColor(config.digweedOverlayHighlight());
        }

        return super.render(graphics);
    }

    private void displayOrderInfo() {
        panelComponent.getChildren().add(TitleComponent.builder()
                .text("==========================================")
                .build());

        var processState = state.getState();

        if (processState == MixologyState.READY_TO_DEPOSIT) {
            panelComponent.getChildren().add(TitleComponent.builder()
                    .color(Color.GREEN)
                    .text("Ready to deposit!")
                    .build());
            return;
        }

        panelComponent.getChildren().add(LineComponent.builder()
                .left("Potions")
                .right(mapPotionListToString())
                .build());


        if (processState == MixologyState.MIXING || processState == MixologyState.MIX_READY) {
            panelComponent.getChildren().add(LineComponent.builder()
                    .left("Components")
                    .right(mapComponentListToString())
                    .build());
        }

        if (processState == MixologyState.REFINING || processState == MixologyState.READY_TO_REFINE) {
            panelComponent.getChildren().add(LineComponent.builder()
                    .left("Refinery")
                    .right(mapRefineryListToString())
                    .build());
        }
    }

    private String mapPotionListToString() {
        var order = state.getOrder();

        return IntStream.range(0, order.potions.size())
                .mapToObj(it -> it == state.getCurrentlyProcessingPotionIndex() ? colorCodeString(order.potions.get(it).potionName,
                        colorToHex(Color.YELLOW)) : order.potions.get(it).shortPotionName)
                .collect(Collectors.joining(" / "));
    }

    private String mapComponentListToString() {
        var order = state.getOrder();

        return order.potions.stream()
                .map(it -> it.componentList.stream().map(this::colorCodePotionComponentShort).collect(Collectors.joining("")))
                .collect(Collectors.joining(" / "));
    }

    private String mapRefineryListToString() {
        var order = state.getOrder();

        return IntStream.range(0, order.refinementTypes.size())
                .mapToObj(it -> it == state.getCurrentlyProcessingPotionIndex() ? colorCodeString(order.refinementTypes.get(it).actionName,
                        colorToHex(Color.YELLOW)) : order.refinementTypes.get(it).actionName)
                .collect(Collectors.joining(" / "));
    }

    private String colorCodePotionComponentShort(PotionComponent component) {
        String color = component == PotionComponent.AGA ? getAgaColor() : component == PotionComponent.LYE ? getLyeColor() : getMoxColor();

        return colorCodeString(component.shortName, color);
    }

    private String colorCodePotionComponent(PotionComponent component) {
        String color = component == PotionComponent.AGA ? getAgaColor() : component == PotionComponent.LYE ? getLyeColor() : getMoxColor();

        return colorCodeString(component.toString(), color);
    }

    private static String colorCodeString(String text, String colorCode) {
        return "<col=" + colorCode + ">" + text + "<col=FFFFFF>";
    }

    private String getMoxColor() {
        return colorToHex(config.moxLeverOutline());
    }

    private String getAgaColor() {
        return colorToHex(config.agaLeverOutline());
    }

    private String getLyeColor() {
        return colorToHex(config.lyeLeverOutline());
    }

    private String colorToHex(Color color) {
        return String.format("%02X%02X%02X", color.getRed(), color.getGreen(), color.getBlue());
    }

    private String formatInt(int number) {
        return String.format("%,d", number);
    }
}
