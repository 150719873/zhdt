package com.dotop.smartwater.project.module.api.revenue.async;

import com.dotop.smartwater.dependence.cache.api.AbstractValueCache;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.utils.BeanUtils;
import com.dotop.smartwater.dependence.core.utils.DateUtils;
import com.dotop.smartwater.project.module.api.revenue.IDeleteOrderExtTaskFactory;
import com.dotop.smartwater.project.module.api.revenue.IPaymentFactory;
import com.dotop.smartwater.project.module.api.revenue.IPushFactory;
import com.dotop.smartwater.project.module.service.revenue.*;
import com.dotop.smartwater.project.module.core.auth.local.AuthCasClient;
import com.dotop.smartwater.project.module.core.auth.vo.UserVo;
import com.dotop.smartwater.project.module.core.water.bo.OrderBo;
import com.dotop.smartwater.project.module.core.water.bo.TradeDetailBo;
import com.dotop.smartwater.project.module.core.water.bo.TradeOrderBo;
import com.dotop.smartwater.project.module.core.water.constants.PaymentConstants;
import com.dotop.smartwater.project.module.core.water.constants.WaterConstants;
import com.dotop.smartwater.project.module.core.water.form.PaymentTradeOrderExtForm;
import com.dotop.smartwater.project.module.core.water.form.customize.BalanceChangeParamForm;
import com.dotop.smartwater.project.module.core.water.vo.OrderVo;
import com.dotop.smartwater.project.module.core.water.vo.PaymentTradeOrderVo;
import com.dotop.smartwater.project.module.core.water.vo.TradeOrderVo;
import com.dotop.smartwater.project.module.core.water.vo.customize.PayDetailRecord;
import com.dotop.smartwater.project.module.service.revenue.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;


/**
 * 消息处理
 */
@Component
public class PushFactoryImpl implements IPushFactory {

    private final static Logger LOGGER = LogManager.getLogger(PushFactoryImpl.class);

    @Autowired
    private IPaymentFactory iPaymentFactory;

    @Autowired
    private IPaymentTradeOrderService service;

    @Autowired
    private IOrderService iOrderService;

    @Autowired
    private ITradeOrderService iTradeOrderService;

    @Autowired
    private ITradeDetailService dtService;


    @Autowired
    protected AbstractValueCache<String> avc;

    @Autowired
    private IBillService iBillService;

    @Autowired
    private IDeleteOrderExtTaskFactory iDeleteOrderExtTaskFactory;

    /**
     * 把账单信息推送到子系统，通知子系统把账单状态改变
     * TODO 现阶段不实现http推送
     *
     * @param ext
     */
    @Override
    @Async("payPushExecutor")
    public void push(PaymentTradeOrderExtForm ext) {
        List<PaymentTradeOrderVo> list = service.findListByIds(ext.getTradeIds());
        LOGGER.info(LogMsg.to("msg:", " 把账单信息推送到子系统開始", "form", ext));
        if (list.size() > 0) {
            for (PaymentTradeOrderVo vo : list) {
                switch (vo.getBusinessId()) {
                    case PaymentConstants.BUSSINESS_TYPE_RECHARGE:
                        BalanceChangeParamForm param = new BalanceChangeParamForm();
                        param.setOwnerid(ext.getOwnerid());
                        param.setTradeNo(vo.getTradeNumber());

                        String key = param.getOwnerid() + param.getTradeNo();
                        if (avc.get(key) == null) {
                            //加锁 防止重复给账号余额充值(重要逻辑)
                            avc.set(param.getOwnerid() + param.getTradeNo(), "0", 30);
                            PayDetailRecord payDetailRecord = iBillService.findDetailRecord(param);
                            if (payDetailRecord == null) {
                                iPaymentFactory.handleRecharge(ext.getOwnerid(), ext.getAmount(), vo.getTradeNumber());
                                //解锁
                                avc.del(key);
                            }
                        }else{
                            LOGGER.info(key + " 已存在");
                        }
                        break;
                    case PaymentConstants.BUSSINESS_TYPE_WATERFEE:
                        OrderVo orderVo = iOrderService.findOrderByTradeNo(vo.getTradeNumber());
                        if (orderVo != null) {
                            OrderBo orderBo = new OrderBo();
                            orderBo.setId(orderVo.getId());
                            orderBo.setPaystatus(WaterConstants.PAY_STATUS_YES);
                            orderBo.setPaytime(DateUtils.formatDatetime(vo.getPayTime()));
                            orderBo.setPaytype(Integer.parseInt(ext.getMode()));
                            iOrderService.updateOrder(orderBo);
                        }
                        break;
                    case PaymentConstants.BUSSINESS_TYPE_REPAIR:
                        TradeOrderBo bo = new TradeOrderBo();
                        bo.setNumber(vo.getTradeNumber());
                        bo.setEnterpriseid(vo.getEnterpriseid());
                        TradeOrderVo tradeOrderVo = iTradeOrderService.get(bo);
                        if (tradeOrderVo != null) {
                            bo = BeanUtils.copy(tradeOrderVo, TradeOrderBo.class);
                            bo.setStatus(PaymentConstants.PATMENT_STATUS_PAYED);
                            bo.setPayTime(DateUtils.formatDatetime(vo.getPayTime()));
                            bo.setMode(ext.getMode());

                            TradeDetailBo tbo = new TradeDetailBo();
                            tbo.setMode(ext.getMode());
                            tbo.setNumber(vo.getTradeNumber());
                            tbo.setTradeNumber(tradeOrderVo.getTradeNumber());
                            tbo.setAmount(vo.getAmount());
                            tbo.setNetReceipts(vo.getAmount());
                            tbo.setGiveChange("0");
                            tbo.setMode(ext.getMode());
                            tbo.setStatus(PaymentConstants.PATMENT_STATUS_PAYED);
                            tbo.setEnterpriseid(vo.getEnterpriseid());
                            tbo.setCreateTime(bo.getPayTime());
                            try {
                                UserVo userVo = AuthCasClient.getUser();
                                if (userVo != null) {
                                    tbo.setOperatorName(userVo.getName());
                                    tbo.setOperatorId(userVo.getUserid());
                                    tbo.setUserBy(userVo.getName());
                                    tbo.setCurr(new Date());

                                    bo.setUserBy(userVo.getName());
                                    bo.setCurr(new Date());
                                }
                            } catch (FrameworkRuntimeException ex) {
                                LOGGER.info(LogMsg.to("msg:", " 通知子系统", "msg", ex.getMsg()));
                            }

                            iTradeOrderService.update(bo);
                            dtService.add(tbo);
                        }
                        break;
                }
            }

            /** 检测用户水表是否关阀，如果关阀则打开 **/
            iDeleteOrderExtTaskFactory.checkDeviceOpen(ext.getOwnerid());
        }
        LOGGER.info(LogMsg.to("msg:", " 把账单信息推送到子系统結束", "form", ext));
    }

}
