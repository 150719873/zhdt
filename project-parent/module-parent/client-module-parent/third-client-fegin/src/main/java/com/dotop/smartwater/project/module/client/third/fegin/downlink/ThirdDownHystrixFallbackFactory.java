package com.dotop.smartwater.project.module.client.third.fegin.downlink;

import com.dotop.smartwater.project.module.core.third.form.middleware.WaterDownLoadForm;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import feign.hystrix.FallbackFactory;

import java.util.HashMap;
import java.util.Map;

/**

 */
public class ThirdDownHystrixFallbackFactory implements FallbackFactory<IThirdDownLinkClient> {

    @Override
    public IThirdDownLinkClient create(Throwable ex) {
        return new IThirdDownLinkClient() {
            @Override
            public String downlink(
                    String enterpriseid,WaterDownLoadForm waterDownLoadForm) {
                Map<String, String> params = new HashMap<>(3);
                params.put("code", ResultCode.Fail);
                params.put("msg", "系统提交下行命令出错");
                params.put("data", null);
                return params.toString();
            }
        };
    }

}
