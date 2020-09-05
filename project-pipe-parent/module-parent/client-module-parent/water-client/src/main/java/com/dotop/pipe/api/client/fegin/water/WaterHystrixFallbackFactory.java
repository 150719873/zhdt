package com.dotop.pipe.api.client.fegin.water;

import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.utils.JSONUtils;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.module.core.water.form.*;
import feign.hystrix.FallbackFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class WaterHystrixFallbackFactory implements FallbackFactory<IWaterFeginClient> {

    private static final Logger LOGGER = LogManager.getLogger(WaterHystrixFallbackFactory.class);

    @Override
    public IWaterFeginClient create(Throwable ex) {
        return new IWaterFeginClient() {
            @Override
            public String storeList(StoreProductForm storeProductForm, String userid, String ticket) {
                Map<String, String> map = new HashMap<>(1);
                map.put("code", ResultCode.Fail);
                return JSONUtils.toJSONString(map);
            }

            @Override
            public String store(StoreProductForm storeProductForm, String userid, String ticket) {
                Map<String, String> map = new HashMap<>(1);
                map.put("code", ResultCode.Fail);
                return JSONUtils.toJSONString(map);
            }

            @Override
            public String inventory(InventoryForm inventoryForm, String userid, String ticket) {
                Map<String, String> map = new HashMap<>(1);
                map.put("code", ResultCode.Fail);
                return JSONUtils.toJSONString(map);
            }

            @Override
            public String dictList(DictionaryForm dictionaryForm, String userid, String ticket) throws FrameworkRuntimeException {
                Map<String, String> map = new HashMap<>(1);
                map.put("code", ResultCode.Fail);
                return JSONUtils.toJSONString(map);
            }

            @Override
            public String addNotice(NoticeForm noticeForm) throws FrameworkRuntimeException {
                Map<String, String> map = new HashMap<>(1);
                map.put("code", ResultCode.Fail);
                return JSONUtils.toJSONString(map);
            }

            @Override
            public String devicePage(DeviceForm device, String userid, String ticket) throws FrameworkRuntimeException {
                Map<String, String> map = new HashMap<>(1);
                map.put("code", ResultCode.Fail);
                return JSONUtils.toJSONString(map);
            }

            @Override
            public String deviceSubscribeBind(DeviceForm device) throws FrameworkRuntimeException {
                Map<String, String> map = new HashMap<>(1);
                map.put("code", ResultCode.Fail);
                /**
                 * 该水表已订阅，不要重复订阅
                 **/
               // public static final String DEVICE_SUBSCRIBE = "1001";
                /**
                 * 水务系统没有该水表
                 **/
               // public static final String DEVICE_NOT_EXIST = "1002";
                /**
                 * 输入的水司ID和水务系统绑定的不匹配
                 **/
              //  public static final String DEVICE_MISMATCH = "1003";

                return JSONUtils.toJSONString(map);
            }

            @Override
            public String deviceSubscribeDel(DeviceForm device) throws FrameworkRuntimeException {
                Map<String, String> map = new HashMap<>(1);
                map.put("code", ResultCode.Fail);
                return JSONUtils.toJSONString(map);
            }
        };
    }

}
