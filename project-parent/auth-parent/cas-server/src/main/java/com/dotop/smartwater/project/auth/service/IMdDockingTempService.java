package com.dotop.smartwater.project.auth.service;

import com.dotop.smartwater.dependence.core.common.BaseService;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.auth.bo.MdDockingTempBo;
import com.dotop.smartwater.project.module.core.auth.vo.MdDockingTempVo;

import java.util.List;

/**

 * @date 2019年5月9日
 * @description
 */
public interface IMdDockingTempService extends BaseService<MdDockingTempBo, MdDockingTempVo> {

	@Override
	Pagination<MdDockingTempVo> page(MdDockingTempBo bo);

	@Override
	MdDockingTempVo get(MdDockingTempBo bo);

	@Override
	MdDockingTempVo add(MdDockingTempBo bo);

	@Override
	MdDockingTempVo edit(MdDockingTempBo bo);

	@Override
	String del(MdDockingTempBo bo);

	@Override
	List<MdDockingTempVo> list(MdDockingTempBo bo);
}
