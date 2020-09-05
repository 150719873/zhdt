package com.dotop.smartwater.project.module.api.revenue.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

import com.dotop.smartwater.project.module.api.revenue.IPaymentFactory;
import com.dotop.smartwater.project.module.core.water.bo.PaymentTradeOrderBo;
import com.dotop.smartwater.project.module.core.water.constants.PaymentConstants;
import com.dotop.smartwater.project.module.core.water.form.PaymentTradeOrderForm;
import com.dotop.smartwater.project.module.core.water.vo.PaymentTradeOrderVo;
import com.dotop.smartwater.project.module.service.revenue.IPaymentTradeOrderService;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.DateUtils;
import com.dotop.smartwater.project.module.api.revenue.INumRuleSetFactory;
import com.dotop.smartwater.project.module.api.revenue.ITradeOrderFactory;
import com.dotop.smartwater.project.module.client.third.http.wechat.WechatUtil;
import com.dotop.smartwater.project.module.core.auth.local.AuthCasClient;
import com.dotop.smartwater.project.module.core.auth.vo.UserVo;
import com.dotop.smartwater.project.module.core.third.utils.wechat.MessageManager;
import com.dotop.smartwater.project.module.core.water.bo.TradeDetailBo;
import com.dotop.smartwater.project.module.core.water.bo.TradeOrderBo;
import com.dotop.smartwater.project.module.core.water.config.Config;
import com.dotop.smartwater.project.module.core.water.constants.PayStatus;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.module.core.water.constants.WaterConstants;
import com.dotop.smartwater.project.module.core.water.form.TradeDetailForm;
import com.dotop.smartwater.project.module.core.water.form.TradeOrderForm;
import com.dotop.smartwater.project.module.core.water.form.customize.MakeNumRequest;
import com.dotop.smartwater.project.module.core.water.vo.TradeDetailVo;
import com.dotop.smartwater.project.module.core.water.vo.TradeOrderVo;
import com.dotop.smartwater.project.module.core.water.vo.customize.MakeNumVo;
import com.dotop.smartwater.project.module.core.water.vo.customize.WechatPublicSettingVo;
import com.dotop.smartwater.project.module.service.revenue.ITradeDetailService;
import com.dotop.smartwater.project.module.service.revenue.ITradeOrderService;
import com.dotop.smartwater.project.module.service.tool.IWechatPublicSettingService;

/**
 * 缴费
 * 

 * @date 2019年5月16日
 *
 */
@Component
public class TradeOrderFactoryImpl implements ITradeOrderFactory {

	private final static Logger logger = LogManager.getLogger(TradeOrderFactoryImpl.class);

	@Autowired
	private ITradeOrderService service;

	@Autowired
	private IPaymentTradeOrderService iPaymentTradeOrderService;

	@Autowired
	private INumRuleSetFactory iNumRuleSetFactory;

	@Autowired
	private ITradeDetailService dtService;

	@Autowired
	private IPaymentFactory iPaymentFactory;

	@Autowired
	private IWechatPublicSettingService iWechatPublicSettingService;

	@Override
	public Pagination<TradeOrderVo> page(TradeOrderForm form) throws FrameworkRuntimeException {
		UserVo user = AuthCasClient.getUser();
		TradeOrderBo bo = new TradeOrderBo();
		BeanUtils.copyProperties(form, bo);
		bo.setEnterpriseid(user.getEnterpriseid());
		Pagination<TradeOrderVo> pagination = service.page(bo);
		return pagination;
	}

	@Override
	public TradeOrderVo get(TradeOrderForm form) {
		UserVo user = AuthCasClient.getUser();
		TradeOrderBo bo = new TradeOrderBo();
		BeanUtils.copyProperties(form, bo);
		bo.setEnterpriseid(user.getEnterpriseid());
		return service.get(bo);
	}

	@Override
	public TradeDetailVo getDetail(TradeDetailForm form) {
		UserVo user = AuthCasClient.getUser();
		TradeDetailBo bo = new TradeDetailBo();
		BeanUtils.copyProperties(form, bo);
		bo.setEnterpriseid(user.getEnterpriseid());
		return dtService.getDetail(bo);
	}

	@Override
	public int save(TradeOrderForm form) throws FrameworkRuntimeException {
		UserVo user = AuthCasClient.getUser();
		TradeOrderBo bo = new TradeOrderBo();
		BeanUtils.copyProperties(form, bo);
		bo.setEnterpriseid(user.getEnterpriseid());
		bo.setUserBy(user.getName());
		bo.setCurr(new Date());
		bo.setNumber(form.getNumber());

		TradeOrderVo order = service.get(bo);
		if (order != null && order.getId() != null) { // 如果已存在
			if (!order.getStatus().equals(PayStatus.PAYSUCC)) { // 如果未支付
				bo.setEnterpriseid(user.getEnterpriseid());
				bo.setStatus(PayStatus.NOPAY);

				PaymentTradeOrderBo paymentTradeOrderBo = new PaymentTradeOrderBo();
				paymentTradeOrderBo.setEnterpriseid(user.getEnterpriseid());
				paymentTradeOrderBo.setTradeNumber(form.getNumber());
				PaymentTradeOrderVo paymentTradeOrderVo = iPaymentTradeOrderService.findByEidAndTradeNum(paymentTradeOrderBo);
				PaymentTradeOrderForm paymentTradeOrderForm;
				if(paymentTradeOrderVo != null){
					paymentTradeOrderBo = new PaymentTradeOrderBo();
					BeanUtils.copyProperties(paymentTradeOrderVo,paymentTradeOrderBo);
					iPaymentTradeOrderService.del(paymentTradeOrderBo);
				}
				//在新增
				//插入营业厅数据
				paymentTradeOrderForm = new PaymentTradeOrderForm();
				paymentTradeOrderForm.setBusinessId(PaymentConstants.BUSSINESS_TYPE_REPAIR);
				paymentTradeOrderForm.setTradeNumber(bo.getNumber());
				paymentTradeOrderForm.setUserName(bo.getUserName());
				paymentTradeOrderForm.setUserno(null);
				paymentTradeOrderForm.setUserAddr(null);
				paymentTradeOrderForm.setUserPhone(bo.getUserPhone());
				paymentTradeOrderForm.setUserid(bo.getUserId());
				paymentTradeOrderForm.setCreateDate(new Date());
				paymentTradeOrderForm.setAmount(bo.getAmount());
				paymentTradeOrderForm.setTradeName(bo.getTradeName());
				paymentTradeOrderForm.setEnterpriseid(bo.getEnterpriseid());
				paymentTradeOrderForm.setStatus(PaymentConstants.PATMENT_STATUS_NOPAY);
				iPaymentFactory.add(paymentTradeOrderForm);

				return service.update(bo);
			} else {
				throw new FrameworkRuntimeException(String.valueOf(ResultCode.ORDER_PAY_ERROR),
						ResultCode.getMessage(ResultCode.ORDER_PAY_ERROR));
			}
		} else {// 不存在则新增
			// 生成交易号
			MakeNumRequest make = new MakeNumRequest();
			make.setRuleid(9);
			make.setCount(1);
			MakeNumVo vo = iNumRuleSetFactory.makeNo(make);
			if (vo != null && vo.getNumbers() != null && vo.getNumbers().size() > 0) {
				bo.setTradeNumber(vo.getNumbers().get(0));
			} else {
				bo.setTradeNumber(String.valueOf(Config.Generator.nextId()));
			}

			bo.setStatus(PayStatus.NOPAY);

			//插入营业厅数据
			PaymentTradeOrderForm paymentTradeOrderForm = new PaymentTradeOrderForm();
			paymentTradeOrderForm.setBusinessId(PaymentConstants.BUSSINESS_TYPE_REPAIR);
			paymentTradeOrderForm.setTradeNumber(bo.getNumber());
			paymentTradeOrderForm.setUserName(bo.getUserName());
			paymentTradeOrderForm.setUserno(null);
			paymentTradeOrderForm.setUserAddr(null);
			paymentTradeOrderForm.setUserPhone(bo.getUserPhone());
			paymentTradeOrderForm.setUserid(bo.getUserId());
			paymentTradeOrderForm.setCreateDate(new Date());
			paymentTradeOrderForm.setAmount(bo.getAmount());
			paymentTradeOrderForm.setTradeName(bo.getTradeName());
			paymentTradeOrderForm.setEnterpriseid(bo.getEnterpriseid());
			paymentTradeOrderForm.setStatus(PaymentConstants.PATMENT_STATUS_NOPAY);
			iPaymentFactory.add(paymentTradeOrderForm);

			return service.save(bo);
		}
	}

	@Override
	public int update(TradeOrderForm form) throws FrameworkRuntimeException {
		UserVo user = AuthCasClient.getUser();
		TradeOrderBo bo = new TradeOrderBo();
		BeanUtils.copyProperties(form, bo);
		bo.setEnterpriseid(user.getEnterpriseid());
		bo.setUserBy(user.getName());
		bo.setCurr(new Date());

		TradeOrderVo vo = service.get(bo);
		if (!vo.getStatus().equals(PayStatus.PAYSUCC)) {
			bo.setEnterpriseid(user.getEnterpriseid());
			bo.setStatus(PayStatus.NOPAY);
			return service.update(bo);
		} else {
			throw new FrameworkRuntimeException(String.valueOf(ResultCode.ORDER_PAY_ERROR),
					ResultCode.getMessage(ResultCode.ORDER_PAY_ERROR));
		}
	}

	@Override
	public boolean updatePayStatus(TradeOrderForm form) {
		boolean temp = false;
		UserVo userVo = AuthCasClient.getUser();
		form.setEnterpriseid(userVo.getEnterpriseid());

		TradeOrderBo orderBo = new TradeOrderBo();
		TradeDetailBo tradeDetailBo = new TradeDetailBo();

		BeanUtils.copyProperties(form, orderBo);
		BeanUtils.copyProperties(form, tradeDetailBo);

		try {
			TradeOrderVo tradeOrderVo = service.get(orderBo);
			TradeDetailVo detailVo = dtService.getDetail(tradeDetailBo);

			BeanUtils.copyProperties(tradeOrderVo, orderBo);
			BeanUtils.copyProperties(detailVo, tradeDetailBo);

			WechatPublicSettingVo weixinConfig = iWechatPublicSettingService
					.getByenterpriseId(userVo.getEnterpriseid());
			Map<String, String> result = (new WechatUtil()).getPayStatus(weixinConfig, detailVo);
			// 如果根据交易流水号获取账单状态为成功时，则根据订单、详情表，如果失败只需要根据详情表即可
			if (MessageManager.SUCCESS.equalsIgnoreCase(result.get("status"))) {
				orderBo.setStatus(WaterConstants.ORDER_PAYSTATUS_PAID + "");
				service.updatePayStatus(orderBo);

				tradeDetailBo.setStatus(WaterConstants.ORDER_PAYSTATUS_PAID + "");
				tradeDetailBo.setExplan(ResultCode.SUCCESS);
			} else {
				tradeDetailBo.setStatus(WaterConstants.ORDER_PAYSTATUS_PAYFAIL + "");
				tradeDetailBo.setExplan(result.get("msg"));
			}
			dtService.edit(tradeDetailBo);
			temp = true;
		} catch (Exception e) {
			throw new FrameworkRuntimeException(ResultCode.Fail, "更新交易状态时异常");
		}
		return temp;
	}

	@Override
	public TradeDetailBo tradeOrderPay(TradeDetailForm form) throws FrameworkRuntimeException {
		UserVo userVo = AuthCasClient.getUser();
		TradeOrderBo orderBo = new TradeOrderBo();
		BeanUtils.copyProperties(form, orderBo);

		orderBo.setEnterpriseid(userVo.getEnterpriseid());
		orderBo.setUserBy(userVo.getName());
		orderBo.setCurr(new Date());
		orderBo.setMode(form.getMode());

		TradeOrderVo tradeOrderVo = service.get(orderBo);
		if (tradeOrderVo == null) {
			throw new FrameworkRuntimeException(ResultCode.Fail, "没有找到要支付的订单");
		}

		int value = new BigDecimal(form.getAmount()).divide(new BigDecimal(tradeOrderVo.getAmount())).intValue();
		if (value < 0) {
			throw new FrameworkRuntimeException(ResultCode.Fail, "账单金额异常");
		}

		TradeDetailBo tradeDetailBo = new TradeDetailBo();
		tradeDetailBo.setNumber(tradeOrderVo.getNumber());
		tradeDetailBo.setEnterpriseid(userVo.getEnterpriseid());
		TradeDetailVo detailVo = dtService.getDetail(tradeDetailBo);

		if (detailVo != null) {
			BeanUtils.copyProperties(detailVo, tradeDetailBo);
		}

		tradeDetailBo.setTradeNumber(tradeOrderVo.getTradeNumber());
		tradeDetailBo.setIp(form.getIp());
		tradeDetailBo.setMode(form.getMode());
		tradeDetailBo.setAmount(tradeOrderVo.getAmount());
		tradeDetailBo.setNetReceipts(form.getNetReceipts());
		tradeDetailBo.setGiveChange(form.getGiveChange());
		tradeDetailBo.setThirdPartyNum(String.valueOf(Config.Generator.nextId()));

		if (StringUtils.isNotBlank(form.getPaycard())) {
			tradeDetailBo.setPaycard(form.getPaycard());
		}

		makeBo(tradeDetailBo);

		switch (Integer.parseInt(form.getMode())) {
		case WaterConstants.ORDER_PAYTYPE_MONEY: // 现金
			tradeDetailBo.setStatus(String.valueOf(WaterConstants.ORDER_PAYSTATUS_PAID));

			orderBo.setStatus(WaterConstants.ORDER_PAYSTATUS_PAID + "");
			orderBo.setPayTime(DateUtils.formatDatetime(new Date()));
			break;
		case WaterConstants.ORDER_PAYTYPE_PAYCARD:// 微信扫码支付

			WechatPublicSettingVo weixinConfig = iWechatPublicSettingService
					.getByenterpriseId(userVo.getEnterpriseid());

			Map<String, String> resultMap = (new WechatUtil()).payByCard(weixinConfig, tradeDetailBo);
			if (MessageManager.SUCCESS.equalsIgnoreCase(resultMap.get(MessageManager.RESP_RETURN_CODE))) {
				if (MessageManager.SUCCESS.equalsIgnoreCase(resultMap.get(MessageManager.RESP_RESULT_CODE))) {
					orderBo.setStatus(WaterConstants.ORDER_PAYSTATUS_PAID + "");
					tradeDetailBo.setStatus(WaterConstants.ORDER_PAYSTATUS_PAID + "");
					tradeDetailBo.setExplan(ResultCode.SUCCESS);
				} else {
					orderBo.setStatus(WaterConstants.ORDER_PAYSTATUS_NOTPAID + "");
					tradeDetailBo.setStatus(WaterConstants.ORDER_PAYSTATUS_PAYFAIL + "");
					tradeDetailBo.setExplan(resultMap.get(MessageManager.RESP_ERR_CODE_DES));
					logger.error("微信扫码支付失败:", resultMap.get(MessageManager.RESP_ERR_CODE_DES));
				}
			} else {
				if (resultMap.get(MessageManager.RESP_ERR_CODE_DES) != null) {
					orderBo.setStatus(WaterConstants.ORDER_PAYSTATUS_NOTPAID + "");
					tradeDetailBo.setStatus(WaterConstants.ORDER_PAYSTATUS_PAYFAIL + "");
					tradeDetailBo.setExplan(resultMap.get(MessageManager.RESP_ERR_CODE_DES));
					logger.error("微信扫码支付失败:", resultMap.get(MessageManager.RESP_ERR_CODE_DES));
				}
			}
			break;
		default:
			throw new FrameworkRuntimeException(ResultCode.Fail, "未知的支付方式");
		}

		// 修改费用管理中支付后的状态
		if (StringUtils.isNotEmpty(tradeDetailBo.getId())) {
			dtService.edit(tradeDetailBo);
		} else {
			dtService.add(tradeDetailBo);
		}
		service.updatePayStatus(orderBo);
		return tradeDetailBo;
	}

	private void makeBo(TradeDetailBo tradeDetailBo) {
		UserVo userVo = AuthCasClient.getUser();
		tradeDetailBo.setEnterpriseid(userVo.getEnterpriseid());
		tradeDetailBo.setOperatorId(userVo.getUserid());
		tradeDetailBo.setOperatorName(userVo.getName());
		tradeDetailBo.setUserBy(userVo.getName());
		tradeDetailBo.setCurr(new Date());
		tradeDetailBo.setCreateTime(DateUtils.formatDatetime(new Date()));
	}

}
