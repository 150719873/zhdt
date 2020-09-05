package com.dotop.pipe.web.factory.report;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.dotop.pipe.web.api.factory.report.IDeviceAnalysisFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dotop.pipe.api.service.alarm.IAlarmService;
import com.dotop.pipe.api.service.device.IDeviceService;
import com.dotop.pipe.auth.cas.web.IAuthCasWeb;
import com.dotop.pipe.auth.core.vo.auth.LoginCas;
import com.dotop.pipe.core.bo.alarm.AlarmBo;
import com.dotop.pipe.core.bo.device.DeviceBo;
import com.dotop.pipe.core.constants.CommonConstants;
import com.dotop.pipe.core.vo.alarm.AlarmVo;
import com.dotop.pipe.core.vo.device.DevicePropertyVo;
import com.dotop.pipe.core.vo.device.DeviceVo;
import com.dotop.pipe.core.vo.report.DeviceAnalysisVo;
import com.dotop.smartwater.dependence.core.common.BaseForm;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;

/**
 */
@Component
public class DeviceAnalysisFactoryImpl implements IDeviceAnalysisFactory {

    private final static Logger logger = LogManager.getLogger(DeviceAnalysisFactoryImpl.class);

    private final Long outTime = (long) (1000 * 60 * 60 * 24 * 3);

    @Autowired
    private IAuthCasWeb iAuthCasApi;

    @Autowired
    private IDeviceService iDeviceService;
    @Autowired
    private IAlarmService iAlarmService;

    @Override
    public DeviceAnalysisVo statuss(BaseForm baseForm) throws FrameworkRuntimeException {
        LoginCas loginCas = iAuthCasApi.get();
        String operEid = loginCas.getOperEid();

        // TODO 查询企业所有设备
        // 封装参数
        DeviceBo deviceBo = new DeviceBo();
        deviceBo.setLimit(10000);
        deviceBo.setProductCategory(CommonConstants.DICTIONARY_TYPE_SENSORTYPE);
        deviceBo.setEnterpriseId(operEid);
        List<DeviceVo> devices = iDeviceService.list(deviceBo);
        Map<String, DeviceVo> deviceMap = devices.stream()
                .collect(Collectors.toMap(DeviceVo::getDeviceId, a -> a, (k1, k2) -> k1));

        // 查询企业所有的报警信息 通过下面转转换方法去重
        AlarmBo alarmBo = new AlarmBo();
        alarmBo.setEnterpriseId(operEid);
        alarmBo.setPage(1);
        alarmBo.setPageSize(10000);
        alarmBo.setStatus(0);
        Pagination<AlarmVo> pagination = iAlarmService.page(alarmBo);
        List<AlarmVo> alarms = pagination.getData();
        Map<String, DeviceVo> deviceAlarmMap = alarms.stream().map(AlarmVo::getDevice)
                .collect(Collectors.toMap(DeviceVo::getDeviceId, a -> a, (k1, k2) -> k1));

        List<String> fieldList = new ArrayList<String>();
        fieldList.add("dev_send_date");// 发送时间
        // 和设备关联 查找设备的属性的发送时间
        List<DevicePropertyVo> reports = iDeviceService.getDevicePropertyAll(operEid,
                CommonConstants.DICTIONARY_TYPE_SENSORTYPE, null);

        Date curDate = new Date();
        long curDateL = curDate.getTime();

        // 在线设备
        Map<String, DevicePropertyVo> onlineMap = reports.stream().filter(item -> {
            Date devSendDate = item.getDevSendDate();
            if (devSendDate == null) {
                return false;
            }
            long devSendDateL = devSendDate.getTime();
            // 时间大于三天 是离线设备
            if (curDateL - devSendDateL > outTime) {
                // 离线设备
                return false;
            } else {
                // 现在设备
                return true;
            }
        }).collect(Collectors.toMap(DevicePropertyVo::getDeviceId, a -> a, (k1, k2) -> k1));

        // 离线设备
        Map<String, DevicePropertyVo> offlineMap = reports.stream().filter(item -> {
            Date devSendDate = item.getDevSendDate();
            if (devSendDate == null) {
                return true;
            }
            long devSendDateL = devSendDate.getTime();
            // 时间大于三天 是离线设备
            if (curDateL - devSendDateL > outTime) {
                // 离线设备
                return true;
            } else {
                // 在线设备
                return false;
            }
        }).collect(Collectors.toMap(DevicePropertyVo::getDeviceId, a -> a, (k1, k2) -> k1));

        // 设备总数
        int totalNum = deviceMap.size();
        // 报警设备
        int alarmNum = deviceAlarmMap.size();
        // 在线设备
        int onlineNum = onlineMap.size();
        // 离线设备
        int offlineNum = offlineMap.size();

        DeviceAnalysisVo deviceAnalysis = new DeviceAnalysisVo(totalNum, alarmNum, onlineNum, offlineNum);

        return deviceAnalysis;
    }

    @Override
    public DeviceAnalysisVo propertys(BaseForm baseForm) throws FrameworkRuntimeException {

        // TODO 查询企业所有设备，流量计或压力计或水质计
        LoginCas loginCas = iAuthCasApi.get();
        String operEid = loginCas.getOperEid();
        // 封装参数
        DeviceBo deviceBo = new DeviceBo();
        deviceBo.setLimit(10000);
        deviceBo.setProductCategory(CommonConstants.DICTIONARY_TYPE_SENSORTYPE);
        deviceBo.setEnterpriseId(operEid);
        List<DeviceVo> devices = iDeviceService.list(deviceBo);
        // List<DeviceVo> devices = new ArrayList<>();
        // TODO 查询企业设备最新数据(以某field作为最新) 每个设备可能有多条数据 全部查出
        List<String> fieldList = new ArrayList<String>();
        fieldList.add("flw_rate"); // 流速
        fieldList.add("flw_measure"); // 行度
        fieldList.add("flw_total_value"); // 行度
        fieldList.add("dev_send_date");// 发送时间
        fieldList.add("pressure_value");// 压力
        fieldList.add("quality_turbid");// 浑浊度
        fieldList.add("quality_oxygen");// 含氧值
        fieldList.add("quality_chlorine");// 含氯值
        fieldList.add("quality_ph");// PH值

        List<DevicePropertyVo> dps = iDeviceService.getDevicePropertyAll(operEid, CommonConstants.DICTIONARY_TYPE_SENSORTYPE, fieldList);

		/*Map<String, List<DevicePropertyVo>> dpsMap = dps.stream().collect(Collectors
				.groupingBy(DevicePropertyVo::getDeviceId, HashMap::new, Collectors.toCollection(ArrayList::new)));*/

        Map<String, DevicePropertyVo> dpsMap = dps.stream().collect(Collectors.toMap(DevicePropertyVo::getDeviceId, a -> a, (k1, k2) -> k1));
        // TODO 查询企业报警设备信息
        AlarmBo alarmBo = new AlarmBo();
        alarmBo.setEnterpriseId(operEid);
        alarmBo.setPage(1);
        alarmBo.setPageSize(10000);
        alarmBo.setStatus(0);
        Pagination<AlarmVo> pagination = iAlarmService.page(alarmBo);
        List<AlarmVo> alarms = pagination.getData();
        Map<String, List<AlarmVo>> alarmsMap = alarms.stream().collect(
                Collectors.groupingBy(AlarmVo::getDeviceId, HashMap::new, Collectors.toCollection(ArrayList::new)));

        Date curDate = new Date();
        long curDateL = curDate.getTime();
        // 组装集合
        devices.forEach((item) -> {
            String deviceId = item.getDeviceId();
            DevicePropertyVo dpl = dpsMap.get(deviceId);
            item.setDeviceProperty(dpl);
            List<AlarmVo> al = alarmsMap.get(deviceId);
            // item.setAlarms(al); // TODO
            if (al != null && al.size() > 0) {
                item.setIsAlarm(true);
            } else {
                item.setIsAlarm(false);
            }
            if (dpl != null) {
                Date devSendDate = dpl.getDevSendDate();
                // 把设备发送时间存储在 最后修改时间的字段上
                item.setLastDate(devSendDate);
                if (devSendDate != null) {
                    long devSendDateL = devSendDate.getTime();
                    // 时间大于三天 是离线设备
                    if (curDateL - devSendDateL > outTime) {
                        // 离线设备
                        item.setOnline(false);
                    } else {
                        // 在线设备
                        item.setOnline(true);
                    }
                } else {
                    item.setOnline(false);
                }
            } else {
                item.setOnline(false);
            }

            // TODO 简单阉割数据
            item.setArea(null);
            item.setPoints(null);
        });
        DeviceAnalysisVo deviceAnalysis = new DeviceAnalysisVo(devices);
        return deviceAnalysis;
    }

}
