package com.vasmatheus.mokhaiotlhelper;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import net.runelite.api.NPC;

@Getter
public class DelveGrub {
  private static final int GRUB_HP = 2;

  private final NPC npc;
  private final int npcIndex;

  @Setter(AccessLevel.PACKAGE)
  private int hp = GRUB_HP;

  @Setter(AccessLevel.PACKAGE)
  private int queuedDamage;

  @Setter(AccessLevel.PACKAGE)
  private int hidden;

  public DelveGrub(NPC npc, int npcIndex) {
    this.npc = npc;
    this.npcIndex = npcIndex;
    this.queuedDamage = 0;
    this.hidden = 0;
  }
}
