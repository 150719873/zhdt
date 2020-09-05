package com.dotop.pipe.server.rest.service.test;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dotop.pipe.core.form.TestForm;
import com.dotop.smartwater.dependence.core.common.BaseController;
import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.dependence.core.log.LogMsg;

@RestController()
@RequestMapping("/test")
public class TestController implements BaseController<TestForm> {

	private final static Logger logger = LogManager.getLogger(TestController.class);

	// @Autowired
	// private ValueOperations<String, String> cacheOpsForValue;

	public TestController() {
		super();
		System.out.println("TestController");
	}

	// @Autowired
	// private IBridgeFactory iBridgeFactory;

	// @Autowired
	// private ITestFactory iTestFactory;

	// private IDevicePropertyService iDevicePropertyService;

	@GetMapping(value = "/{id}", produces = GlobalContext.PRODUCES)
	@Override
	public String get(TestForm testForm) {
		logger.info(LogMsg.to("testForm", testForm));
		// TestVo t = new TestVo();
		// System.out.println(t.getId());
		// cacheOpsForValue.set("id", id, 3600L, TimeUnit.SECONDS);
		//
		// TestVo testVo = iTestFactory.get(id);
		// System.out.println(JSON.toJSONString(testVo));
		// return resp(testVo);
		// iDevicePropertyService.add(UuidUtils.getUuid(), "1", "1", "1", "1", "1", "1",
		// new Date(), new Date(),
		// new Date(), "1", "1", "1");
		return resp();
	}

	@PostMapping(produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
	@Override
	public String add(@RequestBody TestForm testForm) {
		logger.info(LogMsg.to("testForm", testForm));
		return resp(testForm);
	}

	@PutMapping(produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
	@Override
	public String edit(@RequestBody TestForm testForm) {
		logger.info(LogMsg.to("testForm", testForm));
		return resp(testForm);
	}

	@DeleteMapping(value = "/{id}", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
	public String del(TestForm testForm, @RequestBody TestForm testForm2) {
		logger.info(LogMsg.to("testForm", testForm));
		logger.info(LogMsg.to("testForm2", testForm2));
		return resp();
	}

	@GetMapping(value = "/client/client", produces = GlobalContext.PRODUCES)
	public String client() {
		Map<String, Object> params = new HashMap<>();
		params.put("A", "a");
		// String factory = SensorConstants.FACTORY_OTHER;
		// String factory = CommonConstants.DICTIONARY_FACTORYCODE_KBL;
		// iBridgeFactory.createSensor(factory, params);
		return resp("aaa", "111");
	}

	@PostMapping(value = "/timing", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
	public String timing(@RequestBody String list) {
		logger.info(LogMsg.to("list", list));
		return resp(list);
	}

	@GetMapping(value = "/timing2")
	public String timing2(TestForm list) {
		logger.info(LogMsg.to("list", list));
		return resp(list);
	}
}
