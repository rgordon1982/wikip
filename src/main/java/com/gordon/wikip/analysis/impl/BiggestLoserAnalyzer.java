package com.gordon.wikip.analysis.impl;

import com.gordon.wikip.analysis.Analyzer;
import com.gordon.wikip.analysis.AnalyzerType;
import com.gordon.wikip.model.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BiggestLoserAnalyzer extends Analyzer {
	private static final Logger logger = LogManager.getLogger(BiggestLoserAnalyzer.class);

	public BiggestLoserAnalyzer() {
		super(AnalyzerType.BIGGEST_LOSER);
	}

	@Override
	public void analyze(Map<String, List<WikiPriceData>> wikiPriceData, Report report) {

		String biggestLosingSecurity = null;
		long numLosingDays = Long.MIN_VALUE;

		for (Map.Entry<String, List<WikiPriceData>> entry : wikiPriceData.entrySet()) {
			String security = entry.getKey();
			long count = entry.getValue().stream()
					.filter(wpd -> wpd.getClose().compareTo(wpd.getOpen()) < 0)
					.count();

			if(count > numLosingDays) {
				numLosingDays = count;
				biggestLosingSecurity = security;
			}
		}

		if(biggestLosingSecurity != null) {
			BiggestLoser biggestLoser = new BiggestLoser(biggestLosingSecurity, numLosingDays);
			report.setBiggestLoser(biggestLoser);
		}
	}
}
