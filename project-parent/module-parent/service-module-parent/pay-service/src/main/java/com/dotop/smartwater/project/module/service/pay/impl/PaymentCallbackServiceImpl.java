package com.dotop.smartwater.project.module.service.pay.impl;

import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.utils.BeanUtils;
import com.dotop.smartwater.dependence.core.utils.UuidUtils;
import com.dotop.smartwater.project.module.core.pay.bo.PaymentCallbackBo;
import com.dotop.smartwater.project.module.core.pay.dto.PaymentCallbackDto;
import com.dotop.smartwater.project.module.core.pay.vo.PaymentCallbackVo;
import com.dotop.smartwater.project.module.dao.pay.IPaymentCallbackDao;
import com.dotop.smartwater.project.module.service.pay.IPaymentCallbackService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.Date;


/**

 */
@Service
public class PaymentCallbackServiceImpl implements IPaymentCallbackService {

	private static final Logger LOGGER = LogManager.getLogger(PaymentCallbackServiceImpl.class);

	@Autowired
	private IPaymentCallbackDao iPaymentCallbackDao;

	@Override
	public PaymentCallbackVo add(PaymentCallbackBo paymentCallbackBo) {
		try {
			PaymentCallbackDto paymentCallbackDto = BeanUtils.copy(paymentCallbackBo,PaymentCallbackDto.class);
			paymentCallbackDto.setCreateDate(new Date());
			paymentCallbackDto.setId(UuidUtils.getUuid());
			iPaymentCallbackDao.add(paymentCallbackDto);
			return BeanUtils.copy(paymentCallbackDto,PaymentCallbackVo.class);

		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public PaymentCallbackVo edit(PaymentCallbackBo paymentCallbackBo) {
		try {
			PaymentCallbackDto paymentCallbackDto = BeanUtils.copy(paymentCallbackBo,PaymentCallbackDto.class);
			iPaymentCallbackDao.edit(paymentCallbackDto);
			return BeanUtils.copy(paymentCallbackDto,PaymentCallbackVo.class);

		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public PaymentCallbackVo get(PaymentCallbackBo paymentCallbackBo) {
		try {
			PaymentCallbackDto paymentCallbackDto = BeanUtils.copy(paymentCallbackBo,PaymentCallbackDto.class);
			return iPaymentCallbackDao.get(paymentCallbackDto);

		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}


}
