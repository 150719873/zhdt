package com.dotop.smartwater.project.module.core.water.constants;

import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 常量
 * <p>
 * 旧code为兼容不改动，新core需要按照代码规范编写
 * <p>
 * 参考：http://192.168.10.224/confluence/pages/viewpage.action?pageId=1966188
 */
public class ResultCode extends BaseExceptionConstants {

	public static final String Success = "0";

	public static final String SUCCESS = "success";
	public static final String Fail = "1";
	public static final String TimeOut = "2";
	public static final String ParamIllegal = "101";
	public static final String AccountExist = "102";
	public static final String EnterpriseExist = "103";
	public static final String AccountOrPasswordError = "104";
	public static final String RoleExist = "105";
	public static final String CommunityExist = "106";
	public static final String DeviceIsUsed = "107";
	public static final String DevnoNotExist = "108";
	public static final String OwnerNotExist = "109";

	public static final String PricetypeUsed = "110";

	public static final String UserNotLogin = "111";

	public static final String NotFindDevice = "112";

	public static final String NotPermission = "113";
	/**
	 * 账户失效
	 */
	public static final String AccountInvalid = "114";
	/**
	 * 不能删除已使用的角色
	 */
	public static final String NODELETEUSEROLE = "115";
	/**
	 * 该小区下有开户的业主,不能删除小区
	 */
	public static final String OWNERUSE = "116";
	/**
	 * 业主编号已被使用
	 */
	public static final String OWNERBIANHAOUSE = "117";
	/**
	 * 只有没开户的业主才可以删除
	 */
	public static final String NOOPENACCOUNTISDELETE = "118";
	/**
	 * 旧水表记录错误,请联系管理员
	 */
	public static final String OLDWATERMETERRECORDERROR = "119";
	/**
	 * 无法从用户中心获取到该账号
	 */
	public static final String NOFINDUSERINFO = "120";
	/**
	 * 无法找到本地账号，前往管理员绑定页面
	 */
	public static final String SETTINGADMINACCOUNT = "121";
	/**
	 * 文本框校验错误
	 */
	public static final String CHECKTEXTERROR = "-1";
	/**
	 * 用户中心找不到此账号
	 */
	public static final String USERCENTERNOFINDUSER = "122";
	/**
	 * 无法找到本地账号，前往子账号绑定页面
	 */
	public static final String SETTINGSUBACCOUNT = "123";
	/**
	 * 无法找到当前账户的上级账户
	 */
	public static final String NOTFINDPARENTUSER = "124";
	/**
	 * 用户已存在
	 */
	public static final String USERALREADYEXISTED = "125";
	/**
	 * 未找到水表
	 */
	public static final String NOTFINDWATER = "126";
	/**
	 * 水表不带阀,不能开/关阀
	 */
	public static final String WATERNOVALVENOOPENORCLOSEVALVE = "127";
	/**
	 * 开阀失败
	 */
	public static final String OPENVALVEERROR = "128";
	/**
	 * 获取token出现异常
	 */
	public static final String GETTOKENERROR = "129";
	/**
	 * 实时关阀失败
	 */
	public static final String CLOSEVALVEERROR = "130";
	/**
	 * 实时抄表失败
	 */
	public static final String READVALVEERROR = "131";
	/**
	 * 下发失败
	 */
	public static final String DOWNERROR = "132";
	/**
	 * 水表处于离线状态,不能操作
	 */
	public static final String DEVICE_OFFLINE_STATUS = "133";
	/**
	 * 运行错误
	 */
	public static final String RUNNINGERROR = "9999";

	/**
	 * 微信绑定参数输入错误
	 */
	public static final String WECHATBLINDERROR = "134";

	/**
	 * 微信下单错误
	 */
	public static final String WECHATORDERERROR = "135";

	/**
	 * 微信充值错误
	 */
	public static final String WECHATRECHARGEERROR = "136";

	/**
	 * 未有绑定业主错误
	 */
	public static final String WECHAT_NOT_OWNER_ERROR = "137";

	/**
	 * 企业微信模板已有错误
	 */
	public static final String WECHAT_TEMPLATE_EXIST_ERROR = "138";

	/**
	 * 错账错误
	 */
	public static final String WECHAT_WRONG_ACCOUNT_ERROR = "139";

	/**
	 * 业主未开户或者销户错误
	 */
	public static final String WECHAT_OWNER_NOT_EXIST_ERROR = "140";

	/**
	 * 微信订单支付成功错误
	 */
	public static final String WECHAT_ORDER_PAY_SUCCESS_ERROR = "141";

	/**
	 * 微信配置的企业已经存在功错误
	 */
	public static final String WECHAT_SETTING_EXIST_ERROR = "142";

	/**
	 * 微信支付金额计算错误
	 */
	public static final String WECHAT_ACCOUNT_ERROR_ERROR = "143";

	/**
	 * 权重信息不能为空
	 */
	public static final String WEIGHT_IS_NULL_ERROR = "144";
	/**
	 * 结束时间小于开始时间
	 */
	public static final String TIME_ERROR = "145";
	/**
	 * 未获取到填报分数
	 */
	public static final String NO_FIND_FILL_SCORE_ERROR = "146";
	/**
	 * 未设定功能
	 */
	public static final String NO_SET_FUNCTION_ERROR = "147";
	/**
	 * 未获取到预约配置信息
	 */
	public static final String NO_GET_SETTING_INFO_ERROR = "148";
	/**
	 * 平台设置中预约配置异常
	 */
	public static final String SETTING_APPOINTMENT_EXCEPTION_ERROR = "149";
	/**
	 * 预约日期人数已满
	 */
	public static final String APPOINTMENT_NUMBER_EXCEED_ERROR = "150";
	/**
	 * 未处理完已申请报装
	 */
	public static final String APPLY_NO_HANDLE_ERROR = "151";
	/**
	 * 已结束的考核不能删除
	 */
	public static final String END_EXAM_NO_DELETE_ERROR = "152";

	/**
	 * 流水号生成中
	 */
	public static final String NumberLocking = "153";
	/**
	 * 时间格式异常或结束时间小于开始时间
	 */
	public static final String DATE_ERROR = "154";
	/**
	 * 业主不存在或已销户
	 */
	public static final String OWNER_NO_EXIST_ERROR = "155";
	/**
	 * 未获取到验收文件
	 */
	public static final String NO_GET_ACCEPTANCE_ERROR = "156";
	/**
	 * 用户信息为空
	 */
	public static final String USER_INFO_NULL_ERROR = "157";

	/**
	 * 不能删除已使用的角色
	 */
	public static final String CanNotDeleteUserRole = "158";

	/**
	 * 订单已支付
	 */
	public static final String ORDER_PAY_ERROR = "159";

	/** 未查询到数据 */
	public static final String NO_SEARCH_DATA = "160";

	/** 生成方式未设置 */
	public static final String NO_SET_GENERATE_METHOD = "161";

	/** 区域未设置 */
	public static final String NO_SET_COMMUNITY = "162";

	/** 业主编号未设置 */
	public static final String NO_SET_OWNER_NO = "163";

	/** 账单状态未设置 */
	public static final String NO_SET_ORDER_STATUS = "164";

	/** 截止时间未设置 */
	public static final String NO_SET_END_TIME = "165";

	/** 时间间隔未设置 */
	public static final String NO_SET_TIME_INTERVAL = "166";

	/** 账单月份未设置 */
	public static final String NO_SET_MONTH_ORDER = "167";

	/** 账单月份格式错误 */
	public static final String MONTH_ORDER_FORMAT_ERROR = "168";

	/** 正在生成账单，请稍后查询 */
	public static final String GENERATE_ORDER_LATER_ON_SEARCH = "169";

	/** 业主ID不能为空 */
	public static final String OWNER_ID_IS_NULL = "170";

	/** 未选择账单 */
	public final static String NO_CHECK_ORDER = "171";

	/** 账单流水号为空 */
	public final static String ORDER_NUMBER_IS_NULL = "172";

	/** ID不能为空 */
	public final static String ID_IS_NULL = "173";

	/** 参数不能为空 */
	public final static String PARAMS_IS_NULL = "174";

	/** 设备EUI不能为空 */
	public final static String DEVICE_EUI_IS_NULL = "175";

	/** 设备读数不能为空 */
	public final static String DEVICE_WATER_IS_NULL = "176";

	/** 未能获取到文件格式 */
	public final static String NO_GET_FIEL_FORAMT = "177";

	/** 规则类型不能为空 */
	public final static String RULE_TYPE_IS_NOT_NULL = "178";
	/** 要生成的个数不能为空 */
	public final static String NUMBER_CANNOT_BE_EMPTY = "179";
	/** 超过极限值 */
	public final static String EXCEEDING_THE_LIMIT_VALUE = "180";

	/**
	 * 水表不属于该水司
	 */
	public static final String DEVICENOTYOURS = "181";

	/**
	 * 未获取到信息
	 */
	public static final String NO_GET_DATA = "182";
	
	/**
	 * 生产中不能删除
	 */
	public static final String PRODUCTION_STARTING_NO_DELETE = "183";
	
	/**
	 * 已结束不能删除
	 */
	public static final String PRODUCTION_END_NO_DELETE = "184";
	
	/**
	 * 批次已结束
	 */
	public static final String BATCH_END = "185";
	
	/**
	 * 用户未登录
	 */
	public static final String USER_NO_LOGIN = "9999";

	/**
	 * --------------黄金分割线--------------
	 */

	private static final Map<String, String> msgMap = new HashMap<>(getBaseMap());

	static {
		msgMap.put(Success, SUCCESS);
		msgMap.put(Fail, Fail);
		msgMap.put(TimeOut, TimeOut);
		msgMap.put(ParamIllegal, ParamIllegal);
		msgMap.put(AccountExist, AccountExist);
		msgMap.put(EnterpriseExist, EnterpriseExist);
		msgMap.put(AccountOrPasswordError, AccountOrPasswordError);
		msgMap.put(RoleExist, "角色已经存在");
		msgMap.put(CommunityExist, CommunityExist);
		msgMap.put(DeviceIsUsed, DeviceIsUsed);
		msgMap.put(DevnoNotExist, "设备不存在");
		msgMap.put(OwnerNotExist, OwnerNotExist);
		msgMap.put(PricetypeUsed, PricetypeUsed);
		msgMap.put(UserNotLogin, UserNotLogin);
		msgMap.put(NotFindDevice, NotFindDevice);
		msgMap.put(NotPermission, NotPermission);
		msgMap.put(AccountInvalid, "账户失效");
		msgMap.put(NODELETEUSEROLE, "不能删除已使用的角色");
		msgMap.put(OWNERUSE, "该小区下有开户的业主,不能删除小区");
		msgMap.put(OWNERBIANHAOUSE, "业主编号已被使用");
		msgMap.put(NOOPENACCOUNTISDELETE, "只有没开户的业主才可以删除");
		msgMap.put(OLDWATERMETERRECORDERROR, "旧水表记录错误,请联系管理员");
		msgMap.put(NOFINDUSERINFO, "无法从用户中心获取到该账号");
		msgMap.put(SETTINGADMINACCOUNT, "无法找到本地账号，前往管理员绑定页面");
		msgMap.put(CHECKTEXTERROR, "文本框校验错误");
		msgMap.put(USERCENTERNOFINDUSER, "用户中心找不到此账号");
		msgMap.put(SETTINGSUBACCOUNT, "无法找到本地账号，前往子账号绑定页面");
		msgMap.put(NOTFINDPARENTUSER, "无法找到当前账户的上级账户");
		msgMap.put(USERALREADYEXISTED, "用户已存在");
		msgMap.put(NOTFINDWATER, "未找到水表");
		msgMap.put(WATERNOVALVENOOPENORCLOSEVALVE, "水表不带阀,不能开/关阀");
		msgMap.put(OPENVALVEERROR, "开阀失败");
		msgMap.put(GETTOKENERROR, "获取token出现异常");
		msgMap.put(CLOSEVALVEERROR, "实时关阀失败");
		msgMap.put(READVALVEERROR, "实时抄表失败");
		msgMap.put(DOWNERROR, "下发失败");
		msgMap.put(DEVICE_OFFLINE_STATUS, "水表处于离线状态,不能操作");
		msgMap.put(RUNNINGERROR, "运行错误");
		msgMap.put(WECHATBLINDERROR, "微信绑定参数输入错误");
		msgMap.put(WECHATORDERERROR, "微信下单错误");
		msgMap.put(WECHATRECHARGEERROR, "微信充值错误");
		msgMap.put(WECHAT_NOT_OWNER_ERROR, "未有绑定业主错误");
		msgMap.put(WECHAT_TEMPLATE_EXIST_ERROR, "企业微信模板已有错误");
		msgMap.put(WECHAT_WRONG_ACCOUNT_ERROR, "错账错误");
		msgMap.put(WECHAT_OWNER_NOT_EXIST_ERROR, "业主未开户或者销户错误");
		msgMap.put(WECHAT_ORDER_PAY_SUCCESS_ERROR, "微信订单支付成功错误");
		msgMap.put(WECHAT_SETTING_EXIST_ERROR, "微信配置的企业已经存在功错误");
		msgMap.put(WECHAT_ACCOUNT_ERROR_ERROR, " 微信支付金额计算错误");
		msgMap.put(WEIGHT_IS_NULL_ERROR, "权重信息不能为空");
		msgMap.put(TIME_ERROR, "结束时间小于开始时间");
		msgMap.put(NO_FIND_FILL_SCORE_ERROR, "未获取到填报分数");
		msgMap.put(NO_SET_FUNCTION_ERROR, "未设定功能");
		msgMap.put(NO_GET_SETTING_INFO_ERROR, "未获取到预约配置信息");
		msgMap.put(SETTING_APPOINTMENT_EXCEPTION_ERROR, "平台设置中预约配置异常");
		msgMap.put(APPOINTMENT_NUMBER_EXCEED_ERROR, "预约人数已满");
		msgMap.put(APPLY_NO_HANDLE_ERROR, "未处理完已申请报装");
		msgMap.put(END_EXAM_NO_DELETE_ERROR, "已结束的考核不能删除");
		msgMap.put(NumberLocking, "流水号生成中");
		msgMap.put(DATE_ERROR, "时间格式异常或结束时间小于开始时间");
		msgMap.put(OWNER_NO_EXIST_ERROR, "业主不存在或已销户");
		msgMap.put(NO_GET_ACCEPTANCE_ERROR, "未获取到验收文件");
		msgMap.put(USER_INFO_NULL_ERROR, "用户信息为空");
		msgMap.put(CanNotDeleteUserRole, "平台角色已经被使用,不能删除");
		msgMap.put(ORDER_PAY_ERROR, "订单已支付");
		msgMap.put(USER_NO_LOGIN, "用户未登录");

		msgMap.put(NO_SEARCH_DATA, "未查询到数据");

		msgMap.put(NO_SET_GENERATE_METHOD, "生成方式未设置");
		msgMap.put(NO_SET_COMMUNITY, "区域未设置");
		msgMap.put(NO_SET_OWNER_NO, "业主编号未设置");
		msgMap.put(NO_SET_ORDER_STATUS, "账单状态未设置");

		msgMap.put(NO_SET_END_TIME, "截止时间未设置");
		msgMap.put(NO_SET_TIME_INTERVAL, "时间间隔未设置");
		msgMap.put(NO_SET_MONTH_ORDER, "账单月份未设置");
		msgMap.put(MONTH_ORDER_FORMAT_ERROR, "账单月份格式错误");
		msgMap.put(GENERATE_ORDER_LATER_ON_SEARCH, "正在生成账单，请稍后查询");
		msgMap.put(OWNER_ID_IS_NULL, "业主ID不能为空");
		msgMap.put(NO_CHECK_ORDER, "未选择账单");
		msgMap.put(ORDER_NUMBER_IS_NULL, "账单流水号为空");

		msgMap.put(ID_IS_NULL, "ID不能为空");
		msgMap.put(PARAMS_IS_NULL, "参数不能为空");
		msgMap.put(DEVICE_EUI_IS_NULL, "设备EUI不能为空");
		msgMap.put(DEVICE_WATER_IS_NULL, "设备读数不能为空");
		msgMap.put(NO_GET_FIEL_FORAMT, "未能获取到文件格式");

		msgMap.put(RULE_TYPE_IS_NOT_NULL, "规则类型不能为空");
		msgMap.put(NUMBER_CANNOT_BE_EMPTY, "个数不能为空");
		msgMap.put(EXCEEDING_THE_LIMIT_VALUE, "超过极限值");
		msgMap.put(DEVICENOTYOURS, "水表不属于该水司");
		msgMap.put(NO_GET_DATA, "未获取到信息");
		
		msgMap.put(PRODUCTION_STARTING_NO_DELETE, "生产中不能删除");
		msgMap.put(PRODUCTION_END_NO_DELETE, "已结束不能删除");
		msgMap.put(BATCH_END, "批次已结束");
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
