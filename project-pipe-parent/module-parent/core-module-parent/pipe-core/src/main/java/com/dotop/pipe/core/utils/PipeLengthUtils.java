package com.dotop.pipe.core.utils;

import java.math.BigDecimal;

public class PipeLengthUtils {

	private static double DEFAULT_RADIUS = 6371008.8;

	public static String getPipeLength(String longitude1, String latitude1, String longitude2, String latitude2) {
		double radius = DEFAULT_RADIUS;
		double lat1 = Math.toRadians(Double.parseDouble(latitude1));
		double lat2 = Math.toRadians(Double.parseDouble(latitude2));
		double deltaLatBy2 = (lat2 - lat1) / 2;
		double deltaLonBy2 = Math.toRadians(Double.parseDouble(longitude2) - Double.parseDouble(longitude1)) / 2;
		double a = Math.sin(deltaLatBy2) * Math.sin(deltaLatBy2)
				+ Math.sin(deltaLonBy2) * Math.sin(deltaLonBy2) * Math.cos(lat1) * Math.cos(lat2);
		double length = 2 * radius * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

		BigDecimal decimal = new BigDecimal(length);
		return decimal.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
	}
}
