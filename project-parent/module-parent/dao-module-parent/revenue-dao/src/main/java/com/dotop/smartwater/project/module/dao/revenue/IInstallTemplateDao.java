package com.dotop.smartwater.project.module.dao.revenue;

import com.dotop.smartwater.dependence.core.common.BaseDao;
import com.dotop.smartwater.project.module.core.water.dto.InstallTemplateDto;
import com.dotop.smartwater.project.module.core.water.vo.InstallTemplateVo;

import java.util.List;

public interface IInstallTemplateDao extends BaseDao<InstallTemplateDto, InstallTemplateVo> {

	List<InstallTemplateVo> getList(InstallTemplateDto dto);

	int saveTemp(InstallTemplateDto dto);

	InstallTemplateVo getTemp(InstallTemplateDto dto);

	int editTemp(InstallTemplateDto dto);

	int deleteTemp(InstallTemplateDto dto);

}
