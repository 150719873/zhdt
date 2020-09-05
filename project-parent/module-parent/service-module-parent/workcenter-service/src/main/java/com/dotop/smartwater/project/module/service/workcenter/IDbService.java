package com.dotop.smartwater.project.module.service.workcenter;

import java.util.List;

import com.dotop.smartwater.dependence.core.common.BaseService;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.water.bo.WorkCenterDbBo;
import com.dotop.smartwater.project.module.core.water.vo.WorkCenterDbVo;

/**
 * 工作中心数据源管理
 * 

 * @date 2019年4月17日
 * @description
 */
public interface IDbService extends BaseService<WorkCenterDbBo, WorkCenterDbVo> {

	/**
	 * 新增
	 */
	@Override
	WorkCenterDbVo add(WorkCenterDbBo dbBo);

	/**
	 * 查询详情
	 */
	@Override
	WorkCenterDbVo get(WorkCenterDbBo dbBo);

	/**
	 * 查询列表
	 */
	@Override
	List<WorkCenterDbVo> list(WorkCenterDbBo dbBo);

	/**
	 * 查询分页
	 */
	@Override
	Pagination<WorkCenterDbVo> page(WorkCenterDbBo dbBo);

	/**
	 * 修改
	 */
	@Override
	WorkCenterDbVo edit(WorkCenterDbBo dbBo);

	/**
	 * 删除
	 */
	@Override
	String del(WorkCenterDbBo dbBo);

	/**
	 * 批量新增
	 */
	@Override
	void adds(List<WorkCenterDbBo> dbBos);

	/**
	 * 批量删除
	 */
	@Override
	void dels(List<WorkCenterDbBo> dbBos);
}
