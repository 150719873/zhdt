package com.dotop.smartwater.project.module.service.workcenter.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.dotop.smartwater.dependence.core.common.RootModel;
import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.utils.BeanUtils;
import com.dotop.smartwater.project.module.core.water.bo.WorkCenterProcessNodeBo;
import com.dotop.smartwater.project.module.core.water.dto.WorkCenterProcessNodeDto;
import com.dotop.smartwater.project.module.core.water.vo.WorkCenterProcessNodeVo;
import com.dotop.smartwater.project.module.dao.workcenter.IProcessNodeDao;
import com.dotop.smartwater.project.module.service.workcenter.IProcessNodeService;

@Service("IProcessTmplNodeService")
public class ProcessNodeServiceImpl implements IProcessNodeService {

	private static final Logger LOGGER = LogManager.getLogger(ProcessNodeServiceImpl.class);

	@Autowired
	private IProcessNodeDao iProcessNodeDao;

	@Override
	public WorkCenterProcessNodeVo add(WorkCenterProcessNodeBo processNodeBo) {
		try {
			WorkCenterProcessNodeDto workCenterProcessNodeDto = BeanUtils.copy(processNodeBo,
					WorkCenterProcessNodeDto.class);
			workCenterProcessNodeDto.setIsDel(RootModel.NOT_DEL);
			iProcessNodeDao.add(workCenterProcessNodeDto);
			return null;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public WorkCenterProcessNodeVo get(WorkCenterProcessNodeBo processNodeBo) {
		try {
			WorkCenterProcessNodeDto processNodeDto = BeanUtils.copy(processNodeBo, WorkCenterProcessNodeDto.class);
			processNodeBo.setIsDel(RootModel.NOT_DEL);
			return iProcessNodeDao.get(processNodeDto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

}
