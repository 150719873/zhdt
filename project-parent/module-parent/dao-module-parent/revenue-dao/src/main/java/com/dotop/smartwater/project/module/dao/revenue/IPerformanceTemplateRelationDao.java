package com.dotop.smartwater.project.module.dao.revenue;

import com.dotop.smartwater.dependence.core.common.BaseDao;
import com.dotop.smartwater.project.module.core.water.dto.PerforTemplateDto;
import com.dotop.smartwater.project.module.core.water.dto.PerforTemplateRelationDto;
import com.dotop.smartwater.project.module.core.water.vo.PerforTemplateRelationVo;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IPerformanceTemplateRelationDao extends BaseDao<PerforTemplateRelationDto, PerforTemplateRelationVo> {

	int deleteTempRelation(PerforTemplateDto dto);

	int saveTempRelations(@Param("list") List<PerforTemplateRelationDto> perforTemplateRelationDtos);
}
