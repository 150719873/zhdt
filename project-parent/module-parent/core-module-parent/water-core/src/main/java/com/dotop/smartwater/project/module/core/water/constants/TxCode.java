package com.dotop.smartwater.project.module.core.water.constants;

import java.util.HashMap;
import java.util.Map;

/**
 * TODO 迁移water-core 常量
 *

 */
public class TxCode {

   /**下发命令返回状态*/
    public static final int DOWNLINK_STATUS_WAIT = 0;
    public static final int DOWNLINK_STATUS_SUCCESS = 1;
    public static final int DOWNLINK_STATUS_FAIL = 3;


    // 关阀
    public static final int CloseCommand = 1;

    // 开阀
    public static final int OpenCommand = 2;

    // 实时抄表
    public static final int GetWaterCommand = 3;

    // 疏通阀门周期
    public static final int CycleCommand = 4;

    /**
     * 水表调整
     *
     * @author chenjiayi
     */
    public static final int MeterOper = 5;

    /**
     * 水表复位
     *
     * @author chenjiayi
     */
    public static final int Reset = 6;

    /**
     * 设置生命状态
     *
     * @author KJR
     */
    public static final int SetLifeStatus = 7;


    /**
     * 设置定时重启周期
     *
     * @author KJR
     */
    public static final int ResetPeriod = 8;

    /**
     * 设置上报周期
     *
     * @author KJR
     */
    public static final int SetUploadTime = 9;


    /**
     * 定义每种通讯协议有哪些下发指令
     * 返回前端方便控制权限
     * **/
    public static final String DX_LORA_COMMAND = "1,2,3,5,6,8";

    public static final String DX_NB_YD_COMMAND = "1,2,3,5,6,7,8";
    public static final String DX_NB_DX_COMMAND = "1,2,3,5,6,7,8";
    public static final String DX_NB_LT_COMMAND = "1,2,3,5,6,7,8";

    public static final String DT_MBUS_COMMAND = "";
    public static final String HAT_NB_COMMAND ="1,2,3,5,9";
    public static final String HAT_LORA_COMMAND = "1,2,3,5,9";
    public static final String SPZD_COMMAND = "";



    public static Map<Integer,String> TxCodeMap = new HashMap<>();
    static {
        TxCodeMap.put(CloseCommand, "关阀指令");
        TxCodeMap.put(OpenCommand, "开阀指令");
        TxCodeMap.put(GetWaterCommand, "实时抄表");
        TxCodeMap.put(CycleCommand, "疏通阀门周期");
        TxCodeMap.put(MeterOper, "水表调整");
        TxCodeMap.put(Reset, "水表复位");
        TxCodeMap.put(SetLifeStatus, "设置生命状态");
        TxCodeMap.put(ResetPeriod, "设置定时重启周期");
        TxCodeMap.put(SetUploadTime, "设置上报周期");
    }
}
