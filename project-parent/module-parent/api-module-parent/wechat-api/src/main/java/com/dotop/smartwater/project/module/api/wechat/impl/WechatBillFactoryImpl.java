package com.dotop.smartwater.project.module.api.wechat.impl;

import com.alibaba.fastjson.JSON;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.JSONUtils;
import com.dotop.smartwater.project.module.api.wechat.IWechatBillFactory;
import com.dotop.smartwater.project.module.core.auth.wechat.WechatAuthClient;
import com.dotop.smartwater.project.module.core.auth.wechat.WechatUser;
import com.dotop.smartwater.project.module.core.water.bo.CompriseBo;
import com.dotop.smartwater.project.module.core.water.bo.OrderPreviewBo;
import com.dotop.smartwater.project.module.core.water.bo.OwnerBo;
import com.dotop.smartwater.project.module.core.water.bo.PaymentTradeOrderBo;
import com.dotop.smartwater.project.module.core.water.bo.customize.LadderPriceDetailBo;
import com.dotop.smartwater.project.module.core.water.constants.PaymentConstants;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.module.core.water.constants.WaterConstants;
import com.dotop.smartwater.project.module.core.water.form.customize.WechatParamForm;
import com.dotop.smartwater.project.module.core.water.utils.BusinessUtil;
import com.dotop.smartwater.project.module.core.water.utils.CalUtil;
import com.dotop.smartwater.project.module.core.water.vo.*;
import com.dotop.smartwater.project.module.core.water.vo.customize.LadderPriceDetailVo;
import com.dotop.smartwater.project.module.core.water.vo.customize.QueryBillVo;
import com.dotop.smartwater.project.module.service.revenue.IOrderService;
import com.dotop.smartwater.project.module.service.revenue.IOwnerService;
import com.dotop.smartwater.project.module.service.revenue.IPaymentTradeOrderService;
import com.dotop.smartwater.project.module.service.revenue.IWrongAccountService;
import com.dotop.smartwater.project.module.service.wechat.IWechatService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 迁移改造
 *

 * @date 2019年3月22日
 */
@Component
public class WechatBillFactoryImpl implements IWechatBillFactory {

  @Autowired private IOwnerService iOwnerService;

  @Autowired private IWechatService iWechatService;

  @Autowired private IOrderService iOrderService;

  @Autowired private IWrongAccountService iWrongAccountService;

  @Autowired private IPaymentTradeOrderService iPaymentTradeOrderService;

  @Override
  public Pagination<PaymentTradeOrderVo> page(HttpServletRequest request, WechatParamForm wechatParamForm) {
    WechatUser wechatUser = WechatAuthClient.get();
    String ownerId = wechatUser.getOwnerid();
    String enterpriseid = wechatUser.getEnterpriseid();
    // 查询未缴费账单列表
    /*OrderPreviewBo orderPreviewBo = new OrderPreviewBo();
    orderPreviewBo.setOwnerid(ownerId);
    // 未支付的状态
    orderPreviewBo.setPaystatus(WaterConstants.PAY_STATUS_NO);
    // 账单状态 1 正常
    orderPreviewBo.setTradestatus(1);
    orderPreviewBo.setEnterpriseid(enterpriseid);
    orderPreviewBo.setPage(wechatParamForm.getPage());
    orderPreviewBo.setPageCount(wechatParamForm.getPageCount());
    // 调用账单现有的方法分页方法 不必重写 orderList
    return iOrderService.orderList(orderPreviewBo);*/

    PaymentTradeOrderBo paymentTradeOrderBo = new PaymentTradeOrderBo();
    paymentTradeOrderBo.setUserid(ownerId);
    paymentTradeOrderBo.setEnterpriseid(enterpriseid);
    paymentTradeOrderBo.setBusinessId(PaymentConstants.BUSSINESS_TYPE_WATERFEE);
    paymentTradeOrderBo.setStatus(wechatParamForm.getStatus());

    paymentTradeOrderBo.setPage(wechatParamForm.getPage());
    paymentTradeOrderBo.setPageCount(wechatParamForm.getPageCount());
    return iPaymentTradeOrderService.page(paymentTradeOrderBo);

  }

  @Override
  public Pagination<OrderVo> queryPaybill(
      HttpServletRequest request, WechatParamForm wechatParamForm) {
    WechatUser wechatUser = WechatAuthClient.get();
    String ownerId = wechatUser.getOwnerid();
    String enterpriseid = wechatUser.getEnterpriseid();
    // 查询未缴费账单列表
    OrderPreviewBo orderPreviewBo = new OrderPreviewBo();
    orderPreviewBo.setOwnerid(ownerId);
    // 已支付的状态
    orderPreviewBo.setPaystatus(WaterConstants.PAY_STATUS_YES);
    // 账单状态 1 正常
    orderPreviewBo.setTradestatus(1);
    orderPreviewBo.setEnterpriseid(enterpriseid);
    orderPreviewBo.setPage(wechatParamForm.getPage());
    orderPreviewBo.setPageCount(wechatParamForm.getPageCount());
    // 调用账单现有的方法分页方法 不必重写 orderList
    return iOrderService.orderList(orderPreviewBo);
  }

  @Override
  public List<QueryBillVo> getOrderTrend(
      HttpServletRequest request, WechatParamForm wechatParamForm) {
    WechatUser wechatUser = WechatAuthClient.get();
    String ownerId = wechatUser.getOwnerid();
    OwnerBo ownerBo = new OwnerBo();
    ownerBo.setOwnerid(ownerId);

    // 调用存在的方法
    OwnerVo currentOwner = iOwnerService.findByOwnerId(ownerBo);
    if (currentOwner == null) {
      throw new FrameworkRuntimeException(ResultCode.WECHAT_NOT_OWNER_ERROR, "未有绑定业主");
      // 校验当前登录业主是否过户或者销户
    } else if (currentOwner.getStatus().intValue() == 0) {
      throw new FrameworkRuntimeException(ResultCode.WECHAT_OWNER_NOT_EXIST_ERROR, "业主已经过户或者销户");
    }
    wechatParamForm.setOwnerid(ownerId);
    return iWechatService.getOrderTrend(ownerId);
  }

  @Override
  public Pagination<OrderVo> getOrderList(
      HttpServletRequest request, WechatParamForm wechatParamForm) {
    WechatUser wechatUser = WechatAuthClient.get();
    String ownerId = wechatUser.getOwnerid();
    OwnerBo ownerBo = new OwnerBo();
    ownerBo.setOwnerid(ownerId);
    // 调用存在的方法
    OwnerVo currentOwner = iOwnerService.findByOwnerId(ownerBo);
    if (currentOwner == null) {
      throw new FrameworkRuntimeException(ResultCode.WECHAT_NOT_OWNER_ERROR, "未有绑定业主");
      // 校验当前登录业主是否过户或者销户
    } else if (currentOwner.getStatus().intValue() == 0) {
      throw new FrameworkRuntimeException(ResultCode.WECHAT_OWNER_NOT_EXIST_ERROR, "业主已经过户或者销户");
    }
    return iWechatService.getOrderList(
        ownerId, wechatParamForm.getPage(), wechatParamForm.getPageCount());
  }

  @Override
  public Map<String, Object> getbillDetail(String tradeno) {
    Map<String, Object> map = new HashMap<>();
    WechatUser wechatUser = WechatAuthClient.get();
    String ownerId = wechatUser.getOwnerid();
    String enterpriseid = wechatUser.getEnterpriseid();
    OwnerBo ownerBo = new OwnerBo();
    ownerBo.setOwnerid(ownerId);
    OwnerVo currentOwner = iOwnerService.findByOwnerId(ownerBo);

    // 查询工单信息
    OrderVo order = iOrderService.findOrderByTradeNo(tradeno);
    if (order == null) {
      throw new FrameworkRuntimeException(ResultCode.WECHATORDERERROR, "账单不存在");
    } else if (order.getTradestatus().intValue() != 1) {
      throw new FrameworkRuntimeException(ResultCode.WECHATORDERERROR, "账单状态异常");
    }
    OrderExtVo orderExt = iOrderService.findOrderExtByTradeno(tradeno);
    if (orderExt == null) {
      throw new FrameworkRuntimeException(ResultCode.ParamIllegal, "错误的账单");
    }
    PayTypeVo payType = JSONUtils.parseObject(orderExt.getPaytypeinfo(), PayTypeVo.class);
    if (payType == null) {
      throw new FrameworkRuntimeException(ResultCode.ParamIllegal, "不存在的收费种类");
    }
    map.put("order", order);
    OrderExtVo orderExtVo = getExtOrder(order, currentOwner, payType, orderExt);
    map.put("orderExtVo", orderExtVo);
    // 查询账单的错账信息
    //		WrongAccountBo wrongAccountBo = new WrongAccountBo();
    //		wrongAccountBo.setEnterpriseid(enterpriseid);
    //		wrongAccountBo.setOrderid(order.getId());
    //		wrongAccountBo.setOwnerid(ownerId);
    //		WrongAccountVo wrongAccountVo = iWrongAccountService.get(wrongAccountBo);
    //		map.put("wrongAccountVo", wrongAccountVo);
    return map;
  }

  private OrderExtVo getExtOrder(
      OrderVo order, OwnerVo owner, PayTypeVo payType, OrderExtVo orderExt) {
    OrderExtVo orderExtVo = new OrderExtVo();
    orderExtVo.setId(order.getId());

    orderExtVo.setCardid(owner.getCardid());
    orderExtVo.setIschargebacks(owner.getIschargebacks());
    if (owner.getAlreadypay() == null || owner.getAlreadypay() < 0) {
      orderExtVo.setAlreadypay(0.0);
    } else {
      orderExtVo.setAlreadypay(owner.getAlreadypay());
    }

    // 水费组成明细
    List<LadderPriceDetailVo> payDetailList =
        JSON.parseArray(orderExt.getChargeinfo(), LadderPriceDetailVo.class);
    orderExtVo.setPayDetailList(payDetailList);
    List<LadderPriceDetailBo> payDetailListBo = new ArrayList<>();
    for (LadderPriceDetailVo ladderPriceDetailVo : payDetailList) {
      LadderPriceDetailBo ladderPriceDetailBo = new LadderPriceDetailBo();
      BeanUtils.copyProperties(ladderPriceDetailVo, ladderPriceDetailBo);
      payDetailListBo.add(ladderPriceDetailBo);
    }

    List<CompriseBo> compriseBolist = new ArrayList<>();
    if (payType.getComprises() != null) {
      for (CompriseVo compriseVo : payType.getComprises()) {
        CompriseBo compriseBo = new CompriseBo();
        BeanUtils.copyProperties(compriseVo, compriseBo);
        compriseBolist.add(compriseBo);
      }
    }

    // 滞纳金
    orderExtVo.setPenalty(
        BusinessUtil.getPenalty(
            payDetailListBo, compriseBolist, order.getGeneratetime(), payType.getOverdueday()));
    orderExtVo.setRealamount(CalUtil.add(order.getAmount(), orderExtVo.getPenalty()));
    orderExtVo.setBalance(0.0);

    // 异常账单 order.getAmount() 为空
    if (order.getAmount() == null) {
      orderExtVo.setOwnerpay(null);
    } else {
      orderExtVo.setOwnerpay(new BigDecimal(Double.toString(orderExtVo.getRealamount())));
    }

    CouponVo conpon = JSONUtils.parseObject(orderExt.getCouponinfo(), CouponVo.class);
    if (conpon != null) {
      List<CouponVo> couponList = new ArrayList<>();
      couponList.add(conpon);
      orderExtVo.setCouponList(couponList);
      orderExtVo.setCouponid(conpon.getCouponid());
    }
    orderExtVo.setBalance(order.getBalance());
    // 实收
    orderExtVo.setOwnerpay(iOrderService.findOwnerPayByPayNo(order.getPayno()));
    // 滞纳金
    orderExtVo.setPenalty(orderExt.getPenalty());
    // 水表用途
    PurposeVo purpose = JSONUtils.parseObject(orderExt.getPurposeinfo(), PurposeVo.class);
    orderExtVo.setPurpose(purpose);
    // 减免类型
    ReduceVo reduce = JSONUtils.parseObject(orderExt.getReduceinfo(), ReduceVo.class);
    orderExtVo.setReduce(reduce);
    // 交易流水
    TradePayVo trade = iOrderService.getTradePay(order.getTradeno());
    orderExtVo.setTradepay(trade);

    return orderExtVo;
  }
}
