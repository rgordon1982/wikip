package com.gordon.wikip;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.gordon.wikip.analysis.Analyzer;
import com.gordon.wikip.analysis.AnalyzerType;
import com.gordon.wikip.analysis.impl.AvgOpenCloseAnalyzer;
import com.gordon.wikip.analysis.impl.BiggestLoserAnalyzer;
import com.gordon.wikip.analysis.impl.BusyDayAnalyzer;
import com.gordon.wikip.analysis.impl.MaxDailyProfitAnalyzer;
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
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;

public class Application {

    private static final Logger logger = LogManager.getLogger(Application.class);

    public static void main(String[] args) {
        Options options = buildCliOptions();
        try {
            //parses input params
            CommandLineParser parser = new DefaultParser();
            CommandLine cmd = parser.parse(buildCliOptions(), args);

            if(cmd.hasOption("help")) {
                printHelp(options);
                System.exit(0);
            }

            //Load the applications properties
            Properties props = new Properties();
            props.load(Application.class.getClassLoader().getResourceAsStream("application.properties"));

            ReportRequestParams.ReportRequestParamsBuilder requestParamsBuilder = ReportRequestParams.builder();
            requestParamsBuilder.apiKey(props.getProperty("query.wiki.apikey"));
            requestParamsBuilder.analyzer(AnalyzerType.AVG_MONTHLY_OPEN_CLOSE);
            requestParamsBuilder.tickers(Arrays.asList("GOOGL", "MSFT", "COF")); //Default stocks to query for

            if (cmd.hasOption("ticker")) {
                requestParamsBuilder.clearTickers();
                for (String ticker : cmd.getOptionValues("ticker")) {
                    requestParamsBuilder.ticker(ticker);
                }
            }

            if (cmd.hasOption("startDate")) {
                requestParamsBuilder.startDate(LocalDate.parse(cmd.getOptionValue("startDate")));
            }

            if (cmd.hasOption("endDate")) {
                requestParamsBuilder.endDate(LocalDate.parse(cmd.getOptionValue("endDate")));
            }

            //Add extra Analyzers
            if (cmd.hasOption("maxDailyProfit")) {
                requestParamsBuilder.analyzer(AnalyzerType.MAX_DAILY_PROFIT);
            }

            if (cmd.hasOption("biggestLoser")) {
                requestParamsBuilder.analyzer(AnalyzerType.BIGGEST_LOSER);
            }

            if (cmd.hasOption("busyDays")) {
                requestParamsBuilder.analyzer(AnalyzerType.BUSY_DAY);
            }

            //Construct and wire our classes
            QuandlDao quandlDao = new QuandlDaoImpl(props.getProperty("query.wiki.prices"));
            List<Analyzer> analyzers = Arrays.asList(
                    new AvgOpenCloseAnalyzer(),
                    new MaxDailyProfitAnalyzer(),
                    new BiggestLoserAnalyzer(),
                    new BusyDayAnalyzer(Double.parseDouble(props.getProperty("analyzer.busyday.volume.threshold"))));

            ReportRequestParams requestParams = requestParamsBuilder.build();
            verifyRequestParams(requestParams);

            AnalysisService analysisService = new AnalysisServiceImpl(quandlDao, analyzers);
            Optional<Report> reportOptional = analysisService.performAnalysis(requestParams);

            Report report = reportOptional.orElseGet(() -> {
                logger.error("An internal error has occurred; no report can be generated");
                return new Report();
            });

            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
            mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            System.out.println("Result: \n" + mapper.writeValueAsString(report));

        } catch (ParseException e) {
            logger.debug("Unable to parse command line options", e);
            System.out.println(e.getMessage());
            printHelp(options);
        } catch (IOException e) {
            logger.error("Error accessing configuration", e);
        } catch (DateTimeParseException e) {
            logger.error("Unable to parse date", e);
        } catch (IllegalArgumentException e) {
            logger.error("The supplied query parameters are invalid", e);
        }

    }

    private static void verifyRequestParams(ReportRequestParams params) throws IllegalArgumentException {
        Objects.requireNonNull(params);
        if (params.getStartDate().isAfter(params.getEndDate())) {
            throw new IllegalArgumentException(String.format("The StartDate [%s] cannot be after the EndDate [%s]", params.getStartDate(), params.getEndDate()));
        }
    }

    private static void printHelp(Options options) {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("java -jar <path to wikip-1.0-uber.jar> <options>", options);
    }

    private static Options buildCliOptions() {
        Options options = new Options();
        options.addOption("help", "Prints the help message");
        //Analyzers
        options.addOption("maxDailyProfit", "Flag indicating the report should display the date of maximum profit if purchased at the day's low and sold at the days high for each security");
        options.addOption("biggestLoser", "Flag indicating the report should display which security had the most days where the closing price was lower than the opening price");
        options.addOption("busyDays", "Flag indicating the report should display which days generated unusually high activity for our securities");

        //Configs
        Option tickers = Option.builder("ticker")
                .argName("stock ticker symbol")
                .desc("Specifies a Stock Ticker to include in the Report. When not provided the default tickets are [GOOGL, MSFT, COF] are used.")
                .hasArgs()
                .build();
        Option startDate = Option.builder("startDate")
                .argName("ISO-8601 Date")
                .desc("The start date of the query range (inclusive). Defaults to: 2017-01-01")
                .hasArg()
                .build();
        Option endDate = Option.builder("endDate")
                .argName("ISO-8601 Date")
                .desc("The end date of the query range (inclusive). Defaults to: 2017-06-01")
                .hasArg()
                .build();
        options.addOption(tickers);
        options.addOption(startDate);
        options.addOption(endDate);

        return options;
    }
}
