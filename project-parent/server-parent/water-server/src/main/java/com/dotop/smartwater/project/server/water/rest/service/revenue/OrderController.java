package com.dotop.smartwater.project.server.water.rest.service.revenue;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.dotop.smartwater.project.module.api.tool.IPrintBindFactory;
import com.dotop.smartwater.project.module.core.auth.enums.SmsEnum;
import com.dotop.smartwater.project.module.core.water.enums.OperateTypeEnum;
import com.dotop.smartwater.project.module.core.water.vo.*;
import com.dotop.smartwater.project.server.water.common.FoundationController;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.dotop.smartwater.dependence.cache.StringValueCache;
import com.dotop.smartwater.dependence.core.common.BaseController;
import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.VerificationUtils;
import com.dotop.smartwater.project.module.api.revenue.IOrderFactory;
import com.dotop.smartwater.project.module.core.auth.local.AuthCasClient;
import com.dotop.smartwater.project.module.core.auth.vo.UserVo;
import com.dotop.smartwater.project.module.core.third.form.iot.MeterDataForm;
import com.dotop.smartwater.project.module.core.water.constants.OrderGenerateType;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.module.core.water.form.OrderExtForm;
import com.dotop.smartwater.project.module.core.water.form.OrderForm;
import com.dotop.smartwater.project.module.core.water.form.OrderPreviewForm;
import com.dotop.smartwater.project.module.core.water.form.OwnerForm;
import com.dotop.smartwater.project.module.core.water.form.customize.PreviewForm;
import com.dotop.smartwater.project.module.core.water.utils.IpAdrressUtil;

/**
 * @program: project-parent
 * @description: OrderController

 * @create: 2019-02-26 14:28
 **/
@RestController

@RequestMapping("/order")
public class OrderController extends FoundationController implements BaseController<OrderForm> {

	@Autowired
	private IOrderFactory iOrderFactory;

	@Autowired
	private IPrintBindFactory iPrintBindFactory;

	@Resource
	private StringValueCache svc;

	@Override
	@PostMapping(value = "/get", produces = GlobalContext.PRODUCES)
	public String get(@RequestBody OrderForm orderForm) {

		String tradeNo = orderForm.getTradeno();
		VerificationUtils.string("tradeNo", tradeNo);

		// 数据封装
		OrderVo orderVo = iOrderFactory.get(orderForm);
		return resp(ResultCode.Success, ResultCode.SUCCESS, orderVo);

	}

	/**
	 * 手动抄表界面的获取用户信息
	 *
	 * @param ownerForm
	 */
	@PostMapping(value = "/get_owneruser", produces = GlobalContext.PRODUCES)
	public String getOwnerUser(@RequestBody OwnerForm ownerForm) {

		String userNo = ownerForm.getUserno();
		VerificationUtils.string("userNo", userNo);

		// 数据封装
		OwnerVo ownerVo = iOrderFactory.getOwnerUser(ownerForm);
		return resp(ResultCode.Success, ResultCode.SUCCESS, ownerVo);

	}

	/**
	 * 手动抄表
	 *
	 * @param meterDataForm
	 */
	@PostMapping(value = "/manualMeter", produces = GlobalContext.PRODUCES)
	public String manualMeter(@RequestBody MeterDataForm meterDataForm) {
		if (StringUtils.isBlank(meterDataForm.getDeveui())) {
			return resp(ResultCode.ParamIllegal, "设备EUI不能为空", null);
		}
		if (StringUtils.isBlank(meterDataForm.getMeter())) {
			return resp(ResultCode.ParamIllegal, "水表读数不能为空", null);
		}

		iOrderFactory.manualMeter(meterDataForm);
		auditLog(OperateTypeEnum.ARTIFICIAL_METER_READING,"抄表","设备EUI",meterDataForm.getDeveui(),"水表读数",meterDataForm.getMeter());
		return resp(ResultCode.Success, ResultCode.SUCCESS, null);

	}

	/**
	 * 获取预览账单
	 *
	 * @param previewForm
	 * @return
	 */
	@PostMapping(value = "/preview", produces = GlobalContext.PRODUCES)
	public String preview(@RequestBody PreviewForm previewForm) {
		if (StringUtils.isBlank(previewForm.getType())) {
			return resp(ResultCode.NO_SET_GENERATE_METHOD, ResultCode.getMessage(ResultCode.NO_SET_GENERATE_METHOD),
					null);
		} else if (previewForm.getType().equals(OrderGenerateType.COMMUNITY)
				&& StringUtils.isBlank(previewForm.getCommunityIds())) {
			return resp(ResultCode.NO_SET_COMMUNITY, ResultCode.getMessage(ResultCode.NO_SET_COMMUNITY), null);
		} else if (previewForm.getType().equals(OrderGenerateType.OWNER)
				&& StringUtils.isBlank(previewForm.getUsernos())) {
			return resp(ResultCode.NO_SET_OWNER_NO, ResultCode.getMessage(ResultCode.NO_SET_OWNER_NO), null);
		} else if (StringUtils.isBlank(previewForm.getTradeStatus())) {
			return resp(ResultCode.NO_SET_ORDER_STATUS, ResultCode.getMessage(ResultCode.NO_SET_ORDER_STATUS), null);
		}

		Pagination<OrderPreviewVo> pagination = iOrderFactory.OrderPreviewList(previewForm);

		return resp(ResultCode.Success, ResultCode.SUCCESS, pagination);

	}

	/**
	 * 生成预览账单 验证必填参数 获取业主和水表信息 获取当前账单月份上一期账单信息 获取所以收费种类信息
	 * 检测生成业主账单预览数据是否在预览中已存在，只生成不存在的业主 生成账单 验证业主是否开户 验证水表信息是否存在 验证业主是否换表、过户、销户
	 * 验证当前业主是否设置了收费种类 获取当前业主水表最近上报的数据 验证最近上报数据的时间和抄表截止时间间隔是否大于设定间隔时间
	 * 验证本期抄表时间和上期抄表时间相减，是否大于上期抄表
	 *
	 * @param previewForm
	 * @return
	 */
	@PostMapping(value = "/generateOrder", produces = GlobalContext.PRODUCES)
	public String generateOrder(@RequestBody PreviewForm previewForm) {

		if (StringUtils.isBlank(previewForm.getMetertime())) {
			return resp(ResultCode.NO_SET_END_TIME, ResultCode.getMessage(ResultCode.NO_SET_END_TIME), null);
		}

		if (previewForm.getIntervalday() == null) {
			return resp(ResultCode.NO_SET_TIME_INTERVAL, ResultCode.getMessage(ResultCode.NO_SET_TIME_INTERVAL), null);
		}

		if (StringUtils.isBlank(previewForm.getMonth())) {
			return resp(ResultCode.NO_SET_MONTH_ORDER, ResultCode.getMessage(ResultCode.NO_SET_MONTH_ORDER), null);
		}
		if (previewForm.getMonth().length() != OrderGenerateType.DATE_STRING_LENGTH) {
			return resp(ResultCode.MONTH_ORDER_FORMAT_ERROR, ResultCode.getMessage(ResultCode.MONTH_ORDER_FORMAT_ERROR),
					null);
		}

		if (StringUtils.isBlank(previewForm.getType())) {
			return resp(ResultCode.NO_SET_GENERATE_METHOD, ResultCode.getMessage(ResultCode.NO_SET_GENERATE_METHOD),
					null);
		}

		if (previewForm.getType().equals(OrderGenerateType.COMMUNITY)
				&& StringUtils.isBlank(previewForm.getCommunityIds())) {
			return resp(ResultCode.NO_SET_COMMUNITY, ResultCode.getMessage(ResultCode.NO_SET_COMMUNITY), null);
		}

		if (previewForm.getType().equals(OrderGenerateType.OWNER) && StringUtils.isBlank(previewForm.getUsernos())) {
			return resp(ResultCode.NO_SET_OWNER_NO, ResultCode.getMessage(ResultCode.NO_SET_OWNER_NO), null);
		}

		UserVo userVo = AuthCasClient.getUser();
		String flag = svc.get(OrderGenerateType.GENERATE_ORDER_SWITCH + userVo.getEnterpriseid());
		if (StringUtils.isNotBlank(flag)) {
			return resp(ResultCode.GENERATE_ORDER_LATER_ON_SEARCH,
					ResultCode.getMessage(ResultCode.GENERATE_ORDER_LATER_ON_SEARCH), null);
		}

		iOrderFactory.generateOrder(previewForm);

		return resp(ResultCode.Success, ResultCode.SUCCESS, null);

	}

	@PostMapping(value = "/deletePreview", produces = GlobalContext.PRODUCES)
	public String deletePreview(@RequestBody OwnerForm ownerForm) {

		if (StringUtils.isBlank(ownerForm.getOwnerid())) {
			return resp(ResultCode.OWNER_ID_IS_NULL, ResultCode.getMessage(ResultCode.OWNER_ID_IS_NULL), null);
		}

		iOrderFactory.deletePreview(ownerForm);
		return resp(ResultCode.Success, ResultCode.SUCCESS, null);

	}

	@PostMapping(value = "/clearPreview", produces = GlobalContext.PRODUCES)
	public String clearPreview(@RequestBody PreviewForm previewForm) {
		if (StringUtils.isBlank(previewForm.getType())) {
			return resp(ResultCode.NO_SET_GENERATE_METHOD, ResultCode.getMessage(ResultCode.NO_SET_GENERATE_METHOD),
					null);
		}

		if (previewForm.getType().equals(OrderGenerateType.COMMUNITY)
				&& StringUtils.isBlank(previewForm.getCommunityIds())) {
			return resp(ResultCode.NO_SET_COMMUNITY, ResultCode.getMessage(ResultCode.NO_SET_COMMUNITY), null);
		}

		if (previewForm.getType().equals(OrderGenerateType.OWNER) && StringUtils.isBlank(previewForm.getUsernos())) {
			return resp(ResultCode.NO_SET_OWNER_NO, ResultCode.getMessage(ResultCode.NO_SET_OWNER_NO), null);
		}
		if (StringUtils.isBlank(previewForm.getTradeStatus())) {
			return resp(ResultCode.NO_CHECK_ORDER, ResultCode.getMessage(ResultCode.NO_CHECK_ORDER), null);
		}

		iOrderFactory.clearPreview(previewForm, true);
		return resp(ResultCode.Success, ResultCode.SUCCESS, null);

	}

	@PostMapping(value = "/makeOrder", produces = GlobalContext.PRODUCES)
	public String makeOrder(@RequestBody PreviewForm previewForm) {
		if (StringUtils.isBlank(previewForm.getType())) {
			return resp(ResultCode.NO_SET_GENERATE_METHOD, ResultCode.getMessage(ResultCode.NO_SET_GENERATE_METHOD),
					null);
		}

		if (previewForm.getType().equals(OrderGenerateType.COMMUNITY)
				&& StringUtils.isBlank(previewForm.getCommunityIds())) {
			return resp(ResultCode.NO_SET_COMMUNITY, ResultCode.getMessage(ResultCode.NO_SET_COMMUNITY), null);
		}

		if (previewForm.getType().equals(OrderGenerateType.OWNER) && StringUtils.isBlank(previewForm.getUsernos())) {
			return resp(ResultCode.NO_SET_OWNER_NO, ResultCode.getMessage(ResultCode.NO_SET_OWNER_NO), null);
		}
		if (StringUtils.isBlank(previewForm.getTradeStatus())) {
			return resp(ResultCode.NO_CHECK_ORDER, ResultCode.getMessage(ResultCode.NO_CHECK_ORDER), null);
		}

		iOrderFactory.makeOrder(previewForm);
		return resp(ResultCode.Success, ResultCode.SUCCESS, null);

	}

	@PostMapping(value = "/bills", produces = GlobalContext.PRODUCES)
	public String bills(@RequestBody OrderPreviewForm orderPreviewForm) {
		Pagination<OrderVo> pagination = iOrderFactory.bills(orderPreviewForm);
		return resp(ResultCode.Success, ResultCode.SUCCESS, pagination);
	}

	@PostMapping(value = "/OrderList", produces = GlobalContext.PRODUCES)
	public String orderList(@RequestBody OrderPreviewForm orderPreviewForm) {
		if (StringUtils.isBlank(orderPreviewForm.getOwnerid()) && StringUtils.isBlank(orderPreviewForm.getUserno())
				&& StringUtils.isBlank(orderPreviewForm.getUsername())
				&& StringUtils.isBlank(orderPreviewForm.getPhone()) && StringUtils.isBlank(orderPreviewForm.getCardid())
				&& StringUtils.isBlank(orderPreviewForm.getDevno()) && orderPreviewForm.getPaystatus() == null
				&& StringUtils.isBlank(orderPreviewForm.getCids())) {
			return resp(ResultCode.NO_SET_COMMUNITY, ResultCode.getMessage(ResultCode.NO_SET_COMMUNITY), null);
		}
		Pagination<OrderVo> pagination = iOrderFactory.orderList(orderPreviewForm);
		return resp(ResultCode.Success, ResultCode.SUCCESS, pagination);
	}

	/**
	 * 撤销没缴费的账单
	 */
	@PostMapping(value = "/deleteOrder", produces = GlobalContext.PRODUCES)
	public String deleteOrder(@RequestBody OrderForm orderForm) {
		String orderId = orderForm.getId();
		VerificationUtils.string("orderId", orderId);

		iOrderFactory.deleteOrder(orderForm);
		return resp(ResultCode.Success, ResultCode.SUCCESS, null);
	}

	/**
	 * 确认账单为正常
	 */
	@PostMapping(value = "/signNormal", produces = GlobalContext.PRODUCES)
	public String signNormal(@RequestBody OrderForm orderForm) {
		String ownerId = orderForm.getOwnerid();
		VerificationUtils.string("ownerId", ownerId);

		iOrderFactory.signNormal(orderForm);
		return resp(ResultCode.Success, ResultCode.SUCCESS, null);
	}

	/**
	 * 获取缴费账单的收费种类明细
	 */
	@PostMapping(value = "/findOrderPayTypeDetail", produces = GlobalContext.PRODUCES)
	public String findOrderPayTypeDetail(@RequestBody OrderForm orderForm) {
		String tradeNo = orderForm.getTradeno();
		if (StringUtils.isBlank(tradeNo)) {
			return resp(ResultCode.ORDER_NUMBER_IS_NULL, ResultCode.getMessage(ResultCode.ORDER_NUMBER_IS_NULL), null);
		}

		PayTypeVo payType = iOrderFactory.findOrderPayTypeDetail(orderForm);
		return resp(ResultCode.Success, ResultCode.SUCCESS, payType);
	}

	/**
	 * 加载没缴费账单的额外信息
	 */
	@PostMapping(value = "/loadOrderExt", produces = GlobalContext.PRODUCES)
	public String loadOrderExt(@RequestBody OrderForm orderForm) {
		if (StringUtils.isBlank(orderForm.getId())) {
			return resp(ResultCode.ID_IS_NULL, ResultCode.getMessage(ResultCode.ID_IS_NULL), null);
		}
		if (StringUtils.isBlank(orderForm.getOwnerid())) {
			return resp(ResultCode.OWNER_ID_IS_NULL, ResultCode.getMessage(ResultCode.OWNER_ID_IS_NULL), null);
		}
		OrderExtVo orderExtVo = iOrderFactory.loadOrderExt(orderForm);
		return resp(ResultCode.Success, ResultCode.SUCCESS, orderExtVo);
	}

	/**
	 * 更新交易状态（微信支付）
	 */
	@PostMapping(value = "/updatePayStatus", produces = GlobalContext.PRODUCES)
	public String updatePayStatus(@RequestBody OrderExtForm orderExtForm) {

		if (StringUtils.isBlank(orderExtForm.getTradeno())) {
			return resp(ResultCode.ORDER_NUMBER_IS_NULL, ResultCode.getMessage(ResultCode.ORDER_NUMBER_IS_NULL), null);
		}

		iOrderFactory.updatePayStatus(orderExtForm);

		return resp(ResultCode.Success, ResultCode.SUCCESS, null);
	}

	/**
	 * 撤销账单交易信息（包含交易信息）
	 * <p>
	 * 1、删除余额抵扣记录信息，并将抵扣金额补给业主 2、修改代金券为未使用状态 3、删除交易记录信息
	 * 4、清空订单中代金券、余额、已计算出的金额信息（账单金额应该加上代金券、余额抵扣金额）
	 *
	 * @return
	 */
	@PostMapping(value = "/revokePay", produces = GlobalContext.PRODUCES)
	public String revokePay(@RequestBody OrderExtForm orderExtForm) {
		if (StringUtils.isBlank(orderExtForm.getTradeno())) {
			return resp(ResultCode.ORDER_NUMBER_IS_NULL, ResultCode.getMessage(ResultCode.ORDER_NUMBER_IS_NULL), null);
		} else {
			iOrderFactory.updatePayStatus(orderExtForm);
			return resp(ResultCode.Success, ResultCode.SUCCESS, null);
		}
	}

	/**
	 * 支付账单
	 */
	@PostMapping(value = "/orderPay", produces = GlobalContext.PRODUCES)
	public String orderPay(@RequestBody OrderExtForm orderExtForm, HttpServletRequest request) {

		if (orderExtForm.getBalance() == null) {
			return resp(ResultCode.PARAMS_IS_NULL, ResultCode.getMessage(ResultCode.PARAMS_IS_NULL), null);
		} else {
			if (orderExtForm.getAlreadypay() == null || orderExtForm.getRealamount() == null
					|| orderExtForm.getOwnerpay() == null) {
				return resp(ResultCode.PARAMS_IS_NULL, ResultCode.getMessage(ResultCode.PARAMS_IS_NULL), null);
			}

			orderExtForm.setIp(IpAdrressUtil.getIpAdrress(request));
			Map<String, Object> map = iOrderFactory.orderPay(orderExtForm);
			return resp(ResultCode.Success, ResultCode.SUCCESS, map);
		}
	}

	/**
	 * 补打发票
	 */
	@PostMapping(value = "/supplementprint", produces = GlobalContext.PRODUCES)
	public String supplementPrint(@RequestBody OrderExtForm orderExtForm) {

		UserVo user = AuthCasClient.getUser();
		PrintBindVo printBindVo = iPrintBindFactory.getPrintStatus(user.getEnterpriseid(),SmsEnum.pay_fee.intValue());

		Map<String, Object> map = iOrderFactory.supplementPrint(orderExtForm,printBindVo);

		return resp(ResultCode.Success, ResultCode.SUCCESS, map);
	}

	/**
	 * 抄表并生成账单
	 */
	@PostMapping(value = "/readMeterAndBuildOrder", produces = GlobalContext.PRODUCES)
	public String readMeterAndBuildOrder(@RequestBody MeterDataForm meterDataForm) {

		if (StringUtils.isBlank(meterDataForm.getDeveui())) {
			return resp(ResultCode.DEVICE_EUI_IS_NULL, ResultCode.getMessage(ResultCode.DEVICE_EUI_IS_NULL), null);
		}
		if (StringUtils.isBlank(meterDataForm.getMeter())) {
			return resp(ResultCode.DEVICE_WATER_IS_NULL, ResultCode.getMessage(ResultCode.DEVICE_WATER_IS_NULL), null);
		}

		iOrderFactory.readMeterAndBuildOrder(meterDataForm);
		auditLog(OperateTypeEnum.ARTIFICIAL_METER_READING,"抄表并生成账单","设备EUI",meterDataForm.getDeveui(),"水表读数",meterDataForm.getMeter());
		return resp(ResultCode.Success, ResultCode.SUCCESS, null);
	}

}
