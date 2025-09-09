package com.fribbels.baili;

import com.fribbels.enums.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

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

    public static final int GEAR_WEAPON = 1;
    public static final int GEAR_HELMET = 1 << 1;
    public static final int GEAR_ARMOR = 1 << 2;
    public static final int GEAR_NECKLACE = 1 << 3;
    public static final int GEAR_RING = 1 << 4;
    public static final int GEAR_BOOTS = 1 << 5;

    public static final int GEAR_ALL = (1 << 6) - 1;

    public static final int HEALTH_SET = 1 << Set.HEALTH.getIndex();
    public static final int DEFENSE_SET = 1 << Set.DEFENSE.getIndex();
    public static final int SPEED_SET = 1 << Set.SPEED.getIndex();
    public static final int CRIT_SET = 1 << Set.CRIT.getIndex();
    public static final int HIT_SET = 1 << Set.HIT.getIndex();
    public static final int DESTRUCTION_SET = 1 << Set.DESTRUCTION.getIndex();
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

    private int mainType;
    private int gearType;
    private int setType;
    private int subTypes;
    private int offset;
    private int factor;

    public static List<BailiRule> rules;

    public static final int GEAR_NOT_BOOTS = GEAR_ALL & ~GEAR_BOOTS;

    static {
        rules = new ArrayList<>();
        rules.add(new BailiRule(ALL_STATS, GEAR_NOT_BOOTS, SPEED_SET, SPEED, 0, 0));
    }
}
