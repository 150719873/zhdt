package com.dotop.smartwater.project.module.api.mode.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.utils.JSONUtils;
import com.dotop.smartwater.project.module.api.mode.inf.HatInf;
import com.dotop.smartwater.project.module.client.third.fegin.downlink.IThirdDownLinkClient;
import com.dotop.smartwater.project.module.core.third.form.middleware.WaterDownLoadForm;
import com.dotop.smartwater.project.module.core.water.bo.DeviceDownlinkBo;
import com.dotop.smartwater.project.module.core.water.constants.ModeConstants;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.module.core.water.constants.TxCode;
import com.dotop.smartwater.project.module.core.water.vo.DeviceVo;
import com.dotop.smartwater.project.module.service.device.IDeviceDownlinkService;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Map;

/**
 * @program: project-parent
 * @description: 水务系统调用中间系统的下发接口

 * @create: 2019-10-15 14:37
 **/
@Component

public class HatFactoryImpl implements HatInf {

    private static final Logger LOGGER = LogManager.getLogger(HatFactoryImpl.class);
    @Autowired
    private IDeviceDownlinkService iDeviceDownlinkService;
    @Resource
    private IThirdDownLinkClient iThirdDownLinkClient;

    @Override
    public Map<String, String> deviceDownLink(DeviceVo deviceVo, int command, String mode, String value) {
        WaterDownLoadForm waterDownLoadForm = new WaterDownLoadForm();
        waterDownLoadForm.setCommand(command);
        switch (command) {
            //TODO 上报时间设置
            case TxCode.SetUploadTime:
                if(StringUtils.isBlank(value)){
                    throw new FrameworkRuntimeException(ResultCode.Fail, "请输入上报时间");
                }
                waterDownLoadForm.setCycle(value);
                break;
            case TxCode.MeterOper:
                if(StringUtils.isBlank(value)){
                    throw new FrameworkRuntimeException(ResultCode.Fail, "请输入下发设置的水量");
                }
                waterDownLoadForm.setWater(value);
                break;
            case TxCode.OpenCommand:
            case TxCode.CloseCommand:
            case TxCode.GetWaterCommand:
                break;
            default:
                throw new FrameworkRuntimeException(ResultCode.Fail, "华奥通没接入的下发命令");
        }

        waterDownLoadForm.setDevid(deviceVo.getDevid());
        waterDownLoadForm.setEnterpriseid(deviceVo.getEnterpriseid());
        String resultStr = iThirdDownLinkClient.downlink(deviceVo.getEnterpriseid(), waterDownLoadForm);
        Map<String, String> result = JSONUtils.parseObject(resultStr, new TypeReference<Map<String, String>>(){});

        LOGGER.info("调用中间平台命令下发返回:" + JSONUtils.toJSONString(result));

        //插入下行记录表
        if (result.size() != 0 && ResultCode.Success.equals(result.get(ModeConstants.RESULT_CODE))
                &&StringUtils.isNotBlank(result.get(ModeConstants.RESULT_DATA))){
            Map map = (Map) JSON.parse(result.get(ModeConstants.RESULT_DATA));
            String clientid = (String) (map.get(ModeConstants.RESULT_CLIENT_ID));
            if(StringUtils.isNotBlank(clientid)){
                DeviceDownlinkBo downlinkData = new DeviceDownlinkBo();
                downlinkData.setClientid(clientid);
                downlinkData.setDevid(deviceVo.getDevid());
                downlinkData.setDownlinkdata(String.valueOf(command));
                downlinkData.setGentime(new Date());
                downlinkData.setRx2(false);
                downlinkData.setStatus(0);
                downlinkData.setDeveui(deviceVo.getDeveui());
                downlinkData.setDevno(deviceVo.getDevno());
                downlinkData.setEnterpriseid(deviceVo.getEnterpriseid());
                downlinkData.setExpire(null);
                iDeviceDownlinkService.add(downlinkData);
            }
        }
        return result;
    }
}
