package com.dotop.smartwater.project.module.service.revenue.impl;

import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.UuidUtils;
import com.dotop.smartwater.project.module.dao.revenue.*;
import com.dotop.smartwater.project.module.core.water.bo.PaymentTradeExtraBo;
import com.dotop.smartwater.project.module.core.water.bo.PaymentTradeOrderBo;
import com.dotop.smartwater.project.module.core.water.constants.PaymentConstants;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.module.core.water.constants.WaterConstants;
import com.dotop.smartwater.project.module.core.water.dto.*;
import com.dotop.smartwater.project.module.core.water.form.PaymentTradeOrderExtForm;
import com.dotop.smartwater.project.module.core.water.model.PayCallBack;
import com.dotop.smartwater.project.module.core.water.utils.CalUtil;
import com.dotop.smartwater.project.module.core.water.vo.*;
import com.dotop.smartwater.project.module.dao.revenue.*;
import com.dotop.smartwater.project.module.service.revenue.IPaymentTradeOrderService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**

 * @date 2019年8月5日
 */
@Service
public class PaymentTradeOrderServiceImpl implements IPaymentTradeOrderService {

    private static final Logger LOGGER = LogManager.getLogger(PaymentTradeOrderServiceImpl.class);

    @Resource
    private IPaymentTradeOrderDao iPaymentTradeOrderDao;

    @Resource
    private IPaymentTradeProDao iPaymentTradeProDao;

    @Resource
    private IPaymentTradeResultDao iPaymentTradeResultDao;

    @Resource
    private IPaymentTradeOrderExtDao iPaymentTradeOrderExtDao;

    @Resource
    private IOrderDao iOrderDao;

    @Resource
    private IOwnerDao iOwnerDao;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
    public PaymentTradeOrderVo add(PaymentTradeOrderBo paymentTradeOrderBo) {
        try {
            PaymentTradeOrderDto paymentTradeOrderDto = new PaymentTradeOrderDto();
            BeanUtils.copyProperties(paymentTradeOrderBo, paymentTradeOrderDto);
            PaymentTradeOrderVo paymentTradeOrderVo = iPaymentTradeOrderDao.findByEidAndTradeNum(
                    paymentTradeOrderDto.getEnterpriseid(), paymentTradeOrderDto.getTradeNumber());

            if (paymentTradeOrderVo != null) {
                throw new FrameworkRuntimeException(ResultCode.Fail, "单号已存在，请不要重复提交");
            }

            paymentTradeOrderDto.setTradeid(UuidUtils.getUuid(true));
            paymentTradeOrderDto.setCreateDate(new Date());
            paymentTradeOrderDto.setStatus(PaymentConstants.PATMENT_STATUS_NOPAY);
            paymentTradeOrderDto.setMerge(paymentTradeOrderDto.getTradeid());
            iPaymentTradeOrderDao.add(paymentTradeOrderDto);
            paymentTradeOrderVo = new PaymentTradeOrderVo();
            BeanUtils.copyProperties(paymentTradeOrderDto, paymentTradeOrderVo);
            return paymentTradeOrderVo;
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
    public PaymentTradeOrderVo edit(PaymentTradeOrderBo paymentTradeOrderBo) {
        try {
            PaymentTradeOrderDto paymentTradeOrderDto = new PaymentTradeOrderDto();
            BeanUtils.copyProperties(paymentTradeOrderBo, paymentTradeOrderDto);
            iPaymentTradeOrderDao.edit(paymentTradeOrderDto);
            PaymentTradeOrderVo paymentTradeOrderVo = new PaymentTradeOrderVo();
            BeanUtils.copyProperties(paymentTradeOrderDto, paymentTradeOrderVo);
            return paymentTradeOrderVo;
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public PaymentTradeOrderVo get(PaymentTradeOrderBo paymentTradeOrderBo) {
        try {
            PaymentTradeOrderDto paymentTradeOrderDto = new PaymentTradeOrderDto();
            BeanUtils.copyProperties(paymentTradeOrderBo, paymentTradeOrderDto);
            return iPaymentTradeOrderDao.get(paymentTradeOrderDto);
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
    public String del(PaymentTradeOrderBo paymentTradeOrderBo) {
        try {
            PaymentTradeOrderDto paymentTradeOrderDto = new PaymentTradeOrderDto();
            BeanUtils.copyProperties(paymentTradeOrderBo, paymentTradeOrderDto);
            iPaymentTradeOrderDao.del(paymentTradeOrderDto);

            PaymentTradeOrderExtVo vo = iPaymentTradeOrderExtDao.findByTradeid(paymentTradeOrderDto.getTradeid());
            if(vo != null){
                PaymentTradeOrderExtDto dto = new PaymentTradeOrderExtDto();
                BeanUtils.copyProperties(vo,dto);
                iPaymentTradeOrderExtDao.del(dto);
            }

            return null;
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public List<PaymentTradeOrderVo> list(PaymentTradeOrderBo paymentTradeOrderBo) {
        try {
            PaymentTradeOrderDto paymentTradeOrderDto = new PaymentTradeOrderDto();
            BeanUtils.copyProperties(paymentTradeOrderBo, paymentTradeOrderDto);
            return iPaymentTradeOrderDao.list(paymentTradeOrderDto);

        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public Pagination<PaymentTradeOrderVo> page(PaymentTradeOrderBo paymentTradeOrderBo) {
        try {
            PaymentTradeOrderDto paymentTradeOrderDto = new PaymentTradeOrderDto();
            BeanUtils.copyProperties(paymentTradeOrderBo, paymentTradeOrderDto);

            Page<Object> pageHelper = PageHelper.startPage(paymentTradeOrderBo.getPage(), paymentTradeOrderBo.getPageCount());
            List<PaymentTradeOrderVo> list = iPaymentTradeOrderDao.list(paymentTradeOrderDto);
            // 拼接数据返回
            return new Pagination<>(paymentTradeOrderBo.getPage(), paymentTradeOrderBo.getPageCount(), list, pageHelper.getTotal());
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public List<PaymentTradeOrderVo> findListByIds(List<String> tradeIds) {
        try {
            return iPaymentTradeOrderDao.findListByIds(tradeIds);
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
    public void updateList(List<PaymentTradeOrderDto> paymentTradeOrderDtoList) {
        try {
            for (PaymentTradeOrderDto dto : paymentTradeOrderDtoList) {
                iPaymentTradeOrderDao.edit(dto);
            }
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
    public void handleError(PayCallBack payCallBack, String msg) {
        try {
            for (String proId : payCallBack.getProIds()) {
                PaymentTradeProDto paymentTradeProDto = new PaymentTradeProDto();
                paymentTradeProDto.setTransactionProId(proId);
                paymentTradeProDto.setDescription(msg);
                paymentTradeProDto.setStatus(PaymentConstants.STATUS_ERROR);
                iPaymentTradeProDao.edit(paymentTradeProDto);
            }
            for (String resId : payCallBack.getResIds()) {
                PaymentTradeResultDto paymentTradeResultDto = new PaymentTradeResultDto();
                paymentTradeResultDto.setTransactionid(resId);
                paymentTradeResultDto.setDescription(msg);
                paymentTradeResultDto.setStatus(PaymentConstants.STATUS_ERROR);
                iPaymentTradeResultDao.edit(paymentTradeResultDto);
            }

            PaymentTradeOrderExtForm form = payCallBack.getExt();
            if (form != null) {
                iPaymentTradeOrderDao.handleError(form.getTradeIds());
                iPaymentTradeOrderExtDao.handleExtError(form.getTradeIds());

                //代金券返还
                if (StringUtils.isNotBlank(form.getCouponid())) {
                    CouponDto couponDto = new CouponDto();
                    couponDto.setStatus(WaterConstants.COUPON_STATUS_NORMAL);
                    couponDto.setBill(null);
                    couponDto.setCouponid(form.getCouponid());
                    // 优惠券标记为未使用状态
                    iOrderDao.couponIsUsed(couponDto);
                }
                //余额抵扣返还
                if (StringUtils.isNotBlank(form.getBalance()) && StringUtils.isNotBlank(form.getOwnerid())) {
                    OwnerExtDto ownerExtDto = new OwnerExtDto();
                    ownerExtDto.setOwnerId(form.getOwnerid());
                    OwnerVo ownerVo = iOwnerDao.findByOwnerId(ownerExtDto);
                    //把余额抵扣的钱加回去
                    Double alMoney = ownerVo.getAlreadypay();
                    Double money = CalUtil.add(alMoney == null ? 0.0 : alMoney, Double.parseDouble(form.getBalance()));
                    iOrderDao.updateOwnerAlreadypay(form.getOwnerid(), money);
                }
            }

        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public void addExtra(PaymentTradeExtraBo paymentTradeExtraBo) {
        try {
            PaymentTradeExtraDto dto = new PaymentTradeExtraDto();
            BeanUtils.copyProperties(paymentTradeExtraBo, dto);
            iPaymentTradeOrderDao.addExtra(dto);
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public PaymentTradeExtraVo getExtra(String extraid) {
        try {
            return iPaymentTradeOrderDao.getExtra(extraid);
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
    public void handleBalanceAndCoupon(String merge, String ownerid, Double ownerBalance, CouponVo coupon) {
        try {
            if (StringUtils.isNotBlank(ownerid) && ownerBalance != null) {
                iOrderDao.updateOwnerAlreadypay(ownerid, ownerBalance);
            }
            if (coupon != null) {
                CouponDto couponDto = new CouponDto();
                couponDto.setStatus(WaterConstants.COUPON_STATUS_USED);
                couponDto.setBill(merge);
                couponDto.setCouponid(coupon.getCouponid());
                // 优惠券标记为已经使用
                iOrderDao.couponIsUsed(couponDto);
            }
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public PaymentTradeOrderVo findByEidAndTradeNum(PaymentTradeOrderBo paymentTradeOrderBo) {
        try {
            PaymentTradeOrderDto paymentTradeOrderDto = new PaymentTradeOrderDto();
            BeanUtils.copyProperties(paymentTradeOrderBo, paymentTradeOrderDto);
            PaymentTradeOrderVo paymentTradeOrderVo = iPaymentTradeOrderDao.findByEidAndTradeNum(
                    paymentTradeOrderDto.getEnterpriseid(), paymentTradeOrderDto.getTradeNumber());
            return paymentTradeOrderVo;
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
    public void batchAdd(List<PaymentTradeOrderDto> dtoList, List<PaymentTradeOrderExtDto> extDtoList) {
        try {
            iPaymentTradeOrderDao.addList(dtoList);
            iPaymentTradeOrderExtDao.addList(extDtoList);
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

}
