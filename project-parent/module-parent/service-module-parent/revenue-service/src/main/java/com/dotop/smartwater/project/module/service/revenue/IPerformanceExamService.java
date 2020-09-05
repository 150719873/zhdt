package com.dotop.smartwater.project.module.service.revenue;

import java.util.List;

import com.dotop.smartwater.dependence.core.common.BaseService;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.water.bo.PerforExamAuditBo;
import com.dotop.smartwater.project.module.core.water.bo.PerforExamBo;
import com.dotop.smartwater.project.module.core.water.bo.PerforExamPersonnelBo;
import com.dotop.smartwater.project.module.core.water.vo.PerforExamAuditVo;
import com.dotop.smartwater.project.module.core.water.vo.PerforExamFillVo;
import com.dotop.smartwater.project.module.core.water.vo.PerforExamPersonnelVo;
import com.dotop.smartwater.project.module.core.water.vo.PerforExamVo;
import com.dotop.smartwater.project.module.core.water.vo.PerforExamWeightVo;

public interface IPerformanceExamService extends BaseService<PerforExamBo, PerforExamVo> {

	@Override
	Pagination<PerforExamVo> page(PerforExamBo bo);

	boolean saveExam(PerforExamBo bo);

	PerforExamVo getExam(PerforExamBo bo);

	boolean deleteExam(PerforExamBo bo);

	List<PerforExamPersonnelVo> getExamPersonnels(PerforExamBo bo);

	PerforExamPersonnelVo getExamPersonnel(PerforExamPersonnelBo bo);

	List<PerforExamAuditVo> getExamAudits(PerforExamPersonnelBo bo);

	int auditEnd(PerforExamBo bo);

	boolean endExam(PerforExamBo bo);

	List<PerforExamWeightVo> getExamWeights(PerforExamPersonnelBo bo);

	Pagination<PerforExamPersonnelVo> selfPage(PerforExamPersonnelBo bo);

	List<PerforExamFillVo> getExamFills(PerforExamPersonnelBo bo);

	boolean submitFills(PerforExamPersonnelBo bo);

	Pagination<PerforExamAuditVo> auditPage(PerforExamAuditBo bo);

	PerforExamAuditVo getAudit(PerforExamAuditBo bo);

	boolean submitAudit(PerforExamAuditBo bo);

	boolean returnAudit(PerforExamAuditBo bo);

}
