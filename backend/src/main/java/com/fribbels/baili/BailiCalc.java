package com.fribbels.baili;

import com.fribbels.enums.Gear;
import com.fribbels.model.BailiInfo;
import com.fribbels.model.Item;
import com.google.gson.Gson;

import java.util.ArrayList;
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

    public static List<BailiInfo> calcBailiScore(final List<Item> items) {
        List<BailiInfo> bailiInfos = new ArrayList<>();
        for (final Item item : items) {
            BailiInfo bailiInfo = new BailiInfo();
            bailiInfos.add(bailiInfo);
            try {
                if (item == null) {
                    continue;
                }
                if (item.getReforgedStats() == null) {
                    continue;
                }
                StringBuilder sb = new StringBuilder();
                int score = 0;
                // 一速
                final int speed = item.getReforgedStats().getSpeed();
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
                // 速度
                for (final BailiRule rule : BailiRule.speedRules) {
                    final double speedScore = BailiRule.ruleCalc(item, rule);
                    if (speedScore > bailiInfo.getSpeedScore()) {
                        // 四舍五入
                        bailiInfo.setSpeedScore((int) Math.round(speedScore));
                    }
                }
                if (bailiInfo.getSpeedScore() > 0) {
                    if (sb.length() > 0) {
                        sb.append(" ");
                    }
                    score += bailiInfo.getSpeedScore();
                    sb.append("速度").append(bailiInfo.getSpeedScore());
                }
                final int scoreCache = score;
                // 输出
                for (final BailiRule rule : BailiRule.dpsRules) {
                    final double dpsScore = BailiRule.ruleCalc(item, rule);
                    if (dpsScore > bailiInfo.getDpsScore()) {
                        bailiInfo.setDpsScore((int) Math.round(dpsScore));
                    }
                }
                if (bailiInfo.getDpsScore() > 0) {
                    if (sb.length() > 0) {
                        sb.append(" ");
                    }
                    score += bailiInfo.getDpsScore();
                    sb.append("输出").append(bailiInfo.getDpsScore());
                }

                // 坦克
                if (score == scoreCache) {
                    for (final BailiRule rule : BailiRule.tankRules) {
                        final double tankScore = BailiRule.ruleCalc(item, rule);
                        if (tankScore > bailiInfo.getTankScore()) {
                            bailiInfo.setTankScore((int) Math.round(tankScore));
                        }
                    }
                    if (bailiInfo.getTankScore() > 0) {
                        if (sb.length() > 0) {
                            sb.append(" ");
                        }
                        score += bailiInfo.getTankScore();
                        sb.append("坦克").append(bailiInfo.getTankScore());
                    }
                }

                // 双效
                if (score == scoreCache) {
                    for (final BailiRule rule : BailiRule.hitResistRules) {
                        final double hitResistScore = BailiRule.ruleCalc(item, rule);
                        if (hitResistScore > bailiInfo.getHitResistScore()) {
                            bailiInfo.setHitResistScore((int) Math.round(hitResistScore));
                        }
                    }
                    if (bailiInfo.getHitResistScore() > 0) {
                        if (sb.length() > 0) {
                            sb.append(" ");
                        }
                        score += bailiInfo.getHitResistScore();
                        sb.append("双效").append(bailiInfo.getHitResistScore());
                    }
                }

                // 半肉
                if (score == scoreCache) {
                    for (final BailiRule rule : BailiRule.tankHalfRules) {
                        final double tankHalfScore = BailiRule.ruleCalc(item, rule);
                        if (tankHalfScore > bailiInfo.getTankHalfScore()) {
                            bailiInfo.setTankHalfScore((int) Math.round(tankHalfScore));
                        }
                    }
                    if (bailiInfo.getTankHalfScore() > 0) {
                        if (sb.length() > 0) {
                            sb.append(" ");
                        }
                        score += bailiInfo.getTankHalfScore();
                        sb.append("半肉").append(bailiInfo.getTankHalfScore());
                    }
                }

                // 未来可期
                // 75+	2/3*（装等-73.5）
                bailiInfo.setGearScore(calcGearScore(item));
                if (score == 0 && bailiInfo.getGearScore() > 75) {
                    final int futureScore = (int) Math.round(2.0 / 3 * (bailiInfo.getGearScore() - 73.5));
                    score += futureScore;
                    bailiInfo.setFutureScore(futureScore);
                    sb.append("未来可期").append(futureScore);
                }

                bailiInfo.setScore(score);
                bailiInfo.setDetails(sb.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return bailiInfos;
    }

    public static void main(String[] args) {
        String s = "{\n    \"gear\": \"Weapon\",\n    \"rank\": \"Epic\",\n    \"set\": \"CriticalSet\",\n    \"enhance\": 15,\n    \"level\": 90,\n    \"main\": {\n        \"type\": \"Attack\",\n        \"value\": 525,\n        \"reforgedValue\": 525\n    },\n    \"substats\": [\n        {\n            \"type\": \"Speed\",\n            \"value\": 18,\n            \"rolls\": 4,\n            \"max\": 9,\n            \"min\": 5,\n            \"multi\": 3,\n            \"scaledDiff\": 0,\n            \"reforgedValue\": 18,\n            \"unreforgedValue\": 15,\n            \"unreforgedMin\": 4,\n            \"unreforgedMax\": 16\n        },\n        {\n            \"type\": \"AttackPercent\",\n            \"value\": 15,\n            \"rolls\": 2,\n            \"max\": 3,\n            \"min\": 2,\n            \"multi\": 6,\n            \"scaledDiff\": 0,\n            \"reforgedValue\": 15,\n            \"unreforgedValue\": 12,\n            \"unreforgedMin\": 8,\n            \"unreforgedMax\": 16\n        },\n        {\n            \"type\": \"CriticalHitDamagePercent\",\n            \"value\": 16,\n            \"rolls\": 2,\n            \"max\": 4,\n            \"min\": 3,\n            \"multi\": 5.5,\n            \"scaledDiff\": 0,\n            \"reforgedValue\": 16,\n            \"unreforgedValue\": 14,\n            \"unreforgedMin\": 8,\n            \"unreforgedMax\": 14\n        },\n        {\n            \"type\": \"CriticalHitChancePercent\",\n            \"value\": 6,\n            \"rolls\": 1,\n            \"max\": 2,\n            \"min\": 2,\n            \"multi\": 4,\n            \"scaledDiff\": 0,\n            \"reforgedValue\": 6,\n            \"unreforgedValue\": 5,\n            \"unreforgedMin\": 3,\n            \"unreforgedMax\": 5\n        }\n    ],\n    \"op\": [\n        [\n            \"att\",\n            \"105\",\n            null,\n            null,\n            \"1\"\n        ],\n        [\n            \"speed\",\n            \"4\"\n        ],\n        [\n            \"att_rate\",\n            \"0.08\"\n        ],\n        [\n            \"cri_dmg\",\n            \"0.07\"\n        ],\n        [\n            \"cri\",\n            \"0.05\"\n        ],\n        [\n            \"speed\",\n            \"4\"\n        ],\n        [\n            \"speed\",\n            \"4\"\n        ],\n        [\n            \"att_rate\",\n            \"0.04\"\n        ],\n        [\n            \"speed\",\n            \"3\"\n        ],\n        [\n            \"cri_dmg\",\n            \"0.07\"\n        ],\n        [\n            \"speed\",\n            \"3\",\n            \"u\"\n        ],\n        [\n            \"att_rate\",\n            \"0.03\",\n            \"u\"\n        ],\n        [\n            \"cri_dmg\",\n            \"0.02\",\n            \"u\"\n        ],\n        [\n            \"cri\",\n            \"0.01\",\n            \"u\"\n        ]\n    ],\n    \"name\": \"Unknown\",\n    \"augmentedStats\": {\n        \"AttackPercent\": 15,\n        \"HealthPercent\": 0,\n        \"DefensePercent\": 0,\n        \"Attack\": 0,\n        \"Health\": 0,\n        \"Defense\": 0,\n        \"Speed\": 18,\n        \"CriticalHitChancePercent\": 6,\n        \"CriticalHitDamagePercent\": 16,\n        \"EffectivenessPercent\": 0,\n        \"EffectResistancePercent\": 0,\n        \"mainType\": \"Attack\",\n        \"mainValue\": 525\n    },\n    \"reforgedStats\": {\n        \"AttackPercent\": 15,\n        \"HealthPercent\": 0,\n        \"DefensePercent\": 0,\n        \"Attack\": 0,\n        \"Health\": 0,\n        \"Defense\": 0,\n        \"Speed\": 18,\n        \"CriticalHitChancePercent\": 6,\n        \"CriticalHitDamagePercent\": 16,\n        \"EffectivenessPercent\": 0,\n        \"EffectResistancePercent\": 0,\n        \"mainType\": \"Attack\",\n        \"mainValue\": 525\n    },\n    \"id\": \"2261863295\",\n    \"ingameId\": \"2261863295\",\n    \"ingameEquippedId\": \"771423154\",\n    \"equippedById\": \"95f2d51e-e010-4afe-ab32-d436f477fdcc\",\n    \"equippedByName\": \"Vildred\",\n    \"locked\": false,\n    \"disableMods\": false,\n    \"reforgeable\": 0,\n    \"upgradeable\": 0,\n    \"convertable\": 0,\n    \"alreadyEquipped\": 0,\n    \"priority\": 0,\n    \"wss\": 79,\n    \"reforgedWss\": 79,\n    \"dpsWss\": 79,\n    \"supportWss\": 36,\n    \"combatWss\": 79,\n    \"duplicateId\": \"\",\n    \"allowedMods\": \"|Health|HealthPercent|EffectivenessPercent|EffectResistancePercent|\",\n    \"material\": \"Unknown\"\n}";
        Item item = new Gson().fromJson(s, Item.class);
        List<Item> itemList = new ArrayList<>();
        itemList.add(item);
        BailiInfo bailiInfo = calcBailiScore(itemList).get(0);
        System.out.println(bailiInfo);
    }
}
