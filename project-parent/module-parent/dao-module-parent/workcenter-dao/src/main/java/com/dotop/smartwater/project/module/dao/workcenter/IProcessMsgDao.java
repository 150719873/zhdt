package com.dotop.smartwater.project.module.dao.workcenter;

import com.dotop.smartwater.dependence.core.common.BaseDao;
import com.dotop.smartwater.project.module.core.water.dto.WorkCenterProcessMsgDto;
import com.dotop.smartwater.project.module.core.water.vo.WorkCenterProcessMsgVo;

import java.util.List;

public interface IProcessMsgDao extends BaseDao<WorkCenterProcessMsgDto, WorkCenterProcessMsgVo> {
	/**
	 * 新增
	 */
	@Override
	void add(WorkCenterProcessMsgDto processMsgDto);

	/**
	 * 查询详情
	 */
	@Override
	WorkCenterProcessMsgVo get(WorkCenterProcessMsgDto processMsgDto);

	/**
	 * 查询列表
	 */
	@Override
	List<WorkCenterProcessMsgVo> list(WorkCenterProcessMsgDto processMsgDto);

	/**
	 * 修改
	 */
	@Override
	Integer edit(WorkCenterProcessMsgDto processMsgDto);

	/**
	 * 删除
	 */
	@Override
	Integer del(WorkCenterProcessMsgDto processMsgDto);

	/**
	 * 根据当前人查看处理过的所有消息，返回对应的流程id，全表搜索，性能要优化
	 */
	List<String> listProcessId(WorkCenterProcessMsgDto processMsgDto);

}
