package com.example;

public enum RefinementType {
    AGITATOR(1),
    RETORT(2),
    ALEMBIC(3),
    NONE(0);

    public final int varbitValue;

    RefinementType(int varbitValue) {
        this.varbitValue = varbitValue;
    }

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
}
