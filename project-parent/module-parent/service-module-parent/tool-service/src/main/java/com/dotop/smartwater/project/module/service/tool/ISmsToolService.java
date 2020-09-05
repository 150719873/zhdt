package com.dotop.smartwater.project.module.service.tool;

import java.util.Map;

import org.springframework.scheduling.annotation.Async;

import com.dotop.smartwater.project.module.core.auth.bo.SendMsgBo;

public interface ISmsToolService {

	/**
	 * 发送短信
	 */
//	@Async("sendSmsExecutor")
	void sendSMS(String enterpriseId, int model, String[] phoneNumbers, Map<String, String> params, String batchNo);

	/**
	 * 发送短信
	 */
//	@Async("sendSmsExecutor")
	void sendSMS(String enterpriseId, int model, Map<String, String> params, String batchNo);

	/**
	 * 发送短信
	 */
//	@Async("sendSmsExecutor")
//	void sendSMS(String enterpriseId, int model, String batchNo, List<Map<String, String>> params);

	/**
	 * 发送微信消息
	 */
	@Async("sendWeChatMsgExecutor")
	void sendWeChatMsg(SendMsgBo sendMsgBo);
}
