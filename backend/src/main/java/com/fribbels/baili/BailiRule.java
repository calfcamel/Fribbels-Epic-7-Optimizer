package com.fribbels.baili;

import com.fribbels.enums.Set;
import com.fribbels.model.Item;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

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
    public static final int DAC = 1 << 11;

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


    private int type;
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
    public static List<BailiRule> hitResistRules;
    public static List<BailiRule> tankHalfRules;

//    public static final int GEAR_NOT_BOOTS = GEAR_ALL & ~GEAR_BOOTS;

    public static int getSetType(Item item) {
        if (item.getSet() == null) {
            return 0;
        }
        return 1 << item.getSet().index;
    }

    public static int getGearType(Item item) {
        if (item.getGear() == null) {
            return 0;
        }
        return 1 << item.getGear().ordinal();
    }

    public static int getMainType(Item item) {
        if (item.getMain() == null) {
            return 0;
        }
        if (item.getMain().getType() == null) {
            return 0;
        }
        return 1 << item.getMain().getType().getIndex();
    }

    public static double ruleCalc(Item item, BailiRule rule) {
        double score;
        if ((rule.getSetType() & getSetType(item)) == 0) {
            return 0d;
        }
        if ((rule.getGearType() & getGearType(item)) == 0) {
            return 0d;
        }
        if ((rule.getGearType() & (GEAR_NECKLACE | GEAR_RING | GEAR_BOOTS)) > 0) {
            if ((rule.getMainType() & getMainType(item)) == 0) {
                return 0d;
            }
        }
        // 仅针对双效
        if (rule.getType() == 4) {
            // 主属性，套装，副属性 需要包含至少一条命中或抵抗
            int flag = 0;
            // 主属性
            if ((getMainType(item) & (EFFECTIVENESS | EFFECT_RESISTANCE)) > 0) {
                flag = 1;
            }
            // 套装
            if (flag == 0 && (getSetType(item) & (HIT_SET | RESIST_SET)) > 0) {
                flag = 1;
            }
            // 副属性
            if (flag == 0 && (item.getReforgedStats().getEffectiveness() > 0 || item.getReforgedStats().getEffectResistance() > 0)) {
                flag = 1;
            }
            // 不满足条件直接返回0
            if (flag == 0) {
                return 0d;
            }

            // 且攻击%不可与命中抵抗同时存在
            // 先只检查副属性
            if (item.getReforgedStats().getAttackPercent() > 0 && (item.getReforgedStats().getEffectiveness() > 0 || item.getReforgedStats().getEffectResistance() > 0)) {
                return 0d;
            }
        }
        if (rule.getValidProps() == SPEED) {
            score = BailiCalc.calcGearScore(item);
        } else {
            score = BailiCalc.calcGearScore(item, rule.getValidProps());
        }
        return rule.getCalcScoreFunc().apply(item, score);
    }

    public static final int DPS_SET = SPEED_SET | DESTRUCTION_SET | CRIT_SET | PENETRATION_SET | TORRENT_SET | COUNTER_SET | LIFESTEAL_SET | IMMUNITY_SET;
    public static final int DPS_VALID_PROPS = ATTACK | ATTACK_PERCENT | CRIT_RATE | CRIT_DAMAGE | SPEED;

    // 抗坦（坦克）
    // 速度
    //血 防
    //效抗
    //守护 反击 免疫 逆袭
    public static final int TANK_RESIST_SET = SPEED_SET | HEALTH_SET | DEFENSE_SET | RESIST_SET | PROTECTION_SET | COUNTER_SET | IMMUNITY_SET | REVERSAL_SET;
    // 有效属性
    // 生命%，生命，防御%，防御，速度，抵抗
    public static final int TANK_RESIST_VALID_PROPS = HEALTH | HEALTH_PERCENT | DEFENSE | DEFENSE_PERCENT | SPEED | EFFECT_RESISTANCE;

    // 纯肉（坦克）
    // 速度
    //血 防
    //守护 免疫 逆袭
    public static final int TANK_SET = SPEED_SET | HEALTH_SET | DEFENSE_SET | PROTECTION_SET | IMMUNITY_SET | REVERSAL_SET;
    // 有效属性
    // 生命%，生命，防御%，防御，速度
    public static final int TANK_VALID_PROPS = HEALTH | HEALTH_PERCENT | DEFENSE | DEFENSE_PERCENT | SPEED;

    // 命坦（双效）
    // 速度
    //血 防
    //效命
    //免疫
    public static final int TANK_HIT_SET = SPEED_SET | HEALTH_SET | DEFENSE_SET | HIT_SET | RESIST_SET | IMMUNITY_SET;
    // 有效属性
    // 生命%，生命，防御%，防御，速度，命中
    public static final int TANK_HIT_VALID_PROPS = HEALTH | HEALTH_PERCENT | DEFENSE | DEFENSE_PERCENT | SPEED | EFFECTIVENESS;


    // 双效
    // 速度
    //血 防
    //效命 效抗 反击 免疫
    public static final int HIT_RESIST_SET = SPEED_SET | HEALTH_SET | DEFENSE_SET | HIT_SET | RESIST_SET | COUNTER_SET | IMMUNITY_SET;

    // 有效属性
    // 速度，生命%，防御%，防御，抵抗，命中，攻击%
    //主属性，套装，副属性 需要包含至少一条命中或抵抗
    // 且攻击%不可与命中抵抗同时存在
    public static final int HIT_RESIST_VALID_PROPS = SPEED | HEALTH | HEALTH_PERCENT | DEFENSE | DEFENSE_PERCENT | EFFECTIVENESS | EFFECT_RESISTANCE | ATTACK_PERCENT;

    // 半肉（血防)
    // 生命 防御 速度 爆伤 反击 伤口 免疫 贯穿
    public static final int HALF_TANK_HP_DEF_SET = SPEED_SET | HEALTH_SET | DEFENSE_SET | CRIT_DAMAGE | COUNTER_SET | INJURY_SET | IMMUNITY_SET | PENETRATION_SET;
    // 有效属性
    // 暴率，爆伤，速度，生命%，防御%，防御
    public static final int HALF_TANK_HP_DEF_VALID_PROPS = CRIT_RATE | CRIT_DAMAGE | SPEED | HEALTH_PERCENT | DEFENSE | DEFENSE_PERCENT;

    // 半肉
    // 生命 防御 速度 暴击 爆伤 反击 伤口 吸血 免疫 贯穿 回击
    public static final int HALF_TANK_SET = SPEED_SET | HEALTH_SET | DEFENSE_SET | CRIT_SET | COUNTER_SET | INJURY_SET | LIFESTEAL_SET | IMMUNITY_SET | PENETRATION_SET | RIPOSTE_SET;
    // 有效属性
    // 攻击%，暴率，爆伤，速度，生命%，防御%，防御
    public static final int HALF_TANK_VALID_PROPS = ATTACK_PERCENT | CRIT_RATE | CRIT_DAMAGE | SPEED | HEALTH_PERCENT | DEFENSE | DEFENSE_PERCENT;

    public static final BiFunction<Item, Double, Double> HIT_RESIST_CALC_FUNC = (item, score) -> {
        if (score >= 75) {
            return 2 * (score - 73.5);
        }
        if (score >= 72) {
            return 2. / 3 * (score - 70.5);
        }
        return 0d;
    };

    static {
        speedRules = new ArrayList<>();
        speedRules.add(new BailiRule(1, SPEED_SET, SPEED, GEAR_ALL & ~GEAR_BOOTS, ALL_STATS, (item, score) -> {
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
        speedRules.add(new BailiRule(1, CRIT_SET | HEALTH_SET | DEFENSE_SET | IMMUNITY_SET | PENETRATION_SET | TORRENT_SET | HIT_SET | RESIST_SET,
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
        dpsRules.add(new BailiRule(2, DPS_SET, DPS_VALID_PROPS, GEAR_WEAPON | GEAR_HELMET, ALL_STATS, (item, score) -> {
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
        dpsRules.add(new BailiRule(2, DPS_SET, DPS_VALID_PROPS, GEAR_ARMOR, ALL_STATS, (item, score) -> {
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
        dpsRules.add(new BailiRule(2, DPS_SET, DPS_VALID_PROPS, GEAR_NECKLACE, CRIT_RATE | CRIT_DAMAGE, (item, score) -> {
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
        dpsRules.add(new BailiRule(2, DPS_SET, DPS_VALID_PROPS, GEAR_RING, ATTACK_PERCENT, (item, score) -> {
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
        dpsRules.add(new BailiRule(2, DPS_SET, DPS_VALID_PROPS, GEAR_BOOTS, SPEED | ATTACK_PERCENT, (item, score) -> {
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

        tankRules = new ArrayList<>();
        // 抗坦（坦克）
        tankRules.add(new BailiRule(3, TANK_RESIST_SET, TANK_RESIST_VALID_PROPS, GEAR_WEAPON, ALL_STATS, (item, score) -> {
            if (score >= 73) {
                return 3 * score - 201;
            }
            if (score >= 69) {
                return 2 * (score - 64);
            }
            if (score >= 63) {
                return 4. / 3 * (score - 61.5);
            }
            return 0d;
        }));
        tankRules.add(new BailiRule(3, TANK_RESIST_SET, TANK_RESIST_VALID_PROPS, GEAR_HELMET | GEAR_ARMOR, ALL_STATS, (item, score) -> {
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
        tankRules.add(new BailiRule(3, TANK_RESIST_SET, TANK_RESIST_VALID_PROPS, GEAR_NECKLACE, HEALTH_PERCENT | DEFENSE_PERCENT, (item, score) -> {
            if (score >= 73) {
                return 4 * score - 272.3;
            }
            if (score >= 68) {
                return 7. / 3 * (score - 64.5);
            }
            if (score >= 63) {
                return 4. / 3 * (score - 62);
            }
            return 0d;
        }));
        tankRules.add(new BailiRule(3, TANK_RESIST_SET, TANK_RESIST_VALID_PROPS, GEAR_RING, HEALTH_PERCENT | DEFENSE_PERCENT | EFFECT_RESISTANCE, (item, score) -> {
            if (score >= 73) {
                return 4 * score - 272.3;
            }
            if (score >= 68) {
                return 7. / 3 * (score - 64.5);
            }
            if (score >= 63) {
                return 4. / 3 * (score - 62);
            }
            return 0d;
        }));
        tankRules.add(new BailiRule(3, TANK_RESIST_SET, TANK_RESIST_VALID_PROPS, GEAR_BOOTS, HEALTH_PERCENT | DEFENSE_PERCENT | SPEED, (item, score) -> {
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
        // 纯肉（坦克）
        // 头盔				62-68	4/3*（装等-60.5）	68-72	2*（装等-63）	72+	3.5*装等-234
        // 衣服				62-68	4/3*（装等-60.5）	68-72	2*（装等-63）	72+	3.5*装等-234
        tankRules.add(new BailiRule(3, TANK_SET, TANK_VALID_PROPS, GEAR_HELMET | GEAR_ARMOR, ALL_STATS, (item, score) -> {
            if (score >= 72) {
                return 3.5 * score - 234;
            }
            if (score >= 68) {
                return 2 * (score - 63);
            }
            if (score >= 62) {
                return 4. / 3 * (score - 60.5);
            }
            return 0d;
        }));
        // 项链	生命%			58-64	1.5*（装等-56）	64-68	3*（装等-60）	68+	5*（装等-63.2）
        tankRules.add(new BailiRule(3, TANK_SET, TANK_VALID_PROPS, GEAR_NECKLACE, HEALTH_PERCENT, (item, score) -> {
            if (score >= 68) {
                return 5 * (score - 63.2);
            }
            if (score >= 64) {
                return 3 * (score - 60);
            }
            if (score >= 58) {
                return 1.5 * (score - 56);
            }
            return 0d;
        }));
        // 戒指	生命%			58-64	1.5*（装等-56）	64-68	3*（装等-60）	68+	5*（装等-63.2）
        tankRules.add(new BailiRule(3, TANK_SET, TANK_VALID_PROPS, GEAR_RING, HEALTH_PERCENT, (item, score) -> {
            if (score >= 68) {
                return 5 * (score - 63.2);
            }
            if (score >= 64) {
                return 3 * (score - 60);
            }
            if (score >= 58) {
                return 1.5 * (score - 56);
            }
            return 0d;
        }));
        // 鞋子	生命%，速度			58-64	1.5*（装等-56）	64-68	3*（装等-60）	68+	5*（装等-63.2）
        tankRules.add(new BailiRule(3, TANK_SET, TANK_VALID_PROPS, GEAR_BOOTS, HEALTH_PERCENT | SPEED, (item, score) -> {
            if (score >= 68) {
                return 5 * (score - 63.2);
            }
            if (score >= 64) {
                return 3 * (score - 60);
            }
            if (score >= 58) {
                return 1.5 * (score - 56);
            }
            return 0d;
        }));
        // 命坦（双效）
        // 武器	-	#VALUE!	0	62-68	4/3*（装等-60.5）	68-72	2*（装等-63）	72+	3.5*装等-234
        tankRules.add(new BailiRule(3, TANK_HIT_SET, TANK_HIT_VALID_PROPS, GEAR_WEAPON, ALL_STATS, (item, score) -> {
            if (score >= 72) {
                return 3.5 * score - 234;
            }
            if (score >= 68) {
                return 2 * (score - 63);
            }
            if (score >= 62) {
                return 4. / 3 * (score - 60.5);
            }
            return 0d;
        }));
        // 头盔	-	#VALUE!	0	68-74	4/3*（装等-67.25）	74-78	2*（装等-69.5）	78+	3.5*装等-256
        // 衣服	-	#VALUE!	0	68-74	4/3*（装等-67.25）	74-78	2*（装等-69.5）	78+	3.5*装等-256
        tankRules.add(new BailiRule(3, TANK_HIT_SET, TANK_HIT_VALID_PROPS, GEAR_HELMET | GEAR_ARMOR, ALL_STATS, (item, score) -> {
            if (score >= 78) {
                return 3.5 * score - 256;
            }
            if (score >= 74) {
                return 2 * (score - 69.5);
            }
            if (score >= 68) {
                return 4. / 3 * (score - 67.25);
            }
            return 0d;
        }));
        // 项链	生命%，防御%	#VALUE!	0	63-68	4/3*（装等-62）	68-74	7/3*（装等-64.5）	74+	4*装等-274
        tankRules.add(new BailiRule(3, TANK_HIT_SET, TANK_HIT_VALID_PROPS, GEAR_NECKLACE, HEALTH_PERCENT | DEFENSE_PERCENT, (item, score) -> {
            if (score >= 74) {
                return 4 * score - 274;
            }
            if (score >= 68) {
                return 7. / 3 * (score - 64.5);
            }
            if (score >= 63) {
                return 4. / 3 * (score - 62);
            }
            return 0d;
        }));
        // 戒指	生命%，防御%，命中	#VALUE!	0	63-68	4/3*（装等-62）	68-74	7/3*（装等-64.5）	74+	4*装等-274
        tankRules.add(new BailiRule(3, TANK_HIT_SET, TANK_HIT_VALID_PROPS, GEAR_RING, HEALTH_PERCENT | DEFENSE_PERCENT | EFFECTIVENESS, (item, score) -> {
            if (score >= 74) {
                return 4 * score - 274;
            }
            if (score >= 68) {
                return 7. / 3 * (score - 64.5);
            }
            if (score >= 63) {
                return 4. / 3 * (score - 62);
            }
            return 0d;
        }));
        // 鞋子	速度	#VALUE!	0	63-68	装等-62	68-74	7/3*（装等-64.5）	74+	4*装等-274
        tankRules.add(new BailiRule(3, TANK_HIT_SET, TANK_HIT_VALID_PROPS, GEAR_BOOTS, SPEED, (item, score) -> {
            if (score >= 74) {
                return 4 * score - 274;
            }
            if (score >= 68) {
                return 7. / 3 * (score - 64.5);
            }
            if (score >= 63) {
                return score - 62;
            }
            return 0d;
        }));

        // 双效
        // TODO: 搞不明白先跳过
        hitResistRules = new ArrayList<>();
        // 武器	-	#VALUE!	0	72-75	2/3*（装等-70.5）	75+	2*（装等-73.5）
        // 头盔	-	#VALUE!	0	72-75	2/3*（装等-70.5）	75+	2*（装等-73.5）
        // 衣服	-	#VALUE!	0	72-75	2/3*（装等-70.5）	75+	2*（装等-73.5）
        hitResistRules.add(new BailiRule(4, HIT_RESIST_SET, HIT_RESIST_VALID_PROPS, GEAR_WEAPON | GEAR_HELMET | GEAR_ARMOR, ALL_STATS, HIT_RESIST_CALC_FUNC));
        // 项链	生命%，防御%，攻击%	#VALUE!	0	72-75	2/3*（装等-70.5）	75+	2*（装等-73.5）
        // 戒指	生命%，防御%，攻击%，抵抗，命中	#VALUE!	0	72-75	2/3*（装等-70.5）	75+	2*（装等-73.5）
        hitResistRules.add(new BailiRule(4, HIT_RESIST_SET, HIT_RESIST_VALID_PROPS, GEAR_NECKLACE | GEAR_RING, HEALTH_PERCENT | DEFENSE_PERCENT | ATTACK_PERCENT | EFFECTIVENESS | EFFECT_RESISTANCE, HIT_RESIST_CALC_FUNC));
        // 鞋子	速度	#VALUE!	0	72-75	2/3*（装等-70.5）	75+	2*（装等-73.5）
        hitResistRules.add(new BailiRule(4, HIT_RESIST_SET, HIT_RESIST_VALID_PROPS, GEAR_BOOTS, SPEED, HIT_RESIST_CALC_FUNC));


        tankHalfRules = new ArrayList<>();
        // 半肉（血防)
        // 武器	-	#VALUE!	0	71-74	2/3*（装等-69.5）	74-78	2*（装等-72.5）	78+	3.5*装等-262
        // 头盔	-	#VALUE!	0	71-74	2/3*（装等-69.5）	74-78	2*（装等-72.5）	78+	3.5*装等-262
        // 衣服	-	#VALUE!	0	71-74	2/3*（装等-69.5）	74-78	2*（装等-72.5）	78+	3.5*装等-262
        tankHalfRules.add(new BailiRule(5, HALF_TANK_HP_DEF_SET, HALF_TANK_HP_DEF_VALID_PROPS, GEAR_WEAPON | GEAR_HELMET | GEAR_ARMOR, ALL_STATS, (item, score) -> {
            if (score >= 78) {
                return 3.5 * score - 262;
            }
            if (score >= 74) {
                return 2 * (score - 72.5);
            }
            if (score >= 71) {
                return 2. / 3 * (score - 69.5);
            }
            return 0d;
        }));
        // 项链	爆率，爆伤	#VALUE!	0	69-74	4/5*（装等-66.5）	74-78	2*（装等-71）	78+	4*（装等-74.5）
        tankHalfRules.add(new BailiRule(5, HALF_TANK_HP_DEF_SET, HALF_TANK_HP_DEF_VALID_PROPS, GEAR_NECKLACE, CRIT_RATE | CRIT_DAMAGE, (item, score) -> {
            if (score >= 78) {
                return 4 * (score - 74.5);
            }
            if (score >= 74) {
                return 2 * (score - 71);
            }
            if (score >= 69) {
                return 4. / 5 * (score - 66.5);
            }
            return 0d;
        }));
        // 戒指	生命%，防御%	#VALUE!	0	69-74	4/5*（装等-66.5）	74-78	2*（装等-71）	78+	4*（装等-74.5）
        tankHalfRules.add(new BailiRule(5, HALF_TANK_HP_DEF_SET, HALF_TANK_HP_DEF_VALID_PROPS, GEAR_RING, HEALTH_PERCENT | DEFENSE_PERCENT, (item, score) -> {
            if (score >= 78) {
                return 4 * (score - 74.5);
            }
            if (score >= 74) {
                return 2 * (score - 71);
            }
            if (score >= 69) {
                return 4. / 5 * (score - 66.5);
            }
            return 0d;
        }));
        // 鞋子	速度	#VALUE!	0	69-74	4/5*（装等-66.5）	74-78	2*（装等-71）	78+	4*（装等-74.5）
        tankHalfRules.add(new BailiRule(5, HALF_TANK_HP_DEF_SET, HALF_TANK_HP_DEF_VALID_PROPS, GEAR_BOOTS, SPEED, (item, score) -> {
            if (score >= 78) {
                return 4 * (score - 74.5);
            }
            if (score >= 74) {
                return 2 * (score - 71);
            }
            if (score >= 69) {
                return 4. / 5 * (score - 66.5);
            }
            return 0d;
        }));

        // 半肉
        // 武器	-	#VALUE!	0	72-75	2/3*（装等-70.5）	75+	2*（装等-73.5）
        //头盔	-	#VALUE!	0	72-75	2/3*（装等-70.5）	75+	2*（装等-73.5）
        //衣服	-	#VALUE!	0	72-75	2/3*（装等-70.5）	75+	2*（装等-73.5）
        tankHalfRules.add(new BailiRule(5, HALF_TANK_SET, HALF_TANK_VALID_PROPS, GEAR_WEAPON | GEAR_HELMET | GEAR_ARMOR, ALL_STATS, (item, score) -> {
            if (score >= 75) {
                return 2 * (score - 73.5);
            }
            if (score >= 72) {
                return 2. / 3 * (score - 70.5);
            }
            return 0d;
        }));
        //项链	爆率，爆伤，生命%，攻击%	#VALUE!	0	72-75	2/3*（装等-70.5）	75+	2*（装等-73.5）
        tankHalfRules.add(new BailiRule(5, HALF_TANK_SET, HALF_TANK_VALID_PROPS, GEAR_NECKLACE, CRIT_RATE | CRIT_DAMAGE | HEALTH_PERCENT | ATTACK_PERCENT, (item, score) -> {
            if (score >= 75) {
                return 2 * (score - 73.5);
            }
            if (score >= 72) {
                return 2. / 3 * (score - 70.5);
            }
            return 0d;
        }));
        //戒指	生命%，防御%，攻击%	#VALUE!	0	72-75	2/3*（装等-70.5）	75+	2*（装等-73.5）
        tankHalfRules.add(new BailiRule(5, HALF_TANK_SET, HALF_TANK_VALID_PROPS, GEAR_RING, HEALTH_PERCENT | DEFENSE_PERCENT | ATTACK_PERCENT, (item, score) -> {
            if (score >= 75) {
                return 2 * (score - 73.5);
            }
            if (score >= 72) {
                return 2. / 3 * (score - 70.5);
            }
            return 0d;
        }));
        //鞋子	速度	#VALUE!	0	72-75	2/3*（装等-70.5）	75+	2*（装等-73.5）
        tankHalfRules.add(new BailiRule(5, HALF_TANK_SET, HALF_TANK_VALID_PROPS, GEAR_BOOTS, SPEED, (item, score) -> {
            if (score >= 75) {
                return 2 * (score - 73.5);
            }
            if (score >= 72) {
                return 2. / 3 * (score - 70.5);
            }
            return 0d;
        }));
    }
}
