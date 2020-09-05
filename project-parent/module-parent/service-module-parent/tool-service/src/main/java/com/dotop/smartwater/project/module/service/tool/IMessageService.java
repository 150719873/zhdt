package com.dotop.smartwater.project.module.service.tool;

import com.dotop.smartwater.dependence.core.common.BaseService;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.water.bo.MessageBo;
import com.dotop.smartwater.project.module.core.water.form.MessageForm;
import com.dotop.smartwater.project.module.core.water.vo.MessageVo;

/**
 * 消息中心
 * 

 * @date 2019-03-07 11:29
 *
 */
public interface IMessageService extends BaseService<MessageBo, MessageVo> {

	@Override
	Pagination<MessageVo> page(MessageBo messageBo);

	MessageForm getById(String id);

	Integer addMessage(MessageBo messageBo);
}
