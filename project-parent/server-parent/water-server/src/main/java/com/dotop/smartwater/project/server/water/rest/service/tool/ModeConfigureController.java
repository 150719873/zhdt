package com.dotop.smartwater.project.server.water.rest.service.tool;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.dotop.smartwater.dependence.core.common.BaseController;
import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.utils.VerificationUtils;
import com.dotop.smartwater.project.module.api.tool.IModeConfigureFactory;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.module.core.water.form.ModeBindForm;
import com.dotop.smartwater.project.module.core.water.vo.ModeBindVo;
import com.dotop.smartwater.project.server.water.common.FoundationController;

/**
*
* @author YangKe
* @description 通讯方式配置
* @date 2019-10-17
*/

@RestController

@RequestMapping("/configure")
public class ModeConfigureController extends FoundationController implements BaseController<ModeBindForm>{

	private static final Logger LOGGER = LoggerFactory.getLogger(ModeConfigureController.class);
	
	@Autowired
	private IModeConfigureFactory iModeConfigureFactory;
	
	@PostMapping(value = "/configureMode", produces = GlobalContext.PRODUCES)
    public String configureMode(@RequestBody List<ModeBindForm> modeBindForms) {
		LOGGER.info(LogMsg.to("msg:", "通讯方式配置开始", "modeBindForms", modeBindForms));
		
		if(modeBindForms == null || modeBindForms.isEmpty()) {
			modeBindForms = new ArrayList<ModeBindForm>();
			ModeBindForm form = new ModeBindForm();
			form.setMode(ModeBindVo.DEFAULT_WIPE_DATA);
			modeBindForms.add(form);
		}
		for(ModeBindForm modeBindForm: modeBindForms) {
			// 校验
			String mode = modeBindForm.getMode();
	        VerificationUtils.string("mode", mode);
		}
		String message = iModeConfigureFactory.configureMode(modeBindForms);
		LOGGER.info(LogMsg.to("msg:", "通讯方式配置结束", "modeBindForms", modeBindForms));
		if("success".equals(message)) {
			return resp(ResultCode.Success, ResultCode.SUCCESS, null);
		}else {
			return resp(ResultCode.Fail, message, null);
		}
    }
	
	@PostMapping(value = "/listModeConfigure", produces = GlobalContext.PRODUCES)
    public String listModeConfigure(@RequestBody ModeBindForm modeBindForm) {
		LOGGER.info(LogMsg.to("msg:", "获取通讯方式配置开始", "modeBindForm", modeBindForm));
		List<ModeBindVo> list = iModeConfigureFactory.listModeConfigure(modeBindForm);
        LOGGER.info(LogMsg.to("msg:", "获取通讯方式配置结束", "modeBindForm", modeBindForm));
        return resp(ResultCode.Success, ResultCode.SUCCESS, list);
    }
}
