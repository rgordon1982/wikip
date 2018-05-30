package com.gordon.wikip.dao;

import com.gordon.wikip.model.WikiPriceData;
import com.gordon.wikip.params.PricesQueryParams;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface QuandlDao {
	/**
	 * Performs the given params against the Quandl WIKI Prices REST Service and returns the result.
	 *
	 * @param query The params to execute against the remote service
	 * @return A mapping of a Security to its {@link java.util.Collection} of price data. May be empty but will be never
	 * be {@code null}
	 */
	Map<String, List<WikiPriceData>> getWikiPrices(PricesQueryParams query);
}
