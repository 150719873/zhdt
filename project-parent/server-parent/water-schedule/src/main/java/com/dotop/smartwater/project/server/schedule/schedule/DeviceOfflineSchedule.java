package com.dotop.smartwater.project.server.schedule.schedule;

import com.dotop.smartwater.dependence.core.utils.DateUtils;
import com.dotop.smartwater.project.module.core.auth.vo.SettlementVo;
import com.dotop.smartwater.project.module.core.water.vo.DeviceVo;
import com.dotop.smartwater.project.module.service.device.IDeviceService;
import com.dotop.smartwater.project.module.service.tool.ISettlementService;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 自动检测设备离线状态
 *

 * @date 2018年6月25日
 */
// @Component
public class DeviceOfflineSchedule {

    private static final Logger LOGGER = LogManager.getLogger(DeviceOfflineSchedule.class);

    @Autowired
    private IDeviceService iDeviceService;

    @Autowired
    private ISettlementService iSettlementService;

    @Scheduled(initialDelay = 10000, fixedRate = 3600000)
    public void init() {
        LOGGER.info("开始获取所有水司配置信息.....");
        List<SettlementVo> list = new ArrayList<>();
        list = iSettlementService.getSettlements();
        LOGGER.info("共[" + list.size() + "]个水司信息...");
        if (list.size() > 0) {
            for (SettlementVo sl : list) {
                if (sl.getOffday() > 0) {
                    LOGGER.info("开始检测水司[" + sl.getEnterpriseid() + "]下超过[" + sl.getOffday() + "]天未上报数据的水表.....");
                    LOGGER.info("开始获取水司下所有设备信息....");
                    List<DeviceVo> devices = iDeviceService.getDevices(sl.getEnterpriseid());
                    setDeviceOffline(devices, sl);
                } else {
                    LOGGER.info("水司[" + sl.getEnterpriseid() + "]下未设置水表上报时间间隔");
                }
            }
        } else {
            LOGGER.info("无水司信息，结束定时任务");
        }
    }

    public void setDeviceOffline(List<DeviceVo> devices, SettlementVo sl) {
        if (devices.size() > 0) {
            Date now = new Date();
            for (DeviceVo dev : devices) {
                // 如果上报时间大于设定时间，则修改设备状态为在线
                if (StringUtils.isBlank(dev.getUplinktime())) {
                    continue;
                }
                Date date = DateUtils.day(DateUtils.parseDatetime(dev.getUplinktime()), sl.getOffday());
                if (DateUtils.compare(date, now)) {
                    dev.setStatus(0);
                    dev.setExplain("在线");
                } else {
                    dev.setStatus(2);
                    dev.setExplain("设备超过[" + sl.getOffday() + "]天未上报数据");
                }
            }
            iDeviceService.updateBatchDeviceStatus(devices);
        } else {
            LOGGER.info("水司下无设备信息");
        }
    }

}
