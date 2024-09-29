package com.vasmatheus.easymixology.model.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum RefinementType {
    AGITATOR("Agitator", 1, 2),
    RETORT("Retort", 2, 3),
    ALEMBIC("Alembic", 3, 1),
    NONE("", 0, 0);

    public final String displayName;
    public final int varbitValue;
    public final int orderValue;

    public static RefinementType fromVarbitValue(int varbitValue) {
        switch (varbitValue) {
            case 1:
                return AGITATOR;
            case 2:
                return RETORT;
            case 3:
                return ALEMBIC;
            default:
                return NONE;
        }
    }

    @Override
    public String toString() {
        return displayName;
    }
}
