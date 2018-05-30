package com.gordon.wikip.service.impl;

import com.gordon.wikip.analysis.Analyzer;
import com.gordon.wikip.analysis.AnalyzerType;
import com.gordon.wikip.dao.QuandlDao;
import com.gordon.wikip.model.Report;
import com.gordon.wikip.model.WikiPriceData;
import com.gordon.wikip.params.PricesQueryParams;
import com.gordon.wikip.params.ReportRequestParams;
import com.gordon.wikip.service.AnalysisService;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AnalysisServiceTest {

	@Test
	public void verifyAnalyzersRunForReportRequest() {
		Analyzer mockAvgOpenCloseAnalyzer = mock(Analyzer.class);
		Analyzer mockMaxDailyProfit = mock(Analyzer.class);
		QuandlDao mockQuandlDao = mock(QuandlDao.class);

		WikiPriceData mockData = new WikiPriceData("", LocalDate.now(), BigDecimal.ZERO,
				BigDecimal.ZERO,  BigDecimal.ZERO,  BigDecimal.ZERO,
				BigDecimal.ZERO);
		Map<String, List<WikiPriceData>> wikiPriceData = new HashMap<>();
		wikiPriceData.put("GOOGL", Arrays.asList(mockData));
		when(mockQuandlDao.getWikiPrices(any(PricesQueryParams.class))).thenReturn(wikiPriceData);

		List<Analyzer> analyzers = new ArrayList<>();
		analyzers.add(mockAvgOpenCloseAnalyzer);
		analyzers.add(mockMaxDailyProfit);
		when(mockAvgOpenCloseAnalyzer.getAnalyzerType()).thenReturn(AnalyzerType.AVG_MONTHLY_OPEN_CLOSE);
		when(mockMaxDailyProfit.getAnalyzerType()).thenReturn(AnalyzerType.MAX_DAILY_PROFIT);

		AnalysisService analysisService = new AnalysisServiceImpl(mockQuandlDao, analyzers);

		LocalDate now = LocalDate.now();
		ReportRequestParams requestParams = ReportRequestParams.builder()
				.analyzer(AnalyzerType.AVG_MONTHLY_OPEN_CLOSE)
				.apiKey("12345")
				.startDate(now)
				.endDate(now.plusMonths(1))
				.ticker("GOOGL")
				.ticker("MSFT")
				.build();

		Optional<Report> reportOptional = analysisService.performAnalysis(requestParams);
		Assert.assertTrue(reportOptional.isPresent());
		verify(mockAvgOpenCloseAnalyzer, times(1)).analyze(anyMap(), any(Report.class));
		verify(mockMaxDailyProfit, never()).analyze(anyMap(), any(Report.class));
	}
}
