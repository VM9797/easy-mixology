package com.vasmatheus.easymixology.model;

import com.vasmatheus.easymixology.EasyMixologyConfig;
import com.vasmatheus.easymixology.constants.MixologyVarbits;
import com.vasmatheus.easymixology.model.enums.MixologyRewards;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.events.ChatMessage;
import org.apache.commons.lang3.tuple.Pair;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Singleton
@Slf4j
public class MixologyStats {
    @Inject
    private Client client;

    @Inject
    private MixologyStateMachine state;

    @Inject
    private EasyMixologyConfig config;

    @Getter
    private int hopperMoxCount = 0;

    @Getter
    private int hopperAgaCount = 0;

    @Getter
    private int hopperLyeCount = 0;

    @Getter
    private int playerMoxCount = -1;

    @Getter
    private int playerAgaCount = -1;

    @Getter
    private int playerLyeCount = -1;

    @Getter
    private int sessionMoxCount = 0;

    @Getter
    private int sessionAgaCount = 0;

    @Getter
    private int sessionLyeCount = 0;

    @Getter
    private boolean arePlayerCountsLoaded = false;

    @Getter
    private int targetMox = 0;

    @Getter
    private int targetAga = 0;

    @Getter
    private int targetLye = 0;

    @Getter
    private int targetMoxPercent = 0;

    @Getter
    private int targetAgaPercent = 0;

    @Getter
    private int targetLyePercent = 0;



    public void updateVarbits() {
        this.hopperMoxCount = client.getVarbitValue(MixologyVarbits.HOPPER_MOX_COUNT);
        this.hopperAgaCount = client.getVarbitValue(MixologyVarbits.HOPPER_AGA_COUNT);
        this.hopperLyeCount = client.getVarbitValue(MixologyVarbits.HOPPER_LYE_COUNT);
        updateTargets();
    }

    public void processChatMessage(ChatMessage event) {
        var message = event.getMessage();
        if (state.isStarted() &&
                event.getType() == ChatMessageType.GAMEMESSAGE &&
                message.contains("You are rewarded") &&
                message.contains("You now have")) {
            try {
                var playerRewardCounts = parseMatchingMessage(message);
                var turnInCounts = playerRewardCounts.getLeft();
                var totalCounts = playerRewardCounts.getRight();

                if (totalCounts.size() == 3) {
                    playerMoxCount = totalCounts.get(0);
                    playerAgaCount = totalCounts.get(1);
                    playerLyeCount = totalCounts.get(2);
                    arePlayerCountsLoaded = true;
                }

                if (turnInCounts.size() == 3) {
                    sessionMoxCount += turnInCounts.get(0);
                    sessionAgaCount += turnInCounts.get(1);
                    sessionLyeCount += turnInCounts.get(2);
                }
                updateTargets();
            } catch (Exception e) {
                // Failed to parse the message, leave it for now
            }
        }
    }

    public void onConfigChanged() {
        updateTargets();
    }

    private void updateTargets() {
        ArrayList<MixologyRewards> selectedRewards = new ArrayList<>();
        targetMox = 0;
        targetAga = 0;
        targetLye = 0;

        if (config.prescriptionGoggles()) {
            selectedRewards.add(MixologyRewards.PrescriptionGoggles);
        }
        if (config.alchemistLabcoat()) {
            selectedRewards.add(MixologyRewards.AlchemistLabcoat);
        }
        if (config.alchemistPants()) {
            selectedRewards.add(MixologyRewards.AlchemistPants);
        }
        if (config.alchemistGloves()) {
            selectedRewards.add(MixologyRewards.AlchemistGloves);
        }
        if (config.reagentPouch()) {
            selectedRewards.add(MixologyRewards.ReagentPouch);
        }
        if (config.potionStorage()) {
            selectedRewards.add(MixologyRewards.PotionStorage);
        }
        if (config.chuggingBarrel()) {
            selectedRewards.add(MixologyRewards.ChuggingBarrel);
        }
        if (config.alchemistsAmulet()) {
            selectedRewards.add(MixologyRewards.AlchemistsAmulet);
        }

        for (var reward : selectedRewards) {
            targetMox += reward.moxCost;
            targetAga += reward.agaCost;
            targetLye += reward.lyeCost;
        }

        targetMox += config.apprenticePotionPackCount() * MixologyRewards.ApprenticePotionPack.moxCost;
        targetAga += config.apprenticePotionPackCount() * MixologyRewards.ApprenticePotionPack.agaCost;
        targetLye += config.apprenticePotionPackCount() * MixologyRewards.ApprenticePotionPack.lyeCost;

        targetMox += config.adeptPotionPackCount() * MixologyRewards.AdeptPotionPack.moxCost;
        targetAga += config.adeptPotionPackCount() * MixologyRewards.AdeptPotionPack.agaCost;
        targetLye += config.adeptPotionPackCount() * MixologyRewards.AdeptPotionPack.lyeCost;

        targetMox += config.expertPotionPackCount() * MixologyRewards.ExpertPotionPack.moxCost;
        targetAga += config.expertPotionPackCount() * MixologyRewards.ExpertPotionPack.agaCost;
        targetLye += config.expertPotionPackCount() * MixologyRewards.ExpertPotionPack.lyeCost;

        targetMox += config.aldariumCount() * MixologyRewards.Aldarium.moxCost;
        targetAga += config.aldariumCount() * MixologyRewards.Aldarium.agaCost;
        targetLye += config.aldariumCount() * MixologyRewards.Aldarium.lyeCost;


        targetMoxPercent = Integer.min((int)(100.0 * ((double)playerMoxCount / (double)targetMox)), 100);
        targetAgaPercent = Integer.min((int)(100.0 * ((double)playerAgaCount / (double)targetAga)), 100);
        targetLyePercent = Integer.min((int)(100.0 * ((double)playerLyeCount / (double)targetLye)), 100);
    }

    private static Pair<List<Integer>, List<Integer>> parseMatchingMessage(String message) {
        ArrayList<Integer> totalCount = new ArrayList<>();
        ArrayList<Integer> turnInCount = new ArrayList<>();
        Pair<List<Integer>, List<Integer>> result = Pair.of(turnInCount, totalCount);
        String regex = "<col=[^>]+>([\\d,]+)</col>";
        Pattern pattern = Pattern.compile(regex);
        var splitMessage = message.split("You now have");

        int i = 0;
        for (var messagePart : splitMessage) {
            Matcher matcher = pattern.matcher(messagePart);

            while (matcher.find()) {
                String numberStr = matcher.group(1);
                numberStr = numberStr.replace(",", "");
                int number = Integer.parseInt(numberStr);
                (i == 0 ? turnInCount : totalCount).add(number);
            }
            i++;
        }

        return result;
    }


}
