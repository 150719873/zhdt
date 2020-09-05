package com.dotop.smartwater.project.module.api.workcenter;

import com.dotop.smartwater.dependence.core.common.BaseFactory;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.water.form.WorkCenterProcessForm;
import com.dotop.smartwater.project.module.core.water.vo.WorkCenterProcessVo;

/**
 * 工作中心流程管理
 * 

 * @date 2019年4月17日
 * @description
 */
public interface IProcessFactory extends BaseFactory<WorkCenterProcessForm, WorkCenterProcessVo> {

	/**
	 * 查询分页
	 */
	@Override
	Pagination<WorkCenterProcessVo> page(WorkCenterProcessForm processForm) ;

	/**
	 * 查看代办分页流程
	 */
	Pagination<WorkCenterProcessVo> pageAgent(WorkCenterProcessForm processForm) ;

	/**
	 * 根据业务模块类型过滤查看
	 */
	Pagination<WorkCenterProcessVo> pageBusiness(WorkCenterProcessForm processForm) ;

	/**
	 * 根据业务模块id查看
	 */
	WorkCenterProcessVo getBusiness(WorkCenterProcessForm processForm) ;

}
