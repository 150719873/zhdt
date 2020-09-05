package com.dotop.smartwater.project.module.core.water.constants;

import static com.dotop.smartwater.project.module.core.water.constants.WaterConstants.DICTIONARY_0;
import static com.dotop.smartwater.project.module.core.water.constants.WaterConstants.DICTIONARY_1;

/**
 * @program: project-parent
 * @description: 字典编码

 * @create: 2019-03-04 09:28
 **/
public class DictionaryCode {

    public final static String copyDictionaryId(String dictionaryId, String targetEnterpriseid) {
        String[] parts = dictionaryId.split(",");
        if (parts[0].equals(WaterConstants.ADMIN_ENTERPRISE_ID)) {
            return dictionaryId;
        }
        return targetEnterpriseid + "," + parts[1];
    }

    public final static String copyChildId(String childId, String targetEnterpriseid) {
        String[] parts = childId.split(",");
        if (parts[0].equals(WaterConstants.ADMIN_ENTERPRISE_ID)) {
            return childId;
        }
        return targetEnterpriseid + "," + parts[1] + "," + parts[2];
    }

    public final static String getChildValue(String childId) {
        if(childId == null){
            return null;
        }
        String[] parts = childId.split(",");
        return parts[2];
    }

    public final static String getDictionaryId(String enterpriseid, String dictionaryCode) {
        return enterpriseid + "," + dictionaryCode;
    }

    public final static String getChildId(String enterpriseid, String dictionaryCode, String ChildValue) {
        return enterpriseid + "," + dictionaryCode + "," + ChildValue;
    }

    /**
     * 设备上报
     */
    public final static String DEVICE_UPLOAD_REASON = "28,300014";

    /**
     * 区分运维还是水司用
     */
    public final static String DICTIONARY_PROPERTY_ADMIN = WaterConstants.ADMIN_ENTERPRISE_ID;
    public final static String DICTIONARY_PROPERTY_PRIVATE = "private";
    public final static String DICTIONARY_PROPERTY_PUBLIC = "public";

    /**
     * 工作中心表单数据源属性类型
     */
    public final static String DICTIONARY_DATASOURCE_TYPR_INPUT = "1";
    public final static String DICTIONARY_DATASOURCE_TYPR_SELECT = "2";
    public final static String DICTIONARY_DATASOURCE_TYPR_DATE = "3";
    public final static String DICTIONARY_DATASOURCE_TYPR_TEXT = "4";
    public final static String DICTIONARY_DATASOURCE_TYPR_SQL = "5";

    /**
     * 工作中心表单数据源sql条件属性类型
     */
    public final static String DICTIONARY_DATASOURCE_SQL_TYPR_STRING = "1";
    public final static String DICTIONARY_DATASOURCE_SQL_TYPR_LIST = "2";
    public final static String DICTIONARY_DATASOURCE_SQL_TYPR_DATE = "3";

    /**
     * 工作中心业务更新类型 1 营抄账单审核更新
     */
    public final static String DICTIONARY_BUSINESS_UPDATE_ORDER_AUDITING = "1";

    /**
     * 判断是否
     */
    public final static String DICTIONARY_JUDGE_NO = DICTIONARY_0;
    public final static String DICTIONARY_JUDGE_YSE = DICTIONARY_1;

    /**
     * 判断通过不通过
     */
    public final static String DICTIONARY_JUDGE_DENY = DICTIONARY_0;
    public final static String DICTIONARY_JUDGE_PASS = DICTIONARY_1;
    /**
     * 判断是否验证
     */
    public final static String DICTIONARY_AUTH_NO = DICTIONARY_0;
    public final static String DICTIONARY_AUTH_YSE = DICTIONARY_1;

    /**
     * 是/否 同意/不同意 批准/不批准 通过/不通过
     */
    public final static String JUDGE_CODE = "990002";

    /**
     * 通讯方式
     */
    public final static String DEVICE_MODE = "300001";

    /**
     * 工作中心表单数据源属性类型
     */
    public final static String DICTIONARY_DATASOURCE_TYPR = "200001";

    /**
     * 水表用途
     */
    public final static String DICTIONARY_PURPOSE = "300007";


    /**
     * 管漏
     */
    /**
     * 类别
     */
    public static final String PLS_CATEGORY = WaterConstants.ADMIN_ENTERPRISE_ID + "," + "800001";
    /**
     * 传感器
     */
    public static final String PLS_SENSOR = WaterConstants.ADMIN_ENTERPRISE_ID + "," + "800002";
    /**
     * 阀门
     */
    public static final String PLS_VALVE = WaterConstants.ADMIN_ENTERPRISE_ID + "," + "800003";
    /**
     * 管道
     */
    public static final String PLS_PIPE = WaterConstants.ADMIN_ENTERPRISE_ID + "," + "800004";
    /**
     * 节点
     */
    public static final String PLS_NODE = WaterConstants.ADMIN_ENTERPRISE_ID + "," + "800005";
    /**
     * 堵头封板
     */
    public static final String PLS_PLUG = WaterConstants.ADMIN_ENTERPRISE_ID + "," + "800006";
    /**
     * 消防栓
     */
    public static final String PLS_HYDRANT = WaterConstants.ADMIN_ENTERPRISE_ID + "," + "800007";
    /**
     * 水厂
     */
    public static final String PLS_WATER_FACTORY = WaterConstants.ADMIN_ENTERPRISE_ID + "," + "800008";
    /**
     * 污水厂
     */
    public static final String PLS_SLOPS_FACTORY = WaterConstants.ADMIN_ENTERPRISE_ID + "," + "800009";
    /**
     * 自定义设备
     */
    public static final String PLS_CUSTOMIZE_DEVICE = WaterConstants.ADMIN_ENTERPRISE_ID + "," + "800010";
    /**
     * 片区
     */
    public static final String PLS_REGION = WaterConstants.ADMIN_ENTERPRISE_ID + "," + "800011";

    /**
     * 敷设
     */
    public static final String PLS_LAYING = WaterConstants.ADMIN_ENTERPRISE_ID + "," + "800101";
    /**
     * 材质
     */
    public static final String PLS_MATERIAL = WaterConstants.ADMIN_ENTERPRISE_ID + "," + "800102";
}
