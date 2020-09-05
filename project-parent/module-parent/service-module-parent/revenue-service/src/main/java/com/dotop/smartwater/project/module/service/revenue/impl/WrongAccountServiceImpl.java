package com.dotop.smartwater.project.module.service.revenue.impl;

import java.util.List;

import com.dotop.smartwater.project.module.service.revenue.IWrongAccountService;
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
import com.dotop.smartwater.project.module.core.water.bo.WrongAccountBo;
import com.dotop.smartwater.project.module.core.water.dto.WrongAccountDto;
import com.dotop.smartwater.project.module.core.water.vo.WrongAccountVo;
import com.dotop.smartwater.project.module.dao.revenue.IWrongAccountDao;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

@Service
public class WrongAccountServiceImpl implements IWrongAccountService {

	private static final Logger LOGGER = LogManager.getLogger(AccountingServiceImpl.class);

	@Autowired
	private IWrongAccountDao iWrongAccountDao;

	@Override
	public Pagination<WrongAccountVo> page(WrongAccountBo wrongAccountBo) {
		try {
			// 参数转换
			WrongAccountDto wrongAccountDto = new WrongAccountDto();
			BeanUtils.copyProperties(wrongAccountBo, wrongAccountDto);
			Page<Object> pageHelper = PageHelper.startPage(wrongAccountBo.getPage(), wrongAccountBo.getPageCount());
			List<WrongAccountVo> list = iWrongAccountDao.list(wrongAccountDto);
			Pagination<WrongAccountVo> pagination = new Pagination<>(wrongAccountBo.getPageCount(),
					wrongAccountBo.getPage());
			pagination.setData(list);
			pagination.setTotalPageSize(pageHelper.getTotal());
			return pagination;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public WrongAccountVo get(WrongAccountBo wrongAccountBo) {
		try {
			WrongAccountDto wrongAccountDto = new WrongAccountDto();
			BeanUtils.copyProperties(wrongAccountBo, wrongAccountDto);
			return iWrongAccountDao.get(wrongAccountDto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public WrongAccountVo add(WrongAccountBo wrongAccountBo) {
		try {
			String id = UuidUtils.getUuid();
			wrongAccountBo.setId(id);
			WrongAccountDto wrongAccountDto = new WrongAccountDto();
			BeanUtils.copyProperties(wrongAccountBo, wrongAccountDto);
			iWrongAccountDao.add(wrongAccountDto);
			WrongAccountVo wrongAccountVo = new WrongAccountVo();
			wrongAccountVo.setId(id);
			return wrongAccountVo;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public WrongAccountVo update(WrongAccountBo wrongAccountBo) {
		try {
			WrongAccountDto wrongAccountDto = new WrongAccountDto();
			BeanUtils.copyProperties(wrongAccountBo, wrongAccountDto);
			iWrongAccountDao.update(wrongAccountDto);
			return null;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public void complete(WrongAccountBo wrongAccountBo) {
		try {
			WrongAccountDto wrongAccountDto = new WrongAccountDto();
			BeanUtils.copyProperties(wrongAccountBo, wrongAccountDto);
			iWrongAccountDao.update(wrongAccountDto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public void cancel(WrongAccountBo wrongAccountBo) {
		try {
			WrongAccountDto wrongAccountDto = new WrongAccountDto();
			BeanUtils.copyProperties(wrongAccountBo, wrongAccountDto);
			iWrongAccountDao.update(wrongAccountDto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}

	}

	@Override
	public List<WrongAccountVo> getStatus(WrongAccountBo wrongAccountBo) {
		try {
			WrongAccountDto wrongAccountDto = new WrongAccountDto();
			BeanUtils.copyProperties(wrongAccountBo, wrongAccountDto);
			return iWrongAccountDao.getStatus(wrongAccountDto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public boolean isExist(WrongAccountBo wrongAccountBo) {
		try {
			WrongAccountDto wrongAccountDto = new WrongAccountDto();
			BeanUtils.copyProperties(wrongAccountBo, wrongAccountDto);
			return iWrongAccountDao.isexist(wrongAccountDto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public boolean ishandle(WrongAccountBo wrongAccountBo) {
		return false;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public void updateCoupon(WrongAccountBo wrongAccountBo) {
		try {
			WrongAccountDto wrongAccountDto = new WrongAccountDto();
			BeanUtils.copyProperties(wrongAccountBo, wrongAccountDto);
			iWrongAccountDao.update(wrongAccountDto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}

	}
}
