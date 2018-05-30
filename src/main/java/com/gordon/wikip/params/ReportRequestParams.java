package com.gordon.wikip.params;

import com.gordon.wikip.analysis.AnalyzerType;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@Builder
@EqualsAndHashCode
@Getter
@ToString
public class ReportRequestParams {
	@Singular
	private Set<String> tickers;
	@NonNull
	private String apiKey;
	@NonNull
	@Builder.Default
	private final LocalDate startDate = LocalDate.parse("2017-01-01");
	@NonNull
	@Builder.Default
	private final LocalDate endDate = LocalDate.parse("2017-06-01");
	@Singular
	private Set<AnalyzerType> analyzers;
}
