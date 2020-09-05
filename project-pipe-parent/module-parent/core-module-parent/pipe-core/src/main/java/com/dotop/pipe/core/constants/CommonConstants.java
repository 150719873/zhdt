package com.dotop.pipe.core.constants;

import org.apache.commons.lang3.StringUtils;

/**
 * 字典常量
 *
 *
 * @date 2018年10月29日
 */
public class CommonConstants {

    // 字典-产品类别
    // 字典-产品类别-管道
    public final static String PRODUCT_CATEGORY_PIPE = "pipe";
    // 字典-产品类别-传感器
    public final static String PRODUCT_CATEGORY_SENSOR = "sensor";
    // 字典-产品类别-节点
    public final static String PRODUCT_CATEGORY_NODE = "node";
    // 字典-产品类别-堵头封板
    public final static String PRODUCT_CATEGORY_PLUG = "plug";
    // 字典-产品类别-阀门
    public final static String PRODUCT_CATEGORY_VALVE = "valve";
    // 字典-产品类别-消防栓
    public final static String PRODUCT_CATEGORY_HYDRANT = "hydrant";
    // 字典-产品类别-水厂
    public final static String PRODUCT_CATEGORY_WATER_FACTORY = "water_factory";
    // 字典-产品类别-污水厂
    public final static String PRODUCT_CATEGORY_SLOPS_FACTORY = "slops_factory";
    // 字典-产品类别-自定义设备
    public final static String PRODUCT_CATEGORY_CUSTOMIZE_DEVICE = "customize_device";
    // 字典-产品类别-片区
    public final static String PRODUCT_CATEGORY_REGION = "region";
    // 字典-产品类别-加压泵
    public final static String PRODUCT_CATEGORY_PUMP = "pump";

    // 字典
    // 字典-产品类别
    public final static String DICTIONARY_TYPE_PRODUCTCATEGORY = "category";
    // 字典-口径
    public final static String DICTIONARY_TYPE_CALIBER = "caliber";
    // 字典-材质
    public final static String DICTIONARY_TYPE_MATERIAL = "material";
    // 字典-传感器类型
    public final static String DICTIONARY_TYPE_SENSORTYPE = "sensor";
    // 字典-厂商编码
    public final static String DICTIONARY_TYPE_FACTORYCODE = "factory";
    // 字典-敷设类型
    public final static String DICTIONARY_TYPE_LAYING = "laying";

    // 字典-传感器-类型
    // 字典-传感器-流量计
    public final static String DICTIONARY_SENSORTYPE_FM = "fm";
    // 字典-传感器-压力计
    public final static String DICTIONARY_SENSORTYPE_PM = "pm";
    // 字典-传感器-水质计
    public final static String DICTIONARY_SENSORTYPE_WM = "wm";
    // 字典-传感器-流量压力计
    public final static String DICTIONARY_SENSORTYPE_FPM = "fpm";

    // 字典-厂商编码-编码
    // 字典-厂商编码-其他
    public final static String DICTIONARY_FACTORYCODE_OTHER = "OTHER";
    // 字典-厂商编码-康宝莱
    public final static String DICTIONARY_FACTORYCODE_KBL = "KBL";

    public final static String get(String key) {
        if (StringUtils.isEmpty(key)) {
            return null;
        }
        switch (key) {
            case CommonConstants.PRODUCT_CATEGORY_PIPE:
                return "管道";
            case CommonConstants.PRODUCT_CATEGORY_SENSOR:
                return "传感器";
            case CommonConstants.PRODUCT_CATEGORY_NODE:
                return "节点";
            case CommonConstants.PRODUCT_CATEGORY_PLUG:
                return "堵头封板";
            case CommonConstants.PRODUCT_CATEGORY_VALVE:
                return "阀门";
            case CommonConstants.PRODUCT_CATEGORY_HYDRANT:
                return "消防栓";
            case CommonConstants.PRODUCT_CATEGORY_WATER_FACTORY:
                return "水厂";
            default:
                return null;
        }
    }

    public final static String reverse(String name) {
        if (StringUtils.isEmpty(name)) {
            return null;
        }
        switch (name) {
            case "管道":
                return CommonConstants.PRODUCT_CATEGORY_PIPE;
            case "传感器":
                return CommonConstants.PRODUCT_CATEGORY_SENSOR;
            case "节点":
                return CommonConstants.PRODUCT_CATEGORY_NODE;
            case "堵头封板":
                return CommonConstants.PRODUCT_CATEGORY_PLUG;
            case "阀门":
                return CommonConstants.PRODUCT_CATEGORY_VALVE;
            case "消防栓":
                return CommonConstants.PRODUCT_CATEGORY_HYDRANT;
            /*case "水厂":
                return CommonConstants.PRODUCT_CATEGORY_WATER_FACTORY;*/
            default:
                return null;
        }
    }

    /**
     * 是否是水务平台的产品
     */
    public final static boolean isWaterProduct(String category) {
        if (CommonConstants.PRODUCT_CATEGORY_PIPE.equals(category)
                || CommonConstants.PRODUCT_CATEGORY_SENSOR.equals(category)
                || CommonConstants.PRODUCT_CATEGORY_NODE.equals(category)
                || CommonConstants.PRODUCT_CATEGORY_PLUG.equals(category)
                || CommonConstants.PRODUCT_CATEGORY_VALVE.equals(category)
                || CommonConstants.PRODUCT_CATEGORY_HYDRANT.equals(category)

//                || CommonConstants.PRODUCT_CATEGORY_CUSTOMIZE_DEVICE.equals(category)
//                || CommonConstants.PRODUCT_CATEGORY_WATER_FACTORY.equals(category)
//                || CommonConstants.PRODUCT_CATEGORY_SLOPS_FACTORY.equals(category)
//                || CommonConstants.PRODUCT_CATEGORY_REGION.equals(category)
        ) {
            return true;
        }
        return false;
    }
}
