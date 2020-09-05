package com.dotop.smartwater.project.module.api.demo.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.project.module.api.demo.IHelloFactory;
import com.dotop.smartwater.project.module.core.demo.bo.HelloBo;
import com.dotop.smartwater.project.module.core.demo.form.HelloForm;
import com.dotop.smartwater.project.module.core.demo.vo.HelloVo;
import com.dotop.smartwater.project.module.service.demo.IHelloService;

@Component
public class HelloFactoryImpl implements IHelloFactory {

	// @Autowired
	// private IThirdFeginClient iThirdFeginClient;

	@Autowired
	private IHelloService iHelloService;

	@Override
	public HelloVo get(HelloForm helloForm) throws FrameworkRuntimeException {
		Map<String, String> params = new HashMap<>(16);
		params.put("key", "中文");
		// iThirdFeginClient.aaa(params, "userId0", "token0");

		// todo 检验权限
		// todo 业务逻辑
		HelloBo helloBo = new HelloBo();
		helloBo.setName(helloForm.getName());
		return iHelloService.get(helloBo);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public void ggg() throws FrameworkRuntimeException {
		throw new FrameworkRuntimeException("eee", "ggg");
	}

}
