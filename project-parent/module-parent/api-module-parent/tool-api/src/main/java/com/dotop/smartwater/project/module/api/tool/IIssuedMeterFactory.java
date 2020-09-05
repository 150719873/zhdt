package com.dotop.smartwater.project.module.api.tool;

import com.dotop.smartwater.dependence.core.common.BaseFactory;
import com.dotop.smartwater.project.module.core.water.form.IssuedMeterForm;
import com.dotop.smartwater.project.module.core.water.vo.IssuedMeterVo;

public interface IIssuedMeterFactory extends BaseFactory<IssuedMeterForm, IssuedMeterVo> {

	/**
	 * 生成抄表任务
	 * 
	 * @param form
	 * @return
	 * @
	 */
	boolean generate(IssuedMeterForm form) ;

}
