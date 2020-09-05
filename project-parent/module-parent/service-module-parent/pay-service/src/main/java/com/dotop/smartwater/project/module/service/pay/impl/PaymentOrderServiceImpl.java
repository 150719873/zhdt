package com.dotop.smartwater.project.module.service.pay.impl;

import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.utils.BeanUtils;
import com.dotop.smartwater.dependence.core.utils.UuidUtils;
import com.dotop.smartwater.project.module.core.pay.bo.PaymentOrderBo;
import com.dotop.smartwater.project.module.core.pay.dto.PaymentOrderDto;
import com.dotop.smartwater.project.module.core.pay.vo.PaymentOrderVo;
import com.dotop.smartwater.project.module.dao.pay.IPaymentOrderDao;
import com.dotop.smartwater.project.module.service.pay.IPaymentOrderService;
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
public class PaymentOrderServiceImpl implements IPaymentOrderService {

	private static final Logger LOGGER = LogManager.getLogger(PaymentOrderServiceImpl.class);

	@Autowired
	private IPaymentOrderDao iPaymentOrderDao;

	@Override
	public PaymentOrderVo add(PaymentOrderBo paymentOrderBo) {
		try {
			PaymentOrderDto paymentOrderDto = BeanUtils.copy(paymentOrderBo,PaymentOrderDto.class);
			paymentOrderDto.setPayid(UuidUtils.getUuid());
			paymentOrderDto.setCreateDate(new Date());
			iPaymentOrderDao.add(paymentOrderDto);
			return BeanUtils.copy(paymentOrderDto,PaymentOrderVo.class);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public PaymentOrderVo edit(PaymentOrderBo paymentOrderBo) {
		try {
			PaymentOrderDto paymentOrderDto = BeanUtils.copy(paymentOrderBo,PaymentOrderDto.class);
			iPaymentOrderDao.edit(paymentOrderDto);
			return BeanUtils.copy(paymentOrderDto,PaymentOrderVo.class);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public PaymentOrderVo get(PaymentOrderBo paymentOrderBo) {
		try {
			PaymentOrderDto paymentOrderDto = BeanUtils.copy(paymentOrderBo,PaymentOrderDto.class);
			return iPaymentOrderDao.get(paymentOrderDto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public PaymentOrderVo findByTradeNumberAndEid(String tradeNumber, String enterpriseid) {
		try {
			return iPaymentOrderDao.findByTradeNumberAndEid(tradeNumber, enterpriseid);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public List<PaymentOrderVo> getPayInList(String status, Integer minutes) {
		try {
			return iPaymentOrderDao.getPayInList(status, minutes);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}
}
