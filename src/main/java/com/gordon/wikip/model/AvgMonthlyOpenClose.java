package com.gordon.wikip.model;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class AvgMonthlyOpenClose {
    private String month;
    private double averageOpen;
    private double averageClose;
}
