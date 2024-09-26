package com.vasmatheus.easymixology.model;

import com.vasmatheus.easymixology.constants.MixologyVarbits;
import com.vasmatheus.easymixology.model.enums.Potion;
import com.vasmatheus.easymixology.model.enums.PotionComponent;
import net.runelite.api.Client;

public class MixologyVariablesSnapshot {
    public static final MixologyVariablesSnapshot EMPTY = new MixologyVariablesSnapshot(
            Potion.NONE,
            0,
            0,
            0,
            PotionComponent.NONE,
            PotionComponent.NONE,
            PotionComponent.NONE
    );

    public final Potion potionInVessel;
    public final int agitatorLevel;
    public final int retortLevel;
    public final int alembicLevel;
    public final PotionComponent componentInFirstMixer;
    public final PotionComponent componentInSecondMixer;
    public final PotionComponent componentInThirdMixer;


    private MixologyVariablesSnapshot(Potion potionInVessel, int agitatorLevel, int retortLevel, int alembicLevel,
                                      PotionComponent componentInFirstMixer, PotionComponent componentInSecondMixer,
                                      PotionComponent componentInThirdMixer) {
        this.potionInVessel = potionInVessel;
        this.agitatorLevel = agitatorLevel;
        this.retortLevel = retortLevel;
        this.alembicLevel = alembicLevel;
        this.componentInFirstMixer = componentInFirstMixer;
        this.componentInSecondMixer = componentInSecondMixer;
        this.componentInThirdMixer = componentInThirdMixer;
    }

    @Override
    public String toString() {
        return "MixologyVariablesSnapshot{" +
                "potionInVessel=" + potionInVessel +
                ", agitatorLevel=" + agitatorLevel +
                ", retortLevel=" + retortLevel +
                ", alembicLevel=" + alembicLevel +
                ", componentInFirstMixer=" + componentInFirstMixer +
                ", componentInSecondMixer=" + componentInSecondMixer +
                ", componentInThirdMixer=" + componentInThirdMixer +
                '}';
    }

    public static MixologyVariablesSnapshot fromVarbits(Client client) {
        return new MixologyVariablesSnapshot(
                Potion.fromVarbitValue(client.getVarbitValue(MixologyVarbits.VESSEL)),
                client.getVarbitValue(MixologyVarbits.REFINER_AGITATOR),
                client.getVarbitValue(MixologyVarbits.REFINER_RETORT),
                client.getVarbitValue(MixologyVarbits.REFINER_ALEMBIC),
                PotionComponent.fromVarbitValue(client.getVarbitValue(MixologyVarbits.MIXER_LEFT)),
                PotionComponent.fromVarbitValue(client.getVarbitValue(MixologyVarbits.MIXER_MIDDLE)),
                PotionComponent.fromVarbitValue(client.getVarbitValue(MixologyVarbits.MIXER_RIGHT))
        );
    }
}
