package com.dotop.smartwater.project.module.dao.pay;

import com.dotop.smartwater.dependence.core.common.BaseDao;
import com.dotop.smartwater.project.module.core.pay.dto.PaymentOrderDto;
import com.dotop.smartwater.project.module.core.pay.vo.PaymentOrderVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.dao.DataAccessException;

import java.util.List;


/**

 */
public interface IPaymentOrderDao extends BaseDao<PaymentOrderDto, PaymentOrderVo> {

    @Override
    void add(PaymentOrderDto paymentOrderDto) throws DataAccessException;

    @Override
    Integer edit(PaymentOrderDto paymentOrderDto) throws DataAccessException;

    @Override
    PaymentOrderVo get(PaymentOrderDto paymentOrderDto) throws DataAccessException;

    @Override
    List<PaymentOrderVo> list(PaymentOrderDto paymentOrderDto) throws DataAccessException;

    @Override
    Integer del(PaymentOrderDto paymentOrderDto) throws DataAccessException;

    PaymentOrderVo findByTradeNumberAndEid(@Param("tradeNumber") String tradeNumber,
                                     @Param("enterpriseid") String enterpriseid);

    List<PaymentOrderVo> getPayInList(@Param("status")String status,
                                      @Param("minutes")Integer minutes);
}
