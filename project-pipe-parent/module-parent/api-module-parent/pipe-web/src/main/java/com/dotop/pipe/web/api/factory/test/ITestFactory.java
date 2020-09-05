package com.dotop.pipe.web.api.factory.test;

import com.dotop.pipe.core.vo.test.TestVo;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;

public interface ITestFactory {

	public TestVo get(String id) throws FrameworkRuntimeException;

}
