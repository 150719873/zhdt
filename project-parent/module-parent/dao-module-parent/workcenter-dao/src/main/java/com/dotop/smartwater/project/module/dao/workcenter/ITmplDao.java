package com.dotop.smartwater.project.module.dao.workcenter;

import com.dotop.smartwater.dependence.core.common.BaseDao;
import com.dotop.smartwater.project.module.core.water.dto.WorkCenterTmplDto;
import com.dotop.smartwater.project.module.core.water.vo.WorkCenterTmplVo;

import java.util.List;

/**
 * 工作中心模板管理
 *

 * @date 2019年4月17日
 * @description
 */
public interface ITmplDao extends BaseDao<WorkCenterTmplDto, WorkCenterTmplVo> {
	/**
	 * 新增
	 */
	@Override
	void add(WorkCenterTmplDto tmplDto);

	/**
	 * 查询详情
	 */
	@Override
	WorkCenterTmplVo get(WorkCenterTmplDto tmplDto);

	/**
	 * 查询列表
	 */
	@Override
	List<WorkCenterTmplVo> list(WorkCenterTmplDto tmplDto);

	/**
	 * 查询列表数量
	 */
	@Override
	Integer listCount(WorkCenterTmplDto tmplDto);

	/**
	 * 修改
	 */
	@Override
	Integer edit(WorkCenterTmplDto tmplDto);

	/**
	 * 删除
	 */
	@Override
	Integer del(WorkCenterTmplDto tmplDto);

}
