package com.vasmatheus.easymixology;

import com.vasmatheus.easymixology.model.MixologyStateMachine;
import com.vasmatheus.easymixology.model.MixologyStats;
import com.vasmatheus.easymixology.model.enums.PotionComponent;
import com.vasmatheus.easymixology.model.enums.RefinementType;
import net.runelite.api.Point;
import net.runelite.api.*;
import net.runelite.api.coords.LocalPoint;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayUtil;
import net.runelite.client.ui.overlay.outline.ModelOutlineRenderer;

import javax.inject.Inject;
import java.awt.*;
import java.util.HashSet;
import java.util.Set;

public class EasyMixologyOverlay3D extends Overlay {
    @Inject
    private ModelOutlineRenderer modelOutlineRenderer;

    @Inject
    private MixologyStateMachine state;

    @Inject
    private MixologyStats stats;

    @Inject
    private EasyMixologyConfig config;

    @Inject
    private UiHelper uiHelper;

    @Inject
    private Client client;

    Set<GameObject> conveyorBelts = new HashSet<>();

    GameObject lyeLever;
    DecorativeObject agaLever;
    GameObject moxLever;

    GameObject alembic;
    GameObject agitator;
    GameObject retort;

    GameObject vessel;
    GameObject hopper;

    @Override
    public Dimension render(Graphics2D graphics) {
        if (!state.isStarted()) {
            return null;
        }

        switch (state.getState()) {
            case MIXING:
                outlineLevers(graphics, false);
                outlineHopper();
                break;
            case MIX_READY:
                outlineVessel();
                if (state.isLastPotion()) {
                    outlineRefinery(true, false, graphics);
                } else {
                    outlineLevers(graphics, true);
                }
                outlineHopper();
                break;
            case READY_TO_REFINE:
            case REFINING:
                outlineRefinery(false, !state.isLastPotion(), graphics);

                if (state.isLastPotion()) {
                    drawConveyorBelt(true);
                }
                break;
            case READY_TO_DEPOSIT:
                drawConveyorBelt(false);
                break;
        }

        outlineDigweed();

        return null;
    }

    private void outlineDigweed() {
        if (!config.isDigweedHighlightEnabled() || !uiHelper.isMatureDigweedPresent()) {
            return;
        }

        outlineObject(uiHelper.getMatureDigweedObjectOrNull(), config.digweedOutline());
    }

    private void outlineRefinery(boolean preDraw, boolean preDrawNext, Graphics2D graphics) {
        if (agitator == null || alembic == null || retort == null) {
            return;
        }

        var primaryTargetRefinement = preDraw ? state.getFirstTargetRefinementType() : state.getTargetRefinementType();

        var primaryTargetRefinery = primaryTargetRefinement == RefinementType.AGITATOR ? agitator :
                primaryTargetRefinement == RefinementType.ALEMBIC ? alembic : retort;

        var secondaryTargetRefinement = preDrawNext ? state.getNextTargetRefinementType() : RefinementType.NONE;

        var secondaryTargetRefinery = secondaryTargetRefinement == RefinementType.AGITATOR ? agitator :
                secondaryTargetRefinement == RefinementType.ALEMBIC ? alembic : secondaryTargetRefinement == RefinementType.RETORT ?
                        retort : null;

        if (secondaryTargetRefinery != null && secondaryTargetRefinery != primaryTargetRefinery) {
            outlineObject(secondaryTargetRefinery, config.refineryPreOutline());
        }

        if (config.isStationProcessCountEnabled() && !preDraw) {
            var stationOffset = config.stationTextOffset();
            int xOffset = primaryTargetRefinement == RefinementType.ALEMBIC ? -stationOffset : primaryTargetRefinement == RefinementType.RETORT ?
                    stationOffset : 0;
            int yOffset = primaryTargetRefinement == RefinementType.AGITATOR ? stationOffset : 0;
            int zOffset = 250;

            drawTextAtObject(primaryTargetRefinery, String.format("%dx", state.getRefinementTypeCountMap().get(primaryTargetRefinement)),
                    config.refineryOutline(), graphics, xOffset, yOffset, zOffset);
        }

        if (primaryTargetRefinery == alembic && config.isStationSpeedupHighlightEnabled() && uiHelper.isAlembicSpeedupObjectPresent()) {
            outlineObject(primaryTargetRefinery, config.refinerySpeedupOutline());
            return;
        }
        else if (primaryTargetRefinery == agitator && config.isStationSpeedupHighlightEnabled() && uiHelper.isAgitatorSpeedupObjectPresent()) {
            outlineObject(primaryTargetRefinery, config.refinerySpeedupOutline());
            return;
        }

        if (!config.isStationHighlightEnabled()) {
            return;
        }

        outlineObject(primaryTargetRefinery, preDraw ? config.refineryPreOutline() : config.refineryOutline());
    }

    private void outlineVessel() {
        if (vessel == null || !config.isVesselHighlightEnabled()) {
            return;
        }

        outlineObject(vessel, config.vesselOutline());
    }

    private void outlineLevers(Graphics2D graphics, boolean preDraw) {
        var pullCountMap = preDraw ? state.getLeversToPullNextPotionMap() : state.getLeversToPullMap();
        var componentsToAdd = pullCountMap.keySet();

        for (var component : componentsToAdd) {
            if (component == PotionComponent.LYE && lyeLever != null && pullCountMap.get(component) != 0) {
                outlineTargetLever(lyeLever, preDraw ? config.lyeLeverPreOutline() : config.lyeLeverOutline());

                if (!preDraw) {
                    drawLeverPullCount(lyeLever, pullCountMap.get(component), config.lyeLeverOutline(), graphics);
                }
            } else if (component == PotionComponent.AGA && agaLever != null && pullCountMap.get(component) != 0) {
                outlineTargetLever(agaLever, preDraw ? config.agaLeverPreOutline() : config.agaLeverOutline());

                if (!preDraw) {
                    drawLeverPullCount(agaLever, pullCountMap.get(component), config.agaLeverOutline(), graphics);
                }
            } else if (component == PotionComponent.MOX && moxLever != null && pullCountMap.get(component) != 0) {
                outlineTargetLever(moxLever, preDraw ? config.moxLeverPreOutline() : config.moxLeverOutline());

                if (!preDraw) {
                    drawLeverPullCount(moxLever, pullCountMap.get(component), config.moxLeverOutline(), graphics);
                }
            }
        }
    }

    private void outlineTargetLever(TileObject lever, Color color) {
        if (!config.isLeverHighlightEnabled()) {
            return;
        }

        outlineObject(lever, color);
    }

    private void drawLeverPullCount(TileObject targetLever, Integer pullCount, Color color, Graphics2D graphics)
    {
        if (!config.isLeverPullCountTextEnabled() || pullCount == null)
        {
            return;
        }

        drawTextAtObject(targetLever, String.format("%dx", pullCount), color, graphics, 0, 0, 250);
    }

    private void drawTextAtObject(TileObject object, String text, Color color, Graphics2D graphics, int xOffset, int yOffset, int zOffset) {
        LocalPoint localPoint = object.getLocalLocation();
        localPoint = new LocalPoint(localPoint.getX() + xOffset, localPoint.getY() + yOffset);
        Point pos = Perspective.getCanvasTextLocation(client, graphics, localPoint, text, zOffset);

        var originalFont = graphics.getFont();
        var textFont = new Font(originalFont.getFontName(), config.boldText() ? Font.BOLD : originalFont.getStyle(),
                originalFont.getSize() + config.textSize());
        graphics.setFont(textFont);
        OverlayUtil.renderTextLocation(graphics, pos, text, color);
        graphics.setFont(originalFont);
    }

    private void drawConveyorBelt(boolean preDraw) {
        if (!config.isConveyorBeltHighlightEnabled()) {
            return;
        }

        for (var conveyorBelt : conveyorBelts) {
            outlineObject(conveyorBelt, preDraw ? config.conveyorBeltPreOutline() : config.conveyorBeltOutline());
        }
    }

    private void outlineHopper() {
        if (hopper == null || !isHopperOutlineNeeded() || !config.isEmptyHopperHighlightEnabled()) {
            return;
        }

        outlineObject(hopper, config.hopperDepositNeededOutline());
    }

    private void outlineObject(TileObject object, Color color) {
        modelOutlineRenderer.drawOutline(object, config.borderWidth(), color, config.outlineFeather());
    }

    private boolean isHopperOutlineNeeded() {
        var potion = state.getTargetPotion();
        return potion.moxValue > stats.getHopperMoxCount() || potion.agaValue > stats.getHopperAgaCount() || potion.lyeValue > stats.getHopperLyeCount();
    }

}
