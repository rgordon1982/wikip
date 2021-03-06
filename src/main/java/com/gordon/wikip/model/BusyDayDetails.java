package com.gordon.wikip.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Singular;

import java.util.List;

@Builder
@Getter
public class BusyDayDetails {
    @Singular
    private List<BusyDay> busyDays;
    private final double averageVolume;
}
