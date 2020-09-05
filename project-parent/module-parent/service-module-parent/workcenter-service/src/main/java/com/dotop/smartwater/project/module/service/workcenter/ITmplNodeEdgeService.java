package com.dotop.smartwater.project.module.service.workcenter;

import com.dotop.smartwater.dependence.core.common.BaseService;
import com.dotop.smartwater.project.module.core.water.bo.WorkCenterTmplNodeEdgeBo;
import com.dotop.smartwater.project.module.core.water.vo.WorkCenterTmplNodeEdgeVo;

import java.util.List;

/**

 * @date 2019/7/25.
 */
public interface ITmplNodeEdgeService extends BaseService<WorkCenterTmplNodeEdgeBo, WorkCenterTmplNodeEdgeVo> {

	/**
	 * 添加边
	 *
	 * @param workCenterTmplNodeEdgeBo
	 * @return
	 */
	@Override
	WorkCenterTmplNodeEdgeVo add(WorkCenterTmplNodeEdgeBo workCenterTmplNodeEdgeBo);

	/**
	 * 删除
	 *
	 * @param workCenterTmplNodeEdgeBo
	 * @return
	 */
	@Override
	String del(WorkCenterTmplNodeEdgeBo workCenterTmplNodeEdgeBo);

	/**
	 * 查询列表
	 * @param workCenterTmplNodeEdgeBo
	 * @return
	 */
	@Override
	List<WorkCenterTmplNodeEdgeVo> list(WorkCenterTmplNodeEdgeBo workCenterTmplNodeEdgeBo);

	/**
	 * 批量添加
	 * @param workCenterTmplNodeEdgeBos
	 */
	@Override
	void adds(List<WorkCenterTmplNodeEdgeBo> workCenterTmplNodeEdgeBos);
}
