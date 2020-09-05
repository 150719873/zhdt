package com.dotop.smartwater.project.module.service.tool;

import com.dotop.smartwater.dependence.core.common.BaseService;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.water.bo.DataBackupBo;
import com.dotop.smartwater.project.module.core.water.vo.DataBackupVo;

/**
 * 数据备份
 * 

 * @date 2019年2月23日
 */
public interface IDataBackupService extends BaseService<DataBackupBo, DataBackupVo> {

	/**
	 * 分页查询
	 */
	@Override
	Pagination<DataBackupVo> page(DataBackupBo dataBackupBo);

	/**
	 * 下载
	 */
	@Override
	DataBackupVo get(DataBackupBo dataBackupBo);

	/**
	 * 新增
	 */
	@Override
	DataBackupVo add(DataBackupBo dataBackupBo);

	/**
	 * 删除
	 */
	@Override
	String del(DataBackupBo dataBackupBo);
}
