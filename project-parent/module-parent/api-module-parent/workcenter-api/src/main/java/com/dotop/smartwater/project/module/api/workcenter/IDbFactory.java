package com.dotop.smartwater.project.module.api.workcenter;

import com.dotop.smartwater.dependence.core.common.BaseFactory;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.water.form.WorkCenterDbForm;
import com.dotop.smartwater.project.module.core.water.vo.WorkCenterDbVo;

/**
 * 工作中心数据源管理
 * 

 * @date 2019年4月17日
 * @description
 */
public interface IDbFactory extends BaseFactory<WorkCenterDbForm, WorkCenterDbVo> {

	/**
	 * 新增
	 */
	@Override
	WorkCenterDbVo add(WorkCenterDbForm dbForm) ;

	/**
	 * 查询详情
	 */
	@Override
	WorkCenterDbVo get(WorkCenterDbForm dbForm) ;

	/**
	 * 查询分页
	 */
	@Override
	Pagination<WorkCenterDbVo> page(WorkCenterDbForm dbForm) ;

	/**
	 * 修改
	 */
	@Override
	WorkCenterDbVo edit(WorkCenterDbForm dbForm) ;

	/**
	 * 删除
	 */
	@Override
	String del(WorkCenterDbForm dbForm) ;

	/**
	 * 设置自动加载数据源
	 */
	void load(WorkCenterDbForm dbForm) ;

	/**
	 * 数据源复制
	 */
	WorkCenterDbVo copy(WorkCenterDbForm dbForm) ;

}
