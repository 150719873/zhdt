package com.dotop.smartwater.project.module.service.demo;

import com.dotop.smartwater.dependence.core.common.BaseService;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.project.module.core.demo.bo.HelloBo;
import com.dotop.smartwater.project.module.core.demo.vo.HelloVo;

public interface IHelloService extends BaseService<HelloBo, HelloVo> {

	HelloVo get(HelloBo helloBo) throws FrameworkRuntimeException;

}
