package com.dotop.smartwater.project.module.service.revenue.impl;

import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.UuidUtils;
import com.dotop.smartwater.project.module.core.water.bo.PaymentTradeProBo;
import com.dotop.smartwater.project.module.core.water.dto.PaymentTradeProDto;
import com.dotop.smartwater.project.module.core.water.vo.PaymentTradeProVo;
import com.dotop.smartwater.project.module.dao.revenue.IPaymentTradeProDao;
import com.dotop.smartwater.project.module.service.revenue.IPaymentTradeProService;
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
public class PaymentTradeProServiceImpl implements IPaymentTradeProService {

    private static final Logger LOGGER = LogManager.getLogger(PaymentTradeProServiceImpl.class);

    @Autowired
    private IPaymentTradeProDao iPaymentTradeProDao;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
    public PaymentTradeProVo add(PaymentTradeProBo paymentTradeProBo) {
        try {
            PaymentTradeProDto paymentTradeProDto = new PaymentTradeProDto();
            BeanUtils.copyProperties(paymentTradeProBo, paymentTradeProDto);
            paymentTradeProDto.setCreateDate(new Date());
            paymentTradeProDto.setTransactionProId(UuidUtils.getUuid(true));
            iPaymentTradeProDao.add(paymentTradeProDto);
            PaymentTradeProVo paymentTradeProVo = new PaymentTradeProVo();
            BeanUtils.copyProperties(paymentTradeProDto, paymentTradeProVo);
            return paymentTradeProVo;
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public PaymentTradeProVo edit(PaymentTradeProBo paymentTradeProBo) {
        try {
            PaymentTradeProDto paymentTradeProDto = new PaymentTradeProDto();
            BeanUtils.copyProperties(paymentTradeProBo, paymentTradeProDto);
            iPaymentTradeProDao.edit(paymentTradeProDto);
            PaymentTradeProVo paymentTradeProVo = new PaymentTradeProVo();
            BeanUtils.copyProperties(paymentTradeProDto, paymentTradeProVo);
            return paymentTradeProVo;
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public PaymentTradeProVo get(PaymentTradeProBo paymentTradeProBo) {
        try {
            PaymentTradeProDto paymentTradeProDto = new PaymentTradeProDto();
            BeanUtils.copyProperties(paymentTradeProBo, paymentTradeProDto);
            return iPaymentTradeProDao.get(paymentTradeProDto);
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
    public String del(PaymentTradeProBo paymentTradeProBo) {
        try {
            PaymentTradeProDto paymentTradeProDto = new PaymentTradeProDto();
            BeanUtils.copyProperties(paymentTradeProBo, paymentTradeProDto);
            iPaymentTradeProDao.del(paymentTradeProDto);
            return null;
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public List<PaymentTradeProVo> list(PaymentTradeProBo paymentTradeProBo) {
        try {
            PaymentTradeProDto paymentTradeProDto = new PaymentTradeProDto();
            BeanUtils.copyProperties(paymentTradeProBo, paymentTradeProDto);
            return iPaymentTradeProDao.list(paymentTradeProDto);

        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public Pagination<PaymentTradeProVo> page(PaymentTradeProBo paymentTradeProBo) {
        try {
            PaymentTradeProDto paymentTradeProDto = new PaymentTradeProDto();
            BeanUtils.copyProperties(paymentTradeProBo, paymentTradeProDto);

            Page<Object> pageHelper = PageHelper.startPage(paymentTradeProBo.getPage(), paymentTradeProBo.getPageCount());
            List<PaymentTradeProVo> list = iPaymentTradeProDao.list(paymentTradeProDto);
            // 拼接数据返回
            return new Pagination<>(paymentTradeProBo.getPage(), paymentTradeProBo.getPageCount(), list, pageHelper.getTotal());
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

}
