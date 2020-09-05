package com.dotop.pipe.api.service.test;

import com.dotop.pipe.core.vo.test.TestVo;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;

public interface ITestService {

	public TestVo get(String id) throws FrameworkRuntimeException;

}
