package com.gordon.wikip.analysis.impl;

import com.gordon.wikip.analysis.Analyzer;
import com.gordon.wikip.analysis.AnalyzerType;
import com.gordon.wikip.model.AvgMonthlyOpenClose;
import com.gordon.wikip.model.Report;
import com.gordon.wikip.model.SecurityReport;
import com.gordon.wikip.model.WikiPriceData;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class AvgOpenCloseAnalyzer extends Analyzer {
	private static final DateTimeFormatter YEAR_MONTH = DateTimeFormatter.ofPattern("yyyy-MM");

	public AvgOpenCloseAnalyzer() {
		super(AnalyzerType.AVG_MONTHLY_OPEN_CLOSE);
	}

	@Override
	public void analyze(Map<String, List<WikiPriceData>> wikiPriceData, Report report) {
		//Get the average open and close prices across all securities inside of the supplied data
		for (Map.Entry<String, List<WikiPriceData>> entry : wikiPriceData.entrySet()) {
			String security = entry.getKey();

			Map<String, Accumulator> accumulatorMap = entry.getValue().stream()
					.collect(Collectors.groupingBy(
							wpd -> YEAR_MONTH.format(wpd.getDate()),
							Collector.of(Accumulator::new,
									(accumulator, data) -> accumulator.add(data),
									(l, r) -> l.merge(r))
					));

			//Update Report for this security
			SecurityReport securityReport = report.getSecurityReports().computeIfAbsent(security, k -> new SecurityReport());
			for (Map.Entry<String, Accumulator> openCloseEntries : accumulatorMap.entrySet()) {
				Accumulator accumulator = openCloseEntries.getValue();
				BigDecimal numOfRecords = new BigDecimal(accumulator.counter.get());
				String month = openCloseEntries.getKey();
				securityReport.getAvgMonthlyOpenCloses().add(
						AvgMonthlyOpenClose.builder()
								.averageOpen(accumulator.totalOpen.divide(numOfRecords).doubleValue())
								.averageClose(accumulator.totalClose.divide(numOfRecords).doubleValue())
								.month(month)
								.build());
			}
		}
	}

	private class Accumulator {
		BigDecimal totalOpen = BigDecimal.ZERO;
		BigDecimal totalClose = BigDecimal.ZERO;
		AtomicInteger counter = new AtomicInteger();

		public void add(WikiPriceData wpd) {
			totalClose = totalClose.add(wpd.getClose());
			totalOpen = totalOpen.add(wpd.getOpen());
			counter.incrementAndGet();
		}

		public Accumulator merge(Accumulator accumulator) {
			totalOpen = totalOpen.add(accumulator.totalOpen);
			totalClose = totalClose.add(accumulator.totalClose);
			counter.addAndGet(accumulator.counter.get());
			return this;
		}
	}
}
