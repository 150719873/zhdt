package com.dotop.smartwater.project.module.service.revenue;

import java.util.List;

import com.dotop.smartwater.dependence.core.common.BaseService;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.water.bo.InstallTemplateBo;
import com.dotop.smartwater.project.module.core.water.vo.InstallFunctionVo;
import com.dotop.smartwater.project.module.core.water.vo.InstallTemplateRelationVo;
import com.dotop.smartwater.project.module.core.water.vo.InstallTemplateVo;

public interface IInstallTemplateService extends BaseService<InstallTemplateBo, InstallTemplateVo> {

	@Override
	Pagination<InstallTemplateVo> page(InstallTemplateBo bo);

	boolean saveTemp(InstallTemplateBo bo);

	List<InstallFunctionVo> getFuncs();

	InstallTemplateVo getTemp(InstallTemplateBo bo);

	boolean editTemp(InstallTemplateBo bo);

	boolean deleteTemp(InstallTemplateBo bo);

	List<InstallTemplateRelationVo> getTempNodes(InstallTemplateBo bo);

}
