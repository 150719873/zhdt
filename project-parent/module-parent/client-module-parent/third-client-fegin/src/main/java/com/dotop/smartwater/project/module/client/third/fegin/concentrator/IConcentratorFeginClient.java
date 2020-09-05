package com.dotop.smartwater.project.module.client.third.fegin.concentrator;

import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.project.module.core.third.vo.concentrator.ConcentratorDeviceVo;
import com.dotop.smartwater.project.module.core.water.bo.DeviceBo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 第三方调用获取水表、集中器、采集器信息获取
 *

 */
@FeignClient(name = "concentrator-server", fallbackFactory = ConcentratorHystrixFallbackFactory.class, path = "/concentrator-server/")
public interface IConcentratorFeginClient {

    /**
     * 删除
     * 参数：devid、enterpriseid
     */
    @PostMapping(value = "/third/device/del", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    void del(@RequestBody DeviceBo device) throws FrameworkRuntimeException;

    /**
     * 水表、集中器、采集器信息获取
     * 参数：devid、enterpriseid
     */
    @PostMapping(value = "/third/device/get", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    ConcentratorDeviceVo get(@RequestBody DeviceBo device) throws FrameworkRuntimeException;

}
