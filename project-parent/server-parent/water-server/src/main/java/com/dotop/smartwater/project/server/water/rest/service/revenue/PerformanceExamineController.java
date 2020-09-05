package com.dotop.smartwater.project.server.water.rest.service.revenue;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.dotop.smartwater.dependence.core.common.BaseController;
import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.DateUtils;
import com.dotop.smartwater.dependence.core.utils.VerificationUtils;
import com.dotop.smartwater.project.module.api.revenue.IPerformanceExamFactory;
import com.dotop.smartwater.project.module.api.revenue.IPerformanceTemplateFactory;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.module.core.water.form.PerforExamAuditForm;
import com.dotop.smartwater.project.module.core.water.form.PerforExamForm;
import com.dotop.smartwater.project.module.core.water.form.PerforExamPersonnelForm;
import com.dotop.smartwater.project.module.core.water.form.PerforTemplateForm;
import com.dotop.smartwater.project.module.core.water.vo.PerforExamAuditVo;
import com.dotop.smartwater.project.module.core.water.vo.PerforExamFillVo;
import com.dotop.smartwater.project.module.core.water.vo.PerforExamPersonnelVo;
import com.dotop.smartwater.project.module.core.water.vo.PerforExamVo;
import com.dotop.smartwater.project.module.core.water.vo.PerforTemplateVo;

/**
 * 绩效考核-考核
 * 

 * @date 2019年2月27日
 * 
 */
@RestController

@RequestMapping("/perforExam")
public class PerformanceExamineController implements BaseController<PerforExamForm> {

	private static final Logger LOGGER = LogManager.getLogger(PerformanceExamineController.class);

	@Autowired
	private IPerformanceExamFactory performanceExamFactory;

	@Autowired
	private IPerformanceTemplateFactory iPerformanceTemplateFactory;

	private static final String AUDITID = "auditId";

	private static final String NUMBER = "number";

	private static final String ASSESSMENTID = "assessmentId";

	private static final String PERSONNEL = "personnel";

	private static final String TEMPLATE = "template";

	/**
	 * 考核分页查询
	 * 
	 * @param form
	 * @return
	 */
	@Override
	@PostMapping(value = "/page", produces = GlobalContext.PRODUCES)
	public String page(@RequestBody PerforExamForm form) {
		LOGGER.info(LogMsg.to("msg:", " 考核分页查询开始", form));
		Pagination<PerforExamVo> pagination = performanceExamFactory.page(form);
		LOGGER.info(LogMsg.to("msg:", " 考核分页查询结束", form));
		return resp(ResultCode.Success, ResultCode.SUCCESS, pagination);
	}

	/**
	 * 发起考核
	 * 
	 * @param form
	 * @return
	 */
	@Override
	@PostMapping(value = "/add", produces = GlobalContext.PRODUCES)
	public String add(@RequestBody PerforExamForm form) {
		LOGGER.info(LogMsg.to("msg:", " 发起考核开始", form));
		// 参数校验
		VerificationUtils.string("name", form.getName());
		VerificationUtils.string("templateid", form.getTemplateId());
		VerificationUtils.string("templatename", form.getTemplateName());
		VerificationUtils.string("totalScore", form.getTotalScore());
		VerificationUtils.string("passScore", form.getPassScore());
		VerificationUtils.string("examines", form.getExamines());
		VerificationUtils.string("audits", form.getAudits());
		VerificationUtils.string("examineIds", form.getExamineIds(), false, 10240);
		VerificationUtils.string("auditIds", form.getAuditIds(), false, 10240);
		VerificationUtils.string("startdate", form.getStartDate());
		VerificationUtils.string("enddate", form.getEndDate());
		VerificationUtils.string("isfill", form.getIsFill());

		try {
			// 验证开始时间是否大于结束时间
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			if (DateUtils.compare(sf.parse(form.getStartDate()), sf.parse(form.getEndDate()))) {
				return resp(ResultCode.TIME_ERROR, ResultCode.getMessage(ResultCode.TIME_ERROR), null);
			}
			performanceExamFactory.saveExam(form);
			LOGGER.info(LogMsg.to("msg:", " 发起考核结束", form));
		} catch (Exception e) {
			LOGGER.info(e.getMessage());
		}
		return resp(ResultCode.Success, ResultCode.SUCCESS, null);
	}

	/**
	 * 考核详情-考核对象详情
	 * 
	 * @param form
	 * @return
	 */
	@PostMapping(value = "/getPersonnels", produces = GlobalContext.PRODUCES)
	public String getPersonnels(@RequestBody PerforExamForm form) {
		LOGGER.info(LogMsg.to("msg:", "获取考核对象详情", form));
		// 参数校验
		String number = form.getNumber();
		VerificationUtils.string(NUMBER, number);
		Map<String, Object> data = new HashMap<>();

		PerforExamVo vo = performanceExamFactory.getExam(form);
		if (vo != null) {
			List<PerforExamPersonnelVo> listpers = performanceExamFactory.getExamPersonnels(form);
			data.put("exam", vo);
			data.put("personnels", listpers);
		}
		LOGGER.info(LogMsg.to("msg:", "获取考核对象结束", form));
		return resp(ResultCode.Success, ResultCode.SUCCESS, data);
	}

	/**
	 * 考核详情-审核人员详情
	 * 
	 * @param form
	 * @return
	 */
	@PostMapping(value = "/getaudits", produces = GlobalContext.PRODUCES)
	public String get(@RequestBody PerforExamPersonnelForm form) {
		LOGGER.info(LogMsg.to("msg:", "获取审核人员详情开始", form));
		// 参数校验
		VerificationUtils.string(NUMBER, form.getNumber());
		VerificationUtils.string(ASSESSMENTID, form.getAssessmentId());
		Map<String, Object> data = new HashMap<>();

		PerforExamForm examForm = new PerforExamForm();
		examForm.setNumber(form.getNumber());
		PerforExamVo exam = performanceExamFactory.getExam(examForm);

		// 查询考核人员信息
		PerforExamPersonnelVo vo = performanceExamFactory.getExamPersonnel(form);
		if (vo != null) {
			// 查询考核权重及评分信息
			List<PerforExamFillVo> listFills = performanceExamFactory.getExamFills(form);

			// 查询审核人员信息
			List<PerforExamAuditVo> listAudits = performanceExamFactory.getExamAudits(form);
			data.put(PERSONNEL, vo);
			data.put("weights", listFills);
			data.put("audits", listAudits);

			if (exam != null && exam.getTemplateId() != null) {
				PerforTemplateForm tempForm = new PerforTemplateForm();
				tempForm.setId(exam.getTemplateId());
				PerforTemplateVo template = iPerformanceTemplateFactory.getTemp(tempForm);
				data.put(TEMPLATE, template);
			}

		}
		LOGGER.info(LogMsg.to("msg:", "获取审核人员详情结束", form));
		return resp(ResultCode.Success, ResultCode.SUCCESS, data);
	}

	/**
	 * 绩效考核-删除
	 * 
	 * @param form
	 * @return
	 */
	@PostMapping(value = "/deleteExam", produces = GlobalContext.PRODUCES)
	public String deleteExam(@RequestBody PerforExamForm form) {
		LOGGER.info(LogMsg.to("msg:", "绩效考核删除开始", form));
		// 参数校验
		String number = form.getNumber();
		VerificationUtils.string(NUMBER, number);
		performanceExamFactory.deleteExam(form);
		LOGGER.info(LogMsg.to("msg:", "绩效考核删除结束", form));
		return resp(ResultCode.Success, ResultCode.SUCCESS, null);
	}

	/**
	 * 绩效考核-验证审核是否完成
	 * 
	 * @param form
	 * @return
	 */
	@PostMapping(value = "/auditEnd", produces = GlobalContext.PRODUCES)
	public String auditEnd(@RequestBody PerforExamForm form) {
		LOGGER.info(LogMsg.to("msg:", "验证绩效考核是否完成开始", form));
		// 参数校验
		String number = form.getNumber();
		VerificationUtils.string(NUMBER, number);
		int noAuditNumer = performanceExamFactory.auditEnd(form);
		LOGGER.info(LogMsg.to("msg:", "验证绩效考核是否完成结束", form));
		return resp(ResultCode.Success, ResultCode.SUCCESS, noAuditNumer);
	}

	/**
	 * 绩效考核-结束
	 * 
	 * @param form
	 * @return
	 */
	@PostMapping(value = "/endExam", produces = GlobalContext.PRODUCES)
	public String endExam(@RequestBody PerforExamForm form) {
		LOGGER.info(LogMsg.to("msg:", "结束当前批次号考核开始", "PerforExamForm", form));
		// 参数校验
		String number = form.getNumber();
		VerificationUtils.string(NUMBER, number);
		performanceExamFactory.endExam(form);
		LOGGER.info(LogMsg.to("msg:", "结束当前批次号考核结束", "PerforExamForm", form));
		return resp(ResultCode.Success, ResultCode.SUCCESS, null);
	}

	/**
	 * 个人考核-列表
	 * 
	 * @param form
	 * @return
	 */
	@PostMapping(value = "/self", produces = GlobalContext.PRODUCES)
	public String self(@RequestBody PerforExamPersonnelForm form) {
		LOGGER.info(LogMsg.to("msg:", "个人考核列表分页开始", "PerforExamPersonnelForm", form));
		Pagination<PerforExamPersonnelVo> pagination = performanceExamFactory.selfPage(form);
		LOGGER.info(LogMsg.to("msg:", "个人考核列表分页结束", "PerforExamPersonnelForm", form));
		return resp(ResultCode.Success, ResultCode.SUCCESS, pagination);
	}

	/**
	 * 退回审核人提交信息
	 * 
	 * @param form
	 * @return
	 */
	@PostMapping(value = "/returnAudit", produces = GlobalContext.PRODUCES)
	public String returnAudit(@RequestBody PerforExamAuditForm form) {
		LOGGER.info(LogMsg.to("msg:", "退回审核人提交信息开始", form));
		VerificationUtils.string(AUDITID, form.getAuditId());
		VerificationUtils.string(NUMBER, form.getNumber());
		VerificationUtils.string(ASSESSMENTID, form.getAssessmentId());
		performanceExamFactory.returnAudit(form);
		LOGGER.info(LogMsg.to("msg:", "退回审核人提交信息结束", form));
		return resp(ResultCode.Success, ResultCode.SUCCESS, null);
	}

	/**
	 * 个人考核详情
	 * 
	 * @param form
	 * @return
	 */
	@PostMapping(value = "/selfExamDetail", produces = GlobalContext.PRODUCES)
	public String selfExamDetail(@RequestBody PerforExamPersonnelForm form) {
		LOGGER.info(LogMsg.to("msg:", "获取个人考核详情开始", form));
		// 参数校验
		VerificationUtils.string(NUMBER, form.getNumber());
		VerificationUtils.string(ASSESSMENTID, form.getAssessmentId());
		Map<String, Object> data = new HashMap<>();

		PerforExamForm examForm = new PerforExamForm();
		examForm.setNumber(form.getNumber());
		PerforExamVo exam = performanceExamFactory.getExam(examForm);

		// 查询考核人员信息
		PerforExamPersonnelVo vo = performanceExamFactory.getExamPersonnel(form);
		if (vo != null) {
			// 查询考核权重及评分信息
			List<PerforExamFillVo> listFills = performanceExamFactory.getExamFills(form);
			data.put("exam", exam);
			data.put(PERSONNEL, vo);
			data.put("fills", listFills);

			if (exam != null && exam.getTemplateId() != null) {
				PerforTemplateForm tempForm = new PerforTemplateForm();
				tempForm.setId(exam.getTemplateId());
				PerforTemplateVo template = iPerformanceTemplateFactory.getTemp(tempForm);
				data.put(TEMPLATE, template);
			}
		}
		LOGGER.info(LogMsg.to("msg:", "获取个人考核详情结束", form));
		return resp(ResultCode.Success, ResultCode.SUCCESS, data);
	}

	/**
	 * 个人考核-提交
	 * 
	 * @param form
	 * @return
	 */
	@PostMapping(value = "/selfExamSubmit", produces = GlobalContext.PRODUCES)
	public String selfExamSubmit(@RequestBody PerforExamPersonnelForm form) {
		LOGGER.info(LogMsg.to("msg:", "个人考核提交开始", form));
		// 参数校验
		VerificationUtils.string(NUMBER, form.getNumber());
		VerificationUtils.string(ASSESSMENTID, form.getAssessmentId());
		VerificationUtils.string("submitStatus", form.getSubmitStatus());

		// 验证填报信息是否为空
		long count = (form.getFillArry() != null ? form.getFillArry().size() : 0);
		if (form.getFillArry() != null && count > 0) {
			performanceExamFactory.submitFills(form);
		} else {
			return resp(ResultCode.NO_FIND_FILL_SCORE_ERROR, ResultCode.getMessage(ResultCode.NO_FIND_FILL_SCORE_ERROR),
					null);
		}
		LOGGER.info(LogMsg.to("msg:", "个人考核提交结束", form));
		return resp(ResultCode.Success, ResultCode.SUCCESS, null);
	}

	/**
	 * 绩效审核-列表
	 * 
	 * @param form
	 * @return
	 */
	@PostMapping(value = "/selfAudit", produces = GlobalContext.PRODUCES)
	public String selfAudit(@RequestBody PerforExamAuditForm form) {
		LOGGER.info(LogMsg.to("msg:", "绩效审核列表分页开始", form));
		VerificationUtils.string(AUDITID, form.getAuditId());
		Pagination<PerforExamAuditVo> pagination = performanceExamFactory.auditPage(form);
		LOGGER.info(LogMsg.to("msg:", "绩效审核列表分页结束", form));
		return resp(ResultCode.Success, ResultCode.SUCCESS, pagination);
	}

	/**
	 * 绩效审核-审核详情
	 * 
	 * @param form
	 * @return
	 */
	@PostMapping(value = "/getAudit", produces = GlobalContext.PRODUCES)
	public String getAudit(@RequestBody PerforExamAuditForm form) {
		LOGGER.info(LogMsg.to("msg:", "获取绩效审核详情开始", form));
		// 参数校验
		VerificationUtils.string(NUMBER, form.getNumber());
		VerificationUtils.string(AUDITID, form.getAuditId());
		VerificationUtils.string(ASSESSMENTID, form.getAssessmentId());

		Map<String, Object> data = new HashMap<>();
		// 查询考核人员信息
		PerforExamPersonnelForm personnel = new PerforExamPersonnelForm();
		personnel.setNumber(form.getNumber());
		personnel.setAssessmentId(form.getAssessmentId());
		PerforExamPersonnelVo vo = performanceExamFactory.getExamPersonnel(personnel);
		// 查询考核权重及评分信息
		vo.setFills(performanceExamFactory.getExamFills(personnel));
		// 查询审核信息
		PerforExamAuditVo audit = performanceExamFactory.getAudit(form);

		PerforExamForm examForm = new PerforExamForm();
		examForm.setNumber(form.getNumber());
		PerforExamVo exam = performanceExamFactory.getExam(examForm);
		if (exam != null && exam.getTemplateId() != null) {
			PerforTemplateForm tempForm = new PerforTemplateForm();
			tempForm.setId(exam.getTemplateId());
			PerforTemplateVo template = iPerformanceTemplateFactory.getTemp(tempForm);
			data.put(TEMPLATE, template);
		}
		data.put(PERSONNEL, vo);
		data.put("audit", audit);
		data.put("exam", exam);
		LOGGER.info(LogMsg.to("msg:", "获取绩效审核详情结束", form));
		return resp(ResultCode.Success, ResultCode.SUCCESS, data);
	}

	/**
	 * 绩效审核-提交
	 * 
	 * @param form
	 * @return
	 */
	@PostMapping(value = "/submitAudit", produces = GlobalContext.PRODUCES)
	public String submitAudit(@RequestBody PerforExamAuditForm form) {
		LOGGER.info(LogMsg.to("msg:", "绩效审核提交开始", form));
		// 参数校验
		VerificationUtils.string(NUMBER, form.getNumber());
		VerificationUtils.string(AUDITID, form.getAuditId());
		VerificationUtils.string(ASSESSMENTID, form.getAssessmentId());

		performanceExamFactory.submitAudit(form);
		LOGGER.info(LogMsg.to("msg:", "绩效审核提交结", form));
		return resp(ResultCode.Success, ResultCode.SUCCESS, null);
	}

}
