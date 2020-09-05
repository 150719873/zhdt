package com.dotop.smartwater.project.server.water.rest.service.revenue;

import java.math.BigDecimal;
import java.util.List;

import com.dotop.smartwater.project.server.water.rest.service.tool.DeviceParametersController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.dotop.smartwater.dependence.core.common.BaseController;
import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.VerificationUtils;
import com.dotop.smartwater.project.module.api.revenue.IAccountingFactory;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.module.core.water.form.AccountingForm;
import com.dotop.smartwater.project.module.core.water.form.MarkOrderForm;
import com.dotop.smartwater.project.module.core.water.vo.AccountingVo;
import com.dotop.smartwater.project.module.core.water.vo.CommunityVo;
import com.dotop.smartwater.project.module.core.water.vo.OrderVo;
import com.dotop.smartwater.project.server.water.rest.service.tool.DeviceParametersController;

/**
 * 收银核算-- 个人核算
 * 

 * @date 2019年2月25日
 */

@RestController

@RequestMapping("/accounting")
public class AccountingController implements BaseController<AccountingForm> {

	private static final Logger LOGGER = LogManager.getLogger(DeviceParametersController.class);

	@Autowired
	private IAccountingFactory iAccountingFactory;

	/**
	 * 收银个人分管区域查询
	 */
	@PostMapping(value = "/selfCommunity", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
	public String getCommunity(@RequestBody AccountingForm accountingForm) {
		LOGGER.info(LogMsg.to("msg:", "查询收银分管区域开始", "accountingForm", accountingForm));
		List<CommunityVo> list = iAccountingFactory.getCommunity();
		LOGGER.info(LogMsg.to("msg:", "查询收银分管区域结束", "list", list));
		return resp(ResultCode.Success, ResultCode.SUCCESS, list);
	}

	/**
	 * 收营员 查询每个区域下收费账单信息
	 */

	@Override
	@PostMapping(value = "/selfDayOrders", produces = GlobalContext.PRODUCES)
	public String page(@RequestBody AccountingForm accountingForm) {
		LOGGER.info(LogMsg.to("msg:", " 收营员 查询每个区域下收费账单信-分页查询开始", "deviceForm", accountingForm));
		Integer page = accountingForm.getPage();
		Integer pageCount = accountingForm.getPageCount();
		// 验证
		VerificationUtils.integer("page", page);
		VerificationUtils.integer("pageCount", pageCount);
		Pagination<OrderVo> pagination = iAccountingFactory.getPage(accountingForm);
		LOGGER.info(LogMsg.to("msg:", "收营员 查询每个区域下收费账单信-分页查询查询结束"));
		return resp(ResultCode.Success, ResultCode.SUCCESS, pagination);
	}

	/**
	 * 收银员某天核算信息
	 * 
	 * @param accountingForm
	 * @return
	 */
	@PostMapping(value = "/selfDayAccounting", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
	public String userDayMoney(@RequestBody AccountingForm accountingForm) {
		LOGGER.info(LogMsg.to("msg:", "查询 收银员某天核算信息功能开始", "accountingForm", accountingForm));
		AccountingVo accountingVo = iAccountingFactory.userDayMoney(accountingForm);
		LOGGER.info(LogMsg.to("msg:", "查询 收银员某天核算信息功能结束", "accountingVo", accountingVo));
		return resp(ResultCode.Success, ResultCode.SUCCESS, accountingVo);
	}

	/**
	 * 收银员保存个人收银数据
	 */
	@Override
	@PostMapping(value = "/saveAccounting", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
	public String add(@RequestBody AccountingForm accountingForm) {
		LOGGER.info(LogMsg.to("msg:", "保存个人核算数据功能开始", "accountingForm", accountingForm));
		// 批次号
		Double sys = accountingForm.getSys();
		Double artificial = accountingForm.getArtificial();
		// 校验
		VerificationUtils.bigDecimal("sys", new BigDecimal(Double.toString(sys)));
		VerificationUtils.bigDecimal("artificial", new BigDecimal(Double.toString(artificial)));
		AccountingVo accountingVo = iAccountingFactory.add(accountingForm);
		LOGGER.info(LogMsg.to("msg:", "保存个人核算数据功能结束", "accountingVo", accountingVo));
		return resp(ResultCode.Success, ResultCode.SUCCESS, accountingVo);
	}

	/**
	 * 标记账单异常
	 */
	@PostMapping(value = "/markorder", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
	public String markorder(@RequestBody MarkOrderForm markOrderForm) {
		LOGGER.info(LogMsg.to("msg:", "标记账单异常开始", "markOrderForm", markOrderForm));
		iAccountingFactory.markorder(markOrderForm);
		LOGGER.info(LogMsg.to("msg:", "标记账单异常结束"));
		return resp(ResultCode.Success, ResultCode.SUCCESS, null);
	}

}
