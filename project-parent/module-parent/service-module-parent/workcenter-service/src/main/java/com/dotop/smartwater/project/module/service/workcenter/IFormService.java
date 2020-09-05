package com.dotop.smartwater.project.module.service.workcenter;

import com.dotop.smartwater.dependence.core.common.BaseService;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.water.bo.WorkCenterFormBo;
import com.dotop.smartwater.project.module.core.water.vo.WorkCenterFormVo;

/**
 * 工作中心表单管理
 * 

 * @date 2019年4月17日
 * @description
 */
public interface IFormService extends BaseService<WorkCenterFormBo, WorkCenterFormVo> {
	/**
	 * 新增
	 */
	@Override
	WorkCenterFormVo add(WorkCenterFormBo formBo) ;

	/**
	 * 查询详情
	 */
	@Override
	WorkCenterFormVo get(WorkCenterFormBo formBo) ;

	/**
	 * 查询分页
	 */
	@Override
	Pagination<WorkCenterFormVo> page(WorkCenterFormBo formBo) ;

	/**
	 * 修改
	 */
	@Override
	WorkCenterFormVo edit(WorkCenterFormBo formBo) ;

	/**
	 * 删除
	 */
	@Override
	String del(WorkCenterFormBo formBo) ;
}
