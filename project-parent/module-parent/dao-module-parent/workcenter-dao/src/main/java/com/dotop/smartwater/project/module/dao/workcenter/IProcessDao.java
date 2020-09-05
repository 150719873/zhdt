package com.dotop.smartwater.project.module.dao.workcenter;

import com.dotop.smartwater.dependence.core.common.BaseDao;
import com.dotop.smartwater.project.module.core.water.dto.WorkCenterProcessDto;
import com.dotop.smartwater.project.module.core.water.vo.WorkCenterProcessVo;

import java.util.List;

/**
 * 工作中心流程管理
 *

 * @date 2019年4月17日
 * @description
 */
public interface IProcessDao extends BaseDao<WorkCenterProcessDto, WorkCenterProcessVo> {
	/**
	 * 新增
	 */
	@Override
	void add(WorkCenterProcessDto processDto);

	/**
	 * 查询详情
	 */
	@Override
	WorkCenterProcessVo get(WorkCenterProcessDto processDto);

	/**
	 * 查询列表
	 */
	@Override
	List<WorkCenterProcessVo> list(WorkCenterProcessDto processDto);

	/**
	 * 修改基本信息
	 */
	@Override
	Integer edit(WorkCenterProcessDto processDto);

	/**
	 * 修改将要处理下个节点信息
	 */
	Integer editNext(WorkCenterProcessDto processDto);

	/**
	 * 删除
	 */
	@Override
	Integer del(WorkCenterProcessDto processDto);

}
