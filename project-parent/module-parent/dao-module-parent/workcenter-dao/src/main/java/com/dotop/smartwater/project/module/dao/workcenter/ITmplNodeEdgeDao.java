package com.dotop.smartwater.project.module.dao.workcenter;

import com.dotop.smartwater.dependence.core.common.BaseDao;
import com.dotop.smartwater.project.module.core.water.dto.WorkCenterTmplNodeEdgeDto;
import com.dotop.smartwater.project.module.core.water.vo.WorkCenterTmplNodeEdgeVo;

import java.util.List;

public interface ITmplNodeEdgeDao extends BaseDao<WorkCenterTmplNodeEdgeDto, WorkCenterTmplNodeEdgeVo> {

	/**
	 * 新增
	 *
	 * @param workCenterTmplNodeEdgeDto
	 */
	@Override
	void add(WorkCenterTmplNodeEdgeDto workCenterTmplNodeEdgeDto);

	/**
	 * 查询列表
	 *
	 * @param workCenterTmplNodeEdgeDto
	 * @return
	 */
	@Override
	List<WorkCenterTmplNodeEdgeVo> list(WorkCenterTmplNodeEdgeDto workCenterTmplNodeEdgeDto);

	/**
	 * 删除
	 *
	 * @param workCenterTmplNodeEdgeDto
	 * @return
	 */
	@Override
	Integer del(WorkCenterTmplNodeEdgeDto workCenterTmplNodeEdgeDto);

	/**
	 * 批量添加
	 * @param workCenterTmplNodeEdgeDtos
	 */
	@Override
	void adds(List<WorkCenterTmplNodeEdgeDto> workCenterTmplNodeEdgeDtos);
}
