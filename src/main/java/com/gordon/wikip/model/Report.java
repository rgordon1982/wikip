package com.gordon.wikip.model;

import lombok.Builder;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class Report {
	private Map<String, SecurityReport> securityReports = new HashMap<>();
	//private BiggestLoser;
}
