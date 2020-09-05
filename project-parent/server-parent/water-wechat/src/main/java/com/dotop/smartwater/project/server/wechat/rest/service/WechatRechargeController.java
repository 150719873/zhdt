package com.dotop.smartwater.project.server.wechat.rest.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.project.module.api.revenue.IPaymentFactory;
import com.dotop.smartwater.project.module.core.auth.wechat.WechatAuthClient;
import com.dotop.smartwater.project.module.core.auth.wechat.WechatUser;
import com.dotop.smartwater.project.module.core.pay.constants.PayConstants;
import com.dotop.smartwater.project.module.core.water.form.PaymentTradeOrderExtForm;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.dotop.smartwater.dependence.core.common.BaseController;
import com.dotop.smartwater.dependence.core.common.BaseForm;
import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.VerificationUtils;
import com.dotop.smartwater.project.module.api.wechat.IWechatRechargeFactory;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.module.core.water.form.customize.RechargeForm;
import com.dotop.smartwater.project.module.core.water.vo.PayDetailVo;

/**
 * 充值
 * 

 * @date 2019年3月22日
 */
@RestController()
@RequestMapping("/Wechat/Recharge")

public class WechatRechargeController implements BaseController<BaseForm> {

	private static final Logger logger = LogManager.getLogger(WechatRechargeController.class);

	@Autowired
	private IWechatRechargeFactory iWechatRechargeFactory;

	@Autowired
	private IPaymentFactory iPaymentFactory;

	/**
	 * 账号充值
	 * 
	 * @param request
	 * @param wechatParamForm
	 * @return
	 */
	@PostMapping(value = "/toup", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
	public String recharge(HttpServletRequest request, @RequestBody RechargeForm rechargeForm) {
		logger.info(LogMsg.to("msg:", "账号充值下单开始", "参数:", rechargeForm));
		Double amount = rechargeForm.getAmount();
		VerificationUtils.doubles("amount", amount);

		WechatUser wechatUser = WechatAuthClient.get();
		if(wechatUser == null){
			throw new FrameworkRuntimeException(ResultCode.WECHAT_NOT_OWNER_ERROR, "未有绑定业主");
		}
		String ownerId = wechatUser.getOwnerid();
		String enterpriseId = wechatUser.getEnterpriseid();
		String openid = wechatUser.getOpenid();

		PaymentTradeOrderExtForm ext = new PaymentTradeOrderExtForm();
		ext.setMode(PayConstants.PAYTYPE_WEIXIN);
		ext.setActualamount(String.valueOf(amount));
		ext.setChange("0");
		ext.setEnterpriseid(enterpriseId);
		ext.setOpenid(openid);
		ext.setOwnerid(ownerId);
		ext.setAmount(String.valueOf(amount));

		Map<String, String> map = iPaymentFactory.recharge(ext);
		logger.info(LogMsg.to("msg:", "账号充值下单结束", "结果:", map));
		return resp(ResultCode.Success, ResultCode.SUCCESS, map);
	}

	/*@PostMapping(value = "/query", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
	public String query(HttpServletRequest request, @RequestBody RechargeForm rechargeForm) {

		logger.info(LogMsg.to("msg:", "账号充值订单查询功能开始", "参数:", rechargeForm));
		String wechatmchno = rechargeForm.getWechatmchno();
		VerificationUtils.string("wechatmchno", wechatmchno);
		iWechatRechargeFactory.query(request, rechargeForm);
		logger.info(LogMsg.to("msg:", "账号充值订单查询功能结束"));
		return resp(ResultCode.Success, ResultCode.SUCCESS, null);
	}*/

	/**
	 * 分页查询
	 * 
	 * @param request
	 * @param rechargeForm
	 * @return
	 */
	@PostMapping(value = "/getRechargeLit", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
	public String getRechargeLit(HttpServletRequest request, @RequestBody RechargeForm rechargeForm) {

		logger.info(LogMsg.to("msg:", "账号充值订单查询功能开始", "参数:", rechargeForm));
		String ownerid = rechargeForm.getOwnerid();
		VerificationUtils.string("wechatmchno", ownerid);
		Pagination<PayDetailVo> pagination = iWechatRechargeFactory.getRechargeLit(request, rechargeForm);
		logger.info(LogMsg.to("msg:", "账号充值订单查询功能结束", "结果:", rechargeForm));
		return resp(ResultCode.Success, "SUCCESS", pagination);
	}

}
