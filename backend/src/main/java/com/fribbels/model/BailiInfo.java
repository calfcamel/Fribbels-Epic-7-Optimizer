package com.fribbels.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BailiInfo {
    private String details = "";
    private int score = 0;
    private int firstSpeedScore = 0;
    private int speedScore = 0;
    private int tankScore = 0;
    private int dpsScore = 0;
}
