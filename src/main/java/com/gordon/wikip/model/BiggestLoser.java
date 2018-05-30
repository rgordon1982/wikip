package com.gordon.wikip.model;

import lombok.Data;

@Data
public class BiggestLoser {
	private final String ticker;
	private final long numberOfLosingDays;
}
