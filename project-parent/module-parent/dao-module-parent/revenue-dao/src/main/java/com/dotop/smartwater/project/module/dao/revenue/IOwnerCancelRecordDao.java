package com.dotop.smartwater.project.module.dao.revenue;

import com.dotop.smartwater.dependence.core.common.BaseDao;
import com.dotop.smartwater.project.module.core.water.dto.OwnerCancelRecordDto;
import com.dotop.smartwater.project.module.core.water.dto.customize.QueryDto;
import com.dotop.smartwater.project.module.core.water.vo.OwnerCancelRecordVo;

import java.util.List;

/**

 * @date 2019/2/27.
 */
public interface IOwnerCancelRecordDao extends BaseDao<OwnerCancelRecordDto, OwnerCancelRecordVo> {

	/**
	 * 查询列表
	 *
	 * @param queryDto
	 * @return
	 * @
	 */
	List<OwnerCancelRecordVo> getList(QueryDto queryDto);
}
