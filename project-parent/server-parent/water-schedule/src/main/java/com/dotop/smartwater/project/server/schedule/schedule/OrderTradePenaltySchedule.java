package com.dotop.smartwater.project.server.schedule.schedule;

import java.util.Date;
import java.util.List;

import com.dotop.smartwater.project.module.api.revenue.IDeleteOrderExtTaskFactory;
import com.dotop.smartwater.project.module.service.tool.ISettlementService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.utils.DateUtils;
import com.dotop.smartwater.dependence.core.utils.JSONUtils;
import com.dotop.smartwater.project.module.core.water.bo.PaymentTradeOrderBo;
import com.dotop.smartwater.project.module.core.water.bo.PaymentTradeOrderExtBo;
import com.dotop.smartwater.project.module.core.water.constants.PaymentConstants;
import com.dotop.smartwater.project.module.core.water.utils.CalUtil;
import com.dotop.smartwater.project.module.core.water.vo.OrderExtVo;
import com.dotop.smartwater.project.module.core.water.vo.PayTypeVo;
import com.dotop.smartwater.project.module.core.water.vo.PaymentTradeOrderExtVo;
import com.dotop.smartwater.project.module.core.water.vo.PaymentTradeOrderVo;
import com.dotop.smartwater.project.module.service.revenue.IOrderService;
import com.dotop.smartwater.project.module.service.revenue.IPaymentTradeOrderExtService;
import com.dotop.smartwater.project.module.service.revenue.IPaymentTradeOrderService;

/**
 * 每天计算滞纳金
 *

 * @date 2019年8月19日
 */
public class OrderTradePenaltySchedule {

    private static final Logger LOGGER = LogManager.getLogger(OrderTradePenaltySchedule.class);


    @Autowired
    private IPaymentTradeOrderService service;

    @Autowired
    private IOrderService iOrderService;

    @Autowired
    private IPaymentTradeOrderExtService extService;

    @Autowired
    private IDeleteOrderExtTaskFactory iDeleteOrderExtTaskFactory;

    @Autowired
    private ISettlementService iSettlementService;

    /***是否启用滞纳金（1-启动 0-停用）
     * 1-按日加收 0-只计算一次
     * */
    private final static String Open = "1";
    private final static String Close = "0";

    /**
     * 每天凌晨两点执行
     */
    //@Scheduled(initialDelay = 10000, fixedRate = 3000000)
    @Scheduled(cron = "0 0 2 * * ?")
    public void init() {
        LOGGER.info("开始计算滞纳金.....");
        List<PaymentTradeOrderVo> list;
        PaymentTradeOrderBo paymentTradeOrderBo = new PaymentTradeOrderBo();
        paymentTradeOrderBo.setStatus(PaymentConstants.PATMENT_STATUS_NOPAY);
        paymentTradeOrderBo.setBusinessId(PaymentConstants.BUSSINESS_TYPE_WATERFEE);
        list = service.list(paymentTradeOrderBo);
        LOGGER.info("共[" + list.size() + "]个未缴账单...");
        if (list.size() > 0) {
            Date date = new Date();
            for (PaymentTradeOrderVo vo : list) {
                OrderExtVo orderExt = iOrderService.findOrderExtByTradeno(vo.getTradeNumber());
                if (orderExt == null || orderExt.getPaytypeinfo() == null) {
                    continue;
                }
                PayTypeVo payTypeVo;
                try {
                    payTypeVo = JSONUtils.parseObject(orderExt.getPaytypeinfo(), PayTypeVo.class);
                } catch (FrameworkRuntimeException ex) {
                    LOGGER.error("OrderTradePenaltySchedule JSON解析出错");
                    continue;
                }
                if (payTypeVo == null) {
                    continue;
                }
                if (payTypeVo.getIsLatefee() == null || Close.equals(payTypeVo.getIsLatefee())) {
                    continue;
                }
                calPenalty(vo, payTypeVo, date);

                //处理欠费自动关阀
               /* SettlementVo settlementVo = iSettlementService.getSettlement(vo.getEnterpriseid());
                if(settlementVo != null){
                    iDeleteOrderExtTaskFactory.sendArrearsCloseNotice(vo.getUserid(), settlementVo);
                }*/
            }
        } else {
            LOGGER.info("无计算滞纳金，结束定时任务");
        }
    }

    private void calPenalty(PaymentTradeOrderVo vo, PayTypeVo payTypeVo, Date date) {
        //开启和大于逾期天数才算滞纳金
        int days = DateUtils.daysBetween(vo.getCreateDate(), date);
        if (days > payTypeVo.getOverdueday()) {
            //滞纳金比例
            if (payTypeVo.getRatio() == null || Double.parseDouble(payTypeVo.getRatio()) <= 0) {
                return;
            }

            PaymentTradeOrderExtVo extVo = extService.findByTradeid(vo.getTradeid());
            if (extVo == null) {
                return;
            }

            Double money;
            //按日加收
            if (Open.equals(payTypeVo.getIncrease())) {
                Double penalty = vo.getPenalty() == null ? 0.0 : Double.parseDouble(vo.getPenalty());

                //计算滞纳金
                Double currPenalty = CalUtil.mul(vo.getAmount(), payTypeVo.getRatio()).doubleValue();
                currPenalty = CalUtil.div(currPenalty, 100);
                money = CalUtil.add(penalty, currPenalty);
            }
            //不按日加收
            else {
                //没计算则计算,计算了则不用更新
                Double penalty = vo.getPenalty() == null ? 0.0 : Double.parseDouble(vo.getPenalty());
                if (penalty > 0) {
                    return;
                }
                //计算滞纳金
                Double currPenalty = CalUtil.mul(vo.getAmount(), payTypeVo.getRatio()).doubleValue();
                money = CalUtil.div(currPenalty, 100, 2);	
            }
            PaymentTradeOrderExtBo bo = new PaymentTradeOrderExtBo();
            bo.setPenalty(String.valueOf(money));
            bo.setId(extVo.getId());
            extService.edit(bo);
            LOGGER.info("账单号 [{}] 计算滞纳金完成,加收滞纳金 {} 元", vo.getTradeNumber(), money);
        }
    }
}
