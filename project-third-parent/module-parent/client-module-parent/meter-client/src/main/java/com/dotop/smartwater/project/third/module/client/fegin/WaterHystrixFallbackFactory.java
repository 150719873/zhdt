package com.dotop.smartwater.project.third.module.client.fegin;

import com.dotop.smartwater.dependence.core.utils.JSONUtils;
import com.dotop.smartwater.project.module.core.third.form.middleware.WaterDownLoadForm;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.module.core.water.form.DeviceForm;
import com.dotop.smartwater.project.module.core.water.form.DeviceUplinkForm;
import com.dotop.smartwater.project.module.core.water.form.OwnerChangeForm;
import com.dotop.smartwater.project.module.core.water.form.OwnerForm;
import com.dotop.smartwater.project.third.module.core.water.bo.CommandBo;
import feign.hystrix.FallbackFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WaterHystrixFallbackFactory implements FallbackFactory<IWaterFeginClient> {

    private static final Logger LOGGER = LogManager.getLogger(WaterHystrixFallbackFactory.class);

    @Override
    public IWaterFeginClient create(Throwable ex) {
        return new IWaterFeginClient() {

            @Override
            public String ownerAdds(List<OwnerForm> owners, String ticket, String userid) {
                Map<String, String> map = new HashMap<>(1);
                map.put("code", ResultCode.Fail);
                return JSONUtils.toJSONString(map);
            }

            @Override
            public String ownerEdits(List<OwnerForm> owners, String ticket, String userid) {
                Map<String, String> map = new HashMap<>(1);
                map.put("code", ResultCode.Fail);
                return JSONUtils.toJSONString(map);
            }

            @Override
            public String ownerChanges(List<OwnerChangeForm> ownerChanges, String ticket, String userid) {
                Map<String, String> map = new HashMap<>(1);
                map.put("code", ResultCode.Fail);
                return JSONUtils.toJSONString(map);
            }

            @Override
            public String deviceAdds(List<DeviceForm> devices, String ticket, String userid) {
                Map<String, String> map = new HashMap<>(1);
                map.put("code", ResultCode.Fail);
                return JSONUtils.toJSONString(map);
            }

            @Override
            public String deviceEdits(List<DeviceForm> devices, String ticket, String userid) {
                Map<String, String> map = new HashMap<>(1);
                map.put("code", ResultCode.Fail);
                return JSONUtils.toJSONString(map);
            }

            @Override
            public String uplinks(List<DeviceUplinkForm> waters, String ticket, String enterpriseid) {
                Map<String, String> map = new HashMap<>(1);
                map.put("code", ResultCode.Fail);
                return JSONUtils.toJSONString(map);
            }

            @Override
            public String downlink(WaterDownLoadForm waterDownLoadForm, String ticket, String enterpriseid) {
                Map<String, String> map = new HashMap<>(1);
                map.put("code", ResultCode.Fail);
                return JSONUtils.toJSONString(map);
            }

            @Override
            public String downlinkEdit(CommandBo commandBo, String ticket, String enterpriseid) {
                Map<String, String> map = new HashMap<>(1);
                map.put("code", ResultCode.Fail);
                return JSONUtils.toJSONString(map);
            }

        };
    }

}
