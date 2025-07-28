package com.vasmatheus.mokhaiotlhelper;

import com.google.gson.annotations.SerializedName;
import lombok.EqualsAndHashCode;
import lombok.Value;
import net.runelite.client.party.messages.PartyMemberMessage;

@Value
@EqualsAndHashCode(callSuper = true)
public class NpcDamaged extends PartyMemberMessage {
  @SerializedName("i")
  int npcIndex;

  @SerializedName("d")
  int damage;
}
