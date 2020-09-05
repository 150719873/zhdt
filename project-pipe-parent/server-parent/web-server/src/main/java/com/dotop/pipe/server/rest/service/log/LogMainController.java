package com.dotop.pipe.server.rest.service.log;

import com.dotop.pipe.core.form.LogMainForm;
import com.dotop.pipe.web.api.factory.log.ILogMainFactory;
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
@RequestMapping("/logMain")
public class LogMainController implements BaseController<LogMainForm> {

	private final static Logger logger = LogManager.getLogger(LogMainController.class);

	@Autowired
	private ILogMainFactory iLogMainFactory;

	@Override
	@PostMapping(value = "/page", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
	public String page(@RequestBody LogMainForm logMainForm) throws FrameworkRuntimeException {
		return resp(iLogMainFactory.page(logMainForm));
	}

	@Override
	@PostMapping(value = "/add", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
	public String add(@RequestBody LogMainForm logMainForm) throws FrameworkRuntimeException {
		return resp(iLogMainFactory.add(logMainForm));
	}

	@Override
	@PostMapping(value = "/get", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
	public String get(@RequestBody LogMainForm logMainForm) throws FrameworkRuntimeException {
		return resp(iLogMainFactory.get(logMainForm));
	}

	@Override
	@PostMapping(value = "/edit", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
	public String edit(@RequestBody LogMainForm logMainForm) throws FrameworkRuntimeException {
		return resp(iLogMainFactory.edit(logMainForm));
	}

	@Override
	@PostMapping(value = "/del", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
	public String del(@RequestBody LogMainForm logMainForm) throws FrameworkRuntimeException {
		return resp(iLogMainFactory.del(logMainForm));
	}
}
