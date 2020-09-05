package com.dotop.smartwater.project.module.core.water.model;

import com.dotop.smartwater.project.module.core.water.form.PaymentTradeOrderExtForm;
import com.dotop.smartwater.project.module.core.water.form.PaymentTradeOrderExtForm;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @program: project-parent
 * @description: 支付回调参数

 * @create: 2019-08-09 09:38
 **/
@Data
@EqualsAndHashCode(callSuper = false)
public class PayCallBack {
    List<String> proIds;
    List<String> resIds;
    PaymentTradeOrderExtForm ext;
}
