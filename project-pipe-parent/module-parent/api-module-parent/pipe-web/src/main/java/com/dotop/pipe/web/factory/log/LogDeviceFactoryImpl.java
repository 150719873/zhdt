package com.dotop.pipe.web.factory.log;

import com.dotop.pipe.api.service.log.ILogDeviceService;
import com.dotop.pipe.core.bo.log.LogDeviceBo;
import com.dotop.pipe.core.form.LogDeviceForm;
import com.dotop.pipe.core.vo.log.LogDeviceVo;
import com.dotop.pipe.web.api.factory.log.ILogDeviceFactory;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.utils.BeanUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 
 */
@Component
public class LogDeviceFactoryImpl implements ILogDeviceFactory {

	private final static Logger logger = LogManager.getLogger(LogDeviceFactoryImpl.class);

	@Autowired
	ILogDeviceService iLogDeviceService;

	@Override
	public List<LogDeviceVo> list(LogDeviceForm logDeviceForm) throws FrameworkRuntimeException {
		LogDeviceBo logDeviceBo = BeanUtils.copy(logDeviceForm, LogDeviceBo.class);
		return iLogDeviceService.list(logDeviceBo);
	}
}
