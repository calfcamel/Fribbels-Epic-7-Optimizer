package com.fribbels.baili;

import com.fribbels.enums.Gear;
import com.fribbels.model.BailiInfo;
import com.fribbels.model.Item;

import java.util.List;

public class BailiCalc {

    public static double calcGearScore(final Item item) {
        double score = 0;
        score += item.getReforgedStats().getSpeed() * 2;
        score += item.getReforgedStats().getCritDamage() * 1.125;
        score += item.getReforgedStats().getCritRate() * 1.5;
        score += item.getReforgedStats().getAttackPercent();
        score += item.getReforgedStats().getDefensePercent();
        score += item.getReforgedStats().getHealthPercent();
        score += item.getReforgedStats().getEffectiveness();
        score += item.getReforgedStats().getEffectResistance();
        return score;
    }

    public static void calcBailiScore(final List<Item> items) {
        for (Item item : items) {
            if (item == null)
                continue;
            if (item.getReforgedStats() == null) {
                continue;
            }
            StringBuilder sb = new StringBuilder();
            BailiInfo bailiInfo = new BailiInfo();
            int score = 0;
            int speed = item.getReforgedStats().getSpeed();
            int firstSpeedScore = 0;
            if (item.getGear() != Gear.BOOTS && speed >= 22) {
                if (speed >= 27) {
                    firstSpeedScore = 20 * speed - 2 * 245;
                } else if (speed >= 25) {
                    firstSpeedScore = 10 * speed - 225;
                } else {
                    firstSpeedScore = 5 * speed - 5 * 21;
                }
                if (firstSpeedScore > 0) {
                    sb.append("一速").append(firstSpeedScore);
                    score += firstSpeedScore;
                    bailiInfo.setFirstSpeedScore(firstSpeedScore);
                }

            }
            bailiInfo.setScore(score);
            bailiInfo.setDetails(sb.toString());
            item.setBaili(bailiInfo);
        }
    }
}
