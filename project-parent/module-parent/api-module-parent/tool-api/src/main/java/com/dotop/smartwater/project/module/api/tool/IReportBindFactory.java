package com.dotop.smartwater.project.module.api.tool;

import java.util.List;

import com.dotop.smartwater.dependence.core.common.BaseFactory;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.water.bo.ReportBindBo;
import com.dotop.smartwater.project.module.core.water.form.ReportBindForm;
import com.dotop.smartwater.project.module.core.water.vo.ReportBindVo;

public interface IReportBindFactory extends BaseFactory<ReportBindForm, ReportBindVo> {

	@Override
	Pagination<ReportBindVo> page(ReportBindForm reportBindForm) ;

	@Override
	List<ReportBindVo> list(ReportBindForm reportBindForm) ;

	@Override
	ReportBindVo add(ReportBindForm reportBindForm) ;

	@Override
	String del(ReportBindForm reportBindForm) ;

	/**
	 * 通过bindids查询企业绑定报表信息
	 */
	List<ReportBindVo> listByBind(ReportBindBo reportBindBo) ;
}
