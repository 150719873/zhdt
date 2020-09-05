package com.dotop.smartwater.project.module.service.revenue.impl;

import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.UuidUtils;
import com.dotop.smartwater.project.module.core.water.bo.PaymentTradeOrderExtBo;
import com.dotop.smartwater.project.module.core.water.dto.PaymentTradeOrderExtDto;
import com.dotop.smartwater.project.module.core.water.vo.PaymentTradeOrderExtVo;
import com.dotop.smartwater.project.module.dao.revenue.IPaymentTradeOrderExtDao;
import com.dotop.smartwater.project.module.service.revenue.IPaymentTradeOrderExtService;
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

import java.util.List;

/**

 * @date 2019年8月5日
 */
@Service
public class PaymentTradeOrderExtServiceImpl implements IPaymentTradeOrderExtService {

    private static final Logger LOGGER = LogManager.getLogger(PaymentTradeOrderExtServiceImpl.class);

    @Autowired
    private IPaymentTradeOrderExtDao iPaymentTradeOrderExtDao;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
    public PaymentTradeOrderExtVo add(PaymentTradeOrderExtBo paymentTradeOrderExtBo) {
        try {
            PaymentTradeOrderExtDto paymentTradeOrderDto = new PaymentTradeOrderExtDto();
            BeanUtils.copyProperties(paymentTradeOrderExtBo, paymentTradeOrderDto);
            paymentTradeOrderDto.setId(UuidUtils.getUuid(true));
            iPaymentTradeOrderExtDao.add(paymentTradeOrderDto);
            PaymentTradeOrderExtVo paymentTradeOrderVo = new PaymentTradeOrderExtVo();
            BeanUtils.copyProperties(paymentTradeOrderExtBo, paymentTradeOrderVo);
            return paymentTradeOrderVo;
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public PaymentTradeOrderExtVo edit(PaymentTradeOrderExtBo paymentTradeOrderExtBo) {
        try {
            PaymentTradeOrderExtDto paymentTradeOrderDto = new PaymentTradeOrderExtDto();
            BeanUtils.copyProperties(paymentTradeOrderExtBo, paymentTradeOrderDto);
            iPaymentTradeOrderExtDao.edit(paymentTradeOrderDto);
            PaymentTradeOrderExtVo paymentTradeOrderVo = new PaymentTradeOrderExtVo();
            BeanUtils.copyProperties(paymentTradeOrderExtBo, paymentTradeOrderVo);
            return paymentTradeOrderVo;
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public PaymentTradeOrderExtVo get(PaymentTradeOrderExtBo paymentTradeOrderExtBo) {
        try {
            PaymentTradeOrderExtDto paymentTradeOrderDto = new PaymentTradeOrderExtDto();
            BeanUtils.copyProperties(paymentTradeOrderExtBo, paymentTradeOrderDto);
            return iPaymentTradeOrderExtDao.get(paymentTradeOrderDto);
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
    public String del(PaymentTradeOrderExtBo paymentTradeOrderExtBo) {
        try {
            PaymentTradeOrderExtDto paymentTradeOrderDto = new PaymentTradeOrderExtDto();
            BeanUtils.copyProperties(paymentTradeOrderExtBo, paymentTradeOrderDto);
            iPaymentTradeOrderExtDao.del(paymentTradeOrderDto);
            return null;
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public List<PaymentTradeOrderExtVo> list(PaymentTradeOrderExtBo paymentTradeOrderExtBo) {
        try {
            PaymentTradeOrderExtDto paymentTradeOrderDto = new PaymentTradeOrderExtDto();
            BeanUtils.copyProperties(paymentTradeOrderExtBo, paymentTradeOrderDto);
            return iPaymentTradeOrderExtDao.list(paymentTradeOrderDto);

        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public Pagination<PaymentTradeOrderExtVo> page(PaymentTradeOrderExtBo paymentTradeOrderExtBo) {
        try {
            PaymentTradeOrderExtDto paymentTradeOrderDto = new PaymentTradeOrderExtDto();
            BeanUtils.copyProperties(paymentTradeOrderExtBo, paymentTradeOrderDto);

            Page<Object> pageHelper = PageHelper.startPage(paymentTradeOrderExtBo.getPage(), paymentTradeOrderExtBo.getPageCount());
            List<PaymentTradeOrderExtVo> list = iPaymentTradeOrderExtDao.list(paymentTradeOrderDto);
            // 拼接数据返回
            return new Pagination<>(paymentTradeOrderExtBo.getPage(), paymentTradeOrderExtBo.getPageCount(), list, pageHelper.getTotal());
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public List<PaymentTradeOrderExtVo> findNotPayListByTradeIds(List<String> tradeIds, String enterpriseid) {
        try {
            return iPaymentTradeOrderExtDao.findNotPayListByTradeIds(tradeIds, enterpriseid);
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
    public void updateList(List<PaymentTradeOrderExtVo> list) {
        try {
            for (PaymentTradeOrderExtVo vo : list) {
                PaymentTradeOrderExtDto paymentTradeOrderDto = new PaymentTradeOrderExtDto();
                BeanUtils.copyProperties(vo, paymentTradeOrderDto);
                iPaymentTradeOrderExtDao.edit(paymentTradeOrderDto);
            }
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public PaymentTradeOrderExtVo findByTradeid(String tradeid) {
        try {
            return iPaymentTradeOrderExtDao.findByTradeid(tradeid);
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

}
