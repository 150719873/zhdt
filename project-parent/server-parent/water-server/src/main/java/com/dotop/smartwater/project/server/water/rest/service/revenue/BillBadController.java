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
import com.dotop.smartwater.project.module.api.revenue.IBillBadFactory;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.module.core.water.form.BillBadForm;
import com.dotop.smartwater.project.module.core.water.vo.BillBadVo;

/**

 * @date 2019年3月11日
 */

@RestController

@RequestMapping("/billBad")
public class BillBadController implements BaseController<BillBadForm> {

	private static final Logger LOGGER = LogManager.getLogger(BillBadController.class);

	@Autowired
	private IBillBadFactory iBillBadFactory;

	/**
	 * 分页查询
	 */
	@Override
	@PostMapping(value = "/check/page", produces = GlobalContext.PRODUCES)
	public String page(@RequestBody BillBadForm billBadForm) {
		LOGGER.info(LogMsg.to("msg:", "账单对账中未缴费账单分页分页查询开始", "deviceForm", billBadForm));
		Integer page = billBadForm.getPage();
		Integer pageCount = billBadForm.getPageCount();
		// 验证
		VerificationUtils.integer("page", page);
		VerificationUtils.integer("pageCount", pageCount);
		Pagination<BillBadVo> pagination = iBillBadFactory.page(billBadForm);
		LOGGER.info(LogMsg.to("msg:", "账单对账中未缴费账单分页分页查询结束"));
		return resp(ResultCode.Success, ResultCode.SUCCESS, pagination);
	}

	/**
	 * 分页查询
	 */
	@PostMapping(value = "/page", produces = GlobalContext.PRODUCES)
	public String getBillBadPage(@RequestBody BillBadForm billBadForm) {
		LOGGER.info(LogMsg.to("msg:", "账单坏账分页查询开始", "deviceForm", billBadForm));
		Integer page = billBadForm.getPage();
		Integer pageCount = billBadForm.getPageCount();
		// 验证
		VerificationUtils.integer("page", page);
		VerificationUtils.integer("pageCount", pageCount);
		Pagination<BillBadVo> pagination = iBillBadFactory.getBillBadPage(billBadForm);
		LOGGER.info(LogMsg.to("msg:", "账单坏账分页查询结束"));
		return resp(ResultCode.Success, ResultCode.SUCCESS, pagination);
	}

	@PostMapping(value = "/markBadBill", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
	public String markBadBill(@RequestBody BillBadForm billBadForm) {
		LOGGER.info(LogMsg.to("msg:", "标记为坏账开始", "billBadForm", billBadForm));
		String billBadId = billBadForm.getBillBadId();
		VerificationUtils.string("billBadId", billBadId);
		String id = iBillBadFactory.markBadBill(billBadForm);
		LOGGER.info(LogMsg.to("msg:", "标记为坏账结束", "id", id));
		return resp(ResultCode.Success, ResultCode.SUCCESS, id);
	}

}
