package com.gordon.wikip.dao.impl;

import com.gordon.wikip.Constants;
import com.gordon.wikip.dao.QuandlDao;
import com.gordon.wikip.model.WikiPriceData;
import com.gordon.wikip.params.PricesQueryParams;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.text.StringSubstitutor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URLConnection;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class QuandlDaoImpl implements QuandlDao {
	private static final Logger logger = LogManager.getLogger(QuandlDaoImpl.class);
	private final String wikiPricesQueryTemplate;

	public QuandlDaoImpl(String wikiPricesQueryTemplate) {
		this.wikiPricesQueryTemplate = wikiPricesQueryTemplate;
	}

	public Map<String, List<WikiPriceData>> getWikiPrices(PricesQueryParams query) {

		//Open up a URL Connection
		URLConnection connection = null;
		try {
			connection = buildConnection(query);
			logger.info("Retrieving data for resource with params parameters {}", query);
			try(InputStream inputStream = connection.getInputStream();
				BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
				CSVParser records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(reader)) {

				//Read the data into a BufferedReader, parsing the results into WikiPriceData
				Map<String, List<WikiPriceData>> result = StreamSupport.stream(records.spliterator(), false)
						.map(line -> {
							//parse and return a model
							return new WikiPriceData(
									line.get("ticker"),
									LocalDate.parse(line.get("date")),
									new BigDecimal(line.get("open")),
									new BigDecimal(line.get("high")),
									new BigDecimal(line.get("low")),
									new BigDecimal(line.get("close")));
						})
						.collect(Collectors.groupingBy(wikiData -> wikiData.getTicker()));

				if(logger.isDebugEnabled()) {
					int count = 0;
					for(List<WikiPriceData> l : result.values()) {
						count += l.size();
					}
					logger.debug("Retrieved {} total records across the following securities: {}", count, result.keySet());
				}

				return result;

			} finally {
				if(connection != null && connection instanceof HttpURLConnection) {
					((HttpURLConnection)connection).disconnect();
				}
			}

		} catch( IOException e) {
			logger.error("Unable to retrieve Quandl WIKI Dataset", e);
		}

		//Add to the collection and return
		return Collections.EMPTY_MAP;
	}

	protected URLConnection buildConnection(PricesQueryParams query) throws IOException {
		Map<String, String> replacementValues = new HashMap<>();
		String tickers = query.getTickers().stream().collect(Collectors.joining(","));
		replacementValues.put(Constants.START_DATE, query.getStartDate().toString());
		replacementValues.put(Constants.END_DATE, query.getEndDate().toString());
		replacementValues.put(Constants.TICKER, tickers);
		replacementValues.put(Constants.API_KEY, query.getApiKey());
		StringSubstitutor substitutor = new StringSubstitutor(replacementValues);

		String queryUrl = substitutor.replace(wikiPricesQueryTemplate);
		logger.debug("Generated Query Url {}", queryUrl);

		HttpURLConnection connection =
				(HttpURLConnection) URI.create(queryUrl).toURL().openConnection();
		connection.setRequestMethod("GET");
		connection.setReadTimeout((int)TimeUnit.SECONDS.toMillis(10));
		return connection;
	}

}
