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
import com.dotop.smartwater.project.module.core.water.bo.WorkCenterProcessDbFieldBo;
import com.dotop.smartwater.project.module.core.water.dto.WorkCenterProcessDbFieldDto;
import com.dotop.smartwater.project.module.core.water.vo.WorkCenterProcessDbFieldVo;
import com.dotop.smartwater.project.module.dao.workcenter.IProcessDbFieldDao;
import com.dotop.smartwater.project.module.service.workcenter.IProcessDbFieldService;

@Service("IProcessDbFieldService")
public class ProcessDbFieldServiceImpl implements IProcessDbFieldService {

	private static final Logger LOGGER = LogManager.getLogger(ProcessDbFieldServiceImpl.class);

	@Autowired
	private IProcessDbFieldDao iProcessDbFieldDao;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public WorkCenterProcessDbFieldVo add(WorkCenterProcessDbFieldBo processDbFieldBo) {
		try {
			WorkCenterProcessDbFieldDto workCenterProcessDbFieldDto = BeanUtils.copy(processDbFieldBo,
					WorkCenterProcessDbFieldDto.class);
			workCenterProcessDbFieldDto.setIsDel(RootModel.NOT_DEL);
			iProcessDbFieldDao.add(workCenterProcessDbFieldDto);
			return null;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

}
