package com.dotop.smartwater.project.module.api.revenue;

import com.dotop.smartwater.dependence.core.common.BaseFactory;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.pay.vo.PushVo;
import com.dotop.smartwater.project.module.core.water.form.PaymentTradeOrderExtForm;
import com.dotop.smartwater.project.module.core.water.form.PaymentTradeOrderForm;
import com.dotop.smartwater.project.module.core.water.vo.PaymentTradeOrderDetailVo;
import com.dotop.smartwater.project.module.core.water.vo.PaymentTradeOrderVo;
import com.dotop.smartwater.project.module.core.water.vo.customize.LadderPriceDetailVo;

import java.util.List;
import java.util.Map;

/**
 * 营业厅接口
 *
 * @program: project-parent
 * @description: 营业厅接口

 * @create: 2019-08-06 09:23
 **/

public interface IPaymentFactory extends BaseFactory<PaymentTradeOrderForm, PaymentTradeOrderVo> {


    @Override
    PaymentTradeOrderVo add(PaymentTradeOrderForm paymentTradeOrderForm);

    void batchAdd(List<PaymentTradeOrderForm> list);

    @Override
    Pagination<PaymentTradeOrderVo> page(PaymentTradeOrderForm paymentTradeOrderForm);

    Map<String, String> pay(PaymentTradeOrderExtForm ext);

    void handleCallBack(PushVo pushVo);

    Map<String, String> cancel(PaymentTradeOrderForm paymentTradeOrderForm);

    Map<String, String> query(PaymentTradeOrderForm paymentTradeOrderForm);

    List<LadderPriceDetailVo> detail(PaymentTradeOrderForm paymentTradeOrderForm);

    PaymentTradeOrderDetailVo orderTradeDetail(PaymentTradeOrderForm paymentTradeOrderForm);

    Map<String,String> recharge(PaymentTradeOrderExtForm ext);

    void handleRecharge(String ownerId, String rechargeMoney, String tradeNumber);


    void receive();
}
