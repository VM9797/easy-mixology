package com.example;

public enum Potion {
    MAMMOTH(PotionComponent.MOX, PotionComponent.MOX, PotionComponent.MOX, 1),
    MYSTIC(PotionComponent.MOX, PotionComponent.MOX, PotionComponent.AGA, 2),
    MARLEY(PotionComponent.MOX, PotionComponent.MOX, PotionComponent.LYE, 3),
    MIXALOT(PotionComponent.MOX, PotionComponent.AGA, PotionComponent.LYE, 10),

    ALCO(PotionComponent.AGA, PotionComponent.AGA, PotionComponent.AGA, 4),
    AZURE(PotionComponent.AGA, PotionComponent.AGA, PotionComponent.MOX, 5),
    AQUA(PotionComponent.AGA, PotionComponent.AGA, PotionComponent.LYE, 6),

    LIPLACK(PotionComponent.LYE, PotionComponent.LYE, PotionComponent.LYE, 7),
    MEGA(PotionComponent.LYE, PotionComponent.LYE, PotionComponent.MOX, 8),
    ANTI(PotionComponent.LYE, PotionComponent.LYE, PotionComponent.AGA, 9),

    NONE(PotionComponent.NONE, PotionComponent.NONE, PotionComponent.NONE, 0),
    ;

    public final PotionComponent firstComponent;
    public final PotionComponent secondComponent;
    public final PotionComponent thirdComponent;
    public final int varbitValue;
    public final int potionShopValue;


    Potion(PotionComponent firstComponent, PotionComponent secondComponent, PotionComponent thirdComponent, int varbitValue) {
        this.firstComponent = firstComponent;
        this.secondComponent = secondComponent;
        this.thirdComponent = thirdComponent;
        this.varbitValue = varbitValue;
        potionShopValue = firstComponent.rewardValue + secondComponent.rewardValue + thirdComponent.rewardValue;
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
}
