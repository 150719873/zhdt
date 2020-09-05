package com.dotop.pipe.server.rest.service.mark;

import com.dotop.pipe.core.form.MarkForm;
import com.dotop.pipe.web.api.factory.mark.IMarkFactory;
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
@RequestMapping("/mark")
public class MarkController implements BaseController<MarkForm> {

	private final static Logger logger = LogManager.getLogger(MarkController.class);

	@Autowired
	private IMarkFactory iMarkFactory;

	@Override
	@PostMapping(value = "/add", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
	public String add(@RequestBody MarkForm markForm) throws FrameworkRuntimeException {
		return resp(iMarkFactory.add(markForm));
	}

	@PostMapping(value = "/share", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
	public String share(@RequestBody MarkForm markForm) throws FrameworkRuntimeException {
		return resp(iMarkFactory.share(markForm));
	}

	@Override
	@PostMapping(value = "/get", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
	public String get(@RequestBody MarkForm markForm) throws FrameworkRuntimeException {
		return resp(iMarkFactory.get(markForm));
	}

	@Override
	@PostMapping(value = "/page", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
	public String page(@RequestBody MarkForm markForm) throws FrameworkRuntimeException {
		return resp(iMarkFactory.page(markForm));
	}

	@Override
	@PostMapping(value = "/list", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
	public String list(@RequestBody MarkForm markForm) throws FrameworkRuntimeException {
		return resp(iMarkFactory.list(markForm));
	}

	@Override
	@PostMapping(value = "/edit", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
	public String edit(@RequestBody MarkForm markForm) throws FrameworkRuntimeException {
		return resp(iMarkFactory.edit(markForm));
	}

	@Override
	@PostMapping(value = "/del", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
	public String del(@RequestBody MarkForm markForm) throws FrameworkRuntimeException {
		return resp(iMarkFactory.del(markForm));
	}
}
