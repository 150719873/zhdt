package com.dotop.smartwater.project.module.service.workcenter;

import java.util.List;

import com.dotop.smartwater.dependence.core.common.BaseService;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.water.bo.WorkCenterTmplBo;
import com.dotop.smartwater.project.module.core.water.vo.WorkCenterTmplVo;

/**
 * 工作中心模板管理
 * 

 * @date 2019年4月17日
 * @description
 */
public interface ITmplService extends BaseService<WorkCenterTmplBo, WorkCenterTmplVo> {
	/**
	 * 新增
	 */
	@Override
	WorkCenterTmplVo add(WorkCenterTmplBo tmplBo);

	/**
	 * 查询详情
	 */
	@Override
	WorkCenterTmplVo get(WorkCenterTmplBo tmplBo);

	/**
	 * 查询分页
	 */
	@Override
	Pagination<WorkCenterTmplVo> page(WorkCenterTmplBo tmplBo);

	/**
	 * 修改
	 */
	@Override
	WorkCenterTmplVo edit(WorkCenterTmplBo tmplBo);

	/**
	 * 删除
	 */
	@Override
	String del(WorkCenterTmplBo tmplBo);

	/**
	 * 查询列表
	 */
	@Override
	List<WorkCenterTmplVo> list(WorkCenterTmplBo tmplBo);
}
