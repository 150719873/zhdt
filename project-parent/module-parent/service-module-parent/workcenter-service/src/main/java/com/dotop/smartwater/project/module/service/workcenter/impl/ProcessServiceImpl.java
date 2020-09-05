package com.dotop.smartwater.project.module.service.workcenter.impl;

import java.util.List;

import com.dotop.smartwater.project.module.service.workcenter.IProcessService;
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
import com.dotop.smartwater.project.module.core.water.bo.WorkCenterProcessBo;
import com.dotop.smartwater.project.module.core.water.dto.WorkCenterProcessDto;
import com.dotop.smartwater.project.module.core.water.vo.WorkCenterProcessVo;
import com.dotop.smartwater.project.module.dao.workcenter.IProcessDao;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

@Service("IWorkCenterProcessService")
public class ProcessServiceImpl implements IProcessService {

	private static final Logger LOGGER = LogManager.getLogger(ProcessServiceImpl.class);

	@Autowired
	private IProcessDao iProcessDao;

	@Override
	public Pagination<WorkCenterProcessVo> page(WorkCenterProcessBo processBo) {
		try {
			WorkCenterProcessDto processDto = BeanUtils.copy(processBo, WorkCenterProcessDto.class);
			processDto.setIsDel(RootModel.NOT_DEL);
			Page<WorkCenterProcessVo> pageHelper = PageHelper.startPage(processBo.getPage(), processBo.getPageCount());
			List<WorkCenterProcessVo> list = iProcessDao.list(processDto);
			return new Pagination<>(pageHelper.getPageSize(), pageHelper.getPageNum(), list, pageHelper.getTotal());
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public List<WorkCenterProcessVo> list(WorkCenterProcessBo processBo) {
		try {
			WorkCenterProcessDto processDto = BeanUtils.copy(processBo, WorkCenterProcessDto.class);
			processDto.setIsDel(RootModel.NOT_DEL);
			processDto.setOffset((processBo.getPage() - 1) * processBo.getPageCount());
			processDto.setLimit(processBo.getPageCount());
			return iProcessDao.list(processDto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public WorkCenterProcessVo get(WorkCenterProcessBo processBo) {
		try {
			WorkCenterProcessDto processDto = BeanUtils.copy(processBo, WorkCenterProcessDto.class);
			processDto.setIsDel(RootModel.NOT_DEL);
			return iProcessDao.get(processDto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public WorkCenterProcessVo add(WorkCenterProcessBo processBo) {
		try {
			WorkCenterProcessDto processDto = BeanUtils.copy(processBo, WorkCenterProcessDto.class);
			processDto.setIsDel(RootModel.NOT_DEL);
			iProcessDao.add(processDto);
			return null;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public WorkCenterProcessVo edit(WorkCenterProcessBo processBo) {
		try {
			WorkCenterProcessDto processDto = BeanUtils.copy(processBo, WorkCenterProcessDto.class);
			processDto.setIsDel(RootModel.NOT_DEL);
			iProcessDao.edit(processDto);
			return null;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public WorkCenterProcessVo editNext(WorkCenterProcessBo processBo) {
		try {
			WorkCenterProcessDto processDto = BeanUtils.copy(processBo, WorkCenterProcessDto.class);
			processDto.setIsDel(RootModel.NOT_DEL);
			iProcessDao.editNext(processDto);
			return null;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public String del(WorkCenterProcessBo processBo) {
		try {
			WorkCenterProcessDto processDto = BeanUtils.copy(processBo, WorkCenterProcessDto.class);
			processDto.setId(processBo.getId());
			processDto.setEnterpriseid(processBo.getEnterpriseid());
			processDto.setIsDel(RootModel.NOT_DEL);
			processDto.setNewIsDel(RootModel.DEL);
			iProcessDao.del(processDto);
			return null;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

}
