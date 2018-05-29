package com.gordon.wikip.analysis;

import com.gordon.wikip.model.Report;
import com.gordon.wikip.model.WikiPriceData;

import java.util.Collection;
import java.util.Map;

public interface Analyzer {

	/**
	 * Performs an analysis on the supplied WikiPrice Data. Once calculated the findings are inserted
	 * into the {@link Report}
	 *
	 * @param wikiPriceData A mapping of a particular Security to its WikiPriceData
	 * @param report
	 */
	void analyze(Map<String, Collection<WikiPriceData>> wikiPriceData, Report report);
}
