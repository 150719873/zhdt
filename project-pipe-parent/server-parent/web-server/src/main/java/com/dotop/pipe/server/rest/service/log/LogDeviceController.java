package com.dotop.pipe.server.rest.service.log;

import com.dotop.pipe.core.form.LogDeviceForm;
import com.dotop.pipe.web.api.factory.log.ILogDeviceFactory;
import com.dotop.smartwater.dependence.core.common.BaseController;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.global.GlobalContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 
 *
 * @date 2019年7月26日
 */
@RestController
@RequestMapping("/logDevice")
public class LogDeviceController implements BaseController<LogDeviceForm> {

	private final static Logger logger = LogManager.getLogger(LogDeviceController.class);

	@Autowired
	private ILogDeviceFactory iLogDeviceFactory;

	@Override
	@PostMapping(value = "/list", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
	public String list(@RequestBody LogDeviceForm logDeviceForm) throws FrameworkRuntimeException {
		return resp(iLogDeviceFactory.list(logDeviceForm));
	}
}
