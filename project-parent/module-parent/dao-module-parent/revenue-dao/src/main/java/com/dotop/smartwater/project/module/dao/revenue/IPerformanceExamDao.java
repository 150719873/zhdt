package com.dotop.smartwater.project.module.dao.revenue;

import java.util.List;

import org.springframework.data.repository.query.Param;

import com.dotop.smartwater.dependence.core.common.BaseDao;
import com.dotop.smartwater.project.module.core.water.dto.PerforExamAuditDto;
import com.dotop.smartwater.project.module.core.water.dto.PerforExamDto;
import com.dotop.smartwater.project.module.core.water.dto.PerforExamFillDto;
import com.dotop.smartwater.project.module.core.water.dto.PerforExamPersonnelDto;
import com.dotop.smartwater.project.module.core.water.dto.PerforExamWeightDto;
import com.dotop.smartwater.project.module.core.water.vo.PerforExamAuditVo;
import com.dotop.smartwater.project.module.core.water.vo.PerforExamFillVo;
import com.dotop.smartwater.project.module.core.water.vo.PerforExamPersonnelVo;
import com.dotop.smartwater.project.module.core.water.vo.PerforExamVo;
import com.dotop.smartwater.project.module.core.water.vo.PerforExamWeightVo;

public interface IPerformanceExamDao extends BaseDao<PerforExamDto, PerforExamVo> {

	List<PerforExamVo> getList(PerforExamDto dto);

	int saveExam(PerforExamDto dto);

	int saveExamWeights(@Param("list") List<PerforExamWeightDto> list);

	int saveExamPersonnels(@Param("list") List<PerforExamPersonnelDto> listPersonnels);

	int saveExamAudits(@Param("list") List<PerforExamAuditDto> listaudits);

	int saveExamFills(@Param("list") List<PerforExamFillDto> listfills);

	PerforExamVo getExam(PerforExamDto dto);

	int auditEnd(PerforExamDto dto);

	int deleteExam(PerforExamDto dto);

	int deleteExamAudit(PerforExamAuditDto dto);

	int deleteExamFill(PerforExamFillDto dto);

	int deleteExamWeight(PerforExamWeightDto dto);

	int deleteExamPersonnel(PerforExamPersonnelDto dto);

	List<PerforExamPersonnelVo> getExamPersonnels(PerforExamDto dto);

	List<PerforExamAuditVo> getExamSubmitAudit(PerforExamDto dto);

	int calPersonnelScore(PerforExamPersonnelDto dto);

	int endExam(PerforExamDto dto);

	PerforExamPersonnelVo getExamPersonnel(PerforExamPersonnelDto dto);

	List<PerforExamAuditVo> getExamAudits(PerforExamPersonnelDto dto);

	List<PerforExamWeightVo> getExamWeights(PerforExamPersonnelDto dto);

	List<PerforExamPersonnelVo> selfList(PerforExamPersonnelDto dto);

	List<PerforExamFillVo> getExamFills(PerforExamPersonnelDto dto);

	int updatePersonnel(PerforExamPersonnelDto dto);

	int updatePersonnelScore(PerforExamFillDto fill);

	int examination(PerforExamDto dto);

	int updatePersonnelScores(@Param("list") List<PerforExamFillDto> fills);

	int updateAuditFrequency(PerforExamPersonnelDto dto);

	int returnAudit(PerforExamAuditDto dto);

	List<PerforExamAuditVo> auditPage(PerforExamAuditDto dto);

	PerforExamAuditVo getAudit(PerforExamAuditDto dto);

	int submitAudit(PerforExamAuditDto dto);

}
