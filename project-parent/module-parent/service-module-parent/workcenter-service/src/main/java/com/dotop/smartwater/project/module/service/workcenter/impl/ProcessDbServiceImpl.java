package com.dotop.smartwater.project.module.service.workcenter.impl;

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
import com.dotop.smartwater.project.module.core.water.bo.WorkCenterProcessDbBo;
import com.dotop.smartwater.project.module.core.water.dto.WorkCenterProcessDbDto;
import com.dotop.smartwater.project.module.core.water.vo.WorkCenterProcessDbVo;
import com.dotop.smartwater.project.module.dao.workcenter.IProcessDbDao;
import com.dotop.smartwater.project.module.service.workcenter.IProcessDbService;

@Service("IProcessDbService")
public class ProcessDbServiceImpl implements IProcessDbService {

	private static final Logger LOGGER = LogManager.getLogger(ProcessDbServiceImpl.class);

	@Autowired
	private IProcessDbDao iProcessDbDao;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public WorkCenterProcessDbVo add(WorkCenterProcessDbBo workCenterProcessDbBo) {
		try {
			WorkCenterProcessDbDto workCenterProcessDbDto = BeanUtils.copy(workCenterProcessDbBo,
					WorkCenterProcessDbDto.class);
			workCenterProcessDbDto.setIsDel(RootModel.NOT_DEL);
			iProcessDbDao.add(workCenterProcessDbDto);
			return null;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

}
