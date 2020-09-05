package com.dotop.smartwater.project.module.api.revenue.async;

import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.project.module.api.revenue.IPushMsgFactory;
import com.dotop.smartwater.project.module.service.tool.ISmsService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Map;


/**
 * 消息处理
 */
@Component
public class PushMsgFactoryImpl implements IPushMsgFactory {

	private final static Logger logger = LogManager.getLogger(MakeOrderFactoryImpl.class);

	@Autowired
	private ISmsService iSmsService;

	// 异步发送微信和短信推送
	/**
	 * @param params
	 *            map参数
	 * @param phone
	 *            电话
	 * @param type
	 *            1短信 2微信
	 * @param enterpriseid
	 *            水司Id
	 */
	@Async("msgExecutor")
	@Override
	public void sendMsg(Map<String, String> params, String phone, int type, String enterpriseid, int smstype,
			Object... objects) throws FrameworkRuntimeException {
		Long start = System.currentTimeMillis();
		String name = "短信推送";
		try {
			switch (type) {
			case 1:
				String[] phoneNumbers = new String[] { phone };
				iSmsService.sendSMS(enterpriseid, smstype, phoneNumbers, objects);
				break;
			case 2:
				name = "微信推送";
				iSmsService.sendWeChatMsg(enterpriseid, smstype, params);
				break;
			default:
				break;
			}
		} catch (RuntimeException e) {
			logger.error("AsyncSendMsg RuntimeException", e);
		} catch (Exception e) {
			logger.error("AsyncSendMsg Exception", e);
		} finally {
			logger.info("{} 耗时 : {}", name, (System.currentTimeMillis() - start));
		}
	}
}
