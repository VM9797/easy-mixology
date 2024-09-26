package com.vasmatheus.easymixology.model;

import com.vasmatheus.easymixology.constants.MixologyVarbits;
import com.vasmatheus.easymixology.model.enums.Potion;
import com.vasmatheus.easymixology.model.enums.RefinementType;
import net.runelite.api.Client;
import net.runelite.api.Skill;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Comparator;
import java.util.stream.Stream;

public class MixologyOrder {
    public static final MixologyOrder EMPTY = new MixologyOrder(Potion.NONE, Potion.NONE, Potion.NONE, RefinementType.NONE,
            RefinementType.NONE, RefinementType.NONE, 60);

    public final Potion firstPotion;
    public final Potion secondPotion;
    public final Potion thirdPotion;

    public final RefinementType firstPotionRefinement;
    public final RefinementType secondPotionRefinement;
    public final RefinementType thirdPotionRefinement;

    public final Potion mostValuablePotion;
    public final RefinementType mostValuablePotionRefinement;
    public final boolean isValidOrder;

    private final int playerHerbloreLevel;


    public MixologyOrder(Potion firstPotion, Potion secondPotion, Potion thirdPotion, RefinementType firstPotionRefinement,
                         RefinementType secondPotionRefinement, RefinementType thirdPotionRefinement, int playerHerbloreLevel) {
        this.firstPotion = firstPotion;
        this.secondPotion = secondPotion;
        this.thirdPotion = thirdPotion;
        this.firstPotionRefinement = firstPotionRefinement;
        this.secondPotionRefinement = secondPotionRefinement;
        this.thirdPotionRefinement = thirdPotionRefinement;
        this.playerHerbloreLevel = playerHerbloreLevel;

        var potionStream = Stream.of(Pair.of(firstPotion, firstPotionRefinement), Pair.of(secondPotion, secondPotionRefinement),
                Pair.of(thirdPotion, thirdPotionRefinement));
        var targetPair =
                potionStream.filter(it -> it.getLeft().herbloreLevel <= playerHerbloreLevel).max(Comparator.comparingInt(p -> p.getLeft().potionShopValue));

        if (targetPair.isPresent()) {
            mostValuablePotion = targetPair.get().getLeft();
            mostValuablePotionRefinement = targetPair.get().getRight();
        } else {
            mostValuablePotion = Potion.NONE;
            mostValuablePotionRefinement = RefinementType.NONE;
        }

        isValidOrder =
                firstPotion != Potion.NONE && secondPotion != Potion.NONE && thirdPotion != Potion.NONE &&
                        firstPotionRefinement != RefinementType.NONE &&
                        secondPotionRefinement != RefinementType.NONE &&
                        thirdPotionRefinement != RefinementType.NONE &&
                        mostValuablePotion != Potion.NONE &&
                        mostValuablePotionRefinement != RefinementType.NONE;
    }

    public static MixologyOrder fromVarbits(Client client) {
        return new MixologyOrder(Potion.fromVarbitValue(client.getVarbitValue(MixologyVarbits.ORDER_FIRST_POTION)),
                Potion.fromVarbitValue(client.getVarbitValue(MixologyVarbits.ORDER_SECOND_POTION)),
                Potion.fromVarbitValue(client.getVarbitValue(MixologyVarbits.ORDER_THIRD_POTION)),
                RefinementType.fromVarbitValue(client.getVarbitValue(MixologyVarbits.ORDER_FIRST_POTION_REFINEMENT)),
                RefinementType.fromVarbitValue(client.getVarbitValue(MixologyVarbits.ORDER_SECOND_POTION_REFINEMENT)),
                RefinementType.fromVarbitValue(client.getVarbitValue(MixologyVarbits.ORDER_THIRD_POTION_REFINEMENT)),
                client.getBoostedSkillLevel(Skill.HERBLORE)
        );
    }

    public boolean doesEqual(MixologyOrder other) {
        return firstPotion == other.firstPotion &&
                secondPotion == other.secondPotion &&
                thirdPotion == other.thirdPotion &&
                firstPotionRefinement == other.firstPotionRefinement &&
                secondPotionRefinement == other.secondPotionRefinement &&
                thirdPotionRefinement == other.thirdPotionRefinement &&
                mostValuablePotion == other.mostValuablePotion &&
                mostValuablePotionRefinement == other.mostValuablePotionRefinement &&
                playerHerbloreLevel == other.playerHerbloreLevel;
    }
}
