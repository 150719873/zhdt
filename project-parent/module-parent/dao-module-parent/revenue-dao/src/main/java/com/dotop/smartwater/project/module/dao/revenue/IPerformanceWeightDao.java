package com.dotop.smartwater.project.module.dao.revenue;

import com.dotop.smartwater.dependence.core.common.BaseDao;
import com.dotop.smartwater.project.module.core.water.dto.PerforTemplateDto;
import com.dotop.smartwater.project.module.core.water.dto.PerforWeightDto;
import com.dotop.smartwater.project.module.core.water.vo.PerforWeightVo;

import java.util.List;

public interface IPerformanceWeightDao extends BaseDao<PerforWeightDto, PerforWeightVo> {

	List<PerforWeightVo> getList(PerforWeightDto perforWeightDto);

	int addWeight(PerforWeightDto perforWeightDto);

	int updateWeight(PerforWeightDto perforWeightDto);

	int delete(PerforWeightDto perforWeightDto);

	PerforWeightVo getWeight(PerforWeightDto perforWeightDto);

	List<PerforWeightVo> getTempWeights(PerforTemplateDto dto);

}
