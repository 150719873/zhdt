package com.dotop.smartwater.project.module.api.demo;

import com.dotop.smartwater.dependence.core.common.BaseFactory;
import com.dotop.smartwater.project.module.core.demo.form.HelloForm;
import com.dotop.smartwater.project.module.core.demo.vo.HelloVo;

/**
 * 例子

 */
public interface IHelloFactory extends BaseFactory<HelloForm, HelloVo> {

	/**
	 * @param helloForm 对象
	 * @return 对象
	 */
	@Override
	HelloVo get(HelloForm helloForm) ;

	/**
	 * 例子
	 */
	void ggg() ;

}
