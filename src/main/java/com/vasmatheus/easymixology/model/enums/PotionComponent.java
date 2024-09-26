package com.vasmatheus.easymixology.model.enums;

public enum PotionComponent {
    MOX(330, 1),
    AGA(214, 2),
    LYE(455, 3),
    NONE(0, 0);

    // Reward value is based on the amount required for the unlocks in the reward store, excluding potions
    public final int rewardValue;
    public final int varbitValue;

    PotionComponent(int rewardValue, int varbitValue) {
        this.rewardValue = rewardValue;
        this.varbitValue = varbitValue;
    }

    public static PotionComponent fromVarbitValue(int varbitValue) {
        switch (varbitValue) {
            case 1:
                return MOX;
            case 2:
                return AGA;
            case 3:
                return LYE;
            default:
                return NONE;
        }
    }

}
