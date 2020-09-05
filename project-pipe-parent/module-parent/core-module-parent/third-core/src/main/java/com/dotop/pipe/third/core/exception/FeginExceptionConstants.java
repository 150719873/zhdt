package com.dotop.pipe.third.core.exception;

import java.util.HashMap;
import java.util.Map;

import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import org.apache.commons.lang3.StringUtils;


public class FeginExceptionConstants extends BaseExceptionConstants {

	// 桥接
	public static final String THIRD_RUNTIME_ERROR = "-690001";
	public static final String THIRD_CREATE_SENSOR_ERROR = "-690002";
	public static final String THIRD_MODIFY_SENSOR_ERROR = "-690003";
	public static final String THIRD_GET_SENSOR_ERROR = "-690004";
	public static final String THIRD_DEL_SENSOR_ERROR = "-690005";

	// 调度中心
	public static final String DISPATCH_APPLY_ERROR = "-690006";

	private static final Map<String, String> msgMap = new HashMap<String, String>(getBaseMap()) {
		private static final long serialVersionUID = 6909788987793614246L;
		{
			// 桥接
			put(THIRD_RUNTIME_ERROR, "远程调用失败");
			put(THIRD_CREATE_SENSOR_ERROR, "远程创建传感器失败");
			put(THIRD_MODIFY_SENSOR_ERROR, "远程修改传感器失败");
			put(THIRD_GET_SENSOR_ERROR, "远程获取传感器信息失败");
			put(THIRD_DEL_SENSOR_ERROR, "远程删除传感器信息失败");
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
