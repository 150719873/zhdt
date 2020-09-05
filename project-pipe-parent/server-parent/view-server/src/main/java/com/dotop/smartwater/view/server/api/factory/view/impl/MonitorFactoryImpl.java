package com.dotop.smartwater.view.server.api.factory.view.impl;

import com.dotop.smartwater.view.server.core.enums.SecurityEnum;
import com.dotop.smartwater.view.server.service.monitor.IMonitorService;
import com.dotop.pipe.auth.cas.web.IAuthCasWeb;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.DateUtils;
import com.dotop.smartwater.dependence.core.utils.UuidUtils;
import com.dotop.smartwater.view.server.api.factory.view.IMonitorFactory;
import com.dotop.smartwater.view.server.core.device.form.DeviceForm;
import com.dotop.smartwater.view.server.core.device.vo.DeviceVo;
import com.dotop.smartwater.view.server.core.monitor.form.LiquidLevelForm;
import com.dotop.smartwater.view.server.core.monitor.form.PondAlarmForm;
import com.dotop.smartwater.view.server.core.monitor.vo.LiquidLevelVo;
import com.dotop.smartwater.view.server.core.monitor.vo.PondAlarmVo;
import com.dotop.smartwater.view.server.service.device.IDeviceService;
import com.dotop.smartwater.view.server.utils.CalculationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

import static com.dotop.smartwater.dependence.core.common.RootModel.NOT_DEL;
import static com.dotop.smartwater.view.server.constants.ViewConstants.WATER_FACTORY;

@Component
public class MonitorFactoryImpl implements IMonitorFactory {

    @Autowired
    private IAuthCasWeb iAuthCasWeb;

    @Autowired
    private IMonitorService iMonitorService;

    @Autowired
    private IDeviceService iDeviceService;

    @Override
    public Pagination<LiquidLevelVo> liquidPage(LiquidLevelForm liquidLevelForm) throws FrameworkRuntimeException {
        String enterpriseId = iAuthCasWeb.get().getEnterpriseId();
        liquidLevelForm.setEnterpriseId(enterpriseId);
        Pagination<LiquidLevelVo> list = iMonitorService.liquidPage(liquidLevelForm);
        return list;
    }

    @Override
    public Pagination<PondAlarmVo> pondAlarmPage(PondAlarmForm pondAlarmForm) throws FrameworkRuntimeException {
        String enterpriseId = iAuthCasWeb.get().getEnterpriseId();
        pondAlarmForm.setEnterpriseId(enterpriseId);
        Pagination<PondAlarmVo> list = iMonitorService.pondAlarmPage(pondAlarmForm);
        return list;
    }

    @Override
    public String updateTaskLiquid(String enterpriseId) throws FrameworkRuntimeException {
        Date curr = new Date();
        Random random = new Random();
        SecurityEnum[] values = SecurityEnum.values();
        DeviceForm deviceForm = new DeviceForm();
        deviceForm.setEnterpriseId(enterpriseId);
        deviceForm.setProductCategory(WATER_FACTORY);
        deviceForm.setIsDel(NOT_DEL);
        List<DeviceVo> waterFactoryList = iDeviceService.list(deviceForm);
        List<LiquidLevelForm> list = new ArrayList<>();

        for (DeviceVo deviceVo : waterFactoryList) {
            double d = random.nextDouble() * values.length;
            // 随机取一个地点
            LiquidLevelForm securityLogForm = new LiquidLevelForm();
            securityLogForm.setId(UuidUtils.getUuid());
            securityLogForm.setPond(values[(int) d].getParent());
            securityLogForm.setEnterpriseId(enterpriseId);
            Date temp = DateUtils.hour(curr, -1);
            long time = CalculationUtils.randomDate(temp.getTime(), curr.getTime());
            securityLogForm.setCurr(new Date(time));
            securityLogForm.setUserBy("system");
            // 液位
            double a = 1 + random.nextDouble() * 4;
            securityLogForm.setVal(String.valueOf(a).substring(0, 4));
            securityLogForm.setStatus(a >= 4.8 ? "1" : "0");
            securityLogForm.setEnterpriseId(enterpriseId);
            securityLogForm.setFacilityId(deviceVo.getDeviceId());
            list.add(securityLogForm);
        }
        this.iMonitorService.addLiquidLists(list);
        return null;
    }

    @Override
    public String updateTaskPondALarm(String enterpriseId) throws FrameworkRuntimeException {
        Date curr = new Date();
        Random random = new Random();
        List<String> typeList = Arrays.asList("压力计", "流量计", "水质计", "液位计");
        DeviceForm deviceForm = new DeviceForm();
        deviceForm.setEnterpriseId(enterpriseId);
        deviceForm.setProductCategory(WATER_FACTORY);
        deviceForm.setIsDel(NOT_DEL);
        List<DeviceVo> waterFactoryList = iDeviceService.list(deviceForm);
        List<PondAlarmForm> list = new ArrayList<>();

        for (DeviceVo deviceVo : waterFactoryList) {
            // 随机数据
            double d = random.nextDouble() * typeList.size();
            SecurityEnum[] values = SecurityEnum.values();
            double a = random.nextDouble() * values.length;
            SecurityEnum securityEnum = SecurityEnum.values()[(int) a];

            PondAlarmForm securitySwitchForm = new PondAlarmForm();
            securitySwitchForm.setId(UuidUtils.getUuid());
            securitySwitchForm.setUserBy("system");

            Date temp = DateUtils.hour(curr, -1);
            long time = CalculationUtils.randomDate(temp.getTime(), curr.getTime());

            securitySwitchForm.setCurr(new Date(time));
            securitySwitchForm.setType(typeList.get((int) d));
            // 产生0 或1 随机数 百分之80 的概率 是正常的
            securitySwitchForm.setStatus(random.nextDouble() > 0.2 ? "0" : "1");
            securitySwitchForm.setFacilityId(deviceVo.getDeviceId());
            securitySwitchForm.setEnterpriseId(enterpriseId);
            securitySwitchForm.setPond(securityEnum.getParent());
            list.add(securitySwitchForm);
        }
        this.iMonitorService.addPondALarmLists(list);
        return null;
    }

}