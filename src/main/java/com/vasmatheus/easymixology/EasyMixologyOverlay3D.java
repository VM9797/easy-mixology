package com.vasmatheus.easymixology;

import com.vasmatheus.easymixology.model.MixologyStateMachine;
import com.vasmatheus.easymixology.model.enums.PotionComponent;
import com.vasmatheus.easymixology.model.enums.RefinementType;
import net.runelite.api.DecorativeObject;
import net.runelite.api.GameObject;
import net.runelite.api.TileObject;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.outline.ModelOutlineRenderer;

import javax.inject.Inject;
import java.awt.*;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EasyMixologyOverlay3D extends Overlay {
    @Inject
    private ModelOutlineRenderer modelOutlineRenderer;

    @Inject
    private MixologyStateMachine state;

    @Inject
    private EasyMixologyConfig config;

    GameObject conveyorBeltOne;
    GameObject conveyorBeltTwo;

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

        outlineObject(targetRefinery, preDraw ? config.refineryPreOutline() : config.refineryOutline());
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
                outlineObject(lyeLever,config.lyeLeverOutline());
            } else if (component == PotionComponent.AGA && agaLever != null) {
                outlineObject(agaLever, config.agaLeverOutline());
            } else if (component == PotionComponent.MOX && moxLever != null) {
                outlineObject(moxLever, config.moxLeverOutline());
            }
        }
    }

    private void drawConveyorBelt() {
        var conveyorBelts = Stream.of(conveyorBeltOne, conveyorBeltTwo).filter(Objects::nonNull).collect(Collectors.toList());

        for (var conveyorBelt : conveyorBelts) {
            outlineObject(conveyorBelt, config.conveyorBeltOutline());
        }
    }

    private void outlineObject(TileObject object, Color color) {
        modelOutlineRenderer.drawOutline(object, config.borderWidth(), color, config.outlineFeather());
    }
}
