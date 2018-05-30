package com.gordon.wikip.service.impl;

import com.gordon.wikip.analysis.Analyzer;
import com.gordon.wikip.analysis.AnalyzerType;
import com.gordon.wikip.dao.QuandlDao;
import com.gordon.wikip.model.Report;
import com.gordon.wikip.model.WikiPriceData;
import com.gordon.wikip.params.PricesQueryParams;
import com.gordon.wikip.params.ReportRequestParams;
import com.gordon.wikip.service.AnalysisService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

public class AnalysisServiceImpl implements AnalysisService {
    private static final Logger logger = LogManager.getLogger(AnalysisServiceImpl.class);
    private QuandlDao quandlDao;
    private Map<AnalyzerType, Analyzer> analyzerMap;

    public AnalysisServiceImpl(QuandlDao quandlDao, List<Analyzer> analyzers) {
        Objects.requireNonNull(quandlDao);
        Objects.requireNonNull(analyzers);
        this.quandlDao = quandlDao;
        this.analyzerMap = new HashMap<>();
        for (Analyzer a : analyzers) {
            this.analyzerMap.put(a.getAnalyzerType(), a);
        }
    }

    @Override
    public Optional<Report> performAnalysis(ReportRequestParams reportRequest) {
        Objects.requireNonNull(reportRequest);

        if (reportRequest.getAnalyzers().isEmpty()) {
            logger.info("No analyzers set; no analysis to perform or report to build.");
            return Optional.empty();
        }

        logger.info("Building report based on the following request: {}", reportRequest);
        //Build a data request query
        PricesQueryParams pricesQueryParams = PricesQueryParams.builder()
                .tickers(reportRequest.getTickers())
                .apiKey(reportRequest.getApiKey())
                .startDate(reportRequest.getStartDate())
                .endDate(reportRequest.getEndDate())
                .build();

        //Get the data
        final Map<String, List<WikiPriceData>> wikiPriceData = quandlDao.getWikiPrices(pricesQueryParams);

        if (!wikiPriceData.isEmpty()) {
            //Run the data over the set of Analyzers
            final Report report = new Report();
            reportRequest.getAnalyzers().stream()
                    .map(analyzerType -> {
                        logger.debug("Performing analysis using Analyzer: {}", analyzerType);
                        return analyzerMap.get(analyzerType);
                    })
                    .forEach(analyzer -> analyzer.analyze(wikiPriceData, report));

            return Optional.of(report);
        }

        logger.info("No WikiPrice Data was returned from service");
        return Optional.empty();
    }
}
