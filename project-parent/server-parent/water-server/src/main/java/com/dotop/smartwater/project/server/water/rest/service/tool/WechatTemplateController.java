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
import com.dotop.smartwater.project.module.api.tool.IWechatTemplateFactory;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.module.core.water.form.customize.WechatTemplateForm;
import com.dotop.smartwater.project.module.core.water.vo.WechatTemplateVo;

/**
 * 微信模板配置
 * 

 * @date 2019年4月1日
 */
@RestController()

@RequestMapping("/wechat/allocation")
public class WechatTemplateController implements BaseController<WechatTemplateForm> {

	private static final Logger LOGGER = LogManager.getLogger(WechatTemplateController.class);

	@Autowired
	private IWechatTemplateFactory iWechatTemplateFactory;

	private static final int MAXLENGTH100 = 100;

	private static final int MAXLENGTH500 = 500;

	/**
	 * 新增短信发送平台绑定
	 */
	@Override
	@PostMapping(value = "/template/add", produces = GlobalContext.PRODUCES)
	public String add(@RequestBody WechatTemplateForm wechatTemplateForm) {

		LOGGER.info(LogMsg.to("msg:", "微信接入绑定功能开始", wechatTemplateForm));
		String name = wechatTemplateForm.getName();
		String smsptid = wechatTemplateForm.getSmsptid();
		String enterpriseid = wechatTemplateForm.getEnterpriseid();
		Integer wechatname = wechatTemplateForm.getSmstype();

		// 校验
		VerificationUtils.string("name", name);
		VerificationUtils.string("smsptid", smsptid, false, MAXLENGTH100);
		VerificationUtils.string("enterpriseid", enterpriseid);
		VerificationUtils.integer("wechatname", wechatname);
		WechatTemplateVo wechatTemplateVo = iWechatTemplateFactory.add(wechatTemplateForm);
		LOGGER.info(LogMsg.to("msg:", "微信接入绑定功能结束", wechatTemplateForm));
		return resp(ResultCode.Success, ResultCode.Success, wechatTemplateVo);
	}

	/**
	 * 微信模板分页查询
	 */
	@Override
	@PostMapping(value = "/template/list", produces = GlobalContext.PRODUCES)
	public String page(@RequestBody WechatTemplateForm wechatTemplateForm) {
		LOGGER.info(LogMsg.to("msg:", " 分页查询开始", "deviceForm", wechatTemplateForm));
		Integer page = wechatTemplateForm.getPage();
		Integer pageCount = wechatTemplateForm.getPageCount();
		// 验证
		VerificationUtils.integer("page", page);
		VerificationUtils.integer("pageCount", pageCount);
		Pagination<WechatTemplateVo> pagination = iWechatTemplateFactory.page(wechatTemplateForm);
		LOGGER.info(LogMsg.to("msg:", " 分页查询查询结束"));
		return resp(ResultCode.Success, ResultCode.Success, pagination);
	}

	/**
	 * 微信模板修改编辑
	 */
	@Override
	@PostMapping(value = "/template/edit", produces = GlobalContext.PRODUCES)
	public String edit(@RequestBody WechatTemplateForm wechatTemplateForm) {
		LOGGER.info(LogMsg.to("msg:", "编辑内容开始", wechatTemplateForm));

		String id = wechatTemplateForm.getId();
		String content = wechatTemplateForm.getContent();
		String name = wechatTemplateForm.getName();
		String smsptid = wechatTemplateForm.getSmsptid();

		// 校验
		VerificationUtils.string("id", id);
		VerificationUtils.string("content", content, false, MAXLENGTH500);
		VerificationUtils.string("name", name);
		VerificationUtils.string("smsptid", smsptid, false, MAXLENGTH100);
		WechatTemplateVo wechatTemplateVo = iWechatTemplateFactory.edit(wechatTemplateForm);
		LOGGER.info(LogMsg.to("msg:", "编辑内容结束"));
		return resp(ResultCode.Success, ResultCode.Success, wechatTemplateVo);
	}

	/**
	 * 微信模板 删除记录
	 */
	@Override
	@PostMapping(value = "/template/delete", produces = GlobalContext.PRODUCES)
	public String del(@RequestBody WechatTemplateForm wechatTemplateForm) {
		LOGGER.info(LogMsg.to("msg:", "删除内容开始", wechatTemplateForm));
		String id = wechatTemplateForm.getId();
		VerificationUtils.string("id", id);
		String idstr = iWechatTemplateFactory.del(wechatTemplateForm);
		LOGGER.info(LogMsg.to("msg:", "删除内容结束", "id", idstr));
		return resp(ResultCode.Success, ResultCode.Success, id);
	}

	/**
	 * 微信模板详情
	 */
	@Override
	@PostMapping(value = "/template/detail", produces = GlobalContext.PRODUCES)
	public String get(@RequestBody WechatTemplateForm wechatTemplateForm) {
		LOGGER.info(LogMsg.to("msg:", "查询详情开始", wechatTemplateForm));
		String id = wechatTemplateForm.getId();
		VerificationUtils.string("id", id);
		WechatTemplateVo wechatTemplateVo = iWechatTemplateFactory.get(wechatTemplateForm);
		LOGGER.info(LogMsg.to("msg:", "查询详情结束", wechatTemplateForm));
		return resp(ResultCode.Success, ResultCode.Success, wechatTemplateVo);
	}

	/**
	 * 微信模板禁用
	 * 
	 * @param wechatTemplateForm
	 * @return
	 */
	@PostMapping(value = "/template/disable", produces = GlobalContext.PRODUCES)
	public String templateDisable(@RequestBody WechatTemplateForm wechatTemplateForm) {
		LOGGER.info(LogMsg.to("msg:", "禁用微信模板开始", wechatTemplateForm));
		String id = wechatTemplateForm.getId();
		// 校验
		VerificationUtils.string("id", id);
		WechatTemplateVo wechatTemplateVo = iWechatTemplateFactory.disable(wechatTemplateForm);
		LOGGER.info(LogMsg.to("msg:", "禁用微信模板结束"));
		return resp(ResultCode.Success, ResultCode.Success, wechatTemplateVo);
	}

	/**
	 * 微信模板启用
	 * 
	 * @param wechatTemplateForm
	 * @return
	 */
	@PostMapping(value = "/template/enable", produces = GlobalContext.PRODUCES)
	public String templateEnable(@RequestBody WechatTemplateForm wechatTemplateForm) {
		LOGGER.info(LogMsg.to("msg:", "启用微信模板开始", wechatTemplateForm));
		String id = wechatTemplateForm.getId();
		// 校验
		VerificationUtils.string("id", id);
		WechatTemplateVo wechatTemplateVo = iWechatTemplateFactory.enable(wechatTemplateForm);
		LOGGER.info(LogMsg.to("msg:", "启用微信模板结束"));
		return resp(ResultCode.Success, ResultCode.Success, wechatTemplateVo);
	}

}
