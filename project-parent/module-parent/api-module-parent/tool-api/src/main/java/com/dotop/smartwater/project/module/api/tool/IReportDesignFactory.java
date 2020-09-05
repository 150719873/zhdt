package com.dotop.smartwater.project.module.api.tool;

import java.util.List;

import com.dotop.smartwater.dependence.core.common.BaseFactory;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.water.form.ReportDesignForm;
import com.dotop.smartwater.project.module.core.water.vo.ReportDesignVo;

/**
 * 
 * 报表展示设计Factory

 * @date 2019-07-23
 *
 */
public interface IReportDesignFactory extends BaseFactory<ReportDesignForm, ReportDesignVo> {
	
	@Override
	Pagination<ReportDesignVo> page(ReportDesignForm reportDesignForm);
	
	@Override
	List<ReportDesignVo> list(ReportDesignForm reportDesignForm);

	@Override
	ReportDesignVo add(ReportDesignForm reportDesignForm);
	
	@Override
	ReportDesignVo get(ReportDesignForm reportDesignForm);

	@Override
	String del(ReportDesignForm reportBindForm);
	
	@Override
	ReportDesignVo edit(ReportDesignForm reportBindForm);
}
