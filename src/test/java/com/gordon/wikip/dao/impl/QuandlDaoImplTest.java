package com.gordon.wikip.dao.impl;

import com.gordon.wikip.model.WikiPriceData;
import com.gordon.wikip.params.PricesQueryParams;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URLConnection;
import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class QuandlDaoImplTest {

	@Test
	public void testWikiPricesReturnsAllData() throws IOException{
		List<WikiPriceData> wikiData = queryForData("src/test/resources/single_query_results.csv");
		assertEquals(1, wikiData.size());
		WikiPriceData wikiPriceData = wikiData.get(0);
		assertEquals("GOOGL", wikiPriceData.getTicker());
		assertEquals("2017-01-05", wikiPriceData.getDate().toString());
		assertEquals(807.5, wikiPriceData.getOpen().doubleValue(),0.0);
		assertEquals(813.74, wikiPriceData.getHigh().doubleValue(),0.0);
		assertEquals(805.92, wikiPriceData.getLow().doubleValue(),0.0);
	}

	@Test
	public void testWikiPricesAreCorrectlyParsed() throws IOException{
		List<WikiPriceData> wikiData = queryForData("src/test/resources/sample_query_results.csv");
		assertEquals(36, wikiData.size());
		assertEquals(18, wikiData.stream().filter(wd -> wd.getTicker().equals("GOOGL")).count());
		assertEquals(18, wikiData.stream().filter(wd -> wd.getTicker().equals("MSFT")).count());
	}

	@Test
	public void testUrlConnectionGeneration() throws IOException {
		String template = "http://www.test.com?${ticker}&${startDate}&${endDate}&${apiKey}";
		QuandlDaoImpl quandlDao = new QuandlDaoImpl(template);
		String startDate = "2018-01-01";
		String endDate = "2018-01-02";
		PricesQueryParams query = PricesQueryParams.builder()
				.ticker("GOOGL")
				.ticker("BAH")
				.apiKey("1234")
				.startDate(LocalDate.parse(startDate))
				.endDate(LocalDate.parse(endDate))
				.build();
		URLConnection connection = quandlDao.buildConnection(query);
		assertTrue(connection instanceof HttpURLConnection);
		String materializedUrl = connection.getURL().toString();
		assertEquals("http://www.test.com?GOOGL,BAH&2018-01-01&2018-01-02&1234", materializedUrl);
	}

	private List<WikiPriceData> queryForData(String sampleDataFile) throws IOException {
		QuandlDaoImpl quandlDao = spy(new QuandlDaoImpl(""));
		URLConnection mockConnection = mock(URLConnection.class);
		InputStream mockInputStream = new FileInputStream(sampleDataFile);
		when(mockConnection.getInputStream()).thenReturn(mockInputStream);
		doReturn(mockConnection).when(quandlDao).buildConnection(any(PricesQueryParams.class));
		return quandlDao.getWikiPrices(mock(PricesQueryParams.class));
	}
}
