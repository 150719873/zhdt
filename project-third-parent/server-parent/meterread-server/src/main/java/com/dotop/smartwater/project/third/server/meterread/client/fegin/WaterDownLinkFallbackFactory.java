package com.dotop.smartwater.project.third.server.meterread.client.fegin;

import com.dotop.smartwater.project.third.server.meterread.core.third.WaterDownLoadForm;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.utils.JSONUtils;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import feign.hystrix.FallbackFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;


public class WaterDownLinkFallbackFactory implements FallbackFactory<IWaterDownLinkClient> {

    private static final Logger LOGGER = LogManager.getLogger(WaterDownLinkFallbackFactory.class);

    @Override
    public IWaterDownLinkClient create(Throwable ex) {
        return new IWaterDownLinkClient() {
            //            @Override
//            public String downlink(OperationBo operationBo) {
//                LOGGER.error(LogMsg.to("ex", ex));
//                LOGGER.error(LogMsg.to("operationBo", operationBo));
//                Map<Object, Object> params = new HashMap(2);
//                params.put("code", ResultCode.Fail);
//                params.put("msg", "timeout_fail");
//                return JSONUtils.toJSONString(params);
//            }
            @Override
            public String downlink(WaterDownLoadForm form, String ticket, String userid) {
                LOGGER.error(LogMsg.to("ex", ex));
                LOGGER.error(LogMsg.to("form", form, "ticket", ticket, "userid", userid));
                Map<Object, Object> params = new HashMap(2);
                params.put("code", ResultCode.Fail);
                params.put("msg", "timeout_fail");
                return JSONUtils.toJSONString(params);
            }
        };
    }
}
