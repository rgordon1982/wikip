package com.gordon.wikip.model;

import lombok.Data;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;

@Data
public class SecurityReport {

    @NonNull
    private List<AvgMonthlyOpenClose> avgMonthlyOpenCloses = new ArrayList<>();
    private MaxDailyProfit maxDailyProfit;
    private BusyDayDetails busyDayDetails;
}
