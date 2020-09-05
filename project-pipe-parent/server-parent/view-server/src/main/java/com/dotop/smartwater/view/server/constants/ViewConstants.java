package com.dotop.smartwater.view.server.constants;

import com.alibaba.fastjson.JSON;

import java.util.HashMap;
import java.util.Map;

/**
 * 大屏展示常量
 *
 *
 */
public class ViewConstants {

    /**
     * 初始化多少个月之前的数据（包括当前月）
     */
    public static final Integer INIT_MONTHS = 24;

    /**
     * 数据密度
     */
    public static final String DATA_DENSITY_HOUR = "hour";

    /**
     * 数据密度
     */
    public static final String DATA_DENSITY_DAY = "day";

    /**
     * 数据密度
     */
    public static final String DATA_DENSITY_MONTH = "month";


    /*start------------------------------------------------累计供水量-------------------------------------------------*/

    /**
     * 供水量(类别)
     */
    public static final String WATER_SUPPLY = "water_supply";

    /**
     * 商业用水(类型)
     */
    public static final String COMMERCIAL_WATER = "commercial_water";

    /**
     * 工业用水(类型)
     */
    public static final String INDUSTRIAL_WATER = "industrial_water";

    /**
     * 居民用水(类型)
     */
    public static final String RESIDENTIAL_WATER = "residential_water";

    /**
     * 商业用水系数
     */
    public static final Double COMMERCIAL_WATER_COEFFICIENT = 0.238D;

    /**
     * 工业用水系数
     */
    public static final Double INDUSTRIAL_WATER_COEFFICIENT = 0.326D;

    /**
     * 居民用水系数
     */
    public static final Double RESIDENTIAL_WATER_COEFFICIENT = 0.436D;

    /*end------------------------------------------------累计供水量-------------------------------------------------*/

    /*start-----------------------------------------------终端数量-------------------------------------------------*/
    /**
     * 终端数量(类别),类型分为流量计，压力计，水质计
     */
    public static final String TERMINAL_NUMBER = "terminal_number";

    /**
     * 流量计数量
     */
    public static final String FM_NUMBER = "fm_number";
    /**
     * 压力计数量
     */
    public static final String PM_NUMBER = "pm_number";
    /**
     * 水质计数量
     */
    public static final String WM_NUMBER = "wm_number";
    /*end------------------------------------------------终端数量-------------------------------------------------*/
    /*start----------------------------------------------管网-------------------------------------------------*/
    /**
     * 管网长度(类别)
     */
    public static final String PIPE_LENGTH = "pipe_length";
    /*end------------------------------------------------管网-------------------------------------------------*/
    /*start--------------------------------------------用户数量-------------------------------------------------*/
    /**
     * 用户数量(类别)
     */
    public static final String OWNER_NUMBER = "owner_number";
    /**
     * 普通用户(类型)
     */
    public static final String ORDINARY_NUMBER = "ordinary_number";
    /**
     * 商业用户(类型)
     */
    public static final String COMMERCIAL_NUMBER = "commercial_number";
    /**
     * 工业用户(类型)
     */
    public static final String INDUSTRIAL_NUMBER = "industrial_number";
    /**
     * 普通用户系数
     */
    public static final Double ORDINARY_COEFFICIENT = 0.436D;

    /**
     * 商业用户系数
     */
    public static final Double COMMERCIAL_COEFFICIENT = 0.238D;

    /**
     * 工业用户系数
     */
    public static final Double INDUSTRIAL_COEFFICIENT = 0.326D;
    /*end----------------------------------------------用户数量-------------------------------------------------*/
    /*start--------------------------------------------区域用水量-------------------------------------------------*/
    /**
     * 区域用水量(类别)
     */
    public static final String AREA_WATER = "area_water";
    /**
     * DMA分区用水量
     */
    public static final String DMA_WATER = "dma_water";
    /*end----------------------------------------------区域用水量-------------------------------------------------*/

    // 工单处理状态
// 完成
    public static final String WORK_ORDER_COMPLETE = "1";
    // 未处理
    public static final String WORK_ORDER_UNTREATED = "-2";
    // 处理中
    public static final String WORK_ORDER_HANDEDING = "0";
    // 挂起
    public static final String WORK_ORDER_HANG_UP = "2";


    /*start--------------------------------------------传感器-------------------------------------------------*/
    /**
     * 传感器(类别)
     */
    public static final String SENSOR = "sensor";
    /**
     * 流量计(类型)
     */
    public static final String SENSOR_FM = "fm";
    /**
     * 压力计(类型)
     */
    public static final String SENSOR_PM = "pm";
    /*end----------------------------------------------传感器-------------------------------------------------*/

    /*start--------------------------------------------水厂，水厂沉淀池------------------------------------------------*/
    /**
     * 水厂(类别)
     */
    public static final String WATER_FACTORY = "water_factory";
    /**
     * 水厂进水量(类型)
     */
    public static final String WATER_FACTORY_IN_WATER = "in_water";

    public static final String WATER_FACTORY_IN_WATER_CURR = "in_water_curr";
    /**
     * 水厂出水量(类型)
     */
    public static final String WATER_FACTORY_OUT_WATER = "out_water";
    public static final String WATER_FACTORY_OUT_WATER_CURR = "out_water_curr";
    /**
     * 水厂用电(类型)
     */
    public static final String WATER_FACTORY_USED_ELECTRICITY = "used_electricity";
    /**
     * 水厂水压(类型)
     */
    public static final String WATER_FACTORY_PM = "pm";

    public static final String WATER_FACTORY_PH = "ph";

    /**
     * 水厂进水水压(类型)
     */
    public static final String WATER_FACTORY_IN_PM = "in_pm";

    /**
     * 水厂出水水压(类型)
     */
    public static final String WATER_FACTORY_OUT_PM = "out_pm";

    /**
     * 水厂进水流速(类型)
     */
    public static final String WATER_FACTORY_IN_FM = "in_fm";

    /**
     * 水厂出水流速(类型)
     */
    public static final String WATER_FACTORY_OUT_FM = "out_fm";

    /**
     * 水厂温度(类型)
     */
    public static final String WATER_FACTORY_TEMPERATURE = "temperature";

    /**
     * 水厂进水浑浊度(类型)
     */
    public static final String WATER_FACTORY_IN_TURBID = "in_turbid";

    /**
     * 水厂出水浑浊度(类型)
     */
    public static final String WATER_FACTORY_OUT_TURBID = "out_turbid";

    /**
     * 水厂浑浊度
     */
    public static final String WATER_FACTORY_TURBID = "turbid";

    /**
     * 水厂含氯值(类型)
     */
    public static final String WATER_FACTORY_CHLORINE = "chlorine";

    /**
     * 水厂进水氧值(类型)
     */
    public static final String WATER_FACTORY_IN_OXYGEN = "in_oxygen";

    /**
     * 水厂出水氧值(类型)
     */
    public static final String WATER_FACTORY_OUT_OXYGEN = "out_oxygen";
    public static final String WATER_FACTORY_IN_PH = "in_ph";
    public static final String WATER_FACTORY_OUT_PH = "out_ph";

    /**
     * 水厂药物余量
     */
    public static final String WATER_FACTORY_DRUG = "drug";

    /**
     * 水厂沉淀池(类别)
     */
    public static final String WATER_FACTORY_PRECIPITATE_POND = "water_factory_precipitate_pond";

    /**
     * 水厂药池(类型)
     */
    public static final String WATER_FACTORY_MEDICINE_POND = "water_factory_medicine_pond";

    /**
     * 水厂过滤池(类型)
     */
    public static final String WATER_FACTORY_FILTER_POND = "water_factory_filter_pond";

    /**
     * 水厂高压崩池(类型)
     */
    public static final String WATER_FACTORY_HIGH_PUMP_POND = "water_factory_high_pump_pond";

    /**
     * 水厂双阀滤池
     */
    public static final String WATER_FACTORY_DOUBLE_VALVE_FILTER_POND = "water_factory_double_valve_filter_pond";

    /**
     * 水厂清水池
     */
    public static final String WATER_FACTORY_SHIMIZU_POND = "water_factory_shimizu_pond";

    /**
     * 水厂协管滤池
     */
    public static final String WATER_FACTORY_TUBE_FILTER_POND = "water_factory_tube_filter_pond";

    /**
     * 水厂反应池
     */
    public static final String WATER_FACTORY_REACTION_POND = "water_factory_reaction_pond";

    /**
     * 水厂溢流池
     */
    public static final String WATER_FACTORY_OVERFLOW_POND = "water_factory_overflow_pond";

    /*end----------------------------------------------水厂------------------------------------------------*/


    // 参数及其比例
    public final static Map<String, Map<String, String>> water_factory_properties = new HashMap<String, Map<String, String>>() {
        {
            // 水厂的数据范围
            put(WATER_FACTORY, new HashMap<String, String>() {
                {   // 水压
                    put(WATER_FACTORY_IN_PM, "{'max':0.8,'min':0.1}");
                    put(WATER_FACTORY_OUT_PM, "{'max':0.8,'min':0.1}");
                    put(WATER_FACTORY_PM, "{'max':0.8,'min':0.1}");
                    // 流量
                    put(WATER_FACTORY_IN_FM, "{'max':20,'min':10}");
                    put(WATER_FACTORY_OUT_FM, "{'max':20,'min':10}");
                    // 浊度
                    put(WATER_FACTORY_IN_TURBID, "{'max':1.1,'min':0}");
                    put(WATER_FACTORY_OUT_TURBID, "{'max':0.9,'min':0.1}");
                    // 温度
                    put(WATER_FACTORY_TEMPERATURE, "{'max':60,'min':30}");
                    // 氯气
                    put(WATER_FACTORY_CHLORINE, "{'max':0.5,'min':0.3}");
                    // 电量
                    put(WATER_FACTORY_USED_ELECTRICITY, "{'max':1000,'min':100}");

                    // 含氧量
                    put(WATER_FACTORY_IN_OXYGEN, "{'max':9,'min':6}");
                    put(WATER_FACTORY_OUT_OXYGEN, "{'max':9,'min':6}");

                    // ph值
                    put(WATER_FACTORY_IN_PH, "{'max':9.2,'min':5.8}");
                    put(WATER_FACTORY_OUT_PH, "{'max':8.7,'min':6.3}");

                    put(WATER_FACTORY_IN_WATER_CURR, "{'max':500,'min':'300'}");
                }
            });
            // 沉淀池
            put(WATER_FACTORY_PRECIPITATE_POND, new HashMap<String, String>() {
                {// 浊度
                    put(WATER_FACTORY_TURBID, "{'max':1.1,'min':0}");
                }
            });

            // 加药池
            put(WATER_FACTORY_MEDICINE_POND, new HashMap<String, String>() {
                {  // 含氯
                    put(WATER_FACTORY_CHLORINE, "{'max':0.5,'min':0.3}");
                    put(WATER_FACTORY_TEMPERATURE, "{'max':60,'min':30}");
                    put(WATER_FACTORY_PM, "{'max':0.8,'min':0.1}");
                    put(WATER_FACTORY_DRUG, "{'max':60,'min':0}");
                }
            });
            // 高压泵
            put(WATER_FACTORY_HIGH_PUMP_POND, new HashMap<String, String>() {
                {  // 压力值
                    put(WATER_FACTORY_PM, "{'max':0.8,'min':0.1}");
                    put(WATER_FACTORY_TURBID, "{'max':1.1,'min':0}");
                }
            });
            // 双阀滤池
            put(WATER_FACTORY_DOUBLE_VALVE_FILTER_POND, new HashMap<String, String>() {
                {// 浊度
                    put(WATER_FACTORY_PM, "{'max':0.8,'min':0.1}");
                    put(WATER_FACTORY_TURBID, "{'max':1.1,'min':0}");
                }
            });
            // 清水池
            put(WATER_FACTORY_SHIMIZU_POND, new HashMap<String, String>() {
                {   // 压力
                    // put(WATER_FACTORY_PM, "{'max':20,'min':10}");
                    // put(WATER_FACTORY_TURBID,"{'max':3.1,'min':0.9}");
                    put(WATER_FACTORY_PH, "{'max':8.7,'min':6.3}");
                }
            });

            // 协管滤池
            put(WATER_FACTORY_TUBE_FILTER_POND, new HashMap<String, String>() {
                {// 浊度
                    put(WATER_FACTORY_DRUG, "{'max':60,'min':0}");
                }
            });
            // 反应池
            put(WATER_FACTORY_REACTION_POND, new HashMap<String, String>() {
                {// 浊度
                    put(WATER_FACTORY_PM, "{'max':0.8,'min':0.1}");
                }
            });
            // 溢流池
            put(WATER_FACTORY_OVERFLOW_POND, new HashMap<String, String>() {
                {// 浊度
                    put(WATER_FACTORY_DRUG, "{'max':60,'min':0}");
                }
            });
        }
    };
}
