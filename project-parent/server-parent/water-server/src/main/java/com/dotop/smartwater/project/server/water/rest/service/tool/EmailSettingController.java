package com.dotop.smartwater.project.server.water.rest.service.tool;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.dotop.smartwater.dependence.core.common.BaseController;
import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.api.tool.IEmailSettingFactory;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.module.core.water.form.EmailSettingForm;
import com.dotop.smartwater.project.module.core.water.vo.EmailSettingVo;

@RestController

@RequestMapping("/emailSetting")
public class EmailSettingController implements BaseController<EmailSettingForm> {

	private static final Logger LOGGER = LogManager.getLogger(EmailSettingController.class);

	@Autowired
	private IEmailSettingFactory iEmailSettingFactory;

	@Override
	@PostMapping(value = "/page", produces = GlobalContext.PRODUCES)
	public String page(@RequestBody EmailSettingForm emailSettingForm) {
		LOGGER.info(LogMsg.to("msg:", " 分页查询开始", "emailSettingForm", emailSettingForm));

		Pagination<EmailSettingVo> pagination = iEmailSettingFactory.page(emailSettingForm);
		LOGGER.info(LogMsg.to("msg:", " 分页查询结束"));
		return resp(ResultCode.Success, "SUCCESS", pagination);
	}

	@PostMapping(value = "/insertOrUpdate", produces = GlobalContext.PRODUCES)
	public String insert(@RequestBody EmailSettingForm emailSettingForm) {
		LOGGER.info(LogMsg.to("msg:", " 新增或修改开始", "emailSettingForm", emailSettingForm));
		iEmailSettingFactory.insertOrUpdate(emailSettingForm);
		LOGGER.info(LogMsg.to("msg:", " 新增或修改结束"));
		return resp(ResultCode.Success, "Success", null);
	}
}
