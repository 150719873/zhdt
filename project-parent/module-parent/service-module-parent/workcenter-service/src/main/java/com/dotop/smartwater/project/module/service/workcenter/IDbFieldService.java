package com.dotop.smartwater.project.module.service.workcenter;

import java.util.List;

import com.dotop.smartwater.dependence.core.common.BaseService;
import com.dotop.smartwater.project.module.core.water.bo.WorkCenterDbFieldBo;
import com.dotop.smartwater.project.module.core.water.vo.WorkCenterDbFieldVo;

/**
 * 工作中心数据源字段管理
 * 

 * @date 2019年4月17日
 * @description
 */
public interface IDbFieldService extends BaseService<WorkCenterDbFieldBo, WorkCenterDbFieldVo> {

	/**
	 * 新增
	 */
	@Override
	WorkCenterDbFieldVo add(WorkCenterDbFieldBo dbFieldBo);

	/**
	 * 批量新增
	 */
	@Override
	void adds(List<WorkCenterDbFieldBo> dbFieldBos);

	/**
	 * 批量修改
	 */
	@Override
	void edits(List<WorkCenterDbFieldBo> dbFieldBos);

	/**
	 * 批量删除
	 */
	@Override
	void dels(List<WorkCenterDbFieldBo> dbFieldBos);

	/**
	 * 查询列表
	 */
	@Override
	List<WorkCenterDbFieldVo> list(WorkCenterDbFieldBo dbFieldBo);

	/**
	 * 删除
	 */
	@Override
	String del(WorkCenterDbFieldBo dbFieldBo);
}
