package com.dotop.smartwater.project.module.dao.pay;

import com.dotop.smartwater.dependence.core.common.BaseDao;
import com.dotop.smartwater.project.module.core.pay.dto.PaymentCallbackDto;
import com.dotop.smartwater.project.module.core.pay.vo.PaymentCallbackVo;
import org.springframework.dao.DataAccessException;

import java.util.List;

/**

 */
public interface IPaymentCallbackDao extends BaseDao<PaymentCallbackDto, PaymentCallbackVo> {

    @Override
    void add(PaymentCallbackDto paymentPushRecordDto) throws DataAccessException;

    @Override
    Integer edit(PaymentCallbackDto paymentPushRecordDto) throws DataAccessException;

    @Override
    PaymentCallbackVo get(PaymentCallbackDto paymentPushRecordDto) throws DataAccessException;

    @Override
    List<PaymentCallbackVo> list(PaymentCallbackDto paymentPushRecordDto) throws DataAccessException;

    @Override
    Integer del(PaymentCallbackDto paymentPushRecordDto) throws DataAccessException;
    
}
