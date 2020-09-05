package com.dotop.smartwater.project.module.api.tool;

import com.dotop.smartwater.dependence.core.common.BaseFactory;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.water.form.DataBackupForm;
import com.dotop.smartwater.project.module.core.water.vo.DataBackupVo;

/**
 * 数据备份API
 *

 * @date 2019年2月23日
 */
public interface IDataBackupFactory extends BaseFactory<DataBackupForm, DataBackupVo> {

	/**
	 * 分页查询
	 */
	@Override
	Pagination<DataBackupVo> page(DataBackupForm dataBackupForm);

	/**
	 * 下载
	 */
	@Override
	DataBackupVo get(DataBackupForm dataBackupForm);

	/**
	 * 新增
	 */
	@Override
	DataBackupVo add(DataBackupForm dataBackupForm);

	/**
	 * 删除
	 */
	@Override
	String del(DataBackupForm dataBackupForm);

	void backupDb();
}
