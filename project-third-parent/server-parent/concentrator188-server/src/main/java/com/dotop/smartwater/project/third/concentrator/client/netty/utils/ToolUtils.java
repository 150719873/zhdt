package com.dotop.smartwater.project.third.concentrator.client.netty.utils;


import com.dotop.smartwater.project.third.concentrator.core.bo.TerminalMeterReadBo;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.project.third.concentrator.client.netty.dc.model.code.ResultCode;
import com.dotop.smartwater.project.third.concentrator.core.constants.ConcentratorConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: dingtong
 * @description:
 *
 * @create: 2019-06-10 15:03
 **/
public class ToolUtils {
    public static final String START_PARAM = "68";
    public static final String END_PARAM = "16";

    public static boolean verifyDataLegitimacy(String msg) {
        int dataLen = msg.length();
        if (msg == null || dataLen < 4) {
            return false;
        }

        if (dataLen % 2 == 1) {
            return false;
        }

        //CS帧校验
        List<String> data = new ArrayList<>();
        for (int i = 12; i < dataLen - 4; i += 2) {
            data.add(msg.substring(i, i + 2));
        }
        int sum = 0;
        for (String d : data) {
            sum += Integer.parseInt(d, 16);
        }
        sum %= 256;
        String cs = Integer.toHexString(sum);
        if (cs.length() % 2 == 1) {
            cs = "0" + cs;
        }

        if (!msg.substring(dataLen - 4, dataLen - 2).equals(cs.toUpperCase())) {
            return false;
        }

        //头长度校验
        String len = msg.substring(4, 6) + msg.substring(2, 4);
        String lenCopy = msg.substring(8, 10) + msg.substring(6, 8);
        if (!len.equals(lenCopy)) {
            return false;
        }

        //帧格式头尾校验
        if (msg.startsWith(START_PARAM) && msg.endsWith(END_PARAM)) {
            //校验对比实际长度
            int effectiveLength = dataLen - 16;
            int calLen = Integer.parseInt(len, 16);
            if ((calLen - 1) / 2 == effectiveLength) {
                return true;
            }
        }
        return false;
    }

    public static String ConcentratorDecode(String num) {
        if (num.length() != 9) {
            throw new FrameworkRuntimeException(String.valueOf(ResultCode.Fail), "不合法的集中器编号");
        }

        String six = BinaryConversionUtils.intToHexString(Integer.parseInt(num.substring(4, 9)), 3).toUpperCase();

        return num.substring(2, 4) + num.substring(0, 2) + six.substring(4, 6) + six.substring(2, 4) + six.substring(0, 2);

    }


    /**
     * @param num 5个字节 字符长度为10
     * @return 集中器编号
     */
    public static String concentratorEncode(String num) {
        if (num.length() != 10) {
            return null;
        }
        Integer six = Integer.parseInt(num.substring(8, 10) + num.substring(6, 8) + num.substring(4, 6), 16);
        return num.substring(2, 4) + num.substring(0, 2) + ToolUtils.leftPad(String.valueOf(six), 5);

    }

    public static void makeTerminalMeterRead(TerminalMeterReadBo terminalMeterReadBo, String water) {
        if (water.contains("E")) {
            terminalMeterReadBo.setResult(ConcentratorConstants.RESULT_FAIL);
            terminalMeterReadBo.setMeter("0.00");
        } else {
            terminalMeterReadBo.setResult(ConcentratorConstants.RESULT_SUCCESS);
            terminalMeterReadBo.setMeter(ToolUtils.waterDecode(water));
        }
    }

    /**
     * 解析用水量
     **/
    public static String waterDecode(String reading) {
        if (StrUtil.isNotBlank(reading)) {
            StringBuffer sBuffer = new StringBuffer();
            sBuffer.append(reading, 6, 8);
            sBuffer.append(reading, 4, 6);
            sBuffer.append(reading, 2, 4);
            sBuffer.append(".");
            sBuffer.append(reading, 0, 2);
            return String.valueOf(Double.parseDouble(sBuffer.toString()));
        }
        return "0";
    }

    public static String leftPad(String str, int weishu) {
        StringBuilder result = new StringBuilder(str);
        int i = 0;
        while (i < (weishu - str.length())) {
            result.insert(0, "0");
            i++;
        }
        return result.toString();
    }
}
