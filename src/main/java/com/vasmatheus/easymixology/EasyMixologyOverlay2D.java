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

        var targetPotion = state.getTargetPotion();

        panelComponent.getChildren().add(TitleComponent.builder()
                .text("Easy Mixology")
                .color(Color.GREEN)
                .build());

        panelComponent.getChildren().add(LineComponent.builder()
                .left("Player points")
                .right(colorCodeString(stats.getPlayerMoxCount() == -1 ? "?" : String.valueOf(stats.getPlayerMoxCount()), getMoxColor()) + " " +
                        "/ " +
                        colorCodeString(stats.getPlayerAgaCount() == -1 ? "?" : String.valueOf(stats.getPlayerAgaCount()), getAgaColor()) +
                        " / " +
                        colorCodeString(stats.getPlayerLyeCount() == -1 ? "?" : String.valueOf(stats.getPlayerLyeCount()), getLyeColor()))
                .build());

        panelComponent.getChildren().add(LineComponent.builder()
                .left("Target points")
                .right(colorCodeString(String.valueOf(stats.getTargetMox()), getMoxColor()) + " / " +
                        colorCodeString(String.valueOf(stats.getTargetAga()), getAgaColor()) + " / " +
                        colorCodeString(String.valueOf(stats.getTargetLye()), getLyeColor()))
                .build());

        if (stats.isArePlayerCountsLoaded()) {
            panelComponent.getChildren().add(LineComponent.builder()
                    .left("Target %")
                    .right(colorCodeString(String.valueOf(stats.getTargetMoxPercent()), getMoxColor()) + "% / " +
                            colorCodeString(String.valueOf(stats.getTargetAgaPercent()), getAgaColor()) + "% / " +
                            colorCodeString(String.valueOf(stats.getTargetLyePercent()), getLyeColor()) + "%")
                    .build());
        }

        panelComponent.getChildren().add(LineComponent.builder()
                .left("Session points")
                .right(colorCodeString(String.valueOf(stats.getSessionMoxCount()), getMoxColor()) + " / " +
                        colorCodeString(String.valueOf(stats.getSessionAgaCount()), getAgaColor()) + " / " +
                        colorCodeString(String.valueOf(stats.getSessionLyeCount()), getLyeColor()))
                .build());

//        panelComponent.getChildren().add(LineComponent.builder()
//                .left("Strategy")
//                .right(config.potionSelectionStrategy().toString())
//                .build());


//        panelComponent.getChildren().add(LineComponent.builder()
//                .left("Stage")
//                .right(state.getState().toString())
//                .build());
//
//        panelComponent.getChildren().add(LineComponent.builder()
//                .left("Refinery")
//                .right(state.getTargetRefinementType().toString())
//                .build());
//
//        panelComponent.getChildren().add(LineComponent.builder()
//                .left("Potion")
//                .right(targetPotion.toString())
//                .build());
//
//        panelComponent.getChildren().add(LineComponent.builder()
//                .left("Components")
//                .right(colorCodePotionComponent(targetPotion.firstComponent) + " / " + colorCodePotionComponent(targetPotion
//                .secondComponent) + " / " + colorCodePotionComponent(targetPotion.thirdComponent))
//                .build());

        if (config.isStationSpeedupInfoboxHighlightEnabled() && state.getState() == MixologyState.REFINING && (uiHelper.isAgitatorSpeedupObjectPresent()) || uiHelper.isAlembicSpeedupObjectPresent()) {
            panelComponent.setBackgroundColor(config.refinerySpeedupInfoboxHighlight());
        }

        if (config.isDigweedInfoboxHighlightEnabled() && uiHelper.isMatureDigweedPresent()) {
            panelComponent.setBackgroundColor(config.digweedInfoboxHighlight());
        }

        return super.render(graphics);
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
}
