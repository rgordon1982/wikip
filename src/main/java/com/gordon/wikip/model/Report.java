package com.gordon.wikip.model;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class Report {
	private Map<String, SecurityReport> securityReports = new HashMap<>();
	private BiggestLoser biggestLoser;
}
