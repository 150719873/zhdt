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
import com.dotop.smartwater.project.module.core.water.bo.WorkCenterTmplBo;
import com.dotop.smartwater.project.module.core.water.dto.WorkCenterTmplDto;
import com.dotop.smartwater.project.module.core.water.vo.WorkCenterTmplVo;
import com.dotop.smartwater.project.module.dao.workcenter.ITmplDao;
import com.dotop.smartwater.project.module.service.workcenter.ITmplService;

@Service("IWorkCenterTmplService")
public class TmplServiceImpl implements ITmplService {

	private static final Logger LOGGER = LogManager.getLogger(TmplServiceImpl.class);

	@Autowired
	private ITmplDao iTmplDao;

	@Override
	public Pagination<WorkCenterTmplVo> page(WorkCenterTmplBo tmplBo) {
		try {
			WorkCenterTmplDto tmplDto = BeanUtils.copy(tmplBo, WorkCenterTmplDto.class);
			tmplDto.setIsDel(RootModel.NOT_DEL);
			tmplDto.setOffset((tmplBo.getPage() - 1) * tmplBo.getPageCount());
			tmplDto.setLimit(tmplBo.getPageCount());
			List<WorkCenterTmplVo> list = iTmplDao.list(tmplDto);
			Integer count = iTmplDao.listCount(tmplDto);
			return new Pagination<>(tmplBo.getPage(), tmplBo.getPageCount(), list, count);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public WorkCenterTmplVo get(WorkCenterTmplBo tmplBo) {
		try {
			WorkCenterTmplDto tmplDto = BeanUtils.copy(tmplBo, WorkCenterTmplDto.class);
			tmplDto.setIsDel(RootModel.NOT_DEL);
			return iTmplDao.get(tmplDto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public WorkCenterTmplVo add(WorkCenterTmplBo tmplBo) {
		try {
			WorkCenterTmplDto tmplDto = BeanUtils.copy(tmplBo, WorkCenterTmplDto.class);
			tmplDto.setIsDel(RootModel.NOT_DEL);
			iTmplDao.add(tmplDto);
			WorkCenterTmplVo workCenterTmplVo = new WorkCenterTmplVo();
			workCenterTmplVo.setId(tmplDto.getId());
			return workCenterTmplVo;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public WorkCenterTmplVo edit(WorkCenterTmplBo tmplBo) {
		try {
			WorkCenterTmplDto tmplDto = BeanUtils.copy(tmplBo, WorkCenterTmplDto.class);
			tmplDto.setIsDel(RootModel.NOT_DEL);
			iTmplDao.edit(tmplDto);
			return null;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public String del(WorkCenterTmplBo tmplBo) {
		try {
			WorkCenterTmplDto tmplDto = BeanUtils.copy(tmplBo, WorkCenterTmplDto.class);
			tmplDto.setId(tmplBo.getId());
			tmplDto.setEnterpriseid(tmplBo.getEnterpriseid());
			tmplDto.setIsDel(RootModel.NOT_DEL);
			tmplDto.setNewIsDel(RootModel.DEL);
			iTmplDao.del(tmplDto);
			return null;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public List<WorkCenterTmplVo> list(WorkCenterTmplBo tmplBo) {
		try {
			WorkCenterTmplDto tmplDto = BeanUtils.copy(tmplBo, WorkCenterTmplDto.class);
			tmplDto.setIsDel(RootModel.NOT_DEL);
			tmplDto.setIfSort(false);
			return iTmplDao.list(tmplDto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

}
