package com.gordon.wikip.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import lombok.Data;

import java.time.LocalDate;

@Data
public class BusyDay {
	@JsonSerialize(using = LocalDateSerializer.class)
	private final LocalDate date;
	private final double volume;
}
