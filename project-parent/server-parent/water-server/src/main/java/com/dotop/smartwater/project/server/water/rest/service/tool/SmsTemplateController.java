package com.dotop.smartwater.project.server.water.rest.service.tool;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import com.dotop.smartwater.dependence.core.common.BaseController;
import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.VerificationUtils;
import com.dotop.smartwater.project.module.api.tool.ISmsTemplateFactory;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.module.core.water.form.SmsTemplateForm;
import com.dotop.smartwater.project.module.core.water.vo.SmsTemplateVo;

/**

 */

@RestController

@RequestMapping("/smsTemplateController")
public class SmsTemplateController implements BaseController<SmsTemplateForm> {

	@Resource
	private ISmsTemplateFactory iSmsTemplateFactory;

	private static final Logger LOGGER = LoggerFactory.getLogger(SmsTemplateController.class);

	@Override
	@PostMapping(value = "/list", produces = GlobalContext.PRODUCES)
	public String list(@RequestBody SmsTemplateForm smsTemplate) {

		LOGGER.info(LogMsg.to("msg:", " 分页查询开始", smsTemplate));
		Pagination<SmsTemplateVo> pagination = iSmsTemplateFactory.getPageList(smsTemplate);

		LOGGER.info(LogMsg.to("msg:", " 分页查询结束"));
		return resp(ResultCode.Success, ResultCode.SUCCESS, pagination);
	}

	@PostMapping(value = "/getSmsTemplate", produces = GlobalContext.PRODUCES)
	public String getSmsTemplate(@RequestBody SmsTemplateForm smsTemplate) {

		LOGGER.info(LogMsg.to("msg:", " 查询详情开始", smsTemplate));
		String id = smsTemplate.getId();
		VerificationUtils.string("id", id);
		SmsTemplateVo smsTemplateVo = iSmsTemplateFactory.getSmsTemplateVo(smsTemplate);
		if (smsTemplateVo != null) {
			LOGGER.info(LogMsg.to("msg:", " 查询详情结束"));
			return resp(ResultCode.Success, ResultCode.SUCCESS, smsTemplateVo);
		}
		return resp(ResultCode.Fail, "模板不存在", null);
	}

	@PostMapping(value = "/delete", produces = GlobalContext.PRODUCES)
	public String delete(@RequestBody SmsTemplateForm smsTemplate) {

		LOGGER.info(LogMsg.to("msg:", " 删除开始", smsTemplate));

		String id = smsTemplate.getId();
		VerificationUtils.string("id", id);

		int num = iSmsTemplateFactory.delete(smsTemplate.getId());
		if (num > 0) {
			LOGGER.info(LogMsg.to("msg:", " 删除结束"));
			return resp(ResultCode.Success, ResultCode.SUCCESS, null);
		}
		return resp(ResultCode.Fail, "unknown error", null);
	}

	@PostMapping(value = "/add", produces = GlobalContext.PRODUCES)
	public String add(@RequestBody SmsTemplateForm smsTemplate) {
		LOGGER.info(LogMsg.to("msg:", " 新增开始", smsTemplate));
		String name = smsTemplate.getName();
		String code = smsTemplate.getCode();
		Integer smstype = smsTemplate.getSmstype();
		VerificationUtils.string("name", name);
		VerificationUtils.integer("smstype", smstype);
		VerificationUtils.string("code", code);

		// 判断模板标识是否存在
		if (iSmsTemplateFactory.getBySmstype(smsTemplate.getEnterpriseid(), smsTemplate.getSmstype()) != null) {
			return resp(ResultCode.Fail, "消息模板已存在", null);
		}
		smsTemplate.setStatus(0);
		if (iSmsTemplateFactory.addSmsTemplate(smsTemplate) > 0) {
			LOGGER.info(LogMsg.to("msg:", " 新增结束"));
			return resp(ResultCode.Success, ResultCode.SUCCESS, null);
		}
		return resp(ResultCode.Fail, "添加短信模板失败", null);
	}

	@Override
	@PostMapping(value = "/edit", produces = GlobalContext.PRODUCES)
	public String edit(@RequestBody SmsTemplateForm smsTemplate) {
		LOGGER.info(LogMsg.to("msg:", " 更新开始", smsTemplate));
		String name = smsTemplate.getName();
		String code = smsTemplate.getCode();
		String id = smsTemplate.getId();
		String smsptid = smsTemplate.getSmsptid();
		String content = smsTemplate.getContent();

		VerificationUtils.string("name", name);
		VerificationUtils.string("smsptid", smsptid);
		VerificationUtils.string("code", code);
		VerificationUtils.string("id", id);
		VerificationUtils.string("content", content);

		if (iSmsTemplateFactory.updateSmsTemplate(smsTemplate) > 0) {
			LOGGER.info(LogMsg.to("msg:", " 更新结束"));
			return resp(ResultCode.Success, ResultCode.SUCCESS, null);
		}
		return resp(ResultCode.Fail, "unknown error", null);
	}

	@PostMapping(value = "/disable", produces = GlobalContext.PRODUCES)
	public String disable(@RequestBody SmsTemplateForm smsTemplate) {
		LOGGER.info(LogMsg.to("msg:", " 更新状态开始", smsTemplate));
		String id = smsTemplate.getId();
		VerificationUtils.string("id", id);

		// 插入数据库
		int num = iSmsTemplateFactory.updateStatus(smsTemplate.getId(), SmsTemplateVo.STATUS_DISABLE);
		if (num > 0) {
			LOGGER.info(LogMsg.to("msg:", " 更新状态结束"));
			return resp(ResultCode.Success, ResultCode.SUCCESS, null);
		}
		return resp(ResultCode.Fail, "unknown error", null);

	}

	@PostMapping(value = "/enable", produces = GlobalContext.PRODUCES)
	public String enable(@RequestBody SmsTemplateForm smsTemplate) {
		LOGGER.info(LogMsg.to("msg:", " 更新状态开始", smsTemplate));
		String id = smsTemplate.getId();
		VerificationUtils.string("id", id);

		// 插入数据库
		int num = iSmsTemplateFactory.updateStatus(smsTemplate.getId(), SmsTemplateVo.STATUS_ENABLE);
		if (num > 0) {
			LOGGER.info(LogMsg.to("msg:", " 更新状态结束"));
			return resp(ResultCode.Success, ResultCode.SUCCESS, null);
		}
		return resp(ResultCode.Fail, "unknown error", null);

	}
}
