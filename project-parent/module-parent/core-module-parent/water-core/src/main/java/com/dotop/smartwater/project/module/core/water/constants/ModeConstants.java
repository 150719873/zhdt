package com.dotop.smartwater.project.module.core.water.constants;

import java.util.HashMap;
import java.util.Map;

/**
 * 设备通讯方式
 *

 * @date 2019年10月19日
 */
public class ModeConstants {

    public final static String RESULT_CODE = "code";
    public final static String RESULT_MSG = "msg";
    public final static String RESULT_DATA = "data";
    public final static String RESULT_CLIENT_ID = "clientid";

    /**
     * 东信LORA
     */
    public final static String DX_LORA = "1";
    /**
     * 东信NB移动
     */
    public final static String DX_NB_YD = "2";
    /**
     * 东信NB电信
     */
    public final static String DX_NB_DX = "3";
    /**
     * 鼎通MBUS
     */
    public final static String DT_MBUS = "6";
    /**
     * 华奥通NB
     */
    public final static String HAT_NB = "7";
    /**
     * 华奥通LORA
     */
    public final static String HAT_LORA = "8";
    /**
     * 视频直读
     */
    public final static String SPZD = "9";
    /**
     * 东信NB联通
     */
    public final static String DX_NB_LT = "10";

    /**
     * 康宝莱
     */
    public final static String COMBINE = "11";

    public static Map<String, String> ModeMap = new HashMap<>();

    static {
        ModeMap.put(DX_LORA, TxCode.DX_LORA_COMMAND);
        ModeMap.put(DX_NB_YD, TxCode.DX_NB_YD_COMMAND);
        ModeMap.put(DX_NB_DX, TxCode.DX_NB_DX_COMMAND);
        ModeMap.put(DT_MBUS, TxCode.DT_MBUS_COMMAND);
        ModeMap.put(HAT_NB, TxCode.HAT_NB_COMMAND);
        ModeMap.put(HAT_LORA, TxCode.HAT_LORA_COMMAND);
        ModeMap.put(SPZD, TxCode.SPZD_COMMAND);
        ModeMap.put(DX_NB_LT, TxCode.DX_NB_LT_COMMAND);
    }
}
