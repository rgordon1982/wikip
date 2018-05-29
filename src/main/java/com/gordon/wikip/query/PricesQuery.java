package com.gordon.wikip.query;

import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@Builder
@EqualsAndHashCode
@Getter
@ToString
public class PricesQuery {

	@Singular
	private Set<String> tickers;
	@NonNull
	private String apiKey;
	@NonNull
	private LocalDate startDate;
	@NonNull
	private LocalDate endDate;
}
