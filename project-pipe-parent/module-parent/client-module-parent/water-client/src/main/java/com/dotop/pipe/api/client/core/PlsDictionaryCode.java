package com.dotop.pipe.api.client.core;

import com.dotop.pipe.core.constants.CommonConstants;
import com.dotop.smartwater.project.module.core.water.constants.DictionaryCode;
import org.apache.commons.lang3.StringUtils;

/**
 *
 */
public final class PlsDictionaryCode extends DictionaryCode {

    public static String getDictionaryId(String categoryValue) {
        if (StringUtils.isBlank(categoryValue)) {
            return null;
        }
        categoryValue = categoryValue.toLowerCase();
        switch (categoryValue) {
            case CommonConstants.DICTIONARY_TYPE_PRODUCTCATEGORY:
                // 类别
                return DictionaryCode.PLS_CATEGORY;
            case CommonConstants.PRODUCT_CATEGORY_SENSOR:
                // 计量计
//                return DictionaryCode.PLS_SENSOR;
                return "28,900003";
            case CommonConstants.PRODUCT_CATEGORY_PIPE:
                // 管道
//                return DictionaryCode.PLS_PIPE;
                return "28,900002";
            case CommonConstants.PRODUCT_CATEGORY_NODE:
                // 节点
//                return DictionaryCode.PLS_NODE;
                return "28,900008";
            case CommonConstants.PRODUCT_CATEGORY_PLUG:
                // 堵头
//                return DictionaryCode.PLS_PLUG;
                return "28,900009";
            case CommonConstants.PRODUCT_CATEGORY_VALVE:
                // 阀门
//                return DictionaryCode.PLS_VALVE;
                return "28,900006";
            case CommonConstants.PRODUCT_CATEGORY_HYDRANT:
                // 消防栓
//                return DictionaryCode.PLS_HYDRANT;
                return "28,900007";
            case CommonConstants.PRODUCT_CATEGORY_WATER_FACTORY:
                // 水厂
                return DictionaryCode.PLS_WATER_FACTORY;
            case CommonConstants.PRODUCT_CATEGORY_SLOPS_FACTORY:
                // 污水厂
                return DictionaryCode.PLS_SLOPS_FACTORY;
            case CommonConstants.PRODUCT_CATEGORY_CUSTOMIZE_DEVICE:
                // 自定义设备
                return DictionaryCode.PLS_CUSTOMIZE_DEVICE;
            case CommonConstants.PRODUCT_CATEGORY_REGION:
                // 片区
                return DictionaryCode.PLS_REGION;
            case CommonConstants.DICTIONARY_TYPE_LAYING:
                // 敷设
                return DictionaryCode.PLS_LAYING;
            case CommonConstants.DICTIONARY_TYPE_MATERIAL:
                // 材质
//                return DictionaryCode.PLS_MATERIAL;
                return "28,900020";
            default:
                return null;
        }
    }
}
