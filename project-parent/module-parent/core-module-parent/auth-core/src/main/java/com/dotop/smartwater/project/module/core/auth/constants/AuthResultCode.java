package com.dotop.smartwater.project.module.core.auth.constants;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;

public class AuthResultCode extends BaseExceptionConstants {

	public static final String Success = "0";

	public static final String Fail = "1";

	public static final String TimeOut = "2";

	public static final String Object_Serializable_Error = "3";

	public static final String NoPermission = "4";

	public static final String ParamIllegal = "101";

	public static final String AccountExist = "102";

	public static final String EnterpriseExist = "103";

	public static final String AccountOrPasswordError = "104";

	public static final String RoleExist = "105";

	public static final String UserNotLogin = "106";

	/** 账户失效 */
	public static final String AccountInvalid = "107";
	/** 不能删除已使用的角色 */
	public static final String CanNotDeleteUserRole = "108";

	/** 旧密码错误 */
	public static final String OldPwdError = "109";

	/** 流水号生成中 */
	public static final String NumberLocking = "110";

	/** 微信配置的企业已经存在功错误 */
	public static final String WECHAT_SETTING_EXIST_ERROR = "142";

	/**
	 * --------------黄金分割线--------------
	 */

	private static final Map<String, String> msgMap = new HashMap<>(getBaseMap());

	static{
		msgMap.put(Success, SUCCESS);
		msgMap.put(Fail, Fail);
		msgMap.put(TimeOut, TimeOut);
		msgMap.put(Object_Serializable_Error, Object_Serializable_Error);
		msgMap.put(NoPermission, NoPermission);
		msgMap.put(ParamIllegal, ParamIllegal);
		msgMap.put(AccountExist, AccountExist);
		msgMap.put(EnterpriseExist, EnterpriseExist);
		msgMap.put(AccountOrPasswordError, AccountOrPasswordError);
		msgMap.put(RoleExist, RoleExist);
		msgMap.put(UserNotLogin, UserNotLogin);
		msgMap.put(AccountInvalid, AccountInvalid);
		msgMap.put(CanNotDeleteUserRole, CanNotDeleteUserRole);
		msgMap.put(OldPwdError, OldPwdError);
		msgMap.put(NumberLocking, NumberLocking);
		msgMap.put(WECHAT_SETTING_EXIST_ERROR, WECHAT_SETTING_EXIST_ERROR);
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
