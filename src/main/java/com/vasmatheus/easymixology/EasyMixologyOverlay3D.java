package com.vasmatheus.easymixology;

import com.vasmatheus.easymixology.constants.MixologyIDs;
import com.vasmatheus.easymixology.model.MixologyStateMachine;
import com.vasmatheus.easymixology.model.MixologyStats;
import com.vasmatheus.easymixology.model.enums.PotionComponent;
import com.vasmatheus.easymixology.model.enums.RefinementType;
import net.runelite.api.*;
import net.runelite.client.ui.overlay.Overlay;
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
        switch (state.getState()) {
            case MIXING:
                drawLever();
                drawHopperIfNeeded();
                break;
            case MIX_READY:
                drawVessel();
                drawRefinery(true);
                drawHopperIfNeeded();
                break;
            case READY_TO_REFINE:
            case REFINING:
                drawRefinery(false);
                break;
            case READY_TO_DEPOSIT:
                drawConveyorBelt();
                break;
        }
        return null;
    }

    private void drawRefinery(boolean preDraw) {
        if (agitator == null || alembic == null || retort == null) {
            return;
        }

        var targetRefinery = state.getTargetRefinementType() == RefinementType.AGITATOR ? agitator :
                state.getTargetRefinementType() == RefinementType.ALEMBIC ? alembic : retort;

        if (isGraphicsObjectPresent(MixologyIDs.ALEMBIC_SPEEDUP_OBJECT_ID) && targetRefinery == alembic && config.isStationSpeedupHighlightEnabled()) {
            outlineObject(targetRefinery, config.refinerySpeedupOutline());
            return;
        }
        else if (isGraphicsObjectPresent(MixologyIDs.AGITATOR_SPEEDUP_OBJECT_ID) && targetRefinery == agitator && config.isStationSpeedupHighlightEnabled()) {
            outlineObject(targetRefinery, config.refinerySpeedupOutline());
            return;
        }

        if (!config.isStationHighlightEnabled()) {
            return;
        }

        outlineObject(targetRefinery, preDraw ? config.refineryPreOutline() : config.refineryOutline());
    }

    private void drawVessel() {
        if (vessel == null || !config.isVesselHighlightEnabled()) {
            return;
        }

        outlineObject(vessel, config.vesselOutline());
    }

    private void drawLever() {
        if (!config.isLeverHighlightEnabled()) {
            return;
        }

        var componentsToAdd = state.getComponentsToAdd();

        for (var component : componentsToAdd) {
            if (component == PotionComponent.LYE && lyeLever != null) {
                outlineObject(lyeLever, config.lyeLeverOutline());
            } else if (component == PotionComponent.AGA && agaLever != null) {
                outlineObject(agaLever, config.agaLeverOutline());
            } else if (component == PotionComponent.MOX && moxLever != null) {
                outlineObject(moxLever, config.moxLeverOutline());
            }
        }
    }

    private void drawConveyorBelt() {
        if (!config.isConveyorBeltHighlightEnabled()) {
            return;
        }

        for (var conveyorBelt : conveyorBelts) {
            outlineObject(conveyorBelt, config.conveyorBeltOutline());
        }
    }

    private void drawHopperIfNeeded() {
        if (hopper == null || !isHopperOutlineNeeded()) {
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

    private boolean isGraphicsObjectPresent(int graphicsObjectId) {
        for (GraphicsObject graphicsObject : client.getGraphicsObjects()) {
            if (graphicsObject.getId() == graphicsObjectId) {
                return true;
            }
        }
        return false;
    }
}
