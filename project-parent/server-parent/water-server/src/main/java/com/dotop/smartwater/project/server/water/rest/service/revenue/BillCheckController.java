package com.dotop.smartwater.project.server.water.rest.service.revenue;

import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.dotop.smartwater.dependence.core.common.BaseController;
import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.VerificationUtils;
import com.dotop.smartwater.project.module.api.revenue.IBillCheckFactory;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.module.core.water.form.BillCheckForm;
import com.dotop.smartwater.project.module.core.water.vo.BillCheckVo;

/**

 * @date 2019年3月11日
 */

@RestController

@RequestMapping("/billCheck")
public class BillCheckController implements BaseController<BillCheckForm> {

	private static final Logger LOGGER = LogManager.getLogger(BillCheckController.class);

	@Autowired
	private IBillCheckFactory iBillCheckFactory;

	/**
	 * 新增账单对账功能
	 */
	@Override
	@PostMapping(value = "/add", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
	public String add(@RequestBody BillCheckForm billCheckForm) {

		LOGGER.info(LogMsg.to("msg:", "新增账单对账动能开始", "billCheckForm", billCheckForm));
		String serialNumber = billCheckForm.getSerialNumber();
		Date startDate = billCheckForm.getStartDate();
		Date endDate = billCheckForm.getEndDate();
		String billTitle = billCheckForm.getBillTitle();
		VerificationUtils.string("serialNumber", serialNumber);
		VerificationUtils.date("startDate", startDate);
		VerificationUtils.date("endDate", endDate);
		VerificationUtils.string("billTitle", billTitle);
		BillCheckVo billCheckVo = iBillCheckFactory.add(billCheckForm);
		LOGGER.info(LogMsg.to("msg:", "新增账单对账动能结束", "billCheckForm", billCheckForm));
		return resp(ResultCode.Success, ResultCode.SUCCESS, billCheckVo);
	}

	/**
	 * 分页查询
	 */
	@Override
	@PostMapping(value = "/page", produces = GlobalContext.PRODUCES)
	public String page(@RequestBody BillCheckForm billCheckForm) {
		LOGGER.info(LogMsg.to("msg:", "账单对账分页查询开始", "deviceForm", billCheckForm));
		Integer page = billCheckForm.getPage();
		Integer pageCount = billCheckForm.getPageCount();
		// 验证
		VerificationUtils.integer("page", page);
		VerificationUtils.integer("pageCount", pageCount);
		Pagination<BillCheckVo> pagination = iBillCheckFactory.page(billCheckForm);
		LOGGER.info(LogMsg.to("msg:", "账单对账分页查询结束"));
		return resp(ResultCode.Success, "SUCCESS", pagination);
	}

	/**
	 * 删除记录
	 */
	@Override
	@PostMapping(value = "/del", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
	public String del(@RequestBody BillCheckForm billCheckForm) {
		LOGGER.info(LogMsg.to("msg:", "删除内容开始", "billCheckForm", billCheckForm));
		String billCheckId = billCheckForm.getBillCheckId();
		VerificationUtils.string("billCheckId", billCheckId);
		String id = iBillCheckFactory.del(billCheckForm);
		LOGGER.info(LogMsg.to("msg:", "删除内容结束", "id", id));
		return resp(ResultCode.Success, "SUCCESS", id);
	}

	/**
	 * 详情
	 */
	@Override
	@PostMapping(value = "/get", produces = GlobalContext.PRODUCES)
	public String get(@RequestBody BillCheckForm billCheckForm) {
		LOGGER.info(LogMsg.to("msg:", "查询详情开始", "billCheckForm", billCheckForm));
		String billCheckId = billCheckForm.getBillCheckId();
		VerificationUtils.string("billCheckId", billCheckId);
		BillCheckVo billCheckVo = iBillCheckFactory.get(billCheckForm);
		return resp(ResultCode.Success, "SUCCESS", billCheckVo);
	}

	/**
	 * 发起审核
	 */
	@PostMapping(value = "/sendCheck", produces = GlobalContext.PRODUCES)
	public String sendCheck(@RequestBody BillCheckForm billCheckForm) {
		LOGGER.info(LogMsg.to("msg:", "发起审核开始", "billCheckForm", billCheckForm));
		String billCheckId = billCheckForm.getBillCheckId();
		VerificationUtils.string("billCheckId", billCheckId);
		BillCheckVo billCheckVo = iBillCheckFactory.sendCheck(billCheckForm);
		LOGGER.info(LogMsg.to("msg:", "发起审核结束", "billCheckForm", billCheckForm));
		return resp(ResultCode.Success, "SUCCESS", billCheckVo);
	}

	/**
	 * 强制结束工单审核
	 */
	@PostMapping(value = "/closeCheck", produces = GlobalContext.PRODUCES)
	public String closeCheck(@RequestBody BillCheckForm billCheckForm) {
		LOGGER.info(LogMsg.to("msg:", "结束工单审核开始", "billCheckForm", billCheckForm));
		String billCheckId = billCheckForm.getBillCheckId();
		String processId = billCheckForm.getProcessId();
		VerificationUtils.string("processId", processId);
		VerificationUtils.string("billCheckId", billCheckId);
		BillCheckVo billCheckVo = iBillCheckFactory.closeCheck(billCheckForm);
		LOGGER.info(LogMsg.to("msg:", "结束工单审核结束", "billCheckForm", billCheckForm));
		return resp(ResultCode.Success, "SUCCESS", billCheckVo);
	}

	/**
	 * 更新工单状态 回调接口
	 */
	@PostMapping(value = "/editStatus", produces = GlobalContext.PRODUCES)
	public String editProcessStatus(@RequestBody BillCheckForm billCheckForm) {
		LOGGER.info(LogMsg.to("msg:", "更新工单审核状态开始", "billCheckForm", billCheckForm));
		String billCheckId = billCheckForm.getBillCheckId();
		String processId = billCheckForm.getProcessId();
		String processStatus = billCheckForm.getProcessStatus();
		VerificationUtils.string("processStatus", processStatus);
		VerificationUtils.string("processId", processId);
		VerificationUtils.string("billCheckId", billCheckId);
		BillCheckVo billCheckVo = iBillCheckFactory.editStatus(billCheckForm);
		LOGGER.info(LogMsg.to("msg:", "更新工单审核状态结束", "billCheckForm", billCheckForm));
		return resp(ResultCode.Success, "SUCCESS", billCheckVo);
	}

}
