package com.dotop.smartwater.project.module.api.tool;

import com.dotop.smartwater.dependence.core.common.BaseFactory;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.water.form.MessageForm;
import com.dotop.smartwater.project.module.core.water.vo.MessageVo;

/**
 * 消息中心
 * 

 * @date 2019-03-07 10:02
 *
 */
public interface IMessageFactory extends BaseFactory<MessageForm, MessageVo> {

	@Override
	Pagination<MessageVo> page(MessageForm messageForm);

	/*
	 * 补发通知
	 */
	Integer sendMessage(MessageForm messageForm);

	Integer addMessage(MessageForm messageForm);
}
