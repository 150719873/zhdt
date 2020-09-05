package com.dotop.smartwater.project.module.service.workcenter;

import com.dotop.smartwater.dependence.core.common.BaseService;
import com.dotop.smartwater.project.module.core.water.bo.WorkCenterTmplNodePointBo;
import com.dotop.smartwater.project.module.core.water.vo.WorkCenterTmplNodePointVo;

import java.util.List;

/**

 * @date 2019/7/25.
 */
public interface ITmplNodePointService extends BaseService<WorkCenterTmplNodePointBo, WorkCenterTmplNodePointVo> {

	/**
	 * 添加节点
	 *
	 * @param workCenterTmplNodePointBo
	 * @return
	 */
	@Override
	WorkCenterTmplNodePointVo add(WorkCenterTmplNodePointBo workCenterTmplNodePointBo);

	/**
	 * 查询列表
	 * @param workCenterTmplNodePointBo
	 * @return
	 */
	@Override
	List<WorkCenterTmplNodePointVo> list(WorkCenterTmplNodePointBo workCenterTmplNodePointBo);

	/**
	 * 删除
	 *
	 * @param workCenterTmplNodePointBo
	 * @return
	 */
	@Override
	String del(WorkCenterTmplNodePointBo workCenterTmplNodePointBo);


	/**
	 * 批量添加
	 * @param workCenterTmplNodePointBos
	 */
	@Override
	void adds(List<WorkCenterTmplNodePointBo> workCenterTmplNodePointBos);

	/**
	 * 批量编辑
	 * @param workCenterTmplNodePointBos
	 */
	@Override
	void edits(List<WorkCenterTmplNodePointBo> workCenterTmplNodePointBos);

	/**
	 * 批量删除
	 * @param workCenterTmplNodePointBos
	 */
	@Override
	void dels(List<WorkCenterTmplNodePointBo> workCenterTmplNodePointBos);
}
