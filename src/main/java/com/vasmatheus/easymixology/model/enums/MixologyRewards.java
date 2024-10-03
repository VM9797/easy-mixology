package com.vasmatheus.easymixology.model.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum MixologyRewards {
    ApprenticePotionPack("Apprentice potion pack", 420, 70, 30),
    AdeptPotionPack("Adept potion pack", 180, 440, 70),
    ExpertPotionPack("Expert potion pack", 410, 320, 480),
    PrescriptionGoggles("Prescription goggles", 8600,7000,9350),
    AlchemistLabcoat("Alchemist labcoat", 2250,2800,3700),
    AlchemistPants("Alchemist pants", 2250,2800,3700),
    AlchemistGloves("Alchemist gloves", 2250,2800,3700),
    ReagentPouch("Reagent pouch", 13800, 11200, 15100),
    PotionStorage("Potion storage", 7750, 6300, 8950),
    ChuggingBarrel("Chugging barrel", 17250, 14000, 18600),
    AlchemistsAmulet("Alchemist's amulet", 6900, 5650, 7400),
    ;

    public final String itemName;
    public final int moxCost;
    public final int agaCost;
    public final int lyeCost;
}
