package com.dotop.smartwater.project.module.dao.tool;

import com.dotop.smartwater.dependence.core.common.BaseDao;
import com.dotop.smartwater.project.module.core.water.dto.ReportRoleBindDto;
import com.dotop.smartwater.project.module.core.water.vo.ReportRoleBindVo;

import java.util.List;

public interface IReportRoleBindDao extends BaseDao<ReportRoleBindDto, ReportRoleBindVo> {

	@Override
	List<ReportRoleBindVo> list(ReportRoleBindDto reportRoleBindDto);

	@Override
	void adds(List<ReportRoleBindDto> reportRoleBindDtos);

	@Override
	void dels(List<ReportRoleBindDto> reportRoleBindDtos);

}
