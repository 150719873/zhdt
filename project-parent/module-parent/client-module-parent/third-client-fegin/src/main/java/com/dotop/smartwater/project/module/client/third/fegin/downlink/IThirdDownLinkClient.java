package com.dotop.smartwater.project.module.client.third.fegin.downlink;

import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.project.module.core.third.form.middleware.WaterDownLoadForm;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

/**

 */
@FeignClient(name = "meter-server", fallbackFactory = ThirdDownHystrixFallbackFactory.class, path = "/meter-server/")
public interface IThirdDownLinkClient {

	/**
	 * 提交订单
	 * @param enterpriseid 水司ID
	 * @param waterDownLoadForm 类
	 * @return
	 */
	@PostMapping(value = "/water/downlink", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
	String downlink(@RequestHeader("enterpriseid")String enterpriseid,
								 @RequestBody WaterDownLoadForm waterDownLoadForm);

}
