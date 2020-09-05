package com.dotop.deyang.dc.model.code;

public class ResultCode {

	public final static String SUCCESS = "success";

	public final static int Success = 0;

	public final static int Fail = 1;

	public final static int TimeOut = 2;

	public final static int Object_Serializable_Error = 3;

	public final static int NoPermission = 4;

	public final static int ParamIllegal = 101;

	public final static int AccountExist = 102;

	public final static int EnterpriseExist = 103;

	public final static int AccountOrPasswordError = 104;

	public final static int RoleExist = 105;

	public final static int UserNotLogin = 106;

	/** 账户失效 */
	public final static int AccountInvalid = 107;
	/** 不能删除已使用的角色 */
	public final static int CanNotDeleteUserRole = 108;

	/** 旧密码错误 */
	public final static int OldPwdError = 109;

	/** 流水号生成中 */
	public final static int NumberLocking = 110;

	/** 微信配置的企业已经存在功错误 */
	public final static int WECHAT_SETTING_EXIST_ERROR = 142;

}
