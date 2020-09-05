package com.dotop.smartwater.project.server.water.rest.service.payment;

import com.dotop.smartwater.dependence.core.common.BaseController;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.JSONUtils;
import com.dotop.smartwater.dependence.core.utils.VerificationUtils;
import com.dotop.smartwater.project.module.api.revenue.IPaymentFactory;
import com.dotop.smartwater.project.module.core.pay.constants.PayConstants;
import com.dotop.smartwater.project.module.core.pay.vo.PushVo;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.module.core.water.form.PaymentTradeOrderExtForm;
import com.dotop.smartwater.project.module.core.water.form.PaymentTradeOrderForm;
import com.dotop.smartwater.project.module.core.water.vo.PaymentTradeOrderVo;
import com.dotop.smartwater.project.server.water.common.FoundationController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @program: project-parent
 * @description: 营业厅功能

 * @create: 2019-08-01 14:28
 **/
@RestController

@RequestMapping("/payment")
public class PaymentController extends FoundationController implements BaseController<PaymentTradeOrderForm> {

    @Autowired
    private IPaymentFactory paymentFactory;

    private static final Logger LOGGER = LogManager.getLogger(PaymentController.class);

    /**
     * 账单组成明细
     * @param paymentTradeOrderForm
     * @return
     */
    @PostMapping(value = "/detail", produces = GlobalContext.PRODUCES)
    public String detail(@RequestBody PaymentTradeOrderForm paymentTradeOrderForm) {
        // 数据封装
        VerificationUtils.string("tradeNumber", paymentTradeOrderForm.getTradeNumber());
        return resp(ResultCode.Success, ResultCode.SUCCESS, paymentFactory.detail(paymentTradeOrderForm));
    }

    /**
     * 账单详情
     * @param paymentTradeOrderForm
     * @return
     */
    @PostMapping(value = "/orderTradeDetail", produces = GlobalContext.PRODUCES)
    public String orderTradeDetail(@RequestBody PaymentTradeOrderForm paymentTradeOrderForm) {
        // 数据封装
        VerificationUtils.string("tradeid", paymentTradeOrderForm.getTradeid());
        return resp(ResultCode.Success, ResultCode.SUCCESS, paymentFactory.orderTradeDetail(paymentTradeOrderForm));
    }

    @Override
    @PostMapping(value = "/page", produces = GlobalContext.PRODUCES)
    public String page(@RequestBody PaymentTradeOrderForm paymentTradeOrderForm) {
        // 数据封装
        Pagination<PaymentTradeOrderVo> pagination = paymentFactory.page(paymentTradeOrderForm);
        return resp(ResultCode.Success, ResultCode.SUCCESS, pagination);
    }

    @Override
    @PostMapping(value = "/add", produces = GlobalContext.PRODUCES)
    public String add(@RequestBody PaymentTradeOrderForm paymentTradeOrderForm) {

        // 数据封装
        VerificationUtils.string("amount", paymentTradeOrderForm.getAmount());
        VerificationUtils.string("businessid", paymentTradeOrderForm.getBusinessId());
        VerificationUtils.string("enterpriseid", paymentTradeOrderForm.getEnterpriseid());
        VerificationUtils.string("tradeNumber", paymentTradeOrderForm.getTradeNumber());
        VerificationUtils.string("tradeName", paymentTradeOrderForm.getTradeName());
        VerificationUtils.string("userName", paymentTradeOrderForm.getUserName());
        VerificationUtils.string("userPhone", paymentTradeOrderForm.getUserPhone());
        PaymentTradeOrderVo vo = paymentFactory.add(paymentTradeOrderForm);
        return resp(ResultCode.Success, ResultCode.SUCCESS, vo);
    }

    @Override
    @PostMapping(value = "/del", produces = GlobalContext.PRODUCES)
    public String del(@RequestBody PaymentTradeOrderForm paymentTradeOrderForm) {
        LOGGER.info("[del] : " + JSONUtils.toJSONString(paymentTradeOrderForm));

        // 数据封装
        VerificationUtils.string("tradeid", paymentTradeOrderForm.getTradeid());
        paymentFactory.del(paymentTradeOrderForm);
        return resp(ResultCode.Success, ResultCode.SUCCESS, null);
    }


    @PostMapping(value = "/submit", produces = GlobalContext.PRODUCES)
    public String submit(@RequestBody PaymentTradeOrderExtForm ext) {
        LOGGER.info("[submit] : " + JSONUtils.toJSONString(ext));

        // 数据封装
        VerificationUtils.string("actualamount", ext.getActualamount());
        VerificationUtils.strList("tradeIds", ext.getTradeIds());
        VerificationUtils.string("change", ext.getChange());
        VerificationUtils.string("mode", ext.getMode());
        VerificationUtils.string("enterpriseid", ext.getEnterpriseid());

        LOGGER.info(LogMsg.to("msg:", "submit参数", "ext", ext));

        if (PayConstants.PAYTYPE_PAYCARD.equals(ext.getMode())) {
            VerificationUtils.string("payCard", ext.getPayCard());
        }
        if (PayConstants.PAYTYPE_WEIXIN.equals(ext.getMode())) {
            VerificationUtils.string("openid", ext.getOpenid());
        }

        return resp(ResultCode.Success, ResultCode.SUCCESS, paymentFactory.pay(ext));
    }

    @PostMapping(value = "/callback", produces = GlobalContext.PRODUCES)
    public String callback(@RequestBody PushVo pushVo) {
        // 数据封装
        LOGGER.info(JSONUtils.toJSONString(pushVo));
        paymentFactory.handleCallBack(pushVo);
        return "0";
    }


    /**
     * 撤销账单
     * @param paymentTradeOrderForm
     * @return
     */
    @PostMapping(value = "/cancel", produces = GlobalContext.PRODUCES)
    public String cancel(@RequestBody PaymentTradeOrderForm paymentTradeOrderForm) {
        LOGGER.info("[cancel] : " + JSONUtils.toJSONString(paymentTradeOrderForm));

        // 数据封装
        VerificationUtils.string("tradeNumber", paymentTradeOrderForm.getTradeNumber());
        VerificationUtils.string("enterpriseid", paymentTradeOrderForm.getEnterpriseid());


        return resp(ResultCode.Success, ResultCode.SUCCESS, paymentFactory.cancel(paymentTradeOrderForm));
    }

    /**
     * 查询账单
     * @param paymentTradeOrderForm
     * @return
     */
    @PostMapping(value = "/query", produces = GlobalContext.PRODUCES)
    public String query(@RequestBody PaymentTradeOrderForm paymentTradeOrderForm) {
        // 数据封装
        VerificationUtils.string("tradeNumber", paymentTradeOrderForm.getTradeNumber());
        VerificationUtils.string("enterpriseid", paymentTradeOrderForm.getEnterpriseid());


        return resp(ResultCode.Success, ResultCode.SUCCESS, paymentFactory.query(paymentTradeOrderForm));
    }

    @PostMapping(value = "/recharge", produces = GlobalContext.PRODUCES)
    public String recharge(@RequestBody PaymentTradeOrderExtForm ext) {
        // 数据封装
        VerificationUtils.string("amount", ext.getAmount());
        VerificationUtils.string("ownerId", ext.getOwnerid());


        VerificationUtils.string("actualamount", ext.getActualamount());
        VerificationUtils.string("change", ext.getChange());
        VerificationUtils.string("mode", ext.getMode());
        VerificationUtils.string("enterpriseid", ext.getEnterpriseid());

        if(Double.parseDouble(ext.getActualamount()) < Double.parseDouble(ext.getAmount())){
            throw new FrameworkRuntimeException(ResultCode.ParamIllegal,"实收金额不能少于充值金额");
        }

        LOGGER.info(LogMsg.to("msg:", "recharge", "ext", ext));

        if (PayConstants.PAYTYPE_PAYCARD.equals(ext.getMode())) {
            VerificationUtils.string("payCard", ext.getPayCard());
        }
        if (PayConstants.PAYTYPE_WEIXIN.equals(ext.getMode())) {
            VerificationUtils.string("openid", ext.getOpenid());
        }


        Map<String, String> result = paymentFactory.recharge(ext);

        return resp(ResultCode.Success, ResultCode.SUCCESS, result);
    }
}
