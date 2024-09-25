package com.example;

import net.runelite.api.Client;

public class MixologyStateMachine {
    private Order order = new Order(Potion.NONE, Potion.NONE, Potion.NONE, RefinementType.NONE, RefinementType.NONE, RefinementType.NONE);
    private MixologyState state = MixologyState.WAITING_TO_START;
    private MixologyVariablesSnapshot variablesSnapshot = MixologyVariablesSnapshot.EMPTY;

    public MixologyState getState() {
        return state;
    }

    @Override
    public String toString() {
        return "MixologyStateMachine{" +
                "order=" + order +
                ", state=" + state +
                ", variablesSnapshot=" + variablesSnapshot +
                '}';
    }

    public void onTickStart(Client client) {
        var orderFromVarbits = Order.fromVarbits(client);
        if (state == MixologyState.WAITING_TO_START && orderFromVarbits.doesEqual(order)) {
            state = MixologyState.MIXING;
        }
    }

    public void onVarbitsUpdated(Client client) {
        var orderFromVarbits = Order.fromVarbits(client);
        var variablesFromVarbits = MixologyVariablesSnapshot.fromVarbits(client);

        if (!order.doesEqual(orderFromVarbits)) {
            // RESET
            order = orderFromVarbits;
            state = MixologyState.WAITING_TO_START;
            variablesSnapshot = variablesFromVarbits;
            return;
//            client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "RESET HAPPENED", null);
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

    public String getNextStep() {
        StringBuilder sb = new StringBuilder();
        sb.append(state.name());
        sb.append(": ");
        switch (state) {
            case MIXING:
                sb.append(order.mostValuablePotion.name());
                sb.append(" - ");
                sb.append(order.mostValuablePotion.firstComponent.name());
                sb.append(order.mostValuablePotion.secondComponent.name());
                sb.append(order.mostValuablePotion.thirdComponent.name());
                break;
            case MIX_READY:
                sb.append("TAKE POTION FROM VESSEL");
                break;
            case READY_TO_REFINE:
                sb.append("REFINE AT ");
                sb.append(order.mostValuablePotionRefinement.name());
                break;
            case REFINING:
                sb.append("KEEP REFINING");
                break;
            case READY_TO_DEPOSIT:
                sb.append("DEPOSIT");
        }

        return sb.toString();
    }

    private void processMixingState(Client client, MixologyVariablesSnapshot nextSnapshot) {
//        client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "MIXING STATE PROCESS", null);
//        client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "NEXT: " + nextSnapshot.potionInVessel.name(), null);
//        client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "ORDER: " + order.mostValuablePotion.name(), null);
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
