package com.dotop.smartwater.project.module.service.revenue.impl;

import java.util.ArrayList;
import java.util.List;

import com.dotop.smartwater.project.module.service.revenue.IReviewDeviceService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dotop.smartwater.dependence.core.common.RootModel;
import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.UuidUtils;
import com.dotop.smartwater.project.module.core.water.bo.ReviewDetailBo;
import com.dotop.smartwater.project.module.core.water.bo.ReviewDeviceBo;
import com.dotop.smartwater.project.module.core.water.dto.ReviewDetailDto;
import com.dotop.smartwater.project.module.core.water.dto.ReviewDeviceDto;
import com.dotop.smartwater.project.module.core.water.form.ReviewDetailForm;
import com.dotop.smartwater.project.module.core.water.vo.ReviewDetailVo;
import com.dotop.smartwater.project.module.core.water.vo.ReviewDeviceVo;
import com.dotop.smartwater.project.module.dao.revenue.IReviewDetailDao;
import com.dotop.smartwater.project.module.dao.revenue.IReviewDeviceDao;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

@Service
public class ReviewDeviceServiceImpl implements IReviewDeviceService {

	private static final Logger LOGGER = LogManager.getLogger(ReviewDeviceServiceImpl.class);

	@Autowired
	private IReviewDeviceDao dao;

	@Autowired
	private IReviewDetailDao detailDao;

	@Override
	public Pagination<ReviewDeviceVo> page(ReviewDeviceBo bo) {
		try {
			Integer isNotDel = RootModel.NOT_DEL;
			// 参数转换
			ReviewDeviceDto dto = new ReviewDeviceDto();
			BeanUtils.copyProperties(bo, dto);
			dto.setIsDel(isNotDel);
			Page<Object> pageHelper = PageHelper.startPage(bo.getPage(), bo.getPageCount());
			List<ReviewDeviceVo> list = dao.getList(dto);
			Pagination<ReviewDeviceVo> pagination = new Pagination<>(bo.getPageCount(), bo.getPage());
			pagination.setData(list);
			pagination.setTotalPageSize(pageHelper.getTotal());
			return pagination;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public ReviewDeviceVo edit(ReviewDeviceBo reviewDeviceBo) {
		try {
			// 参数转换
			ReviewDeviceDto dto = new ReviewDeviceDto();
			BeanUtils.copyProperties(reviewDeviceBo, dto);
			dao.edit(dto);
			return null;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public String del(ReviewDeviceBo reviewDeviceBo) {
		try {
			// 参数转换
			dao.delByBatchNo(reviewDeviceBo.getBatchNo());
			detailDao.delByBatchNo(reviewDeviceBo.getBatchNo());
			return "";
		}catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public Pagination<ReviewDetailVo> detailPage(ReviewDetailBo bo) {
		try {
			Integer isNotDel = RootModel.NOT_DEL;
			// 参数转换
			ReviewDetailDto dto = new ReviewDetailDto();
			BeanUtils.copyProperties(bo, dto);
			dto.setIsDel(isNotDel);
			Page<Object> pageHelper = PageHelper.startPage(bo.getPage(), bo.getPageCount());
			List<ReviewDetailVo> list = detailDao.getList(dto);
			Pagination<ReviewDetailVo> pagination = new Pagination<>(bo.getPageCount(), bo.getPage());
			pagination.setData(list);
			pagination.setTotalPageSize(pageHelper.getTotal());
			return pagination;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public ReviewDetailVo getDevice(ReviewDetailBo bo) {
		try {
			// 参数转换
			ReviewDetailDto dto = new ReviewDetailDto();
			BeanUtils.copyProperties(bo, dto);
			return detailDao.getDevice(dto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public boolean submitReviewDevice(ReviewDetailBo bo) {
		try {
			ReviewDetailDto dto = new ReviewDetailDto();
			BeanUtils.copyProperties(bo, dto);
			boolean flag = false;
			if (detailDao.submitReviewDevice(dto) > 0) {
				flag = true;
			} else {
				flag = false;
			}
			return flag;
		}catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	public ReviewDeviceVo get(ReviewDeviceBo bo) {
		try {
			// 参数转换
			ReviewDeviceDto dto = new ReviewDeviceDto();
			BeanUtils.copyProperties(bo, dto);
			return dao.get(dto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public boolean generate(ReviewDeviceBo bo) {
		boolean flag = false;
		try {
			ReviewDeviceDto dto = new ReviewDeviceDto();
			BeanUtils.copyProperties(bo, dto);
			dto.setId(UuidUtils.getUuid());

			if (dao.generate(dto) > 0) {
				List<ReviewDetailDto> list = new ArrayList<>();
				for (ReviewDetailBo dbo : bo.getDetails()) {
					ReviewDetailDto ddto = new ReviewDetailDto();
					BeanUtils.copyProperties(dbo, ddto);
					ddto.setId(UuidUtils.getUuid());
					list.add(ddto);
				}
				detailDao.batchAdd(list);
				flag = true;
			}
			return flag;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public List<ReviewDetailForm> getRandomDevice(String communityIds, String devNumber) {
		try {
			return dao.getRandomDevice(communityIds, devNumber);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}
}
