package com.dotop.smartwater.project.module.dao.pay;

import com.dotop.smartwater.dependence.core.common.BaseDao;
import com.dotop.smartwater.project.module.core.pay.dto.PaymentResultDto;
import com.dotop.smartwater.project.module.core.pay.vo.PaymentResultVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.dao.DataAccessException;

import java.util.List;


/**

 */
public interface IPaymentResultDao extends BaseDao<PaymentResultDto, PaymentResultVo> {

    @Override
    void add(PaymentResultDto paymentResultDto) throws DataAccessException;

    @Override
    Integer edit(PaymentResultDto paymentResultDto) throws DataAccessException;

    @Override
    PaymentResultVo get(PaymentResultDto paymentResultDto) throws DataAccessException;

    @Override
    List<PaymentResultVo> list(PaymentResultDto paymentResultDto) throws DataAccessException;

    @Override
    Integer del(PaymentResultDto paymentResultDto) throws DataAccessException;

    PaymentResultVo findByPayId(@Param("payid")String payid) throws DataAccessException;

}
