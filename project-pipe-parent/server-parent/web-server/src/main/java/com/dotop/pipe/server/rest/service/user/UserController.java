package com.dotop.pipe.server.rest.service.user;

import com.dotop.pipe.core.form.MarkForm;
import com.dotop.pipe.web.api.factory.user.IUserFactory;
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
@RequestMapping("/user")
public class UserController implements BaseController<MarkForm> {

	private final static Logger logger = LogManager.getLogger(UserController.class);

	@Autowired
	private IUserFactory iUserFactory;

	@Override
	@PostMapping(value = "/list", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
	public String list(@RequestBody MarkForm markForm) throws FrameworkRuntimeException {
		return resp(iUserFactory.getUserList(markForm.getEnterpriseId()));
	}
}
