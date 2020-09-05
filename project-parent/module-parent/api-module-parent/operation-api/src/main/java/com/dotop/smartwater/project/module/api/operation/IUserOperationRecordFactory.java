package com.dotop.smartwater.project.module.api.operation;

import com.dotop.smartwater.dependence.core.common.BaseFactory;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.water.form.UserOperationRecordForm;
import com.dotop.smartwater.project.module.core.water.vo.UserOperationRecordVo;

/**
 * 用户操作记录
 *

 * @date 2019/2/25.
 */
public interface IUserOperationRecordFactory extends BaseFactory<UserOperationRecordForm, UserOperationRecordVo> {
	/**
	 * 添加用户操作记录
	 *
	 * @param userOperationRecordForm 对象
	 * @return
	 */
	@Override
	UserOperationRecordVo add(UserOperationRecordForm userOperationRecordForm);

	/**
	 * 分页查询用户操作记录
	 *
	 * @param userOperationRecordForm
	 * @return
	 */
	@Override
	Pagination<UserOperationRecordVo> page(UserOperationRecordForm userOperationRecordForm);
}
