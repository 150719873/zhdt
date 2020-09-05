package com.dotop.smartwater.project.module.service.revenue.impl;

import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.UuidUtils;
import com.dotop.smartwater.project.module.core.water.bo.PaymentTradeResultBo;
import com.dotop.smartwater.project.module.core.water.dto.PaymentTradeResultDto;
import com.dotop.smartwater.project.module.core.water.vo.PaymentTradeResultVo;
import com.dotop.smartwater.project.module.dao.revenue.IPaymentTradeResultDao;
import com.dotop.smartwater.project.module.service.revenue.IPaymentTradeResultService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**

 * @date 2019年8月5日
 */
@Service
public class PaymentTradeResultServiceImpl implements IPaymentTradeResultService {

    private static final Logger LOGGER = LogManager.getLogger(PaymentTradeResultServiceImpl.class);

    @Autowired
    private IPaymentTradeResultDao iPaymentTradeResultDao;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
    public PaymentTradeResultVo add(PaymentTradeResultBo paymentTradeResultBo) {
        try {
            PaymentTradeResultDto paymentTradeResultDto = new PaymentTradeResultDto();
            BeanUtils.copyProperties(paymentTradeResultBo, paymentTradeResultDto);
            PaymentTradeResultVo paymentTradeResultVo = iPaymentTradeResultDao.findByTradeId(paymentTradeResultBo.getTradeid());
            if (paymentTradeResultVo != null) {
                paymentTradeResultDto.setTransactionid(paymentTradeResultVo.getTransactionid());
                iPaymentTradeResultDao.edit(paymentTradeResultDto);
            } else {
                paymentTradeResultVo = new PaymentTradeResultVo();
                paymentTradeResultDto.setCreateDate(new Date());
                paymentTradeResultDto.setTransactionid(UuidUtils.getUuid(true));
                iPaymentTradeResultDao.add(paymentTradeResultDto);
            }

            BeanUtils.copyProperties(paymentTradeResultDto, paymentTradeResultVo);
            return paymentTradeResultVo;
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public PaymentTradeResultVo edit(PaymentTradeResultBo paymentTradeResultBo) {
        try {
            PaymentTradeResultDto paymentTradeResultDto = new PaymentTradeResultDto();
            BeanUtils.copyProperties(paymentTradeResultBo, paymentTradeResultDto);
            iPaymentTradeResultDao.edit(paymentTradeResultDto);
            PaymentTradeResultVo paymentTradeResultVo = new PaymentTradeResultVo();
            BeanUtils.copyProperties(paymentTradeResultDto, paymentTradeResultVo);
            return paymentTradeResultVo;
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public PaymentTradeResultVo get(PaymentTradeResultBo paymentTradeResultBo) {
        try {
            PaymentTradeResultDto paymentTradeResultDto = new PaymentTradeResultDto();
            BeanUtils.copyProperties(paymentTradeResultBo, paymentTradeResultDto);
            return iPaymentTradeResultDao.get(paymentTradeResultDto);
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
    public String del(PaymentTradeResultBo paymentTradeResultBo) {
        try {
            PaymentTradeResultDto paymentTradeResultDto = new PaymentTradeResultDto();
            BeanUtils.copyProperties(paymentTradeResultBo, paymentTradeResultDto);
            iPaymentTradeResultDao.del(paymentTradeResultDto);
            return null;
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public List<PaymentTradeResultVo> list(PaymentTradeResultBo paymentTradeResultBo) {
        try {
            PaymentTradeResultDto paymentTradeResultDto = new PaymentTradeResultDto();
            BeanUtils.copyProperties(paymentTradeResultBo, paymentTradeResultDto);
            return iPaymentTradeResultDao.list(paymentTradeResultDto);

        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public Pagination<PaymentTradeResultVo> page(PaymentTradeResultBo paymentTradeResultBo) {
        try {
            PaymentTradeResultDto paymentTradeResultDto = new PaymentTradeResultDto();
            BeanUtils.copyProperties(paymentTradeResultBo, paymentTradeResultDto);

            Page<Object> pageHelper = PageHelper.startPage(paymentTradeResultBo.getPage(), paymentTradeResultBo.getPageCount());
            List<PaymentTradeResultVo> list = iPaymentTradeResultDao.list(paymentTradeResultDto);
            // 拼接数据返回
            return new Pagination<>(paymentTradeResultBo.getPage(), paymentTradeResultBo.getPageCount(), list, pageHelper.getTotal());
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public PaymentTradeResultVo findByTradeId(String tradeid) {
        try {
            return iPaymentTradeResultDao.findByTradeId(tradeid);
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

}
