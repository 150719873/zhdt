package com.dotop.smartwater.project.module.api.wechat.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dotop.smartwater.dependence.cache.api.AbstractValueCache;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.DateUtils;
import com.dotop.smartwater.project.module.api.revenue.INumRuleSetFactory;
import com.dotop.smartwater.project.module.api.wechat.IWechatCommonFactory;
import com.dotop.smartwater.project.module.api.wechat.IWechatRechargeFactory;
import com.dotop.smartwater.project.module.client.third.http.wechat.CaptchaUtil;
import com.dotop.smartwater.project.module.client.third.http.wechat.WechatUtil;
import com.dotop.smartwater.project.module.core.auth.bo.SendMsgBo;
import com.dotop.smartwater.project.module.core.auth.bo.WechatMessageParamBo;
import com.dotop.smartwater.project.module.core.auth.enums.SmsEnum;
import com.dotop.smartwater.project.module.core.auth.wechat.WechatAuthClient;
import com.dotop.smartwater.project.module.core.auth.wechat.WechatUser;
import com.dotop.smartwater.project.module.core.third.bo.wechat.RechargeParamBo;
import com.dotop.smartwater.project.module.core.third.bo.wechat.WechatOrderBo;
import com.dotop.smartwater.project.module.core.third.enums.wechat.TradeState;
import com.dotop.smartwater.project.module.core.third.enums.wechat.WechatOrderType;
import com.dotop.smartwater.project.module.core.third.utils.wechat.MessageManager;
import com.dotop.smartwater.project.module.core.third.vo.wechat.WechatOrderVo;
import com.dotop.smartwater.project.module.core.third.vo.wechat.WeixinOrderQueryVo;
import com.dotop.smartwater.project.module.core.water.bo.OwnerBo;
import com.dotop.smartwater.project.module.core.water.bo.WechatPublicSettingBo;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.module.core.water.constants.WaterConstants;
import com.dotop.smartwater.project.module.core.water.form.customize.MakeNumRequest;
import com.dotop.smartwater.project.module.core.water.form.customize.RechargeForm;
import com.dotop.smartwater.project.module.core.water.utils.IpAdrressUtil;
import com.dotop.smartwater.project.module.core.water.vo.OwnerVo;
import com.dotop.smartwater.project.module.core.water.vo.PayDetailVo;
import com.dotop.smartwater.project.module.core.water.vo.customize.MakeNumVo;
import com.dotop.smartwater.project.module.core.water.vo.customize.WechatPublicSettingVo;
import com.dotop.smartwater.project.module.service.revenue.IOwnerService;
import com.dotop.smartwater.project.module.service.tool.ISmsToolService;
import com.dotop.smartwater.project.module.service.tool.IWechatPublicSettingService;
import com.dotop.smartwater.project.module.service.wechat.IWechatRechargeService;
import com.dotop.smartwater.project.module.service.wechat.IWechatUserService;

/**
 * 迁移改造
 * 

 * @date 2019年3月22日
 */
@Component
public class WechatRechargeImpl implements IWechatRechargeFactory {

	private static final Logger logger = LogManager.getLogger(WechatRechargeImpl.class);

	@Autowired
	private IOwnerService iOwnerService;

	@Autowired
	private IWechatRechargeService iWechatRechargeService;
	@Autowired
	private IWechatUserService iWechatUserService;

	@Autowired
	private IWechatPublicSettingService iWechatPublicSettingService;
	@Autowired
	private ISmsToolService iSmsToolService;

	@Autowired
	private INumRuleSetFactory iNumRuleSetFactory;

	@Autowired
	private IWechatCommonFactory iWechatCommonFactory;
	
	@Autowired
	protected AbstractValueCache<String> avc;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public Map<String, Object> recharge(HttpServletRequest request, RechargeForm rechargeForm) {
		WechatUser wechatUser = WechatAuthClient.get();
		String ownerId = wechatUser.getOwnerid();
		String enterpriseid = wechatUser.getEnterpriseid();
		String openid = wechatUser.getOpenid();
		OwnerBo ownerBo = new OwnerBo();
		ownerBo.setOwnerid(ownerId);
		// 调用存在的方法
		OwnerVo currentOwner = iOwnerService.findByOwnerId(ownerBo);
		if (currentOwner == null) {
			throw new FrameworkRuntimeException(ResultCode.WECHAT_NOT_OWNER_ERROR, "未有绑定业主");
			// 校验当前登录业主是否过户或者销户
		} else if (currentOwner.getStatus().intValue() == 0) {
			throw new FrameworkRuntimeException(ResultCode.WECHAT_OWNER_NOT_EXIST_ERROR, "业主已经过户或者销户");
		}

		WechatPublicSettingVo weixinConfig = iWechatPublicSettingService.getByenterpriseId(enterpriseid);

		List<OwnerVo> returnOwnList = iWechatUserService.getOwnerList(openid, enterpriseid);

		for (int i = 0; i < returnOwnList.size(); i++) {
			if (returnOwnList.get(i).getOwnerid().equals(rechargeForm.getOwnerid())) {
				break;
			}
			if (i == returnOwnList.size() - 1) {
				throw new FrameworkRuntimeException(ResultCode.WECHATRECHARGEERROR, "充值业主未关联当前微信号");
			}
		}

		String ip = IpAdrressUtil.getIpAdrress(request);
		return weixinCharge(weixinConfig, openid, rechargeForm, ip, enterpriseid);
	}

	public Map<String, Object> weixinCharge(WechatPublicSettingVo weixinConfig, String openid,
			RechargeForm rechargeForm, String ip, String enterpriseid) {
		Double amount = rechargeForm.getAmount();
		// 业主充值的商户订单号 编号规则统一接口获取

		MakeNumRequest makeNumRequest = new MakeNumRequest();
		makeNumRequest.setEnterpriseid(enterpriseid);
		makeNumRequest.setRuleid(WaterConstants.NUM_RULE_CHARGE);
		makeNumRequest.setCount(1);
		MakeNumVo makeNumVo = iNumRuleSetFactory.wechatMakeNo(makeNumRequest);
		String outTradeNO = makeNumVo.getNumbers().get(0);

		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("amount", String.valueOf(amount));
		WechatOrderVo wechatOrder = null;
		RechargeParamBo rechargeParamBo = new RechargeParamBo();
		BeanUtils.copyProperties(rechargeForm, rechargeParamBo);
		WechatPublicSettingBo wechatPublicSettingbo = new WechatPublicSettingBo();
		BeanUtils.copyProperties(weixinConfig, wechatPublicSettingbo);
		wechatOrder = WechatUtil.unifiedorderRecharge(rechargeParamBo, wechatPublicSettingbo, openid, ip, outTradeNO);
		logger.debug("===================================================");
		logger.debug(wechatOrder);
		if (MessageManager.SUCCESS.equalsIgnoreCase(wechatOrder.getWechatresultcode())) {
			logger.debug("[Weixin]微信支付预下单成功: smOid[{}] --> prepayId[{}]", outTradeNO, wechatOrder.getPrepayid());
			resultMap.putAll(
					unifiedorderSign(wechatOrder.getPrepayid(), weixinConfig.getAppid(), weixinConfig.getPaysecret()));
			resultMap.put("toPay", 2);
			resultMap.put("wechatmchno", wechatOrder.getWechatmchno());
		} else if (MessageManager.FAIL.equalsIgnoreCase(wechatOrder.getWechatresultcode())) {
			resultMap.put("toPay", -1);
			if ("OUT_TRADE_NO_USED".equalsIgnoreCase(wechatOrder.getWechatretruncode())) {
				// 订单号重复，重新下单
				// 生成新的订单号
				logger.debug("[Weixin]微信支付预下单失败 --> 订单号重复[{}]", outTradeNO);
			} else {
				logger.debug("[Weixin]微信支付预下单失败: smOid[{}] --> tradeSN[{}] ERROR[{}]", outTradeNO,
						wechatOrder.getWechatmchno(), wechatOrder.getWechatretruncode());
				wechatOrder.setWechatorderstatus(TradeState.PAYERROR.intValue());
			}
		}
		logger.debug("===================================================");
		// 默认正常
		wechatOrder.setWechatorderstate(0);
		// 默认充值
		wechatOrder.setPaytype(WechatOrderType.recharge.intValue());
		// 无论统一下单成功与否，都要存储统一下单流水
		WechatOrderBo wechatOrderBo = new WechatOrderBo();
		BeanUtils.copyProperties(wechatOrder, wechatOrderBo);
		iWechatRechargeService.saveChargeRecord(wechatOrderBo);
		return resultMap;
	}

	private Map<String, String> unifiedorderSign(String prepayId, String appId, String paySecret) {
		Map<String, String> signMap = new HashMap<>();
		signMap.put("appId", appId);
		signMap.put("timeStamp", String.valueOf(System.currentTimeMillis() / 1000));
		signMap.put("package", "prepay_id=" + prepayId);
		signMap.put("nonceStr", CaptchaUtil.getAlphabetCaptcha(30));
		signMap.put("signType", "MD5");
		String sign = WechatUtil.sign(signMap, paySecret);
		signMap.put("paySign", sign);
		return signMap;
	}

	@Override
	public void query(HttpServletRequest request, RechargeForm rechargeForm) {
		WechatUser wechatUser = WechatAuthClient.get();
		String ownerId = wechatUser.getOwnerid();
		String enterpriseid = wechatUser.getEnterpriseid();
		String openid = wechatUser.getOpenid();
		OwnerBo ownerBo = new OwnerBo();
		ownerBo.setOwnerid(ownerId);
		// 调用存在的方法
		OwnerVo currentOwner = iOwnerService.findByOwnerId(ownerBo);
		if (currentOwner == null) {
			throw new FrameworkRuntimeException(ResultCode.WECHAT_NOT_OWNER_ERROR, "未有绑定业主");
			// 校验当前登录业主是否过户或者销户
		} else if (currentOwner.getStatus().intValue() == 0) {
			throw new FrameworkRuntimeException(ResultCode.WECHAT_OWNER_NOT_EXIST_ERROR, "业主已经过户或者销户");
		}

		WechatPublicSettingVo weixinConfig = iWechatPublicSettingService.getByenterpriseId(enterpriseid);

		RechargeParamBo rechargeParamBo = new RechargeParamBo();
		BeanUtils.copyProperties(rechargeForm, rechargeParamBo);
		WechatOrderVo wechatOrder = iWechatRechargeService.rechargeQuery(rechargeParamBo);

		if (wechatOrder == null) {
			throw new FrameworkRuntimeException(ResultCode.WECHATORDERERROR, "未找到充值的订单");
		} else if (wechatOrder.getWechatorderstatus().intValue() == TradeState.SUCCESS.intValue()) {
			sendMsg(wechatOrder, ownerId, enterpriseid, currentOwner, openid);
			return;
		} else if (wechatOrder.getWechatorderstatus().intValue() == TradeState.REFUND.intValue()
				|| wechatOrder.getWechatorderstatus().intValue() == TradeState.CLOSED.intValue()
				|| wechatOrder.getWechatorderstatus().intValue() == TradeState.REVOKED.intValue()
				|| wechatOrder.getWechatorderstatus().intValue() == TradeState.PAYERROR.intValue()) {
			throw new FrameworkRuntimeException(ResultCode.WECHATORDERERROR, "充值的订单异常");
		}

		WechatPublicSettingBo wechatPublicSettingBo = new WechatPublicSettingBo();
		BeanUtils.copyProperties(weixinConfig, wechatPublicSettingBo);
		WeixinOrderQueryVo orderQuery = WechatUtil.orderQuery(rechargeForm.getWechatmchno(), wechatPublicSettingBo);
		// 测试 设置交易成功
		// orderQuery.setTradeState(TradeState.SUCCESS);
		if (orderQuery == null) {
			throw new FrameworkRuntimeException(ResultCode.WECHATORDERERROR, "充值的查询异常");
		} else if (orderQuery.getTradeState().intValue() != TradeState.SUCCESS.intValue()) {
			if (!rechargeForm.getPayflag()) { // 即 !false
				wechatOrder.setWechatmchno(rechargeForm.getWechatmchno());
				wechatOrder.setUpdatetime(new Date());
				wechatOrder.setWechatorderstatus(TradeState.PAYERROR.intValue());
				wechatOrder.setWechaterrormsg(rechargeForm.getErrormsg());

				WechatOrderBo wechatOrderBo = new WechatOrderBo();
				BeanUtils.copyProperties(wechatOrder, wechatOrderBo);
				iWechatRechargeService.updateRechargeRecord(wechatOrderBo);
			} else {
				throw new FrameworkRuntimeException(ResultCode.WECHATORDERERROR, "充值的订单未支付成功");
			}
		} else {

			wechatOrder.setWechatorderstatus(orderQuery.getTradeState().intValue());
			wechatOrder.setWechatmchno(rechargeForm.getWechatmchno());
			wechatOrder.setWechatpaytime(new Date());
			wechatOrder.setUpdatetime(new Date());
			wechatOrder.setWechatorderstatus(TradeState.SUCCESS.intValue());
			wechatOrder.setWechaterrormsg(rechargeForm.getErrormsg());
			WechatOrderBo wechatOrderBo = new WechatOrderBo();
			BeanUtils.copyProperties(wechatOrder, wechatOrderBo);
			if (iWechatCommonFactory.updateOrderRecord(wechatOrderBo, ownerId) == 1) {
				sendMsg(wechatOrder, ownerId, enterpriseid, currentOwner, openid);
			} else {
				throw new FrameworkRuntimeException(ResultCode.Fail, "充值失败");
			}
			if (!rechargeForm.getPayflag()) {
				throw new FrameworkRuntimeException(ResultCode.ParamIllegal, "微信订单查询支付成功，请求状态错误");
			}
		}
	}

	@Override
	public Pagination<PayDetailVo> getRechargeLit(HttpServletRequest request, RechargeForm rechargeForm) {
		WechatUser wechatUser = WechatAuthClient.get();
		String ownerId = wechatUser.getOwnerid();
		String enterpriseid = wechatUser.getEnterpriseid();
		String openid = wechatUser.getOpenid();
		OwnerBo ownerBo = new OwnerBo();
		ownerBo.setOwnerid(ownerId);
		// 调用存在的方法
		OwnerVo currentOwner = iOwnerService.findByOwnerId(ownerBo);
		if (currentOwner == null) {
			throw new FrameworkRuntimeException(ResultCode.WECHAT_NOT_OWNER_ERROR, "未有绑定业主");
			// 校验当前登录业主是否过户或者销户
		} else if (currentOwner.getStatus().intValue() == 0) {
			throw new FrameworkRuntimeException(ResultCode.WECHAT_OWNER_NOT_EXIST_ERROR, "业主已经过户或者销户");
		}

		List<OwnerVo> returnOwnList = iWechatUserService.getOwnerList(openid, enterpriseid);

		// 判断传入ownerid是否在绑定业主内
		for (int i = 0; i < returnOwnList.size(); i++) {
			if (ownerId.equals(returnOwnList.get(i).getOwnerid())) {
				break;
			}
			if (i == (returnOwnList.size() - 1)) {
				throw new FrameworkRuntimeException(ResultCode.ParamIllegal, "onwerid不在关联的业主内");
			}
		}

		RechargeParamBo rechargeParamBo = new RechargeParamBo();
		BeanUtils.copyProperties(rechargeForm, rechargeParamBo);
		rechargeParamBo.setOwnerid(ownerId);
		return iWechatRechargeService.getRechargeLit(rechargeParamBo);

	}

	// 发送短信和微信
	private void sendMsg(WechatOrderVo wechatOrder, String ownerid, String enterpriseid, OwnerVo currentOwner,
			String openid) {

		WechatMessageParamBo wechatMessageParam = new WechatMessageParamBo();
		wechatMessageParam.setMessageState(SmsEnum.recharge.intValue());
		wechatMessageParam.setOwnerid(ownerid);
		wechatMessageParam.setSendType(1);
		wechatMessageParam.setEnterpriseid(enterpriseid);
		wechatMessageParam.setUserName(currentOwner.getUsername());
		wechatMessageParam.setRechargemoney(wechatOrder.getWechatamount());
		wechatMessageParam.setRechargetime(DateUtils.formatDatetime(wechatOrder.getWechatpaytime()));

		Map<String, String> params = new HashMap<>();
		params.put("name", currentOwner.getUsername());
		params.put("phone", currentOwner.getUserphone());
		params.put("money", String.valueOf(wechatOrder.getWechatamount()));
		params.put("time", DateUtils.formatDatetime(wechatOrder.getWechatpaytime()));
		iSmsToolService.sendSMS(enterpriseid, SmsEnum.recharge.intValue(), params, null);
		SendMsgBo sendMsgBo = new SendMsgBo();
		sendMsgBo.setWechatMessageParam(wechatMessageParam);
		sendMsgBo.setOpenId(openid);
		iSmsToolService.sendWeChatMsg(sendMsgBo);
	}

}
