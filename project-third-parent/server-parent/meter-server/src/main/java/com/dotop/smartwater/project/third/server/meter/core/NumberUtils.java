package com.dotop.smartwater.project.third.server.meter.core;

import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class NumberUtils {

    // 16转10
    public final static int hexToTen(String data) {
        if (StringUtils.isBlank(data)) {
            return 0;
        }
        String trim = data.trim();
        BigInteger bi = new BigInteger(trim.toString(), 16);
        int ai = bi.intValue();
        return ai;
    }

    // 16转10
    public final static String hexToTenStr(String data) {
        if (StringUtils.isBlank(data)) {
            return "";
        }
        String trim = data.trim();
        BigInteger bi = new BigInteger(trim.toString(), 16);
        return bi.toString();
    }

    // 16转10
    public final static List<Integer> hexToTen(List<String> datas) {
        if (datas == null || datas.isEmpty()) {
            return new ArrayList<>();
        }
        List<Integer> is = new ArrayList<>();
        for (String s : datas) {
            is.add(hexToTen(s));
        }
        return is;
    }

    // 10转16
    public final static String tenToHexStr(String data, int hexLen) throws RuntimeException {
        BigDecimal bd = new BigDecimal(data);
        data = Integer.toHexString(bd.intValue());
        // data 长度认证
        if (data.length() > hexLen * 2) {
            throw new RuntimeException(BaseExceptionConstants.PARSE_EXCEPTION);
        }
        while (data.length() > 0 && data.length() < hexLen * 2) {
            data = "0" + data;
        }
        return data;
    }

    /**
     * 16进制转byte
     */
    public static byte[] hexToBytes(String hex) {
        int l = hex.length() / 2;
        byte[] ret = new byte[l];
        for (int i = 0; i < l; i++) {
            ret[i] = (byte) Integer.valueOf(hex.substring(i * 2, i * 2 + 2), 16).byteValue();
        }
        return ret;
    }

    /**
     * 16进制转byte转16进制
     */
    public static String bytesToHex(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }
}
