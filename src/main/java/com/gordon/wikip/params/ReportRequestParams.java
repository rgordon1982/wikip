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
	private LocalDate startDate;
	@NonNull
	private LocalDate endDate;
	@Singular
	private Set<AnalyzerType> analyzers;
}
