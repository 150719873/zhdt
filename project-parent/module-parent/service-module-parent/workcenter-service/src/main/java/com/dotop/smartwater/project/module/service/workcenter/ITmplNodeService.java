package com.dotop.smartwater.project.module.service.workcenter;

import java.util.List;

import com.dotop.smartwater.dependence.core.common.BaseService;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.water.bo.WorkCenterTmplNodeBo;
import com.dotop.smartwater.project.module.core.water.vo.WorkCenterTmplNodeVo;
import com.dotop.smartwater.project.module.core.water.vo.customize.WorkCenterTmplNodeGraphVo;

/**
 * 工作中心模板节点管理
 *

 * @date 2019年4月17日
 * @description
 */
public interface ITmplNodeService extends BaseService<WorkCenterTmplNodeBo, WorkCenterTmplNodeVo> {
	/**
	 * 新增
	 */
	@Override
	WorkCenterTmplNodeVo add(WorkCenterTmplNodeBo tmplNodeBo);

	/**
	 * 流程图数据
	 *
	 * @param workCenterTmplNodeBo
	 * @return
	 */
	WorkCenterTmplNodeGraphVo graph(WorkCenterTmplNodeBo workCenterTmplNodeBo);

	/**
	 * 查询详情
	 */
	@Override
	WorkCenterTmplNodeVo get(WorkCenterTmplNodeBo tmplNodeBo);

	/**
	 * 查询分页
	 */
	@Override
	Pagination<WorkCenterTmplNodeVo> page(WorkCenterTmplNodeBo tmplNodeBo);

	/**
	 * 修改
	 */
	@Override
	WorkCenterTmplNodeVo edit(WorkCenterTmplNodeBo tmplNodeBo);

	/**
	 * 删除
	 */
	@Override
	String del(WorkCenterTmplNodeBo tmplNodeBo);

	/**
	 * 查询列表
	 */
	@Override
	List<WorkCenterTmplNodeVo> list(WorkCenterTmplNodeBo tmplNodeBo);

	/**
	 * 批量新增
	 */
	@Override
	void adds(List<WorkCenterTmplNodeBo> tmplNodeBos);

	/**
	 * 批量修改
	 */
	@Override
	void edits(List<WorkCenterTmplNodeBo> tmplNodeBos);

	/**
	 * 批量删除
	 */
	@Override
	void dels(List<WorkCenterTmplNodeBo> tmplNodeBos);

}
