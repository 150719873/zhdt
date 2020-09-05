package com.dotop.smartwater.project.module.service.revenue.impl;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.BeanUtils;
import com.dotop.smartwater.dependence.core.utils.DateUtils;
import com.dotop.smartwater.dependence.core.utils.JSONUtils;
import com.dotop.smartwater.dependence.core.utils.UuidUtils;
import com.dotop.smartwater.project.module.client.third.http.wechat.CaptchaUtil;
import com.dotop.smartwater.project.module.client.third.http.wechat.WechatHttpClientUtils;
import com.dotop.smartwater.project.module.client.third.http.wechat.WechatUtil;
import com.dotop.smartwater.project.module.core.auth.local.AuthCasClient;
import com.dotop.smartwater.project.module.core.auth.vo.UserVo;
import com.dotop.smartwater.project.module.core.third.utils.wechat.MessageManager;
import com.dotop.smartwater.project.module.core.water.bo.CompriseBo;
import com.dotop.smartwater.project.module.core.water.bo.OrderBo;
import com.dotop.smartwater.project.module.core.water.bo.OrderExtBo;
import com.dotop.smartwater.project.module.core.water.bo.OrderPreviewBo;
import com.dotop.smartwater.project.module.core.water.bo.TradePayBo;
import com.dotop.smartwater.project.module.core.water.bo.WechatPublicSettingBo;
import com.dotop.smartwater.project.module.core.water.bo.customize.LadderPriceDetailBo;
import com.dotop.smartwater.project.module.core.water.config.Config;
import com.dotop.smartwater.project.module.core.water.constants.OrderGenerateType;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.module.core.water.constants.WaterConstants;
import com.dotop.smartwater.project.module.core.water.dto.CouponDto;
import com.dotop.smartwater.project.module.core.water.dto.OrderDto;
import com.dotop.smartwater.project.module.core.water.dto.OrderExtDto;
import com.dotop.smartwater.project.module.core.water.dto.OrderLastUplinkDto;
import com.dotop.smartwater.project.module.core.water.dto.OrderPreviewDto;
import com.dotop.smartwater.project.module.core.water.dto.PayDetailDto;
import com.dotop.smartwater.project.module.core.water.dto.TradePayDto;
import com.dotop.smartwater.project.module.core.water.dto.WechatPublicSettingDto;
import com.dotop.smartwater.project.module.core.water.form.OrderExtForm;
import com.dotop.smartwater.project.module.core.water.form.TradePayForm;
import com.dotop.smartwater.project.module.core.water.form.customize.PreviewForm;
import com.dotop.smartwater.project.module.core.water.utils.BusinessUtil;
import com.dotop.smartwater.project.module.core.water.utils.CalUtil;
import com.dotop.smartwater.project.module.core.water.utils.CalcPenalty;
import com.dotop.smartwater.project.module.core.water.vo.CouponVo;
import com.dotop.smartwater.project.module.core.water.vo.OrderExtVo;
import com.dotop.smartwater.project.module.core.water.vo.OrderPreviewVo;
import com.dotop.smartwater.project.module.core.water.vo.OrderVo;
import com.dotop.smartwater.project.module.core.water.vo.PayTypeVo;
import com.dotop.smartwater.project.module.core.water.vo.PurposeVo;
import com.dotop.smartwater.project.module.core.water.vo.ReduceVo;
import com.dotop.smartwater.project.module.core.water.vo.TradePayVo;
import com.dotop.smartwater.project.module.core.water.vo.customize.AutoPayVo;
import com.dotop.smartwater.project.module.core.water.vo.customize.IncomeVo;
import com.dotop.smartwater.project.module.core.water.vo.customize.LadderPriceDetailVo;
import com.dotop.smartwater.project.module.core.water.vo.customize.LastUpLinkVo;
import com.dotop.smartwater.project.module.core.water.vo.customize.WechatPublicSettingVo;
import com.dotop.smartwater.project.module.dao.revenue.IOrderDao;
import com.dotop.smartwater.project.module.dao.revenue.IOrderExtDao;
import com.dotop.smartwater.project.module.dao.revenue.IOrderPreviewDao;
import com.dotop.smartwater.project.module.dao.revenue.ITradePayDao;
import com.dotop.smartwater.project.module.dao.tool.IWechatPublicSettingDao;
import com.dotop.smartwater.project.module.service.revenue.IOrderService;
import com.dotop.smartwater.project.module.service.tool.IWechatPublicSettingService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

/**

 */
@Service
public class OrderServiceImpl implements IOrderService {

	private static final Logger LOGGER = LogManager.getLogger(OrderServiceImpl.class);

	@Resource
	private IOrderDao iOrderDao;

	@Resource
	private IOrderExtDao iOrderExtDao;

	@Resource
	private IOrderPreviewDao iOrderPreviewDao;

	@Resource
	private ITradePayDao iTradePayDao;

	@Autowired
	private IWechatPublicSettingDao iWechatPublicSettingDao;

	@Autowired
	private IWechatPublicSettingService iWechatPublicSettingService;

	@Override
	public List<OrderVo> orderList(OrderBo orderBo) {

		try {
			OrderPreviewDto orderPreviewDto = BeanUtils.copy(orderBo, OrderPreviewDto.class);
			return iOrderDao.orderList(orderPreviewDto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public List<OrderVo> orderListByWeChat(OrderBo orderBo) {
		try {
			OrderPreviewDto orderPreviewDto = BeanUtils.copy(orderBo, OrderPreviewDto.class);
			return iOrderDao.orderListByWeChat(orderPreviewDto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public List<OrderVo> list(OrderPreviewBo orderPreviewBo) {
		try {
			return iOrderDao.orderList(BeanUtils.copy(orderPreviewBo, OrderPreviewDto.class));
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public OrderVo findByUserNo(String enterpriseid, String userno) {
		try {
			return iOrderDao.findByUserNo(enterpriseid, userno);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}

	}

	@Override
	public Map<String, String> generatePayQrCode(String enterpriseId, String userNo, String ip) {
		Map<String, String> resultMap = new HashMap<>();
		try {
			// 查找最新账单
			UserVo user = AuthCasClient.getUser();
			OrderVo order = iOrderDao.findByUserNo(enterpriseId, userNo);
			if (order != null) {
				// 获取微信配置信息
				WechatPublicSettingDto wechatPublicSettingDto = new WechatPublicSettingDto();
				wechatPublicSettingDto.setEnterpriseid(enterpriseId);
				WechatPublicSettingVo weixinConfig = iWechatPublicSettingDao.get(wechatPublicSettingDto);
				WechatPublicSettingBo weixinConfigBo = BeanUtils.copy(weixinConfig, WechatPublicSettingBo.class);

				// 获取交易流水
				TradePayVo tradePay = getTradePay(order.getTradeno());
				TradePayBo tradePayBo = null;
				if (tradePay != null) {
					tradePayBo = BeanUtils.copy(tradePay, TradePayBo.class);
				} else {
					tradePay = new TradePayVo();
					tradePay.setActualamount(order.getAmount());
					tradePay.setAmount(order.getAmount());
					tradePay.setOwnerpay(new BigDecimal(0));
					tradePay.setGivechange(new BigDecimal(0));
					tradePay.setPaytype(WaterConstants.ORDER_PAYTYPE_QRCODE);
					tradePay.setTradeno(order.getTradeno());
					tradePay.setPaytime(new Date());
					tradePay.setCreatetime(new Date());
					tradePay.setPayno(String.valueOf(Config.Generator.nextId()));
					if (user != null) {
						tradePay.setOperatorid(user.getUserid());
						tradePay.setOperatorname(user.getName());
					}
					tradePay.setOperatortime(new Date());
					tradePay.setUserno(order.getUserno());
					tradePay.setUsername(order.getUsername());

					tradePay.setPayno(String.valueOf(Config.Generator.nextId()));
					tradePay.setPaystatus(WaterConstants.TRADE_PAYSTATUS_IN);
					tradePay.setRemark("支付中");
					tradePay.setId(UuidUtils.getUuid());

					TradePayDto tradePayDto = BeanUtils.copy(tradePay, TradePayDto.class);
					iTradePayDao.addTradePay(tradePayDto);

					tradePayBo = BeanUtils.copy(tradePay, TradePayBo.class);
				}
				resultMap = (new WechatUtil()).payScan(order, weixinConfigBo, tradePayBo, ip);
				resultMap.put("amount", order.getAmount() + "");
			}
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
		return resultMap;
	}

	public static Map<String, Object> generateQrcodeTrade(OrderVo order) {
		Map<String, Object> result = new HashMap<>();

		// 获取系统

		return result;
	}

	@Override
	public LastUpLinkVo findLastUplink(String devid, String currentMonth, String lastMonth, String meterTime) {
		try {
			return iOrderDao.findLastUplink(devid, currentMonth, lastMonth, meterTime);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public Pagination<OrderVo> bills(OrderPreviewBo orderPreviewBo, String ids) {
		try {
			OrderPreviewDto orderPreviewDto = BeanUtils.copy(orderPreviewBo, OrderPreviewDto.class);
			if (StringUtils.isNotBlank(ids)) {
				orderPreviewDto.setCids(ids);
			}

			Page<Object> pageHelper = PageHelper.startPage(orderPreviewBo.getPage(), orderPreviewBo.getPageCount());

			List<OrderVo> list = iOrderDao.bills(orderPreviewDto);

			// 计算滞纳金
			calcPenalty(list);

			// 拼接数据返回
			return new Pagination<>(orderPreviewBo.getPage(), orderPreviewBo.getPageCount(), list,
					pageHelper.getTotal());

		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Deprecated
	private void calcPenalty(List<OrderVo> list) {
		for (OrderVo order : list) {
			/*OrderExtVo orderExt = iOrderExtDao.findOrderExtByTradeno(order.getTradeno());
			PayTypeVo payType = JSONUtils.parseObject(orderExt.getPaytypeinfo(), PayTypeVo.class);
			List<LadderPriceDetailVo> payDetailList = JSONUtils.parseArray(orderExt.getChargeinfo(),
					LadderPriceDetailVo.class);

			List<LadderPriceDetailBo> payDetailListBo = BeanUtils.copy(payDetailList, LadderPriceDetailBo.class);
			List<CompriseBo> compriseBos = BeanUtils.copy(payType.getComprises(), CompriseBo.class);
			order.setPenalty(BusinessUtil.getPenalty(payDetailListBo, compriseBos, order.getGeneratetime(),
					payType.getOverdueday()));*/
			order.setPenalty(0.0);
		}
	}

	@Override
	public Pagination<OrderPreviewVo> orderPreviewList(PreviewForm previewForm) {

		if ("2".equals(previewForm.getTradeStatus())) {
			previewForm.setTradeStatus(null);
		}

		Page<Object> pageHelper = PageHelper.startPage(previewForm.getPage(), previewForm.getPageCount());

		List<OrderPreviewVo> list = iOrderPreviewDao.findOrderPreview(previewForm);

		return new Pagination<>(previewForm.getPage(), previewForm.getPageCount(), list, pageHelper.getTotal());
	}

	@Override
	public Map<String, LastUpLinkVo> findLastUplinkList(PreviewForm previewForm, String currentMonth, String lastMonth,
			String meterTime) {
		try {
			String type = previewForm.getType();
			String communityIds = previewForm.getCommunityIds();
			String usernos = previewForm.getUsernos();

			OrderLastUplinkDto orderLastUplinkDto = new OrderLastUplinkDto();
			orderLastUplinkDto.setType(type);
			orderLastUplinkDto.setCommunityIds(communityIds);
			orderLastUplinkDto.setUsernos(usernos);
			orderLastUplinkDto.setCurrentMonth(currentMonth);
			orderLastUplinkDto.setLastMonth(lastMonth);
			orderLastUplinkDto.setMeterTime(meterTime);

			return iOrderDao.findLastUplinkList(orderLastUplinkDto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public Map<String, OrderVo> findByUserNoMap(String enterpriseid, String usernos) {
		try {
			return iOrderDao.findByUserNoMap(enterpriseid, usernos);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public void deletePreview(String ownerid) {
		try {
			iOrderExtDao.deleteOrderExtByOwnerId(ownerid);
			iOrderPreviewDao.deletePreview(ownerid);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	/**
	 * 批量删除账单预览
	 *
	 * @param previewForm
	 * @param flag
	 * @
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public void clearPreview(PreviewForm previewForm, boolean flag) {
		try {
			String condition = "";
			StringBuilder str = new StringBuilder();

			// 根据区域生成预览账单
			switch (previewForm.getType()) {
			case OrderGenerateType.COMMUNITY:
				// 根据选择的区域获取业主和水表信息
				for (String id : previewForm.getCommunityIds().split(",")) {
					if (str.length() == 0) {
						str.append("'").append(id).append("'");
					} else {
						str.append(",'").append(id).append("'");
					}
				}
				condition += String.format(" and communityid in (%s)", str);
				break;
			case OrderGenerateType.OWNER:
				// 根据水表号获取业主和水表信息
				for (String id : previewForm.getUsernos().split(",")) {
					if (str.length() == 0) {
						str.append("'").append(id).append("'");
					} else {
						str.append(",'").append(id).append("'");
					}
				}
				condition += String.format(" and userno in (%s)", str);
				break;
			default:
				return;
			}
			if (!"2".equals(previewForm.getTradeStatus()) && previewForm.getTradeStatus() != null) {
				condition += String.format(" and tradestatus = %s", previewForm.getTradeStatus());
			}
			// flag = true 清空预览
			if (flag) {
				/****
				 * 可以不删,后期统一处理，因为数据大的时候操作删除会进行锁表,影响其他用户的操作 同时,order_ext为原始记录表,不会有关联查询,不影响查询速度
				 * 因此,决定就算是问题账单的明细也冗余保存下来
				 ****/
				// deleteOrderExtTask.clearPreviewByCondition(condition);
			} else {
				// 确认账单时过滤异常账单
				condition += " and tradestatus = 1";
			}
			condition += " ORDER BY communityid LIMIT 50000;";

			int i = 50000;
			while (i == 50000) {
				i = iOrderPreviewDao.clearPreview(condition);
			}
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public List<OrderPreviewVo> findOrderPreview(PreviewForm previewForm) {
		try {
			if ("2".equals(previewForm.getTradeStatus())) {
				previewForm.setTradeStatus(null);
			}
			return iOrderPreviewDao.findOrderPreview(previewForm);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public int addOrders(PreviewForm previewForm) {
		try {
			if ("2".equals(previewForm.getTradeStatus())) {
				previewForm.setTradeStatus(null);
			}
			iOrderDao.addOrders(previewForm);
			return 1;

		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public Pagination<OrderVo> orderList(OrderPreviewBo orderPreviewBo) {

		try {
			OrderPreviewDto orderPreviewDto = BeanUtils.copy(orderPreviewBo, OrderPreviewDto.class);
			//拼接时间条件,这里查询半年内账单
			if(StringUtils.isNotBlank(orderPreviewDto.getGeneratetime())) {
				//传过来的数据格式为201904-201910
				if(orderPreviewDto.getGeneratetime().length() == 13) {
					String start = orderPreviewDto.getGeneratetime().substring(0, 6);
					String end = orderPreviewDto.getGeneratetime().substring(7, orderPreviewDto.getGeneratetime().length());
					orderPreviewDto.setGeneratetime(start);
					orderPreviewDto.setGenerateTimeEnd(end);
				}
			}
			Page<Object> pageHelper = PageHelper.startPage(orderPreviewBo.getPage(), orderPreviewBo.getPageCount());
			List<OrderVo> list = iOrderDao.orderList(orderPreviewDto);

			/** V2.6 滞纳金不在这里算 KJR
			calcPenalty(list);
			*/

			// 拼接数据返回
			return new Pagination<>(orderPreviewBo.getPage(), orderPreviewBo.getPageCount(), list,
					pageHelper.getTotal());
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public OrderVo findById(String id) {
		try {
			return iOrderDao.findById(id);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public void deleteById(OrderBo orderBo) {
		try {
			iOrderExtDao.deleteOrderExtByTradeno(orderBo.getTradeno());
			iOrderDao.delete(orderBo.getId());
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public OrderVo findByOwnerId(String ownerid) {
		try {
			return iOrderDao.findByOwnerId(ownerid);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public void updateOrderPreviewStatus(String ownerId) {
		try {
			OrderPreviewDto orderPreviewDto = new OrderPreviewDto();
			orderPreviewDto.setOwnerid(ownerId);
			orderPreviewDto.setTradestatus(WaterConstants.ORDER_TRADESTATUS_NORMAL);
			iOrderPreviewDao.update(orderPreviewDto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public OrderExtVo findOrderExtByTradeno(String tradeNo) {
		try {
			return iOrderExtDao.findOrderExtByTradeno(tradeNo);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public int findSubOrderCountByTradeNo(String tradeno) {
		try {
			return iOrderDao.findSubOrderCountByTradeNo(tradeno);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public BigDecimal findOwnerPayByPayNo(String payno) {
		try {
			Double result = iOrderDao.findOwnerPayByPayNo(payno);
			if (result == null) {
				result = 0.0;
			}
			return BigDecimal.valueOf(result);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public TradePayVo getTradePay(String tradeno) {
		try {
			return iTradePayDao.getTradePay(tradeno);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public IncomeVo statisIncome(String enterpriseid) {
		try {
			return iOrderDao.statisIncome(enterpriseid);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public OrderVo findOrderByTradeNo(String tradeno) {
		try {
			return iOrderDao.findOrderByTradeNo(tradeno);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public Map<String, String> getOrderPayStatus(OrderVo order, String payno, String enterpriseid) {
		Map<String, String> resultMap = new HashMap<>(50);
		InputStream inputStream = null;
		Map<String, String> returnMap;
		try {
			WechatPublicSettingDto wechatPublicSettingDto = new WechatPublicSettingDto();
			wechatPublicSettingDto.setEnterpriseid(enterpriseid);
			WechatPublicSettingVo weixinConfig = iWechatPublicSettingDao.get(wechatPublicSettingDto);
			if (weixinConfig != null) {

				Map<String, String> queryMap = new HashMap<>(10);
				queryMap.put("appid", weixinConfig.getAppid());
				queryMap.put("mch_id", weixinConfig.getMchid());
				queryMap.put("out_trade_no", payno);
				queryMap.put("nonce_str", CaptchaUtil.getAlphabetCaptcha(31));
				queryMap.put("sign", WechatUtil.sign(queryMap, weixinConfig.getPaysecret()));

				inputStream = WechatHttpClientUtils.sendPostXMLRequest(weixinConfig.getOrderqueryurl(),
						WechatUtil.map2XML(queryMap));
				returnMap = MessageManager.parseXml(inputStream);

				if (returnMap.get("return_code").equals("SUCCESS") && returnMap.get("result_code").equals("SUCCESS")) {
					if (returnMap.get("trade_state").equals("SUCCESS")) {
						// 支付成功
						order.setPaystatus(WaterConstants.ORDER_PAYSTATUS_PAID);
						order.setPaytime(DateUtils.formatDatetime(new Date()));

						iTradePayDao.updateTradePayStatus(payno, WaterConstants.TRADE_PAYSTATUS_SUCCESS, "支付成功");

						OrderDto orderDto = BeanUtils.copy(order, OrderDto.class);
						iOrderDao.updateOrder(orderDto);

						resultMap.put("status", "SUCCESS");
						resultMap.put("msg", "支付成功");
					} else {
						iTradePayDao.updateTradePayStatus(payno, WaterConstants.TRADE_PAYSTATUS_ERROR,
								returnMap.get("trade_state_desc"));
						resultMap.put("status", returnMap.get("trade_state"));
						resultMap.put("msg", returnMap.get("trade_state_desc"));
					}
				} else {
					resultMap.put("status", "ERROR");
					resultMap.put("msg", "更新异常");
				}
			}
			return resultMap;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		} catch (Exception e) {
			LOGGER.error(LogMsg.to(e));
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					LOGGER.error(LogMsg.to(e));
				}
			}
		}
		return resultMap;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public String mergeOrders(String orderids, UserVo user) {
		try {
			List<OrderVo> list = iOrderDao.findByIds(orderids);
			OrderDto newOrder = new OrderDto();
			OrderExtVo orderExt = new OrderExtVo();
			Double penaltyAll = 0.0;
			if (list != null && !list.isEmpty()) {
				OrderVo order = list.get(0);

				newOrder = BeanUtils.copy(order, OrderDto.class);
				String tradeno = String.valueOf(Config.Generator.nextId());

				List<LadderPriceDetailVo> payDetails = new ArrayList<>();

				newOrder.setAmount(0.00);
				newOrder.setWater(0.00);
				newOrder.setOriginal(new BigDecimal(0));

				for (int i = 0; i < list.size(); i++) {
					OrderVo od = list.get(i);
					newOrder.setWater(newOrder.getWater() + od.getWater());
					newOrder.setOriginal(newOrder.getOriginal().add(od.getOriginal()));
					newOrder.setAmount(newOrder.getAmount() + od.getAmount());

					if (i == (list.size() - 1)) {
						// 如果当前到达最后一条,则取上期读数和上期抄表当做新账单上去读数
						newOrder.setUpreadtime(od.getUpreadtime());
						newOrder.setUpreadwater(od.getUpreadwater());
					}

					orderExt = iOrderExtDao.findOrderExtByTradeno(od.getTradeno());

					// 滞纳金
					PayTypeVo payType = JSONUtils.parseObject(orderExt.getPaytypeinfo(), PayTypeVo.class);
					if (payType == null) {
						throw new FrameworkRuntimeException(ResultCode.ParamIllegal, "子账单:不存在的收费种类", null);
					}
					// 水费组成明细
					List<LadderPriceDetailVo> payDetailList = JSONUtils.parseArray(orderExt.getChargeinfo(),
							LadderPriceDetailVo.class);
					List<LadderPriceDetailBo> payDetailListBo = BeanUtils.copy(payDetailList,
							LadderPriceDetailBo.class);
					List<CompriseBo> compriseBos = BeanUtils.copy(payType.getComprises(), CompriseBo.class);
					Double penalty = BusinessUtil.getPenalty(payDetailListBo, compriseBos, order.getGeneratetime(),
							payType.getOverdueday());

					// 滞纳金累加
					penaltyAll = (CalUtil.add(penalty, penaltyAll));

					if (orderExt.getChargeinfo().contains("保底收费")) {
						for (LadderPriceDetailVo v2 : payDetailList) {
							if (v2.getName().equals("保底收费")) {
								boolean temp = false;
								// 如果总的费用明细下有保底收费，则合并计算 如果不存在则直接写入
								for (LadderPriceDetailVo v1 : payDetails) {
									if (v1.getName().equals("保底收费")) {
										temp = true;
										break;
									}
								}

								if (payDetails.isEmpty()) {
									temp = false;
								}

								if (temp) {
									for (LadderPriceDetailVo v1 : payDetails) {
										if (v1.getName().equals("保底收费")) {
											v1.setAmount(CalUtil.add(v1.getAmount(), v2.getAmount()));
											break;
										}
									}
								} else {
									payDetails.add(v2);
								}
							}
						}
					} else {
						if (i == 0) {
							payDetails = payDetailList;
						} else {
							for (LadderPriceDetailVo v1 : payDetails) {
								for (LadderPriceDetailVo v2 : payDetailList) {
									if (v1.getName().equals(v2.getName())) {
										v1.setAmount(CalUtil.add(v1.getAmount(), v2.getAmount()));
									}
								}
							}
						}
					}
					// 建立新账单与自账单之间的关系
					iOrderDao.insertOrderSubInf(tradeno, od.getTradeno());
				}

				newOrder.setAmount(CalUtil.add(newOrder.getAmount(), penaltyAll));

				newOrder.setTradeno(tradeno);
				newOrder.setGenerateuserid(user.getUserid());
				newOrder.setGenerateusername(user.getName());
				newOrder.setGeneratetime(DateUtils.formatDatetime(new Date()));

				orderExt.setTradeno(tradeno);
				orderExt.setChargeinfo(JSONUtils.toJSONString(payDetails));

				newOrder.setId(UuidUtils.getUuid());
				iOrderDao.addOrder(newOrder);

				OrderExtDto orderExtDto = BeanUtils.copy(orderExt, OrderExtDto.class);
				orderExtDto.setPenalty(penaltyAll);
				iOrderExtDao.addOrderExt(orderExtDto);

				// 修改原有账单为子账单
				iOrderDao.updateOrderSub(orderids);
			}

			return newOrder.getId();
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public Map<String, String> orderPay(OrderExtForm orderExtForm, OrderVo order, UserVo user, Double ownerBalance,
			int payMode, CouponVo coupon) {
		try {
			String payNo = String.valueOf(Config.Generator.nextId());
			Date d = new Date();
			TradePayDto tradePay = new TradePayDto();

			CouponDto couponDto = BeanUtils.copy(coupon, CouponDto.class);

			// 余额抵扣
			order.setBalance(orderExtForm.getBalance());
			// 剩余应缴
			order.setRealamount(orderExtForm.getRealamount());
			String date = DateUtils.formatDatetime(new Date());
			order.setPayno(payNo);
			order.setPaytype(payMode);
			if (user != null) {
				order.setOperatorid(user.getUserid());
				order.setOperatorname(user.getName());
			}
			order.setOperatortime(date);

			// 插入支付记录
			if (orderExtForm.getPayMode() != null
					&& orderExtForm.getPayMode().equals("" + WaterConstants.ORDER_PAYTYPE_PAYCARD)) {
				// 如果支付方式为刷卡支付
				tradePay.setActualamount(orderExtForm.getSurpluspay());
				tradePay.setAmount(orderExtForm.getSurpluspay());
				tradePay.setOwnerpay(BigDecimal.valueOf(orderExtForm.getSurpluspay()));
				tradePay.setGivechange(new BigDecimal(0));
			} else {
				tradePay.setActualamount(orderExtForm.getRealamount());
				tradePay.setAmount(order.getAmount());
				tradePay.setOwnerpay(orderExtForm.getOwnerpay());
				tradePay.setGivechange(
						CalUtil.sub(orderExtForm.getOwnerpay().toString(), orderExtForm.getRealamount().toString()));
			}
			tradePay.setPaytype(payMode);
			tradePay.setAftermoney(ownerBalance);
			tradePay.setBeforemoney(orderExtForm.getAlreadypay());
			tradePay.setTradeno(order.getTradeno());
			tradePay.setPaytime(d);
			tradePay.setCreatetime(d);
			tradePay.setPayno(payNo);
			if (user != null) {
				tradePay.setOperatorid(user.getUserid());
				tradePay.setOperatorname(user.getName());
			}
			tradePay.setOperatortime(d);
			tradePay.setUserno(order.getUserno());
			tradePay.setUsername(order.getUsername());

			// 修改账单额外的信息
			OrderExtDto orderExt = new OrderExtDto();
			if (couponDto != null) {
				couponDto.setStatus(WaterConstants.COUPON_STATUS_USED);
				couponDto.setBill(order.getTradeno());
				if (user != null) {
					couponDto.setUpdateuser(user.getUserid());
					couponDto.setUpdateusername(user.getName());
				}
				// 优惠券标记为已经使用
				iOrderDao.couponIsUsed(couponDto);
				orderExt.setCouponinfo(JSONUtils.toJSONString(coupon));
				// 写入账单表
				order.setCouponmoney(coupon.getFacevalue());
			}

			// 水表用途
			if (orderExtForm.getPurpose() != null) {
				orderExt.setPurposeinfo(JSONUtils.toJSONString(orderExtForm.getPurpose()));
			}
			if (orderExtForm.getAlreadypay() != null) {
				orderExt.setAlreadypay(orderExtForm.getAlreadypay());
			} else {
				orderExt.setAlreadypay(0.00);
			}

			orderExt.setIschargebacks(orderExtForm.getIschargebacks());
			orderExt.setCardid(orderExtForm.getCardid());
			if (orderExtForm.getPenalty() != null) {
				orderExt.setPenalty(orderExtForm.getPenalty());
			} else {
				orderExt.setPenalty(0.00);
			}

			orderExt.setTradeno(order.getTradeno());
			iOrderExtDao.updateOrderExt(orderExt);

			// 修改业主余额 update order_ext SET penalty = ?, alreadypay = ? where tradeno = ?
			// 修改业主滞纳金 和余额
			iOrderDao.updateOwnerAlreadypay(order.getOwnerid(), ownerBalance);

			// 余额抵扣大于0,写进记录表
			if (order.getBalance() > 0) {
				PayDetailDto payDetail = new PayDetailDto();
				payDetail.setId(UuidUtils.getUuid());
				payDetail.setOwnerid(order.getOwnerid());
				payDetail.setOwnerno(order.getUserno());
				payDetail.setOwnername(order.getUsername());
				payDetail.setPaymoney(new BigDecimal(order.getBalance()));
				payDetail.setCreatetime(d);
				if (user != null) {
					payDetail.setCreateuser(user.getUserid());
					payDetail.setUsername(user.getAccount());
				}
				payDetail.setType(WaterConstants.PAY_DETAIL_TYPE_OUT);
				payDetail.setBeforemoney(new BigDecimal(orderExtForm.getAlreadypay()));
				payDetail.setAftermoney(new BigDecimal(ownerBalance));
				payDetail.setPayno(payNo);
				payDetail.setTradeno(order.getTradeno());
				payDetail.setRemark("余额抵扣水费");
				iOrderDao.addPayDetail(payDetail);
			}

			Map<String, String> data = new HashMap<>(50);
			OrderDto orderDto;
			switch (payMode) {
			// 现金
			case WaterConstants.ORDER_PAYTYPE_MONEY:
				tradePay.setPaystatus(WaterConstants.TRADE_PAYSTATUS_SUCCESS);
				tradePay.setRemark("支付成功");
				tradePay.setId(UuidUtils.getUuid());
				iTradePayDao.addTradePay(tradePay);

				order.setPaystatus(WaterConstants.ORDER_PAYSTATUS_PAID);
				order.setPaytime(DateUtils.formatDatetime(new Date()));
				orderDto = BeanUtils.copy(order, OrderDto.class);
				// 修改账单
				iOrderDao.updateOrder(orderDto);

				// 验证当前账单下是否有子账单，如果存在则修改成已缴费
				iOrderDao.updateSubOrder(order.getTradeno());
				data.put("return_code", "SUCCESS");
				data.put("return_msg", "支付成功");
				break;
			// 微信手机支付
			case WaterConstants.ORDER_PAYTYPE_WEIXIN:
				TradePayVo tpay = iTradePayDao.getTradePay(orderExtForm.getTradeno());
				if (tpay != null) {
					tradePay = BeanUtils.copy(tpay, TradePayDto.class);
					if (tradePay.getPaystatus().intValue() == WaterConstants.TRADE_PAYSTATUS_IN) {
						data.put("return_code", "SUCCESS");
						data.put("return_msg", "支付中");
					} else if (tradePay.getPaystatus().intValue() == WaterConstants.TRADE_PAYSTATUS_SUCCESS) {
						data.put("return_code", "SUCCESS");
						data.put("return_msg", "已支付");
					}
					tradePay.setPayno(String.valueOf(Config.Generator.nextId()));
				} else {
					tradePay.setPayno(String.valueOf(Config.Generator.nextId()));
					tradePay.setPaystatus(WaterConstants.TRADE_PAYSTATUS_IN);
					tradePay.setRemark("支付中");
					order.setPayno(tradePay.getPayno());

					order.setPaystatus(WaterConstants.ORDER_PAYSTATUS_NOTPAID);
					order.setPaytime(DateUtils.formatDatetime(new Date()));
					orderDto = BeanUtils.copy(order, OrderDto.class);
					// 修改账单
					iOrderDao.updateOrder(orderDto);

					// 发起支付前先保存交易记录
					tradePay.setId(UuidUtils.getUuid());
					iTradePayDao.addTradePay(tradePay);
					data.put("return_code", "SUCCESS");
					data.put("return_msg", "支付中");

				}

				// 验证当前账单下是否有子账单，如果存在则修改成已缴费
				iOrderDao.updateSubOrder(order.getTradeno());

				break;
			// 支付宝支付
			case WaterConstants.ORDER_PAYTYPE_ALIPAY:
				break;
			// 微信刷卡支付
			case WaterConstants.ORDER_PAYTYPE_PAYCARD:
				WechatPublicSettingVo weixinConfig = iWechatPublicSettingService
						.getByenterpriseId(user.getEnterpriseid());
				tradePay.setPayno(String.valueOf(Config.Generator.nextId()));
				order.setPayno(tradePay.getPayno());
				// 发起支付前先保存交易记录
				tradePay.setId(UuidUtils.getUuid());
				iTradePayDao.addTradePay(tradePay);

				OrderExtBo orderExtBo = BeanUtils.copy(orderExtForm, OrderExtBo.class);
				WechatPublicSettingBo weixinConfigBo = BeanUtils.copy(weixinConfig, WechatPublicSettingBo.class);
				TradePayBo tradePayBo = BeanUtils.copy(tradePay, TradePayBo.class);

				Map<String, String> resultMap = (new WechatUtil()).payByCard(orderExtBo, weixinConfigBo, tradePayBo);
				if (MessageManager.SUCCESS.equalsIgnoreCase(resultMap.get(MessageManager.RESP_RETURN_CODE))) {
					if (MessageManager.SUCCESS.equalsIgnoreCase(resultMap.get(MessageManager.RESP_RESULT_CODE))) {
						order.setPaystatus(WaterConstants.ORDER_PAYSTATUS_PAID);
						order.setPaytime(DateUtils.formatDatetime(new Date()));
						iTradePayDao.updateTradePayStatus(tradePay.getPayno(), WaterConstants.TRADE_PAYSTATUS_SUCCESS,
								"支付成功");

						order.setPaytime(DateUtils.formatDatetime(new Date()));
						// 修改账单
						orderDto = BeanUtils.copy(order, OrderDto.class);
						iOrderDao.updateOrder(orderDto);

						// 验证当前账单下是否有子账单，如果存在则修改成已缴费
						iOrderDao.updateSubOrder(order.getTradeno());

						data.put("return_code", "SUCCESS");
						data.put("return_msg", "支付成功");
					} else {
						data.put("return_code", "FAIL");
						data.put("return_msg", resultMap.get(MessageManager.RESP_ERR_CODE_DES));
						iTradePayDao.updateTradePayStatus(tradePay.getPayno(), WaterConstants.TRADE_PAYSTATUS_ERROR,
								resultMap.get(MessageManager.RESP_ERR_CODE_DES));

						order.setPaystatus(WaterConstants.ORDER_PAYSTATUS_NOTPAID);
						order.setPaytime(DateUtils.formatDatetime(new Date()));
						orderDto = BeanUtils.copy(order, OrderDto.class);
						// 修改账单
						iOrderDao.updateOrder(orderDto);

						// 验证当前账单下是否有子账单，如果存在则修改成已缴费
						iOrderDao.updateSubOrder(order.getTradeno());

						return data;
					}
				} else {
					if (resultMap.get(MessageManager.RESP_ERR_CODE_DES) != null) {
						data.put("return_code", "FAIL");
						data.put("return_msg", resultMap.get(MessageManager.RESP_ERR_CODE_DES));
						iTradePayDao.updateTradePayStatus(tradePay.getPayno(), WaterConstants.TRADE_PAYSTATUS_ERROR,
								resultMap.get(MessageManager.RESP_ERR_CODE_DES));

						order.setPaystatus(WaterConstants.ORDER_PAYSTATUS_NOTPAID);
						order.setPaytime(DateUtils.formatDatetime(new Date()));
						orderDto = BeanUtils.copy(order, OrderDto.class);
						// 修改账单
						iOrderDao.updateOrder(orderDto);

						// 验证当前账单下是否有子账单，如果存在则修改成已缴费
						iOrderDao.updateSubOrder(order.getTradeno());
						return data;
					}
				}
				break;
			default:
				data.put("return_code", "FAIL");
				data.put("return_msg", "不明确的支付方式");
				iTradePayDao.updateTradePayStatus(tradePay.getPayno(), WaterConstants.TRADE_PAYSTATUS_ERROR,
						"不明确的支付方式");
				return data;
			}
			return data;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public int addOrder(OrderBo order, PayTypeVo payType, ReduceVo reduce, PurposeVo purpose,
			List<LadderPriceDetailVo> payDetailList) {
		try {

			OrderDto orderDto = BeanUtils.copy(order, OrderDto.class);
			orderDto.setIsprint(WaterConstants.ORDER_PRINT_NO);
			orderDto.setCouponmoney(0.0);
			orderDto.setId(order.getTradeno());
			orderDto.setIssub(WaterConstants.ORDER_ISSUB_NO);
			iOrderDao.addOrder(orderDto);

			OrderExtDto orderExtDto = new OrderExtDto();
			orderExtDto.setTradeno(order.getTradeno());
			orderExtDto.setPaytypeinfo(JSONUtils.toJSONString(payType));
			orderExtDto.setReduceinfo(JSONUtils.toJSONString(reduce));
			orderExtDto.setPurposeinfo(JSONUtils.toJSONString(purpose));
			orderExtDto.setChargeinfo(JSONUtils.toJSONString(payDetailList));
			iOrderExtDao.addOrderExt(orderExtDto);

			//金额等于0，设置为已缴
			if(orderDto.getAmount() <= 0){
				orderDto.setPaystatus(WaterConstants.PAY_STATUS_YES);
				orderDto.setPaytime(DateUtils.formatDatetime(new Date()));
				orderDto.setPaytype(WaterConstants.ORDER_PAYTYPE_MONEY);
				iOrderDao.updateOrder(orderDto);
			}

			return 2;

		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public void addOrderPreviewList(List<OrderPreviewBo> list) {
		try {
			iOrderDao.addOrderPreviewList(list);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public void addOrderExtList(List<OrderExtBo> extList) {
		try {
			iOrderDao.addOrderExtList(extList);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public void clearOrderPreviewExtByCondition(Object obj) {

	}

	@Override
	public List<AutoPayVo> getAutoPay(String enterpriseid) {
		try {
			List<AutoPayVo> list = iOrderDao.getAutoPay(enterpriseid);
			return list;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public List<OrderVo> getListBetweenDate(String operEid, String startDate, String endDate) {
		try {
			List<OrderVo> list = iOrderDao.getListBetweenDate(operEid, startDate, endDate);
			CalcPenalty.calcPenalty(list);
			return list;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public List<OrderVo> findByIds(List<String> orderIds) {
		try {
			StringBuilder orderIdstr = new StringBuilder();
			for (String str : orderIds) {
				orderIdstr.append(str).append(",");
			}
			orderIdstr.append("id"); // 占位
			List<OrderVo> list = iOrderDao.findByIds(orderIdstr.toString());
			CalcPenalty.calcPenalty(list);
			return list;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public Map<String, OrderExtVo> findOrderExtByTradenos(List<String> tradenos) {
		try {
			return iOrderExtDao.findOrderExtByTradenos(tradenos);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public Map<String, TradePayVo> getTradePayByTradenos(List<String> tradenos) {
		try {
			return iTradePayDao.getTradePayByTradenos(tradenos);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public Map<String, Map> findSubOrderCountByTradeNo(List<String> tradenos) {
		try {
			return iOrderDao.findSubOrderCountByTradeNoMap(tradenos);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public void deleteTradePay(String payno) {
		try {
			iOrderDao.deleteTradePay(payno);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public void updateCouponStatus(String tradeno) {
		try {
			iOrderDao.updateCouponStatus(tradeno);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}

	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public void revokeOrder(OrderBo orderBo) {
		try {
			OrderDto orderDto = BeanUtils.copyProperties(orderBo, OrderDto.class);
			iOrderDao.revokeOrder(orderDto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}

	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public void updateOrder(OrderBo orderBo) {
		try {
			OrderDto orderDto = BeanUtils.copyProperties(orderBo, OrderDto.class);
			iOrderDao.updateOrder(orderDto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}

	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public void updateTradePayStatus(String payno, String paystatus, String remark) {
		try {
			iOrderDao.updateTradePayStatus(payno, paystatus, remark);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}

	}

	@Override
	public void updateSubOrder(String tradeno) {
		try {
			iOrderDao.updateSubOrder(tradeno);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}

	}

	@Override
	public Pagination<OrderPreviewVo> auditingOrderPreviewList(PreviewForm previewForm) {
		try {
			if ("2".equals(previewForm.getTradeStatus())) {
				previewForm.setTradeStatus(null);
			}

			Page<Object> pageHelper = PageHelper.startPage(previewForm.getPage(), previewForm.getPageCount());

			List<OrderPreviewVo> list = iOrderPreviewDao.AuditingOrderPreviewList(previewForm);

			return new Pagination<>(previewForm.getPage(), previewForm.getPageCount(), list, pageHelper.getTotal());
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public int addOrdersByAuditing(PreviewForm previewForm) {
		try {
			iOrderDao.addOrdersByAuditing(previewForm);
			return 1;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public void clearPreviewByAuditing(PreviewForm previewForm, Boolean flag) {
		try {
			String condition = "";
			// 根据选择的区域获取业主和水表信息
			if (StringUtils.isNotBlank(previewForm.getCommunityIds())) {
				StringBuilder str = new StringBuilder();
				for (String id : previewForm.getCommunityIds().split(",")) {
					if (str.length() == 0) {
						str.append("'").append(id).append("'");
					} else {
						str.append(",'").append(id).append("'");
					}
				}
				condition += String.format(" and communityid in (%s)", str);
			}

			if (StringUtils.isNotBlank(previewForm.getUsernos())) {
				condition += String.format(" and userno = '%s'", previewForm.getUsernos());
			}
			if (StringUtils.isNotBlank(previewForm.getDevno())) {
				condition += String.format(" and devno = '%s'", previewForm.getDevno());
			}
			if (StringUtils.isNotBlank(previewForm.getUsername())) {
				condition += String.format(" and username = '%s'", previewForm.getUsername());
			}

			if (!"2".equals(previewForm.getTradeStatus()) && previewForm.getTradeStatus() != null) {
				condition += String.format(" and tradestatus = %s", previewForm.getTradeStatus());
			}
			// flag = true 清空预览
			if (flag) {
				// TODO
				/****
				 * 可以不删,后期统一处理，因为数据大的时候操作删除会进行锁表,影响其他用户的操作 同时,order_ext为原始记录表,不会有关联查询,不影响查询速度
				 * 因此,决定就算是问题账单的明细也冗余保存下来
				 ****/
				// deleteOrderExtTask.clearPreviewByCondition(condition);
			} else {
				// 确认账单时过滤异常账单
				condition += " and tradestatus = 1";
			}
			condition += " ORDER BY communityid LIMIT 50000;";

			int i = 50000;
			while (i == 50000) {
				i = iOrderPreviewDao.clearPreview(condition);
			}
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public void updateOrderPreviewApprovalResult(PreviewForm previewForm) {
		try {
			iOrderPreviewDao.updateOrderPreviewApprovalResult(previewForm);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public int addTradePay(TradePayForm tradePayForm) {
		try {
			iTradePayDao.addTradePay(BeanUtils.copy(tradePayForm, TradePayDto.class));
			return 1;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	public TradePayVo findPayNo(TradePayForm tradePayForm) {
		TradePayDto dto = BeanUtils.copy(tradePayForm, TradePayDto.class);
		return iTradePayDao.findPayNo(dto);
	}
}
