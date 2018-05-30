package com.gordon.wikip.service;

import com.gordon.wikip.model.Report;
import com.gordon.wikip.params.ReportRequestParams;

import java.util.Optional;

public interface AnalysisService {

	/**
	 * Performs the requested set of analyses over WIKI Price data for the specified securities. The result
	 * of each analysis will be stored in the returned {@link Report}
	 *
	 * @param reportRequest The parameters specifying how the report will be build.
	 * @return An {@link Optional} containing the {@link Report} containing the results of a successful analysis.
	 */
	Optional<Report> performAnalysis(ReportRequestParams reportRequest);
}
