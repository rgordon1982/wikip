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

import static org.junit.Assert.*;

public class BusyDayAnalyzerTest {

	@Test
	public void testBusyDaysIsCalculatedForReport() {
		LocalDate date = LocalDate.parse("2017-01-01");
		Map<String, List<WikiPriceData>> inputData = new HashMap<>();
		inputData.put("GOOGL", generateData("GOOGL" , date));
		inputData.put("MSFT", generateData("MSFT" , date));

		Report report = new Report();
		BusyDayAnalyzer busyDayAnalyzer = new BusyDayAnalyzer(1.1);
		busyDayAnalyzer.analyze(inputData, report);

		verifyReportForSecurity(report, "GOOGL");
		verifyReportForSecurity(report, "MSFT");
	}

	private void verifyReportForSecurity(Report report, String security) {
		assertTrue(report.getSecurityReports().containsKey(security));
		SecurityReport securityReport = report.getSecurityReports().get(security);

		BusyDays busyDays = securityReport.getBusyDays();
		assertNotNull(busyDays);
		assertEquals(14.5, busyDays.getAverageVolume(), 0.0);
		assertEquals(2, busyDays.getBusyDays().size());
		assertEquals("2017-01-01", busyDays.getBusyDays().get(0).getDate().toString());
		assertEquals("2017-01-11", busyDays.getBusyDays().get(1).getDate().toString());

		for(BusyDay bd : busyDays.getBusyDays()) {
			assertEquals(100, bd.getVolume(),0.0);
		}
	}

	private List<WikiPriceData> generateData(final String ticker, final LocalDate date ) {
		List<WikiPriceData> data = new ArrayList<>();
		IntStream.range(0,20).forEach(i -> {
			WikiPriceData wpd = new WikiPriceData(
					ticker,
					date.plusDays(i),
					BigDecimal.ZERO,
					BigDecimal.ZERO,
					BigDecimal.ZERO,
					BigDecimal.ZERO,
					new BigDecimal(i % 10 == 0 ? 100 : 5));//Produces outliers in regards to volume when i is 0, 10 20, etc...
			data.add(wpd);
		});

		return data;
	}
}
