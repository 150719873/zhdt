package com.dotop.smartwater.project.module.api.app;

import com.dotop.smartwater.dependence.core.common.BaseFactory;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.water.form.WorkCenterProcessForm;
import com.dotop.smartwater.project.module.core.water.vo.WorkCenterProcessVo;

/**
 * 工作中心

 */
public interface IAppWorkCenterProcessFactory extends BaseFactory<WorkCenterProcessForm, WorkCenterProcessVo> {

	/**
	 * 分页
	 * @param processForm
	 * @return
	 */
	@Override
	Pagination<WorkCenterProcessVo> page(WorkCenterProcessForm processForm);

}
