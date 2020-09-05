package com.dotop.smartwater.project.module.service.workcenter.impl;

import java.util.List;

import com.dotop.smartwater.project.module.service.workcenter.IFormService;
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
import com.dotop.smartwater.project.module.core.water.bo.WorkCenterFormBo;
import com.dotop.smartwater.project.module.core.water.dto.WorkCenterFormDto;
import com.dotop.smartwater.project.module.core.water.vo.WorkCenterFormVo;
import com.dotop.smartwater.project.module.dao.workcenter.IFormDao;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

@Service("IWorkCenterFormService")
public class FormServiceImpl implements IFormService {

	private static final Logger LOGGER = LogManager.getLogger(FormServiceImpl.class);

	@Autowired
	private IFormDao iFormDao;

	@Override
	public Pagination<WorkCenterFormVo> page(WorkCenterFormBo formBo) {
		try {
			WorkCenterFormDto formDto = BeanUtils.copy(formBo, WorkCenterFormDto.class);
			formDto.setIsDel(RootModel.NOT_DEL);
			Page<WorkCenterFormVo> pageHelper = PageHelper.startPage(formBo.getPage(), formBo.getPageCount());
			List<WorkCenterFormVo> list = iFormDao.list(formDto);
			return new Pagination<>(pageHelper.getPageSize(), pageHelper.getPageNum(), list, pageHelper.getTotal());
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public WorkCenterFormVo get(WorkCenterFormBo formBo) {
		try {
			WorkCenterFormDto formDto = BeanUtils.copy(formBo, WorkCenterFormDto.class);
			formDto.setIsDel(RootModel.NOT_DEL);
			return iFormDao.get(formDto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public WorkCenterFormVo add(WorkCenterFormBo formBo) {
		try {
			WorkCenterFormDto formDto = BeanUtils.copy(formBo, WorkCenterFormDto.class);
			formDto.setIsDel(RootModel.NOT_DEL);
			iFormDao.add(formDto);
			WorkCenterFormVo workCenterDbVo = new WorkCenterFormVo();
			workCenterDbVo.setId(formDto.getId());
			return workCenterDbVo;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public WorkCenterFormVo edit(WorkCenterFormBo formBo) {
		try {
			WorkCenterFormDto formDto = BeanUtils.copy(formBo, WorkCenterFormDto.class);
			formDto.setIsDel(RootModel.NOT_DEL);
			iFormDao.edit(formDto);
			return null;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public String del(WorkCenterFormBo formBo) {
		try {
			WorkCenterFormDto formDto = BeanUtils.copy(formBo, WorkCenterFormDto.class);
			formDto.setId(formBo.getId());
			formDto.setEnterpriseid(formBo.getEnterpriseid());
			formDto.setIsDel(RootModel.NOT_DEL);
			formDto.setNewIsDel(RootModel.DEL);
			iFormDao.del(formDto);
			return null;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

}
