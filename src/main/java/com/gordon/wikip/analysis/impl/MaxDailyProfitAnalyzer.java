package com.gordon.wikip.analysis.impl;

import com.gordon.wikip.analysis.Analyzer;
import com.gordon.wikip.analysis.AnalyzerType;
import com.gordon.wikip.model.MaxDailyProfit;
import com.gordon.wikip.model.Report;
import com.gordon.wikip.model.SecurityReport;
import com.gordon.wikip.model.WikiPriceData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Map;

public class MaxDailyProfitAnalyzer extends Analyzer {
    private static final Logger logger = LogManager.getLogger(MaxDailyProfitAnalyzer.class);

    public MaxDailyProfitAnalyzer() {
        super(AnalyzerType.MAX_DAILY_PROFIT);
    }

    @Override
    public void analyze(Map<String, List<WikiPriceData>> wikiPriceData, Report report) {
        for (Map.Entry<String, List<WikiPriceData>> entry : wikiPriceData.entrySet()) {
            String security = entry.getKey();
            double maxProfit = Double.MIN_VALUE;
            WikiPriceData bestDay = null;
            for (WikiPriceData wpd : entry.getValue()) {
                double currentProfit = wpd.getHigh().subtract(wpd.getLow()).doubleValue();
                if (currentProfit > maxProfit) {
                    maxProfit = currentProfit;
                    bestDay = wpd;
                }
            }

            if (bestDay != null) {
                SecurityReport securityReport = report.getSecurityReports().computeIfAbsent(security, k -> new SecurityReport());
                MaxDailyProfit maxDailyProfit = new MaxDailyProfit(bestDay.getDate(), maxProfit);
                securityReport.setMaxDailyProfit(maxDailyProfit);
            } else {
                //should never happen; as long we have a single entry within this loop it will set best day
                logger.error("Unexpected error: could not determine Best Day for {}", security);
            }
        }
    }
}
