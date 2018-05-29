package com.gordon.wikip.model;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 *  A Model object containing the trade information for a particular security for a single day.
 */
@Data
public class WikiPriceData {
	private final String ticker;
	private final LocalDate date;
	private final BigDecimal open;
	private final BigDecimal high;
	private final BigDecimal low;
	private final BigDecimal close;
}
