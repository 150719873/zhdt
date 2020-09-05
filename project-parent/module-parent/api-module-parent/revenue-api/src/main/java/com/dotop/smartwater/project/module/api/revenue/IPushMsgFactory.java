package com.dotop.smartwater.project.module.api.revenue;

import java.util.Map;

public interface IPushMsgFactory {


	/**
	 * 发送消息
	 * @param params
	 * @param phone
	 * @param type
	 * @param enterpriseid
	 * @param smstype
	 * @param objects
	 */
	void sendMsg(Map<String, String> params, String phone, int type, String enterpriseid, int smstype,
			Object... objects) ;

}
