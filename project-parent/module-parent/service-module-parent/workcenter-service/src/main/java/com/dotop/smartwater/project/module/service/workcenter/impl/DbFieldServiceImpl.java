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
import com.dotop.smartwater.dependence.core.utils.BeanUtils;
import com.dotop.smartwater.dependence.core.utils.UuidUtils;
import com.dotop.smartwater.project.module.core.water.bo.WorkCenterDbFieldBo;
import com.dotop.smartwater.project.module.core.water.dto.WorkCenterDbFieldDto;
import com.dotop.smartwater.project.module.core.water.vo.WorkCenterDbFieldVo;
import com.dotop.smartwater.project.module.dao.workcenter.IDbFieldDao;
import com.dotop.smartwater.project.module.service.workcenter.IDbFieldService;

@Service
public class DbFieldServiceImpl implements IDbFieldService {

	private static final Logger LOGGER = LogManager.getLogger(DbFieldServiceImpl.class);

	@Autowired
	private IDbFieldDao iDbFieldDao;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public WorkCenterDbFieldVo add(WorkCenterDbFieldBo dbFieldBo) {
		try {
			WorkCenterDbFieldDto dbFieldDto = BeanUtils.copy(dbFieldBo, WorkCenterDbFieldDto.class);
			dbFieldDto.setId(UuidUtils.getUuid());
			dbFieldDto.setIsDel(RootModel.NOT_DEL);
			iDbFieldDao.add(dbFieldDto);
			WorkCenterDbFieldVo workCenterDbFieldVo = new WorkCenterDbFieldVo();
			workCenterDbFieldVo.setId(dbFieldDto.getId());
			return workCenterDbFieldVo;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public void adds(List<WorkCenterDbFieldBo> dbFieldBos) {
		try {
			if (dbFieldBos != null && !dbFieldBos.isEmpty()) {
				List<WorkCenterDbFieldDto> dbFieldDtos = BeanUtils.copy(dbFieldBos, WorkCenterDbFieldDto.class);
				for (WorkCenterDbFieldDto dto : dbFieldDtos) {
					dto.setIsDel(RootModel.NOT_DEL);
				}
				iDbFieldDao.adds(dbFieldDtos);
			}
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public void edits(List<WorkCenterDbFieldBo> dbFieldBos) {
		try {
			if (dbFieldBos != null && !dbFieldBos.isEmpty()) {
				List<WorkCenterDbFieldDto> dbFieldDtos = BeanUtils.copy(dbFieldBos, WorkCenterDbFieldDto.class);
				for (WorkCenterDbFieldDto dto : dbFieldDtos) {
					dto.setIsDel(RootModel.NOT_DEL);
				}
				iDbFieldDao.edits(dbFieldDtos);
			}
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public void dels(List<WorkCenterDbFieldBo> dbFieldBos) {
		try {
			if (dbFieldBos != null && !dbFieldBos.isEmpty()) {
				List<WorkCenterDbFieldDto> dbFieldDtos = BeanUtils.copy(dbFieldBos, WorkCenterDbFieldDto.class);
				for (WorkCenterDbFieldDto dto : dbFieldDtos) {
					dto.setNewIsDel(RootModel.DEL);
					dto.setIsDel(RootModel.NOT_DEL);
				}
				iDbFieldDao.dels(dbFieldDtos);
			}
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public List<WorkCenterDbFieldVo> list(WorkCenterDbFieldBo dbFieldBo) {
		try {
			WorkCenterDbFieldDto dbFieldDto = BeanUtils.copy(dbFieldBo, WorkCenterDbFieldDto.class);
			dbFieldDto.setIsDel(RootModel.NOT_DEL);
			return iDbFieldDao.list(dbFieldDto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public String del(WorkCenterDbFieldBo dbFieldBo) {
		try {
			WorkCenterDbFieldDto dbFieldDto = BeanUtils.copy(dbFieldBo, WorkCenterDbFieldDto.class);
			dbFieldDto.setNewIsDel(RootModel.DEL);
			dbFieldDto.setIsDel(RootModel.NOT_DEL);
			iDbFieldDao.del(dbFieldDto);
			return null;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}
}
