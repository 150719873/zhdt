package com.dotop.smartwater.project.module.service.revenue;

import com.dotop.smartwater.dependence.core.common.BaseService;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.water.bo.PerforTemplateBo;
import com.dotop.smartwater.project.module.core.water.vo.PerforTemplateVo;

public interface IPerformanceTemplateService extends BaseService<PerforTemplateBo, PerforTemplateVo> {

	@Override
	Pagination<PerforTemplateVo> page(PerforTemplateBo bo) ;

	boolean saveTemp(PerforTemplateBo bo) ;

	boolean updateTemp(PerforTemplateBo bo) ;

	PerforTemplateVo getTemp(PerforTemplateBo bo) ;

	boolean deleteTemp(PerforTemplateBo bo) ;
}
