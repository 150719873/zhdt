package com.dotop.smartwater.project.module.core.water.constants;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;

/**
 * 迁移water-core 常量
 *

 *         <p>
 *         旧code为兼容不改动，新core需要按照代码规范编写
 */
public class AppCode extends BaseExceptionConstants {

	/**
	 * 成功完成提交
	 */
	public static final String SUCCESS = "100";
	/**
	 * Token错误
	 */
	public static final String TOKEN_ERROR = "2";
	/**
	 * 服务端捕获到的异常, APP可直接打印MSG
	 */
	public static final String SERVICE_ERROR = "4";

	public static final String Token_Expired = "3";

	/**
	 * 请求的JSON体为空
	 */
	public static final String REQUEST_DATA_NULL = "105";

	/**
	 * 账号与密码不匹配
	 */
	public static final String ACCOUNT_OR_PASSWORD_NULL = "106";

	/**
	 * 参数 ENTERPRISEID 为空
	 */
	public static final String ENTERPRISEID_NULL = "107";

	/**
	 * 账号不存在
	 */
	public static final String ACCOUNT_NOT_EXIST = "108";

	/**
	 * 密码错误
	 */
	public static final String PASSWORD_ERROR = "109";

	/**
	 * 参数 deveui为空
	 */
	public static final String DEVEUI_NULL = "110";

	/**
	 * 参数 devno为空
	 */
	public static final String DEVNO_NULL = "111";

	/**
	 * 参数 meter为空
	 */
	public static final String METER_NULL = "112";

	/**
	 * 参数 token为空
	 */
	public static final String TOKEN_NULL = "113";

	/**
	 * 参数 userid为空
	 */
	public static final String USERID_NULL = "114";

	/**
	 * 放弃不用
	 */
	public static final String DEVEUI_IS_BAND = "115";

	/**
	 * 设备已存在
	 */
	public static final String DEVNO_IS_EXIST = "116";

	/**
	 * 设备不存在
	 */
	public static final String DEVICE_NOT_EXIST = "117";

	/**
	 * 设备没有绑定表号
	 */
	public static final String DEVICE_NOT_BAND = "118";

	/**
	 * 设备已经绑定业主
	 */
	public static final String DEVICE_IS_BAND_OWNER = "119";

	/**
	 * 该账号所属的水司没有在后台添加对应的IOT账号
	 */
	public static final String NOT_SET_IOTACCOUNT = "120";

	public static final String IMSI_NULL = "122";

	/**用水量为空*/
	public static final String WATER_METER_IS_NULL = "123";
	/**未说明原因*/
	public static final String NO_EXPLAN_REASON = "124";
	/**不同类设备*/
	public static final String DISTINCT_DEVICE = "125";
	/**未找到下发绑定参数*/
	public static final String NOT_FOND_BIND_PARAMS = "126";
	
	/***
	 * 以下是IOT的Code
	 */

	/**
	 * IOT 服务提交完成并成功返回
	 */
	public static final String IotSucceccCode = "000000";
	/**
	 * 设备eui存在
	 */
	public static final String IotDeviceExixt = "-910001";
	/**
	 * 设备不存在
	 */
	public static final String IotDeviceNotExixt = "-910002";
	/**
	 * 账户不在线
	 */
	public static final String IotTokenAuthFail = "-990004";
	/**
	 * 设备添加失败
	 */
	public static final String IotDeviceCreateFail = "-810001";

	/**
	 * --------------黄金分割线--------------
	 */

	private static final Map<String, String> msgMap = new HashMap<>(getBaseMap());
	static {
		msgMap.put(SUCCESS, "成功完成提交");
		msgMap.put(TOKEN_ERROR, "Token错误");
		msgMap.put(SERVICE_ERROR, "服务端捕获到的异常, APP可直接打印MSG");
		msgMap.put(Token_Expired, Token_Expired);
		msgMap.put(REQUEST_DATA_NULL, "请求的JSON体为空");
		msgMap.put(ACCOUNT_NOT_EXIST, "账号不存在");
		msgMap.put(PASSWORD_ERROR, "密码错误");
		msgMap.put(DEVEUI_NULL, "参数 deveui为空");
		msgMap.put(DEVNO_NULL, "参数 devno为空");
		msgMap.put(METER_NULL, "参数 meter为空");
		msgMap.put(TOKEN_NULL, "参数 token为空");
		msgMap.put(USERID_NULL, "参数 userid为空");
		msgMap.put(DEVEUI_IS_BAND, "设备已绑定");
		msgMap.put(DEVNO_IS_EXIST, "设备已存在");
		msgMap.put(DEVICE_NOT_EXIST, "设备不存在");
		msgMap.put(DEVICE_NOT_BAND, "设备没有绑定表号");
		msgMap.put(DEVICE_IS_BAND_OWNER, "设备已经绑定业主");
		msgMap.put(NOT_SET_IOTACCOUNT, "该账号所属的水司没有在后台添加对应的IOT账号");
		msgMap.put(IMSI_NULL, "IMSI为空");
		msgMap.put(WATER_METER_IS_NULL, "用水量为空");
		msgMap.put(NO_EXPLAN_REASON, "未说明原因");
		msgMap.put(DISTINCT_DEVICE, "不同类设备");
		msgMap.put(NOT_FOND_BIND_PARAMS, "未找到下发绑定参数");
		
		
		msgMap.put(IotSucceccCode, "IOT 服务提交完成并成功返回");
		msgMap.put(IotDeviceExixt, "设备eui存在");
		msgMap.put(IotDeviceNotExixt, "设备不存在");
		msgMap.put(IotTokenAuthFail, "账户不在线");
		msgMap.put(IotDeviceCreateFail, "设备添加失败");
		msgMap.put(ACCOUNT_OR_PASSWORD_NULL, "用户名密码为空");
		msgMap.put(ENTERPRISEID_NULL, "水司ID为空");
	}

	public static String getMessage(String code, String... params) {
		String str = msgMap.get(code);
		if (StringUtils.isEmpty(str)) {
			return null;
		}
		if (params != null) {
			for (int i = 0; i < params.length; i++) {
				str = StringUtils.replace(str, "{" + i + "}", params[i]);
			}
		}
		return str;
	}
}
