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

public class BiggestLoserAnalyzerTest {

	@Test
	public void testBiggestLoserIsCalculatedForReport() {
		LocalDate date = LocalDate.parse("2017-01-01");
		Map<String, List<WikiPriceData>> inputData = new HashMap<>();
		inputData.put("GOOGL", generateData("GOOGL" , date, 10));
		inputData.put("MSFT", generateData("MSFT" , date, 20));

		Report report = new Report();
		BiggestLoserAnalyzer biggestLoserAnalyzer = new BiggestLoserAnalyzer();
		biggestLoserAnalyzer.analyze(inputData, report);

		BiggestLoser biggestLoser = report.getBiggestLoser();
		assertNotNull(biggestLoser);
		assertEquals("MSFT", biggestLoser.getTicker());
		assertEquals(20, biggestLoser.getNumberOfLosingDays());
	}

	private List<WikiPriceData> generateData(final String ticker, final LocalDate date, int numOfDays ) {
		List<WikiPriceData> data = new ArrayList<>();
		IntStream.range(0,numOfDays).forEach(i -> {
			WikiPriceData wpd = new WikiPriceData(
					ticker,
					date.plusDays(i),
					new BigDecimal(20),
					BigDecimal.ZERO,
					BigDecimal.ZERO,
					new BigDecimal(15),
					BigDecimal.ZERO);
			data.add(wpd);
		});

		return data;
	}
}
