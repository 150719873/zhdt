package com.dotop.smartwater.project.third.concentrator.client.netty.utils;

import com.dotop.smartwater.project.third.concentrator.client.netty.dc.model.form.DeviceForm;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

public class StrUtil {

	public static boolean isBlank(String value) {
		return value == null || value.trim().length() == 0;
	}
	
	public static boolean isBlank(Integer value) {
		return value == null;
	}
	
	public static boolean isBlank(Long value) {
		return value == null;
	}
	
	public static boolean isBlank(Double value) {
		return value == null;
	}
	
	public static boolean isNotBlank(String value) {
		return !isBlank(value);
	}
	
	public static boolean isNotBlank(Double value) {
		return !isBlank(value);
	}
	
	public static boolean isNotBlank(Integer value) {
		return !isBlank(value);
	}
	
	public static boolean isNotBlank(Long value) {
		return !isBlank(value);
	}
	
	public static boolean isDigit(String str) {
		  return str.matches("[0-9]+");
	}
	
	public static String newGuid() {
		return UUID.randomUUID().toString().replace("-", "");
	}


	private static Pattern pMobile = Pattern.compile("^[1][3,4,5,7,8][0-9]{9}$");
	private static Pattern pWeChatParams = Pattern.compile("\\{\\{[^\\}]*\\}\\}");
	
	public static String join(String delimiter, String[] values) {
		StringBuilder sb = new StringBuilder("");
		for(int i = 0; i < values.length; i++) {
			if(i > 0) {
				sb.append(delimiter);
			}
			sb.append(values[i]);
		}
		return sb.toString();
	}
	
	/**
	   * 手机号验证
	   * 2016年12月5日下午4:34:46
	   * @param  str
	   * @return 验证通过返回true
	   */
	  public static boolean isMobile(String str) {
		  if(isBlank(str)){
			  return false;
		  }

		  return pMobile.matcher(str).matches();
	  }
	
	//重载一个join方法 ,操作list
	//内部不锁定,效率最高,但是当写多线程时要考虑并发操作的问题。
	public static String join(String delimiter, List<String> values) {
		StringBuilder sb = new StringBuilder("");
		for(int i = 0; i < values.size(); i++) {
			if(i > 0) {
				sb.append(delimiter);
			}
			sb.append(values.get(i));
		}
		return sb.toString();
	}
	
	//生成随机数 KJR
	public static String GetRandomNumberString(int weishu) {
		  String str = "";
		  str += (int)(Math.random()*9+1);
		  for(int i = 1; i < weishu; i++){
		  str += (int)(Math.random()*10);
		  }
		  return str;
	}



	/**
	 * 按指定大小，分隔集合，将集合按规定个数分为n个部分
	 *
	 * @param list
	 * @param len
	 * @return
	 */
	public static List<List<String>> splitList(List<String> list, int len) {
		if (list == null || list.size() == 0 || len < 1) {
			return null;
		}
		List<List<String>> result = new ArrayList<>();
		int size = list.size();
		int count = (size + len - 1) / len;
		for (int i = 0; i < count; i++) {
			List<String> subList = list.subList(i * len, ((i + 1) * len > size ? size : len * (i + 1)));
			result.add(subList);
		}
		return result;
	}

	public static List<List<DeviceForm>> splitDeviceList(List<DeviceForm> list, int len) {
		if (list == null || list.size() == 0 || len < 1) {
			return null;
		}
		List<List<DeviceForm>> result = new ArrayList<>();
		int size = list.size();
		int count = (size + len - 1) / len;
		for (int i = 0; i < count; i++) {
			List<DeviceForm> subList = list.subList(i * len, ((i + 1) * len > size ? size : len * (i + 1)));
			result.add(subList);
		}
		return result;
	}

}
