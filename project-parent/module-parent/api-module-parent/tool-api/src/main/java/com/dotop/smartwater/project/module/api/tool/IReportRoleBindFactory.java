package com.dotop.smartwater.project.module.api.tool;

import java.util.List;

import com.dotop.smartwater.dependence.core.common.BaseFactory;
import com.dotop.smartwater.project.module.core.water.bo.ReportRoleBindBo;
import com.dotop.smartwater.project.module.core.water.form.ReportRoleBindForm;
import com.dotop.smartwater.project.module.core.water.vo.ReportRoleBindVo;

public interface IReportRoleBindFactory extends BaseFactory<ReportRoleBindForm, ReportRoleBindVo> {

	@Override
	List<ReportRoleBindVo> list(ReportRoleBindForm reportRoleBindForm) ;

	@Override
	ReportRoleBindVo edit(ReportRoleBindForm reportRoleBindForm) ;

	/**
	 * 通过角色id获取角色绑定的报表id
	 */
	List<ReportRoleBindVo> listByRole(ReportRoleBindBo reportRoleBindBo) ;
}
