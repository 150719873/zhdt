package com.dotop.smartwater.dependence.core.utils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.util.CollectionUtils;

import static java.util.regex.Pattern.*;

/**

 * @date 2019/4/19.
 */
public class StrUtils {

	private StrUtils() {
		super();
	}

	/**
	 * 根据分隔符去除多余空白内容
	 * 
	 * @param str
	 * @param split
	 * @return
	 */
	public static final String removeMoreSplit(String str, String split) {
		if (org.springframework.util.StringUtils.isEmpty(str)) {
			return str;
		}
		String[] strs = str.split(split);
		List<String> coll = new ArrayList<>();
		for (String s : strs) {
			if (!org.springframework.util.StringUtils.isEmpty(s) && !coll.contains(s)) {
				coll.add(s);
			}
		}
		StringBuilder result = new StringBuilder();
		if (!CollectionUtils.isEmpty(coll)) {
			for (int i = 0; i < coll.size(); i++) {
				if (i != 0) {
					result.append(split);
				}
				result.append(coll.get(i));
			}
		}
		return result.toString();
	}

	/**
	 * 判断字符串中是否包含中文
	 * @param str
	 * 待校验字符串
	 * @return 是否为中文
	 * @warn 不能校验是否为中文标点符号
	 */
	public static boolean isContainChinese(String str) {
		Pattern pattern = compile("[\u4e00-\u9fa5]");
		Matcher m = pattern.matcher(str);
		if (m.find()) {
			return true;
		}
		return false;
	}

	public static <T> String join(String delimiter, List<T> values) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < values.size(); i++) {
			if (i > 0) {
				sb.append(delimiter);
			}
			sb.append(values.get(i));
		}
		return sb.toString();
	}

	/**
	 * 求差集
	 *
	 * @param m
	 * @param n
	 * @return
	 */
	public static String[] getDifferenceSet(String[] m, String[] n) {
		// 将较长的数组转换为set
		Set<String> set = new HashSet<>(Arrays.asList(m.length > n.length ? m : n));

		// 遍历较短的数组，实现最少循环
		for (String i : m.length > n.length ? n : m) {
			// 如果集合里有相同的就删掉，如果没有就将值添加到集合
			if (set.contains(i)) {
				set.remove(i);
			} else {
				set.add(i);
			}
		}

		String[] arr = {};
		return set.toArray(arr);
	}
}
