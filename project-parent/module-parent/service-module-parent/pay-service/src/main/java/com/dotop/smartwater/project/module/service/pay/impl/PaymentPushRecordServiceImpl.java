package com.dotop.smartwater.project.module.service.pay.impl;

import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.utils.BeanUtils;
import com.dotop.smartwater.dependence.core.utils.UuidUtils;
import com.dotop.smartwater.project.module.core.pay.bo.PaymentPushRecordBo;
import com.dotop.smartwater.project.module.core.pay.dto.PaymentPushRecordDto;
import com.dotop.smartwater.project.module.core.pay.vo.PaymentPushRecordVo;
import com.dotop.smartwater.project.module.dao.pay.IPaymentPushRecordDao;
import com.dotop.smartwater.project.module.service.pay.IPaymentPushRecordService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;


/**

 */
@Service
public class PaymentPushRecordServiceImpl implements IPaymentPushRecordService {

    private static final Logger LOGGER = LogManager.getLogger(PaymentPushRecordServiceImpl.class);

    @Autowired
    private IPaymentPushRecordDao iPaymentPushRecordDao;

    @Override
    public PaymentPushRecordVo add(PaymentPushRecordBo paymentPushRecordBo) {
        try {
            PaymentPushRecordDto paymentPushRecordDto = BeanUtils.copy(paymentPushRecordBo, PaymentPushRecordDto.class);
            paymentPushRecordDto.setCtime(new Date());
            paymentPushRecordDto.setPushId(UuidUtils.getUuid());
            iPaymentPushRecordDao.add(paymentPushRecordDto);
            return BeanUtils.copy(paymentPushRecordDto, PaymentPushRecordVo.class);

        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public PaymentPushRecordVo edit(PaymentPushRecordBo paymentPushRecordBo) {
        try {
            PaymentPushRecordDto paymentPushRecordDto = BeanUtils.copy(paymentPushRecordBo, PaymentPushRecordDto.class);
            iPaymentPushRecordDao.edit(paymentPushRecordDto);
            return BeanUtils.copy(paymentPushRecordDto, PaymentPushRecordVo.class);

        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public PaymentPushRecordVo get(PaymentPushRecordBo paymentPushRecordBo) {
        try {
            PaymentPushRecordDto paymentPushRecordDto = BeanUtils.copy(paymentPushRecordBo, PaymentPushRecordDto.class);
            return iPaymentPushRecordDao.get(paymentPushRecordDto);

        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }


    @Override
    public List<PaymentPushRecordVo> getFailList(String status, Integer minutes) {
        try {
            return iPaymentPushRecordDao.getFailList(status, minutes);
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }
}
