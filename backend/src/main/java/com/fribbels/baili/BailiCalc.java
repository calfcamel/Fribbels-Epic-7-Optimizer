package com.fribbels.baili;

import com.fribbels.enums.Gear;
import com.fribbels.model.BailiInfo;
import com.fribbels.model.Item;

import java.util.List;

public class BailiCalc {

    static final double atkValue = 3.46 / 39;
    static final double defValue = 4.99 / 31;
    static final double hpValue = 3.09 / 174;

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
        score += item.getReforgedStats().getAttack() * atkValue;
        score += item.getReforgedStats().getDefense() * defValue;
        score += item.getReforgedStats().getHealth() * hpValue;
        return score;
    }

    public static double calcGearScore(final Item item, final int validMask) {
        double score = 0;
        if ((validMask & BailiRule.SPEED) > 0) {
            score += item.getReforgedStats().getSpeed() * 2;
        }
        if ((validMask & BailiRule.CRIT_DAMAGE) > 0) {
            score += item.getReforgedStats().getCritDamage() * 1.125;
        }

        if ((validMask & BailiRule.CRIT_RATE) > 0) {
            score += item.getReforgedStats().getCritRate() * 1.5;

        }
        if ((validMask & BailiRule.ATTACK_PERCENT) > 0) {
            score += item.getReforgedStats().getAttackPercent();

        }
        if ((validMask & BailiRule.DEFENSE_PERCENT) > 0) {
            score += item.getReforgedStats().getDefensePercent();

        }
        if ((validMask & BailiRule.HEALTH_PERCENT) > 0) {
            score += item.getReforgedStats().getHealthPercent();

        }
        if ((validMask & BailiRule.EFFECTIVENESS) > 0) {
            score += item.getReforgedStats().getEffectiveness();

        }
        if ((validMask & BailiRule.EFFECT_RESISTANCE) > 0) {
            score += item.getReforgedStats().getEffectResistance();
        }
        if ((validMask & BailiRule.ATTACK) > 0) {
            score += item.getReforgedStats().getAttack() * atkValue;
        }
        if ((validMask & BailiRule.DEFENSE) > 0) {
            score += item.getReforgedStats().getDefense() * defValue;
        }
        if ((validMask & BailiRule.HEALTH) > 0) {
            score += item.getReforgedStats().getHealth() * hpValue;
        }
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
