package com.gordon.wikip.analysis.impl;

import com.gordon.wikip.model.AvgMonthlyOpenClose;
import com.gordon.wikip.model.Report;
import com.gordon.wikip.model.SecurityReport;
import com.gordon.wikip.model.WikiPriceData;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AvgOpenCloseAnalyzerTest {

	@Test
	public void testAveragesAreCalculatedForReport() {
		LocalDate currentDate = LocalDate.parse("2017-02-15");

		Report report = new Report();
		Map<String, List<WikiPriceData>> inputData = new HashMap<>();
		inputData.put("GOOGL", generateData("GOOGL" , currentDate));
		inputData.put("MSFT", generateData("MSFT" , currentDate));

		AvgOpenCloseAnalyzer analyzer = new AvgOpenCloseAnalyzer();
		analyzer.analyze(inputData, report);

		verifyReportForSecurity(report, "GOOGL");
		verifyReportForSecurity(report, "MSFT");
	}

	private void verifyReportForSecurity(Report report, String security) {
		assertTrue(report.getSecurityReports().containsKey(security));
		SecurityReport googleSecurityReport = report.getSecurityReports().get(security);
		assertEquals(2, googleSecurityReport.getAvgMonthlyOpenCloses().size());
		AvgMonthlyOpenClose first = googleSecurityReport.getAvgMonthlyOpenCloses().get(0);

		assertEquals("2017-01", first.getMonth());
		assertEquals(50, first.getAverageOpen(), 0.0);
		assertEquals(100, first.getAverageClose(), 0.0);

		AvgMonthlyOpenClose second = googleSecurityReport.getAvgMonthlyOpenCloses().get(1);
		assertEquals("2017-02", second.getMonth());
		assertEquals(50, second.getAverageOpen(), 0.0);
		assertEquals(100, second.getAverageClose(), 0.0);
	}

	private List<WikiPriceData> generateData(final String ticker, final LocalDate date ) {
		List<WikiPriceData> data = new ArrayList<>();
		IntStream.range(1,5).forEach(i -> {
			WikiPriceData wpd = new WikiPriceData(
					ticker,
					date.minusMonths(1),
					new BigDecimal(50),
					BigDecimal.ZERO,
					BigDecimal.ZERO,
					new BigDecimal(100));
			data.add(wpd);
		});

		IntStream.range(1,5).forEach(i -> {
			WikiPriceData wpd = new WikiPriceData(
					ticker,
					date,
					new BigDecimal(50),
					BigDecimal.ZERO,
					BigDecimal.ZERO,
					new BigDecimal(100));
			data.add(wpd);
		});

		return data;
	}
}
