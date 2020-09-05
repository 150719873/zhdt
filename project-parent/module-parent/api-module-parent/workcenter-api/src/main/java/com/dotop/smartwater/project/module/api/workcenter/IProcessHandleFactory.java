package com.dotop.smartwater.project.module.api.workcenter;

import com.dotop.smartwater.dependence.core.common.BaseFactory;
import com.dotop.smartwater.project.module.core.water.form.WorkCenterProcessHandleForm;
import com.dotop.smartwater.project.module.core.water.vo.WorkCenterProcessHandleVo;

/**
 * 工作中心流程处理管理
 * 

 * @date 2019年4月17日
 * @description
 */
public interface IProcessHandleFactory extends BaseFactory<WorkCenterProcessHandleForm, WorkCenterProcessHandleVo> {
	/**
	 * 接收模板参数调整显示新增页面
	 */
	WorkCenterProcessHandleVo show(WorkCenterProcessHandleForm processHandleForm) ;

	/**
	 * 流程模板表单信息展示sql数据源源
	 */
	WorkCenterProcessHandleVo listTmplDbAuto(WorkCenterProcessHandleForm processHandleForm)
			;

	/**
	 * 新增流程
	 */
	@Override
	WorkCenterProcessHandleVo add(WorkCenterProcessHandleForm processHandleForm) ;

	/**
	 * 流程模板表单信息展示
	 */
	@Override
	WorkCenterProcessHandleVo get(WorkCenterProcessHandleForm processHandleForm) ;

	/**
	 * 流程模板表单信息展示sql数据源源
	 */
	WorkCenterProcessHandleVo listProcessDbAuto(WorkCenterProcessHandleForm processHandleForm)
			;

	/**
	 * 查看当前进程处理节点信息展示流程信息处理
	 */
	WorkCenterProcessHandleVo getCurrNode(WorkCenterProcessHandleForm processHandleForm)
			;

}
