package com.dotop.smartwater.project.module.service.workcenter;

import java.util.List;

import com.dotop.smartwater.dependence.core.common.BaseService;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.water.bo.WorkCenterProcessBo;
import com.dotop.smartwater.project.module.core.water.vo.WorkCenterProcessVo;

/**
 * 工作中心流程管理
 * 

 * @date 2019年4月17日
 * @description
 */
public interface IProcessService extends BaseService<WorkCenterProcessBo, WorkCenterProcessVo> {
	/**
	 * 新增
	 */
	@Override
	WorkCenterProcessVo add(WorkCenterProcessBo processBo);

	/**
	 * 查询详情
	 */
	@Override
	WorkCenterProcessVo get(WorkCenterProcessBo processBo);

	/**
	 * 查询分页
	 */
	@Override
	Pagination<WorkCenterProcessVo> page(WorkCenterProcessBo processBo);

	/**
	 * 查询列表
	 */
	@Override
	List<WorkCenterProcessVo> list(WorkCenterProcessBo processBo);

	/**
	 * 修改基本信息
	 */
	@Override
	WorkCenterProcessVo edit(WorkCenterProcessBo processBo);

	/**
	 * 修改将要处理下个节点信息
	 */
	WorkCenterProcessVo editNext(WorkCenterProcessBo processBo);

	/**
	 * 删除
	 */
	@Override
	String del(WorkCenterProcessBo processBo);
}
