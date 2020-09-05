package com.dotop.smartwater.project.module.service.revenue;

import com.dotop.smartwater.dependence.core.common.BaseService;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.water.bo.PaymentTradeOrderExtBo;
import com.dotop.smartwater.project.module.core.water.vo.PaymentTradeOrderExtVo;

import java.util.List;

/**

 * @date 2019年8月5日
 */
public interface IPaymentTradeOrderExtService extends BaseService<PaymentTradeOrderExtBo, PaymentTradeOrderExtVo> {

    @Override
    PaymentTradeOrderExtVo add(PaymentTradeOrderExtBo paymentTradeOrderExtBo);

    @Override
    PaymentTradeOrderExtVo edit(PaymentTradeOrderExtBo paymentTradeOrderExtBo);

    @Override
    PaymentTradeOrderExtVo get(PaymentTradeOrderExtBo paymentTradeOrderExtBo);

    @Override
    String del(PaymentTradeOrderExtBo paymentTradeOrderExtBo);

    @Override
    List<PaymentTradeOrderExtVo> list(PaymentTradeOrderExtBo paymentTradeOrderExtBo);

    @Override
    Pagination<PaymentTradeOrderExtVo> page(PaymentTradeOrderExtBo paymentTradeOrderExtBo);

    List<PaymentTradeOrderExtVo> findNotPayListByTradeIds(List<String> tradeIds, String enterpriseid);

    void updateList(List<PaymentTradeOrderExtVo> list);

    PaymentTradeOrderExtVo findByTradeid(String tradeid);
}
