package com.dotop.smartwater.project.module.api.wechat.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import com.dotop.smartwater.project.module.core.pay.constants.PayConstants;
import com.dotop.smartwater.project.module.service.revenue.*;
import com.dotop.smartwater.project.module.core.water.bo.*;
import com.dotop.smartwater.project.module.core.water.constants.PaymentConstants;
import com.dotop.smartwater.project.module.core.water.vo.*;
import com.dotop.smartwater.project.module.service.revenue.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.alibaba.fastjson.JSONObject;
import com.dotop.smartwater.dependence.core.utils.BeanUtils;
import com.dotop.smartwater.dependence.core.utils.DateUtils;
import com.dotop.smartwater.dependence.core.utils.JSONUtils;
import com.dotop.smartwater.project.module.api.wechat.IWechatCommonFactory;
import com.dotop.smartwater.project.module.api.wechat.IWechatReturnUrlFactory;
import com.dotop.smartwater.project.module.core.auth.bo.SendMsgBo;
import com.dotop.smartwater.project.module.core.auth.bo.WechatMessageParamBo;
import com.dotop.smartwater.project.module.core.auth.enums.SmsEnum;
import com.dotop.smartwater.project.module.core.third.bo.wechat.RechargeParamBo;
import com.dotop.smartwater.project.module.core.third.bo.wechat.WechatNotifyMsgBo;
import com.dotop.smartwater.project.module.core.third.bo.wechat.WechatOrderBo;
import com.dotop.smartwater.project.module.core.third.enums.wechat.TradeState;
import com.dotop.smartwater.project.module.core.third.enums.wechat.WechatOrderState;
import com.dotop.smartwater.project.module.core.third.enums.wechat.WechatOrderType;
import com.dotop.smartwater.project.module.core.third.utils.wechat.MessageManager;
import com.dotop.smartwater.project.module.core.third.vo.wechat.WechatNotifyMsgVo;
import com.dotop.smartwater.project.module.core.third.vo.wechat.WechatOrderVo;
import com.dotop.smartwater.project.module.core.water.constants.WaterConstants;
import com.dotop.smartwater.project.module.core.water.form.TradePayForm;
import com.dotop.smartwater.project.module.core.water.form.customize.OrderPayParamForm;
import com.dotop.smartwater.project.module.service.tool.ISmsToolService;
import com.dotop.smartwater.project.module.service.wechat.IWechatPayService;
import com.dotop.smartwater.project.module.service.wechat.IWechatRechargeService;
import com.dotop.smartwater.project.module.service.wechat.IWechatService;

/**
 * 迁移改造
 * 

 * @date 2019年3月22日
 */
@Component
public class WechatReturnUrlFactoryImpl implements IWechatReturnUrlFactory {

	private static final Logger logger = LogManager.getLogger(WechatReturnUrlFactoryImpl.class);

	@Autowired
	private IWechatPayService iWechatPayService;

	@Autowired
	private IOrderService iOrderService;

	@Autowired
	private IWechatRechargeService iWechatRechargeService;

	@Autowired
	private IWechatService iWechatService;

	@Autowired
	private ISmsToolService iSmsToolService;

	@Autowired
	private IOwnerService iOwnerService;

	@Autowired
	private IWechatCommonFactory iWechatCommonFactory;

	@Autowired
	private IPaymentTradeOrderService service;

	@Autowired
	private IPaymentTradeOrderExtService extService;

	@Autowired
	private IPaymentTradeResultService resultService;

	@Autowired
	private IPaymentTradeProService proService;

	@Override
	public void get(HttpServletRequest request) {
		logger.debug(
				"*****************************" + request.getRequestURI() + "微信支付结果回调开始*****************************");
		int type = -1;
		try {
			InputStream inputStream = request.getInputStream();
			Map<String, String> xmlMap = parseXml(inputStream);
			logger.debug("[Weixin]微信支付回调 --> {}", xmlMap);
			WechatNotifyMsgVo result = new WechatNotifyMsgVo();
			String returnCode = xmlMap.get(MessageManager.RESP_RETURN_CODE);
			if (MessageManager.SUCCESS.equalsIgnoreCase(returnCode)) {

				String resultCode = xmlMap.get(MessageManager.RESP_RESULT_CODE);
				result.setTransactionId(xmlMap.get("transaction_id"));
				result.setNonceStr(xmlMap.get("nonce_str"));
				result.setBankType(xmlMap.get("bank_type"));
				result.setOpenid(xmlMap.get("openid"));
				result.setSign(xmlMap.get("sign"));
				result.setFeeType(xmlMap.get("fee_type"));
				result.setMchId(xmlMap.get("mch_id"));
				result.setCashFee(xmlMap.get("cash_fee"));
				result.setOutTradeNo(xmlMap.get("out_trade_no"));
				result.setAppid(xmlMap.get("appid"));
				result.setTotalFee(xmlMap.get("total_fee") == null ? 0 : Integer.parseInt(xmlMap.get("total_fee")));
				result.setTradeType(xmlMap.get("trade_type"));
				result.setResultCode(xmlMap.get("result_code"));
				result.setAttach(xmlMap.get("attach"));
				result.setTimeEnd(xmlMap.get("time_end"));
				result.setIsSubscribe(xmlMap.get("is_subscribe"));
				result.setReturnCode(xmlMap.get("return_code"));
				result.setCreatetime(new Date());
				result.setNotifyMsg(JSONUtils.toJSONString(xmlMap));

				if (MessageManager.SUCCESS.equalsIgnoreCase(resultCode)) {
					logger.debug("[Weixin]微信支付回调 --> 支付成功[resultCode[{}]]", resultCode);
				} else {
					String errCode = xmlMap.get(MessageManager.RESP_ERR_CODE);
					String errCodeDes = xmlMap.get(MessageManager.RESP_ERR_CODE_DES);
					result.setErrCode(errCode);
					result.setErrCodeDes(errCodeDes);
					logger.debug("[Weixin]微信支付回调 --> 支付失败[errCode[{}] errCodeDes[{}]]", errCode, errCodeDes);

				}
				// 新增充值记录
				WechatNotifyMsgBo wechatNotifyMsgBo = BeanUtils.copyProperties(result, WechatNotifyMsgBo.class);
				if (iWechatService.insertRecord(wechatNotifyMsgBo) == 1) {
					logger.debug("新增mchid：" + result.getMchId() + "成功");
				} else {
					logger.debug("新增mchid：" + result.getMchId() + "失败");
				}
				// 根据attach的第一个值判断是充值还是缴费：1缴费，2充值
				String attach = xmlMap.get("attach");
				logger.debug("attach---" + attach);
				String[] attachs = attach.split("\\|");

				if (attachs.length == 3) {
					logger.debug(result.getMchId() + "该笔微信支付是水费充值");
					type = Integer.parseInt(attachs[0]);

				} else if (attachs.length == 5) {
					logger.debug(result.getMchId() + "该笔微信支付是账单缴费");
					type = Integer.parseInt(attachs[0]);

				} else {
					logger.debug("错误的attach标识");
				}

			} else {
				String returnMsg = xmlMap.get(MessageManager.RESP_RETURN_MSG);
				logger.debug("[Warn][Weixin]微信支付回调 --> 通信失败[returnCode[{}] returnMsg[{}]]", returnCode, returnMsg);
				result.setReturnCode(returnCode);
				result.setReturnMsg(returnMsg);
			}

			// 如果支付回调 returnCode返回失败，直接返回
			if (!returnCode.equals(MessageManager.SUCCESS)) {
				logger.debug("支付回调返回失败，直接返回");
				return;
			}

			if (type == WechatOrderType.recharge.intValue()) {
				recharge(result);
			} else if (type == WechatOrderType.pay_cost.intValue()) {
				paybill(result);
			}
		} catch (Exception e) {
			logger.error("wechat_notify_url", e);
		} finally {
			logger.debug("*****************************" + request.getRequestURI()
					+ "微信支付结果回调结束*****************************");
		}

	}

	// 缴费相关处理
	private void paybill(WechatNotifyMsgVo wechatNotifyMsg) throws IOException {
		RechargeParamBo orderQueryParam = new RechargeParamBo();
		String tradeno = wechatNotifyMsg.getAttach().split("\\|")[4];
		String ownerid = String.valueOf(wechatNotifyMsg.getAttach().split("\\|")[1]);
		String orderid = String.valueOf(wechatNotifyMsg.getAttach().split("\\|")[2]);

		orderQueryParam.setTradeno(tradeno);
		orderQueryParam.setWechatmchno(wechatNotifyMsg.getOutTradeNo());
		logger.debug("缴费参数：orderQueryParam-tradeno[{}] orderQueryParam-Wechatmchno[{}]", tradeno,
				wechatNotifyMsg.getOutTradeNo());

		List<WechatOrderVo> orderWechatList = iWechatPayService.findPayingByOrderid(ownerid, orderid);
		WechatOrderVo orderWechat = null;
		// 缴费充值状态，true是缴费，false是充值，
		boolean payFlag = true;

		for (int i = 0; i < orderWechatList.size(); i++) {
			WechatOrderVo orderWechatTemp = orderWechatList.get(i);
			if (orderWechatTemp.getWechatmchno().equals(orderQueryParam.getWechatmchno())) {
				orderWechat = orderWechatTemp;
				if (orderWechatTemp.getWechatorderstatus().intValue() == TradeState.SUCCESS.intValue()) {
					if (i > 0) {
						payFlag = false;
					}
				}
				break;
			}
		}
		// 查询是否缴费成功
		OrderVo order = iOrderService.findById(orderid);
		if (order.getPaystatus() == 1) {
			logger.debug("订单已经缴费");
			payFlag = false;
		}

		if (orderWechat == null) {
			logger.debug("查询订单及交易信息是否存在，并更新");
			if (wechatNotifyMsg.getResultCode().equals(MessageManager.SUCCESS)) {
				TradePayForm tradePayForm = new TradePayForm();
				tradePayForm.setPayno(wechatNotifyMsg.getOutTradeNo());
				TradePayVo tradepay = iOrderService.findPayNo(tradePayForm);

				if (tradepay != null && !tradepay.getPaystatus().equals(WaterConstants.TRADE_PAYSTATUS_SUCCESS)) {
					iOrderService.updateTradePayStatus(wechatNotifyMsg.getOutTradeNo(),
							WaterConstants.TRADE_PAYSTATUS_SUCCESS + "", "支付成功");
				}

				OrderVo ordervo = iOrderService.findOrderByTradeNo(tradepay.getTradeno());
				if (!ordervo.getPaystatus().equals(WaterConstants.TRADE_PAYSTATUS_SUCCESS)) {
					ordervo.setPaystatus(WaterConstants.ORDER_PAYSTATUS_PAID);
					ordervo.setPaytime(DateUtils.formatDatetime(new Date()));
					OrderBo orderBo = new OrderBo();
					orderBo = BeanUtils.copy(ordervo, OrderBo.class);
					iOrderService.updateOrder(orderBo);

					/**
					 * 反向更新营业厅数据 （最合适做法）
					 * **/
					Date date = new Date();
					PaymentTradeOrderBo paymentTradeOrderBo = new PaymentTradeOrderBo();
					paymentTradeOrderBo.setTradeNumber(ordervo.getTradeno());
					paymentTradeOrderBo.setEnterpriseid(ordervo.getEnterpriseid());
					PaymentTradeOrderVo vo = service.findByEidAndTradeNum(paymentTradeOrderBo);
					if(vo != null){
						PaymentTradeProBo paymentTradeProBo = new PaymentTradeProBo();
						paymentTradeProBo.setTradeid(vo.getTradeid());
						paymentTradeProBo.setTransactionNumber(wechatNotifyMsg.getOutTradeNo());
						paymentTradeProBo.setAmount(vo.getAmount());
						paymentTradeProBo.setMode(PayConstants.PAYTYPE_QRCODE);
						paymentTradeProBo.setStatus(PaymentConstants.STATUS_PAYED);
						paymentTradeProBo.setEnterpriseid(vo.getEnterpriseid());
						paymentTradeProBo.setPayTime(date);
						paymentTradeProBo.setDescription("PDA二维码支付");
						proService.add(paymentTradeProBo);

						PaymentTradeResultBo paymentTradeResultBo = BeanUtils.copy(paymentTradeProBo, PaymentTradeResultBo.class);
						resultService.add(paymentTradeResultBo);

						paymentTradeOrderBo = new PaymentTradeOrderBo();
						paymentTradeOrderBo.setStatus(PaymentConstants.PATMENT_STATUS_PAYED);
						paymentTradeOrderBo.setPayTime(date);
						paymentTradeOrderBo.setTradeid(vo.getTradeid());
						service.edit(paymentTradeOrderBo);

						PaymentTradeOrderExtVo ext = extService.findByTradeid(vo.getTradeid());
						if(ext != null){
							PaymentTradeOrderExtBo paymentTradeOrderExtBo = BeanUtils.copy(ext, PaymentTradeOrderExtBo.class);
							paymentTradeOrderExtBo.setMode(PayConstants.PAYTYPE_QRCODE);
							paymentTradeOrderExtBo.setBalance("0.0");
							paymentTradeOrderExtBo.setCouponMoney("0.0");
							paymentTradeOrderExtBo.setChange("0.0");
							paymentTradeOrderExtBo.setActualamount(vo.getAmount());
							extService.edit(paymentTradeOrderExtBo);
						}
					}
				}

				logger.debug("订单状态已更新[" + tradepay.getTradeno() + "]");
			} else {
				logger.debug("支付失败");
			}
		} else {
			orderWechat.setTradeno(order.getTradeno());
			int databaseStatus = orderWechat.getWechatorderstatus().intValue();
			if (databaseStatus == TradeState.SUCCESS.intValue()
					&& wechatNotifyMsg.getResultCode().equals(MessageManager.SUCCESS)) {
				logger.debug("数据支付状态：wechatorderstatus[{}],微信返回结果：resultCode:[{}] 状态一致", databaseStatus,
						wechatNotifyMsg.getResultCode());
				sendMsg(ownerid, order);
				return;
			} else {
				logger.debug("数据支付状态：wechatorderstatus[{}],微信返回结果：resultCode:[{}] 状态 不相同", databaseStatus,
						wechatNotifyMsg.getResultCode());
				if (databaseStatus != TradeState.SUCCESS.intValue()) {
					// 订单支付不成功，微信返回成功，更新订单状态
					if (wechatNotifyMsg.getResultCode().equals(MessageManager.SUCCESS)) {
						logger.debug("订单支付不成功，微信返回成功，更新订单状态");
						// 同一个账单微信同时下单，微信先支付的是缴费，后面支付转为充值
						if (payFlag == false) {
							orderWechat.setWechatpaytime(new Date());
							orderWechat.setUpdatetime(new Date());
							orderWechat.setWechatorderstatus(TradeState.SUCCESS.intValue());
							orderWechat.setPaytype(WechatOrderType.recharge.intValue());
							orderWechat.setRemark("同一个账单微信同时下单，先支付的是缴费，后面支付转为充值");
							WechatOrderBo wechatOrderBo = BeanUtils.copyProperties(orderWechat, WechatOrderBo.class);
							if (iWechatCommonFactory.updateOrderRecord(wechatOrderBo, ownerid) == 1) {
								logger.debug("更新充值记录wechatOrder--->成功：" + orderWechat.getId());
							} else {
								logger.debug("更新充值记录wechatOrder--->失败：" + orderWechat.getId());
							}

						} else {
							// 更新相关缴费记录
							orderWechat.setWechatorderstatus(TradeState.SUCCESS.intValue());
							orderWechat.setWechatpaytime(new Date());
							orderWechat.setUpdatetime(new Date());
							orderWechat.setWechatorderstatus(TradeState.SUCCESS.intValue());
							orderWechat.setWechatretruncode(wechatNotifyMsg.getReturnCode());
							orderWechat.setWechatresultcode(wechatNotifyMsg.getResultCode());
							orderWechat.setWechatorderstate(WechatOrderState.state_normal.intValue());
							// orderWechat中不存在tradeno，强行赋值导致后续修改报错
							OrderPayParamForm orderPayParam = JSONObject.parseObject(orderWechat.getOrderpayparam(),
									OrderPayParamForm.class);
							orderPayParam.setTradeno(order.getTradeno());
							if (iWechatRechargeService.updateRelateRecord(orderWechat, orderPayParam) == 1) {
								logger.debug("更新wechatPaySrv.updateRelateRecord成功");
							} else {
								logger.debug("更新wechatPaySrv.updateRelateRecord失败");
							}
						}
					}
				} else {
					// 订单支付成功，但是微信返回失败
					logger.debug("订单支付失败，标识订单异常，后续通知管理人员处理");
					orderWechat.setWechatorderstatus(TradeState.CLOSED.intValue());
					orderWechat.setWechaterrormsg("微信支付状态：" + wechatNotifyMsg.getResultCode() + ",订单支付失败，关闭订单");
					orderWechat.setWechatorderstatus(TradeState.CLOSED.intValue());
					orderWechat.setRemark("微信支付状态：" + wechatNotifyMsg.getResultCode() + ",订单支成功，关闭订单，后续工作人员跟进");
					orderWechat.setWechatretruncode(wechatNotifyMsg.getReturnCode());
					orderWechat.setWechatresultcode(wechatNotifyMsg.getResultCode());
					orderWechat.setWechatorderstate(WechatOrderState.state_unusual.intValue());
					WechatOrderBo wechatOrderBo = BeanUtils.copyProperties(orderWechat, WechatOrderBo.class);
					if (iWechatPayService.updateOrderRecord(wechatOrderBo) == 1) {
						logger.debug("更新充值记录wechatOrder--->成功：" + orderWechat.getId());
					}
				}
			}

		}
	}

	// 充值相关处理
	private void recharge(WechatNotifyMsgVo wechatNotifyMsg) throws IOException {
		RechargeParamBo rechargeQueryParam = new RechargeParamBo();
		rechargeQueryParam.setWechatmchno(wechatNotifyMsg.getOutTradeNo());
		WechatOrderVo wechatOrder = iWechatRechargeService.rechargeQuery(rechargeQueryParam);
		if (wechatOrder == null) {
			logger.debug("商户订单号：" + wechatNotifyMsg.getOutTradeNo() + "不存在数据库，新增记录");

		} else {
			int databaseStatus = wechatOrder.getWechatorderstatus().intValue();
			if (databaseStatus == TradeState.SUCCESS.intValue()
					&& wechatNotifyMsg.getResultCode().equals(MessageManager.SUCCESS)) {
				logger.debug("数据支付状态：wechatorderstatus[{}],微信返回结果：resultCode:[{}] 状态一致", databaseStatus,
						wechatNotifyMsg.getResultCode());

				//TODO 充值成功自动扣费 V2.6.3

				return;
			} else {
				logger.debug("数据支付状态：wechatorderstatus[{}],微信返回结果：resultCode:[{}] 状态 不相同", databaseStatus,
						wechatNotifyMsg.getResultCode());
				if (databaseStatus != TradeState.SUCCESS.intValue()) {
					// 订单支付不成功，微信返回成功，更新订单状态
					if (wechatNotifyMsg.getResultCode().equals(MessageManager.SUCCESS)) {
						logger.debug("订单支付不成功，微信返回成功，更新订单状态");
						wechatOrder.setWechatorderstatus(TradeState.SUCCESS.intValue());
						wechatOrder.setWechatmchno(rechargeQueryParam.getWechatmchno());
						wechatOrder.setWechatpaytime(new Date());
						wechatOrder.setUpdatetime(new Date());
						wechatOrder.setWechatorderstatus(TradeState.SUCCESS.intValue());
						// wechatOrder.setWechaterrormsg("订单支付不成功，微信返回成功");
						wechatOrder.setRemark("订单支付不成功，微信返回成功");
						wechatOrder.setWechatretruncode(wechatNotifyMsg.getReturnCode());
						wechatOrder.setWechatresultcode(wechatNotifyMsg.getResultCode());
						wechatOrder.setWechatorderstate(WechatOrderState.state_normal.intValue());
						WechatOrderBo wechatOrderBo = BeanUtils.copyProperties(wechatOrder, WechatOrderBo.class);
						if (iWechatCommonFactory.updateOrderRecord(wechatOrderBo, wechatOrder.getOwnerid()) == 1) {
							logger.debug("更新充值记录wechatOrder--->成功：" + wechatOrder.getId());
						}
						// 订单支付不成功，微信返回失败，关闭订单
					} else {
						logger.debug("订单支付失败，关闭订单");
						wechatOrder.setUpdatetime(new Date());
						wechatOrder.setWechatorderstatus(TradeState.CLOSED.intValue());
						// wechatOrder.setWechaterrormsg("微信支付状态："
						// + wechatNotifyMsg.getResultCode()
						// + ",订单支付失败，关闭订单");
						wechatOrder.setRemark("微信支付状态：" + wechatNotifyMsg.getResultCode() + ",订单支付失败，关闭订单");
						wechatOrder.setWechatretruncode(wechatNotifyMsg.getReturnCode());
						wechatOrder.setWechatresultcode(wechatNotifyMsg.getResultCode());
						wechatOrder.setWechatorderstate(WechatOrderState.state_normal.intValue());
						WechatOrderBo wechatOrderBo = BeanUtils.copyProperties(wechatOrder, WechatOrderBo.class);
						if (iWechatCommonFactory.updateOrderRecord(wechatOrderBo, wechatOrder.getOwnerid()) == 1) {
							logger.debug("更新充值记录wechatOrder--->成功：" + wechatOrder.getId());
						}
					}
				} else {
					// 订单支付成功，但是微信返回失败
					logger.debug("订单支付失败，标识订单异常，后续通知管理人员处理");
					wechatOrder.setWechatorderstatus(TradeState.CLOSED.intValue());
					wechatOrder.setWechatorderstatus(TradeState.CLOSED.intValue());
					wechatOrder.setRemark("微信支付状态：" + wechatNotifyMsg.getResultCode() + ",订单支成功，微信支付失败，关闭订单，后续工作人员跟进");
					wechatOrder.setWechatretruncode(wechatNotifyMsg.getReturnCode());
					wechatOrder.setWechatresultcode(wechatNotifyMsg.getResultCode());
					WechatOrderBo wechatOrderBo = BeanUtils.copyProperties(wechatOrder, WechatOrderBo.class);
					if (iWechatCommonFactory.updateOrderRecord(wechatOrderBo, wechatOrder.getOwnerid()) == 1) {
						logger.debug("更新充值记录wechatOrder--->成功：" + wechatOrder.getId());
					}
				}
			}

		}
	}

	/**
	 * 原始版本
	 * 
	 * @param inputStream
	 * @return
	 * @throws Exception
	 */
	/*
	 * private Map<String, String> parseXmlOrigin(InputStream inputStream) throws
	 * Exception { Map<String, String> map = new HashMap<String, String>(); // 读取输入流
	 * SAXReader reader = new SAXReader(); Document document =
	 * reader.read(inputStream); // 得到xml根元素 Element root =
	 * document.getRootElement(); // 得到根元素的所有子节点 List<Element> elementList =
	 * root.elements(); // 遍历所有子节点 for (Element e : elementList) {
	 * logger.info("微信回调数据原始数据:"+e.getName()+":"+e.getText());
	 * if(e.getName().equals("DOCTYPE") || e.getName().equals("SYSTEM") ||
	 * e.getName().equals("ENTITY") || e.getName().equals("PUBLIC")) {
	 * map.put(e.getName(), ""); }else { map.put(e.getName(), e.getText()); }
	 * logger.info("微信回调数据处理后数据:"+e.getName()+":"+e.getText()); } // 释放资源
	 * inputStream.close(); inputStream = null; return map; }
	 */

	// 解决xxe漏洞问题
	private Map<String, String> parseXml(InputStream inputStream) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		String FEATURE = null;
		try {
			FEATURE = "http://apache.org/xml/features/disallow-doctype-decl";
			documentBuilderFactory.setFeature(FEATURE, true);
			FEATURE = "http://xml.org/sax/features/external-general-entities";
			documentBuilderFactory.setFeature(FEATURE, false);
			FEATURE = "http://xml.org/sax/features/external-parameter-entities";
			documentBuilderFactory.setFeature(FEATURE, false);
			FEATURE = "http://apache.org/xml/features/nonvalidating/load-external-dtd";
			documentBuilderFactory.setFeature(FEATURE, false);
			documentBuilderFactory.setXIncludeAware(false);
			documentBuilderFactory.setExpandEntityReferences(false);
		} catch (ParserConfigurationException e) {
			logger.info("ParserConfigurationException was thrown. The feature '" + FEATURE
					+ "' is probably not supported by your XML processor.");
		}
		DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
		Document doc = documentBuilder.parse(inputStream);
		Element root = doc.getDocumentElement();
		NodeList childNodes = root.getChildNodes();
		if (childNodes != null) {
			for (int i = 0; i < childNodes.getLength(); i++) {
				Node node = childNodes.item(i);
				map.put(node.getNodeName(), node.getTextContent());
			}
		}
		inputStream.close();
		logger.info("xml解析结果:" + map);
		return map;
	}

	// 发送短信和微信
	private void sendMsg(String ownerid, OrderVo order) {

		OwnerBo ownerBo = new OwnerBo();
		ownerBo.setOwnerid(ownerid);
		// 调用存在的方法
		OwnerVo currentOwner = iOwnerService.findByOwnerId(ownerBo);

		WechatMessageParamBo wechatMessageParam = new WechatMessageParamBo();
		wechatMessageParam.setMessageState(SmsEnum.pay_fee.intValue());
		wechatMessageParam.setOwnerid(ownerid);
		wechatMessageParam.setUserName(currentOwner.getUsername());
		wechatMessageParam.setSendType(SmsEnum.pay_fee.intValue());
		wechatMessageParam.setEnterpriseid(currentOwner.getEnterpriseid());
		wechatMessageParam.setTradeno(order.getTradeno());
		wechatMessageParam.setTrademoney(order.getRealamount());
		wechatMessageParam.setTradetime(DateUtils.formatDatetime(new Date()));

		Map<String, String> params = new HashMap<String, String>();
		params.put("phone", currentOwner.getUserphone());
		params.put("date", order.getMonth());
		params.put("name", order.getUsername());
		params.put("money", String.valueOf(order.getAmount()));
		iSmsToolService.sendSMS(currentOwner.getEnterpriseid(), SmsEnum.pay_fee.intValue(), params, order.getTradeno());
		SendMsgBo sendMsgBo = new SendMsgBo();
		sendMsgBo.setWechatMessageParam(wechatMessageParam);
		iSmsToolService.sendWeChatMsg(sendMsgBo);
	}

}
