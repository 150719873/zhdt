package com.dotop.smartwater.project.server.pay.rest.service.pay;

import com.dotop.smartwater.dependence.core.common.BaseController;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.project.module.api.pay.IPayFactory;
import com.dotop.smartwater.project.module.core.pay.constants.PayConstants;
import com.dotop.smartwater.project.module.core.pay.form.PaymentOrderForm;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 支付平台
 * @date 2019年7月19日 11:12
 */
@RestController

@RequestMapping("/pay")
public class PayController extends Base implements BaseController<PaymentOrderForm> {

    @Autowired
    private IPayFactory iPayFactory;

    @PostMapping(value = "/submit", produces = GlobalContext.PRODUCES)
    public String submit(HttpServletRequest request, @RequestBody PaymentOrderForm paymentOrderForm) {
        String tradeNumber = paymentOrderForm.getTradeNumber();
        String tradeName = paymentOrderForm.getTradeName();
        String amount = paymentOrderForm.getAmount();
        String mode = paymentOrderForm.getMode();
        String enterpriseId = paymentOrderForm.getEnterpriseid();

        // 验证
        string("tradeNumber", tradeNumber);
        string("tradeName", tradeName);
        string("amount", amount);
        string("mode", mode);
        string("enterpriseId", enterpriseId);

        if(mode.equals(PayConstants.PAYTYPE_WEIXIN)){
            string("openid", paymentOrderForm.getOpenid());
        }

        if(mode.equals(PayConstants.PAYTYPE_PAYCARD)){
            string("payCard", paymentOrderForm.getPayCard());
        }

        if(mode.equals(PayConstants.PAYTYPE_MONEY)){
            throw new FrameworkRuntimeException(ResultCode.ParamIllegal, "暂时不支持现金支付");
        }

        String realIP = request.getHeader("X-Real-IP");
        if (StringUtils.isBlank(realIP)) {
            realIP = request.getRemoteAddr();
        }
        return resp(PayConstants.RESULT_RESPONSE_SUCCESS_CODE, ResultCode.SUCCESS, iPayFactory.submit(paymentOrderForm, realIP));
    }


    @PostMapping(value = "/orderquery", produces = GlobalContext.PRODUCES)
    public String orderquery(@RequestBody PaymentOrderForm paymentOrderForm) throws Exception {
        String tradeNumber = paymentOrderForm.getTradeNumber();
        String enterpriseId = paymentOrderForm.getEnterpriseid();
        string("tradeNumber", tradeNumber);
        string("enterpriseId", enterpriseId);
        return resp(PayConstants.RESULT_RESPONSE_SUCCESS_CODE, ResultCode.SUCCESS, iPayFactory.orderquery(paymentOrderForm));
    }

    @PostMapping(value = "/ordercancel", produces = GlobalContext.PRODUCES)
    public String ordercancel(@RequestBody PaymentOrderForm paymentOrderForm) throws Exception {
        String tradeNumber = paymentOrderForm.getTradeNumber();
        String enterpriseId = paymentOrderForm.getEnterpriseid();
        string("tradeNumber", tradeNumber);
        string("enterpriseId", enterpriseId);
        return resp(PayConstants.RESULT_RESPONSE_SUCCESS_CODE, ResultCode.SUCCESS, iPayFactory.ordercancel(paymentOrderForm));
    }
}
