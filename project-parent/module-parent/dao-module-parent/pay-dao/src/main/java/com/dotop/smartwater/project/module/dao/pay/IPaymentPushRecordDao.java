package com.dotop.smartwater.project.module.dao.pay;

import com.dotop.smartwater.dependence.core.common.BaseDao;
import com.dotop.smartwater.project.module.core.pay.dto.PaymentPushRecordDto;
import com.dotop.smartwater.project.module.core.pay.vo.PaymentPushRecordVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.dao.DataAccessException;

import java.util.List;

/**

 */
public interface IPaymentPushRecordDao extends BaseDao<PaymentPushRecordDto, PaymentPushRecordVo> {

    @Override
    void add(PaymentPushRecordDto paymentPushRecordDto) throws DataAccessException;

    @Override
    Integer edit(PaymentPushRecordDto paymentPushRecordDto) throws DataAccessException;

    @Override
    PaymentPushRecordVo get(PaymentPushRecordDto paymentPushRecordDto) throws DataAccessException;

    @Override
    List<PaymentPushRecordVo> list(PaymentPushRecordDto paymentPushRecordDto) throws DataAccessException;

    @Override
    Integer del(PaymentPushRecordDto paymentPushRecordDto) throws DataAccessException;

    List<PaymentPushRecordVo> getFailList(@Param("status") String status, @Param("minutes") Integer minutes);
}
