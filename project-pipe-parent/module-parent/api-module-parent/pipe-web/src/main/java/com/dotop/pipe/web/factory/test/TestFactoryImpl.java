package com.dotop.pipe.web.factory.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dotop.pipe.api.service.test.ITestService;
import com.dotop.pipe.core.vo.test.TestVo;
import com.dotop.pipe.web.api.factory.test.ITestFactory;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;

@Component
public class TestFactoryImpl implements ITestFactory {

	@Autowired
	private ITestService iTestService;

	@Override
	public TestVo get(String id) throws FrameworkRuntimeException {

		TestVo testVo = iTestService.get(id);

		return testVo;
	}
}
