package com.vasmatheus.easymixology.model.enums;

public enum PotionSelectionStrategy {
    ALL_POTIONS,
    NO_TRIPLES,
    ONLY_LYE_TRIPLES,
    NO_AGA_TRIPLES,
    NO_AGA_TRIPLES_UNLESS_MIXALOT,
    NO_AGA_MMM_TRIPLES_UNLESS_MIXALOT;

    @Override
    public String toString() {
        switch (this) {
            case ALL_POTIONS:
                return "Make all";
            case NO_TRIPLES:
                return "No triples";
            case ONLY_LYE_TRIPLES:
                return "No AAA/MMM";
            case NO_AGA_TRIPLES:
                return "No AAA";
            case NO_AGA_TRIPLES_UNLESS_MIXALOT:
                return "No AAA except when Mixalot";
            case NO_AGA_MMM_TRIPLES_UNLESS_MIXALOT:
                return "No AAA/MMM except when Mixalot";
            default:
                return name();
        }
    }
}
