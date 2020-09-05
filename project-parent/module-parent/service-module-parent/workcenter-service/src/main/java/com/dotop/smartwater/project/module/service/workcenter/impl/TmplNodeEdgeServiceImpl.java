package com.dotop.smartwater.project.module.service.workcenter.impl;

import com.dotop.smartwater.dependence.core.common.RootModel;
import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.utils.BeanUtils;
import com.dotop.smartwater.dependence.core.utils.UuidUtils;
import com.dotop.smartwater.project.module.core.water.bo.WorkCenterTmplNodeEdgeBo;
import com.dotop.smartwater.project.module.core.water.dto.WorkCenterTmplNodeEdgeDto;
import com.dotop.smartwater.project.module.core.water.vo.WorkCenterTmplNodeEdgeVo;
import com.dotop.smartwater.project.module.dao.workcenter.ITmplNodeEdgeDao;
import com.dotop.smartwater.project.module.service.workcenter.ITmplNodeEdgeService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service("ITmplNodeEdgeService")
public class TmplNodeEdgeServiceImpl implements ITmplNodeEdgeService {

	private static final Logger LOGGER = LogManager.getLogger(TmplNodeEdgeServiceImpl.class);

	@Autowired
	private ITmplNodeEdgeDao iTmplNodeEdgeDao;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public WorkCenterTmplNodeEdgeVo add(WorkCenterTmplNodeEdgeBo workCenterTmplNodeEdgeBo) {
		try {
			WorkCenterTmplNodeEdgeDto tmplNodeEdgeDto = BeanUtils.copy(workCenterTmplNodeEdgeBo, WorkCenterTmplNodeEdgeDto.class);
			tmplNodeEdgeDto.setId(UuidUtils.getUuid());
			tmplNodeEdgeDto.setIsDel(RootModel.NOT_DEL);
			iTmplNodeEdgeDao.add(tmplNodeEdgeDto);
			WorkCenterTmplNodeEdgeVo workCenterTmplNodeVo = new WorkCenterTmplNodeEdgeVo();
			workCenterTmplNodeVo.setId(tmplNodeEdgeDto.getId());
			return workCenterTmplNodeVo;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public String del(WorkCenterTmplNodeEdgeBo workCenterTmplNodeEdgeBo) {
		try {
			WorkCenterTmplNodeEdgeDto tmplNodeDto = BeanUtils.copy(workCenterTmplNodeEdgeBo, WorkCenterTmplNodeEdgeDto.class);
			tmplNodeDto.setEnterpriseid(workCenterTmplNodeEdgeBo.getEnterpriseid());
			tmplNodeDto.setIsDel(RootModel.NOT_DEL);
			tmplNodeDto.setNewIsDel(RootModel.DEL);
			iTmplNodeEdgeDao.del(tmplNodeDto);
			return null;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public List<WorkCenterTmplNodeEdgeVo> list(WorkCenterTmplNodeEdgeBo workCenterTmplNodeEdgeBo) {
		try {
			WorkCenterTmplNodeEdgeDto tmplNodeDto = BeanUtils.copy(workCenterTmplNodeEdgeBo, WorkCenterTmplNodeEdgeDto.class);
			tmplNodeDto.setIsDel(RootModel.NOT_DEL);
			return iTmplNodeEdgeDao.list(tmplNodeDto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public void adds(List<WorkCenterTmplNodeEdgeBo> workCenterTmplNodeEdgeBos) {
		try {
			if (!CollectionUtils.isEmpty(workCenterTmplNodeEdgeBos)) {
				List<WorkCenterTmplNodeEdgeDto> tmplNodeDtos = BeanUtils.copy(workCenterTmplNodeEdgeBos, WorkCenterTmplNodeEdgeDto.class);
				for (WorkCenterTmplNodeEdgeDto dto : tmplNodeDtos) {
					dto.setIsDel(RootModel.NOT_DEL);
				}
				iTmplNodeEdgeDao.adds(tmplNodeDtos);
			}
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}
}
