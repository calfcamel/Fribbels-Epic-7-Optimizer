package com.fribbels.enums;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Set {

    @SerializedName("HealthSet")      HEALTH      (0, 2, new int[]{2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, new int[]{0, 0}, "HealthSet"),        // 生命套装
    @SerializedName("DefenseSet")     DEFENSE     (1, 2, new int[]{0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, new int[]{1, 1}, "DefenseSet"),       // 防御套装
    @SerializedName("AttackSet")      ATTACK      (2, 4, new int[]{0, 0, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, new int[]{2, 2, 2, 2}, "AttackSet"),  // 攻击套装
    @SerializedName("SpeedSet")       SPEED       (3, 4, new int[]{0, 0, 0, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, new int[]{3, 3, 3, 3}, "SpeedSet"),   // 速度套装
    @SerializedName("CriticalSet")    CRIT        (4, 2, new int[]{0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, new int[]{4, 4}, "CriticalSet"),      // 暴击套装
    @SerializedName("HitSet")         HIT         (5, 2, new int[]{0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, new int[]{5, 5}, "HitSet"),           // 命中套装
    @SerializedName("DestructionSet") DESTRUCTION (6, 4, new int[]{0, 0, 0, 0, 0, 0, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, new int[]{6, 6, 6, 6}, "DestructionSet"), // 破坏套装
    @SerializedName("LifestealSet")   LIFESTEAL   (7, 4, new int[]{0, 0, 0, 0, 0, 0, 0, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, new int[]{7, 7, 7, 7}, "LifestealSet"), // 吸血套装
    @SerializedName("CounterSet")     COUNTER     (8, 4, new int[]{0, 0, 0, 0, 0, 0, 0, 0, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, new int[]{8, 8, 8, 8}, "CounterSet"),    // 反击套装
    @SerializedName("ResistSet")      RESIST      (9, 2, new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, new int[]{9, 9}, "ResistSet"),           // 效果抗性套装
    @SerializedName("UnitySet")       UNITY       (10, 2, new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0}, new int[]{10, 10}, "UnitySet"),        // 团结套装
    @SerializedName("RageSet")        RAGE        (11, 4, new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 4, 0, 0, 0, 0, 0, 0, 0, 0}, new int[]{11, 11, 11, 11}, "RageSet"), // 愤怒套装
    @SerializedName("ImmunitySet")    IMMUNITY    (12, 2, new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0}, new int[]{12, 12}, "ImmunitySet"),     // 免疫套装
    @SerializedName("PenetrationSet") PENETRATION (13, 2, new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0}, new int[]{13, 13}, "PenetrationSet"), // 贯穿套装
    @SerializedName("RevengeSet")     REVENGE     (14, 4, new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 4, 0, 0, 0, 0, 0}, new int[]{14, 14, 14, 14}, "RevengeSet"), // 复仇套装
    @SerializedName("InjurySet")      INJURY      (15, 4, new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 4, 0, 0, 0, 0}, new int[]{15, 15, 15, 15}, "InjurySet"),  // 重伤套装
    @SerializedName("ProtectionSet")  PROTECTION  (16, 4, new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 4, 0, 0, 0}, new int[]{16, 16, 16, 16}, "ProtectionSet"), // 守护套装
    @SerializedName("TorrentSet")     TORRENT     (17, 4, new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0}, new int[]{17, 17}, "TorrentSet"),         // 激流套装
    @SerializedName("ReversalSet")    REVERSAL    (18, 4, new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 4, 0}, new int[]{18, 18, 18, 18}, "ReversalSet"), // 逆转套装
    @SerializedName("RiposteSet")     RIPOSTE     (19, 4, new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 4}, new int[]{19, 19, 19, 19}, "RiposteSet"); // 回击套装

    public int index;
    private int count;
    private int[] arr;
    private int[] indices;
    private String name;
}
