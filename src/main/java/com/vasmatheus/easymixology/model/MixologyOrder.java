package com.vasmatheus.easymixology.model;

import com.vasmatheus.easymixology.constants.MixologyVarbits;
import com.vasmatheus.easymixology.model.enums.Potion;
import com.vasmatheus.easymixology.model.enums.RefinementType;
import net.runelite.api.Client;

public class MixologyOrder {
    public final Potion firstPotion;
    public final Potion secondPotion;
    public final Potion thirdPotion;

    public final RefinementType firstPotionRefinement;
    public final RefinementType secondPotionRefinement;
    public final RefinementType thirdPotionRefinement;

    public final Potion mostValuablePotion;
    public final RefinementType mostValuablePotionRefinement;
    public final boolean isValidOrder;


    public MixologyOrder(Potion firstPotion, Potion secondPotion, Potion thirdPotion, RefinementType firstPotionRefinement,
                         RefinementType secondPotionRefinement, RefinementType thirdPotionRefinement) {
        this.firstPotion = firstPotion;
        this.secondPotion = secondPotion;
        this.thirdPotion = thirdPotion;
        this.firstPotionRefinement = firstPotionRefinement;
        this.secondPotionRefinement = secondPotionRefinement;
        this.thirdPotionRefinement = thirdPotionRefinement;

        isValidOrder =
                firstPotion != Potion.NONE && secondPotion != Potion.NONE && thirdPotion != Potion.NONE && firstPotionRefinement != RefinementType.NONE && secondPotionRefinement != RefinementType.NONE && thirdPotionRefinement != RefinementType.NONE;

        Potion mostValuablePotion = firstPotion;
        RefinementType mostValuablePotionRefinement = firstPotionRefinement;

        if (secondPotion.potionShopValue > mostValuablePotion.potionShopValue) {
            mostValuablePotion = secondPotion;
            mostValuablePotionRefinement = secondPotionRefinement;
        }
        if (thirdPotion.potionShopValue > mostValuablePotion.potionShopValue) {
            mostValuablePotion = thirdPotion;
            mostValuablePotionRefinement = thirdPotionRefinement;
        }

        this.mostValuablePotion = mostValuablePotion;
        this.mostValuablePotionRefinement = mostValuablePotionRefinement;
    }

    @Override
    public String toString() {
        return "Order{" +
                "firstPotion=" + firstPotion +
                ", secondPotion=" + secondPotion +
                ", thirdPotion=" + thirdPotion +
                ", firstPotionRefinement=" + firstPotionRefinement +
                ", secondPotionRefinement=" + secondPotionRefinement +
                ", thirdPotionRefinement=" + thirdPotionRefinement +
                ", mostValuablePotion=" + mostValuablePotion +
                ", mostValuablePotionRefinement=" + mostValuablePotionRefinement +
                ", isValidOrder=" + isValidOrder +
                '}';
    }

    public static MixologyOrder fromVarbits(Client client) {
        return new MixologyOrder(Potion.fromVarbitValue(client.getVarbitValue(MixologyVarbits.ORDER_FIRST_POTION)),
                Potion.fromVarbitValue(client.getVarbitValue(MixologyVarbits.ORDER_SECOND_POTION)),
                Potion.fromVarbitValue(client.getVarbitValue(MixologyVarbits.ORDER_THIRD_POTION)),
                RefinementType.fromVarbitValue(client.getVarbitValue(MixologyVarbits.ORDER_FIRST_POTION_REFINEMENT)),
                RefinementType.fromVarbitValue(client.getVarbitValue(MixologyVarbits.ORDER_SECOND_POTION_REFINEMENT)),
                RefinementType.fromVarbitValue(client.getVarbitValue(MixologyVarbits.ORDER_THIRD_POTION_REFINEMENT)));
    }

    public boolean doesEqual(MixologyOrder other) {
        return firstPotion == other.firstPotion &&
                secondPotion == other.secondPotion &&
                thirdPotion == other.thirdPotion &&
                firstPotionRefinement == other.firstPotionRefinement &&
                secondPotionRefinement == other.secondPotionRefinement &&
                thirdPotionRefinement == other.thirdPotionRefinement;
    }
}
