package com.dotop.smartwater.project.module.core.water.vo;

import com.dotop.smartwater.project.module.core.water.vo.customize.LadderPriceDetailVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class PaymentTradeOrderDetailVo extends PaymentTradeOrderVo {

    private String couponMoney;
    private String balance;
    private String actualamount;
    private String change;
    private String cost;

    /** 滞纳金 */
    private String penalty;

    /** 合并缴费时总的滞纳金 */
    private String totalPenalty;

    /** 是否合并缴费 */
    private String isMerge;
    private List<PaymentTradeOrderVo> orders;

    /** 交易流水 */
    private List<PaymentTradeProVo> proVos;

    /** 水费组成 */
    private List<LadderPriceDetailVo> details;

    /** 收费种类名称 */
    private PayTypeVo payTypeVo;
}