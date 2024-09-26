package com.vasmatheus.easymixology;

import com.vasmatheus.easymixology.constants.MixologyIDs;
import com.vasmatheus.easymixology.model.MixologyStateMachine;
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

    @Override
    public Dimension render(Graphics2D graphics) {
        switch (state.getState()) {
            case MIXING:
                drawLever();
                break;
            case MIX_READY:
                drawVessel();
                drawRefinery(true);
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

        if (isGraphicsObjectPresent(MixologyIDs.ALEMBIC_SPEEDUP_OBJECT_ID) && targetRefinery == alembic) {
            outlineObject(targetRefinery, config.refinerySpeedupOutline());
            return;
        }
        else if (isGraphicsObjectPresent(MixologyIDs.AGITATOR_SPEEDUP_OBJECT_ID) && targetRefinery == agitator) {
            outlineObject(targetRefinery, config.refinerySpeedupOutline());
            return;
        }

        outlineObject(targetRefinery, preDraw ? config.refineryPreOutline() : config.refineryOutline());
    }

    private boolean isGraphicsObjectPresent(int graphicsObjectId) {
        for (GraphicsObject graphicsObject : client.getGraphicsObjects()) {
            if (graphicsObject.getId() == graphicsObjectId) {
                return true;
            }
        }
        return false;
    }

    private void drawVessel() {
        if (vessel == null) {
            return;
        }
        outlineObject(vessel, config.vesselOutline());
    }

    private void drawLever() {
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
        for (var conveyorBelt : conveyorBelts) {
            outlineObject(conveyorBelt, config.conveyorBeltOutline());
        }
    }

    private void outlineObject(TileObject object, Color color) {
        modelOutlineRenderer.drawOutline(object, config.borderWidth(), color, config.outlineFeather());
    }
}
