package com.vasmatheus.easymixology.model.enums;

public enum PotionSelectionStrategy {
    ALL_POTIONS,
    NO_TRIPLES,
    ONLY_LYE_TRIPLES,
    NO_AGA_TRIPLES,
    NO_AGA_TRIPLES_UNLESS_MIXALOT;

    @Override
    public String toString() {
        switch (this) {
            case ALL_POTIONS:
                return "Always 3 potions";
            case NO_TRIPLES:
                return "No 3 component potions";
            case ONLY_LYE_TRIPLES:
                return "Only keep LLL triple potions";
            case NO_AGA_TRIPLES:
                return "Only discard AAA triple potions";
            case NO_AGA_TRIPLES_UNLESS_MIXALOT:
                return "Only discard AAA triple potions, unless mixalot is in order";
            default:
                return name();
        }
    }
}
