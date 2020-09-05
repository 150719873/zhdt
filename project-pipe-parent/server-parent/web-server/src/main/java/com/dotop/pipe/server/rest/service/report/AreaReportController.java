package com.dotop.pipe.server.rest.service.report;

import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dotop.pipe.core.enums.DateTypeEnum;
import com.dotop.pipe.data.report.core.form.AreaReportForm;
import com.dotop.pipe.data.report.core.vo.AreaReportVo;
import com.dotop.pipe.web.api.factory.report.IAreaReportFactory;
import com.dotop.smartwater.dependence.core.common.BaseController;
import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.VerificationUtils;

/**
 * 
 */
@RestController()
@RequestMapping("/areaReport")
public class AreaReportController implements BaseController<AreaReportForm> {
	private final static Logger logger = LogManager.getLogger(AreaReportController.class);

	@Autowired
	private IAreaReportFactory iAreaReportFactory;

	public AreaReportController() {
		super();
	}

	/**
	 * 区域报表查询
	 * 
	 * @param page
	 * @param pageSize
	 * @param areaReportForm
	 * @return
	 */
	@PostMapping(value = "/{page}/{pageSize}", produces = GlobalContext.PRODUCES)
	public String realTime(@PathVariable("page") Integer page, @PathVariable("pageSize") Integer pageSize,
			@RequestBody AreaReportForm areaReportForm) {
		logger.info(LogMsg.to("msg:", "报表统计查询开始"));
		logger.info(LogMsg.to("page", page, "pageSize", pageSize));
		String sensorCode = null;
		Date startDate = areaReportForm.getStartDate();
		Date endDate = areaReportForm.getEndDate();
		String sensorType = null;
		DateTypeEnum dateType = areaReportForm.getDateType();
		String areaId = areaReportForm.getAreaId();

		// 验证
		VerificationUtils.integer("page", page);
		VerificationUtils.integer("pageSize", pageSize);
		// 时间不能为空
		VerificationUtils.date("startDate", startDate);
		// 时间不能为空
		VerificationUtils.date("endDate", endDate);

		Pagination<AreaReportVo> pagination = iAreaReportFactory.getTotalByArea(sensorCode, startDate, endDate,
				sensorType, page, pageSize, dateType, areaId);
		logger.info(LogMsg.to("msg:", "报表统计查询结束"));
		return resp(pagination);

	}

	/**
	 * 区域预警次数统计
	 * 
	 * @param page
	 * @param pageSize
	 * @param areaReportForm
	 * @return
	 */
	@GetMapping(value = "/areaAlarmReport", produces = GlobalContext.PRODUCES)
	public String areaAlarmReport() {
		List<AreaReportVo> list = iAreaReportFactory.getAreaAlarmReport();
		return resp(list);
	}

}
