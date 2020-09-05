package com.dotop.smartwater.project.module.service.workcenter;

import java.util.List;

import com.dotop.smartwater.dependence.core.common.BaseService;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.water.bo.WorkCenterProcessMsgBo;
import com.dotop.smartwater.project.module.core.water.vo.WorkCenterProcessMsgVo;

/**
 * 工作中心流程消息管理
 * 

 * @date 2019年4月17日
 * @description
 */
public interface IProcessMsgService extends BaseService<WorkCenterProcessMsgBo, WorkCenterProcessMsgVo> {

	/**
	 * 新增
	 */
	@Override
	WorkCenterProcessMsgVo add(WorkCenterProcessMsgBo processMsgBo);

	/**
	 * 查询分页
	 */
	@Override
	Pagination<WorkCenterProcessMsgVo> page(WorkCenterProcessMsgBo processMsgBo);

	/**
	 * 根据当前人查看处理过的所有消息，返回对应的流程id，全表搜索，性能要优化
	 */
	List<String> listProcessId(WorkCenterProcessMsgBo processMsgBo);

}
