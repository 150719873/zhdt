package com.dotop.smartwater.project.module.core.pay.constants;

/**
 * @program: project-parent
 * @description:

 * @create: 2019-07-22 10:52
 **/
public class PayConstants {

    public static final String WECHAT_CLOSE_ORDER_URL = "https://api.mch.weixin.qq.com/pay/closeorder";


    public static final String PUSH_STATUS_SUCCESS = "success";
    public static final String PUSH_STATUS_FAIL = "fail";

    public static String WeChatNotifyUrl;
    public static final String WECHAT_PAY_TYPE_JSAPI = "JSAPI";
    public static final String WECHAT_PAY_TYPE_NATIVE = "NATIVE";

    public static final String WECHAT_ORDER_STATUS_SUCCESS = "ORDER_SUCCESS";
    public static final String WECHAT_ORDER_STATUS_FAIL = "ORDER_FAIL";

    public static final String WECHAT_ORDER_STATUS_SUCCESS_MSG = "下单成功";

    public static final String WECHAT_ORDER_NOT_EXIST = "ORDER_NOT_EXIST";
    public static final String WECHAT_ORDER_NOT_EXIST_MSG = "订单不存在";

    public static final String WECHAT_ORDER_EXIST = "ORDER_EXIST";
    public static final String WECHAT_ORDER_EXIST_MSG = "业务单号已存在,请不要重复下单";

    public static final String WECHAT_ORDER_COMPLETE = "ORDER_COMPLETE";
    public static final String WECHAT_ORDER_COMPLETE_MSG = "订单已完成,请调用查询接口查询支付状态";

    public static final String WECHAT_ORDER_CANCEL_SUCCESS = "ORDER_CANCEL_SUCCESS";
    public static final String WECHAT_ORDER_CANCEL_SUCCESS_MSG = "订单关闭成功";

    public static final String WECHAT_ORDER_CANCEL_FAIL = "ORDER_CANCEL_FAIL";


    public static final String RESULT_RESPONSE_SUCCESS_CODE = "0";
    public static final String RESULT_RESPONSE_ERROR_CODE = "1";

    // 现金
    public static final String PAYTYPE_MONEY = "1";
    // 微信
    public static final String PAYTYPE_WEIXIN = "2";
    // 支付宝
    public static final String PAYTYPE_ALIPAY = "3";
    // 微信刷卡支付
    public static final String PAYTYPE_PAYCARD = "4";
    // 微信二维码支付
    public static final String PAYTYPE_QRCODE = "5";

    /**
     * 交易
     */

    public static final String TRADE_PAYSTATUS_SUCCESS = "PAYSTATUS_SUCCESS";
    public static final String TRADE_PAYSTATUS_SUCCESS_MSG = "支付成功";


    public static final String TRADE_PAYSTATUS_ERROR = "PAYSTATUS_ERROR";
    public static final String TRADE_PAYSTATUS_ORDERCANCEL = "订单已经撤销";
    public static final String TRADE_PAYSTATUS_TIMEOUT = "订单已经超时";

    public static final String TRADE_PAYSTATUS_IN = "PAYSTATUS_IN";
    public static final String TRADE_PAYSTATUS_IN_MSG = "等待支付";

}
