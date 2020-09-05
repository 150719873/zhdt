package com.dotop.smartwater.project.third.server.meter.core;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public final class StringUtils extends org.apache.commons.lang3.StringUtils {

    /**
     * 把原始字符串分割成指定长度的字符串列表
     */
    public static List<String> getStrList(String inputString, int length) {
        int size = inputString.length() / length;
        if (inputString.length() % length != 0) {
            size += 1;
        }
        return getStrList(inputString, length, size);
    }

    /**
     * 把原始字符串分割成指定长度的字符串列表
     */
    public static List<String> getStrList(String inputString, int length, int size) {
        List<String> list = new ArrayList<>();
        for (int index = 0; index < size; index++) {
            String childStr = substring(inputString, index * length, (index + 1) * length);
            list.add(childStr);
        }
        return list;
    }

    /**
     * 分割字符串，如果开始位置大于字符串长度，返回空
     */
    public static String substring(String str, int f, int t) {
        if (f > str.length()) {
            return null;
        }
        if (t > str.length()) {
            return str.substring(f, str.length());
        } else {
            return str.substring(f, t);
        }
    }
}
