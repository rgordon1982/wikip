package com.gordon.wikip.dao;

import com.gordon.wikip.model.WikiPriceData;
import com.gordon.wikip.query.PricesQuery;

import java.util.Collection;
import java.util.List;

public interface QuandlDao {
	/**
	 * Performs the given query against the Quandl WIKI Prices REST Service and returns the result.
	 *
	 * @param query The query to execute against the remote service
	 * @return a {@link java.util.Collection} containing the results of the query
	 */
	List<WikiPriceData> getWikiPrices(PricesQuery query);
}
