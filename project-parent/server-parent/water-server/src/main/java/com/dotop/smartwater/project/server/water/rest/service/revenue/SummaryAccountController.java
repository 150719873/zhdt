package com.dotop.smartwater.project.server.water.rest.service.revenue;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.dotop.smartwater.dependence.core.common.BaseController;
import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.utils.VerificationUtils;
import com.dotop.smartwater.project.module.api.revenue.ISummaryAccountFactory;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.module.core.water.form.customize.SummaryForm;
import com.dotop.smartwater.project.module.core.water.vo.AccountingVo;
import com.dotop.smartwater.project.module.core.water.vo.customize.SummaryVo;

/**
 * 收银核算 -- 汇总核算
 * 

 * @date 2019年2月25日
 */

@RestController

@RequestMapping("/accounting")
public class SummaryAccountController implements BaseController<SummaryForm> {

	private static final Logger LOGGER = LogManager.getLogger(SummaryAccountController.class);

	@Autowired
	private ISummaryAccountFactory iSummaryAccountFactory;

	private static final String MONTH = "month";

	/**
	 * 汇总当年水司所有收银核算
	 */
	@PostMapping(value = "/summaryYear", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
	public String summaryYear(@RequestBody SummaryForm summaryForm) {
		LOGGER.info(LogMsg.to("msg:", "汇总当年水司所有收银核算开始", summaryForm));
		String year = summaryForm.getYear();
		VerificationUtils.string("year", year);
		SummaryVo summaryVo = iSummaryAccountFactory.summaryYear(summaryForm);
		LOGGER.info(LogMsg.to("msg:", "汇总当年水司所有收银核算结束", summaryVo));
		return resp(ResultCode.Success, ResultCode.SUCCESS, summaryVo);
	}

	/**
	 * 汇总当月水司所有收银核算
	 */
	@PostMapping(value = "/summaryMonth", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
	public String summaryMonth(@RequestBody SummaryForm summaryForm) {
		LOGGER.info(LogMsg.to("msg:", "汇总当月水司所有收银核算开始", summaryForm));
		String year = summaryForm.getYear();
		String month = summaryForm.getMonth();
		VerificationUtils.string("year", year);
		VerificationUtils.string(MONTH, month);
		SummaryVo summaryVo = iSummaryAccountFactory.summaryMonth(summaryForm);
		LOGGER.info(LogMsg.to("msg:", "汇总当月水司所有收银核算结束", "summaryVo", summaryVo));
		return resp(ResultCode.Success, ResultCode.SUCCESS, summaryVo);
	}

	/**
	 * 汇总当月所有收营员核算明细
	 */
	@PostMapping(value = "/summaryMonthDetail", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
	public String summaryMonthDetail(@RequestBody SummaryForm summaryForm) {
		LOGGER.info(LogMsg.to("msg:", "汇总当月所有收营员核算明细开始", summaryForm));
		String year = summaryForm.getYear();
		String month = summaryForm.getMonth();
		VerificationUtils.string("year", year);
		VerificationUtils.string(MONTH, month);
		List<SummaryVo> summaryList = iSummaryAccountFactory.summaryMonthDetail(summaryForm);
		LOGGER.info(LogMsg.to("msg:", "汇总当月所有收营员核算明细结束", "summaryList", summaryList));
		return resp(ResultCode.Success, ResultCode.SUCCESS, summaryList);
	}

	/**
	 * 汇总当月某个收营员每天核算数据
	 */
	@PostMapping(value = "/summarySelfMonthDetail", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
	public String summarySelfMonthDetail(@RequestBody SummaryForm summaryForm) {
		LOGGER.info(LogMsg.to("msg:", "汇总当月某个收营员每天核算数据开始", summaryForm));
		String year = summaryForm.getYear();
		String month = summaryForm.getMonth();
		String userid = summaryForm.getUserid();
		VerificationUtils.string("year", year);
		VerificationUtils.string(MONTH, month);
		VerificationUtils.string("userid", userid);
		List<AccountingVo> summaryList = iSummaryAccountFactory.summarySelfMonthDetail(summaryForm);
		LOGGER.info(LogMsg.to("msg:", "汇总当月某个收营员每天核算数据结束", "summaryList", summaryList));
		return resp(ResultCode.Success, ResultCode.SUCCESS, summaryList);
	}

	/**
	 * 汇总当月某个收营员核算数据
	 * 
	 * @param summaryForm
	 * @return
	 */

	@PostMapping(value = "/summarySelf", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
	public String summarySelf(@RequestBody SummaryForm summaryForm) {
		LOGGER.info(LogMsg.to("msg:", "汇总当月某个收营员每天核算数据", summaryForm));
		String year = summaryForm.getYear();
		String month = summaryForm.getMonth();
		String userid = summaryForm.getUserid();
		VerificationUtils.string("year", year);
		VerificationUtils.string(MONTH, month);
		VerificationUtils.string("userid", userid);
		SummaryVo summaryVo = iSummaryAccountFactory.summarySelf(summaryForm);
		LOGGER.info(LogMsg.to("msg:", "汇总当月某个收营员每天核算数据", "summaryVo", summaryVo));
		return resp(ResultCode.Success, ResultCode.SUCCESS, summaryVo);
	}

}
