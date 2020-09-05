package com.dotop.smartwater.project.module.service.tool;

import java.util.List;

import com.dotop.smartwater.dependence.core.common.BaseService;
import com.dotop.smartwater.project.module.core.water.bo.ReportRoleBindBo;
import com.dotop.smartwater.project.module.core.water.vo.ReportRoleBindVo;

public interface IReportRoleBindService extends BaseService<ReportRoleBindBo, ReportRoleBindVo> {

	@Override
	List<ReportRoleBindVo> list(ReportRoleBindBo reportRoleBindBo) ;

	@Override
	void adds(List<ReportRoleBindBo> reportRoleBindBos) ;

	@Override
	void dels(List<ReportRoleBindBo> reportRoleBindBos) ;
}
