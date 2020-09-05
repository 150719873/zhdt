package com.dotop.pipe.web.factory.alarm;

import com.dotop.pipe.api.service.alarm.IAlarmSettingService;
import com.dotop.pipe.api.service.alarm.IAlarmWMSettingService;
import com.dotop.pipe.api.service.device.IDeviceService;
import com.dotop.pipe.auth.cas.web.IAuthCasWeb;
import com.dotop.pipe.auth.core.vo.auth.LoginCas;
import com.dotop.pipe.core.bo.alarm.AlarmSettingBo;
import com.dotop.pipe.core.bo.alarm.AlarmWMSettingBo;
import com.dotop.pipe.core.constants.CommonConstants;
import com.dotop.pipe.core.form.AlarmWMSettingForm;
import com.dotop.pipe.core.utils.AlarmCalulationUtils;
import com.dotop.pipe.core.utils.ReportUtils;
import com.dotop.pipe.core.vo.alarm.AlarmSettingVo;
import com.dotop.pipe.core.vo.alarm.AlarmWMSettingVo;
import com.dotop.pipe.core.vo.device.DevicePropertyVo;
import com.dotop.pipe.core.vo.device.DeviceVo;
import com.dotop.pipe.data.report.api.service.IReportService;
import com.dotop.pipe.data.report.core.vo.ReportVo;
import com.dotop.pipe.web.api.factory.alarm.IAlarmWMSettingFactory;
import com.dotop.smartwater.dependence.core.common.BaseVo;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.BeanUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.dotop.pipe.core.constants.PropertyConstants.*;

/**
 *
 * @date 2019年2月15日
 */
@Component
public class AlarmWMSettingFactoryImpl implements IAlarmWMSettingFactory {

    private final static Logger logger = LogManager.getLogger(AlarmWMSettingFactoryImpl.class);

    @Autowired
    private IAuthCasWeb iAuthCasApi;

    @Autowired
    private IAlarmWMSettingService iAlarmWMSettingService;

    @Autowired
    private IReportService iReportService;

    @Autowired
    private IAlarmSettingService iAlarmSettingService;

    @Autowired
    private IDeviceService iDeviceService;

    @Override
    public Pagination<DeviceVo> getPage(AlarmWMSettingForm alarmWMSettingForm) throws FrameworkRuntimeException {
        LoginCas loginCas = iAuthCasApi.get();
        String operEid = loginCas.getOperEid();
        String userId = loginCas.getUserId();
        AlarmWMSettingBo alarmWMSettingBo = BeanUtils.copyProperties(alarmWMSettingForm, AlarmWMSettingBo.class);
        alarmWMSettingBo.setUserId(userId);
        alarmWMSettingBo.setEnterpriseId(operEid);
        Pagination<DeviceVo> pagination = iAlarmWMSettingService.getPage(alarmWMSettingBo);
        return pagination;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
    public BaseVo add(AlarmWMSettingForm alarmWMSettingForm) {
        LoginCas loginCas = iAuthCasApi.get();
        String operEid = loginCas.getOperEid();
        String userId = loginCas.getUserId();
        String userBy = loginCas.getUserName();

        AlarmWMSettingBo alarmWMSettingBo = BeanUtils.copyProperties(alarmWMSettingForm, AlarmWMSettingBo.class);
        alarmWMSettingBo.setCurr(new Date());
        alarmWMSettingBo.setUserBy(userBy);
        alarmWMSettingBo.setUserId(userId);
        alarmWMSettingBo.setEnterpriseId(operEid);
        iAlarmWMSettingService.add(alarmWMSettingBo);
        return null;
    }

    @Override
    public BaseVo edit(AlarmWMSettingForm alarmWMSettingForm) {
        LoginCas loginCas = iAuthCasApi.get();
        String operEid = loginCas.getOperEid();
        String userId = loginCas.getUserId();
        String userBy = loginCas.getUserName();
        AlarmWMSettingBo alarmWMSettingBo = BeanUtils.copyProperties(alarmWMSettingForm, AlarmWMSettingBo.class);
        alarmWMSettingBo.setCurr(new Date());
        alarmWMSettingBo.setUserBy(userBy);
        alarmWMSettingBo.setUserId(userId);
        alarmWMSettingBo.setEnterpriseId(operEid);
        return iAlarmWMSettingService.edit(alarmWMSettingBo);
    }

    @Override
    public List<AlarmWMSettingVo> getByDeviceId(AlarmWMSettingForm alarmWMSettingForm) {
        LoginCas loginCas = iAuthCasApi.get();
        String operEid = loginCas.getOperEid();
        String userId = loginCas.getUserId();
        String deviceId = alarmWMSettingForm.getDeviceId();
        // 查询设备最新的上报数据
        List<ReportVo> list = iReportService.getDeviceRealTime(operEid, Arrays.asList(deviceId), null);
        AlarmSettingBo alarmSettingBo = new AlarmSettingBo();
        alarmSettingBo.setEnterpriseId(operEid);
        alarmSettingBo.setDeviceId(deviceId);
        // 查询设备配置的预警信息数据
        List<AlarmSettingVo> alarmSettingVoList = iAlarmSettingService.gets(alarmSettingBo);
        Map<String, AlarmSettingVo> maps = alarmSettingVoList.stream()
                .collect(Collectors.toMap(AlarmSettingVo::getField, Function.identity(), (key1, key2) -> key2));
        // 将预警信息和上报数据整合
        if (list != null && list.size() > 0) {
            ReportVo reportVo = list.get(0);
            return (List<AlarmWMSettingVo>) ReportUtils.alarmWMToList(reportVo, maps);
        } else {
            return new ArrayList<>();
        }

    }

    @Override
    public List<DeviceVo> getAlarm(AlarmWMSettingForm alarmWMSettingForm) {
        // 同getAlarmByData方法
//		LoginCas loginCas = iAuthCasApi.get();
//		String operEid = loginCas.getOperEid();
//		String userId = loginCas.getUserId();
//		AlarmWMSettingBo alarmWMSettingBo = new AlarmWMSettingBo();
//		alarmWMSettingBo.setUserId(userId);
//		alarmWMSettingBo.setEnterpriseId(operEid);
//		List<DeviceVo> list = iAlarmWMSettingService.getWmAlarm(alarmWMSettingBo);
//		return list ;
        LoginCas loginCas = iAuthCasApi.get();
        String operEid = loginCas.getOperEid();
        String userId = loginCas.getUserId();
        // 查询所有设备的集合
        AlarmWMSettingBo alarmWMSettingBo = new AlarmWMSettingBo();
        alarmWMSettingBo.setUserId(userId);
        alarmWMSettingBo.setEnterpriseId(operEid);
        List<DeviceVo> deviceVoList = iAlarmWMSettingService.getWmAlarm(alarmWMSettingBo);
        // 查询设备属性的集合
        List<String> fieldList = new ArrayList<String>();
//        fieldList.add(TYPE_FM_RATE); // 流速
//        fieldList.add(TYPE_FM_FLWMEASURE); // 行度
//        fieldList.add(TYPE_FM_FLWTOTAL); // 总行度
//        fieldList.add("dev_send_date");// 发送时间
//        fieldList.add(TYPE_PRESSURE_VALUE);// 压力
        fieldList.add(TYPE_WM_QUALITYTEMONE);// 一号温度
        fieldList.add(TYPE_WM_QUALITYTEMTWO);// 二号温度
        fieldList.add(TYPE_WM_QUALITYTEMTHREE);// 三号温度
        fieldList.add(TYPE_WM_QUALITYTEMFOUR);// 四号温度
        fieldList.add(TYPE_WM_QUALITYTURBID);// 浑浊度
        fieldList.add(TYPE_WM_QUALITYOXYGEN);// 含氧值
        fieldList.add(TYPE_WM_QUALITYCHLORINE);// 含氯值
        fieldList.add(TYPE_WM_QUALITYPH);// PH值
        List<DevicePropertyVo> dps = iDeviceService.getDevicePropertyAll(operEid, CommonConstants.DICTIONARY_TYPE_SENSORTYPE, fieldList);
        Map<String, DevicePropertyVo> dpsMap = dps.stream().collect(Collectors.toMap(DevicePropertyVo::getDeviceId, a -> a, (k1, k2) -> k1));
        // 查询设置的预警值得集合
        AlarmSettingBo alarmSettingBo = new AlarmSettingBo();
        alarmSettingBo.setEnterpriseId(operEid);
        List<AlarmSettingVo> alarmSettingVoList = iAlarmSettingService.gets(alarmSettingBo);
        Map<String, List<AlarmSettingVo>> alarmSettingListMap = alarmSettingVoList.stream().collect(
                Collectors.groupingBy(AlarmSettingVo::getDeviceId, HashMap::new, Collectors.toCollection(ArrayList::new)));
        // 遍历计算预警值
        List<DeviceVo> alarmList = new ArrayList<>();
        for (DeviceVo deviceVo : deviceVoList) {
            if (!dpsMap.containsKey(deviceVo.getDeviceId())) {
                continue;
            }
            if (!alarmSettingListMap.containsKey(deviceVo.getDeviceId())) {
                continue;
            }
            DevicePropertyVo devicePropertyVo = dpsMap.get(deviceVo.getDeviceId());
            List<AlarmSettingVo> alarmSetting = alarmSettingListMap.get(deviceVo.getDeviceId());

            boolean flag = AlarmCalulationUtils.getAlarmCalulation(alarmSetting, devicePropertyVo, "already");
            if (flag) {
                alarmList.add(deviceVo);
            }
        }
        return alarmList;

    }

}
