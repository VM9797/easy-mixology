package com.vasmatheus.easymixology.model;

import com.vasmatheus.easymixology.EasyMixologyConfig;
import com.vasmatheus.easymixology.model.enums.MixologyState;
import com.vasmatheus.easymixology.model.enums.Potion;
import com.vasmatheus.easymixology.model.enums.PotionComponent;
import com.vasmatheus.easymixology.model.enums.RefinementType;
import lombok.Getter;
import net.runelite.api.Client;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.*;

@Singleton
public class MixologyStateMachine {
    @Inject
    private Client client;

    @Inject
    private EasyMixologyConfig config;

    private MixologyOrder order = MixologyOrder.EMPTY;

    @Getter
    private MixologyState state = MixologyState.WAITING_TO_START;

    @Getter
    private MixologyVariablesSnapshot variablesSnapshot = MixologyVariablesSnapshot.EMPTY;

    @Getter
    private boolean isStarted = false;

    @Getter
    private final Map<PotionComponent, Integer> leversToPullMap = new HashMap<>();

    @Getter
    private final Map<PotionComponent, Integer> leversToPullNextPotionMap = new HashMap<>();

    @Getter
    private int currentlyProcessingPotionIndex = 0;

    public Potion getTargetPotion() {
        return order.potions.get(currentlyProcessingPotionIndex);
    }

    public RefinementType getTargetRefinementType() {
        return order.refinementTypes.get(currentlyProcessingPotionIndex);
    }

    public RefinementType getFirstTargetRefinementType() {
        return order.refinementTypes.get(0);
    }

    public RefinementType getNextTargetRefinementType() {
        if (isLastPotion()) {
            return RefinementType.NONE;
        }
        return order.refinementTypes.get(currentlyProcessingPotionIndex + 1);
    }

    public boolean isLastPotion() {
        return currentlyProcessingPotionIndex == order.getNumberOfPotionsToDo();
    }

    public void stop() {
        isStarted = false;
        order = MixologyOrder.EMPTY;
        state = MixologyState.WAITING_TO_START;
        variablesSnapshot = MixologyVariablesSnapshot.EMPTY;
        currentlyProcessingPotionIndex = 0;
    }

    public void start() {
        isStarted = true;
        order = MixologyOrder.fromVarbits(client, config.potionSelectionStrategy());
        state = MixologyState.MIXING;
        variablesSnapshot = MixologyVariablesSnapshot.fromVarbits(client);
        currentlyProcessingPotionIndex = 0;

        processMixingState(variablesSnapshot);
    }

    public void onTickUpdate() {
        if (!isStarted) {
            return;
        }

        if (state == MixologyState.MIXING) {
            updateLeversToPull();
        }
    }

    public void onVarbitUpdate() {
        var orderFromVarbits = MixologyOrder.fromVarbits(client, config.potionSelectionStrategy());
        var variablesFromVarbits = MixologyVariablesSnapshot.fromVarbits(client);

        if (!order.doesEqual(orderFromVarbits) || !order.isValidOrder) {
            // RESET
            order = orderFromVarbits;
            state = MixologyState.MIXING;
            variablesSnapshot = variablesFromVarbits;
            currentlyProcessingPotionIndex = 0;
        }


        switch (state) {
            case MIXING:
                processMixingState(variablesFromVarbits);
                break;
            case MIX_READY:
                processPotionReadyState(variablesFromVarbits);
                break;
            case READY_TO_REFINE:
                processReadyToRefineState(variablesFromVarbits);
                break;
            case REFINING:
                processRefiningState(variablesFromVarbits);
                break;
        }

        variablesSnapshot = variablesFromVarbits;

    }

    private void updateLeversToPull() {
        var targetPotion = getTargetPotion();
        var nextPotion = currentlyProcessingPotionIndex == order.getNumberOfPotionsToDo() ? null : order.potions.get(currentlyProcessingPotionIndex + 1);

        var leversToPull = new ArrayList<>(Arrays.asList(targetPotion.firstComponent, targetPotion.secondComponent,
                targetPotion.thirdComponent));

        List<PotionComponent> leversToPullNext = nextPotion == null ? List.of() : new ArrayList<>(Arrays.asList(nextPotion.firstComponent,
                nextPotion.secondComponent,
                nextPotion.thirdComponent));

        for (var component : PotionComponent.values()) {
            leversToPullMap.put(component, 0);
            leversToPullNextPotionMap.put(component, 0);
        }

        if (leversToPull.remove(variablesSnapshot.componentInFirstMixer)) {
            leversToPull.remove(variablesSnapshot.componentInSecondMixer);
        }

        for (var component : leversToPull) {
            leversToPullMap.put(component, leversToPullMap.get(component) + 1);
        }

        for (var compontent : leversToPullNext) {
            leversToPullNextPotionMap.put(compontent, leversToPullNextPotionMap.get(compontent) + 1);
        }
    }

    private void processMixingState(MixologyVariablesSnapshot nextSnapshot) {
        updateLeversToPull();
        if (nextSnapshot.potionInVessel == getTargetPotion()) {
            state = MixologyState.MIX_READY;
        }
    }

    private void processPotionReadyState(MixologyVariablesSnapshot nextSnapshot) {
        if (nextSnapshot.potionInVessel == Potion.NONE && currentlyProcessingPotionIndex < order.getNumberOfPotionsToDo()) {
            state = MixologyState.MIXING;
            currentlyProcessingPotionIndex++;
        } else if (nextSnapshot.potionInVessel == Potion.NONE) {
            state = MixologyState.READY_TO_REFINE;
            currentlyProcessingPotionIndex = 0;
        } else if (nextSnapshot.potionInVessel != variablesSnapshot.potionInVessel) {
            state = MixologyState.MIXING;
        }
    }

    private void processReadyToRefineState(MixologyVariablesSnapshot nextSnapshot) {
        switch (getTargetRefinementType()) {
            case AGITATOR:
                if (variablesSnapshot.agitatorLevel < nextSnapshot.agitatorLevel) {
                    state = MixologyState.REFINING;
                }
            case ALEMBIC:
                if (variablesSnapshot.alembicLevel < nextSnapshot.alembicLevel) {
                    state = MixologyState.REFINING;
                }
            case RETORT:
                if (variablesSnapshot.retortLevel < nextSnapshot.retortLevel) {
                    state = MixologyState.REFINING;
                }
        }
    }


    private void processRefiningState(MixologyVariablesSnapshot nextSnapshot) {
        switch (getTargetRefinementType()) {
            case AGITATOR:
                if (variablesSnapshot.agitatorLevel > 9 && nextSnapshot.agitatorLevel == 0) {
                    state = MixologyState.READY_TO_DEPOSIT;
                }
                break;
            case ALEMBIC:
                if (variablesSnapshot.alembicLevel > 9 && nextSnapshot.alembicLevel == 0) {
                    state = MixologyState.READY_TO_DEPOSIT;
                }
                break;
            case RETORT:
                if (variablesSnapshot.retortLevel > 9 && nextSnapshot.retortLevel == 0) {
                    state = MixologyState.READY_TO_DEPOSIT;
                }
                break;
        }

        if (state == MixologyState.READY_TO_DEPOSIT) {
            if (currentlyProcessingPotionIndex < order.getNumberOfPotionsToDo()) {
                state = MixologyState.READY_TO_REFINE;
                currentlyProcessingPotionIndex++;
            } else {
                currentlyProcessingPotionIndex = 0;
            }
        }
    }
}
