package com.dotop.smartwater.project.third.server.meterread.client.fegin;

import com.dotop.smartwater.project.third.server.meterread.core.third.WaterDownLoadForm;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.global.GlobalContext;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "water-server", fallbackFactory = WaterDownLinkFallbackFactory.class, path = "/water-server/")
public interface IWaterDownLinkClient {

//    @PostMapping(value = "/third/downlink", produces = GlobalContext.PRODUCES)
//    String downlink(@RequestBody OperationBo operationBo) throws FrameworkRuntimeException;

    @PostMapping(value = "/device/third/downlink", produces = GlobalContext.PRODUCES)
    String downlink(@RequestBody WaterDownLoadForm form, @RequestHeader("ticket") String ticket, @RequestHeader("userid") String userid) throws FrameworkRuntimeException;
}
