package com.vasmatheus.easymixology.constants;

import com.google.common.collect.ImmutableList;
import java.util.List;

public class MixologyVarbits {
  public static final int MIXER_LEFT = 11324;
  public static final int MIXER_MIDDLE = 11325;
  public static final int MIXER_RIGHT = 11326;

  public static final int VESSEL = 11339;

  public static final int REFINER_ALEMBIC = 11328;
  public static final int REFINER_AGITATOR = 11329;
  public static final int REFINER_RETORT = 11327;

  public static final int ORDER_FIRST_POTION = 11315;
  public static final int ORDER_FIRST_POTION_REFINEMENT = 11316;

  public static final int ORDER_SECOND_POTION = 11317;
  public static final int ORDER_SECOND_POTION_REFINEMENT = 11318;

  public static final int ORDER_THIRD_POTION = 11319;
  public static final int ORDER_THIRD_POTION_REFINEMENT = 11320;

  public static final int PLAYER_MOX_COUNT = 4416;
  public static final int PLAYER_AGA_COUNT = 4415;
  public static final int PLAYER_LYE_COUNT = 4414;

  public static final int HOPPER_MOX_COUNT = 11431;
  public static final int HOPPER_AGA_COUNT = 11432;
  public static final int HOPPER_LYE_COUNT = 11433;

  public static final int AGITATOR_POTION_VARBIT = 11340;
  public static final int RETORT_POTION_VARBIT = 11341;
  public static final int ALEMBIC_POTION_VARBIT = 11342;

  private static final List<Integer> relevantVarbits =
      ImmutableList.of(
          MIXER_LEFT,
          MIXER_MIDDLE,
          MIXER_RIGHT,
          VESSEL,
          REFINER_ALEMBIC,
          REFINER_AGITATOR,
          REFINER_RETORT,
          ORDER_FIRST_POTION,
          ORDER_FIRST_POTION_REFINEMENT,
          ORDER_SECOND_POTION,
          ORDER_SECOND_POTION_REFINEMENT,
          ORDER_THIRD_POTION,
          ORDER_THIRD_POTION_REFINEMENT,
          PLAYER_MOX_COUNT,
          PLAYER_AGA_COUNT,
          PLAYER_LYE_COUNT,
          HOPPER_MOX_COUNT,
          HOPPER_AGA_COUNT,
          HOPPER_LYE_COUNT,
          AGITATOR_POTION_VARBIT,
          RETORT_POTION_VARBIT,
          ALEMBIC_POTION_VARBIT);

  // TODO: Reward varbits are -1?

  public static boolean isRelevantVarbit(int varbit) {
    return relevantVarbits.contains(varbit);
  }
}
