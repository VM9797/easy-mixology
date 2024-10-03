package com.vasmatheus.easymixology.model;

import com.vasmatheus.easymixology.constants.MixologyVarbits;
import com.vasmatheus.easymixology.model.enums.Potion;
import com.vasmatheus.easymixology.model.enums.PotionSelectionStrategy;
import com.vasmatheus.easymixology.model.enums.RefinementType;
import lombok.Getter;
import net.runelite.api.Client;
import net.runelite.api.Skill;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MixologyOrder {
    public static final MixologyOrder EMPTY = new MixologyOrder(Potion.NONE, Potion.NONE, Potion.NONE, RefinementType.NONE,
            RefinementType.NONE, RefinementType.NONE, 60, PotionSelectionStrategy.REWARD_SHOP_BALANCE);

    private final Potion firstPotion;
    private final Potion secondPotion;
    private final Potion thirdPotion;

    private final RefinementType firstPotionRefinement;
    private final RefinementType secondPotionRefinement;
    private final RefinementType thirdPotionRefinement;
    public final List<Potion> potions = new ArrayList<>();
    public final List<RefinementType> refinementTypes = new ArrayList<>();

    public final boolean isValidOrder;

    @Getter
    private int numberOfPotionsToDo = 0;

    private final int playerHerbloreLevel;
    private final PotionSelectionStrategy strategy;


    public MixologyOrder(Potion firstPotion, Potion secondPotion, Potion thirdPotion, RefinementType firstPotionRefinement,
                         RefinementType secondPotionRefinement, RefinementType thirdPotionRefinement, int playerHerbloreLevel,
                         PotionSelectionStrategy strategy) {
        this.firstPotion = firstPotion;
        this.secondPotion = secondPotion;
        this.thirdPotion = thirdPotion;
        this.firstPotionRefinement = firstPotionRefinement;
        this.secondPotionRefinement = secondPotionRefinement;
        this.thirdPotionRefinement = thirdPotionRefinement;
        this.playerHerbloreLevel = playerHerbloreLevel;
        this.strategy = strategy;

        var potions = Stream.of(Pair.of(firstPotion, firstPotionRefinement),
                Pair.of(secondPotion,
                        secondPotionRefinement),
                Pair.of(thirdPotion, thirdPotionRefinement))
                .filter(it -> !(it.getLeft().isAllMox || it.getLeft().isAllAga))
                .sorted(Comparator.comparingInt(a -> a.getRight().orderValue))
                .collect(Collectors.toList());

        if (potions.isEmpty()) {
            this.potions.add(firstPotion);
            this.refinementTypes.add(firstPotionRefinement);
            numberOfPotionsToDo = 1;
        } else {
            for (var potion : potions) {
                this.potions.add(potion.getLeft());
                this.refinementTypes.add(potion.getRight());
                numberOfPotionsToDo++;
            }
        }

        isValidOrder =
                firstPotion != Potion.NONE && secondPotion != Potion.NONE && thirdPotion != Potion.NONE &&
                        firstPotionRefinement != RefinementType.NONE &&
                        secondPotionRefinement != RefinementType.NONE &&
                        thirdPotionRefinement != RefinementType.NONE;
    }

    public static MixologyOrder fromVarbits(Client client, PotionSelectionStrategy strategy) {
        return new MixologyOrder(Potion.fromVarbitValue(client.getVarbitValue(MixologyVarbits.ORDER_FIRST_POTION)),
                Potion.fromVarbitValue(client.getVarbitValue(MixologyVarbits.ORDER_SECOND_POTION)),
                Potion.fromVarbitValue(client.getVarbitValue(MixologyVarbits.ORDER_THIRD_POTION)),
                RefinementType.fromVarbitValue(client.getVarbitValue(MixologyVarbits.ORDER_FIRST_POTION_REFINEMENT)),
                RefinementType.fromVarbitValue(client.getVarbitValue(MixologyVarbits.ORDER_SECOND_POTION_REFINEMENT)),
                RefinementType.fromVarbitValue(client.getVarbitValue(MixologyVarbits.ORDER_THIRD_POTION_REFINEMENT)),
                client.getBoostedSkillLevel(Skill.HERBLORE),
                strategy
        );
    }

    // TODO: Use lombok to generate equals
    public boolean doesEqual(MixologyOrder other) {
        return firstPotion == other.firstPotion &&
                secondPotion == other.secondPotion &&
                thirdPotion == other.thirdPotion &&
                firstPotionRefinement == other.firstPotionRefinement &&
                secondPotionRefinement == other.secondPotionRefinement &&
                thirdPotionRefinement == other.thirdPotionRefinement &&
                playerHerbloreLevel == other.playerHerbloreLevel &&
                strategy == other.strategy;
    }
}
