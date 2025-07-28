package com.vasmatheus.easymixology;

import com.google.common.collect.ImmutableList;
import com.vasmatheus.easymixology.constants.MixologyIDs;
import com.vasmatheus.easymixology.model.MixologyStateMachine;
import com.vasmatheus.easymixology.model.enums.MixologyState;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.runelite.api.Client;
import net.runelite.api.MenuAction;
import net.runelite.api.ObjectComposition;

@Singleton
public class EasyMixologyLeftClickSwapper {
  @Inject private EasyMixologyConfig config;

  @Inject private Client client;

  @Inject private MixologyStateMachine state;

  private static final List<MenuAction> OBJECT_MENU_TYPES =
      ImmutableList.of(
          MenuAction.GAME_OBJECT_FIRST_OPTION,
          MenuAction.GAME_OBJECT_SECOND_OPTION,
          MenuAction.GAME_OBJECT_THIRD_OPTION,
          MenuAction.GAME_OBJECT_FOURTH_OPTION,
          MenuAction.GAME_OBJECT_FIFTH_OPTION);

  public void updateLeftClickOptions() {
    if (!config.isLeftClickSwapForAgitatorWhenDepositEnabled()
        || client.isMenuOpen()
        || !state.isStarted()
        || state.getState() != MixologyState.READY_TO_DEPOSIT) {
      return;
    }

    for (var menuEntry : client.getMenuEntries()) {
      var type = menuEntry.getType();

      if (!OBJECT_MENU_TYPES.contains(type)) {
        continue;
      }

      // Get multiloc id
      int objectId = menuEntry.getIdentifier();
      ObjectComposition objectComposition = client.getObjectDefinition(objectId);
      if (objectComposition.getImpostorIds() != null) {
        objectComposition = objectComposition.getImpostor();
        objectId = objectComposition.getId();
      }

      if (objectId != MixologyIDs.AGITATOR && menuEntry.getIdentifier() != MixologyIDs.AGITATOR) {
        continue;
      }

      menuEntry.setDeprioritized(true);
    }
  }
}
