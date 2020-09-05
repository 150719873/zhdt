package com.dotop.smartwater.project.module.service.workcenter;

import com.dotop.smartwater.dependence.core.common.BaseService;
import com.dotop.smartwater.project.module.core.water.bo.WorkCenterProcessNodeBo;
import com.dotop.smartwater.project.module.core.water.vo.WorkCenterProcessNodeVo;

/**
 * 工作中心流程节点管理
 * 

 * @date 2019年4月17日
 * @description
 */
public interface IProcessNodeService extends BaseService<WorkCenterProcessNodeBo, WorkCenterProcessNodeVo> {
	/**
	 * 新增
	 */
	@Override
	WorkCenterProcessNodeVo add(WorkCenterProcessNodeBo processNodeBo);

	/**
	 * 查询详情
	 */
	@Override
	WorkCenterProcessNodeVo get(WorkCenterProcessNodeBo processNodeBo);

}
