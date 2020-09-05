package com.dotop.smartwater.project.module.service.revenue;

import com.dotop.smartwater.dependence.core.common.BaseService;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.water.bo.PaymentTradeExtraBo;
import com.dotop.smartwater.project.module.core.water.bo.PaymentTradeOrderBo;
import com.dotop.smartwater.project.module.core.water.dto.PaymentTradeOrderDto;
import com.dotop.smartwater.project.module.core.water.dto.PaymentTradeOrderExtDto;
import com.dotop.smartwater.project.module.core.water.model.PayCallBack;
import com.dotop.smartwater.project.module.core.water.vo.CouponVo;
import com.dotop.smartwater.project.module.core.water.vo.PaymentTradeExtraVo;
import com.dotop.smartwater.project.module.core.water.vo.PaymentTradeOrderVo;

import java.util.List;

/**

 * @date 2019年8月5日
 */
public interface IPaymentTradeOrderService extends BaseService<PaymentTradeOrderBo, PaymentTradeOrderVo> {

    @Override
    PaymentTradeOrderVo add(PaymentTradeOrderBo paymentTradeOrderBo);

    @Override
    PaymentTradeOrderVo edit(PaymentTradeOrderBo paymentTradeOrderBo);

    @Override
    PaymentTradeOrderVo get(PaymentTradeOrderBo paymentTradeOrderBo);

    @Override
    String del(PaymentTradeOrderBo paymentTradeOrderBo);

    @Override
    List<PaymentTradeOrderVo> list(PaymentTradeOrderBo paymentTradeOrderBo);

    @Override
    Pagination<PaymentTradeOrderVo> page(PaymentTradeOrderBo paymentTradeOrderBo);

    List<PaymentTradeOrderVo> findListByIds(List<String> tradeIds);

    void updateList(List<PaymentTradeOrderDto> paymentTradeOrderDtoList);

    void handleError(PayCallBack payCallBack, String msg);

    void addExtra(PaymentTradeExtraBo paymentTradeExtraBo);

    PaymentTradeExtraVo getExtra(String extraid);

    void handleBalanceAndCoupon(String merge,String ownerid, Double ownerBalance, CouponVo coupon);

    PaymentTradeOrderVo findByEidAndTradeNum(PaymentTradeOrderBo paymentTradeOrderBo);

    void batchAdd(List<PaymentTradeOrderDto> dtoList, List<PaymentTradeOrderExtDto> extDtoList);
}
