package com.dotop.smartwater.project.module.api.workcenter;

import com.dotop.smartwater.dependence.core.common.BaseFactory;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.water.form.WorkCenterTmplNodeForm;
import com.dotop.smartwater.project.module.core.water.vo.WorkCenterTmplNodeVo;
import com.dotop.smartwater.project.module.core.water.vo.customize.WorkCenterTmplNodeGraphVo;

/**
 * 工作中心模板节点管理
 * 

 * @date 2019年4月17日
 * @description
 */
public interface ITmplNodeFactory extends BaseFactory<WorkCenterTmplNodeForm, WorkCenterTmplNodeVo> {

	/**
	 * 新增
	 */
	@Override
	WorkCenterTmplNodeVo add(WorkCenterTmplNodeForm workCenterTmplNodeForm) ;

	/**
	 * 查询详情
	 */
	@Override
	WorkCenterTmplNodeVo get(WorkCenterTmplNodeForm workCenterTmplNodeForm) ;

	/**
	 * 查询分页
	 */
	@Override
	Pagination<WorkCenterTmplNodeVo> page(WorkCenterTmplNodeForm workCenterTmplNodeForm);

	/**
	 * 流程图数据
	 * @param workCenterTmplNodeForm
	 * @return
	 */
	WorkCenterTmplNodeGraphVo graph(WorkCenterTmplNodeForm workCenterTmplNodeForm);

	/**
	 * 修改
	 */
	@Override
	WorkCenterTmplNodeVo edit(WorkCenterTmplNodeForm workCenterTmplNodeForm) ;

	/**
	 * 删除
	 */
	@Override
	String del(WorkCenterTmplNodeForm workCenterTmplNodeForm) ;

}
