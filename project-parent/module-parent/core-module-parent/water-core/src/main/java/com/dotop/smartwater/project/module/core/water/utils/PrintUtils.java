package com.dotop.smartwater.project.module.core.water.utils;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.dotop.smartwater.project.module.core.water.vo.DesignPrintVo;

public class PrintUtils {

	public static String changeContent(String content, boolean temp) {
		if (temp) {
			content = content.replaceAll("'", "’");
		} else {
			content = content.replaceAll("’", "'");
		}
		return content;
	}

	public static String matcherData(Map<String, String> data, String content, DesignPrintVo template) {
		Matcher m = null;
		content = resetTemplatePage(content);
		String[] array = content.split(";");
		StringBuilder returnData = new StringBuilder("");

		for (int i = 0; i < array.length; i++) {
			/** 验证当前行中是否包含需要解析的字段，如果没有则直接返回当前行 */
			m = Pattern.compile("(\\{[^\\}]+})").matcher(array[i]);
			if (!m.find()) {
				returnData.append(array[i]).append(";");
				continue;
			}

			m = Pattern.compile("(\\{[^\\}]+})").matcher(array[i]);
			while (m.find()) {
				String filed = m.group(1).replace("{", "").replace("}", "");
				String value = String.valueOf(data.get(filed) == null ? "" : data.get(filed));
				value = value.replaceAll("\r", "\\\\r");
				value = value.replaceAll("\n", "\\\\n");
				returnData.append(array[i].replace(m.group(1), value) + ";");
			}
		}
		return returnData.toString();
	}

	public static String resetTemplatePage(String content) {
		StringBuilder str = new StringBuilder();
		String[] array = content.split(";");

		for (int i = 0; i < array.length; i++) {
			if (!array[i].contains("PRINT_INITA")) {
				if ("".equals(array[i].trim())) {
					continue;
				}
				str.append(array[i]).append(";");
			}
		}
		return str.toString();
	}

}
