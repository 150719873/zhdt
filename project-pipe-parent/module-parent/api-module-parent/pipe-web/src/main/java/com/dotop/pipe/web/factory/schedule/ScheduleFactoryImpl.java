package com.dotop.pipe.web.factory.schedule;

import com.alibaba.fastjson.JSONObject;
import com.dotop.pipe.web.api.factory.schedule.IScheduleFactory;
import com.dotop.pipe.api.service.common.ICommonService;
import com.dotop.pipe.api.service.device.IDeviceMappingService;
import com.dotop.pipe.api.service.device.IDeviceService;
import com.dotop.pipe.auth.cas.web.IAuthCasWeb;
import com.dotop.pipe.core.bo.device.DeviceMappingBo;
import com.dotop.pipe.core.enums.FieldTypeEnum;
import com.dotop.pipe.core.vo.device.DeviceMappingVo;
import com.dotop.pipe.data.receiver.api.factory.IKBLReceiveFactory;
import com.dotop.pipe.data.report.api.service.IReportService;
import com.dotop.pipe.data.report.core.vo.ReportVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class ScheduleFactoryImpl implements IScheduleFactory {

    @Autowired
    private IDeviceMappingService iDeviceMappingService;

    @Autowired
    private IAuthCasWeb iAuthCasWeb;

    @Autowired
    private IReportService iReportService;

    @Autowired
    private IDeviceService iDeviceService;

    @Autowired
    private ICommonService iCommonService;

    @Autowired
    private IKBLReceiveFactory iKBLReceiveFactory;

    @Override
    public void updateData(String _type) {
        // 查询企业id
        List<String> enterpriseidList = iCommonService.getEnterpriseIdList();
        if (enterpriseidList == null || enterpriseidList.isEmpty()) {
            return;
        }
        for (String enterpriseId : enterpriseidList) {
            // 1 查询关联的设备 封装查询信息参数
            DeviceMappingBo deviceMappingBo = new DeviceMappingBo();
            deviceMappingBo.setLevel("1");
            deviceMappingBo.setEnterpriseId(enterpriseId);
            List<DeviceMappingVo> list = new ArrayList<>();
            if ("area".equals(_type)) {
                list = iDeviceMappingService.mappingList(deviceMappingBo);
            } else if ("region".equals(_type)) {
                list = iDeviceMappingService.regionMappingList(deviceMappingBo);
            }

            Map<String, List<DeviceMappingVo>> areaMap = new HashMap<>();
            List<String> deviceIds = new ArrayList<>();
            for (DeviceMappingVo deviceMappingVo : list) {
                // 封装设备集合
                String deviceId = deviceMappingVo.getOtherDeviceVos().get(0).getDeviceId();
                deviceIds.add(deviceId);
                if (areaMap.containsKey(deviceMappingVo.getDeviceId())) {
                    areaMap.get(deviceMappingVo.getDeviceId()).add(deviceMappingVo);
                } else {
                    List<DeviceMappingVo> list1 = new ArrayList<>();
                    list1.add(deviceMappingVo);
                    areaMap.put(deviceMappingVo.getDeviceId(), list1);
                }
            }
            if (deviceIds.isEmpty()) {
                break;
            }
            // 2 查看最新的数据
            FieldTypeEnum[] fieldType = new FieldTypeEnum[]{
                    FieldTypeEnum.flwMeasure, FieldTypeEnum.flwRate, FieldTypeEnum.flwTotalValue,
                    FieldTypeEnum.pressureValue,
                    FieldTypeEnum.qualityChlorine, FieldTypeEnum.qualityOxygen, FieldTypeEnum.qualityPh, FieldTypeEnum.qualityTurbid
            };
            List<ReportVo> reportVoList = iReportService.getDeviceRealTime(enterpriseId, deviceIds, fieldType);
            Map<String, ReportVo> reportMap = reportVoList.stream().collect(Collectors.toMap(ReportVo::getDeviceId, a -> a, (k1, k2) -> k1));
            //List<DevicePropertyVo> reportVoList = iDeviceService.getDeviceRealTime(deviceIds, fieldType, enterpriseId);
            //Map<String, DevicePropertyVo> reportMap = reportVoList.stream().collect(Collectors.toMap(DevicePropertyVo::getDeviceId, a -> a, (k1, k2) -> k1));
            // 3 计算数据
            for (String key : areaMap.keySet()) {
                List<DeviceMappingVo> value = areaMap.get(key);
                calculationData(value, reportMap, key, enterpriseId);
            }
        }
    }

    public void calculationData(List<DeviceMappingVo> value, Map<String, ReportVo> reportMap, String deviceId, String enterpriseId) {
        BigDecimal flwMeasureTotal = BigDecimal.ZERO;
        BigDecimal flwRateTotal = BigDecimal.ZERO;
        BigDecimal flwTotalTotal = BigDecimal.ZERO;

        for (DeviceMappingVo _value : value) {
            String otherId = _value.getOtherDeviceVos().get(0).getDeviceId();
            ReportVo devicePropertyVo = reportMap.get(otherId);
            if (devicePropertyVo == null) {
                continue;
            }
            // 行度值
            if (devicePropertyVo.getFlwMeasure() != null) {
                if ("1".equals(_value.getDirection())) {
                    flwMeasureTotal = flwMeasureTotal.add(new BigDecimal(devicePropertyVo.getFlwMeasure()));
                } else if ("0".equals(_value.getDirection())) {
                    flwMeasureTotal = flwMeasureTotal.subtract(new BigDecimal(devicePropertyVo.getFlwMeasure()));
                }
                // System.out.println(flwMeasureTotal.toString());
            }
            // 流速值
            if (devicePropertyVo.getFlwRate() != null) {
                if ("1".equals(_value.getDirection())) {
                    flwRateTotal = flwRateTotal.add(new BigDecimal(devicePropertyVo.getFlwRate()));
                } else if ("0".equals(_value.getDirection())) {
                    flwRateTotal = flwRateTotal.subtract(new BigDecimal(devicePropertyVo.getFlwRate()));
                }
            }
            // 总行度
            if (devicePropertyVo.getFlwTotalValue() != null) {
                if ("1".equals(_value.getDirection())) {
                    flwTotalTotal = flwTotalTotal.add(new BigDecimal(devicePropertyVo.getFlwTotalValue()));
                } else if ("0".equals(_value.getDirection())) {
                    flwTotalTotal = flwTotalTotal.subtract(new BigDecimal(devicePropertyVo.getFlwTotalValue()));
                }
            }
        }
        // System.out.println(total.toString());
        // 4 更新数据
        JSONObject deviceData = new JSONObject();
        deviceData.put("enterprise_id", enterpriseId);
        deviceData.put("device_id", deviceId);
        deviceData.put("device_code", value.get(0).getDeviceVo().getCode());
        deviceData.put("flw_total_value", flwTotalTotal.toString());
        deviceData.put("flw_rate", flwRateTotal.toString());
        deviceData.put("flw_measure", flwMeasureTotal.toString());
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(new Date());
        deviceData.put("dev_send_date", dateString);
        System.out.println(deviceData.toString());
        iKBLReceiveFactory.handle(value.get(0).getDeviceVo().getCode(), deviceData);
    }
}
