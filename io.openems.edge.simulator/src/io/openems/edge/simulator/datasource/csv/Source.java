package io.openems.edge.simulator.datasource.csv;

public enum Source {
	H0_HOUSEHOLD_SUMMER_WEEKDAY_STANDARD_LOAD_PROFILE("h0-summer-weekday-standard-load-profile.csv"), //
	H0_HOUSEHOLD_SUMMER_WEEKDAY_PV_PRODUCTION("h0-summer-weekday-pv-production.csv"), //
	H0_HOUSEHOLD_SUMMER_WEEKDAY_NON_REGULATED_CONSUMPTION("h0-summer-weekday-non-regulated-consumption.csv"), //
	H0_HOUSEHOLD_SUMMER_WEEKDAY_PV_PRODUCTION2("h0-summer-weekday-pv-production2.csv"),
	HEATING_ELEMENT_TEST("h0-HeatingElement-test.csv"),
	ANOTHERONE("h0-HeatingElement-test01.csv");

	public final String filename;

	private Source(String filename) {
		this.filename = filename;
	}
}
