package com.dotop.smartwater.project.server.wechat.rest.service;

import java.util.Arrays;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.dotop.smartwater.project.module.api.revenue.IPaymentFactory;
import com.dotop.smartwater.project.module.core.auth.wechat.WechatAuthClient;
import com.dotop.smartwater.project.module.core.pay.constants.PayConstants;
import com.dotop.smartwater.project.module.core.water.form.PaymentTradeOrderExtForm;
import com.dotop.smartwater.project.module.core.water.form.PaymentTradeOrderForm;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.dotop.smartwater.dependence.core.common.BaseController;
import com.dotop.smartwater.dependence.core.common.BaseForm;
import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.utils.VerificationUtils;
import com.dotop.smartwater.project.module.api.wechat.IWechatPayFactory;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.module.core.water.form.customize.OrderPayParamForm;
import com.dotop.smartwater.project.module.core.water.form.customize.WechatParamForm;
import com.dotop.smartwater.project.module.core.water.vo.customize.JSApiTicketVo;

/**
 * 缴费相关 整合了 原来微信的PayController 和 PayConfigController
 *

 * @date 2019年3月22日
 */
@RestController()
@RequestMapping("/WechatPay")

public class WechatPayController implements BaseController<BaseForm> {

    private static final Logger logger = LogManager.getLogger(WechatPayController.class);

    @Autowired
    private IWechatPayFactory iWechatPayFactory;

    @Autowired
    private IPaymentFactory iPaymentFactory;

    /**
     * 发起支付前获取微信支付的配置信息，orderConfigParam的url是当前页面的url地址
     *
     * @param request
     * @param
     * @return
     */
    @PostMapping(value = "/Config/JSApiTicket", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    public String jSApiTicket(HttpServletRequest request) {
        JSApiTicketVo jSApiTicketVo = iWechatPayFactory.jSApiTicket(request);
        return resp(ResultCode.Success, ResultCode.SUCCESS, jSApiTicketVo);
    }

    /**
     * 检测支付前是否有账单锁定代金券、余额等信息 如果锁定则撤销锁定
     *
     * @param request
     * @return
     */
    @PostMapping(value = "/checkOrderStatus", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    public String checkOrderStatus(HttpServletRequest request, @RequestBody WechatParamForm wechatParamForm) {
        logger.info(LogMsg.to("msg:", "是否有账单锁定代金券、余额等 功能开始", "参数:", wechatParamForm));
        wechatParamForm.setRequest(request);
        // 测试代码注释掉
        iWechatPayFactory.checkOrderStatus(wechatParamForm);
        logger.info(LogMsg.to("msg:", "是否有账单锁定代金券、余额等 功能结束"));
        return resp(ResultCode.Success, ResultCode.SUCCESS, null);
    }

    /**
     * 缴费动作
     *
     * @param request
     * @param
     * @return
     */
    @PostMapping(value = "/orderPayment", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    public String orderPayment(HttpServletRequest request, @RequestBody OrderPayParamForm orderPayParamForm) {
        logger.info(LogMsg.to("msg:", "开始缴费", "orderPayParamForm", orderPayParamForm));
        Double balance = orderPayParamForm.getBalance();
        String couponid = orderPayParamForm.getCouponid();
        Double realamount = orderPayParamForm.getRealamount();
        String ids = orderPayParamForm.getOrderids();
        VerificationUtils.doubles("balance", balance);
        VerificationUtils.string("couponid", couponid);
        VerificationUtils.doubles("realamount", realamount);
        if(StringUtils.isBlank(ids)){
            return resp(ResultCode.Fail, "参数错误", null);
        }
        if (realamount < 0) {
            return resp(ResultCode.Fail, "实收金额不能为负数", null);
        }

        //公众号缴费没有找零
        PaymentTradeOrderExtForm form = new PaymentTradeOrderExtForm();
        form.setTradeIds(Arrays.asList(orderPayParamForm.getOrderids().split(",")));
        form.setOwnerid(WechatAuthClient.get().getOwnerid());
        form.setCouponid("0".equals(couponid) ? null : couponid);
        form.setEnterpriseid(WechatAuthClient.get().getEnterpriseid());
        form.setBalance(String.valueOf(balance));
        form.setActualamount(String.valueOf(realamount));
        if (realamount > 0) {
            form.setMode(PayConstants.PAYTYPE_WEIXIN);
            form.setOpenid(WechatAuthClient.get().getOpenid());
        } else {
            form.setMode(PayConstants.PAYTYPE_MONEY);
        }

        Map<String, String> result = iPaymentFactory.pay(form);

        logger.info(LogMsg.to("msg:", "缴费结束", "map", result));
        return resp(ResultCode.Success, ResultCode.SUCCESS, result);
    }

    /**
     * 前端H5页面请求微信成功后，需要回调这个接口再次查询订单状态
     *
     * @param request
     * @param wechatParamForm
     * @return
     */
    @PostMapping(value = "/orderQuery", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    public String orderQuery(@RequestBody WechatParamForm wechatParamForm) {
        logger.info(LogMsg.to("msg:", "前端回调再次查询工单状态开始", "wechatParamForm", wechatParamForm));
        String tradeno = wechatParamForm.getTradeno();
        VerificationUtils.string("tradeno", tradeno);

        PaymentTradeOrderForm form = new PaymentTradeOrderForm();
        form.setEnterpriseid(WechatAuthClient.get().getEnterpriseid());
        form.setTradeNumber(wechatParamForm.getTradeno());
        Map<String, String> result = iPaymentFactory.query(form);

        logger.info(LogMsg.to("msg:", "前端回调再次查询工单状态结束"));
        return resp(ResultCode.Success, ResultCode.SUCCESS, result);
    }

    /**
     * 前端支付返回错误信息，请求进行销单
     *
     * @param request
     * @param wechatParamForm
     * @return
     */
    @PostMapping(value = "/orderError", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    public String orderError(@RequestBody WechatParamForm wechatParamForm) {
        logger.info(LogMsg.to("msg:", "是否有账单锁定代金券、余额等 功能开始", "wechatParamForm", wechatParamForm));
        String tradeno = wechatParamForm.getTradeno();
        VerificationUtils.string("tradeno", tradeno);


        PaymentTradeOrderForm form = new PaymentTradeOrderForm();
        form.setEnterpriseid(WechatAuthClient.get().getEnterpriseid());
        form.setTradeNumber(wechatParamForm.getTradeno());
        Map<String, String> result = iPaymentFactory.cancel(form);

        return resp(ResultCode.Success, ResultCode.SUCCESS, result);
    }

}
