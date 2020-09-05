package com.dotop.smartwater.project.server.water.rest.service.revenue;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.*;

import com.dotop.smartwater.dependence.core.common.BaseController;
import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.VerificationUtils;
import com.dotop.smartwater.project.module.api.revenue.ITradeOrderFactory;
import com.dotop.smartwater.project.module.core.water.bo.TradeDetailBo;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.module.core.water.constants.WaterConstants;
import com.dotop.smartwater.project.module.core.water.form.TradeDetailForm;
import com.dotop.smartwater.project.module.core.water.form.TradeOrderForm;
import com.dotop.smartwater.project.module.core.water.utils.IpAdrressUtil;
import com.dotop.smartwater.project.module.core.water.vo.TradeDetailVo;
import com.dotop.smartwater.project.module.core.water.vo.TradeOrderVo;

/**
 * 费用管理
 * 

 * @date 2019年3月11日
 *
 */
@RestController

@RequestMapping("/oPay")
public class TradeOrderController implements BaseController<TradeOrderForm> {

	private static final Logger LOGGER = LogManager.getLogger(TradeOrderController.class);

	@Resource
	private ITradeOrderFactory factory;

	private static final String AMOUNT = "amount";

	// 费用管理分页查询
	@Override
	@PostMapping(value = "/page", produces = GlobalContext.PRODUCES)
	public String page(@RequestBody TradeOrderForm form) {
		LOGGER.info(LogMsg.to("msg:", " 费用管理分页查询", "form", form));
		Pagination<TradeOrderVo> pagination = factory.page(form);
		LOGGER.info(LogMsg.to("msg:", " 费用管理分页查询结束", "form", form));
		return resp(ResultCode.Success, ResultCode.SUCCESS, pagination);
	}

	// 新增费用信息
	@Override
	@PostMapping(value = "/add", produces = GlobalContext.PRODUCES)
	public String add(@RequestBody TradeOrderForm form) {
		LOGGER.info(LogMsg.to("msg:", " 新增费用信息开始", "form", form));

		VerificationUtils.string("tradeName", form.getTradeName());
		VerificationUtils.string("userName", form.getUserName());
		VerificationUtils.string("userPhone", form.getUserPhone());
		VerificationUtils.string(AMOUNT, form.getAmount());
		VerificationUtils.string("type", form.getType());

		factory.save(form);
		LOGGER.info(LogMsg.to("msg:", " 新增费用信息结束", "form", form));
		return resp(ResultCode.Success, ResultCode.SUCCESS, null);
	}

	// 获取账单信息
	@Override
	@PostMapping(value = "/get", produces = GlobalContext.PRODUCES)
	public String get(@RequestBody TradeOrderForm form) {
		LOGGER.info(LogMsg.to("msg:", " 获取费用信息开始", "form", form));
		VerificationUtils.string("number", form.getNumber());
		TradeOrderVo vo = factory.get(form);
		LOGGER.info(LogMsg.to("msg:", " 获取费用信息结束", "form", form));
		return resp(ResultCode.Success, ResultCode.SUCCESS, vo);
	}

	// 获取账单信息及详情
	@PostMapping(value = "/detail", produces = GlobalContext.PRODUCES)
	public String detail(@RequestBody TradeOrderForm form) {
		LOGGER.info(LogMsg.to("msg:", " 获取费用信息开始", "form", form));
		VerificationUtils.string("number", form.getNumber());
		Map<String, Object> data = new HashMap<>();
		TradeOrderVo vo = factory.get(form);
		if (vo != null) {
			TradeDetailForm tform = new TradeDetailForm();
			tform.setNumber(form.getNumber());
			TradeDetailVo detail = factory.getDetail(tform);
			data.put("detail", detail);
		}
		data.put("order", vo);
		LOGGER.info(LogMsg.to("msg:", " 获取费用信息结束", "form", form));
		return resp(ResultCode.Success, ResultCode.SUCCESS, data);
	}

	// 修改费用信息
	@Override
	@PostMapping(value = "/edit", produces = GlobalContext.PRODUCES)
	public String edit(@RequestBody TradeOrderForm form) {
		LOGGER.info(LogMsg.to("msg:", " 修改费用信息开始", "form", form));

		VerificationUtils.string("tradeName", form.getTradeName());
		VerificationUtils.string("userName", form.getUserName());
		VerificationUtils.string("userPhone", form.getUserPhone());
		VerificationUtils.string(AMOUNT, form.getAmount());
		VerificationUtils.string("type", form.getType());

		factory.update(form);
		LOGGER.info(LogMsg.to("msg:", " 修改费用信息结束", "form", form));
		return resp(ResultCode.Success, ResultCode.SUCCESS, null);
	}

	// 更新交易状态
	@PostMapping(value = "/updatePayStatus", produces = GlobalContext.PRODUCES)
	public String updatePayStatus(@RequestBody TradeOrderForm form) {
		LOGGER.info(LogMsg.to("msg:", " 更新交易状态开始", "form", form));
		VerificationUtils.string("number", form.getNumber());
		factory.updatePayStatus(form);
		LOGGER.info(LogMsg.to("msg:", "更新交易状态结束", "form", form));
		return resp(ResultCode.Success, ResultCode.SUCCESS, null);
	}

	// 缴费
	@PostMapping(value = "/tradeOrderPay", produces = GlobalContext.PRODUCES)
	public String tradeOrderPay(@RequestBody TradeDetailForm form, HttpServletRequest request) {
		LOGGER.info(LogMsg.to("msg:", " 缴费", "form", form));
		VerificationUtils.string("mode", form.getMode());
		VerificationUtils.string("trade_number", form.getTradeNumber());
		VerificationUtils.string("number", form.getNumber());
		VerificationUtils.string(AMOUNT, form.getAmount());
		form.setIp(IpAdrressUtil.getIpAdrress(request));

		if (Integer.parseInt(form.getMode()) == WaterConstants.ORDER_PAYTYPE_MONEY) {
			VerificationUtils.string("netReceipts", form.getNetReceipts());
			VerificationUtils.string("giveChange", form.getGiveChange());
		} else if (Integer.parseInt(form.getMode()) == WaterConstants.ORDER_PAYTYPE_PAYCARD) {
			VerificationUtils.string("paycard", form.getPaycard());
		}

		TradeDetailBo detail = factory.tradeOrderPay(form);
		LOGGER.info(LogMsg.to("msg:", " 缴费结束", "form", form));
		return resp(ResultCode.Success, ResultCode.SUCCESS, detail);
	}
}
