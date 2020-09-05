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
import com.dotop.smartwater.project.module.core.water.bo.WorkCenterProcessFormBo;
import com.dotop.smartwater.project.module.core.water.dto.WorkCenterProcessFormDto;
import com.dotop.smartwater.project.module.core.water.vo.WorkCenterProcessFormVo;
import com.dotop.smartwater.project.module.dao.workcenter.IProcessFormDao;
import com.dotop.smartwater.project.module.service.workcenter.IProcessFormService;

@Service("IProcessFormService")
public class ProcessFormServiceImpl implements IProcessFormService {

	private static final Logger LOGGER = LogManager.getLogger(ProcessFormServiceImpl.class);

	@Autowired
	private IProcessFormDao iProcessFormDao;

	@Override
	public WorkCenterProcessFormVo add(WorkCenterProcessFormBo workCenterProcessFormBo) {
		try {
			WorkCenterProcessFormDto workCenterProcessFormDto = BeanUtils.copy(workCenterProcessFormBo,
					WorkCenterProcessFormDto.class);
			workCenterProcessFormDto.setIsDel(RootModel.NOT_DEL);
			iProcessFormDao.add(workCenterProcessFormDto);
			return null;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

}
