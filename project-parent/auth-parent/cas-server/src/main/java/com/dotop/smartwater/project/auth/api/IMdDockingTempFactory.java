package com.dotop.smartwater.project.auth.api;

import com.dotop.smartwater.dependence.core.common.BaseFactory;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.auth.form.MdDockingTempForm;
import com.dotop.smartwater.project.module.core.auth.vo.MdDockingTempVo;

import java.util.List;

/**

 */
public interface IMdDockingTempFactory extends BaseFactory<MdDockingTempForm, MdDockingTempVo> {

	@Override
	Pagination<MdDockingTempVo> page(MdDockingTempForm form);

	@Override
	MdDockingTempVo add(MdDockingTempForm form);

	@Override
	MdDockingTempVo edit(MdDockingTempForm form);

	@Override
	String del(MdDockingTempForm form);

	@Override
	MdDockingTempVo get(MdDockingTempForm form);

	@Override
	List<MdDockingTempVo> list(MdDockingTempForm form);

}
