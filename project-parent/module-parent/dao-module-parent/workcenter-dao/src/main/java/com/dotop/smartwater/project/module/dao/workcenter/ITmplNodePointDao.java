package com.dotop.smartwater.project.module.dao.workcenter;

import com.dotop.smartwater.dependence.core.common.BaseDao;
import com.dotop.smartwater.project.module.core.water.dto.WorkCenterTmplNodePointDto;
import com.dotop.smartwater.project.module.core.water.vo.WorkCenterTmplNodePointVo;

import java.util.List;

public interface ITmplNodePointDao extends BaseDao<WorkCenterTmplNodePointDto, WorkCenterTmplNodePointVo> {

	/**
	 * 新增
	 *
	 * @param workCenterTmplNodePointDto
	 */
	@Override
	void add(WorkCenterTmplNodePointDto workCenterTmplNodePointDto);

	/**
	 * 查找列表
	 *
	 * @param workCenterTmplNodePointDto
	 * @return
	 */
	@Override
	List<WorkCenterTmplNodePointVo> list(WorkCenterTmplNodePointDto workCenterTmplNodePointDto);

	/**
	 * 删除
	 *
	 * @param workCenterTmplNodePointDto
	 * @return
	 */
	@Override
	Integer del(WorkCenterTmplNodePointDto workCenterTmplNodePointDto);


	/**
	 * 批量新增
	 *
	 * @param workCenterTmplNodePointDtos
	 */
	@Override
	void adds(List<WorkCenterTmplNodePointDto> workCenterTmplNodePointDtos);

	/**
	 * 批量修改
	 *
	 * @param workCenterTmplNodePointDtos
	 */
	@Override
	void edits(List<WorkCenterTmplNodePointDto> workCenterTmplNodePointDtos);

	/**
	 * 批量删除
	 *
	 * @param workCenterTmplNodePointDtos
	 */
	@Override
	void dels(List<WorkCenterTmplNodePointDto> workCenterTmplNodePointDtos);
}
