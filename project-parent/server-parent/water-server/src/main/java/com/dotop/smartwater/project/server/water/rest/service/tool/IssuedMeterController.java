package com.dotop.smartwater.project.server.water.rest.service.tool;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.dotop.smartwater.dependence.core.common.BaseController;
import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.utils.VerificationUtils;
import com.dotop.smartwater.project.module.api.tool.IIssuedMeterFactory;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.module.core.water.form.IssuedMeterForm;

/**
 * 下发抄表任务
 * 

 * @date 2019年4月5日
 *
 */
@RestController()

@RequestMapping("/issuedMeter")
public class IssuedMeterController implements BaseController<IssuedMeterForm> {

	private static final Logger LOGGER = LogManager.getLogger(IssuedMeterController.class);

	private static final int MAXLENGTH = 4000;

	@Autowired
	private IIssuedMeterFactory factory;

	// 生成下发抄表任务
	@PostMapping(value = "/generate", produces = GlobalContext.PRODUCES)
	public String generate(@RequestBody IssuedMeterForm form) {
		LOGGER.info(LogMsg.to("msg:", "下发抄表任务开始", "form", form));
		// 参数校验
		VerificationUtils.string("name", form.getName());
		VerificationUtils.string("communityids", form.getCommunityids(), false, MAXLENGTH);
		VerificationUtils.string("endTime", form.getEndTime());
		factory.generate(form);
		LOGGER.info(LogMsg.to("msg:", " 下发抄表任务结束", "form", form));
		return resp(ResultCode.Success, ResultCode.SUCCESS, null);
	}

}
