package com.dotop.pipe.service.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dotop.pipe.api.dao.test.ITestDao;
import com.dotop.pipe.api.service.test.ITestService;
import com.dotop.pipe.core.vo.test.TestVo;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;

@Service
public class TestServiceImpl implements ITestService {

	@Autowired
	private ITestDao iTestDao;

	@Override
	public TestVo get(String id) throws FrameworkRuntimeException {

		TestVo testVo = iTestDao.get(id);

		return testVo;
	}

}
