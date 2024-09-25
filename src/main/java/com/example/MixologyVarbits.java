package com.example;

import java.util.Arrays;
import java.util.List;

public class MixologyVarbits {
    public final static int MIXER_LEFT = 11324;
    public final static int MIXER_MIDDLE = 11325;
    public final static int MIXER_RIGHT = 11326;

    public final static int VESSEL = 11339;

    public final static int REFINER_ALEMBIC = 11328;
    public final static int REFINER_AGITATOR = 11329;
    public final static int REFINER_RETORT = 11327;

    public final static int ORDER_FIRST_POTION = 11315;
    public final static int ORDER_FIRST_POTION_REFINEMENT = 11316;

    public final static int ORDER_SECOND_POTION = 11317;
    public final static int ORDER_SECOND_POTION_REFINEMENT = 11318;

    public final static int ORDER_THIRD_POTION = 11319;
    public final static int ORDER_THIRD_POTION_REFINEMENT = 11320;

    private final static List<Integer> relevantVarbits = Arrays.asList(
            MIXER_LEFT, MIXER_MIDDLE, MIXER_RIGHT,
            VESSEL,
            REFINER_ALEMBIC, REFINER_AGITATOR, REFINER_RETORT,
            ORDER_FIRST_POTION, ORDER_FIRST_POTION_REFINEMENT,
            ORDER_SECOND_POTION, ORDER_SECOND_POTION_REFINEMENT,
            ORDER_THIRD_POTION, ORDER_THIRD_POTION_REFINEMENT
    );

    // TODO: Reward varbits are -1?

    public static boolean isRelevantVarbit(int varbit) {
        return relevantVarbits.contains(varbit);
    }
}
