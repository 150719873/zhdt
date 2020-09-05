package com.dotop.smartwater.project.auth.service;

import com.dotop.smartwater.dependence.core.common.BaseService;
import com.dotop.smartwater.project.module.core.auth.bo.DataTypeBo;
import com.dotop.smartwater.project.module.core.auth.form.DataPermissionPerForm;
import com.dotop.smartwater.project.module.core.auth.vo.CheckboxOptionVo;
import com.dotop.smartwater.project.module.core.auth.vo.DataTypeVo;

import java.util.List;

/**

 * @date 2019/8/7.
 */

public interface IDataPermissionService extends BaseService<DataTypeBo, DataTypeVo> {

	List<CheckboxOptionVo> loadPermissionById(String id);

	void updateDataPermission(DataPermissionPerForm dataPermissionPerForm);
}
