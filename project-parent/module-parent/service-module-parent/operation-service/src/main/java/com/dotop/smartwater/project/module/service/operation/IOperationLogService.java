package com.dotop.smartwater.project.module.service.operation;

import com.dotop.smartwater.dependence.core.common.BaseService;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.water.bo.OperationLogBo;
import com.dotop.smartwater.project.module.core.water.vo.OperationLogVo;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 运维日志
 *

 * @date 2019/3/4.
 */
public interface IOperationLogService extends BaseService<OperationLogBo, OperationLogVo> {
	/**
	 * 获取运维日志分页
	 *
	 * @param operationLogBo 查询参数
	 * @return 分页
	 */
	@Override
	Pagination<OperationLogVo> page(OperationLogBo operationLogBo);

	/**
	 * 查找
	 *
	 * @param operationLogBo 参数
	 * @return 运维日志对象
	 */
	@Override
	OperationLogVo get(OperationLogBo operationLogBo);

	/**
	 * 保存运维日志
	 *
	 * @param operationLogBo 参数
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	OperationLogVo edit(OperationLogBo operationLogBo);
}
