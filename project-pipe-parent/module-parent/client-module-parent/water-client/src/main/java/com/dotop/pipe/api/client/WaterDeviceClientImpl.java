package com.dotop.pipe.api.client;

import com.alibaba.fastjson.TypeReference;
import com.dotop.pipe.api.client.core.WaterDeviceVo;
import com.dotop.pipe.api.client.fegin.water.IWaterFeginClient;
import com.dotop.pipe.auth.cas.web.IAuthCasWeb;
import com.dotop.pipe.auth.core.vo.auth.LoginCas;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.JSONObjects;
import com.dotop.smartwater.dependence.core.utils.JSONUtils;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.module.core.water.form.DeviceForm;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class WaterDeviceClientImpl implements IWaterDeviceClient {

    private final static Logger logger = LogManager.getLogger(WaterDeviceClientImpl.class);

    @Autowired
    private IAuthCasWeb iAuthCasApi;

    @Autowired
    private IWaterFeginClient iWaterFeginClient;

    @Override
    public Pagination<WaterDeviceVo> page(Integer page, Integer pageSize, String devno, String deveui) throws FrameworkRuntimeException {
        LoginCas loginCas = iAuthCasApi.get();
        String userid = loginCas.getUserId();
        String ticket = loginCas.getTicket();
        DeviceForm deviceForm = new DeviceForm();
        deviceForm.setPage(page);
        deviceForm.setPageCount(pageSize);
        deviceForm.setDevno(devno);
        deviceForm.setDeveui(deveui);
        String result = iWaterFeginClient.devicePage(deviceForm, userid, ticket);
        JSONObjects jsonObjects = JSONUtils.parseObject(result);
        String code = jsonObjects.getString("code");
        if (!ResultCode.Success.equals(code)) {
            return new Pagination<>(page, pageSize, new ArrayList<>(), 0);
        }
        List<WaterDeviceVo> waterDevices = jsonObjects.getObject("data", new TypeReference<List<WaterDeviceVo>>() {
        });
        Pagination<WaterDeviceVo> pagination = new Pagination<>();
        pagination.setData(waterDevices);
        pagination.setTotalPageSize(jsonObjects.getLong("count"));
        logger.info(LogMsg.to("pagination", pagination));
        return pagination;
    }


}
