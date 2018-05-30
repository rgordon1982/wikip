package com.gordon.wikip.analysis.impl;

import com.gordon.wikip.model.*;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class MaxDailyProfitAnalyzerTest {

	@Test
	public void testMaxDailyProfitIsCalculatedForReport() {
		LocalDate date = LocalDate.parse("2017-01-01");
		Map<String, List<WikiPriceData>> inputData = new HashMap<>();
		inputData.put("GOOGL", generateData("GOOGL" , date));
		inputData.put("MSFT", generateData("MSFT" , date));

		Report report = new Report();
		MaxDailyProfitAnalyzer maxDailyProfitAnalyzer = new MaxDailyProfitAnalyzer();
		maxDailyProfitAnalyzer.analyze(inputData, report);

		verifyReportForSecurity(report, "GOOGL");
		verifyReportForSecurity(report, "MSFT");
	}

	private void verifyReportForSecurity(Report report, String security) {
		assertTrue(report.getSecurityReports().containsKey(security));
		SecurityReport googleSecurityReport = report.getSecurityReports().get(security);

		MaxDailyProfit maxDailyProfit = googleSecurityReport.getMaxDailyProfit();
		assertNotNull(maxDailyProfit);
		assertEquals(LocalDate.parse("2017-01-05"), maxDailyProfit.getDate());
		assertEquals(13, maxDailyProfit.getProfit(), 0.0);
	}

	private List<WikiPriceData> generateData(final String ticker, final LocalDate date ) {
		List<WikiPriceData> data = new ArrayList<>();
		IntStream.range(0,5).forEach(i -> {
			WikiPriceData wpd = new WikiPriceData(
					ticker,
					date.plusDays(i),
					new BigDecimal(20),
					new BigDecimal((20+i)), //increase the high
					new BigDecimal((15-i)), //decrease the low; biggest gap will be when i=4, which equals 24-11=13
					new BigDecimal(15));
			data.add(wpd);
		});

		return data;
	}
}
