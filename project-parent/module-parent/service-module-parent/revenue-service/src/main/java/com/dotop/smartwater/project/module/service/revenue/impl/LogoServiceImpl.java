package com.dotop.smartwater.project.module.service.revenue.impl;

import com.dotop.smartwater.project.module.service.revenue.ILogoService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.project.module.core.auth.bo.LogoBo;
import com.dotop.smartwater.project.module.core.auth.dto.LogoDto;
import com.dotop.smartwater.project.module.core.auth.vo.LogoVo;
import com.dotop.smartwater.project.module.dao.revenue.ILogoDao;

/**
 * logo 操作
 * 

 * @date 2019年2月25日
 */
@Service
public class LogoServiceImpl implements ILogoService {

	private static final Logger LOGGER = LogManager.getLogger(LogoServiceImpl.class);

	@Autowired
	private ILogoDao iLogoDao;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public LogoVo add(LogoBo logoBo) {
		try {
			LogoDto logoDto = new LogoDto();
			BeanUtils.copyProperties(logoBo, logoDto);
			iLogoDao.add(logoDto);
			LogoVo logoVo = new LogoVo();
			BeanUtils.copyProperties(logoBo, logoVo);
			return logoVo;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public LogoVo get(LogoBo logoBo) {
		try {
			LogoDto logoDto = new LogoDto();
			BeanUtils.copyProperties(logoBo, logoDto);
			return iLogoDao.get(logoDto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public String del(LogoBo logoBo) {
		try {
			LogoDto logoDto = new LogoDto();
			BeanUtils.copyProperties(logoBo, logoDto);
			iLogoDao.del(logoDto);
			return null;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

}
