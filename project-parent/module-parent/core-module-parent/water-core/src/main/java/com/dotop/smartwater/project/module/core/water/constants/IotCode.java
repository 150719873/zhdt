package com.dotop.smartwater.project.module.core.water.constants;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

/**
 * IOT平台返回CODE码对应值
 *

 * @date 2019年4月12日
 */
public class IotCode {

	public static final String Success = "0";

	public static final String SUCCESS = "success";

	private static final Map<String, String> msgMap = new HashMap<>();

	static {
		msgMap.put(Success, SUCCESS);
		msgMap.put("-999999", "内部异常");
		msgMap.put("-999997", "请求错误");
		msgMap.put("-999996", "请求失败");
		msgMap.put("-999995", "JSON异常");
		msgMap.put("-999994", "重复插入违反约束");
		msgMap.put("-999993", "重复插入违反约束");
		msgMap.put("-999992", "解析异常");
		msgMap.put("-999991", "非法异常");
		msgMap.put("-999990", "类型转换异常");
		msgMap.put("-999988", "参数为空");
		msgMap.put("-999987", "不是十进制");
		msgMap.put("-999984", "参数过长");
		msgMap.put("-999983", "参数值过大");
		msgMap.put("-999982", "参数值过小");
		msgMap.put("-910001", "设备eui存在");
		msgMap.put("-910002", "设备不存在");
		msgMap.put("-910003", "设备存在设备组");
		msgMap.put("-910004", "设备存在规则引擎");
		msgMap.put("-910005", "设备不允许解绑");
		msgMap.put("-910101", "产品不存在");
		msgMap.put("-910103", "产品名字已存在");
		msgMap.put("-910201", "物模型不存在");
		msgMap.put("-910301", "服务不存在");
		msgMap.put("-910302", "服务名字已存在");
		msgMap.put("-910401", "属性不存在");
		msgMap.put("-910402", "属性名字已存在");
		msgMap.put("-910501", "命令不存在");
		msgMap.put("-910502", "命令名字已存在");
		msgMap.put("-910601", "参数不存在");
		msgMap.put("-910602", "参数名字已存在");
		msgMap.put("-910701", "设备组不存在");
		msgMap.put("-910702", "不允许删除设备组");
		msgMap.put("-910703", "设备组名字已存在");
		msgMap.put("-910704", "设备组与设备产品不匹配");
		msgMap.put("-910705", "设备组与设备提供商不匹配");
		msgMap.put("-910706", "设备组不存在该设备");
		msgMap.put("-920001", "应用不存在");
		msgMap.put("-920002", "应用创建数量过多");
		msgMap.put("-920003", "应用不允许删除");
		msgMap.put("-990001", "账户不存在");
		msgMap.put("-990002", "账户没有角色");
		msgMap.put("-990003", "账户认证失败");
		msgMap.put("-990004", "账户不在线");
		msgMap.put("-990005", "账户被锁定");
		msgMap.put("-990006", "账户没有权限");
		msgMap.put("-990007", "账户票据过期");
		msgMap.put("-810001", "创建设备失败");
		msgMap.put("-810002", "删除设备失败");
		msgMap.put("-810003", "创建设备组失败");
		msgMap.put("-810004", "删除设备组失败");
		msgMap.put("-810005", "添加设备组设备失败");
		msgMap.put("-810006", "删除设备组设备失败");
		msgMap.put("-810007", "编辑应用失败");
		msgMap.put("-810008", "获取应用失败");
		msgMap.put("-820001", "规则引擎调用失败");
		msgMap.put("-910901", "规则引擎名字重复");
		msgMap.put("-910902", "规则引擎不存在");
		msgMap.put("-930001", "消息体解析错误");
		msgMap.put("-940001", "虚拟设备不允许下发");
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
