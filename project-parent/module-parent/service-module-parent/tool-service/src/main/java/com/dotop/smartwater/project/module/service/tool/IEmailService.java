package com.dotop.smartwater.project.module.service.tool;

import java.util.List;

import com.dotop.smartwater.dependence.core.common.BaseService;
import com.dotop.smartwater.project.module.core.water.bo.MessageBo;
import com.dotop.smartwater.project.module.core.water.vo.MessageVo;

/**

 * @date 2018/12/3.
 */

public interface IEmailService extends BaseService<MessageBo, MessageVo> {
	/**
	 * 发邮件
	 * 
	 * @param vo
	 * @throws Exception
	 */
	void sendEmail(MessageBo messageBo);

	void sendEmailWithoutAuth(MessageBo messageBo);

	void sendEmail(List<MessageBo> list, String batchNo);
}
