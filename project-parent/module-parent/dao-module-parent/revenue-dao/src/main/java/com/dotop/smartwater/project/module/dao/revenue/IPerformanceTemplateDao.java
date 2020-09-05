package com.dotop.smartwater.project.module.dao.revenue;

import com.dotop.smartwater.dependence.core.common.BaseDao;
import com.dotop.smartwater.project.module.core.water.dto.PerforTemplateDto;
import com.dotop.smartwater.project.module.core.water.vo.PerforTemplateVo;

import java.util.List;

public interface IPerformanceTemplateDao extends BaseDao<PerforTemplateDto, PerforTemplateVo> {

	List<PerforTemplateVo> getList(PerforTemplateDto dto);

	int saveTemp(PerforTemplateDto dto);

	int updateTemp(PerforTemplateDto dto);

	PerforTemplateVo getTemp(PerforTemplateDto dto);

	int deleteTemp(PerforTemplateDto dto);

}
