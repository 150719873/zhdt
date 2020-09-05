package com.dotop.smartwater.project.module.client.third.fegin.pay;

import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.project.module.core.third.bo.pay.PaymentOrderBo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**

 */
@FeignClient(name = "water-pay", fallbackFactory = PayHystrixFallbackFactory.class, path = "/water-pay/")
public interface IPayFeginClient {

	/**
	 * 提交订单
	 * @param paymentOrderBo 订单
	 * @return
	 */
	@PostMapping(value = "/pay/submit", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
	String submit(@RequestBody PaymentOrderBo paymentOrderBo);

	/**
	 * 撤销订单
	 * @param paymentOrderBo 订单
	 * @return
	 */
	@PostMapping(value = "/pay/ordercancel", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
	String ordercancel(@RequestBody PaymentOrderBo paymentOrderBo);

	/**
	 * 查询订单
	 * @param paymentOrderBo 订单
	 * @return
	 */
	@PostMapping(value = "/pay/orderquery", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
	String orderquery(@RequestBody PaymentOrderBo paymentOrderBo);
}
