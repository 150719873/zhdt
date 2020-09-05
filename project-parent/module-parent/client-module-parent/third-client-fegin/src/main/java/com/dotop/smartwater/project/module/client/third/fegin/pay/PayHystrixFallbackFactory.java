package com.dotop.smartwater.project.module.client.third.fegin.pay;

import com.dotop.smartwater.dependence.core.utils.JSONUtils;
import com.dotop.smartwater.project.module.core.third.bo.pay.PaymentOrderBo;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import feign.hystrix.FallbackFactory;
import java.util.HashMap;
import java.util.Map;

/**

 */
public class PayHystrixFallbackFactory implements FallbackFactory<IPayFeginClient> {

    @Override
    public IPayFeginClient create(Throwable ex) {
        return new IPayFeginClient() {

            @Override
            public String submit(PaymentOrderBo paymentOrderBo) {
                Map<Object, Object> params = new HashMap<>(3);
                params.put("code", ResultCode.Fail);
                params.put("msg", "支付系统提交订单出错");
                params.put("data", null);
                return JSONUtils.toJSONString(params);
            }

            @Override
            public String ordercancel(PaymentOrderBo paymentOrderBo) {
                Map<Object, Object> params = new HashMap<>(3);
                params.put("code", ResultCode.Fail);
                params.put("msg", "支付系统撤销出错");
                params.put("data", null);
                return JSONUtils.toJSONString(params);
            }

            @Override
            public String orderquery(PaymentOrderBo paymentOrderBo) {
                Map<Object, Object> params = new HashMap<>(3);
                params.put("code", ResultCode.Fail);
                params.put("msg", "支付系统查询出错");
                params.put("data", null);
                return JSONUtils.toJSONString(params);
            }
        };
    }

}
