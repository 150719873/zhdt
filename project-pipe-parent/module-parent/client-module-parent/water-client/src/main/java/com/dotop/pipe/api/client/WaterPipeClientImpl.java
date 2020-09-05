package com.dotop.pipe.api.client;

import com.dotop.pipe.api.client.fegin.water.IWaterFeginClient;
import com.dotop.pipe.auth.cas.web.IAuthCasWeb;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.utils.JSONObjects;
import com.dotop.smartwater.dependence.core.utils.JSONUtils;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.module.core.water.form.DeviceForm;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WaterPipeClientImpl implements IWaterPipeClient {

    private final static Logger logger = LogManager.getLogger(WaterPipeClientImpl.class);

    @Autowired
    private IAuthCasWeb iAuthCasApi;

    @Autowired
    private IWaterFeginClient iWaterFeginClient;


    @Override
    public String deviceSubscribeBind(String enterpriseid, String devno) throws FrameworkRuntimeException {
        DeviceForm deviceForm = new DeviceForm();
        deviceForm.setEnterpriseid(enterpriseid);
        deviceForm.setDevno(devno);
        String result = iWaterFeginClient.deviceSubscribeBind(deviceForm);
        JSONObjects jsonObjects = JSONUtils.parseObject(result);
        String code = jsonObjects.getString("code");
        if (!ResultCode.Success.equals(code)) {
            // TODO 临时写法
            if ("1001".equals(code)) {
                throw new FrameworkRuntimeException(code, "该水表已订阅，不要重复订阅");
            } else if ("1002".equals(code)) {
                throw new FrameworkRuntimeException(code, "水务系统没有该水表");
            } else if ("1003".equals(code)) {
                throw new FrameworkRuntimeException(code, "输入的水司和水务系统绑定的不匹配");
            }
            throw new FrameworkRuntimeException(code, "请求异常，请稍后再试");
        }
        return null;
    }

    @Override
    public String deviceSubscribeDel(String enterpriseid, String devno) throws FrameworkRuntimeException {
        DeviceForm deviceForm = new DeviceForm();
        deviceForm.setEnterpriseid(enterpriseid);
        deviceForm.setDevno(devno);
        String result = iWaterFeginClient.deviceSubscribeDel(deviceForm);
        JSONObjects jsonObjects = JSONUtils.parseObject(result);
        String code = jsonObjects.getString("code");
        if (!ResultCode.Success.equals(code)) {
            throw new FrameworkRuntimeException(code, "请求异常，请稍后再试");
        }
        return null;
    }
}
