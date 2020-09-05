package com.dotop.smartwater.project.server.water.rest.service.tool;

import static com.dotop.smartwater.project.module.core.water.constants.CacheKey.WaterRemindersSms;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.dotop.smartwater.dependence.cache.StringValueCache;
import com.dotop.smartwater.dependence.core.common.BaseController;
import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.dependence.core.utils.VerificationUtils;
import com.dotop.smartwater.project.module.api.revenue.IOrderSmsFactory;
import com.dotop.smartwater.project.module.core.auth.enums.SmsEnum;
import com.dotop.smartwater.project.module.core.auth.local.AuthCasClient;
import com.dotop.smartwater.project.module.core.auth.vo.UserVo;
import com.dotop.smartwater.project.module.core.water.bo.SmsConfigBo;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.module.core.water.form.customize.RemindersForm;
import com.dotop.smartwater.project.module.core.water.vo.SmsConfigVo;
import com.dotop.smartwater.project.module.core.water.vo.SmsTemplateVo;
import com.dotop.smartwater.project.module.service.tool.ISmsConfigService;
import com.dotop.smartwater.project.module.service.tool.ISmsTemplateService;

/**
 * @program: project-parent
 * @description: 账单类短信

 * @create: 2019-03-13 09:27
 **/
@RestController

@RequestMapping("/orderSms")
public class OrderSmsController implements BaseController<RemindersForm> {

	@Autowired
	private IOrderSmsFactory iOrderSmsFactory;
	@Resource
	private ISmsConfigService iSmsConfigService;
	@Resource
	private StringValueCache svc;

	@Resource
	private ISmsTemplateService iSmsTemplateService;

	/***
	 * 账单催缴
	 **/
	@PostMapping(value = "/reminders", produces = GlobalContext.PRODUCES)
	public String reminders(@RequestBody RemindersForm remindersForm) {

		VerificationUtils.integer("ModelId", remindersForm.getModelId());
		VerificationUtils.string("Days", remindersForm.getDays());
		VerificationUtils.string("BatchNo", remindersForm.getBatchNo());
		VerificationUtils.strList("CommunityIds", remindersForm.getCommunityIds());

		UserVo userVo = AuthCasClient.getUser();
		String enterpriseid = userVo.getEnterpriseid();
		int modelId = remindersForm.getModelId();

		String flag = svc.get(WaterRemindersSms + userVo.getEnterpriseid());
		if (StringUtils.isNotBlank(flag)) {
			return resp(ResultCode.Fail, "短信正在发送中，请勿多次提交", null);
		} else if (modelId != SmsEnum.dunning.intValue()) {
			return resp(ResultCode.Fail, "请选择催缴的模板", null);
		}

		SmsConfigBo smsConfigBo = new SmsConfigBo();
		smsConfigBo.setEnterpriseid(enterpriseid);
		SmsConfigVo smsSetup = iSmsConfigService.getByEnable(smsConfigBo);
		if (smsSetup == null) {
			return resp(ResultCode.Fail, "未启用短信配置", null);
		}
		// 获取真正的模板平台标识
		SmsTemplateVo smsTemplate = iSmsTemplateService.getEnableByCode(enterpriseid, modelId);
		if (smsTemplate == null) {
			return resp(ResultCode.Fail, "短信模板不存或未启用", null);
		}

		// 异步任务
		iOrderSmsFactory.reminders(remindersForm, userVo);
		return resp(ResultCode.Success, ResultCode.SUCCESS, null);
	}
}
