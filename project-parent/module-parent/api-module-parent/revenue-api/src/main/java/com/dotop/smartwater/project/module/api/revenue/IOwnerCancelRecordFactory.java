package com.dotop.smartwater.project.module.api.revenue;

import com.dotop.smartwater.dependence.core.common.BaseFactory;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.water.form.OwnerCancelRecordForm;
import com.dotop.smartwater.project.module.core.water.form.customize.QueryForm;
import com.dotop.smartwater.project.module.core.water.vo.OwnerCancelRecordVo;

/**
 * 业主销户

 * @date 2019/2/27.
 */
public interface IOwnerCancelRecordFactory extends BaseFactory<OwnerCancelRecordForm, OwnerCancelRecordVo> {

	/**
	 * 分頁查询
	 *
	 * @param queryForm 参数对象
	 * @return 分頁
	 */
	Pagination<OwnerCancelRecordVo> findByPage(QueryForm queryForm) ;
}
