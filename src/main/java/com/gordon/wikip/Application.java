package com.gordon.wikip;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.gordon.wikip.analysis.Analyzer;
import com.gordon.wikip.analysis.AnalyzerType;
import com.gordon.wikip.analysis.impl.AvgOpenCloseAnalyzer;
import com.gordon.wikip.dao.QuandlDao;
import com.gordon.wikip.dao.impl.QuandlDaoImpl;
import com.gordon.wikip.model.Report;
import com.gordon.wikip.params.ReportRequestParams;
import com.gordon.wikip.service.AnalysisService;
import com.gordon.wikip.service.impl.AnalysisServiceImpl;
import org.apache.commons.cli.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.*;

public class Application {

  private static final Logger logger = LogManager.getLogger(Application.class);

  public static void main(String[] args) {
    Options options = buildCliOptions();
    try {
      //parses input params
      CommandLineParser parser = new DefaultParser();
      CommandLine cmd = parser.parse( buildCliOptions(), args);

      //Load the applications properties
      Properties props = new Properties();
      props.load(Application.class.getClassLoader().getResourceAsStream("application.properties"));

      ReportRequestParams.ReportRequestParamsBuilder requestParamsBuilder = ReportRequestParams.builder();
      requestParamsBuilder.apiKey(props.getProperty("query.wiki.apikey"));
      requestParamsBuilder.analyzer(AnalyzerType.AVG_MONTHLY_OPEN_CLOSE);
      requestParamsBuilder
              .ticker("GOOGL")
              .ticker("MSFT")
              .ticker("COF");

      //Construct and wire our classes
      QuandlDao quandlDao = new QuandlDaoImpl(props.getProperty("query.wiki.prices"));
      List<Analyzer> analyzers = Arrays.asList(new AvgOpenCloseAnalyzer());

      ReportRequestParams requestParams = requestParamsBuilder.build();
      AnalysisService analysisService = new AnalysisServiceImpl(quandlDao, analyzers);
      Optional<Report> reportOptional = analysisService.performAnalysis(requestParams);

      Report report = reportOptional.orElseGet(() -> {
        logger.error("An internal error has occurred; no report can be generated");
        return new Report();
      });

      ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
      System.out.println("Result: \n"+ mapper.writeValueAsString(report));

    } catch (ParseException e) {
      logger.error("Unable to parse command line options", e);
      HelpFormatter formatter = new HelpFormatter();
      formatter.printHelp( "java -jar wikip-1.0-uber.jar", options );
    } catch (IOException e) {
      logger.error("Error accessing configuration",e);
    }

  }

  private static Options buildCliOptions() {
    Options options = new Options();
    //Analyzers
    options
            .addOption("maxDailyProfit", "Displays the date of maximum profit if purchased at the day's low and sold at the days high for each security");
    return options;
  }
}
