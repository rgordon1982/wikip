package com.gordon.wikip.params;

import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@Builder
@EqualsAndHashCode
@Getter
@ToString
public class PricesQueryParams {

	@Singular
	private Set<String> tickers;
	@NonNull
	private String apiKey;
	@NonNull
	private LocalDate startDate;
	@NonNull
	private LocalDate endDate;
}
