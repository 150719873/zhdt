package com.dotop.smartwater.project.auth.api;

import com.dotop.smartwater.dependence.core.common.BaseFactory;
import com.dotop.smartwater.project.module.core.auth.form.DataPermissionPerForm;
import com.dotop.smartwater.project.module.core.auth.form.DataTypeForm;
import com.dotop.smartwater.project.module.core.auth.vo.CheckboxOptionVo;
import com.dotop.smartwater.project.module.core.auth.vo.DataTypeVo;
import com.dotop.smartwater.project.module.core.auth.vo.UserVo;

import java.util.List;

/**

 * @date 2019/8/7.
 */
public interface IDataPermissionFactory extends BaseFactory<DataTypeForm, DataTypeVo> {
	/**
	 * 加载数据权限
	 *
	 * @param dataTypeForm
	 * @return
	 */
	List<CheckboxOptionVo> loadPermissionById(DataTypeForm dataTypeForm);

	/**
	 * 更新数据权限
	 *
	 * @param dataPermissionPerForm
	 * @param user
	 */
	void updateDataPermission(DataPermissionPerForm dataPermissionPerForm, UserVo user);
}
