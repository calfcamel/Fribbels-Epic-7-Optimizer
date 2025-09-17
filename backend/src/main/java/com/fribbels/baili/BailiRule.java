package com.fribbels.baili;

import com.fribbels.enums.Set;
import com.fribbels.model.Item;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

@Getter
@Setter
@AllArgsConstructor
public class BailiRule {
    public static final int ATTACK = 1;
    public static final int HEALTH = 2;
    public static final int DEFENSE = 1 << 2;
    public static final int ATTACK_PERCENT = 1 << 3;
    public static final int HEALTH_PERCENT = 1 << 4;
    public static final int DEFENSE_PERCENT = 1 << 5;
    public static final int CRIT_RATE = 1 << 6;
    public static final int CRIT_DAMAGE = 1 << 7;
    public static final int EFFECTIVENESS = 1 << 8;
    public static final int EFFECT_RESISTANCE = 1 << 9;
    public static final int SPEED = 1 << 10;
//    public static final int DAC = 1 << 11;

    public static final int ALL_STATS = (1 << 12) - 1;

    public static final int GEAR_WEAPON = 1;        // 武器
    public static final int GEAR_HELMET = 1 << 1;   // 头盔
    public static final int GEAR_ARMOR = 1 << 2;    // 护甲
    public static final int GEAR_NECKLACE = 1 << 3; // 项链
    public static final int GEAR_RING = 1 << 4;     // 戒指
    public static final int GEAR_BOOTS = 1 << 5;    // 靴子

    public static final int GEAR_ALL = (1 << 6) - 1;

    public static final int HEALTH_SET = 1 << Set.HEALTH.getIndex();
    public static final int DEFENSE_SET = 1 << Set.DEFENSE.getIndex();
    public static final int SPEED_SET = 1 << Set.SPEED.getIndex();
    public static final int CRIT_SET = 1 << Set.CRIT.getIndex();
    public static final int HIT_SET = 1 << Set.HIT.getIndex();
    public static final int DESTRUCTION_SET = 1 << Set.DESTRUCTION.getIndex();
    public static final int LIFESTEAL_SET = 1 << Set.LIFESTEAL.getIndex();
    public static final int COUNTER_SET = 1 << Set.COUNTER.getIndex();
    public static final int RESIST_SET = 1 << Set.RESIST.getIndex();
    public static final int UNITY_SET = 1 << Set.UNITY.getIndex();
    public static final int RAGE_SET = 1 << Set.RAGE.getIndex();
    public static final int IMMUNITY_SET = 1 << Set.IMMUNITY.getIndex();
    public static final int PENETRATION_SET = 1 << Set.PENETRATION.getIndex();
    public static final int REVENGE_SET = 1 << Set.REVENGE.getIndex();
    public static final int INJURY_SET = 1 << Set.INJURY.getIndex();
    public static final int PROTECTION_SET = 1 << Set.PROTECTION.getIndex();
    public static final int TORRENT_SET = 1 << Set.TORRENT.getIndex();
    public static final int REVERSAL_SET = 1 << Set.REVERSAL.getIndex();
    public static final int RIPOSTE_SET = 1 << Set.RIPOSTE.getIndex();

    public static final int SET_ALL = (1 << 20) - 1;

    public static final int ALL = (1 << 30) - 1;

    /**
     * 套装
     */
    private int setType;
    /**
     * 有效属性
     */
    private int validProps;

    /**
     * 部位
     */
    private int gearType;

    /**
     * 主属性
     */
    private int mainType;

    BiFunction<Item, Double, Double> calcScoreFunc;

    public static List<BailiRule> speedRules;
    public static List<BailiRule> dpsRules;
    public static List<BailiRule> tankRules;

//    public static final int GEAR_NOT_BOOTS = GEAR_ALL & ~GEAR_BOOTS;

    public static double ruleCalc(Item item, BailiRule rule) {
        double score;
        if (rule.getValidProps() == SPEED) {
            score = BailiCalc.calcGearScore(item);
        } else {
            score = BailiCalc.calcGearScore(item, rule.getValidProps());
        }
        return rule.getCalcScoreFunc().apply(item, score);
    }

    public static final int DPS_SET = SPEED_SET | DESTRUCTION_SET | CRIT_SET | PENETRATION_SET | TORRENT_SET | COUNTER_SET | LIFESTEAL_SET | IMMUNITY_SET;
    public static final int DPS_VALID_PROPS = ATTACK | ATTACK_PERCENT | CRIT_RATE | CRIT_DAMAGE | SPEED;

    static {
        speedRules = new ArrayList<>();
        speedRules.add(new BailiRule(SPEED_SET, SPEED, GEAR_ALL & ~GEAR_BOOTS, ALL_STATS, (item, score) -> {
            if (item.getReforgedStats().getSpeed() >= 18) {
                if (score >= 78) {
                    return 4 * (score - 72.75);
                }
                if (score >= 73) {
                    return 2 * (score - 66.5);
                }
                if (score >= 65) {
                    return score - 60;
                }
            }
            return 0d;
        }));
        speedRules.add(new BailiRule(CRIT_SET | HEALTH_SET | DEFENSE_SET | IMMUNITY_SET | PENETRATION_SET | TORRENT_SET | HIT_SET | RESIST_SET,
            SPEED, GEAR_ALL & ~GEAR_BOOTS, ALL_STATS, (item, score) -> {
            if (item.getReforgedStats().getSpeed() >= 18) {
                if (score >= 75) {
                    return score - 69;
                }
                if (score >= 65) {
                    return .8 * (score - 67.5);
                }
            }
            return 0d;
        }));

        dpsRules = new ArrayList<>();
        dpsRules.add(new BailiRule(DPS_SET, DPS_VALID_PROPS, GEAR_WEAPON | GEAR_HELMET, ALL_STATS, (item, score) -> {
            if (score >= 79) {
                return 3 * score - 220;
            }
            if (score >= 75) {
                return 2 * (score - 70.5);
            }
            if (score >= 69) {
                return 4. / 3 * (score - 68.25);
            }
            return 0d;
        }));
        dpsRules.add(new BailiRule(DPS_SET, DPS_VALID_PROPS, GEAR_ARMOR, ALL_STATS, (item, score) -> {
            if (score >= 72) {
                return 3 * score - 220;
            }
            if (score >= 66) {
                return 2 * (score - 64);
            }
            if (score >= 63) {
                return 4. / 3 * (score - 61.5);
            }
            return 0d;
        }));
        dpsRules.add(new BailiRule(DPS_SET, DPS_VALID_PROPS, GEAR_NECKLACE, CRIT_RATE | CRIT_DAMAGE, (item, score) -> {
            if (score >= 73) {
                return 4 * score - 272.3;
            }
            if (score >= 68) {
                return 7. / 3 * (score - 64.5);
            }
            if (score >= 65) {
                return 4. / 3 * (score - 62);
            }
            return 0d;
        }));
        dpsRules.add(new BailiRule(DPS_SET, DPS_VALID_PROPS, GEAR_RING,  ATTACK_PERCENT, (item, score) -> {
            if (score >= 73) {
                return 4 * score - 272.3;
            }
            if (score >= 68) {
                return 7. / 3 * (score - 64.5);
            }
            if (score >= 65) {
                return 4. / 3 * (score - 62);
            }
            return 0d;
        }));
        dpsRules.add(new BailiRule(DPS_SET, DPS_VALID_PROPS, GEAR_BOOTS, SPEED | ATTACK_PERCENT, (item, score) -> {
            if (score >= 73) {
                return 4 * score - 272.3;
            }
            if (score >= 68) {
                return 7. / 3 * (score - 64.5);
            }
            if (score >= 63) {
                return score - 62;
            }
            return 0d;
        }));
    }
}
