package com.dotop.smartwater.project.module.dao.tool;

import com.dotop.smartwater.dependence.core.common.BaseDao;
import com.dotop.smartwater.project.module.core.water.dto.MessageDto;
import com.dotop.smartwater.project.module.core.water.vo.MessageVo;

import java.util.List;


/**
 * 消息中心
 *

 * @date 2019-03-07 10:02
 */

public interface IMessageDao extends BaseDao<MessageDto, MessageVo> {

	@Override
	List<MessageVo> list(MessageDto messageDto);

	Integer addMessage(MessageDto messageDto);

	@Override
	MessageVo get(MessageDto messageDto);
}
