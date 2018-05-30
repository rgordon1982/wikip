package com.gordon.wikip.analysis;

import com.gordon.wikip.model.Report;
import com.gordon.wikip.model.WikiPriceData;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;

@Getter
@RequiredArgsConstructor
public abstract class Analyzer {

    private final AnalyzerType analyzerType;

    /**
     * Performs an analysis on the supplied WikiPrice Data. Once calculated the findings are inserted
     * into the {@link Report}
     *
     * @param wikiPriceData A mapping of a particular Security to its WikiPriceData
     * @param report
     */
    abstract public void analyze(Map<String, List<WikiPriceData>> wikiPriceData, Report report);

}
