package com.vasmatheus.easymixology.model.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum PotionComponent {
    MOX("Mox", 1),
    AGA("Aga", 2),
    LYE("Lye", 3),
    NONE("", 0);

    // Reward value is based on the amount required for the unlocks in the reward store, excluding potions
    public final String displayName;
    public final int varbitValue;

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

    @Override
    public String toString() {
        return displayName;
    }
}
