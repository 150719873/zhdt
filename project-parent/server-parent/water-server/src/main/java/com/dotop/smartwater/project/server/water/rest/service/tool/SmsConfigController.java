package com.dotop.smartwater.project.server.water.rest.service.tool;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.dotop.smartwater.dependence.core.common.BaseController;
import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.VerificationUtils;
import com.dotop.smartwater.project.module.api.tool.ISmsConfigFactory;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.module.core.water.form.SmsConfigForm;
import com.dotop.smartwater.project.module.core.water.vo.SmsConfigVo;

/**
 * 短信配置
 * 

 * @date 2019年2月23日
 */
@RestController()

@RequestMapping("/smsConfig")
public class SmsConfigController implements BaseController<SmsConfigForm> {

	private static final Logger LOGGER = LogManager.getLogger(SmsConfigController.class);

	@Autowired
	private ISmsConfigFactory iSmsConfigFactory;

	/**
	 * 新增短信发送平台绑定
	 */
	@Override
	@PostMapping(value = "/add", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
	public String add(@RequestBody SmsConfigForm smsConfigForm) {

		LOGGER.info(LogMsg.to("msg:", "新增短信发送平台绑定功能开始", smsConfigForm));
		String name = smsConfigForm.getName();
		String code = smsConfigForm.getCode();
		String sign = smsConfigForm.getSign();
		String mkey = smsConfigForm.getMkey();
		String mkeysecret = smsConfigForm.getMkeysecret();

		// 校验
		VerificationUtils.string("name", name);
		VerificationUtils.string("code", code);
		VerificationUtils.string("sign", sign);
		VerificationUtils.string("mkey", mkey);
		VerificationUtils.string("mkeysecret", mkeysecret);
		SmsConfigVo smsConfigVo = iSmsConfigFactory.add(smsConfigForm);
		LOGGER.info(LogMsg.to("msg:", "新增短信发送平台绑定功能结束", smsConfigForm));
		return resp(ResultCode.Success, ResultCode.Success, smsConfigVo);
	}

	@Override
	@PostMapping(value = "/page", produces = GlobalContext.PRODUCES)
	public String page(@RequestBody SmsConfigForm smsConfigForm) {
		LOGGER.info(LogMsg.to("msg:", " 分页查询开始", "deviceForm", smsConfigForm));
		Integer page = smsConfigForm.getPage();
		Integer pageCount = smsConfigForm.getPageCount();
		// 验证
		VerificationUtils.integer("page", page);
		VerificationUtils.integer("pageCount", pageCount);
		Pagination<SmsConfigVo> pagination = iSmsConfigFactory.page(smsConfigForm);
		LOGGER.info(LogMsg.to("msg:", " 分页查询查询结束"));
		return resp(ResultCode.Success, ResultCode.Success, pagination);
	}

	/**
	 * 编辑
	 */
	@Override
	@PostMapping(value = "/edit", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
	public String edit(@RequestBody SmsConfigForm smsConfigForm) {
		LOGGER.info(LogMsg.to("msg:", "编辑内容开始", smsConfigForm));

		String id = smsConfigForm.getId();
		String name = smsConfigForm.getName();
		String code = smsConfigForm.getCode();
		String sign = smsConfigForm.getSign();
		String mkey = smsConfigForm.getMkey();
		String mkeysecret = smsConfigForm.getMkeysecret();

		// 校验
		VerificationUtils.string("id", id);
		VerificationUtils.string("name", name);
		VerificationUtils.string("code", code);
		VerificationUtils.string("sign", sign);
		VerificationUtils.string("mkey", mkey);
		VerificationUtils.string("mkeysecret", mkeysecret);
		SmsConfigVo smsConfigVo = iSmsConfigFactory.edit(smsConfigForm);
		LOGGER.info(LogMsg.to("msg:", "编辑内容结束"));
		return resp(ResultCode.Success, ResultCode.Success, smsConfigVo);
	}

	/**
	 * 删除记录
	 */
	@Override
	@PostMapping(value = "/del", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
	public String del(@RequestBody SmsConfigForm smsConfigForm) {
		LOGGER.info(LogMsg.to("msg:", "删除内容开始", smsConfigForm));
		String smsId = smsConfigForm.getId();
		VerificationUtils.string("smsId", smsId);
		String id = iSmsConfigFactory.del(smsConfigForm);
		LOGGER.info(LogMsg.to("msg:", "删除内容结束", "id", id));
		return resp(ResultCode.Success, ResultCode.Success, id);
	}

	/**
	 * 详情
	 */
	@Override
	@GetMapping(value = "/get/{id}", produces = GlobalContext.PRODUCES)
	public String get(SmsConfigForm smsConfigForm) {
		LOGGER.info(LogMsg.to("msg:", "查询详情开始", smsConfigForm));
		String id = smsConfigForm.getId();
		VerificationUtils.string("id", id);
		SmsConfigVo smsConfigVo = iSmsConfigFactory.get(smsConfigForm);
		LOGGER.info(LogMsg.to("msg:", "查询详情结束", smsConfigForm));
		return resp(ResultCode.Success, ResultCode.Success, smsConfigVo);
	}

}
