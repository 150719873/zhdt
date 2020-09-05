package com.dotop.smartwater.project.module.client.third.http.wechat;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dotop.smartwater.dependence.cache.StringValueCache;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.utils.BeanUtils;
import com.dotop.smartwater.dependence.core.utils.MD5Util;
import com.dotop.smartwater.project.module.core.third.bo.wechat.RechargeParamBo;
import com.dotop.smartwater.project.module.core.third.bo.wechat.TradeRequestBo;
import com.dotop.smartwater.project.module.core.third.bo.wechat.WechatOrderBo;
import com.dotop.smartwater.project.module.core.third.bo.wechat.WeixinOrderQueryBo;
import com.dotop.smartwater.project.module.core.third.constants.CommonConstant;
import com.dotop.smartwater.project.module.core.third.enums.wechat.TradeState;
import com.dotop.smartwater.project.module.core.third.enums.wechat.WechatOrderType;
import com.dotop.smartwater.project.module.core.third.utils.wechat.MessageManager;
import com.dotop.smartwater.project.module.core.third.vo.wechat.GetAccessTokenMessageVo;
import com.dotop.smartwater.project.module.core.third.vo.wechat.WechatMessageVo;
import com.dotop.smartwater.project.module.core.third.vo.wechat.WechatOrderVo;
import com.dotop.smartwater.project.module.core.third.vo.wechat.WeixinOrderQueryVo;
import com.dotop.smartwater.project.module.core.water.bo.OrderExtBo;
import com.dotop.smartwater.project.module.core.water.bo.TradeDetailBo;
import com.dotop.smartwater.project.module.core.water.bo.TradePayBo;
import com.dotop.smartwater.project.module.core.water.bo.WechatPublicSettingBo;
import com.dotop.smartwater.project.module.core.water.config.Config;
import com.dotop.smartwater.project.module.core.water.vo.OrderVo;
import com.dotop.smartwater.project.module.core.water.vo.TradeDetailVo;
import com.dotop.smartwater.project.module.core.water.vo.customize.WechatPublicSettingVo;

/**
 * 微信接口工具类
 */

@Component
public class WechatUtil {

	private static final Logger LOGGER = LogManager.getLogger(WechatUtil.class);

	@Autowired
	private StringValueCache svc;

	/**
	 * 生成二维码
	 */
	private static final String WECHAT_QRCODE = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=<accessToken>";

	/**
	 * 生成永久二维码参数
	 */
	private static final String WECHAT_QR_LIMIT_SCENE = "{\"action_name\": \"QR_LIMIT_STR_SCENE\", \"action_info\": {\"scene\": {\"scene_str\": \"<sceneId>\"}}}";

	/**
	 * 通过ticket换取二维码
	 */
	private static final String WECHAT_SHOWQRCODE = "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=<TICKET>";

	/**
	 * 拿取微信accessToken
	 */
	private String wei_xin_access_token = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=JIUTONG_APPID&secret=JIUTONG_SECRET";

	/**
	 * 创建菜单
	 */
	private String menu_create_url = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=JIUTONG_ACCESS_TOKEN";

	/**
	 * 发送微信模版消息
	 */
	private static String GET_JS_API_TICKET_URL = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=<ACCESS_TOKEN>&type=jsapi";

	/**
	 * 微信appid
	 */
	public static String JIUTONG_APPID = "wx465107c730c47281";
	/**
	 * 微信SECRET
	 */
	public static String JIUTONG_SECRET = "7c7fec78f05695f13634943efc3b8b44";

	public static String JIUTONG_MCH_ID = "1235120102";

	public static String JIUTONG_PAY_SECRET = "3A71FFCDDB44051382E566F92392BC9E";

	/**
	 * 微信错误信息
	 */
	private static String WEIXIN_ERROR_CODE = "errcode";

	/**
	 * 创建菜单响应码 成功
	 */
	private static String CREATE_MENU_STATE_SUCCESS = "0";

	/**
	 * 微信支付
	 */
	private static String WEIXIN_SCAN_PAY_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";

	/**
	 * 接力购access_token_key
	 */
	private static String JIUTONG_ACCESS_TOKEN_KEY = "JIUTONG_WX_ACCESS_TOKEN_KEY:" + JIUTONG_APPID;

	private static String JIUTONG_JS_API_TICKET_KEY = "JIUTONG_WX_JS_API_TICKET_KEY:" + JIUTONG_APPID;

	/**
	 * 程序通过此字段获取微信Access_token
	 *
	 * @return
	 * @throws Exception
	 */
	public String getWeiXinAccessToken(WechatPublicSettingBo wechatPublicSetting) throws Exception {

		LOGGER.warn("-------------------------获取微信AccessToken start-------------------------------");

		JIUTONG_APPID = wechatPublicSetting.getAppid();
		JIUTONG_SECRET = wechatPublicSetting.getAppsecret();

		wei_xin_access_token = wei_xin_access_token.replace("JIUTONG_APPID", WechatUtil.JIUTONG_APPID);
		wei_xin_access_token = wei_xin_access_token.replace("JIUTONG_SECRET", WechatUtil.JIUTONG_SECRET);
		String json = WechatHttpClientUtils.sendGetRequest(wei_xin_access_token);
		if (json.contains(WechatUtil.WEIXIN_ERROR_CODE)) {
			WechatMessageVo message = JSONObject.parseObject(json, WechatMessageVo.class);
			LOGGER.info("获取微信AccessToken失败,返回码:{},errmsg:{}", message.errcode, message.errmsg);
		} else {
			GetAccessTokenMessageVo accessToken = JSONObject.parseObject(json, GetAccessTokenMessageVo.class);
			// 添加到redis缓存 ，并设置过期时间设置为微信时间-500秒
			svc.set(WechatUtil.JIUTONG_ACCESS_TOKEN_KEY, accessToken.getAccess_token(),
					accessToken.getExpires_in() - 500);
			LOGGER.warn("Access Token: {}", accessToken.getAccess_token());
			return accessToken.getAccess_token();
		}
		LOGGER.warn("-------------------------获取微信AccessToken end -------------------------------");

		return null;
	}

	/**
	 * 刷新微信菜单
	 *
	 * @return
	 * @throws Exception
	 */
	public void resetWeiXinMenu(String menuJson, WechatPublicSettingBo wechatPublicSetting) throws Exception {
		LOGGER.info("-------------------------创建微信菜单 start-------------------------------");
		String accessToken = this.getWeiXinAccessToken(wechatPublicSetting);
		LOGGER.info("传入的json参数为[menuJson:{},accessToken{}]", menuJson, accessToken);

		if (!StringUtils.isEmpty(accessToken)) {
			String json = WechatHttpClientUtils
					.sendPostJSONRequest(this.menu_create_url.replace("JIUTONG_ACCESS_TOKEN", accessToken), menuJson);
			// WechatMessageVo message = JSONObject.parseObject(json,
			// WechatMessageVo.class);
			// if (!WechatUtil.CREATE_MENU_STATE_SUCCESS.equals(message)) {
			// LOGGER.error("传入的json参数为[errcode:{},errmsg{}]", message.getErrcode(),
			// message.getErrmsg());
			// }

		} else {
			LOGGER.warn("获取微信AccessToken出现异常");
		}

		LOGGER.info("-------------------------创建微信菜单 end-------------------------------");
	}

	public String getJsApiTicket(WechatPublicSettingBo wechatPublicSetting) {
		LOGGER.warn("[Weixin]获取微信jsapi_ticket");
		String ticket = null;
		try {
			ticket = svc.get(JIUTONG_JS_API_TICKET_KEY, String.class);
			if (ticket == null) {
				WechatUtil wehchatUtil = new WechatUtil();
				String accessToken = wehchatUtil.getWeiXinAccessToken(wechatPublicSetting);
				if (StringUtils.isNotEmpty(accessToken)) {
					String url = GET_JS_API_TICKET_URL.replaceFirst("<ACCESS_TOKEN>", accessToken);
					String result = WechatHttpClientUtils.sendGetRequest(url);
					JSONObject jsonObject = JSON.parseObject(result);
					if (jsonObject.getIntValue("errcode") == 0) {
						ticket = jsonObject.getString("ticket");
						long timeout = jsonObject.getLongValue("expires_in");
						// dsa
						svc.set(JIUTONG_JS_API_TICKET_KEY, ticket, timeout);
					} else {
						LOGGER.warn("[Warn]获取微信jsapi_ticket失败: errCode[{}] errMsg[{}]",
								jsonObject.getIntValue("errcode"), jsonObject.getIntValue("errmsg"));
					}
				}
			}
		} catch (Exception e) {
			LOGGER.warn("[Exception]获取微信jsapi_ticket", e);
		}
		LOGGER.warn("[Weixin]获取微信jsapi_ticket: {}", ticket);
		return ticket;
	}

	public Map<String, String> payScan(OrderVo view, WechatPublicSettingBo weixinConfig, TradePayBo tradePay,
			String ip) {
		Map<String, String> paramMap = new HashMap<>();
		Map<String, String> resultMap = new HashMap<>();
		InputStream inputStream = null;
		try {
			NumberFormat numberFormat = new DecimalFormat("#.##");
			String attach = WechatOrderType.pay_cost.intValue() + "|" + view.getOwnerid() + "|" + view.getId() + "|"
					+ tradePay.getAmount() + "|" + tradePay.getTradeno();
			paramMap.put("appid", weixinConfig.getAppid());
			paramMap.put("mch_id", weixinConfig.getMchid());
			paramMap.put("nonce_str", CaptchaUtil.getAlphabetCaptcha(31));
			String body = "扫码支付" + numberFormat.format(view.getAmount()) + "元";
			paramMap.put("body", body);
			paramMap.put("attach", attach);
			paramMap.put("detail", "");
			paramMap.put("out_trade_no", tradePay.getPayno());
			paramMap.put("total_fee", "1");
			paramMap.put("spbill_create_ip", ip);
			paramMap.put("notify_url", weixinConfig.getDomain() + weixinConfig.getRequestreturnurl());

			// 微信公众号支付
			paramMap.put("trade_type", "NATIVE");
			String sign = sign(paramMap, weixinConfig.getPaysecret());
			paramMap.put("sign", sign);
			String paramStr = paramMap.toString();
			LOGGER.warn(paramStr);

			inputStream = WechatHttpClientUtils.sendPostXMLRequest(WEIXIN_SCAN_PAY_URL, map2XML(paramMap));
			resultMap = MessageManager.parseXml(inputStream);
			LOGGER.warn("result : {}", resultMap);
		} catch (Exception e) {
			LOGGER.warn("[Exception]生成支付二维码失败", e);
			resultMap.put("return_code", "fail");
			resultMap.put("return_msg", "生成支付二维码失败");
			LOGGER.error(e.getMessage(), e);
		} finally {
			try {
				if (inputStream != null) {
					inputStream.close();
				}
			} catch (IOException e) {
				LOGGER.error(e.getMessage(), e);
			}
		}
		return resultMap;
	}

	// 刷卡支付-原账单管理支付
	public Map<String, String> payByCard(OrderExtBo view, WechatPublicSettingBo weixinConfig, TradePayBo tradePay) {
		Map<String, String> orderMap = new HashMap<>();
		Map<String, String> resultMap = new HashMap<>();
		InputStream inputStream = null;
		try {
			String nonceStr = CaptchaUtil.getAlphabetCaptcha(31);
			NumberFormat numberFormat = new DecimalFormat("#.##");
			orderMap.put("appid", weixinConfig.getAppid());
			orderMap.put("mch_id", weixinConfig.getMchid());
			orderMap.put("nonce_str", nonceStr);
			String body = "刷卡支付" + numberFormat.format(view.getSurpluspay()) + "元";
			orderMap.put("body", body);
			orderMap.put("out_trade_no", tradePay.getPayno());
			// orderMap.put("total_fee", String.valueOf(Math.round(view.getSurpluspay() *
			// 100)));
			orderMap.put("total_fee", "1");
			orderMap.put("spbill_create_ip", view.getIp());
			// 微信支付授权码
			orderMap.put("auth_code", view.getPaycard());
			String sign = sign(orderMap, weixinConfig.getPaysecret());
			orderMap.put("sign", sign);
			LOGGER.warn(orderMap.toString());

			inputStream = WechatHttpClientUtils.sendPostXMLRequest(weixinConfig.getPaybycardurl(), map2XML(orderMap));
			resultMap = MessageManager.parseXml(inputStream);
			LOGGER.warn("result : {}", resultMap);

			if (MessageManager.SUCCESS.equalsIgnoreCase(resultMap.get(MessageManager.RESP_RETURN_CODE))) {
				// 如果返回结果错误中包含系统超时、银行系统异常、需要输入密码时
				if (MessageManager.FAIL.equalsIgnoreCase(resultMap.get(MessageManager.RESP_RESULT_CODE))
						&& (MessageManager.USERPAYING.equalsIgnoreCase(resultMap.get(MessageManager.RESP_ERR_CODE))
								|| MessageManager.BANKERROR
										.equalsIgnoreCase(resultMap.get(MessageManager.RESP_ERR_CODE))
								|| MessageManager.SYSTEMERROR
										.equalsIgnoreCase(resultMap.get(MessageManager.RESP_ERR_CODE)))) {
					Map<String, String> queryMap = new HashMap<>();
					queryMap.put("appid", orderMap.get("appid"));
					queryMap.put("mch_id", orderMap.get("mch_id"));
					queryMap.put("out_trade_no", orderMap.get("out_trade_no"));
					queryMap.put("nonce_str", CaptchaUtil.getAlphabetCaptcha(31));
					queryMap.put("sign", sign(queryMap, weixinConfig.getPaysecret()));

					/** 查询账单支付状态 */
					Map<String, String> resultQuery = queryOrder(weixinConfig, queryMap);

					resultMap.put("return_code", resultQuery.get("return_code"));
					resultMap.put("result_code", resultQuery.get("result_code"));
					if (StringUtils.isNotBlank(resultQuery.get("err_code_des"))) {
						resultMap.put("err_code_des", resultQuery.get("err_code_des"));
					}
				}
			} else {
				if (resultMap.get(MessageManager.RESP_ERR_CODE_DES) != null) {
					resultMap.put("return_code", "FAIL");
					resultMap.put("return_msg", resultMap.get(MessageManager.RESP_ERR_CODE_DES));
				}
			}

		} catch (Exception e) {
			LOGGER.warn("[Exception]微信刷卡支付失败", e);
			resultMap.put("return_code", "fail");
			resultMap.put("return_msg", "支付失败");
			LOGGER.error(e.getMessage(), e);
		} finally {

			try {
				if (inputStream != null) {
					inputStream.close();
				}
			} catch (IOException e) {
				LOGGER.error(e.getMessage(), e);
			}
		}
		return resultMap;
	}

	public Map<String, String> getPayStatus(WechatPublicSettingVo config, TradeDetailVo detailVo) {
		Map<String, String> resultMap = new HashMap<>();
		Map<String, String> returnMap = new HashMap<>();
		InputStream inputStream = null;
		try {
			Map<String, String> queryMap = new HashMap<>(10);
			queryMap.put("appid", config.getAppid());
			queryMap.put("mch_id", config.getMchid());
			queryMap.put("out_trade_no", detailVo.getThirdPartyNum());
			queryMap.put("nonce_str", CaptchaUtil.getAlphabetCaptcha(31));
			queryMap.put("sign", WechatUtil.sign(queryMap, config.getPaysecret()));

			inputStream = WechatHttpClientUtils.sendPostXMLRequest(config.getOrderqueryurl(),
					WechatUtil.map2XML(queryMap));
			returnMap = MessageManager.parseXml(inputStream);

			if (returnMap.get("return_code").equals(MessageManager.SUCCESS)
					&& returnMap.get("result_code").equals(MessageManager.SUCCESS)) {
				if (returnMap.get("trade_state").equals(MessageManager.SUCCESS)) {
					resultMap.put("status", MessageManager.SUCCESS);
					resultMap.put("msg", "支付成功");
				} else {
					resultMap.put("status", returnMap.get("trade_state"));
					resultMap.put("msg", returnMap.get("trade_state_desc"));
				}
			} else {
				resultMap.put("status", "ERROR");
				resultMap.put("msg", "更新异常");
			}
			return resultMap;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			resultMap.put("status", "error");
			resultMap.put("msg", "获取账单状态时异常");
		} catch (Exception e) {
			resultMap.put("status", "error");
			resultMap.put("msg", "获取账单状态时异常");
			LOGGER.error(LogMsg.to(e));
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					LOGGER.error(LogMsg.to(e));
				}
			}
		}
		return resultMap;
	}

	public Map<String, String> payByCard(WechatPublicSettingVo config, TradeDetailBo tradeDetail) {
		Map<String, String> orderMap = new HashMap<>();
		Map<String, String> resultMap = new HashMap<>();
		InputStream inputStream = null;
		try {
			String nonceStr = CaptchaUtil.getAlphabetCaptcha(31);
			NumberFormat numberFormat = new DecimalFormat("#.##");
			orderMap.put("appid", config.getAppid());
			orderMap.put("mch_id", config.getMchid());
			orderMap.put("nonce_str", nonceStr);
			String body = "刷卡支付" + numberFormat.format(new BigDecimal(tradeDetail.getAmount())) + "元";
			orderMap.put("body", body);
			orderMap.put("out_trade_no", tradeDetail.getThirdPartyNum());
			// orderMap.put("total_fee", String.valueOf(Math.round(view.getSurpluspay() *
			// 100)));
			orderMap.put("total_fee", "1");
			orderMap.put("spbill_create_ip", tradeDetail.getIp());
			// 微信支付授权码
			orderMap.put("auth_code", tradeDetail.getPaycard());
			String sign = sign(orderMap, config.getPaysecret());
			orderMap.put("sign", sign);
			LOGGER.warn(orderMap.toString());

			inputStream = WechatHttpClientUtils.sendPostXMLRequest(config.getPaybycardurl(), map2XML(orderMap));
			resultMap = MessageManager.parseXml(inputStream);
			LOGGER.warn("result : {}", resultMap);

			if (MessageManager.SUCCESS.equalsIgnoreCase(resultMap.get(MessageManager.RESP_RETURN_CODE))) {
				// 如果返回结果错误中包含系统超时、银行系统异常、需要输入密码时
				if (MessageManager.FAIL.equalsIgnoreCase(resultMap.get(MessageManager.RESP_RESULT_CODE))
						&& (MessageManager.USERPAYING.equalsIgnoreCase(resultMap.get(MessageManager.RESP_ERR_CODE))
								|| MessageManager.BANKERROR
										.equalsIgnoreCase(resultMap.get(MessageManager.RESP_ERR_CODE))
								|| MessageManager.SYSTEMERROR
										.equalsIgnoreCase(resultMap.get(MessageManager.RESP_ERR_CODE)))) {
					Map<String, String> queryMap = new HashMap<>();
					queryMap.put("appid", orderMap.get("appid"));
					queryMap.put("mch_id", orderMap.get("mch_id"));
					queryMap.put("out_trade_no", orderMap.get("out_trade_no"));
					queryMap.put("nonce_str", CaptchaUtil.getAlphabetCaptcha(31));
					queryMap.put("sign", sign(queryMap, config.getPaysecret()));

					/** 查询账单支付状态 */
					WechatPublicSettingBo weixinConfigBo = BeanUtils.copy(config, WechatPublicSettingBo.class);
					Map<String, String> resultQuery = queryOrder(weixinConfigBo, queryMap);
					resultMap.put("return_code", resultQuery.get("return_code"));
					resultMap.put("result_code", resultQuery.get("result_code"));
					if (StringUtils.isNotBlank(resultQuery.get("err_code_des"))) {
						resultMap.put("err_code_des", resultQuery.get("err_code_des"));
					}
				}
			} else {
				if (resultMap.get(MessageManager.RESP_ERR_CODE_DES) != null) {
					resultMap.put("return_code", "FAIL");
					resultMap.put("return_msg", resultMap.get(MessageManager.RESP_ERR_CODE_DES));
				}
			}

		} catch (Exception e) {
			LOGGER.warn("[Exception]微信刷卡支付失败", e);
			resultMap.put("return_code", "fail");
			resultMap.put("return_msg", "支付失败");
			LOGGER.error(e.getMessage(), e);
		} finally {

			try {
				if (inputStream != null) {
					inputStream.close();
				}
			} catch (IOException e) {
				LOGGER.error(e.getMessage(), e);
			}
		}
		return resultMap;
	}

	// 刷卡支付-查询账单是否已经支付
	public static Map<String, String> queryOrder(WechatPublicSettingBo weixinConfig, Map<String, String> reqData) {
		Map<String, String> resultMap = new HashMap<>();
		try {
			long start = System.currentTimeMillis();
			long end = System.currentTimeMillis() + 40 * 1000;
			while (System.currentTimeMillis() - start < 40 * 1000) {
				try {
					Map<String, String> lastResult = requestWechat(weixinConfig.getOrderqueryurl(), reqData);
					String returnCode = lastResult.get("return_code");
					if (returnCode.equals("SUCCESS")) {
						String resultCode = lastResult.get("result_code");
						String tradeState = lastResult.get("trade_state");
						if (resultCode.equals("SUCCESS") && tradeState.equals("SUCCESS")) {
							resultMap.put("return_code", "SUCCESS");
							resultMap.put("result_code", "SUCCESS");
							break;
						} else {
							// 看错误码，若支付结果未知，则重试提交刷卡支付
							if (tradeState.equals("SYSTEMERROR") || tradeState.equals("BANKERROR")
									|| tradeState.equals("USERPAYING")) {
								// 如果查询时间超过40秒，则撤销账单
								if (end - System.currentTimeMillis() <= 5000) {
									// 撤销账单
									resultMap.put("return_code", "FAIL");
									resultMap.put("err_code_des", "支付超时已撤销，请重新支付");
									/*
									 * Map<String, String> revokeMap = new HashMap<String , String>();
									 * revokeMap.put("appid", reqData.get("appid")); revokeMap.put("mch_id",
									 * reqData.get("mch_id")); revokeMap.put("nonce_str",
									 * CaptchaUtil.getAlphabetCaptcha(31)); revokeMap.put("out_trade_no",
									 * reqData.get("out_trade_no")); revokeMap.put("sign", sign(revokeMap,
									 * weixinConfig.getPaysecret())); Map<String, String> resultRevoke =
									 * requestWechat(weixinConfig.getRevokeorderurl() ,revokeMap);
									 * 
									 * if (resultRevoke.get("return_code").equals("SUCCESS") &&
									 * resultRevoke.get("result_code").equals("SUCCESS")) {
									 * resultMap.put("return_code", "FAIL"); resultMap.put("err_code_des",
									 * "支付超时已撤销，请重新支付"); } else { resultMap.put("return_code", "FAIL");
									 * resultMap.put("err_code_des", "支付超时且撤销交易失败，请重新扫码支付"); }
									 */
									break;
								} else {
									LOGGER.warn("microPayWithPos: try micropay again");
									Thread.sleep(5 * 1000);
									continue;
								}
							} else {
								break;
							}
						}
					} else {
						break;
					}
				} catch (Exception ex) {
					LOGGER.error(ex.getMessage(), ex);
				}
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
		return resultMap;
	}

	// 请求微信接口
	public static Map<String, String> requestWechat(String requestUrl, Map<String, String> reqData) {
		InputStream inputStream = null;
		Map<String, String> resultMap = new HashMap<>();
		try {
			inputStream = WechatHttpClientUtils.sendPostXMLRequest(requestUrl, map2XML(reqData));
			resultMap = MessageManager.parseXml(inputStream);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		} finally {
			try {
				if (inputStream != null) {
					inputStream.close();
				}
			} catch (Exception e2) {
				LOGGER.error(e2.getMessage(), e2);
			}
		}
		return resultMap;
	}

	public static WechatOrderVo unifiedorder(TradeRequestBo tradeRequest, WechatPublicSettingBo weixinConfig,
			String openid) {
		// ***************************开发测试阶段：微信支付金额设置为0.01，重要后续一定改回去**************************************************

		WechatOrderBo tradePost = new WechatOrderBo();
		tradePost.setCreatetime(new Date());
		tradePost.setOrderid(tradeRequest.getOrder().getId());
		tradePost.setOwnerid(tradeRequest.getOrder().getOwnerid());
		tradePost.setWechatamount(tradeRequest.getAmount());
		tradePost.setWechatmchno(tradeRequest.getWechatmchno());
		tradePost.setWechatorderstatus(TradeState.NOTPAY.intValue());
		tradePost.setWechatpaytype("JSAPI");

		String nonceStr = CaptchaUtil.getAlphabetCaptcha(31);
		NumberFormat numberFormat = new DecimalFormat("#.##");
		// sign
		String body = "公众号缴费" + numberFormat.format(tradeRequest.getAmount()) + "元";
		String attach = WechatOrderType.pay_cost.intValue() + "|" + tradeRequest.getOrder().getOwnerid() + "|"
				+ tradeRequest.getOrder().getId() + "|" + tradeRequest.getAmount() + "|"
				+ tradeRequest.getOrder().getTradeno();
		tradePost.setAttach(attach);
		// 开发模式设置0.1
		int amount2 = 0;
		if (Config.weixinDebug) {
			amount2 = 1;
		} else {
			amount2 = (int) (tradeRequest.getAmount() * 100);
		}

		try {
			Map<String, String> orderMap = new HashMap<>();
			orderMap.put("appid", weixinConfig.getAppid());
			orderMap.put("mch_id", weixinConfig.getMchid());
			orderMap.put("nonce_str", nonceStr);
			orderMap.put("body", body);
			orderMap.put("attach", attach);
			orderMap.put("out_trade_no", tradeRequest.getWechatmchno());
			orderMap.put("total_fee", String.valueOf(amount2));
			orderMap.put("spbill_create_ip", tradeRequest.getIp());
			orderMap.put("notify_url", weixinConfig.getDomain() + weixinConfig.getRequestreturnurl());
			orderMap.put("trade_type", "JSAPI");// 微信公众号支付
			orderMap.put("openid", openid);

			String sign = sign(orderMap, weixinConfig.getPaysecret());
			orderMap.put("sign", sign);
			tradePost.setWechatsign(sign);

			InputStream inputStream = WechatHttpClientUtils.sendPostXMLRequest(weixinConfig.getUnifiedorderurl(),
					map2XML(orderMap));
			Map<String, String> resultMap = MessageManager.parseXml(inputStream);

			if (MessageManager.SUCCESS.equalsIgnoreCase(resultMap.get(MessageManager.RESP_RETURN_CODE))) {
				if (MessageManager.SUCCESS.equalsIgnoreCase(resultMap.get(MessageManager.RESP_RESULT_CODE))) {
					tradePost.setWechatresultcode(MessageManager.SUCCESS);
					tradePost.setPrepayid(resultMap.get(MessageManager.RESP_PREPAY_ID));
					tradePost.setWechatorderstatus(TradeState.NOTPAY.intValue());
					if (resultMap.get(MessageManager.RESP_ERR_CODE_DES) != null) {
						tradePost.setWechaterrormsg(resultMap.get(MessageManager.RESP_ERR_CODE_DES));
					}
				} else {
					tradePost.setWechatresultcode(MessageManager.FAIL);
					tradePost.setWechatretruncode(resultMap.get(MessageManager.RESP_ERR_CODE));
					tradePost.setWechatorderstatus(TradeState.PAYERROR.intValue());
					if (resultMap.get(MessageManager.RESP_ERR_CODE_DES) != null) {
						tradePost.setWechaterrormsg(resultMap.get(MessageManager.RESP_ERR_CODE_DES));
					}
				}
			} else {
				LOGGER.warn("[Warn]微信统一下单失败: returnCode[{}] returnMsg[{}]",
						resultMap.get(MessageManager.RESP_RETURN_CODE), resultMap.get("return_msg"));
				tradePost.setWechatresultcode(MessageManager.FAIL);
				tradePost.setWechatorderstatus(TradeState.PAYERROR.intValue());
				if (resultMap.get(MessageManager.RESP_ERR_CODE_DES) != null) {
					tradePost.setWechaterrormsg(resultMap.get(MessageManager.RESP_ERR_CODE_DES));
				}
			}
		} catch (Exception e) {
			LOGGER.warn("[Exception]微信统一下单", e);
			tradePost.setWechatresultcode(MessageManager.FAIL);
			tradePost.setWechatorderstatus(TradeState.PAYERROR.intValue());
		}

		return BeanUtils.copy(tradePost, WechatOrderVo.class);
	}

	public static WechatOrderVo unifiedorderRecharge(RechargeParamBo rechargeParam, WechatPublicSettingBo weixinConfig,
			String openid, String ip, String wechatmchno) {
		WechatOrderBo wechatOrder = new WechatOrderBo();
		// ***************************开发测试阶段：微信支付金额设置为0.01，重要后续一定改回去**************************************************
		// String wechatmchno = GenerateSNUtil.nextChargeSN();
		wechatOrder.setCreatetime(new Date());
		wechatOrder.setOrderpayparam(JSONObject.toJSONString(rechargeParam));
		wechatOrder.setOwnerid(rechargeParam.getOwnerid());
		wechatOrder.setWechatamount(rechargeParam.getAmount());
		wechatOrder.setWechatmchno(wechatmchno);
		wechatOrder.setWechatorderstatus(TradeState.NOTPAY.intValue());
		wechatOrder.setWechatpaytype("JSAPI");
		wechatOrder.setPaytype(WechatOrderType.recharge.intValue());

		String nonceStr = CaptchaUtil.getAlphabetCaptcha(31);
		NumberFormat numberFormat = new DecimalFormat("#.##");
		// sign
		String body = "公众号充值" + numberFormat.format(rechargeParam.getAmount()) + "元";
		// 第三位是区分缴费或者充值
		String attach = WechatOrderType.recharge.intValue() + "|" + rechargeParam.getOwnerid() + "|"
				+ rechargeParam.getAmount();
		wechatOrder.setAttach(attach);

		// 开发模式设置0.1
		if (Config.weixinDebug) {
			rechargeParam.setAmount(0.01d);
		} else {
			rechargeParam.setAmount(rechargeParam.getAmount());
		}
		int amount2 = (int) (rechargeParam.getAmount() * 100);

		try {
			Map<String, String> orderMap = new HashMap<String, String>();
			orderMap.put("appid", weixinConfig.getAppid());
			orderMap.put("mch_id", weixinConfig.getMchid());
			orderMap.put("nonce_str", nonceStr);
			orderMap.put("body", body);
			orderMap.put("attach", attach);
			orderMap.put("out_trade_no", wechatmchno);
			orderMap.put("total_fee", String.valueOf(amount2));
			orderMap.put("spbill_create_ip", ip);
			orderMap.put("notify_url", weixinConfig.getDomain() + weixinConfig.getRequestreturnurl());
			orderMap.put("trade_type", "JSAPI");// 微信公众号支付
			orderMap.put("openid", openid);

			String sign = sign(orderMap, weixinConfig.getPaysecret());
			orderMap.put("sign", sign);
			wechatOrder.setWechatsign(sign);
			InputStream inputStream = WechatHttpClientUtils.sendPostXMLRequest(weixinConfig.getUnifiedorderurl(),
					map2XML(orderMap));
			Map<String, String> resultMap = MessageManager.parseXml(inputStream);

			if (MessageManager.SUCCESS.equalsIgnoreCase(resultMap.get(MessageManager.RESP_RETURN_CODE))) {
				if (MessageManager.SUCCESS.equalsIgnoreCase(resultMap.get(MessageManager.RESP_RESULT_CODE))) {
					wechatOrder.setWechatresultcode(MessageManager.SUCCESS);
					wechatOrder.setPrepayid(resultMap.get(MessageManager.RESP_PREPAY_ID));
					wechatOrder.setWechatorderstatus(TradeState.NOTPAY.intValue());
					if (resultMap.get(MessageManager.RESP_ERR_CODE_DES) != null) {
						wechatOrder.setWechaterrormsg(resultMap.get(MessageManager.RESP_ERR_CODE_DES));
					}
				} else {
					wechatOrder.setWechatresultcode(MessageManager.FAIL);
					wechatOrder.setWechatretruncode(resultMap.get(MessageManager.RESP_ERR_CODE));
					wechatOrder.setWechatorderstatus(TradeState.PAYERROR.intValue());
					if (resultMap.get(MessageManager.RESP_ERR_CODE_DES) != null) {
						wechatOrder.setWechaterrormsg(resultMap.get(MessageManager.RESP_ERR_CODE_DES));
					}
				}
			} else {
				LOGGER.warn("[Warn]微信统一下单失败: returnCode[{}] returnMsg[{}]",
						resultMap.get(MessageManager.RESP_RETURN_CODE), resultMap.get("return_msg"));
				wechatOrder.setWechatresultcode(MessageManager.FAIL);
				wechatOrder.setWechatorderstatus(TradeState.PAYERROR.intValue());
				if (resultMap.get(MessageManager.RESP_ERR_CODE_DES) != null) {
					wechatOrder.setWechaterrormsg(resultMap.get(MessageManager.RESP_ERR_CODE_DES));
				}
			}
		} catch (Exception e) {
			LOGGER.warn("[Exception]微信统一下单", e);
			wechatOrder.setWechatresultcode(MessageManager.FAIL);
			wechatOrder.setWechatorderstatus(TradeState.PAYERROR.intValue());
		}

		return BeanUtils.copy(wechatOrder, WechatOrderVo.class);
	}

	public static WeixinOrderQueryVo orderQuery(String outTradeNO, WechatPublicSettingBo wechatPublicSetting) {
		WeixinOrderQueryBo orderQuery = null;
		InputStream inputStream = null;
		try {
			String nonceStr = CaptchaUtil.getAlphabetCaptcha(31);
			Map<String, String> orderMap = new HashMap<String, String>();
			orderMap.put("appid", wechatPublicSetting.getAppid());
			orderMap.put("mch_id", wechatPublicSetting.getMchid());

			// orderMap.put("transaction_id ", "");
			orderMap.put("out_trade_no", outTradeNO);
			orderMap.put("nonce_str", nonceStr);

			String sign = sign(orderMap, wechatPublicSetting.getPaysecret());
			orderMap.put("sign", sign);

			inputStream = WechatHttpClientUtils.sendPostXMLRequest(wechatPublicSetting.getOrderqueryurl(),
					map2XML(orderMap));
			Map<String, String> resultMap = MessageManager.parseXml(inputStream);
			LOGGER.warn("订单查询返回数据：{}", JSONObject.toJSONString(resultMap));
			orderQuery = new WeixinOrderQueryBo();
			orderQuery.setTradeNO(outTradeNO);
			if (MessageManager.SUCCESS.equalsIgnoreCase(resultMap.get(MessageManager.RESP_RETURN_CODE))) {
				orderQuery.setReturnCode(MessageManager.SUCCESS);

				if (MessageManager.SUCCESS.equalsIgnoreCase(resultMap.get(MessageManager.RESP_RESULT_CODE))) {
					orderQuery.setResultCode(MessageManager.SUCCESS);
					orderQuery.setDeviceInfo(resultMap.get("device_info"));
					orderQuery.setTradeState(TradeState.valueOf(resultMap.get("trade_state")));
				} else {
					orderQuery.setResultCode(MessageManager.FAIL);
					orderQuery.setErrCode(resultMap.get(MessageManager.RESP_ERR_CODE));
				}
			} else {
				LOGGER.warn("[Warn]微信查询订单失败: returnCode[{}] returnMsg[{}]",
						resultMap.get(MessageManager.RESP_RETURN_CODE), resultMap.get(MessageManager.RESP_RETURN_MSG));
				orderQuery.setReturnCode(MessageManager.FAIL);
				orderQuery.setReturnMsg(resultMap.get(MessageManager.RESP_RETURN_MSG));
			}
		} catch (Exception e) {
			LOGGER.warn("[Exception]微信查询订单", e);
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					LOGGER.error(e.getMessage(), e);
				}
			}
		}

		return BeanUtils.copy(orderQuery, WeixinOrderQueryVo.class);
	}

	/**
	 * 将字节数组转换为十六进制字符串
	 *
	 * @param byteArray
	 * @return
	 */
	private static String byteToStr(byte[] byteArray) {
		StringBuilder strDigest = new StringBuilder();
		for (int i = 0; i < byteArray.length; i++) {
			strDigest.append(byteToHexStr(byteArray[i]));
		}
		return strDigest.toString();
	}

	public static String map2XML(Map<String, String> map) {
		StringBuilder builder = new StringBuilder();
		builder.append("<xml>");
		for (Map.Entry<String, String> entry : map.entrySet()) {
			builder.append('<').append(entry.getKey()).append('>')
					// .append("<![CDATA[").append(entry.getValue()).append("]]>")
					.append(entry.getValue()).append("</").append(entry.getKey()).append('>');
		}
		builder.append("</xml>");
		return builder.toString();
	}

	/**
	 * 将字节转换为十六进制字符串
	 *
	 * @param mByte
	 * @return
	 */
	private static String byteToHexStr(byte mByte) {
		char[] Digit = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
		char[] tempArr = new char[2];
		tempArr[0] = Digit[(mByte >>> 4) & 0X0F];
		tempArr[1] = Digit[mByte & 0X0F];

		return new String(tempArr);
	}

	public static String sign(Map<String, String> map, String key) {
		if (map == null) {
			throw new NullPointerException();
		}

		StringBuilder builder = new StringBuilder();
		Map<String, String> treeMap = new TreeMap<>(map);
		for (Map.Entry<String, String> entry : treeMap.entrySet()) {
			if (entry.getValue() != null && entry.getValue().length() > 0) {
				builder.append(entry.getKey()).append('=').append(entry.getValue()).append('&');
			}
		}
		if (key != null && key.length() > 0) {
			builder.append("key=").append(key);
		} else if (builder.length() > 1) {
			builder.deleteCharAt(builder.length() - 1);
		}

		LOGGER.warn("[Weixin]签名符串 --> {}", builder);
		String sign = MD5Util.encode(builder.toString());
		LOGGER.warn("[Weixin]最终签名 --> {}", sign);

		return sign;
	}

	public Map<String, Object> getWeixinOpenId(String code, WechatPublicSettingVo weixinConfig) {
		Map<String, Object> map = new HashMap<>();
		String openId = null;
		try {
			String url = MessageFormat.format(weixinConfig.getGatewayopenidbycode(), weixinConfig.getAppid(),
					weixinConfig.getAppsecret(), code);

			String tokenInfo = WechatHttpClientUtils.sendGetRequest(url);
			JSONObject jsonObject = JSON.parseObject(tokenInfo);
			openId = jsonObject.getString(CommonConstant.WEIXIN_OPEN_ID);
		} catch (Exception e) {
			LOGGER.warn("", e);
		}
		map.put(CommonConstant.WEIXIN_OPEN_ID, openId);
		map.put(CommonConstant.WECHAT_CONFIG, weixinConfig);
		return map;
	}

	public String getWeixinAuthorizeCode(String requestUrl, WechatPublicSettingVo weixinConfig) {
		String code = null;
		try {
			String url = MessageFormat.format(weixinConfig.getGatewayauthorizecode(), weixinConfig.getAppid(),
					weixinConfig.getAppsecret(), URLEncoder.encode(requestUrl, "UTF-8"));
			String codeInfo = WechatHttpClientUtils.sendGetRequest(url);
			JSONObject jsonObject = JSON.parseObject(codeInfo);
			code = jsonObject.getString(CommonConstant.WEIXIN_AUTHORIZE_CODE);
		} catch (Exception e) {
			LOGGER.warn("", e);
		}
		return code;
	}

	public String getAuthorizeCodeRedirectURL(String requestUrl, WechatPublicSettingVo weixinConfig) {
		try {
			LOGGER.info("requestUrl:{}", requestUrl);
			LOGGER.info("weixinConfig:{}", weixinConfig);
			return MessageFormat.format(weixinConfig.getGatewayauthorizecode(), weixinConfig.getAppid(),
					URLEncoder.encode(requestUrl, "UTF-8"));
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
		return requestUrl;
	}

	public static String sign2(Map<String, String> map, String key) {
		if (map == null) {
			throw new NullPointerException();
		}

		StringBuilder builder = new StringBuilder();
		Map<String, String> treeMap = new TreeMap<>(map);
		for (Map.Entry<String, String> entry : treeMap.entrySet()) {
			if (entry.getValue() != null && entry.getValue().length() > 0) {
				builder.append(entry.getKey()).append('=').append(entry.getValue()).append('&');
			}
		}
		if (key != null && key.length() > 0) {
			builder.append("key=").append(key);
		} else if (builder.length() > 1) {
			builder.deleteCharAt(builder.length() - 1);
		}

		return MD5Util.encode(builder.toString());
	}

}
