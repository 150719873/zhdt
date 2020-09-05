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
import com.dotop.smartwater.project.module.api.tool.IWechatPublicSettingFactory;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.module.core.water.form.WechatPublicSettingForm;
import com.dotop.smartwater.project.module.core.water.vo.customize.WechatPublicSettingVo;

/**
 * 微信接入配置
 * 

 * @date 2019年4月1日
 */
@RestController()

@RequestMapping("/wechat/allocation")
public class WechatPublicSettingController implements BaseController<WechatPublicSettingForm> {

	private static final Logger LOGGER = LogManager.getLogger(WechatPublicSettingController.class);

	@Autowired
	private IWechatPublicSettingFactory iWechatPublicSettingFactory;

	private static final String WECHATPUBLICID = "wechatpublicid";

	/**
	 * 新增短信发送平台绑定
	 */
	@Override
	@PostMapping(value = "/setting/save", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
	public String add(@RequestBody WechatPublicSettingForm wechatPublicSettingForm) {
		LOGGER.info(LogMsg.to("msg:", "微信接入绑定功能开始", wechatPublicSettingForm));
		String appid = wechatPublicSettingForm.getAppid();
		String mchid = wechatPublicSettingForm.getMchid();
		String appsecret = wechatPublicSettingForm.getAppsecret();
		String wechatname = wechatPublicSettingForm.getWechatname();
		String servicephone = wechatPublicSettingForm.getServicephone();
		String domain = wechatPublicSettingForm.getDomain();
		String paysecret = wechatPublicSettingForm.getPaysecret();
		String requestreturnurl = wechatPublicSettingForm.getRequestreturnurl();

		// 校验
		VerificationUtils.string("appid", appid);
		VerificationUtils.string("mchid", mchid);
		VerificationUtils.string("appsecret", appsecret);
		VerificationUtils.string("wechatname", wechatname);
		VerificationUtils.string("servicephone", servicephone);
		VerificationUtils.string("domain", domain);
		VerificationUtils.string("paysecret", paysecret);
		VerificationUtils.string("requestreturnurl", requestreturnurl);
		WechatPublicSettingVo wechatPublicSettingVo = iWechatPublicSettingFactory.add(wechatPublicSettingForm);
		LOGGER.info(LogMsg.to("msg:", "微信接入绑定功能结束", wechatPublicSettingForm));
		return resp(ResultCode.Success, ResultCode.Success, wechatPublicSettingVo);
	}

	@Override
	@PostMapping(value = "/setting/list", produces = GlobalContext.PRODUCES)
	public String page(@RequestBody WechatPublicSettingForm wechatPublicSettingForm) {
		LOGGER.info(LogMsg.to("msg:", " 分页查询开始", "deviceForm", wechatPublicSettingForm));
		Integer page = wechatPublicSettingForm.getPage();
		Integer pageCount = wechatPublicSettingForm.getPageCount();
		// 验证
		VerificationUtils.integer("page", page);
		VerificationUtils.integer("pageCount", pageCount);
		Pagination<WechatPublicSettingVo> pagination = iWechatPublicSettingFactory.page(wechatPublicSettingForm);
		LOGGER.info(LogMsg.to("msg:", " 分页查询查询结束"));
		return resp(ResultCode.Success, ResultCode.Success, pagination);
	}

	/**
	 * 编辑
	 */
	@Override
	@PostMapping(value = "/setting/update", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
	public String edit(@RequestBody WechatPublicSettingForm wechatPublicSettingForm) {
		LOGGER.info(LogMsg.to("msg:", "编辑内容开始", wechatPublicSettingForm));

		String wechatpublicid = wechatPublicSettingForm.getWechatpublicid();
		String appid = wechatPublicSettingForm.getAppid();
		String mchid = wechatPublicSettingForm.getMchid();
		String appsecret = wechatPublicSettingForm.getAppsecret();
		String wechatname = wechatPublicSettingForm.getWechatname();
		String servicephone = wechatPublicSettingForm.getServicephone();
		String domain = wechatPublicSettingForm.getDomain();
		String paysecret = wechatPublicSettingForm.getPaysecret();
		String requestreturnurl = wechatPublicSettingForm.getRequestreturnurl();

		// 校验
		VerificationUtils.string(WECHATPUBLICID, wechatpublicid);
		VerificationUtils.string("appid", appid);
		VerificationUtils.string("mchid", mchid);
		VerificationUtils.string("appsecret", appsecret);
		VerificationUtils.string("wechatname", wechatname);
		VerificationUtils.string("servicephone", servicephone);
		VerificationUtils.string("domain", domain);
		VerificationUtils.string("paysecret", paysecret);
		VerificationUtils.string("requestreturnurl", requestreturnurl);
		WechatPublicSettingVo wechatPublicSettingVo = iWechatPublicSettingFactory.edit(wechatPublicSettingForm);
		LOGGER.info(LogMsg.to("msg:", "编辑内容结束"));
		return resp(ResultCode.Success, ResultCode.Success, wechatPublicSettingVo);
	}

	/**
	 * 删除记录
	 */
	@Override
	@PostMapping(value = "/setting/delete", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
	public String del(@RequestBody WechatPublicSettingForm wechatPublicSettingForm) {
		LOGGER.info(LogMsg.to("msg:", "删除内容开始", wechatPublicSettingForm));
		String wechatpublicid = wechatPublicSettingForm.getWechatpublicid();
		VerificationUtils.string(WECHATPUBLICID, wechatpublicid);
		String id = iWechatPublicSettingFactory.del(wechatPublicSettingForm);
		LOGGER.info(LogMsg.to("msg:", "删除内容结束", "id", id));
		return resp(ResultCode.Success, ResultCode.Success, id);
	}

	/**
	 * 详情
	 */
	@Override
	@PostMapping(value = "/setting/detail", produces = GlobalContext.PRODUCES)
	public String get(@RequestBody WechatPublicSettingForm wechatPublicSettingForm) {
		LOGGER.info(LogMsg.to("msg:", "查询详情开始", wechatPublicSettingForm));
		String wechatpublicid = wechatPublicSettingForm.getWechatpublicid();
		VerificationUtils.string(WECHATPUBLICID, wechatpublicid);
		WechatPublicSettingVo wechatPublicSettingVo = iWechatPublicSettingFactory.get(wechatPublicSettingForm);
		LOGGER.info(LogMsg.to("msg:", "查询详情结束", wechatPublicSettingForm));
		return resp(ResultCode.Success, ResultCode.Success, wechatPublicSettingVo);
	}

}
