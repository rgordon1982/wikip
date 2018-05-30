package com.gordon.wikip.analysis.impl;

import com.gordon.wikip.analysis.Analyzer;
import com.gordon.wikip.analysis.AnalyzerType;
import com.gordon.wikip.model.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BusyDayAnalyzer extends Analyzer {
	private static final Logger logger = LogManager.getLogger(BusyDayAnalyzer.class);
	private final double busyVolumeLimit;

	public BusyDayAnalyzer(double busyVolumeLimit) {
		super(AnalyzerType.BUSY_DAY);
		this.busyVolumeLimit = busyVolumeLimit;
	}

	@Override
	public void analyze(Map<String, List<WikiPriceData>> wikiPriceData, Report report) {
		for (Map.Entry<String, List<WikiPriceData>> entry : wikiPriceData.entrySet()) {
			String security = entry.getKey();

			double averageVolume = entry.getValue().stream()
					.map(wpd -> wpd.getVolume())
					.collect(Collectors.averagingDouble(data -> data.doubleValue()));
			double volumeThreshold = averageVolume * busyVolumeLimit;

			List<WikiPriceData> busyDays = entry.getValue().stream()
				.filter(wpd -> wpd.getVolume().doubleValue() > volumeThreshold)
				.collect(Collectors.toList());

			SecurityReport securityReport = report.getSecurityReports().computeIfAbsent(security, k -> new SecurityReport());
			BusyDays.BusyDaysBuilder busyDaysBuilder = BusyDays.builder()
					.averageVolume(averageVolume);
			for(WikiPriceData wpd : busyDays) {
				busyDaysBuilder.busyDay(new BusyDay(wpd.getDate(), wpd.getVolume().doubleValue()));
			}

			securityReport.setBusyDays(busyDaysBuilder.build());
		}
	}
}
