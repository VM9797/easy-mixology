package com.vasmatheus.easymixology.model;

import com.vasmatheus.easymixology.model.enums.MixologyState;
import com.vasmatheus.easymixology.model.enums.Potion;
import com.vasmatheus.easymixology.model.enums.PotionComponent;
import com.vasmatheus.easymixology.model.enums.RefinementType;
import lombok.Getter;
import net.runelite.api.Client;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Singleton
public class MixologyStateMachine {
    @Inject
    Client client;

    private MixologyOrder order = MixologyOrder.EMPTY;

    @Getter
    private MixologyState state = MixologyState.WAITING_TO_START;

    @Getter
    private MixologyVariablesSnapshot variablesSnapshot = MixologyVariablesSnapshot.EMPTY;

    @Getter
    private boolean isStarted = false;

    public Potion getTargetPotion() {
        return order.mostValuablePotion;
    }

    public RefinementType getTargetRefinementType() {
        return order.mostValuablePotionRefinement;
    }

    public void stop() {
        isStarted = false;
        order = MixologyOrder.EMPTY;
        state = MixologyState.WAITING_TO_START;
        variablesSnapshot = MixologyVariablesSnapshot.EMPTY;
    }

    public void start() {
        isStarted = true;
        order = MixologyOrder.fromVarbits(client);
        state = MixologyState.MIXING;
        variablesSnapshot = MixologyVariablesSnapshot.fromVarbits(client);
    }

    public void onVarbitsUpdated() {
        var orderFromVarbits = MixologyOrder.fromVarbits(client);
        var variablesFromVarbits = MixologyVariablesSnapshot.fromVarbits(client);

        if (!order.doesEqual(orderFromVarbits) || !order.isValidOrder) {
            // RESET
            order = orderFromVarbits;
            state = MixologyState.MIXING;
            variablesSnapshot = variablesFromVarbits;
            return;
        }


        switch (state) {
            case MIXING:
                processMixingState(client, variablesFromVarbits);
                break;
            case MIX_READY:
                processPotionReadyState(client, variablesFromVarbits);
                break;
            case READY_TO_REFINE:
                processReadyToRefineState(client, variablesFromVarbits);
                break;
            case REFINING:
                processRefiningState(client, variablesFromVarbits);
                break;
        }

        variablesSnapshot = variablesFromVarbits;

    }

    // TODO: It's unnecessary to do this on every render cycle
    public Set<PotionComponent> getComponentsToAdd() {
        var targetPotion = getTargetPotion();
        var componentsToAdd = new ArrayList<>(Arrays.asList(targetPotion.firstComponent, targetPotion.secondComponent,
                targetPotion.thirdComponent));

        componentsToAdd.remove(variablesSnapshot.componentInFirstMixer);
        componentsToAdd.remove(variablesSnapshot.componentInSecondMixer);

        return new HashSet<>(componentsToAdd);
    }

    private void processMixingState(Client client, MixologyVariablesSnapshot nextSnapshot) {
        if (nextSnapshot.potionInVessel == order.mostValuablePotion) {
            state = MixologyState.MIX_READY;
        }
    }

    private void processPotionReadyState(Client client, MixologyVariablesSnapshot nextSnapshot) {
        if (nextSnapshot.potionInVessel == Potion.NONE) {
            state = MixologyState.READY_TO_REFINE;
        } else if (nextSnapshot.potionInVessel != variablesSnapshot.potionInVessel) {
            state = MixologyState.MIXING;
        }
    }

    private void processReadyToRefineState(Client client, MixologyVariablesSnapshot nextSnapshot) {
        switch (order.mostValuablePotionRefinement) {
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


    private void processRefiningState(Client client, MixologyVariablesSnapshot nextSnapshot) {
        switch (order.mostValuablePotionRefinement) {
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
    }
}
