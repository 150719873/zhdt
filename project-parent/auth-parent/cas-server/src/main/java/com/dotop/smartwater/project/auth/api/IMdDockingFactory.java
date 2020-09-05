package com.dotop.smartwater.project.auth.api;

import com.dotop.smartwater.dependence.core.common.BaseFactory;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.auth.form.MdDockingEnterpriseForm;
import com.dotop.smartwater.project.module.core.auth.form.MdDockingForm;
import com.dotop.smartwater.project.module.core.auth.vo.MdDockingVo;
import com.dotop.smartwater.project.module.core.auth.vo.md.ConfigListVo;
import com.dotop.smartwater.project.module.core.auth.vo.md.MdDockingExtForm;
import com.dotop.smartwater.project.module.core.auth.vo.md.MdDockingExtVo;

import java.util.List;

/**

 */
public interface IMdDockingFactory extends BaseFactory<MdDockingForm, MdDockingVo> {

	@Override
	Pagination<MdDockingVo> page(MdDockingForm form);

	@Override
	MdDockingVo add(MdDockingForm form);

	@Override
	MdDockingVo edit(MdDockingForm form);

	@Override
	String del(MdDockingForm form);

	@Override
	MdDockingVo get(MdDockingForm form);

	@Override
	List<MdDockingVo> list(MdDockingForm form);

	List<MdDockingExtVo> load(MdDockingEnterpriseForm form);

	void save(MdDockingExtForm form);

	ConfigListVo loadConfigList(MdDockingEnterpriseForm form);
}
