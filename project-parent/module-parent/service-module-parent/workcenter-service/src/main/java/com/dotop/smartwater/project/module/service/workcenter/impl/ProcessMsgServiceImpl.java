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
import com.dotop.smartwater.dependence.core.utils.UuidUtils;
import com.dotop.smartwater.project.module.core.water.bo.WorkCenterProcessMsgBo;
import com.dotop.smartwater.project.module.core.water.dto.WorkCenterProcessMsgDto;
import com.dotop.smartwater.project.module.core.water.vo.WorkCenterProcessMsgVo;
import com.dotop.smartwater.project.module.dao.workcenter.IProcessMsgDao;
import com.dotop.smartwater.project.module.service.workcenter.IProcessMsgService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

@Service("IProcessMsgService")
public class ProcessMsgServiceImpl implements IProcessMsgService {

	private static final Logger LOGGER = LogManager.getLogger(ProcessMsgServiceImpl.class);

	@Autowired
	private IProcessMsgDao iProcessMsgDao;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public WorkCenterProcessMsgVo add(WorkCenterProcessMsgBo processMsgBo) {
		try {
			WorkCenterProcessMsgDto processMsgDto = BeanUtils.copy(processMsgBo, WorkCenterProcessMsgDto.class);
			processMsgDto.setId(UuidUtils.getUuid());
			processMsgDto.setIsDel(RootModel.NOT_DEL);
			iProcessMsgDao.add(processMsgDto);
			WorkCenterProcessMsgVo processMsgVo = new WorkCenterProcessMsgVo();
			processMsgVo.setId(processMsgDto.getId());
			return processMsgVo;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public Pagination<WorkCenterProcessMsgVo> page(WorkCenterProcessMsgBo processMsgBo) {
		try {
			WorkCenterProcessMsgDto processMsgDto = BeanUtils.copy(processMsgBo, WorkCenterProcessMsgDto.class);
			processMsgDto.setIsDel(RootModel.NOT_DEL);
			Page<WorkCenterProcessMsgVo> pageHelper = PageHelper.startPage(processMsgBo.getPage(),
					processMsgBo.getPageCount());
			List<WorkCenterProcessMsgVo> list = iProcessMsgDao.list(processMsgDto);
			return new Pagination<>(pageHelper.getPageSize(), pageHelper.getPageNum(), list, pageHelper.getTotal());
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public List<String> listProcessId(WorkCenterProcessMsgBo processMsgBo) {
		try {
			WorkCenterProcessMsgDto processMsgDto = BeanUtils.copy(processMsgBo, WorkCenterProcessMsgDto.class);
			processMsgDto.setIsDel(RootModel.NOT_DEL);
			return iProcessMsgDao.listProcessId(processMsgDto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

}
