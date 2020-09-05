package com.dotop.smartwater.project.module.service.tool;

import java.util.List;

import com.dotop.smartwater.dependence.core.common.BaseService;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.water.bo.ReportBindBo;
import com.dotop.smartwater.project.module.core.water.vo.ReportBindVo;

public interface IReportBindService extends BaseService<ReportBindBo, ReportBindVo> {

	@Override
	Pagination<ReportBindVo> page(ReportBindBo reportBindBo) ;

	@Override
	List<ReportBindVo> list(ReportBindBo reportBindBo) ;

	@Override
	ReportBindVo add(ReportBindBo reportBindBo) ;

	@Override
	String del(ReportBindBo reportBindBo) ;

}
