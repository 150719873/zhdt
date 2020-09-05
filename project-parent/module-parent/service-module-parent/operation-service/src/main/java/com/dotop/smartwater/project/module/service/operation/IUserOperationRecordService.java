package com.dotop.smartwater.project.module.service.operation;

import com.dotop.smartwater.dependence.core.pagination.Pagination;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dotop.smartwater.dependence.core.common.BaseService;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.project.module.core.water.bo.UserOperationRecordBo;
import com.dotop.smartwater.project.module.core.water.vo.UserOperationRecordVo;

/**
 * 用户操作记录
 *

 * @date 2019/2/25.
 */
public interface IUserOperationRecordService extends BaseService<UserOperationRecordBo, UserOperationRecordVo> {

	/**
	 * 添加用户操作记录
	 *
	 * @param userOperationRecordBo
	 * @return
	 * @throws FrameworkRuntimeException
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	UserOperationRecordVo add(UserOperationRecordBo userOperationRecordBo);

	/**
	 * 分页查询用户操作记录
	 *
	 * @param userOperationRecordBo
	 * @return
	 */
	@Override
	Pagination<UserOperationRecordVo> page(UserOperationRecordBo userOperationRecordBo);
}
