package com.dotop.smartwater.project.module.service.pay.impl;

import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.utils.BeanUtils;
import com.dotop.smartwater.dependence.core.utils.UuidUtils;
import com.dotop.smartwater.project.module.core.pay.bo.PaymentResultBo;
import com.dotop.smartwater.project.module.core.pay.dto.PaymentResultDto;
import com.dotop.smartwater.project.module.core.pay.vo.PaymentResultVo;
import com.dotop.smartwater.project.module.dao.pay.IPaymentResultDao;
import com.dotop.smartwater.project.module.service.pay.IPaymentResultService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.Date;


/**

 */
@Service
public class PaymentResultServiceImpl implements IPaymentResultService {

	private static final Logger LOGGER = LogManager.getLogger(PaymentResultServiceImpl.class);

	@Autowired
	private IPaymentResultDao iPaymentResultDao;

	@Override
	public PaymentResultVo add(PaymentResultBo paymentResultBo) {
		try {
			PaymentResultVo paymentResultVo = iPaymentResultDao.findByPayId(paymentResultBo.getPayid());
			PaymentResultDto paymentResultDto = BeanUtils.copy(paymentResultBo,PaymentResultDto.class);
			if(paymentResultVo == null){
				paymentResultDto.setPayResId(UuidUtils.getUuid());
				paymentResultDto.setCreateDate(new Date());
				iPaymentResultDao.add(paymentResultDto);
			}
			else{
				paymentResultDto.setPayResId(paymentResultVo.getPayResId());
				iPaymentResultDao.edit(paymentResultDto);
			}
			return BeanUtils.copy(paymentResultDto,PaymentResultVo.class);

		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public PaymentResultVo edit(PaymentResultBo paymentResultBo) {
		try {
			PaymentResultDto paymentResultDto = BeanUtils.copy(paymentResultBo,PaymentResultDto.class);
			iPaymentResultDao.edit(paymentResultDto);
			return BeanUtils.copy(paymentResultDto,PaymentResultVo.class);

		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public PaymentResultVo get(PaymentResultBo paymentResultBo) {
		try {
			PaymentResultDto paymentResultDto = BeanUtils.copy(paymentResultBo,PaymentResultDto.class);
			return iPaymentResultDao.get(paymentResultDto);

		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public PaymentResultVo findByPayId(String payid) {
		try {
			return iPaymentResultDao.findByPayId(payid);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}
}
