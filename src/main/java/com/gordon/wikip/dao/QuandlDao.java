package com.gordon.wikip.dao;

import com.gordon.wikip.model.WikiPriceData;
import com.gordon.wikip.params.PricesQueryParams;

import java.util.List;

public interface QuandlDao {
	/**
	 * Performs the given params against the Quandl WIKI Prices REST Service and returns the result.
	 *
	 * @param query The params to execute against the remote service
	 * @return a {@link java.util.Collection} containing the results of the params
	 */
	List<WikiPriceData> getWikiPrices(PricesQueryParams query);
}
