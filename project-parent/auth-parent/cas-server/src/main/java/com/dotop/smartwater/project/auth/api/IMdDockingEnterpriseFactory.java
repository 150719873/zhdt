package com.dotop.smartwater.project.auth.api;

import com.dotop.smartwater.dependence.core.common.BaseFactory;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.auth.form.MdDockingEnterpriseForm;
import com.dotop.smartwater.project.module.core.auth.vo.EnterpriseVo;
import com.dotop.smartwater.project.module.core.auth.vo.MdDockingEnterpriseVo;

import java.util.List;

/**

 */
public interface IMdDockingEnterpriseFactory extends BaseFactory<MdDockingEnterpriseForm, MdDockingEnterpriseVo> {

	@Override
	Pagination<MdDockingEnterpriseVo> page(MdDockingEnterpriseForm form);

	@Override
	MdDockingEnterpriseVo add(MdDockingEnterpriseForm form);

	@Override
	MdDockingEnterpriseVo edit(MdDockingEnterpriseForm form);

	@Override
	String del(MdDockingEnterpriseForm form);

	@Override
	MdDockingEnterpriseVo get(MdDockingEnterpriseForm form);

	@Override
	List<MdDockingEnterpriseVo> list(MdDockingEnterpriseForm form);

    List<EnterpriseVo> enterpriseList();
}
