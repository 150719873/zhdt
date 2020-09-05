package com.dotop.smartwater.project.module.service.revenue.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.dotop.smartwater.dependence.core.common.RootModel;
import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.UuidUtils;
import com.dotop.smartwater.project.module.core.auth.bo.UserBo;
import com.dotop.smartwater.project.module.core.water.bo.PerforExamAuditBo;
import com.dotop.smartwater.project.module.core.water.bo.PerforExamBo;
import com.dotop.smartwater.project.module.core.water.bo.PerforExamFillBo;
import com.dotop.smartwater.project.module.core.water.bo.PerforExamPersonnelBo;
import com.dotop.smartwater.project.module.core.water.constants.ExamStatus;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.module.core.water.dto.PerforExamAuditDto;
import com.dotop.smartwater.project.module.core.water.dto.PerforExamDto;
import com.dotop.smartwater.project.module.core.water.dto.PerforExamFillDto;
import com.dotop.smartwater.project.module.core.water.dto.PerforExamPersonnelDto;
import com.dotop.smartwater.project.module.core.water.dto.PerforExamWeightDto;
import com.dotop.smartwater.project.module.core.water.dto.PerforTemplateDto;
import com.dotop.smartwater.project.module.core.water.utils.CalUtil;
import com.dotop.smartwater.project.module.core.water.vo.PerforExamAuditVo;
import com.dotop.smartwater.project.module.core.water.vo.PerforExamFillVo;
import com.dotop.smartwater.project.module.core.water.vo.PerforExamPersonnelVo;
import com.dotop.smartwater.project.module.core.water.vo.PerforExamVo;
import com.dotop.smartwater.project.module.core.water.vo.PerforExamWeightVo;
import com.dotop.smartwater.project.module.core.water.vo.PerforWeightVo;
import com.dotop.smartwater.project.module.dao.revenue.IPerformanceExamDao;
import com.dotop.smartwater.project.module.dao.revenue.IPerformanceWeightDao;
import com.dotop.smartwater.project.module.service.revenue.IPerformanceExamService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

@Service
public class PerformanceExamServiceImpl implements IPerformanceExamService {

	private static final Logger LOGGER = LogManager.getLogger(PerformanceExamServiceImpl.class);

	@Autowired
	private IPerformanceExamDao performanceExamDao;

	@Autowired
	private IPerformanceWeightDao performanceWeightDao;

	@Override
	public Pagination<PerforExamVo> page(PerforExamBo bo) {
		try {
			Integer isNotDel = RootModel.NOT_DEL;
			// 参数转换
			PerforExamDto dto = new PerforExamDto();
			BeanUtils.copyProperties(bo, dto);
			dto.setIsDel(isNotDel);
			Page<Object> pageHelper = PageHelper.startPage(bo.getPage(), bo.getPageCount());
			List<PerforExamVo> list = performanceExamDao.getList(dto);
			Pagination<PerforExamVo> pagination = new Pagination<>(bo.getPageCount(), bo.getPage());
			pagination.setData(list);
			pagination.setTotalPageSize(pageHelper.getTotal());
			return pagination;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public PerforExamVo getExam(PerforExamBo bo) {
		try {
			PerforExamDto dto = new PerforExamDto();
			BeanUtils.copyProperties(bo, dto);
			return performanceExamDao.getExam(dto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	public int auditEnd(PerforExamBo bo) {
		try {
			PerforExamDto dto = new PerforExamDto();
			BeanUtils.copyProperties(bo, dto);
			return performanceExamDao.auditEnd(dto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	public boolean endExam(PerforExamBo bo) {
		boolean flag = false;
		try {
			PerforExamDto dto = new PerforExamDto();
			BeanUtils.copyProperties(bo, dto);
			// 获取考核信息
			PerforExamVo exam = performanceExamDao.getExam(dto);
			// 获取所有考核对象
			List<PerforExamPersonnelVo> personnels = performanceExamDao.getExamPersonnels(dto);
			// 获取所有审核人员
			List<PerforExamAuditVo> audits = performanceExamDao.getExamSubmitAudit(dto);
			for (PerforExamPersonnelVo personnel : personnels) {
				int number = 0; // 验证每个人参与审核的人员的数量
				double average = 0.0D; // 记录每个人最终平均分数
				List<PerforExamAuditVo> personAudits = new ArrayList<>();
				for (PerforExamAuditVo audit : audits) {
					if (personnel.getAssessmentId().equals(audit.getAssessmentId())
							&& ExamStatus.ISSUBMIT.equals(personnel.getSubmitStatus())) {
						number++;
						personAudits.add(audit);
					}
				}

				if (number > 2) { // 如果审核人数大于2，则去掉最高分和最低分，取平均分
					average = average(personAudits, true);
				} else if (number > 0 && number <= 2) {
					// 如果审核人数大于0，小于2 则直接计算平均分
					average = average(personAudits, false);
				} else {
					// 如果当前没有人审核，则不计算当前考核对象得分
					continue;
				}

				personnel.setFinalScore(String.valueOf(average));
				// 如果当前考核及格分数小于最终得分，则认为及格
				if (Double.valueOf(exam.getPassScore()) < average) {
					personnel.setIsPass(ExamStatus.ISPASS);
				} else {
					personnel.setIsPass(ExamStatus.NOPASS);
				}

				PerforExamPersonnelDto pdto = new PerforExamPersonnelDto();
				BeanUtils.copyProperties(personnel, pdto);
				performanceExamDao.calPersonnelScore(pdto);
			}

			performanceExamDao.endExam(dto);
			flag = true;
			return flag;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	/**
	 * 计算平均分
	 * 
	 * @param audits
	 * @param temp
	 * @return
	 */
	public static double average(List<PerforExamAuditVo> audits, boolean temp) {
		double average;
		double totalScore = 0D;
		if (temp) { // 去除最高分和最低分
			for (int i = 0; i < audits.size(); i++) {
				if (i == 0 || i == (audits.size() - 1)) {
					continue;
				}
				totalScore = CalUtil.add(totalScore, Double.valueOf(audits.get(i).getScore()));
			}
			average = BigDecimal.valueOf(totalScore / (audits.size() - 2)).setScale(1, BigDecimal.ROUND_HALF_UP)
					.doubleValue();
		} else { // 直接计算
			for (PerforExamAuditVo vo : audits) {
				totalScore = CalUtil.add(totalScore, Double.valueOf(vo.getScore()));
			}
			average = BigDecimal.valueOf(totalScore / audits.size()).setScale(1, BigDecimal.ROUND_HALF_UP)
					.doubleValue();
		}
		return average;
	}

	public boolean deleteExam(PerforExamBo bo) {
		boolean flag = false;
		try {
			PerforExamDto dto = new PerforExamDto();
			BeanUtils.copyProperties(bo, dto);
			PerforExamVo vo = performanceExamDao.getExam(dto);
			if (vo.getStatus().equals("2")) {
				throw new FrameworkRuntimeException(String.valueOf(ResultCode.END_EXAM_NO_DELETE_ERROR),
						ResultCode.getMessage(ResultCode.END_EXAM_NO_DELETE_ERROR));
			}

			// 删除考核对象填报表
			PerforExamFillDto fdto = new PerforExamFillDto();
			fdto.setNumber(bo.getNumber());
			fdto.setEnterpriseid(bo.getEnterpriseid());
			performanceExamDao.deleteExamFill(fdto);

			// 删除考核权重表
			PerforExamWeightDto wdto = new PerforExamWeightDto();
			wdto.setNumber(bo.getNumber());
			wdto.setEnterpriseid(bo.getEnterpriseid());
			performanceExamDao.deleteExamWeight(wdto);

			// 删除审核人员表
			PerforExamAuditDto adto = new PerforExamAuditDto();
			adto.setNumber(bo.getNumber());
			adto.setEnterpriseid(bo.getEnterpriseid());
			performanceExamDao.deleteExamAudit(adto);

			// 删除考核对象表
			PerforExamPersonnelDto pdto = new PerforExamPersonnelDto();
			pdto.setNumber(bo.getNumber());
			pdto.setEnterpriseid(bo.getEnterpriseid());
			performanceExamDao.deleteExamPersonnel(pdto);

			// 删除考试表
			performanceExamDao.deleteExam(dto);
			flag = true;
			return flag;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	public List<PerforExamPersonnelVo> getExamPersonnels(PerforExamBo bo) {
		try {
			PerforExamDto dto = new PerforExamDto();
			BeanUtils.copyProperties(bo, dto);
			return performanceExamDao.getExamPersonnels(dto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public boolean saveExam(PerforExamBo bo) {
		boolean flag = false;
		try {
			PerforExamDto dto = new PerforExamDto();
			BeanUtils.copyProperties(bo, dto);
			dto.setId(UuidUtils.getUuid());
			dto.setExamines(String.valueOf(bo.getExamineUsers().size()));
			dto.setAudits(String.valueOf(bo.getAuditUsers().size()));

			// 根据模板ID获取权重信息
			PerforTemplateDto pdto = new PerforTemplateDto();
			pdto.setId(bo.getTemplateId());
			List<PerforWeightVo> weights = performanceWeightDao.getTempWeights(pdto);

			// 保存考核信息
			if (weights != null && !weights.isEmpty() && performanceExamDao.saveExam(dto) > 0) {

				// 生成考核权重信息并保存
				List<PerforExamWeightDto> listweight = new ArrayList<>();
				for (PerforWeightVo vo : weights) {
					PerforExamWeightDto obj = new PerforExamWeightDto();
					BeanUtils.copyProperties(vo, obj);
					obj.setId(UuidUtils.getUuid());
					obj.setNumber(bo.getNumber());
					listweight.add(obj);
				}
				performanceExamDao.saveExamWeights(listweight);

				// 生成考核对象信息
				List<PerforExamPersonnelDto> listPersonnel = new ArrayList<>();

				// 生成审核人员评分表
				List<PerforExamAuditDto> listAudits = new ArrayList<>();

				// 生成考核人员填报表
				List<PerforExamFillDto> listfills = new ArrayList<>();

				for (UserBo user : bo.getExamineUsers()) {
					PerforExamPersonnelDto userdto = new PerforExamPersonnelDto();
					userdto.setId(UuidUtils.getUuid());
					userdto.setNumber(bo.getNumber());
					userdto.setName(bo.getName());
					userdto.setAssessmentId(user.getUserid());
					userdto.setAssessmentName(user.getName());
					if (bo.getIsFill().equals(ExamStatus.NOFILL)) { // 无需填报
						userdto.setSubmitDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
						userdto.setSubmitStatus(ExamStatus.ISSUBMIT);
					} else {
						userdto.setSubmitStatus(ExamStatus.NOSUBMIT);
					}
					userdto.setAuditeds("0");
					userdto.setAuditStatus(ExamStatus.NOSTART);
					userdto.setEnterpriseid(bo.getEnterpriseid());
					userdto.setUserBy(bo.getUserBy());
					userdto.setCurr(bo.getCurr());
					listPersonnel.add(userdto);

					for (UserBo auser : bo.getAuditUsers()) {
						PerforExamAuditDto adto = new PerforExamAuditDto();
						adto.setId(UuidUtils.getUuid());
						adto.setNumber(bo.getNumber());
						adto.setAssessmentId(user.getUserid());
						adto.setAssessmentName(user.getName());
						adto.setAuditId(auser.getUserid());
						adto.setAuditName(auser.getName());
						adto.setEnterpriseid(bo.getEnterpriseid());
						adto.setUserBy(bo.getUserBy());
						adto.setCurr(bo.getCurr());
						adto.setAuditStatus(ExamStatus.NOSUBMIT);
						listAudits.add(adto);
					}

					for (PerforExamWeightDto weight : listweight) {
						PerforExamFillDto wdto = new PerforExamFillDto();
						wdto.setId(UuidUtils.getUuid());
						wdto.setNumber(bo.getNumber());
						wdto.setWeightId(weight.getId());
						wdto.setAssessmentId(user.getUserid());
						wdto.setAssessmentName(user.getName());
						wdto.setEnterpriseid(bo.getEnterpriseid());
						wdto.setUserBy(bo.getUserBy());
						wdto.setCurr(bo.getCurr());
						listfills.add(wdto);
					}
				}
				performanceExamDao.saveExamPersonnels(listPersonnel);
				performanceExamDao.saveExamAudits(listAudits);
				performanceExamDao.saveExamFills(listfills);
				flag = true;
			}
			return flag;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public PerforExamPersonnelVo getExamPersonnel(PerforExamPersonnelBo bo) {
		try {
			PerforExamPersonnelDto dto = new PerforExamPersonnelDto();
			BeanUtils.copyProperties(bo, dto);
			return performanceExamDao.getExamPersonnel(dto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public List<PerforExamAuditVo> getExamAudits(PerforExamPersonnelBo bo) {
		try {
			PerforExamPersonnelDto dto = new PerforExamPersonnelDto();
			BeanUtils.copyProperties(bo, dto);
			return performanceExamDao.getExamAudits(dto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public List<PerforExamWeightVo> getExamWeights(PerforExamPersonnelBo bo) {
		try {
			PerforExamPersonnelDto dto = new PerforExamPersonnelDto();
			BeanUtils.copyProperties(bo, dto);
			return performanceExamDao.getExamWeights(dto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public Pagination<PerforExamPersonnelVo> selfPage(PerforExamPersonnelBo bo) {
		try {
			Integer isNotDel = RootModel.NOT_DEL;
			// 参数转换
			PerforExamPersonnelDto dto = new PerforExamPersonnelDto();
			BeanUtils.copyProperties(bo, dto);
			dto.setIsDel(isNotDel);
			Page<Object> pageHelper = PageHelper.startPage(bo.getPage(), bo.getPageCount());
			List<PerforExamPersonnelVo> list = performanceExamDao.selfList(dto);
			Pagination<PerforExamPersonnelVo> pagination = new Pagination<>(bo.getPageCount(), bo.getPage());
			pagination.setData(list);
			pagination.setTotalPageSize(pageHelper.getTotal());
			return pagination;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public List<PerforExamFillVo> getExamFills(PerforExamPersonnelBo bo) {
		try {
			PerforExamPersonnelDto dto = new PerforExamPersonnelDto();
			BeanUtils.copyProperties(bo, dto);
			return performanceExamDao.getExamFills(dto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public boolean submitFills(PerforExamPersonnelBo bo) {
		boolean flag = false;
		try {
			PerforExamPersonnelDto dto = new PerforExamPersonnelDto();
			BeanUtils.copyProperties(bo, dto);

			dto.setSubmitDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

			double tScore = 0.0D;
			for (PerforExamFillBo fill : bo.getFillArry()) {
				tScore = CalUtil.add(tScore, Double.parseDouble(fill.getScore()));
			}
			dto.setFillScore(String.valueOf(tScore));
			// 更新考核人员信息
			if (performanceExamDao.updatePersonnel(dto) > 0) {
				// 更新考核人员填报信息
				for (PerforExamFillBo fill : bo.getFillArry()) {
					PerforExamFillDto fdto = new PerforExamFillDto();
					BeanUtils.copyProperties(fill, fdto);
					performanceExamDao.updatePersonnelScore(fdto);
				}

				flag = true;
			}

			// 修改考核状态为考核中
			PerforExamDto examDto = new PerforExamDto();
			examDto.setNumber(bo.getNumber());
			performanceExamDao.examination(examDto);

			return flag;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public Pagination<PerforExamAuditVo> auditPage(PerforExamAuditBo bo) {
		try {
			Integer isNotDel = RootModel.NOT_DEL;
			// 参数转换
			PerforExamAuditDto dto = new PerforExamAuditDto();
			BeanUtils.copyProperties(bo, dto);
			dto.setIsDel(isNotDel);
			Page<Object> pageHelper = PageHelper.startPage(bo.getPage(), bo.getPageCount());
			List<PerforExamAuditVo> list = performanceExamDao.auditPage(dto);
			Pagination<PerforExamAuditVo> pagination = new Pagination<>(bo.getPageCount(), bo.getPage());
			pagination.setData(list);
			pagination.setTotalPageSize(pageHelper.getTotal());
			return pagination;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public PerforExamAuditVo getAudit(PerforExamAuditBo bo) {
		try {
			PerforExamAuditDto dto = new PerforExamAuditDto();
			BeanUtils.copyProperties(bo, dto);
			return performanceExamDao.getAudit(dto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public boolean submitAudit(PerforExamAuditBo bo) {
		boolean flag = false;
		try {
			PerforExamAuditDto dto = new PerforExamAuditDto();

			PerforExamDto exam = new PerforExamDto();
			exam.setNumber(bo.getNumber());
			exam.setEnterpriseid(bo.getEnterpriseid());
			PerforExamVo examVo = performanceExamDao.getExam(exam);
			// 如果及格分数小于审核分数 则认为及格
			if (Double.parseDouble(examVo.getPassScore()) < Double.parseDouble(bo.getScore())) {
				bo.setIsPass(ExamStatus.ISPASS);
			} else {
				bo.setIsPass(ExamStatus.NOPASS);
			}
			BeanUtils.copyProperties(bo, dto);

			performanceExamDao.submitAudit(dto);
			if (bo.getAuditStatus().equals(ExamStatus.ISSUBMIT)) {
				// 更新已审核次数
				PerforExamPersonnelDto pdto = new PerforExamPersonnelDto();
				pdto.setEnterpriseid(bo.getEnterpriseid());
				pdto.setNumber(bo.getNumber());
				pdto.setAssessmentId(bo.getAssessmentId());
				performanceExamDao.updateAuditFrequency(pdto);
				flag = true;
			}

			// 修改考核状态为考核中
			PerforExamDto examDto = new PerforExamDto();
			examDto.setNumber(bo.getNumber());
			performanceExamDao.examination(examDto);

			return flag;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public boolean returnAudit(PerforExamAuditBo bo) {
		boolean flag = false;
		try {
			PerforExamAuditDto dto = new PerforExamAuditDto();
			BeanUtils.copyProperties(bo, dto);
			performanceExamDao.returnAudit(dto);
			return flag;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

}
