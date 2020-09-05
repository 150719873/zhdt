package com.dotop.smartwater.project.module.service.demo.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.project.module.core.demo.bo.HelloBo;
import com.dotop.smartwater.project.module.core.demo.dto.HelloDto;
import com.dotop.smartwater.project.module.core.demo.vo.HelloVo;
import com.dotop.smartwater.project.module.dao.demo.IHelloDao;
import com.dotop.smartwater.project.module.service.demo.IHelloService;

@Service
public class HelloServiceImpl implements IHelloService {

	private final static Logger logger = LogManager.getLogger(HelloServiceImpl.class);

	@Autowired
	private IHelloDao iHelloDao;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public HelloVo get(HelloBo helloBo) throws FrameworkRuntimeException {
		try {
			// todo 请求数据参数组装
			HelloDto helloDto = new HelloDto();
			helloDto.setName(helloBo.getName());
			return iHelloDao.get(helloDto);
		} catch (FrameworkRuntimeException e) {
			logger.error(LogMsg.to(e));
			throw e;
		} catch (DataAccessException e) {
			logger.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		} catch (Throwable e) {
			logger.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.BASE_ERROR, e.getMessage(), e);
		}
	}

}
