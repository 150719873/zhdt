package com.dotop.smartwater.project.module.api.operation;

import com.dotop.smartwater.dependence.core.common.BaseFactory;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.water.form.OperationLogForm;
import com.dotop.smartwater.project.module.core.water.vo.OperationLogVo;

/**
 * 运维日志api

 * @date 2019/3/4.
 */
public interface IOperationLogFactory extends BaseFactory<OperationLogForm, OperationLogVo> {

	/**
	 * 获取运维日志分页
	 * @param operationLogForm 查询参数
	 * @return 分页
	 */
	@Override
	Pagination<OperationLogVo> page(OperationLogForm operationLogForm);

	/**
	 * 查找
	 * @param operationLogForm 参数
	 * @return 运维日志对象
	 */
	@Override
	OperationLogVo get(OperationLogForm operationLogForm);

	/**
	 * 保存运维日志
	 * @param operationLogForm 参数
	 */
	@Override
	OperationLogVo edit(OperationLogForm operationLogForm);
}
