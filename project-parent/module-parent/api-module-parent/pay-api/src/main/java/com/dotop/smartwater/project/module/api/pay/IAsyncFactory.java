package com.dotop.smartwater.project.module.api.pay;
import com.dotop.smartwater.project.module.core.pay.bo.PaymentOrderBo;
import com.dotop.smartwater.project.module.core.pay.bo.PaymentResultBo;

import java.util.Map;

/**

 */
public interface IAsyncFactory {
	void payByMoney(PaymentOrderBo paymentOrderBo);
	void payByCard(PaymentOrderBo paymentOrderBo,String realIP);
	void handleCallBack(PaymentResultBo paymentResultBo, Map<String, String> resultMap);
}
