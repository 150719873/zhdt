package com.dotop.smartwater.view.server.utils;

import java.math.BigDecimal;

public class CalculationUtils {

    public static double doubleFix(Double d, Integer fix) {
        BigDecimal b = new BigDecimal(d);
        return b.setScale(fix, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public static long randomDate(long begin, long end) {
        long rtn = begin + (long) (Math.random() * (end - begin));
        if (rtn == begin || rtn == end) {
            return randomDate(begin, end);
        }
        return rtn;
    }

}
