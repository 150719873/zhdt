package com.dotop.pipe.server.rest.service.report;

import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dotop.pipe.core.form.LeakageAnalysisForm;
import com.dotop.pipe.core.form.LeakageAnalysisItemForm;
import com.dotop.pipe.core.vo.report.LeakageAnalysisVo;
import com.dotop.pipe.core.vo.report.TimingCalculationVo;
import com.dotop.pipe.web.api.factory.report.ILeakageAnalysisFactory;
import com.dotop.smartwater.dependence.core.common.BaseController;
import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.utils.VerificationUtils;

@RestController()
@RequestMapping("/report/leakage/analysis")
public class LeakageAnalysisController implements BaseController<LeakageAnalysisForm> {

	private final static Logger logger = LogManager.getLogger(LeakageAnalysisController.class);

	@Autowired
	private ILeakageAnalysisFactory iLeakageAnalysisFactory;

	// 漏损计算
	@PostMapping(value = "/calculation", produces = GlobalContext.PRODUCES)
	public String calculation(@RequestBody LeakageAnalysisForm leakageAnalysisForm) {
		logger.info(LogMsg.to("msg:", " 分页查询开始", "leakageAnalysisForm", leakageAnalysisForm));
		String type = leakageAnalysisForm.getType();
		String field = leakageAnalysisForm.getField();
		List<LeakageAnalysisItemForm> items = leakageAnalysisForm.getItems();
		Date startDate = leakageAnalysisForm.getStartDate();
		Date endDate = leakageAnalysisForm.getEndDate();
		// Date startDate = new Date(118, 10, 1, 0, 0, 0);
		// Date endDate = new Date(119, 2, 31, 0, 0, 0);
		// leakageAnalysisForm.setStartDate(startDate);
		// leakageAnalysisForm.setEndDate(endDate);

		// 验证
		VerificationUtils.string("type", type);
		VerificationUtils.string("field", field);
		VerificationUtils.objList("items", items);
		VerificationUtils.date("startDate", startDate); // 时间不能为空
		VerificationUtils.date("endDate", endDate); // 时间不能为空
		LeakageAnalysisVo calculation = iLeakageAnalysisFactory.calculation(leakageAnalysisForm);
		logger.info(LogMsg.to("msg:", " 分页查询查询结束"));
		return resp(calculation);
	}

	// 定时请求漏损计算(暂时设计由前端定时请求计算结果，参考TimingCalculationController.forecastData)
	// @GetMapping(value = "/timing1", produces = GlobalContext.PRODUCES)
	// public String timing1(LeakageAnalysisForm leakageAnalysisForm) {
	// logger.info(LogMsg.to("msg:", " 分页查询开始", "leakageAnalysisForm",
	// leakageAnalysisForm));
	// List<LeakageAnalysisVo> list =
	// iLeakageAnalysisFactory.timing1(leakageAnalysisForm);
	// logger.info(LogMsg.to("msg:", " 分页查询查询结束"));
	// return resp(list);
	// }

	// 定时请求漏损计算 (公式下面放结果)
	@GetMapping(value = "/timing", produces = GlobalContext.PRODUCES)
	public String timing(LeakageAnalysisForm leakageAnalysisForm) {
		logger.info(LogMsg.to("msg:", " 分页查询开始", "leakageAnalysisForm", leakageAnalysisForm));
		List<TimingCalculationVo> list = iLeakageAnalysisFactory.timing(leakageAnalysisForm);
		logger.info(LogMsg.to("msg:", " 分页查询查询结束"));
		return resp(list);
	}

}
