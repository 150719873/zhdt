package com.dotop.smartwater.project.module.service.workcenter.impl;

import com.dotop.smartwater.dependence.core.common.RootModel;
import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.utils.BeanUtils;
import com.dotop.smartwater.dependence.core.utils.UuidUtils;
import com.dotop.smartwater.project.module.core.water.bo.WorkCenterTmplNodePointBo;
import com.dotop.smartwater.project.module.core.water.dto.WorkCenterTmplNodePointDto;
import com.dotop.smartwater.project.module.core.water.vo.WorkCenterTmplNodePointVo;
import com.dotop.smartwater.project.module.dao.workcenter.ITmplNodePointDao;
import com.dotop.smartwater.project.module.service.workcenter.ITmplNodePointService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service("ITmplNodePointService")
public class TmplNodePointServiceImpl implements ITmplNodePointService {

	private static final Logger LOGGER = LogManager.getLogger(TmplNodePointServiceImpl.class);

	@Autowired
	private ITmplNodePointDao iTmplNodePointDao;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public WorkCenterTmplNodePointVo add(WorkCenterTmplNodePointBo workCenterTmplNodePointBo) {
		try {
			WorkCenterTmplNodePointDto tmplNodePointDto = BeanUtils.copy(workCenterTmplNodePointBo, WorkCenterTmplNodePointDto.class);
			tmplNodePointDto.setId(UuidUtils.getUuid());
			tmplNodePointDto.setIsDel(RootModel.NOT_DEL);
			iTmplNodePointDao.add(tmplNodePointDto);
			WorkCenterTmplNodePointVo workCenterTmplNodePointVo = new WorkCenterTmplNodePointVo();
			workCenterTmplNodePointVo.setId(tmplNodePointDto.getId());
			return workCenterTmplNodePointVo;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public List<WorkCenterTmplNodePointVo> list(WorkCenterTmplNodePointBo workCenterTmplNodePointBo) {
		try {
			WorkCenterTmplNodePointDto tmplNodePointDto = BeanUtils.copy(workCenterTmplNodePointBo, WorkCenterTmplNodePointDto.class);
			tmplNodePointDto.setIsDel(RootModel.NOT_DEL);
			return iTmplNodePointDao.list(tmplNodePointDto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public String del(WorkCenterTmplNodePointBo workCenterTmplNodePointBo) {
		try {
			WorkCenterTmplNodePointDto tmplNodeDto = BeanUtils.copy(workCenterTmplNodePointBo, WorkCenterTmplNodePointDto.class);
			tmplNodeDto.setEnterpriseid(workCenterTmplNodePointBo.getEnterpriseid());
			tmplNodeDto.setIsDel(RootModel.NOT_DEL);
			tmplNodeDto.setNewIsDel(RootModel.DEL);
			iTmplNodePointDao.del(tmplNodeDto);
			return null;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public void adds(List<WorkCenterTmplNodePointBo> workCenterTmplNodePointBos) {
		try {
			if (!CollectionUtils.isEmpty(workCenterTmplNodePointBos)) {
				List<WorkCenterTmplNodePointDto> tmplNodeDtos = BeanUtils.copy(workCenterTmplNodePointBos, WorkCenterTmplNodePointDto.class);
				for (WorkCenterTmplNodePointDto dto : tmplNodeDtos) {
					dto.setIsDel(RootModel.NOT_DEL);
				}
				iTmplNodePointDao.adds(tmplNodeDtos);
			}
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public void edits(List<WorkCenterTmplNodePointBo> workCenterTmplNodePointBos) {
		try {
			if (!CollectionUtils.isEmpty(workCenterTmplNodePointBos)) {
				List<WorkCenterTmplNodePointDto> tmplNodeDtos = BeanUtils.copy(workCenterTmplNodePointBos, WorkCenterTmplNodePointDto.class);
				for (WorkCenterTmplNodePointDto dto : tmplNodeDtos) {
					dto.setIsDel(RootModel.NOT_DEL);
				}
				iTmplNodePointDao.edits(tmplNodeDtos);
			}
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public void dels(List<WorkCenterTmplNodePointBo> workCenterTmplNodePointBos) {
		try {
			if (!CollectionUtils.isEmpty(workCenterTmplNodePointBos)) {
				List<WorkCenterTmplNodePointDto> tmplNodeDtos = BeanUtils.copy(workCenterTmplNodePointBos, WorkCenterTmplNodePointDto.class);
				for (WorkCenterTmplNodePointDto dto : tmplNodeDtos) {
					dto.setNewIsDel(RootModel.DEL);
					dto.setIsDel(RootModel.NOT_DEL);
				}
				iTmplNodePointDao.dels(tmplNodeDtos);
			}
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}
}
