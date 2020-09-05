package com.dotop.smartwater.project.module.service.revenue.impl;

import java.util.Date;
import java.util.List;
import java.util.Random;

import com.dotop.smartwater.project.module.service.revenue.ICouponService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.DateUtils;
import com.dotop.smartwater.dependence.core.utils.UuidUtils;
import com.dotop.smartwater.project.module.core.water.bo.CouponBo;
import com.dotop.smartwater.project.module.core.water.dto.CouponDto;
import com.dotop.smartwater.project.module.core.water.vo.CouponVo;
import com.dotop.smartwater.project.module.dao.revenue.ICouponDao;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

@Service
public class CouponServiceImpl implements ICouponService {

	private static final Logger LOGGER = LogManager.getLogger(CouponServiceImpl.class);

	@Autowired
	private ICouponDao iCouponDao;

	@Override
	public Pagination<CouponVo> getCouponList(CouponBo couponBo) {
		try {
			// 参数转换
			CouponDto couponDto = new CouponDto();
			BeanUtils.copyProperties(couponBo, couponDto);
			// 操作数据
			Page<Object> pageHelper = PageHelper.startPage(couponBo.getPage(), couponBo.getPageCount());
			List<CouponVo> list = iCouponDao.list(couponDto);
			// 拼接数据返回
			return new Pagination<>(couponBo.getPage(), couponBo.getPageCount(), list, pageHelper.getTotal());
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}

	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public CouponVo addCoupon(CouponBo couponBo) {
		try {
			// 参数转换
			CouponDto couponDto = new CouponDto();
			BeanUtils.copyProperties(couponBo, couponDto);
			String couponid = UuidUtils.getUuid();
			couponDto.setCouponid(couponid);
			// 操作数据
			iCouponDao.addCoupon(couponDto);
			CouponVo couponVo = new CouponVo();
			couponVo.setCouponid(couponid);
			return couponVo;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public CouponVo getByNo(CouponBo couponBo) {
		try {
			// 参数转换
			CouponDto couponDto = new CouponDto();
			BeanUtils.copyProperties(couponBo, couponDto);
			// 操作数据
			return iCouponDao.getCoupon(couponDto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public CouponVo getCoupon(CouponBo couponBo) {
		try {
			// 参数转换
			CouponDto couponDto = new CouponDto();
			BeanUtils.copyProperties(couponBo, couponDto);
			// 操作数据
			return iCouponDao.getCoupon(couponDto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public void delete(CouponBo couponBo) {
		try {
			// 参数转换
			CouponDto couponDto = new CouponDto();
			BeanUtils.copyProperties(couponBo, couponDto);
			// 操作数据
			iCouponDao.delete(couponDto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public void disable(CouponBo couponBo) {
		try {
			// 参数转换
			CouponDto couponDto = new CouponDto();
			BeanUtils.copyProperties(couponBo, couponDto);
			// 操作数据
			iCouponDao.disable(couponDto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public String genNo() {
		Date currentTime = new Date();
		String dateString = DateUtils.format(currentTime, DateUtils.YYYYMMDDHHMMSS);
		Random r = new Random();
		int x = r.nextInt(900) + 100;
		String no = dateString + x;
		CouponBo couponBo = new CouponBo();
		couponBo.setNo(no);
		if (this.getByNo(couponBo) != null) {
			return genNo();
		}
		return no;
	}

	@Override
	public List<CouponVo> list(CouponBo couponBo) {
		try {
			// 参数转换
			CouponDto couponDto = new CouponDto();
			BeanUtils.copyProperties(couponBo, couponDto);
			// 操作数据
			return iCouponDao.list(couponDto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public int editStatus(CouponBo couponBo) {
		try {
			// 参数转换
			CouponDto couponDto = new CouponDto();
			BeanUtils.copyProperties(couponBo, couponDto);
			// 操作数据
			return iCouponDao.edit(couponDto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}
}
