package com.dotop.smartwater.project.module.api.revenue.async;

import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.utils.DateUtils;
import com.dotop.smartwater.dependence.core.utils.VerificationUtils;
import com.dotop.smartwater.project.module.api.revenue.IDeleteOrderExtTaskFactory;
import com.dotop.smartwater.project.module.api.revenue.IMakeOrderFactory;
import com.dotop.smartwater.project.module.api.revenue.IPaymentFactory;
import com.dotop.smartwater.project.module.api.revenue.IPushMsgFactory;
import com.dotop.smartwater.project.module.core.auth.enums.SmsEnum;
import com.dotop.smartwater.project.module.core.auth.vo.SettlementVo;
import com.dotop.smartwater.project.module.core.auth.vo.UserVo;
import com.dotop.smartwater.project.module.core.pay.constants.PayConstants;
import com.dotop.smartwater.project.module.core.water.bo.OrderBo;
import com.dotop.smartwater.project.module.core.water.bo.OwnerBo;
import com.dotop.smartwater.project.module.core.water.bo.PaymentTradeOrderBo;
import com.dotop.smartwater.project.module.core.water.constants.PaymentConstants;
import com.dotop.smartwater.project.module.core.water.constants.WaterConstants;
import com.dotop.smartwater.project.module.core.water.form.PaymentTradeOrderExtForm;
import com.dotop.smartwater.project.module.core.water.vo.OrderPreviewVo;
import com.dotop.smartwater.project.module.core.water.vo.OrderVo;
import com.dotop.smartwater.project.module.core.water.vo.OwnerVo;
import com.dotop.smartwater.project.module.core.water.vo.PaymentTradeOrderVo;
import com.dotop.smartwater.project.module.core.water.vo.customize.AutoPayVo;
import com.dotop.smartwater.project.module.service.revenue.IOrderService;
import com.dotop.smartwater.project.module.service.revenue.IOwnerService;
import com.dotop.smartwater.project.module.service.revenue.IPaymentTradeOrderService;
import com.dotop.smartwater.project.module.service.tool.ISettlementService;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 此类参数传参需要优化调整
 */
@Component
public class MakeOrderFactoryImpl implements IMakeOrderFactory {

    private final static Logger logger = LogManager.getLogger(MakeOrderFactoryImpl.class);

    @Autowired
    private IOrderService iOrderService;

    @Autowired
    private IPushMsgFactory iPushMsgFactory;

    @Autowired
    private IOwnerService iOwnerService;

    @Autowired
    private IPaymentTradeOrderService service;

    @Autowired
    private IPaymentFactory iPaymentFactory;

    @Autowired
    private IDeleteOrderExtTaskFactory iDeleteOrderExtTaskFactory;

    @Autowired
    private ISettlementService iSettlementService;

    // 处理自动扣费的账单,一并处理<账单金额等于0则直接将账单设置为已缴费>
    @Async("asyncAutoPay")
    @Override
    public void asyncAutoPay(UserVo user) throws FrameworkRuntimeException {
        long time = System.currentTimeMillis();
        List<AutoPayVo> list = iOrderService.getAutoPay(user.getEnterpriseid());
        if (list == null || list.size() == 0) {
            return;
        }

        logger.info("asyncAutoPay 业务");

        // 找出需要自动缴费的业主
        for (AutoPayVo autoPay : list) {
            if (autoPay.getIschargebacks() == null || 0 == autoPay.getIschargebacks()) {
                // 账单金额为0,直接设置为已缴费
                if (autoPay.getAmount() == 0) {
                    handleAutoPay(user, autoPay);
                }

                continue;
            }

            // 处理自动扣费
            handleAutoPay(user, autoPay);
        }

        //处理欠费自动关阀
        SettlementVo vo = iSettlementService.getSettlement(user.getEnterpriseid());
        if(vo != null){
            for (AutoPayVo autoPay : list) {
                iDeleteOrderExtTaskFactory.sendArrearsCloseNotice(autoPay.getOwnerid(), vo);
            }
        }

        logger.info("[{}]异步耗时:{}", Thread.currentThread().getName(), System.currentTimeMillis() - time);

    }

    private void handleAutoPay(UserVo user, AutoPayVo autoPay) throws FrameworkRuntimeException {
        //0元要变成已缴
        if (autoPay.getAmount() <= 0) {
            OrderBo orderBo = new OrderBo();
            orderBo.setId(autoPay.getId());
            orderBo.setPaystatus(WaterConstants.ORDER_PAYSTATUS_PAID);
            iOrderService.updateOrder(orderBo);
            return;
        }

        PaymentTradeOrderBo bo = new PaymentTradeOrderBo();
        bo.setTradeNumber(autoPay.getTradeno());
        bo.setEnterpriseid(user.getEnterpriseid());
        PaymentTradeOrderVo vo = service.findByEidAndTradeNum(bo);

        if (vo != null) {
            //营业厅变为已缴
            PaymentTradeOrderExtForm ext = new PaymentTradeOrderExtForm();
            ext.setOwnerid(autoPay.getOwnerid());
            ext.setBalance(String.valueOf(autoPay.getAmount()));
            ext.setEnterpriseid(user.getEnterpriseid());
            ext.setMode(PayConstants.PAYTYPE_MONEY);
            ext.setActualamount("0.0");
            List<String> list = new ArrayList<>();
            list.add(vo.getTradeid());
            ext.setTradeIds(list);
            Map<String, String> payMap = iPaymentFactory.pay(ext);
            if (payMap.get("status") != null && payMap.get("status").equals(PaymentConstants.STATUS_PAYED)) {
                logger.info("账单自动缴费 {} ， {}", autoPay.getUsername(), ext.getBalance());
                // 发短信
                // 金额大于0, 发通知推送
                if (autoPay.getAmount() > 0 && VerificationUtils.regex(autoPay.getPhone(), VerificationUtils.REG_PHONE)) {
                    OwnerBo ownerBo = new OwnerBo();
                    ownerBo.setUsername(autoPay.getUsername());
                    OrderBo orderBo = new OrderBo();
                    orderBo.setMonth(autoPay.getMonth());
                    orderBo.setAmount(autoPay.getAmount());
                    iPushMsgFactory.sendMsg(null, autoPay.getPhone(), 1, user.getEnterpriseid(),
                            SmsEnum.pay_fee.intValue(),
                            ownerBo, orderBo);

                    // 微信推送
                    OrderVo order = new OrderVo();
                    BeanUtils.copyProperties(autoPay, order);
                    Map<String, String> weixinparams = new HashMap<>();
                    weixinparams.put("messageState", "" + SmsEnum.pay_bill.intValue());
                    weixinparams.put("ownerid", String.valueOf(autoPay.getOwnerid()));
                    weixinparams.put("userName", autoPay.getUsername());
                    weixinparams.put("enterpriseid", String.valueOf(user.getEnterpriseid()));
                    weixinparams.put("tradeno", autoPay.getTradeno());
                    weixinparams.put("createtime", DateUtils.formatDatetime(new Date()));
                    weixinparams.put("enterpriseName", user.getEnterprise().getName());
                    weixinparams.put("tradetime", DateUtils.formatDatetime(new Date()));
                    weixinparams.put("trademoney", String.valueOf(autoPay.getAmount()));
                    iPushMsgFactory.sendMsg(weixinparams, null, 2, user.getEnterpriseid(), SmsEnum.pay_fee.intValue(),
                            order);
                }
            }
        }

    }

    @Async("batchSendmakeOrderExecutor")
    @Override
    public void batchSendmakeOrderExecutor(List<OrderPreviewVo> orders) throws FrameworkRuntimeException {
        long time = System.currentTimeMillis();

        List<String> ownerids = new ArrayList<>();
        for (OrderPreviewVo p : orders) {
            if (p.getOwnerid() != null) {
                ownerids.add(p.getOwnerid());
            }
        }

        if (ownerids == null || ownerids.size() == 0) {
            logger.error("[ownerids]生成订单没找到业主");
            return;
        }

        List<OwnerVo> ownerList = iOwnerService.getByIds(ownerids);
        if (ownerList == null || ownerList.size() == 0) {
            logger.error("[ownerList]生成订单没找到业主");
            return;
        }

        Map<String, OwnerVo> ownerMap = ownerList.stream().collect(Collectors.toMap(x -> x.getOwnerid(), x -> x));

        for (OrderPreviewVo p : orders) {
            // 金额大于0才发短信和发微信
            if (StringUtils.isNotBlank(p.getPhone()) && p.getAmount() != null && p.getAmount() > 0) {
                Object[] objects = new Object[2];
                OwnerVo owner = ownerMap.get(p.getOwnerid());
                if (owner == null) {
                    continue;
                }

                OrderVo order = new OrderVo();
                BeanUtils.copyProperties(p, order);
                objects[1] = order;
                objects[0] = owner;

                iPushMsgFactory.sendMsg(null, p.getPhone(), 1, p.getEnterpriseid(), SmsEnum.pay_bill.intValue(),
                        objects);

                // 微信推送
                Map<String, String> weixinparams = new HashMap<>();
                weixinparams.put("messageState", "" + SmsEnum.pay_bill.intValue());
                weixinparams.put("ownerid", String.valueOf(p.getOwnerid()));
                weixinparams.put("userName", p.getUsername());
                weixinparams.put("enterpriseid", String.valueOf(p.getEnterpriseid()));
                weixinparams.put("tradeno", p.getTradeno());
                weixinparams.put("money", String.valueOf(p.getAmount()));
                weixinparams.put("water", String.valueOf(p.getWater()));
                weixinparams.put("billmonth", p.getYear() + "-" + p.getMonth());
                weixinparams.put("billBeginTime", p.getUpreadtime());
                weixinparams.put("billEndTime", p.getReadtime());
                weixinparams.put("alreadypay", String.valueOf(owner.getAlreadypay()));
                iPushMsgFactory.sendMsg(weixinparams, null, 2, p.getEnterpriseid(), SmsEnum.pay_bill.intValue(),
                        objects);
            }
        }

        logger.info("[{}]异步耗时:{}", Thread.currentThread().getName(), System.currentTimeMillis() - time);
    }
}
