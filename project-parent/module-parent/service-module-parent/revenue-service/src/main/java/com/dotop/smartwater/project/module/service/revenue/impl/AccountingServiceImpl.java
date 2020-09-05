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

import com.dotop.smartwater.dependence.core.common.RootModel;
import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.UuidUtils;
import com.dotop.smartwater.project.module.core.water.bo.AccountingBo;
import com.dotop.smartwater.project.module.core.water.bo.MarkOrderBo;
import com.dotop.smartwater.project.module.core.water.dto.AccountingDto;
import com.dotop.smartwater.project.module.core.water.dto.MarkOrderDto;
import com.dotop.smartwater.project.module.core.water.vo.AccountingVo;
import com.dotop.smartwater.project.module.core.water.vo.OrderVo;
import com.dotop.smartwater.project.module.dao.revenue.IAccountingDao;
import com.dotop.smartwater.project.module.service.revenue.IAccountingService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

/**
 * 收银核算 个人核算功能
 * 

 * @date 2019年2月25日
 */
@Service
public class AccountingServiceImpl implements IAccountingService {

	private static final Logger LOGGER = LogManager.getLogger(AccountingServiceImpl.class);

	@Autowired
	private IAccountingDao iAccountingDao;

	@Override
	public Pagination<OrderVo> getPage(AccountingBo accountingBo) {
		try {
			Integer isNotDel = RootModel.NOT_DEL;
			// 参数转换
			AccountingDto accountingDto = new AccountingDto();
			BeanUtils.copyProperties(accountingBo, accountingDto);
			accountingDto.setIsDel(isNotDel);
			Page<Object> pageHelper = PageHelper.startPage(accountingBo.getPage(), accountingBo.getPageCount());
			List<OrderVo> list = iAccountingDao.getList(accountingDto);
			Pagination<OrderVo> pagination = new Pagination<>(accountingBo.getPageCount(), accountingBo.getPage());
			pagination.setData(list);
			pagination.setTotalPageSize(pageHelper.getTotal());
			return pagination;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public AccountingVo get(AccountingBo accountingBo) {
		try {
			// 参数转换
			AccountingDto accountingDto = new AccountingDto();
			BeanUtils.copyProperties(accountingBo, accountingDto);
			return iAccountingDao.get(accountingDto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public Double getUserDayMoney(AccountingBo accountingBo) {
		try {
			// 参数转换
			AccountingDto accountingDto = new AccountingDto();
			BeanUtils.copyProperties(accountingBo, accountingDto);
			return iAccountingDao.getUserDayMoney(accountingDto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public AccountingVo add(AccountingBo accountingBo) {
		try {
			String id = UuidUtils.getUuid();
			AccountingDto accountingDto = new AccountingDto();
			BeanUtils.copyProperties(accountingBo, accountingDto);
			accountingDto.setId(id);
			iAccountingDao.add(accountingDto);
			AccountingVo accountingVo = new AccountingVo();
			accountingVo.setId(accountingDto.getId());
			return accountingVo;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public String del(AccountingBo accountingBo) {

		try {
			AccountingDto accountingDto = new AccountingDto();
			BeanUtils.copyProperties(accountingBo, accountingDto);
			iAccountingDao.del(accountingDto);
			return null;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public void saveMarkOrder(MarkOrderBo markOrderBo) {
		try {
			String id = UuidUtils.getUuid();
			MarkOrderDto markOrderDto = new MarkOrderDto();
			BeanUtils.copyProperties(markOrderBo, markOrderDto);
			markOrderDto.setId(id);
			iAccountingDao.saveMarkOrder(markOrderDto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public void deleteMark(MarkOrderBo markOrderBo) {
		try {
			MarkOrderDto markOrderDto = new MarkOrderDto();
			BeanUtils.copyProperties(markOrderBo, markOrderDto);
			iAccountingDao.deleteMark(markOrderDto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}

	}

}
