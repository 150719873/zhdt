package com.dotop.smartwater.project.module.api.revenue;

import com.dotop.smartwater.dependence.core.common.BaseFactory;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.water.form.PerforTemplateForm;
import com.dotop.smartwater.project.module.core.water.vo.PerforTemplateVo;

public interface IPerformanceTemplateFactory extends BaseFactory<PerforTemplateForm, PerforTemplateVo> {

	Pagination<PerforTemplateVo> page(PerforTemplateForm form) ;

	boolean saveTemp(PerforTemplateForm form) ;

	PerforTemplateVo getTemp(PerforTemplateForm form) ;

	boolean updateTemp(PerforTemplateForm form) ;

	boolean deleteTemp(PerforTemplateForm form) ;

}
