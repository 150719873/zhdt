package com.dotop.smartwater.project.module.dao.revenue;

import com.dotop.smartwater.dependence.core.common.BaseDao;
import com.dotop.smartwater.project.module.core.water.dto.InstallTemplateRelationDto;
import com.dotop.smartwater.project.module.core.water.vo.InstallTemplateRelationVo;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IInstallTemplateRelationDao extends BaseDao<InstallTemplateRelationDto, InstallTemplateRelationVo> {

	int saveTempRelations(@Param("list") List<InstallTemplateRelationDto> list);

	List<InstallTemplateRelationVo> getRelations(InstallTemplateRelationDto dto);

	int deleteRelations(InstallTemplateRelationDto dto);

}
