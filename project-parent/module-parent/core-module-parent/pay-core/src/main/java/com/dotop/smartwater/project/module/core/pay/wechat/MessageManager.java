package com.dotop.smartwater.project.module.core.pay.wechat;

/**
 * 消息工具类

 */
public class MessageManager {

	public static final String OK = "OK";

	public static final String SUCCESS = "SUCCESS";

	public static final String FAIL = "FAIL";

	public static final String RESP_RETURN_CODE = "return_code";

	public static final String RESP_RETURN_MSG = "return_msg";

	public static final String RESP_RESULT_CODE = "result_code";

	public static final String RESP_ERR_CODE = "err_code";

	public static final String RESP_ERR_CODE_DES = "err_code_des";

	// 用户支付中
	public static final String USERPAYING = "USERPAYING";
	// 银行系统异常
	public static final String BANKERROR = "BANKERROR";
	// 系统超时
	public static final String SYSTEMERROR = "SYSTEMERROR";

	// 付款码过期
	public static final String AUTH_CODE_INVALID = "AUTH_CODE_INVALID";

	public static final String RESP_PREPAY_ID = "prepay_id";
	public static final String RESP_APP_ID = "appid";

	public static final String SIGN_FLAG = "sign_flag";

	public static final String RESP_TRADE_STATE = "trade_state";
	public static final String RESP_TRADE_DESC = "trade_state_desc";
	public static final String RESP_TRADE_STATE_NOTPAY = "NOTPAY";
	public static final String RESP_TRADE_STATE_USERPAYING = "USERPAYING";
}