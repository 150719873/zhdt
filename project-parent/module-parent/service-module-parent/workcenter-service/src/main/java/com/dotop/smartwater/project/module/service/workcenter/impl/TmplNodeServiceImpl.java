package com.dotop.smartwater.project.module.service.workcenter.impl;

import java.util.List;

import com.dotop.smartwater.project.module.core.water.dto.WorkCenterTmplNodeEdgeDto;
import com.dotop.smartwater.project.module.core.water.dto.WorkCenterTmplNodePointDto;
import com.dotop.smartwater.project.module.core.water.vo.WorkCenterTmplNodeEdgeVo;
import com.dotop.smartwater.project.module.core.water.vo.WorkCenterTmplNodePointVo;
import com.dotop.smartwater.project.module.core.water.vo.customize.WorkCenterTmplNodeGraphVo;
import com.dotop.smartwater.project.module.dao.workcenter.ITmplNodeEdgeDao;
import com.dotop.smartwater.project.module.dao.workcenter.ITmplNodePointDao;
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
import com.dotop.smartwater.dependence.core.utils.UuidUtils;
import com.dotop.smartwater.project.module.core.water.bo.WorkCenterTmplNodeBo;
import com.dotop.smartwater.project.module.core.water.dto.WorkCenterTmplNodeDto;
import com.dotop.smartwater.project.module.core.water.vo.WorkCenterTmplNodeVo;
import com.dotop.smartwater.project.module.dao.workcenter.ITmplNodeDao;
import com.dotop.smartwater.project.module.service.workcenter.ITmplNodeService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

@Service("IWorkCenterTmplNodeService")
public class TmplNodeServiceImpl implements ITmplNodeService {

	private static final Logger LOGGER = LogManager.getLogger(TmplNodeServiceImpl.class);

	@Autowired
	private ITmplNodeDao iTmplNodeDao;

	@Autowired
	private ITmplNodePointDao iTmplNodePointDao;

	@Autowired
	private ITmplNodeEdgeDao iTmplNodeEdgeDao;

	@Override
	public Pagination<WorkCenterTmplNodeVo> page(WorkCenterTmplNodeBo tmplNodeBo) {
		try {
			WorkCenterTmplNodeDto tmplNodeDto = BeanUtils.copy(tmplNodeBo, WorkCenterTmplNodeDto.class);
			tmplNodeDto.setIsDel(RootModel.NOT_DEL);
			Page<WorkCenterTmplNodeVo> pageHelper = PageHelper.startPage(tmplNodeBo.getPage(),
					tmplNodeBo.getPageCount());
			List<WorkCenterTmplNodeVo> list = iTmplNodeDao.list(tmplNodeDto);
			return new Pagination<>(pageHelper.getPageSize(), pageHelper.getPageNum(), list, pageHelper.getTotal());
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public WorkCenterTmplNodeGraphVo graph(WorkCenterTmplNodeBo workCenterTmplNodeBo) {
		try {
			WorkCenterTmplNodeGraphVo tmplNodeGraphVo = new WorkCenterTmplNodeGraphVo();
			WorkCenterTmplNodePointDto workCenterTmplNodePointDto = BeanUtils.copy(workCenterTmplNodeBo, WorkCenterTmplNodePointDto.class);
			workCenterTmplNodePointDto.setIsDel(RootModel.NOT_DEL);
			WorkCenterTmplNodeEdgeDto workCenterTmplNodeEdgeDto = BeanUtils.copy(workCenterTmplNodeBo, WorkCenterTmplNodeEdgeDto.class);
			workCenterTmplNodeEdgeDto.setIsDel(RootModel.NOT_DEL);
			List<WorkCenterTmplNodePointVo> nodes = iTmplNodePointDao.list(workCenterTmplNodePointDto);
			List<WorkCenterTmplNodeEdgeVo> edges = iTmplNodeEdgeDao.list(workCenterTmplNodeEdgeDto);
			tmplNodeGraphVo.setNodes(nodes);
			tmplNodeGraphVo.setEdges(edges);
			return tmplNodeGraphVo;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public WorkCenterTmplNodeVo get(WorkCenterTmplNodeBo tmplNodeBo) {
		try {
			WorkCenterTmplNodeDto tmplNodeDto = BeanUtils.copy(tmplNodeBo, WorkCenterTmplNodeDto.class);
			tmplNodeDto.setIsDel(RootModel.NOT_DEL);
			return iTmplNodeDao.get(tmplNodeDto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public WorkCenterTmplNodeVo add(WorkCenterTmplNodeBo tmplNodeBo) {
		try {
			WorkCenterTmplNodeDto tmplNodeDto = BeanUtils.copy(tmplNodeBo, WorkCenterTmplNodeDto.class);
			tmplNodeDto.setId(UuidUtils.getUuid());
			tmplNodeDto.setIsDel(RootModel.NOT_DEL);
			iTmplNodeDao.add(tmplNodeDto);
			WorkCenterTmplNodeVo workCenterTmplNodeVo = new WorkCenterTmplNodeVo();
			workCenterTmplNodeVo.setId(tmplNodeDto.getId());
			return workCenterTmplNodeVo;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public WorkCenterTmplNodeVo edit(WorkCenterTmplNodeBo tmplNodeBo) {
		try {
			WorkCenterTmplNodeDto tmplNodeDto = BeanUtils.copy(tmplNodeBo, WorkCenterTmplNodeDto.class);
			tmplNodeDto.setIsDel(RootModel.NOT_DEL);
			iTmplNodeDao.edit(tmplNodeDto);
			return null;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public String del(WorkCenterTmplNodeBo tmplNodeBo) {
		try {
			WorkCenterTmplNodeDto tmplNodeDto = BeanUtils.copy(tmplNodeBo, WorkCenterTmplNodeDto.class);
			tmplNodeDto.setEnterpriseid(tmplNodeBo.getEnterpriseid());
			tmplNodeDto.setIsDel(RootModel.NOT_DEL);
			tmplNodeDto.setNewIsDel(RootModel.DEL);
			iTmplNodeDao.del(tmplNodeDto);
			return null;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public List<WorkCenterTmplNodeVo> list(WorkCenterTmplNodeBo tmplNodeBo) {
		try {
			WorkCenterTmplNodeDto tmplNodeDto = BeanUtils.copy(tmplNodeBo, WorkCenterTmplNodeDto.class);
			tmplNodeDto.setIsDel(RootModel.NOT_DEL);
			return iTmplNodeDao.list(tmplNodeDto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public void adds(List<WorkCenterTmplNodeBo> tmplNodeBos) {
		try {
			if (tmplNodeBos != null && !tmplNodeBos.isEmpty()) {
				List<WorkCenterTmplNodeDto> tmplNodeDtos = BeanUtils.copy(tmplNodeBos, WorkCenterTmplNodeDto.class);
				for (WorkCenterTmplNodeDto dto : tmplNodeDtos) {
					dto.setIsDel(RootModel.NOT_DEL);
				}
				iTmplNodeDao.adds(tmplNodeDtos);
			}
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public void edits(List<WorkCenterTmplNodeBo> tmplNodeBos) {
		try {
			if (tmplNodeBos != null && !tmplNodeBos.isEmpty()) {
				List<WorkCenterTmplNodeDto> tmplNodeDtos = BeanUtils.copy(tmplNodeBos, WorkCenterTmplNodeDto.class);
				for (WorkCenterTmplNodeDto dto : tmplNodeDtos) {
					dto.setIsDel(RootModel.NOT_DEL);
				}
				iTmplNodeDao.edits(tmplNodeDtos);
			}
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public void dels(List<WorkCenterTmplNodeBo> tmplNodeBos) {
		try {
			if (tmplNodeBos != null && !tmplNodeBos.isEmpty()) {
				List<WorkCenterTmplNodeDto> tmplNodeDtos = BeanUtils.copy(tmplNodeBos, WorkCenterTmplNodeDto.class);
				for (WorkCenterTmplNodeDto dto : tmplNodeDtos) {
					dto.setNewIsDel(RootModel.DEL);
					dto.setIsDel(RootModel.NOT_DEL);
				}
				iTmplNodeDao.dels(tmplNodeDtos);
			}
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

}
