package com.dotop.smartwater.project.module.service.workcenter.impl;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
import com.dotop.smartwater.dependence.core.utils.BeanUtils;
import com.dotop.smartwater.project.module.core.water.bo.WorkCenterDbBo;
import com.dotop.smartwater.project.module.core.water.dto.WorkCenterDbDto;
import com.dotop.smartwater.project.module.core.water.vo.WorkCenterDbVo;
import com.dotop.smartwater.project.module.dao.workcenter.IDbDao;
import com.dotop.smartwater.project.module.service.workcenter.IDbService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

@Service("IWorkCenterDbService")
public class DbServiceImpl implements IDbService {

	private static final Logger LOGGER = LogManager.getLogger(DbServiceImpl.class);

	@Autowired
	private IDbDao iDbDao;

	@Override
	public Pagination<WorkCenterDbVo> page(WorkCenterDbBo dbBo) {
		try {
			WorkCenterDbDto workCenterDbDto = BeanUtils.copy(dbBo, WorkCenterDbDto.class);
			workCenterDbDto.setIsDel(RootModel.NOT_DEL);
			Page<WorkCenterDbVo> pageHelper = PageHelper.startPage(dbBo.getPage(), dbBo.getPageCount());
			List<WorkCenterDbVo> list = iDbDao.list(workCenterDbDto);
			return new Pagination<>(pageHelper.getPageSize(), pageHelper.getPageNum(), list, pageHelper.getTotal());
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public List<WorkCenterDbVo> list(WorkCenterDbBo dbBo) {
		try {
			WorkCenterDbDto workCenterDbDto = BeanUtils.copy(dbBo, WorkCenterDbDto.class);
			workCenterDbDto.setIsDel(RootModel.NOT_DEL);
			return iDbDao.list(workCenterDbDto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public WorkCenterDbVo get(WorkCenterDbBo dbBo) {
		try {
			WorkCenterDbDto workCenterDbDto = BeanUtils.copy(dbBo, WorkCenterDbDto.class);
			workCenterDbDto.setIsDel(RootModel.NOT_DEL);
			return iDbDao.get(workCenterDbDto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public WorkCenterDbVo add(WorkCenterDbBo dbBo) {
		try {
			WorkCenterDbDto workCenterDbDto = BeanUtils.copy(dbBo, WorkCenterDbDto.class);
			workCenterDbDto.setIsDel(RootModel.NOT_DEL);
			iDbDao.add(workCenterDbDto);
			WorkCenterDbVo workCenterDbVo = new WorkCenterDbVo();
			workCenterDbVo.setId(workCenterDbDto.getId());
			return workCenterDbVo;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public WorkCenterDbVo edit(WorkCenterDbBo dbBo) {
		try {
			WorkCenterDbDto workCenterDbDto = BeanUtils.copy(dbBo, WorkCenterDbDto.class);
			workCenterDbDto.setIsDel(RootModel.NOT_DEL);
			iDbDao.edit(workCenterDbDto);
			return null;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public String del(WorkCenterDbBo dbBo) {
		try {
			WorkCenterDbDto workCenterDbDto = BeanUtils.copy(dbBo, WorkCenterDbDto.class);
			workCenterDbDto.setId(dbBo.getId());
			workCenterDbDto.setEnterpriseid(dbBo.getEnterpriseid());
			workCenterDbDto.setIsDel(RootModel.NOT_DEL);
			workCenterDbDto.setNewIsDel(RootModel.DEL);
			iDbDao.del(workCenterDbDto);
			return null;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public void adds(List<WorkCenterDbBo> dbBos) {
		try {
			if (dbBos != null && !dbBos.isEmpty()) {
				List<WorkCenterDbDto> dbDtos = BeanUtils.copy(dbBos, WorkCenterDbDto.class);
				for (WorkCenterDbDto dto : dbDtos) {
					dto.setIsDel(RootModel.NOT_DEL);
				}
				iDbDao.adds(dbDtos);
			}
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public void dels(List<WorkCenterDbBo> dbBos) {
		try {
			if (dbBos != null && !dbBos.isEmpty()) {
				List<WorkCenterDbDto> dbDtos = BeanUtils.copy(dbBos, WorkCenterDbDto.class);
				for (WorkCenterDbDto dto : dbDtos) {
					dto.setNewIsDel(RootModel.DEL);
					dto.setIsDel(RootModel.NOT_DEL);
				}
				iDbDao.dels(dbDtos);
			}
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}

	}

}
