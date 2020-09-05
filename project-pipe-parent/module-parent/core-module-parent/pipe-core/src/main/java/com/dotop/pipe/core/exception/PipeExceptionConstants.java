package com.dotop.pipe.core.exception;

import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class PipeExceptionConstants extends BaseExceptionConstants {

    // 坐标
    public static final String POINT_CODE_EXIST = "-890001";
    public static final String POINT_USE = "-890002";

    // 管道
    public static final String PIPE_CODE_EXIST = "-880001";

    // 区域
    public static final String AREA_CODE_EXIST = "-870001";

    // 区域
    public static final String AREA_UPDATE_ERROR = "-870002";
    public static final String AREA_USE = "-870003";

    // 传感器
    public static final String SENEOR_PARAMETER_NOT_EXIST = "-860001";
    public static final String SENSOR_CODE_EXIST = "-860002";
    // 传感器更新
    public static final String SENSOR_UPDATE_ERROR = "-860003";
    // 传感器第三方创建失败
    public static final String SENSOR_THIRD_CREATE_ERROR = "-860004";
    // 传感器本地thirdMap不存在
    public static final String SENSOR_THIRD_MAP_NOT_EXIST = "-860005";
    // 警报处理失败
    public static final String ALARM_UPDATE_ERROR = "-850001";
    // 警报新增失败
    public static final String ALARM_ADD_ERROR = "-850002";

    // 节点更新失败
    public static final String NODE_UPDATE_ERROR = "-840001";
    // 节点删除失败
    public static final String NODE_DEL_ERROR = "-840002";
    // 节点已存在
    public static final String NODE_CODE_EXIST = "-840003";

    // 消防栓
    public static final String HYDRANT_UPDATE_ERROR = "-830001";
    public static final String HYDRANT_DEL_ERROR = "-830002";
    public static final String HYDRANT_CODE_EXIST = "-830003";
    // 堵头
    public static final String PLUG_UPDATE_ERROR = "-820001";
    public static final String PLUG_DEL_ERROR = "-820002";
    public static final String PLUG_CODE_EXIST = "-820003";

    // 阀门
    public static final String VALVE_UPDATE_ERROR = "-810001";
    public static final String VALVE_DEL_ERROR = "-810002";
    public static final String VALVE_CODE_EXIST = "-810003";

    // 设备编码已经存在
    public static final String DEVICE_CODE_EXIST = "-800001";
    // 设备更新失败
    public static final String DEVICE_UPDATE_ERROR = "-800002";
    // 设备删除失败
    public static final String DEVICE_DEL_ERROR = "-800003";
    // 设备的设施已经绑定存在
    public static final String DEVICE_PRODUCT_EXIST = "-800004";

    // 工单编号已经存在
    public static final String WORK_ORDER_CODE_EXIST = "-790001";

    // 文件上传
    public static final String FILE_UPLOAD_ERROR = "-780001";

    // 定时公式编码已存在
    public static final String TIMING_CALCULATION_CODE_EXIST = "-770001";

    // 字典已存在
    public static final String DICTIONARY_EXIST = "-760001";
    // 字典被使用
    public static final String DICTIONARY_USED = "-760002";

    // 厂商已存在
    public static final String FACTORY_EXIST = "-750001";
    // 厂商被使用
    public static final String FACTORY_USED = "-750002";

    // 上传出错
    public static final String COMMON_UPLOAD_ERR = "-740001";

    private static final Map<String, String> msgMap = new HashMap<String, String>(getBaseMap()) {
        private static final long serialVersionUID = 6909788987793614246L;

        {
            // 坐标
            put(POINT_CODE_EXIST, "坐标编号已存在");
            put(POINT_USE, "坐标编号正在被使用");
            // 管道
            put(PIPE_CODE_EXIST, "管道编号已存在");
            // 区域
            put(AREA_CODE_EXIST, "区域编号已存在");
            put(AREA_UPDATE_ERROR, "区域更新错误");
            put(AREA_USE, "区域正在被使用");
            put(SENEOR_PARAMETER_NOT_EXIST, "参数不存在");
            put(SENSOR_CODE_EXIST, "code 已存在");
            put(SENSOR_THIRD_CREATE_ERROR, "第三方创建传感器失败");
            put(SENSOR_THIRD_MAP_NOT_EXIST, "ThirdMap 不存在");
            put(ALARM_UPDATE_ERROR, "报警处理失败");
            put(ALARM_ADD_ERROR, "报警新增失败");
            put(NODE_UPDATE_ERROR, "节点更新失败");
            put(NODE_DEL_ERROR, "节点删除失败");
            put(NODE_CODE_EXIST, "节点编号已存在");
            put(HYDRANT_CODE_EXIST, "消防栓编号已存在");
            put(HYDRANT_UPDATE_ERROR, "消防栓更新失败");
            put(HYDRANT_DEL_ERROR, "消防栓删除失败");
            put(VALVE_CODE_EXIST, "阀门编号已存在");
            put(VALVE_UPDATE_ERROR, "阀门更新失败");
            put(VALVE_DEL_ERROR, "阀门删除失败");
            put(PLUG_CODE_EXIST, "堵头编号已存在");
            put(PLUG_UPDATE_ERROR, "堵头更新失败");
            put(PLUG_DEL_ERROR, "堵头删除失败");

            put(DEVICE_CODE_EXIST, "设备编码已经存在");
            put(DEVICE_UPDATE_ERROR, "设备更新失败");
            put(DEVICE_DEL_ERROR, "设备删除失败");
            put(DEVICE_PRODUCT_EXIST, "设施产品已经存在绑定");

            put(WORK_ORDER_CODE_EXIST, "工单编号已经存在");

            put(TIMING_CALCULATION_CODE_EXIST, "定时公式编码已存在");

            put(DICTIONARY_EXIST, "字典已存在");
            put(DICTIONARY_USED, "字典被使用中");

            put(FACTORY_EXIST, "厂商已存在");
            put(FACTORY_USED, "厂商被使用中");
            put(COMMON_UPLOAD_ERR, "文件处理错误");
        }
    };

    public final static String getMessage(String code, String... params) {
        String str = msgMap.get(code);
        if (StringUtils.isEmpty(str)) {
            return null;
        }
        if (params != null) {
            for (int i = 0; i < params.length; i++) {
                StringBuffer sb = new StringBuffer("{");
                sb.append(i).append("}");
                str = StringUtils.replace(str, sb.toString(), params[i]);
            }
        }
        return str;
    }
}
