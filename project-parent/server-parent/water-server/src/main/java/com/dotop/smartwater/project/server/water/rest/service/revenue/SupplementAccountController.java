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
import com.dotop.smartwater.project.module.api.revenue.ISupplementAccountFactory;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.module.core.water.form.customize.SupplementForm;
import com.dotop.smartwater.project.module.core.water.vo.customize.SupplementVo;

/**
 * 收银核算 -- 平账记录
 * 

 * @date 2019年2月25日
 */

@RestController

@RequestMapping("/accounting")
public class SupplementAccountController implements BaseController<SupplementForm> {

	private static final Logger LOGGER = LogManager.getLogger(SupplementAccountController.class);

	@Autowired
	private ISupplementAccountFactory iSupplementAccountFactory;

	/**
	 * 平账--个人
	 */
	@PostMapping(value = "/supplementSelf", produces = GlobalContext.PRODUCES)
	public String supplementSelf(@RequestBody SupplementForm supplementForm) {
		LOGGER.info(LogMsg.to("msg:", "平账--个人开始", supplementForm));
		iSupplementAccountFactory.supplementSelf(supplementForm);
		return resp(ResultCode.Success, ResultCode.SUCCESS, null);
	}

	/**
	 * 一键平账
	 */
	@PostMapping(value = "/oneKeyBalance", produces = GlobalContext.PRODUCES)
	public String oneKeyBalance(@RequestBody SupplementForm supplementForm) {
		LOGGER.info(LogMsg.to("msg:", "一键平账开始", supplementForm));
		String year = supplementForm.getYear();
		String month = supplementForm.getMonth();
		VerificationUtils.string("year", year);
		VerificationUtils.string("month", month);
		iSupplementAccountFactory.oneKeyBalance(supplementForm);
		return resp(ResultCode.Success, ResultCode.SUCCESS, null);
	}

	/**
	 * 平账记录
	 */
	@PostMapping(value = "/supplementlist", produces = GlobalContext.PRODUCES)
	public String supplementlist(@RequestBody SupplementForm supplementForm) {
		LOGGER.info(LogMsg.to("msg:", "平账记录开始", supplementForm));
		Integer pageCount = supplementForm.getPageCount();
		Integer page = supplementForm.getPage();
		VerificationUtils.integer("page", page);
		VerificationUtils.integer("pageCount", pageCount);
		Pagination<SupplementVo> pagination = iSupplementAccountFactory.page(supplementForm);
		LOGGER.info(LogMsg.to("msg:", "平账记录结束", "pagination", pagination));
		return resp(ResultCode.Success, ResultCode.SUCCESS, pagination);
	}
}
