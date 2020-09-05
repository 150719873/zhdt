package com.dotop.smartwater.project.auth.service;

import com.dotop.smartwater.dependence.core.common.BaseService;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.auth.bo.MdDockingEnterpriseBo;
import com.dotop.smartwater.project.module.core.auth.vo.EnterpriseVo;
import com.dotop.smartwater.project.module.core.auth.vo.MdDockingEnterpriseVo;

import java.util.List;

/**

 * @date 2019年5月9日
 * @description
 */
public interface IMdDockingEnterpriseService extends BaseService<MdDockingEnterpriseBo, MdDockingEnterpriseVo> {

	@Override
	Pagination<MdDockingEnterpriseVo> page(MdDockingEnterpriseBo bo);

	@Override
	MdDockingEnterpriseVo get(MdDockingEnterpriseBo bo);

	@Override
	MdDockingEnterpriseVo add(MdDockingEnterpriseBo bo);

	@Override
	MdDockingEnterpriseVo edit(MdDockingEnterpriseBo bo);

	@Override
	String del(MdDockingEnterpriseBo bo);

	@Override
	List<MdDockingEnterpriseVo> list(MdDockingEnterpriseBo bo);

    List<EnterpriseVo> enterpriseList();
}
