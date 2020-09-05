package com.dotop.pipe.server.rest.service.report;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dotop.pipe.core.vo.report.TimingCalculationVo;
import com.dotop.pipe.data.report.core.enums.TimingStatusEnum;
import com.dotop.pipe.data.report.core.form.TimingCalculationForm;
import com.dotop.pipe.data.report.core.form.TimingFormulaForm;
import com.dotop.pipe.web.api.factory.report.ITimingCalculationFactory;
import com.dotop.smartwater.dependence.core.common.BaseController;
import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.VerificationUtils;

/**
 * 
 *
 * @date 2019年1月16日
 */

@RestController()
@RequestMapping("/report/timing/calculation")
public class TimingCalculationController implements BaseController<TimingCalculationForm> {

	private final static Logger logger = LogManager.getLogger(TimingCalculationController.class);

	@Autowired
	private ITimingCalculationFactory iTimingCalculationFactory;

	/**
	 * 分页查询
	 */
	@Override
	@GetMapping(value = "/page/{page}/{pageSize}", produces = GlobalContext.PRODUCES)
	public String page(TimingCalculationForm timingCalculationForm) {
		logger.info(LogMsg.to("msg:", " 分页查询开始", "timingCalculationForm", timingCalculationForm));
		Integer page = timingCalculationForm.getPage();
		Integer pageSize = timingCalculationForm.getPageSize();
		// 验证
		VerificationUtils.integer("page", page);
		VerificationUtils.integer("pageSize", pageSize);
		Pagination<TimingCalculationVo> pagination = iTimingCalculationFactory.page(timingCalculationForm);
		logger.info(LogMsg.to("msg:", " 分页查询查询结束"));
		return resp(pagination);
	}

	/**
	 * 详情
	 */
	@Override
	@GetMapping(value = "/{tcId}", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
	public String get(TimingCalculationForm timingCalculationForm) {
		logger.info(LogMsg.to("msg:", "详情查询开始", "timingCalculationForm", timingCalculationForm));
		String tcId = timingCalculationForm.getTcId();
		// 验证
		VerificationUtils.string("tcId", tcId);
		TimingCalculationVo obj = iTimingCalculationFactory.get(timingCalculationForm);
		logger.info(LogMsg.to("msg:", "详情查询结束"));
		return resp(obj);
	}

	/**
	 * 新增
	 */
	@Override
	@PostMapping(produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
	public String add(@RequestBody TimingCalculationForm timingCalculationForm) {
		logger.info(LogMsg.to("msg:", "新增开始", "timingCalculationForm", timingCalculationForm));
		String code = timingCalculationForm.getCode();
		String name = timingCalculationForm.getName();
		String des = timingCalculationForm.getDes();
		TimingStatusEnum status = timingCalculationForm.getStatus();
		List<TimingFormulaForm> formulas = timingCalculationForm.getFormulas();
		// 校验
		VerificationUtils.string("code", code);
		VerificationUtils.string("name", name);
		VerificationUtils.string("des", des, true, 100);
		VerificationUtils.obj("status", status);
		VerificationUtils.objList("formulas", formulas);
		TimingCalculationVo obj = iTimingCalculationFactory.add(timingCalculationForm);
		return resp(obj);
	}

	/**
	 * 修改
	 */
	@Override
	@PutMapping(produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
	public String edit(@RequestBody TimingCalculationForm timingCalculationForm) {
		logger.info(LogMsg.to("timingCalculationForm", timingCalculationForm));
		String tcId = timingCalculationForm.getTcId();
		String code = timingCalculationForm.getCode();
		String name = timingCalculationForm.getName();
		String des = timingCalculationForm.getDes();
		List<TimingFormulaForm> formulas = timingCalculationForm.getFormulas();
		// 校验
		VerificationUtils.string("tcId", tcId);
		VerificationUtils.string("code", code);
		VerificationUtils.string("name", name);
		VerificationUtils.string("des", des, true, 100);
		VerificationUtils.objList("formulas", formulas);
		TimingCalculationVo obj = iTimingCalculationFactory.edit(timingCalculationForm);
		return resp(obj);
	}

	/**
	 * 删除
	 */
	@Override
	@DeleteMapping(value = "/{tcId}", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
	public String del(TimingCalculationForm timingCalculationForm) {
		logger.info(LogMsg.to("msg:", "删除开始", timingCalculationForm, timingCalculationForm));
		String tcId = timingCalculationForm.getTcId();
		// 校验
		VerificationUtils.string("tcId", tcId);
		String count = iTimingCalculationFactory.del(timingCalculationForm);
		logger.info(LogMsg.to("msg:", "删除结束", "更新数据", count));
		return resp();
	}

	/**
	 * 爆管预测获取数据(开启后台定时计算，此功能暂时由前端定时，参考LeakageAnalysisController.timing)
	 * 
	 * @return
	 */
	@GetMapping(value = "/forecast/data", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
	public String forecastData() {
		logger.info(LogMsg.to("msg:", "爆管预测获取数据开始"));
		logger.info(LogMsg.to("msg:", "爆管预测获取数据结束"));
		return resp();
	}

}
