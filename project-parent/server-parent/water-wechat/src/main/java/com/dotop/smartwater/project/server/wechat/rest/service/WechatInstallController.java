package com.dotop.smartwater.project.server.wechat.rest.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.dotop.smartwater.dependence.core.common.BaseController;
import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.VerificationUtils;
import com.dotop.smartwater.project.module.api.revenue.IInstallAppointmentFactory;
import com.dotop.smartwater.project.module.api.tool.IDictionaryFactory;
import com.dotop.smartwater.project.module.api.wechat.IWechatInstallFactory;
import com.dotop.smartwater.project.module.api.wechat.IWechatUserFactory;
import com.dotop.smartwater.project.module.core.third.constants.CommonConstant;
import com.dotop.smartwater.project.module.core.water.constants.AppointmentStatus;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.module.core.water.form.DictionaryForm;
import com.dotop.smartwater.project.module.core.water.form.InstallAppointmentDetailForm;
import com.dotop.smartwater.project.module.core.water.form.InstallAppointmentForm;
import com.dotop.smartwater.project.module.core.water.form.WechatUserForm;
import com.dotop.smartwater.project.module.core.water.vo.DictionaryChildVo;
import com.dotop.smartwater.project.module.core.water.vo.InstallAppointmentDetailVo;
import com.dotop.smartwater.project.module.core.water.vo.InstallAppointmentVo;
import com.dotop.smartwater.project.module.core.water.vo.WechatUserVo;

/**
 * 微信报装信息管理
 * 

 * @date 2019年3月31日
 *
 */
@RestController
@RequestMapping("/install")

public class WechatInstallController implements BaseController<WechatUserForm> {

	private static final Logger logger = LogManager.getLogger(WechatInstallController.class);

	@Autowired
	private IWechatInstallFactory factory;

	@Resource
	private IInstallAppointmentFactory appFactory;

	@Autowired
	private IDictionaryFactory iDictionaryFactory;

	@Autowired
	private IWechatUserFactory iWechatUserFactory;

	/**
	 * 保存用户信息
	 */
	@Override
	@PostMapping(value = "/add", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
	public String add(@RequestBody WechatUserForm form) {
		logger.info(LogMsg.to("msg:", "新增用户信息开始", "form", form));
		// 校验
		VerificationUtils.string("openid", form.getOpenid());
		factory.save(form);
		logger.info(LogMsg.to("msg:", "新增用户信息结束"));
		return resp(ResultCode.Success, ResultCode.SUCCESS, null);
	}

	@PostMapping(value = "/login", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
	public String login(@RequestBody WechatUserForm form) {
		logger.info(LogMsg.to("msg:", "微信公众号登录开始", "form", form));
		// 校验
		VerificationUtils.string("openid为空", form.getOpenid());

		if (!"99999".equals(form.getOpenid()) && !"88888".equals(form.getOpenid()) && !"77777".equals(form.getOpenid())
				&& !"66666".equals(form.getOpenid()) && !"55555".equals(form.getOpenid())) {
			// form.getOpenid() 这个openid不是真正的openid 是一个可变的值 所以要获取一个固定的值
			Map<String, Object> mapConfig = iWechatUserFactory.getWeixinOpenId(form.getOpenid(),
					form.getEnterpriseid());
			String openId = (String) mapConfig.get(CommonConstant.WEIXIN_OPEN_ID);
			form.setOpenid(openId); // 这个是一个不变的值
		}
		Map<String, String> map = factory.login(form);
		logger.info(LogMsg.to("msg:", "微信公众号登录结束"));
		return resp(ResultCode.Success, ResultCode.SUCCESS, map);
	}

	/**
	 * 修改用户信息
	 */
	@Override
	@PostMapping(value = "/edit", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
	public String edit(@RequestBody WechatUserForm form) {
		logger.info(LogMsg.to("msg:", "更新用户信息开始", "form", form));
		// 校验
		VerificationUtils.string("id", form.getId());
		VerificationUtils.string("name", form.getName());
		VerificationUtils.string("联系方式", form.getPhone());
		VerificationUtils.string("cardid", form.getCardid());
		factory.update(form);
		Map<String, String> map = factory.login(form);
		logger.info(LogMsg.to("msg:", "更新用户信息结束"));
		return resp(ResultCode.Success, ResultCode.SUCCESS, map);
	}

	/**
	 * 获取用户信息
	 */
	@Override
	@PostMapping(value = "/get", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
	public String get(@RequestBody WechatUserForm form) {
		logger.info(LogMsg.to("msg:", "获取用户信息开始", "form", form));
		WechatUserVo vo = factory.get(form);
		logger.info(LogMsg.to("msg:", "获取用户信息结束"));
		return resp(ResultCode.Success, ResultCode.SUCCESS, vo);
	}

	// 预约管理分页查询
	@PostMapping(value = "/page", produces = GlobalContext.PRODUCES)
	public String page(@RequestBody InstallAppointmentForm form) {
		logger.info(LogMsg.to("msg:", " 预约管理分页查询", "form", form));
		VerificationUtils.string("applyId", form.getApplyId());
		VerificationUtils.string("enterpriseid", form.getEnterpriseid());
		Pagination<InstallAppointmentVo> pagination = appFactory.wechatPage(form);
		logger.info(LogMsg.to("msg:", " 预约管理分页查询结束", "form", form));
		return resp(ResultCode.Success, ResultCode.SUCCESS, pagination);
	}

	// 预约管理-详情
	@PostMapping(value = "/detail", produces = GlobalContext.PRODUCES)
	public String detail(@RequestBody InstallAppointmentForm form) {
		logger.info(LogMsg.to("msg:", " 预约管理详情查询", "form", form));
		VerificationUtils.string("number", form.getNumber());
		InstallAppointmentVo vo = appFactory.wechatDetail(form);
		logger.info(LogMsg.to("msg:", " 预约管理详情查询结束", "form", form));
		return resp(ResultCode.Success, ResultCode.SUCCESS, vo);
	}

	// 验证是否有待办申请未处理
	@PostMapping(value = "/getTodoApply", produces = GlobalContext.PRODUCES)
	public String getTodoApply(@RequestBody InstallAppointmentForm form) {
		logger.info(LogMsg.to("msg:", "获取预约详情开始", "form", form));
		// 参数校验
		VerificationUtils.string("applyId", form.getApplyId());
		if (appFactory.checkNohandles(form) > 0) {
			return resp(ResultCode.APPLY_NO_HANDLE_ERROR, ResultCode.getMessage(ResultCode.APPLY_NO_HANDLE_ERROR),
					null);
		}

		logger.info(LogMsg.to("msg:", " 获取预约详情结束", "form", form));
		return resp(ResultCode.Success, ResultCode.SUCCESS, null);
	}

	// 获取预约详情
	@PostMapping(value = "/getAppointmentDetail", produces = GlobalContext.PRODUCES)
	public String getAppointmentDetail(@RequestBody InstallAppointmentDetailForm form) {
		logger.info(LogMsg.to("msg:", "获取预约详情开始", "form", form));
		// 参数校验
		VerificationUtils.string("enterpriseid", form.getEnterpriseid());
		List<InstallAppointmentDetailVo> details = appFactory.getAppointmentDetail(form);
		logger.info(LogMsg.to("msg:", " 获取预约详情结束", "form", form));
		return resp(ResultCode.Success, ResultCode.SUCCESS, details);
	}

	// 获取字典信息
	@PostMapping(value = "/getChildren", produces = GlobalContext.PRODUCES)
	public String getChildren(@RequestBody DictionaryForm form) {
		logger.info(LogMsg.to("msg:", "获取字典开始", "form", form));
		// 参数校验
		VerificationUtils.string("dictionaryCode", form.getDictionaryCode());
		VerificationUtils.string("enterpriseid", form.getEnterpriseid());
		List<DictionaryChildVo> list = iDictionaryFactory.getWechatChildren(form);
		logger.info(LogMsg.to("msg:", " 获取字典结束", "form", form));
		return resp(ResultCode.Success, ResultCode.SUCCESS, list);
	}

	// 报装申请提交
	@PostMapping(value = "/subApply", produces = GlobalContext.PRODUCES)
	public String subApply(@RequestBody InstallAppointmentForm form) {
		logger.info(LogMsg.to("msg:", "报装申请提交开始", "form", form));
		// 参数校验
		VerificationUtils.string("typeId", form.getTypeId());
		VerificationUtils.string("typeName", form.getTypeName());
		VerificationUtils.string("applyId", form.getApplyId());
		VerificationUtils.string("applyName", form.getApplyName());
		VerificationUtils.string("phone", form.getPhone());
		VerificationUtils.string("cardType", form.getCardType());
		VerificationUtils.string("cardId", form.getCardId());
		VerificationUtils.string("appTime", form.getAppTime());
		if (form.getTypeId().equals(AppointmentStatus.TYPEAPPLY)) { // 报装
			VerificationUtils.string("applyTypeId", form.getApply().getApplyTypeId());
			VerificationUtils.string("applyTypeName", form.getApply().getApplyTypeName());
			VerificationUtils.string("name", form.getApply().getName());
			VerificationUtils.string("contacts", form.getApply().getContacts());
			VerificationUtils.string("phone", form.getApply().getPhone());
			VerificationUtils.string("cardType", form.getApply().getCardType());
			VerificationUtils.string("cardId", form.getApply().getCardId());
			VerificationUtils.string("households", form.getApply().getHouseholds());
			VerificationUtils.string("deviceNumbers", form.getApply().getDeviceNumbers());

			VerificationUtils.string("addr", form.getApply().getAddr());
			VerificationUtils.string("purposeId", form.getApply().getPurposeId());
			VerificationUtils.string("purposeName", form.getApply().getPurposeName());
		} else if (form.getTypeId().equals(AppointmentStatus.TYPECHANGE)) { // 换表
			VerificationUtils.string("userNo", form.getChange().getUserNo());
			VerificationUtils.string("userName", form.getChange().getUserName());
			VerificationUtils.string("userPhone", form.getChange().getUserPhone());
			VerificationUtils.string("cardId", form.getChange().getCardId());
			VerificationUtils.string("cardType", form.getChange().getCardType());

		}
		appFactory.save(form);
		logger.info(LogMsg.to("msg:", " 报装申请提交结束", "form", form));
		return resp(ResultCode.Success, ResultCode.SUCCESS, null);
	}

}
