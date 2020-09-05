//package com.dotop.smartwater.view.server.rest.service.test;
//
//import com.dotop.pipe.core.form.TestForm;
//import com.dotop.smartwater.dependence.core.common.BaseController;
//import com.dotop.smartwater.dependence.core.global.GlobalContext;
//import com.dotop.smartwater.dependence.core.log.LogMsg;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController()
//@RequestMapping("/test")
//public class TestController implements BaseController<TestForm> {
//
//	private final static Logger logger = LogManager.getLogger(TestController.class);
//
//	public TestController() {
//		super();
//	}
//
//	@GetMapping(value = "/{id}", produces = GlobalContext.PRODUCES)
//	@Override
//	public String get(TestForm testForm) {
//		logger.info(LogMsg.to("testForm", testForm));
//		return resp();
//	}
//
//}
