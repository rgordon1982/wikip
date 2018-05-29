package com.gordon.wikip.model;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
public class SecurityReport {

	@NonNull
	private List<AvgMonthlyOpenClose> avgMonthlyOpenCloses = new ArrayList<>();
}
