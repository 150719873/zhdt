package com.dotop.smartwater.project.module.service.revenue;

import com.dotop.smartwater.dependence.core.common.BaseService;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.water.bo.OwnerCancelRecordBo;
import com.dotop.smartwater.project.module.core.water.bo.customize.QueryBo;
import com.dotop.smartwater.project.module.core.water.vo.OwnerCancelRecordVo;

/**

 * @date 2019/2/27.
 */
public interface IOwnerCancelRecordService extends BaseService<OwnerCancelRecordBo, OwnerCancelRecordVo> {

	/**
	 * 分页查找
	 *
	 * @param queryBo
	 * @return
	 */
	Pagination<OwnerCancelRecordVo> findByPage(QueryBo queryBo) ;

}
