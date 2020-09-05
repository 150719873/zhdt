package com.dotop.smartwater.project.module.api.pay.mode;

import com.dotop.smartwater.dependence.core.utils.JSONUtils;
import com.dotop.smartwater.project.module.core.pay.constants.PayConstants;
import com.dotop.smartwater.project.module.core.pay.form.WeChatParam;
import com.dotop.smartwater.project.module.core.pay.wechat.MessageManager;
import com.dotop.smartwater.project.module.core.pay.wechat.WXPayConstants;
import com.dotop.smartwater.project.module.core.pay.wechat.WXPayUtil;
import com.dotop.smartwater.project.module.core.water.vo.customize.WechatPublicSettingVo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static com.dotop.smartwater.project.module.core.pay.wechat.WXPayUtil.*;
import static com.dotop.smartwater.project.module.core.pay.wechat.WXReques.sendPostXMLRequest;

/**
 * @program: project-parent
 * @description: 微信刷卡支付

 * @create: 2019-07-22 11:14
 **/
@Component
public class WeChatCom {

    private static final Logger LOGGER = LogManager.getLogger(WeChatCom.class);

    /**
     * 查询账单是否已经支付
     */
    public static Map<String, String> queryOrderOnce(WechatPublicSettingVo weixinConfig, Map<String, String> reqData) {
        LOGGER.info("[queryOrderOnce] 请求数据 ：" + JSONUtils.toJSONString(reqData));
        Map<String, String> lastResult = requestWechat(weixinConfig.getOrderqueryurl(), reqData);
        LOGGER.info("[queryOrderOnce] 返回数据 ：" + JSONUtils.toJSONString(lastResult));
        return lastResult;
    }

    /**
     * 关闭订单
     */
    public static Map<String, String> cancelOrder(Map<String, String> reqData) {
        LOGGER.info("[cancelOrder] 请求数据 ：" + JSONUtils.toJSONString(reqData));
        Map<String, String> lastResult = requestWechat(PayConstants.WECHAT_CLOSE_ORDER_URL, reqData);
        LOGGER.info("[cancelOrder] 返回数据 ：" + JSONUtils.toJSONString(lastResult));
        return lastResult;
    }

    /**
     * 刷卡支付-多次查询账单是否已经支付
     */
    public static Map<String, String> queryOrderTimes(WechatPublicSettingVo weixinConfig, Map<String, String> reqData) throws InterruptedException {
        Map<String, String> resultMap = new HashMap<>(10);
        long time = 40 * 1000;
        long start = System.currentTimeMillis();
        long end = System.currentTimeMillis() + time;
        while (System.currentTimeMillis() - start < time) {
            LOGGER.info("[queryOrderTimes] 请求数据 ：" + JSONUtils.toJSONString(reqData));
            Map<String, String> lastResult = requestWechat(weixinConfig.getOrderqueryurl(), reqData);
            LOGGER.info("[queryOrderTimes] 返回数据 ：" + JSONUtils.toJSONString(lastResult));

            String returnCode = lastResult.get(MessageManager.RESP_RETURN_CODE);
            if (returnCode.equals(MessageManager.SUCCESS)) {
                String resultCode = lastResult.get(MessageManager.RESP_RESULT_CODE);
                String tradeState = lastResult.get(MessageManager.RESP_TRADE_STATE);
                if (resultCode.equals(MessageManager.SUCCESS) && tradeState.equals(MessageManager.SUCCESS)) {
                    return lastResult;
                } else {
                    // 看错误码，若支付结果未知，则重试提交刷卡支付
                    if (tradeState.equals(MessageManager.SYSTEMERROR) || tradeState.equals(MessageManager.BANKERROR)
                            || tradeState.equals(MessageManager.USERPAYING)) {

                        /***如果查询时间超过30秒，则撤销账单
                         现在是微信自动撤销,如果等待时间小于30秒，则需要手动调用微信撤单接口取消订单*/
                        if (end - System.currentTimeMillis() <= 3000) {
                            // 撤销账单
                            resultMap.put(MessageManager.RESP_RESULT_CODE, MessageManager.FAIL);
                            resultMap.put(MessageManager.RESP_ERR_CODE_DES, "支付超时已撤销，请重新扫码提交");
                            break;
                        } else {
                            LOGGER.info("microPayWithPos: try micropay again");
                            Thread.sleep(3000);
                            continue;
                        }
                    } else {
                        LOGGER.error("[queryOrderTimes] tradeState ：" + tradeState);
                        LOGGER.error("[queryOrderTimes] MSG ：" + lastResult.get(MessageManager.RESP_ERR_CODE_DES));
                        resultMap.put(MessageManager.RESP_RESULT_CODE, MessageManager.FAIL);
                        resultMap.put(MessageManager.RESP_ERR_CODE_DES, "该笔订单支付失败");

                        //TODO 撤单
                        break;
                    }
                }
            } else {
                break;
            }
        }
        return resultMap;
    }


    /**
     * 微信预下单
     *
     * @param weChatParam
     * @param weixinConfig
     * @return
     */
    public static Map<String, String> unifiedorder(WeChatParam weChatParam, WechatPublicSettingVo weixinConfig) {
        Map<String, String> orderMap = new HashMap<>(20);
        Map<String, String> resultMap = new HashMap<>(20);
        InputStream inputStream = null;
        try {
            BigDecimal money = new BigDecimal(weChatParam.getAmount());
            BigDecimal times = new BigDecimal("100");
            BigDecimal amount = money.multiply(times);

            String nonceStr = generateNonceStr();
            orderMap.put("appid", weixinConfig.getAppid());
            orderMap.put("mch_id", weixinConfig.getMchid());
            orderMap.put("nonce_str", nonceStr);
            String body = "公众号缴费" + weChatParam.getAmount() + "元";
            orderMap.put("body", body);
            orderMap.put("out_trade_no", weChatParam.getOutTradeNo());
            orderMap.put("total_fee", String.valueOf(amount.intValue()));
            orderMap.put("spbill_create_ip", weChatParam.getIp());

            // 微信OpenId
            orderMap.put("openid", weChatParam.getOpenid());
            orderMap.put("notify_url", PayConstants.WeChatNotifyUrl);

            // 微信公众号支付
            orderMap.put("trade_type", PayConstants.WECHAT_PAY_TYPE_JSAPI);
            orderMap.put("attach", weChatParam.getEnterpriseid() + "|" + weChatParam.getResId());


            // 最后签名
            String sign = generateSignature(orderMap, weixinConfig.getPaysecret());
            orderMap.put("sign", sign);

            LOGGER.info("微信预下单请求参数 : {}", JSONUtils.toJSONString(orderMap));

            inputStream = sendPostXMLRequest(weixinConfig.getUnifiedorderurl(), mapToXml(orderMap));
            resultMap = WXPayUtil.parseXml(inputStream);

            LOGGER.info("微信支付预下单返回resultMap {}", resultMap);

            if (WXPayUtil.isSignatureValid(resultMap, weixinConfig.getPaysecret())) {
                resultMap.put(MessageManager.SIGN_FLAG, "签名正确");
                LOGGER.info(weChatParam.getOutTradeNo() + " 签名正确");
            }

            String return_code = resultMap.get(MessageManager.RESP_RETURN_CODE);
            if (MessageManager.SUCCESS.equals(return_code)) {
                String err_code = resultMap.get(MessageManager.RESP_ERR_CODE);
                String result_code = resultMap.get(MessageManager.RESP_RESULT_CODE);
                if (MessageManager.SUCCESS.equals(result_code)) {
                    String prepayId = resultMap.get(MessageManager.RESP_PREPAY_ID);
                    String appId = resultMap.get(MessageManager.RESP_APP_ID);

                    Map<String, String> signMap = new HashMap<>(10);
                    signMap.put("appId", appId);
                    signMap.put("timeStamp", String.valueOf(System.currentTimeMillis() / 1000));
                    signMap.put("package", "prepay_id=" + prepayId);
                    signMap.put("nonceStr", generateNonceStr());
                    signMap.put("signType", WXPayConstants.MD5);
                    String paySign = generateSignature(signMap, weixinConfig.getPaysecret());
                    signMap.put("paySign", paySign);
                    resultMap.putAll(signMap);

                } else {
                    LOGGER.info(body + " 微信支付预下单失败:" + err_code);
                }
            } else {
                LOGGER.error("微信内部错误返回 ：" + JSONUtils.toJSONString(resultMap));
            }

        } catch (Exception e) {
            LOGGER.error("[Exception]微信支付预下单失败", e);
            resultMap.put(MessageManager.RESP_RETURN_CODE, MessageManager.FAIL);
            resultMap.put(MessageManager.RESP_RETURN_MSG, "微信支付预下单失败");
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


    public static Map<String, String> payByCard(WeChatParam weChatParam, WechatPublicSettingVo weixinConfig) {
        Map<String, String> orderMap = new HashMap<>(20);
        Map<String, String> resultMap = new HashMap<>(20);
        InputStream inputStream = null;
        try {
            BigDecimal money = new BigDecimal(weChatParam.getAmount());
            BigDecimal times = new BigDecimal("100");
            BigDecimal amount = money.multiply(times);

            String nonceStr = generateNonceStr();
            orderMap.put("appid", weixinConfig.getAppid());
            orderMap.put("mch_id", weixinConfig.getMchid());
            orderMap.put("nonce_str", nonceStr);
            String body = "刷卡支付" + weChatParam.getAmount() + "元";
            orderMap.put("body", body);
            orderMap.put("out_trade_no", weChatParam.getOutTradeNo());
            orderMap.put("total_fee", String.valueOf(amount.intValue()));
            orderMap.put("spbill_create_ip", weChatParam.getIp());

            // 微信支付授权码
            orderMap.put("auth_code", weChatParam.getPayCard());
            orderMap.put("attach", weChatParam.getEnterpriseid() + "|" + weChatParam.getResId());

            // 最后签名
            String sign = generateSignature(orderMap, weixinConfig.getPaysecret());
            orderMap.put("sign", sign);

            LOGGER.info("刷卡支付请求参数 : {}", JSONUtils.toJSONString(orderMap));

            inputStream = sendPostXMLRequest(weixinConfig.getPaybycardurl(), mapToXml(orderMap));
            resultMap = WXPayUtil.parseXml(inputStream);

            if (WXPayUtil.isSignatureValid(resultMap, weixinConfig.getPaysecret())) {
                resultMap.put(MessageManager.SIGN_FLAG, "签名正确");
                LOGGER.info(weChatParam.getOutTradeNo() + " 签名正确");
            }

            String return_code = resultMap.get(MessageManager.RESP_RETURN_CODE);
            if (MessageManager.SUCCESS.equals(return_code)) {
                String err_code = resultMap.get(MessageManager.RESP_ERR_CODE);
                String result_code = resultMap.get(MessageManager.RESP_RESULT_CODE);
                // 如果返回结果错误中包含系统超时、银行系统异常、需要输入密码时
                if (MessageManager.FAIL.equals(result_code)) {
                    switch (err_code) {
                        case MessageManager.USERPAYING:
                        case MessageManager.BANKERROR:
                        case MessageManager.SYSTEMERROR:
                            Map<String, String> queryMap = new HashMap<>(10);
                            queryMap.put("appid", orderMap.get("appid"));
                            queryMap.put("mch_id", orderMap.get("mch_id"));
                            queryMap.put("out_trade_no", orderMap.get("out_trade_no"));
                            queryMap.put("nonce_str", generateNonceStr());
                            queryMap.put("sign", generateSignature(queryMap, weixinConfig.getPaysecret()));

                            /*查询账单支付状态*/
                            Map<String, String> resultQuery = queryOrderTimes(weixinConfig, queryMap);

                            //通过查询账单更新状态
                            resultMap.put(MessageManager.RESP_RETURN_CODE, resultQuery.get(MessageManager.RESP_RETURN_CODE));
                            resultMap.put(MessageManager.RESP_RESULT_CODE, resultQuery.get(MessageManager.RESP_RESULT_CODE));
                            resultMap.put(MessageManager.RESP_ERR_CODE, resultQuery.get(MessageManager.RESP_ERR_CODE));
                            resultMap.put(MessageManager.RESP_ERR_CODE_DES, resultQuery.get(MessageManager.RESP_ERR_CODE_DES));
                            break;
                        default:
                            break;
                    }
                } else {
                    LOGGER.info(body + " 支付成功");
                }
            } else {
                LOGGER.error("微信内部错误返回 ：" + JSONUtils.toJSONString(resultMap));
            }

        } catch (Exception e) {
            LOGGER.error("[Exception]微信刷卡支付失败", e);
            resultMap.put(MessageManager.RESP_RETURN_CODE, MessageManager.FAIL);
            resultMap.put(MessageManager.RESP_RETURN_MSG, "支付失败");
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


    public static Map<String, String> payScan(WeChatParam weChatParam, WechatPublicSettingVo weixinConfig) {
        Map<String, String> paramMap = new HashMap<>(20);
        Map<String, String> resultMap = new HashMap<>(20);
        InputStream inputStream = null;

        BigDecimal money = new BigDecimal(weChatParam.getAmount());
        BigDecimal times = new BigDecimal("100");
        BigDecimal amount = money.multiply(times);
        try {
            paramMap.put("appid", weixinConfig.getAppid());
            paramMap.put("mch_id", weixinConfig.getMchid());
            paramMap.put("nonce_str", generateNonceStr());
            String body = weChatParam.getTradeName() + " [" + weChatParam.getAmount() + "元]";
            paramMap.put("body", body);

            paramMap.put("out_trade_no", weChatParam.getOutTradeNo());
            paramMap.put("total_fee", String.valueOf(amount.intValue()));
            paramMap.put("spbill_create_ip", weChatParam.getIp());
            paramMap.put("notify_url", PayConstants.WeChatNotifyUrl);

            // 扫码支付
            paramMap.put("trade_type", PayConstants.WECHAT_PAY_TYPE_NATIVE);
            paramMap.put("attach", weChatParam.getEnterpriseid() + "|" + weChatParam.getResId());

            // 最后签名
            String sign = generateSignature(paramMap, weixinConfig.getPaysecret());
            paramMap.put("sign", sign);


            LOGGER.info("二维码生成请求参数 : {}", JSONUtils.toJSONString(paramMap));

            inputStream = sendPostXMLRequest(weixinConfig.getUnifiedorderurl(), mapToXml(paramMap));
            resultMap = WXPayUtil.parseXml(inputStream);

            if (WXPayUtil.isSignatureValid(resultMap, weixinConfig.getPaysecret())) {
                resultMap.put(MessageManager.SIGN_FLAG, "签名正确");
                LOGGER.info(weChatParam.getOutTradeNo() + " 签名正确");
            }

            LOGGER.info("result : {}", resultMap);

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

}
