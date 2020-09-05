package com.dotop.smartwater.project.server.water.rest.service.tool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.dotop.smartwater.dependence.core.common.BaseController;
import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.api.tool.IEmailTemplateFactory;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.module.core.water.form.EmailTemplateForm;
import com.dotop.smartwater.project.module.core.water.vo.EmailTemplateVo;

/**

 * @date 2018/11/29.
 */

@RestController

@RequestMapping("/emailTemplate")
public class EmailTemplateController implements BaseController<EmailTemplateForm> {

	private static final Logger LOGGER = LoggerFactory.getLogger(EmailTemplateController.class);

	@Autowired
	private IEmailTemplateFactory iEmailTemplateFactory;

	@Override
	@PostMapping(value = "/page", produces = GlobalContext.PRODUCES)
	public String page(@RequestBody EmailTemplateForm emailTemplateForm) {
		LOGGER.info(LogMsg.to("msg:", " 分页查询开始", emailTemplateForm));
		Pagination<EmailTemplateVo> pagination = iEmailTemplateFactory.page(emailTemplateForm);
		LOGGER.info(LogMsg.to("msg:", " 分页查询结束"));
		return resp(ResultCode.Success, "SUCCESS", pagination);
	}

	@PostMapping(value = "/insertOrUpdate", produces = GlobalContext.PRODUCES)
	public String insert(@RequestBody EmailTemplateForm emailTemplateForm) {
		LOGGER.info(LogMsg.to("msg:", " 新增或修改开始", emailTemplateForm));
		iEmailTemplateFactory.insertOrUpdate(emailTemplateForm);
		LOGGER.info(LogMsg.to("msg:", " 新增或修改结束", emailTemplateForm));

		return resp(ResultCode.Success, "Success", null);
	}

}
