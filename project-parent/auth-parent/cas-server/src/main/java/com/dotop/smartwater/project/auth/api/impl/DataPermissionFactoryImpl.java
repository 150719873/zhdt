package com.dotop.smartwater.project.auth.api.impl;

import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.lock.IDistributedLock;
import com.dotop.smartwater.project.auth.service.IDataPermissionService;
import com.dotop.smartwater.project.auth.api.IDataPermissionFactory;
import com.dotop.smartwater.project.module.core.auth.form.DataPermissionPerForm;
import com.dotop.smartwater.project.module.core.auth.form.DataTypeForm;
import com.dotop.smartwater.project.module.core.auth.vo.CheckboxOptionVo;
import com.dotop.smartwater.project.module.core.auth.vo.UserVo;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**

 * @date 2019/8/7.
 */
@Component
public class DataPermissionFactoryImpl implements IDataPermissionFactory {

	@Autowired
	private IDataPermissionService iDataPermissionService;

	@Autowired
	private IDistributedLock iDistributedLock;

	@Override
	public List<CheckboxOptionVo> loadPermissionById(DataTypeForm dataTypeForm) {
		return iDataPermissionService.loadPermissionById(dataTypeForm.getId());
	}


	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public void updateDataPermission(DataPermissionPerForm dataPermissionPerForm, UserVo user) {
		String key = "update_data_permission_" + user.getEnterpriseid() + "_" + dataPermissionPerForm.getId();
		boolean flag = iDistributedLock.lock(key, 2);
		try {
			// 编辑权限要加锁,避免冲突
			if (flag) {
				iDataPermissionService.updateDataPermission(dataPermissionPerForm);
			} else {
				throw new FrameworkRuntimeException(ResultCode.Fail, "Locking");
			}
		} catch (Exception e) {
			throw new FrameworkRuntimeException(ResultCode.Fail, e.getMessage(), e);
		} finally {
			if (flag) {
				iDistributedLock.releaseLock(key);
			}
		}
	}
}
