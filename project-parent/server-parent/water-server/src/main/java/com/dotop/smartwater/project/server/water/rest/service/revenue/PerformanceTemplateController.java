package com.dotop.smartwater.project.server.water.rest.service.revenue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.dotop.smartwater.dependence.core.common.BaseController;
import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.VerificationUtils;
import com.dotop.smartwater.project.module.api.revenue.IPerformanceTemplateFactory;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.module.core.water.form.PerforTemplateForm;
import com.dotop.smartwater.project.module.core.water.vo.PerforTemplateVo;

/**
 * 绩效考核-模板管理
 * 

 * @date 2019年2月27日
 * 
 */
@RestController

@RequestMapping("/perforTemp")
public class PerformanceTemplateController implements BaseController<PerforTemplateForm> {

	private static final Logger LOGGER = LogManager.getLogger(PerformanceTemplateController.class);

	@Autowired
	private IPerformanceTemplateFactory iPerformanceTemplateFactory;

	private static final int SIZE = 2048;

	// 考核分页查询
	@Override
	@PostMapping(value = "/page", produces = GlobalContext.PRODUCES)
	public String page(@RequestBody PerforTemplateForm form) {
		LOGGER.info(LogMsg.to("msg:", " 考核分页查询开始", form));
		Pagination<PerforTemplateVo> pagination = iPerformanceTemplateFactory.page(form);
		LOGGER.info(LogMsg.to("msg:", " 考核分页查询结束", form));
		return resp(ResultCode.Success, ResultCode.SUCCESS, pagination);
	}

	// 新增考核模板
	@Override
	@PostMapping(value = "/add", produces = GlobalContext.PRODUCES)
	public String add(@RequestBody PerforTemplateForm form) {
		LOGGER.info(LogMsg.to("msg:", " 新增考核模板开始", form));
		// 参数校验
		String name = form.getName();
		String weights = form.getWeights();
		String totalScore = form.getTotalScore();
		String passScore = form.getPassScore();
		String reports = form.getReports();

		VerificationUtils.string("name", name);
		VerificationUtils.string("weights", weights);
		VerificationUtils.string("totalScore", totalScore);
		VerificationUtils.string("passScore", passScore);
		VerificationUtils.string("reports", reports, false, SIZE);

		if (form.getWeightId() != null && form.getWeightId().length > 0) {

			iPerformanceTemplateFactory.saveTemp(form);
			LOGGER.info(LogMsg.to("msg:", "PerforTemplateForm", form));
			return resp(ResultCode.Success, ResultCode.SUCCESS, null);
		} else {
			return resp(ResultCode.WEIGHT_IS_NULL_ERROR, ResultCode.getMessage(ResultCode.WEIGHT_IS_NULL_ERROR), null);
		}
	}

	// 修改考核模板
	@Override
	@PostMapping(value = "/edit", produces = GlobalContext.PRODUCES)
	public String edit(@RequestBody PerforTemplateForm form) {
		LOGGER.info(LogMsg.to("msg:", " 修改考核模板开始", form));
		// 参数校验
		String id = form.getId();
		String name = form.getName();
		String weights = form.getName();
		String totalScore = form.getTotalScore();
		String passScore = form.getPassScore();
		String reports = form.getReports();

		VerificationUtils.string("id", id);
		VerificationUtils.string("name", name);
		VerificationUtils.string("weights", weights);
		VerificationUtils.string("totalScore", totalScore);
		VerificationUtils.string("passScore", passScore);
		VerificationUtils.string("reports", reports, false, 2048);

		iPerformanceTemplateFactory.updateTemp(form);
		LOGGER.info(LogMsg.to("msg:", " 修改考核模板结束", form));
		return resp(ResultCode.Success, ResultCode.SUCCESS, null);
	}

	// 详情
	@Override
	@PostMapping(value = "/get", produces = GlobalContext.PRODUCES)
	public String get(@RequestBody PerforTemplateForm form) {
		LOGGER.info(LogMsg.to("msg:", " 获取考核模板开始", form));
		// 参数校验
		String id = form.getId();
		VerificationUtils.string("id", id);
		PerforTemplateVo vo = iPerformanceTemplateFactory.getTemp(form);
		LOGGER.info(LogMsg.to("msg:", " 获取考核模板结束", form));
		return resp(ResultCode.Success, ResultCode.SUCCESS, vo);
	}

	// 删除模板
	@PostMapping(value = "/delete", produces = GlobalContext.PRODUCES)
	public String delete(@RequestBody PerforTemplateForm form) {
		LOGGER.info(LogMsg.to("msg:", "删除考核模板开始", form));
		// 参数校验
		String id = form.getId();
		VerificationUtils.string("id", id);
		iPerformanceTemplateFactory.deleteTemp(form);
		LOGGER.info(LogMsg.to("msg:", " 删除考核模板结束", form));
		return resp(ResultCode.Success, ResultCode.SUCCESS, null);
	}

}
