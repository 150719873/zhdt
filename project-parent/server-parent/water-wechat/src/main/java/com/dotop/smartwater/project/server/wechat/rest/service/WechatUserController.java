package com.dotop.smartwater.project.server.wechat.rest.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.dotop.smartwater.project.module.core.water.vo.customize.WechatPayTypeShowVo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.dotop.smartwater.dependence.core.common.BaseController;
import com.dotop.smartwater.dependence.core.common.BaseForm;
import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.utils.VerificationUtils;
import com.dotop.smartwater.project.module.api.wechat.IWechatUserFactory;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.module.core.water.form.customize.WechatParamForm;
import com.dotop.smartwater.project.module.core.water.vo.OwnerVo;

/**
 * 余额变动
 * 

 * @date 2019年3月22日
 */
@RestController()

@RequestMapping("/Wechat")
public class WechatUserController implements BaseController<BaseForm> {

	private static final Logger logger = LogManager.getLogger(WechatUserController.class);

	@Autowired
	private IWechatUserFactory iWechatUserFactory;

	@PostMapping(value = "/Session/set", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
	public String set(HttpServletRequest request, @RequestBody WechatParamForm wechatParamForm) {
		logger.info(LogMsg.to("msg:", "微信用户登录功能开始", "wechatParamForm", wechatParamForm));
		String code = wechatParamForm.getCode();
		VerificationUtils.string("code", code);
		wechatParamForm.setRequest(request);
		Map<String, String> map = iWechatUserFactory.setSession(wechatParamForm);
		logger.info(LogMsg.to("msg:", "微信用户登录功能结束", "wechatParamForm", map));
		return resp(ResultCode.Success, ResultCode.SUCCESS, map);
	}

	/**
	 * 获取绑定的所有业主列表 前台没有传递参数
	 *
	 * @return
	 */
	@PostMapping(value = "/getowner", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
	public String getowner() {
		logger.info(LogMsg.to("msg:", "获取绑定的所有业主列表功能开始"));
		List<OwnerVo> list = iWechatUserFactory.getOwnerList();
		logger.info(LogMsg.to("msg:", "获取绑定的所有业主列表功能结束"));
		return resp(ResultCode.Success, ResultCode.SUCCESS, list);
	}

	/**
	 * 获取当前登录后的绑定业主信息
	 *
	 * @return
	 */
	@PostMapping(value = "/getownerInfo", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
	public String getOwnerInfo() {
		logger.info(LogMsg.to("msg:", "查询登录后的绑定业主信息功能开始"));
		OwnerVo ownerVo = iWechatUserFactory.getOwnerInfo();
		logger.info(LogMsg.to("msg:", "查询登录后的绑定业主信息功能结束"));
		return resp(ResultCode.Success, ResultCode.SUCCESS, ownerVo);
	}

	/**
	 * 绑定业主信息
	 *
	 * @return
	 */
	@PostMapping(value = "/blindOwner", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
	public String blindOwner(@RequestBody WechatParamForm wechatParamForm) {
		logger.info(LogMsg.to("msg:", "绑定业主信息功能开始"));
		String userName = wechatParamForm.getUsername();
		String userMsg = wechatParamForm.getUsermsg();
		VerificationUtils.string("userName", userName);
		VerificationUtils.string("userMsg", userMsg);
		iWechatUserFactory.blindOwner(wechatParamForm);
		logger.info(LogMsg.to("msg:", "绑定业主信息功能结束"));
		return resp(ResultCode.Success, ResultCode.SUCCESS, null);
	}

	/**
	 * 切换当前的绑定业主
	 *
	 * @param wechatParamForm
	 * @return
	 */
	@PostMapping(value = "/changeOwner", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
	public String changeOwner(@RequestBody WechatParamForm wechatParamForm) {
		logger.info(LogMsg.to("msg:", "切换业主信息功能开始"));
		String ownerid = wechatParamForm.getOwnerid();
		VerificationUtils.string("ownerid:", ownerid);
		iWechatUserFactory.changeOwner(ownerid);
		logger.info(LogMsg.to("msg:", "切换业主信息功能结束"));
		return resp(ResultCode.Success, ResultCode.SUCCESS, null);
	}

	/**
	 * 删除绑定关系
	 *
	 * @param wechatParamForm
	 * @return
	 */
	@PostMapping(value = "/deleteOwnerBlind", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
	public String deleteOwnerBlind(@RequestBody WechatParamForm wechatParamForm) {
		logger.info(LogMsg.to("msg:", "删除绑定功能开始"));
		String ownerid = wechatParamForm.getOwnerid();
		VerificationUtils.string("ownerid", ownerid);
		iWechatUserFactory.deleteOwnerBlind(ownerid);
		logger.info(LogMsg.to("msg:", "删除绑定功能结束"));
		return resp(ResultCode.Success, ResultCode.SUCCESS, null);
	}

	/**
	 * 更新业主的绑定状态
	 *
	 * @param wechatParamForm
	 * @return
	 */
	@PostMapping(value = "/updateBlindStatus", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
	public String updateBlindStatus(@RequestBody WechatParamForm wechatParamForm) {
		logger.info(LogMsg.to("msg:", "更新业主的绑定状态功能开始"));
		String ownerid = wechatParamForm.getOwnerid();
		Integer isdefaultblind = wechatParamForm.getIsdefaultblind();
		VerificationUtils.string("ownerid", ownerid);
		VerificationUtils.integer("isdefaultblind", isdefaultblind);
		iWechatUserFactory.updateBlindStatus(wechatParamForm);
		logger.info(LogMsg.to("msg:", "更新业主的绑定状态功能结束"));
		return resp(ResultCode.Success, ResultCode.SUCCESS, null);
	}

	/**
	 * 设置自动扣费功能
	 *
	 * @param wechatParamForm
	 * @return
	 */
	@PostMapping(value = "/setIschargebacks", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
	public String setIschargebacks(@RequestBody WechatParamForm wechatParamForm) {
		logger.info(LogMsg.to("msg:", "设置自动扣费功能开始"));
		String ownerid = wechatParamForm.getOwnerid();
		Integer ischargebacks = wechatParamForm.getIschargebacks();
		VerificationUtils.string("ownerid", ownerid);
		VerificationUtils.integer("ischargebacks", ischargebacks);
		iWechatUserFactory.setIschargebacks(wechatParamForm);
		logger.info(LogMsg.to("msg:", "设置自动扣费功能结束"));
		return resp(ResultCode.Success, ResultCode.SUCCESS, null);
	}

	/**
	 * 显示用户收费标准和半年用水
	 *
	 * @param wechatParamForm
	 * @return
	 */
	@PostMapping(value = "/showPayType", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
	public String showPayType(@RequestBody WechatParamForm wechatParamForm) {
		logger.info(LogMsg.to("msg:", "显示用户收费标准和半年用水"));
		WechatPayTypeShowVo vo = iWechatUserFactory.showPayType();
		logger.info(LogMsg.to("msg:", "显示用户收费标准和半年用水"));
		return resp(ResultCode.Success, ResultCode.SUCCESS, vo);
	}

}
