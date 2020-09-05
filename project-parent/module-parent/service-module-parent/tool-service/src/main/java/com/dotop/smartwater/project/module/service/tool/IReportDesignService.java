package com.dotop.smartwater.project.module.service.tool;

import java.util.List;

import com.dotop.smartwater.dependence.core.common.BaseService;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.water.bo.ReportDesignBo;
import com.dotop.smartwater.project.module.core.water.vo.ReportDesignVo;

/**
 * 
 * 报表展示设计Service

 * @date 2019-07-23
 *
 */

public interface IReportDesignService extends BaseService<ReportDesignBo, ReportDesignVo> {

	@Override
	Pagination<ReportDesignVo> page(ReportDesignBo reportDesignBo);

	@Override
	List<ReportDesignVo> list(ReportDesignBo reportDesignBo);
	
	@Override
	ReportDesignVo add(ReportDesignBo reportDesignBo);
	
	@Override
	ReportDesignVo get(ReportDesignBo reportDesignBo);

	@Override
	String del(ReportDesignBo reportDesignBo);
	
	@Override
	ReportDesignVo edit(ReportDesignBo reportDesignBo);
}
