package com.dotop.smartwater.project.module.api.tool.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.JSONUtils;
import com.dotop.smartwater.dependence.core.utils.UuidUtils;
import com.dotop.smartwater.project.module.api.tool.IMessageFactory;
import com.dotop.smartwater.project.module.core.auth.bo.SendMsgBo;
import com.dotop.smartwater.project.module.core.auth.bo.WechatMessageParamBo;
import com.dotop.smartwater.project.module.core.auth.local.AuthCasClient;
import com.dotop.smartwater.project.module.core.auth.vo.UserVo;
import com.dotop.smartwater.project.module.core.water.bo.MessageBo;
import com.dotop.smartwater.project.module.core.water.config.Config;
import com.dotop.smartwater.project.module.core.water.form.MessageForm;
import com.dotop.smartwater.project.module.core.water.vo.MessageVo;
import com.dotop.smartwater.project.module.service.tool.IEmailService;
import com.dotop.smartwater.project.module.service.tool.IMessageService;
import com.dotop.smartwater.project.module.service.tool.ISmsToolService;

/**
 * 消息中心
 * 

 * @date 2019-03-07 11:29
 *
 */
@Component
public class MessageFactoryImpl implements IMessageFactory {

	@Autowired
	private IMessageService iMessageService;

	@Autowired
	private ISmsToolService iSmsService;

	@Autowired
	private IEmailService iEmailService;

	@Override
	public Pagination<MessageVo> page(MessageForm messageForm) {
		UserVo user = AuthCasClient.getUser();
		String operEid = user.getEnterpriseid();

		MessageBo messageBo = new MessageBo();
		BeanUtils.copyProperties(messageForm, messageBo);
		messageBo.setEnterpriseid(operEid);

		return iMessageService.page(messageBo);
	}

	@Override
	public Integer sendMessage(MessageForm messageForm) {
		UserVo user = AuthCasClient.getUser();
		String operEid = user.getEnterpriseid();
		boolean flag = false;

		if (messageForm.getId() != null) {
			messageForm = iMessageService.getById(messageForm.getId());
		}
		MessageBo messageBo = new MessageBo();
		BeanUtils.copyProperties(messageForm, messageBo);
		messageBo.setEnterpriseid(operEid);

		String[] phones = { messageBo.getReceiveaddress() };
		Map<String, Object> jsonMap = JSONUtils.parseObject(messageBo.getParams(), String.class, Object.class);
		Map<String, String> map = new HashMap<>();
		if (messageBo.getMessagetype() != 3) {
			for (Map.Entry<String, Object> m : jsonMap.entrySet()) {
				map.put(m.getKey(), String.valueOf(m.getValue()));
			}
		}
		map.put("title", messageBo.getTitle());
		if (messageBo.getMessagetype() == 1) {
			// 发送短信
			iSmsService.sendSMS(messageBo.getEnterpriseid(), messageBo.getModeltype(), phones, map,
					messageBo.getBatchNo());
			flag = true;
		} else if (messageBo.getMessagetype() == 2) {
			// 发送邮件
			iEmailService.sendEmail(messageBo);
			flag = true;
		} else if (messageBo.getMessagetype() == 3) {
			// 发送微信
			SendMsgBo sendMsgBo = new SendMsgBo();
			sendMsgBo.setEnterpriseid(messageBo.getEnterpriseid());
			sendMsgBo.setModeltype(messageBo.getModeltype());
			sendMsgBo.setOpenId(messageBo.getReceiveaddress());
			WechatMessageParamBo wechatMessageParamBo = JSONUtils.parseObject(messageBo.getParams(),
					WechatMessageParamBo.class);
			sendMsgBo.setWechatMessageParam(wechatMessageParamBo);
			iSmsService.sendWeChatMsg(sendMsgBo);
			flag = true;
		}
		if (flag) {
			return 1;
		} else {
			return 0;
		}
	}

	@Override
	public Integer addMessage(MessageForm messageForm) {
		UserVo user = AuthCasClient.getUser();
		Date curr = new Date();
		String userBy = user.getName();
		String operEid = user.getEnterpriseid();

		MessageBo messageBo = new MessageBo();
		BeanUtils.copyProperties(messageForm, messageBo);
		messageBo.setEnterpriseid(operEid);
		messageBo.setSendusername(userBy);
		messageBo.setSendtime(curr);
		messageBo.setId(UuidUtils.getUuid());
		if (messageBo.getBatchNo() == null || "".equals(messageBo.getBatchNo())) {
			messageBo.setBatchNo(String.valueOf(Config.Generator.nextId()));// 批次号
		}

		return iMessageService.addMessage(messageBo);
	}

}
