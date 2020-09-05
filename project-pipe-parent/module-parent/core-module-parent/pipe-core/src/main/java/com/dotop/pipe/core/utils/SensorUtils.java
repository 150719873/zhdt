package com.dotop.pipe.core.utils;

@Deprecated
public class SensorUtils {

	public final static String newSensorCode(String type, String code) {

		String newSensorCode = "";
		if (code == null || "".equals(code)) {
			// 默认编码从00001 开始
			// 例子 FM00001
			// newSensorCode = type + PipeConstants.SENSOR_CODE_START;
		} else { // 执行递增操作
			String num = code.substring(3, 7);
			String numAddOne = String.format("%5d", Integer.parseInt(num) + 1).replace(" ", "0"); // 补0 操作
			newSensorCode = type + numAddOne;
		}

		return newSensorCode;
	}
}
