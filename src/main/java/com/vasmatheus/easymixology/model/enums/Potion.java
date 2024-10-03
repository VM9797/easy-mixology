package com.vasmatheus.easymixology.model.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Potion {
    MAMMOTH("Mammoth-might mix", PotionComponent.MOX, PotionComponent.MOX, PotionComponent.MOX, 1, 60, 190),
    MYSTIC("Mystic mana amalgam", PotionComponent.MOX, PotionComponent.MOX, PotionComponent.AGA, 2, 63, 215),
    MARLEY("Marley's moonlight", PotionComponent.MOX, PotionComponent.MOX, PotionComponent.LYE, 3, 66, 240),
    MIXALOT("Mixalot", PotionComponent.MOX, PotionComponent.AGA, PotionComponent.LYE, 10, 81, 365),

    ALCO("Alco-augmentator", PotionComponent.AGA, PotionComponent.AGA, PotionComponent.AGA, 4, 60, 190),
    AZURE("Azure aura mix", PotionComponent.AGA, PotionComponent.AGA, PotionComponent.MOX, 5, 69, 265),
    AQUA("Aqualux amalgam", PotionComponent.AGA, PotionComponent.AGA, PotionComponent.LYE, 6, 72, 290),

    LIPLACK("Liplack liquor", PotionComponent.LYE, PotionComponent.LYE, PotionComponent.LYE, 7, 60, 190),
    MEGA("Megalite liquid", PotionComponent.LYE, PotionComponent.LYE, PotionComponent.MOX, 8, 75, 315),
    ANTI("Anti-leech lotion", PotionComponent.LYE, PotionComponent.LYE, PotionComponent.AGA, 9, 78, 340),

    NONE("", PotionComponent.NONE, PotionComponent.NONE, PotionComponent.NONE, 0, 0, 0),
    ;

    public final String potionName;
    public final PotionComponent firstComponent;
    public final PotionComponent secondComponent;
    public final PotionComponent thirdComponent;
    public final int varbitValue;
    public final int herbloreLevel;
    public final int totalXP;
    public final int moxPasteRequirement;
    public final int agaPasteRequirement;
    public final int lyePasteRequirement;
    public final int moxRewardValue;
    public final int agaRewardValue;
    public final int lyeRewardValue;
    public final boolean isAllMox;
    public final boolean isAllAga;
    public final boolean isAllLye;


    Potion(String potionName, PotionComponent firstComponent, PotionComponent secondComponent, PotionComponent thirdComponent,
           int varbitValue,
           int herbloreLevel, int totalXP) {
        this.firstComponent = firstComponent;
        this.secondComponent = secondComponent;
        this.thirdComponent = thirdComponent;
        this.varbitValue = varbitValue;
        this.herbloreLevel = herbloreLevel;
        this.totalXP = totalXP;
        this.potionName = potionName;

        int moxCount = 0;
        int agaCount = 0;
        int lyeCount = 0;

        if (firstComponent == PotionComponent.AGA) {
            agaCount++;
        }
        if (secondComponent == PotionComponent.AGA) {
            agaCount++;
        }
        if (thirdComponent == PotionComponent.AGA) {
            agaCount++;
        }
        if (firstComponent == PotionComponent.LYE) {
            lyeCount++;
        }
        if (secondComponent == PotionComponent.LYE) {
            lyeCount++;
        }
        if (thirdComponent == PotionComponent.LYE) {
            lyeCount++;
        }
        if (firstComponent == PotionComponent.MOX) {
            moxCount++;
        }
        if (secondComponent == PotionComponent.MOX) {
            moxCount++;
        }
        if (thirdComponent == PotionComponent.MOX) {
            moxCount++;
        }

        agaPasteRequirement = agaCount * 10;
        moxPasteRequirement = moxCount * 10;
        lyePasteRequirement = lyeCount * 10;

        isAllMox = moxCount == 3;
        isAllAga = agaCount == 3;
        isAllLye = lyeCount == 3;
        boolean isMixalot = moxCount == 1 && agaCount == 1 && lyeCount == 1;

        moxRewardValue = (isAllMox || isMixalot) ? 20 : moxCount * 10;
        agaRewardValue = (isAllAga || isMixalot) ? 20 : agaCount * 10;
        lyeRewardValue = (isAllLye || isMixalot) ? 20 : lyeCount * 10;
    }

    public static Potion fromVarbitValue(int varbitValue) {
        switch (varbitValue) {
            case 1:
                return MAMMOTH;
            case 2:
                return MYSTIC;
            case 3:
                return MARLEY;
            case 10:
                return MIXALOT;
            case 4:
                return ALCO;
            case 5:
                return AZURE;
            case 6:
                return AQUA;
            case 7:
                return LIPLACK;
            case 8:
                return MEGA;
            case 9:
                return ANTI;
            default:
                return NONE;
        }
    }

    @Override
    public String toString() {
        return potionName;
    }
}
