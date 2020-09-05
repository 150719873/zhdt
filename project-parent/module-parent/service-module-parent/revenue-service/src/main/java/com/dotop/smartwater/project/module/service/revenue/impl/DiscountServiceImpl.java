package com.dotop.smartwater.project.module.service.revenue.impl;

import java.util.List;

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
import com.dotop.smartwater.dependence.core.utils.UuidUtils;
import com.dotop.smartwater.project.module.core.water.bo.DiscountBo;
import com.dotop.smartwater.project.module.core.water.dto.DiscountDto;
import com.dotop.smartwater.project.module.core.water.vo.DiscountVo;
import com.dotop.smartwater.project.module.dao.revenue.IDiscountDao;
import com.dotop.smartwater.project.module.service.revenue.IDiscountService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

@Service
public class DiscountServiceImpl implements IDiscountService {

	private static final Logger LOGGER = LogManager.getLogger(DiscountServiceImpl.class);

	@Autowired
	private IDiscountDao iDiscountDao;

	@Override
	public Pagination<DiscountVo> find(DiscountBo discountBo) {
		try {
			// 参数转换
			DiscountDto discountDto = new DiscountDto();
			BeanUtils.copyProperties(discountBo, discountDto);
			// 操作数据
			Page<Object> pageHelper = PageHelper.startPage(discountBo.getPage(), discountBo.getPageCount());
			List<DiscountVo> list = iDiscountDao.find(discountBo);
			// 拼接数据返回
			return new Pagination<>(discountBo.getPage(), discountBo.getPageCount(), list, pageHelper.getTotal());
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public void save(DiscountBo discountBo) {
		try {
			// 参数转换
			DiscountDto discountDto = new DiscountDto();
			BeanUtils.copyProperties(discountBo, discountDto);
			String id = UuidUtils.getUuid();
			discountDto.setId(id);

			if (discountBo.getIsdefault() == 1) { // 1-表示默认，只能有一个默认
				iDiscountDao.updateDiscountDefault(discountBo.getEnterpriseid());
			}
			// 操作数据
			iDiscountDao.save(discountDto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public DiscountVo edit(DiscountBo discountBo) {
		try {
			// 参数转换
			DiscountDto discountDto = new DiscountDto();
			BeanUtils.copyProperties(discountBo, discountDto);

			if (discountBo.getIsdefault() == 1) { // 1-表示默认，只能有一个默认
				iDiscountDao.updateDiscountDefault(discountBo.getEnterpriseid());
			}
			// 操作数据
			iDiscountDao.edit(discountDto);
			return null;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public void delete(DiscountBo discountBo) {
		try {
			// 参数转换
			DiscountDto discountDto = new DiscountDto();
			BeanUtils.copyProperties(discountBo, discountDto);

			// 操作数据
			iDiscountDao.deleteConds(discountDto);// 这个逻辑应该是分开处理的
			iDiscountDao.delete(discountDto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}

	}

	@Override
	public DiscountVo getisDefaultDiscount(DiscountBo discountBo) {
		try {
			// 参数转换
			DiscountDto discountDto = new DiscountDto();
			BeanUtils.copyProperties(discountBo, discountDto);

			// 操作数据
			return iDiscountDao.getisDefaultDiscount(discountDto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public DiscountVo getDiscountById(String discountid) {
		try {
			// 参数转换
			DiscountDto discountDto = new DiscountDto();
			discountDto.setId(discountid);

			// 操作数据
			return iDiscountDao.getDiscount(discountDto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public DiscountVo get(DiscountBo discountBo) {
		try {
			// 参数转换
			DiscountDto discountDto = new DiscountDto();
			BeanUtils.copyProperties(discountBo, discountDto);
			// 操作数据
			return iDiscountDao.getDiscount(discountDto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}
}
