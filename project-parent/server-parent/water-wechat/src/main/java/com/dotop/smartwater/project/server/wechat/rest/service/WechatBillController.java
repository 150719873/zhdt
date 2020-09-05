package com.dotop.smartwater.project.server.wechat.rest.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.dotop.smartwater.project.module.core.water.constants.PaymentConstants;
import com.dotop.smartwater.project.module.core.water.vo.PaymentTradeOrderVo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.dotop.smartwater.dependence.core.common.BaseController;
import com.dotop.smartwater.dependence.core.common.BaseForm;
import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.VerificationUtils;
import com.dotop.smartwater.project.module.api.revenue.IOrderFactory;
import com.dotop.smartwater.project.module.api.wechat.IWechatBillFactory;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.module.core.water.form.OrderForm;
import com.dotop.smartwater.project.module.core.water.form.customize.WechatParamForm;
import com.dotop.smartwater.project.module.core.water.vo.OrderExtVo;

/**
 * 账单
 * 

 * @date 2019年3月22日
 */
@RestController()

@RequestMapping("/Wechat")
public class WechatBillController implements BaseController<BaseForm> {

	private static final Logger logger = LogManager.getLogger(WechatBillController.class);

	@Autowired
	private IWechatBillFactory iWechatBillFactory;

	@Autowired
	private IOrderFactory iOrderFactory;

	/**
	 * 未缴费的账单列表
	 * 
	 * @param request
	 * @param wechatParamForm
	 * @return
	 */
	@PostMapping(value = "/querybill", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
	public String billList(HttpServletRequest request, @RequestBody WechatParamForm wechatParamForm) {

		logger.info(LogMsg.to("msg:", "查询未缴费账单功能开始", "传入参数", wechatParamForm));
		Integer page = wechatParamForm.getPage();
		Integer pageCount = wechatParamForm.getPageCount();
		VerificationUtils.integer("page", page);
		VerificationUtils.integer("pageCount", pageCount);
		wechatParamForm.setStatus(PaymentConstants.PATMENT_STATUS_NOPAY);
		Pagination<PaymentTradeOrderVo> pagination = iWechatBillFactory.page(request, wechatParamForm);
		return resp(ResultCode.Success, ResultCode.SUCCESS, pagination);
	}

	/**
	 * 已缴费账单列表
	 * 
	 * @param request
	 * @param wechatParamForm
	 * @return
	 */
	@PostMapping(value = "/queryPaybill", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
	public String queryPaybill(HttpServletRequest request, @RequestBody WechatParamForm wechatParamForm) {
		logger.info(LogMsg.to("msg:", "查询已缴费账单功能开始", "wechatParamForm", wechatParamForm));
		Integer page = wechatParamForm.getPage();
		Integer pageCount = wechatParamForm.getPageCount();
		VerificationUtils.integer("page", page);
		VerificationUtils.integer("pageCount", pageCount);
		//Pagination<OrderVo> pagination = iWechatBillFactory.queryPaybill(request, wechatParamForm);

		wechatParamForm.setStatus(PaymentConstants.PATMENT_STATUS_PAYED);
		Pagination<PaymentTradeOrderVo> pagination = iWechatBillFactory.page(request, wechatParamForm);
		return resp(ResultCode.Success, ResultCode.SUCCESS, pagination);
	}

	/**
	 * 未缴费账单详情
	 * 
	 * @param request
	 * @param wechatParamForm
	 * @return
	 */
	@PostMapping(value = "/querypayDetail", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
	public String querypayDetail(HttpServletRequest request, @RequestBody WechatParamForm wechatParamForm) {
		logger.info(LogMsg.to("msg:", "未缴费账单详情功能开始", "wechatParamForm", wechatParamForm));
		String tradeno = wechatParamForm.getTradeno();
		String orderId = wechatParamForm.getOrderid();
		VerificationUtils.string("tradeno", tradeno);
		VerificationUtils.string("orderId", orderId);
		OrderForm orderForm = new OrderForm();
		orderForm.setTradeno(tradeno);
		orderForm.setId(orderId);
		// 调用水务的账单详情功能
		OrderExtVo orderExtVo = iOrderFactory.loadOrderExt(orderForm);
		logger.info(LogMsg.to("msg:", "未缴费账单详情功能结束"));
		return resp(ResultCode.Success, ResultCode.SUCCESS, orderExtVo);
	}

	/**
	 * 账单详情
	 * 
	 * @param request
	 * @param wechatParamForm
	 * @return
	 */
	@PostMapping(value = "/getbillDetail", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
	public String getbillDetail(HttpServletRequest request, @RequestBody WechatParamForm wechatParamForm) {
		logger.info(LogMsg.to("msg:", "获取账单的账单列表的详情开始"));
		String tradeno = wechatParamForm.getTradeno();
		VerificationUtils.string("tradeno", tradeno);
		Map<String, Object> map = iWechatBillFactory.getbillDetail(wechatParamForm.getTradeno());
		logger.info(LogMsg.to("msg:", "获取账单的账单列表的详情结束"));
		return resp(ResultCode.Success, ResultCode.SUCCESS, map);
	}
}
