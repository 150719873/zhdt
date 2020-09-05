package com.dotop.smartwater.project.module.dao.revenue;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.dotop.smartwater.dependence.core.common.BaseDao;
import com.dotop.smartwater.project.module.core.water.dto.OwnerExtDto;
import com.dotop.smartwater.project.module.core.water.vo.OwnerExtVo;

/**

 */
public interface IOwnerExtDao extends BaseDao<OwnerExtDto, OwnerExtVo> {

	@Override
	void add(OwnerExtDto ownerExtDto);
	
	int batchAddExt(@Param("list") List<OwnerExtDto> exts);

	@Override
	Integer edit(OwnerExtDto ownerExtDto);

	@Override
	Integer del(OwnerExtDto ownerExtDto);

	@Override
	OwnerExtVo get(OwnerExtDto ownerExtDto);
}