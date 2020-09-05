package com.dotop.smartwater.project.module.api.revenue.impl;

import java.util.Date;
import java.util.List;

import com.dotop.smartwater.project.module.api.revenue.IThirdDeviceDataPushFactory;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.utils.BeanUtils;
import com.dotop.smartwater.dependence.core.utils.DateUtils;
import com.dotop.smartwater.dependence.core.utils.JSONUtils;
import com.dotop.smartwater.project.module.core.auth.vo.UserVo;
import com.dotop.smartwater.project.module.core.water.bo.DeviceBo;
import com.dotop.smartwater.project.module.core.water.bo.DeviceUplinkBo;
import com.dotop.smartwater.project.module.core.water.constants.WaterConstants;
import com.dotop.smartwater.project.module.core.water.form.DeviceUplinkForm;
import com.dotop.smartwater.project.module.core.water.vo.DeviceVo;
import com.dotop.smartwater.project.module.service.device.IDeviceService;
import com.dotop.smartwater.project.module.service.device.IDeviceUplinkService;

/**
 * 处理第三方平台上行数据
 *
+YangKe
 */
@Component
public class ThirdDeviceDataPushFactoryImpl implements IThirdDeviceDataPushFactory {

    private final static Logger LOGGER = LogManager.getLogger(ThirdDeviceDataPushFactoryImpl.class);
    @Autowired
    private IDeviceService iDeviceService;

    @Autowired
    private IDeviceUplinkService iDeviceUplinkService;
    /**
     * 批量处理第三方平台上行数据
     *
     * @param enterpriseid
     * @param list
     */
    @Override
    @Async("calExecutor")
    public String batchUplink(List<DeviceUplinkForm> list, UserVo user) {
//    	UserVo user = AuthCasClient.getUser();
        Long start = System.currentTimeMillis();
        LOGGER.info(LogMsg.to("msg:", " 批量处理第三方平台上行数据開始", "form", list));
        if (list.size() > 0) {
            for (DeviceUplinkForm form : list) {
                try{
                    // 更新上级水表读数
                    DeviceVo vo = null;
                    if(StringUtils.isNotBlank(form.getDevid())){
                        vo =iDeviceService.findById(form.getDevid());
                    }else{
                        if(StringUtils.isNotBlank(form.getDeveui())){
                            vo =iDeviceService.findByDevEUI(form.getDeveui());
                        }
                    }
                    if(vo == null || form.getRxtime() == null){
                        continue;
                    }
                    LOGGER.info(LogMsg.to("msg:", "数据填充开始"));
                    DeviceBo bo = BeanUtils.copyProperties(vo,DeviceBo.class);
                    bo.setUplinktime(DateUtils.formatDatetime(form.getRxtime()));
                    bo.setWater(Double.parseDouble(form.getWater()));
                    bo.setEnterpriseid(user.getEnterpriseid());
                    bo.setStatus(WaterConstants.DEVICE_STATUS_ON_USE);
                    bo.setExplain("在线");
                    bo.setTapstatus(form.getTapstatus());
                    if(StringUtils.isNotBlank(form.getRssi())) {
                    	bo.setRssi(Long.parseLong(form.getRssi()));
                    }
                    bo.setLsnr(form.getLsnr());
                    bo.setIccid(form.getIccid());
                    if(StringUtils.isNotBlank(form.getBeginWater())) {
                    	bo.setBeginvalue(new Double(form.getBeginWater()));
                    }else {
                    	bo.setBeginvalue(bo.getWater());
                    }
                    LOGGER.info(LogMsg.to("msg:", "数据填充结束"));
                    LOGGER.info(LogMsg.to("msg:", "更新水表开始"));
                    iDeviceService.updateDeviceWaterV2(bo);
                    LOGGER.info(LogMsg.to("msg:", "更新水表结束"));
                    LOGGER.info(LogMsg.to("msg:", "插入上行表数据填充开始"));
                    DeviceUplinkBo deviceUplinkBo = BeanUtils.copyProperties(form, DeviceUplinkBo.class);
                    if(StringUtils.isNotBlank(form.getRssi())) {
                        deviceUplinkBo.setRssi(Long.parseLong(form.getRssi()));
                    }
                    deviceUplinkBo.setConfirmed(true);
                    deviceUplinkBo.setUplinkData(JSONUtils.toJSONString(form));
                    deviceUplinkBo.setDate(DateUtils.format(form.getRxtime(), "yyyyMM"));
                    deviceUplinkBo.setEnterpriseid(user.getEnterpriseid());
                    deviceUplinkBo.setUserBy(user.getName());
            		deviceUplinkBo.setCurr(new Date());
            		 LOGGER.info(LogMsg.to("msg:", "插入上行表数据填充结束"));
            		 LOGGER.info(LogMsg.to("msg:", "插入上行表数据开始"));
                    iDeviceUplinkService.add(deviceUplinkBo);
                    LOGGER.info(LogMsg.to("msg:", "插入上行表数据结束"));
                }catch (Exception e){
                    LOGGER.error(form.getDeveui() + "插入上行数据出错");
                }
            }
        }
        LOGGER.info(LogMsg.to("msg:", " 批量处理第三方平台上行数据結束, 耗时:" + (System.currentTimeMillis() - start)));
        return "";
    }
}
