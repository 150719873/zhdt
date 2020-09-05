package com.dotop.smartwater.project.module.api.mode.impl;

import com.alibaba.fastjson.JSON;
import com.dotop.smartwater.dependence.cache.StringValueCache;
import com.dotop.smartwater.dependence.core.utils.JSONUtils;
import com.dotop.smartwater.project.module.api.mode.inf.DongXinInf;
import com.dotop.smartwater.project.module.client.third.http.IHttpTransfer;
import com.dotop.smartwater.project.module.core.auth.bo.UserLoraBo;
import com.dotop.smartwater.project.module.core.auth.vo.UserLoraVo;
import com.dotop.smartwater.project.module.core.water.bo.DeviceBo;
import com.dotop.smartwater.project.module.core.water.bo.DeviceDownlinkBo;
import com.dotop.smartwater.project.module.core.water.bo.customize.DownLinkDataBo;
import com.dotop.smartwater.project.module.core.water.constants.*;
import com.dotop.smartwater.project.module.core.water.vo.DeviceVo;
import com.dotop.smartwater.project.module.core.water.vo.customize.IotMsgEntityVo;
import com.dotop.smartwater.project.module.service.device.IDeviceDownlinkService;
import com.dotop.smartwater.project.module.service.tool.IUserLoraService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: project-parent
 * @description: 东信下发实现类

 * @create: 2019-10-15 14:37
 **/
@Component
public class DongXinFactoryImpl implements DongXinInf {

    private static final Logger LOGGER = LogManager.getLogger(DongXinFactoryImpl.class);

    @Autowired
    private IDeviceDownlinkService iDeviceDownlinkService;

    @Autowired
    private IUserLoraService iUserLoraService;

    @Autowired
    private IHttpTransfer iHttpTransfer;

    @Autowired
    private StringValueCache svc;

    @Override
    public Map<String, String> deviceDownLink(DeviceVo deviceVo, int command, String value, Integer expire,
                                              DownLinkDataBo downLinkDataBo) {
        Map<String, String> result = new HashMap<>(16);
        UserLoraVo u = iUserLoraService.findByEnterpriseId(deviceVo.getEnterpriseid());
        if(u == null){
            result.put(ModeConstants.RESULT_CODE, ResultCode.Fail);
            result.put(ModeConstants.RESULT_MSG, "请先配置IOT账号");
            return result;
        }

        String token = svc.get(CacheKey.WaterIotToken + u.getEnterpriseid());
        UserLoraBo userLoraBo = new UserLoraBo();
        BeanUtils.copyProperties(u, userLoraBo);
        DeviceBo deviceBo = new DeviceBo();
        BeanUtils.copyProperties(deviceVo, deviceBo);
        IotMsgEntityVo authResult;
        if (token == null) {
            if (u.getAccount() != null && u.getPassword() != null) {
                authResult = iHttpTransfer.getLoginInfo(userLoraBo);
            } else {
                LOGGER.info("水司账号没有关联lora核心网");
                result.put(ModeConstants.RESULT_CODE, ResultCode.Fail);
                result.put(ModeConstants.RESULT_MSG, "没在系统设置水司关联的IOT账号");
                return result;
            }
            if (authResult.getCode().equals(AppCode.IotSucceccCode)) {
                Map map = (Map) JSON.parse(authResult.getData().toString());
                token = (String) (map.get("token"));
                LOGGER.info("get token success !");
                svc.set(CacheKey.WaterIotToken + u.getEnterpriseid(), token, 1800L);
            } else {
                LOGGER.info("get token error: {}", authResult.getMsg());
                result.put(ModeConstants.RESULT_CODE, ResultCode.Fail);
                result.put(ModeConstants.RESULT_MSG, "获取token出错: " + authResult.getMsg());
                return result;
            }
        }

        DeviceDownlinkBo downlinkData = new DeviceDownlinkBo();
        IotMsgEntityVo downlinkResult;
        if (command != 0) {
            String orderNum = null;
            downlinkResult = iHttpTransfer.sendDownLoadRequest(expire, deviceBo, command, value, token, userLoraBo,
                    downLinkDataBo);

            LOGGER.info("调用IOT平台命令下发返回:" + JSONUtils.toJSONString(downlinkResult));

            if (downlinkResult != null) {
                if (downlinkResult.getCode().equals(AppCode.IotSucceccCode)) {
                    LOGGER.info("下发成功");
                    Map map = (Map) JSON.parse(downlinkResult.getData().toString());
                    orderNum = (String) (map.get("orderNum"));
                    result.put(ModeConstants.RESULT_CODE, ResultCode.Success);
                    result.put(ModeConstants.RESULT_MSG, "数据下发成功!");
                    result.put(ModeConstants.RESULT_CLIENT_ID, orderNum);

                } else {
                    LOGGER.info(downlinkResult.getCode());
                    result.put(ModeConstants.RESULT_CODE, ResultCode.Fail);
                    result.put(ModeConstants.RESULT_MSG, downlinkResult.getCode());
                    return result;
                }
            }
            // 把下行数据插入数据库
            downlinkData.setTagid(null);
            downlinkData.setTagvalue(null);
            downlinkData.setClientid(orderNum);
            downlinkData.setDevid(deviceVo.getDevid());
            downlinkData.setDownlinkdata(String.valueOf(command));
            downlinkData.setGentime(new Date());
            downlinkData.setRx2(false);
            downlinkData.setStatus(0);
            downlinkData.setDeveui(deviceVo.getDeveui());
            downlinkData.setDevno(deviceVo.getDevno());
            downlinkData.setEnterpriseid(deviceVo.getEnterpriseid());
            packageData(expire, downlinkData, downLinkDataBo);
            iDeviceDownlinkService.add(downlinkData);
            //todo 上面的service实现层没有将下行数据放入数据库中
        }
        return result;
    }

    private void packageData(Integer expire, DeviceDownlinkBo downlinkData, DownLinkDataBo downLinkDataBo) {
        if (downLinkDataBo != null) {
            downlinkData.setReqData(JSONUtils.toJSONString(downLinkDataBo));
            downlinkData.setMeasurementMethods(downLinkDataBo.getMeasureMethod());
            downlinkData.setMeasurementType(downLinkDataBo.getMeasureType());
            downlinkData.setMeasurementUnit(downLinkDataBo.getMeasureUnit());
            downlinkData.setMeasurementValue(downLinkDataBo.getMeasureValue());
            downlinkData.setNetworkInterval(downLinkDataBo.getNetworkInterval());
            downlinkData.setReason(downLinkDataBo.getReason());
            downlinkData.setPeriod(downLinkDataBo.getPeriod());
            downlinkData.setLife(downLinkDataBo.getLife());
            downlinkData.setExpire(expire != null ? String.valueOf(expire) : null);
        }
    }
}
