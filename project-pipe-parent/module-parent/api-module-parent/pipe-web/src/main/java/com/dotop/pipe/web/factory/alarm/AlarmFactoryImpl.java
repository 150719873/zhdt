package com.dotop.pipe.web.factory.alarm;

import com.dotop.pipe.web.api.factory.device.IDeviceFactory;
import com.dotop.pipe.api.service.alarm.IAlarmService;
import com.dotop.pipe.api.service.alarm.IAlarmSettingService;
import com.dotop.pipe.api.service.area.IAreaService;
import com.dotop.pipe.api.service.device.IDeviceService;
import com.dotop.pipe.auth.cas.web.IAuthCasWeb;
import com.dotop.pipe.auth.core.vo.auth.LoginCas;
import com.dotop.pipe.core.bo.alarm.AlarmBo;
import com.dotop.pipe.core.bo.alarm.AlarmSettingBo;
import com.dotop.pipe.core.bo.device.DeviceBo;
import com.dotop.pipe.core.constants.CommonConstants;
import com.dotop.pipe.core.exception.PipeExceptionConstants;
import com.dotop.pipe.core.form.AlarmForm;
import com.dotop.pipe.core.form.DeviceForm;
import com.dotop.pipe.core.utils.AlarmCalulationUtils;
import com.dotop.pipe.core.vo.alarm.AlarmSettingVo;
import com.dotop.pipe.core.vo.alarm.AlarmVo;
import com.dotop.pipe.core.vo.area.AreaModelVo;
import com.dotop.pipe.core.vo.device.DevicePropertyVo;
import com.dotop.pipe.core.vo.device.DeviceVo;
import com.dotop.pipe.web.api.factory.alarm.IAlarmFactory;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static com.dotop.pipe.core.constants.PropertyConstants.*;

/**
 *
 * @date 2019年2月15日
 */
@Component
public class AlarmFactoryImpl implements IAlarmFactory {

    private final static Logger logger = LogManager.getLogger(AlarmFactoryImpl.class);

    @Autowired
    private IAuthCasWeb iAuthCasApi;

    @Autowired
    private IAlarmService iAlarmService;

    @Autowired
    private IDeviceFactory iDeviceFactory;

    @Autowired
    private IDeviceService iDeviceService;
    @Autowired
    private IAlarmSettingService iAlarmSettingService;
    @Autowired
    private IAreaService iAreaService;

    @Override
    public Pagination<AlarmVo> page(AlarmForm alarmForm) throws FrameworkRuntimeException {
        LoginCas loginCas = iAuthCasApi.get();
        String operEid = loginCas.getOperEid();
        Integer page = alarmForm.getPage();
        Integer pageSize = alarmForm.getPageSize();
        AlarmBo alarmBo = BeanUtils.copyProperties(alarmForm, AlarmBo.class);
        alarmBo.setEnterpriseId(operEid);
        if (!"all".equals(alarmForm.getStatus())) {
            alarmBo.setStatus(Integer.parseInt(alarmForm.getStatus()));
        }
        // 报表查看 不能区分类型
        if ("areaAlarm".equals(alarmForm.getProductCategory())) {
            alarmBo.setProductCategory(null);
            Pagination<AlarmVo> pagination = iAlarmService.page(alarmBo);
            return pagination;
        } else if ("area".equals(alarmForm.getProductCategory())) {
            Pagination<AlarmVo> pagination = iAlarmService.areaPage(alarmBo);
            return pagination;
        } else if (CommonConstants.PRODUCT_CATEGORY_SENSOR.equals(alarmForm.getProductCategory())) { // 查询非区域的报警信息
            Pagination<AlarmVo> pagination = iAlarmService.page(alarmBo);
            return pagination;
        } else if (CommonConstants.PRODUCT_CATEGORY_HYDRANT.equals(alarmForm.getProductCategory())) { // 查询非区域的报警信息
            Pagination<AlarmVo> pagination = iAlarmService.page(alarmBo);
            return pagination;
        } else {
            // 添加设施固定类型
            if (StringUtils.isBlank(alarmForm.getProductCategory())) {
                List<String> list = new ArrayList<>();
                list.add(CommonConstants.PRODUCT_CATEGORY_WATER_FACTORY);
                list.add(CommonConstants.PRODUCT_CATEGORY_SLOPS_FACTORY);
                list.add(CommonConstants.PRODUCT_CATEGORY_REGION);
                alarmBo.setProductCategorys(list);
            }
            Pagination<AlarmVo> pagination = iAlarmService.page(alarmBo);
            return pagination;
        }
    }


    @Override
    public Pagination<AlarmVo> list(AlarmForm alarmForm) throws FrameworkRuntimeException {
        LoginCas loginCas = iAuthCasApi.get();
        String operEid = loginCas.getOperEid();
        Integer page = alarmForm.getPage();
        Integer pageSize = alarmForm.getPageSize();
        AlarmBo alarmBo = BeanUtils.copyProperties(alarmForm, AlarmBo.class);
        alarmBo.setEnterpriseId(operEid);
        if (!"all".equals(alarmForm.getStatus())) {
            alarmBo.setStatus(Integer.parseInt(alarmForm.getStatus()));
        }
        // 封装所有三种类型的数据到一个集合中
        alarmBo.setProductCategory("area");
        Pagination<AlarmVo> areaList = iAlarmService.areaPage(alarmBo);

        List<String> list = new ArrayList<>();
        list.add(CommonConstants.PRODUCT_CATEGORY_WATER_FACTORY);
        list.add(CommonConstants.PRODUCT_CATEGORY_SLOPS_FACTORY);
        list.add(CommonConstants.PRODUCT_CATEGORY_REGION);
        list.add(CommonConstants.PRODUCT_CATEGORY_SENSOR);
        list.add(CommonConstants.PRODUCT_CATEGORY_HYDRANT);
        alarmBo.setProductCategorys(list);
        alarmBo.setProductCategory(null);
        Pagination<AlarmVo> decicelist = iAlarmService.page(alarmBo);

        Pagination<AlarmVo> pagination = new Pagination<AlarmVo>(alarmBo.getPageSize(), alarmBo.getPage());
        List<AlarmVo> alarmVoList = new ArrayList<>();
        alarmVoList.addAll(areaList.getData());
        alarmVoList.addAll(decicelist.getData());
        pagination.setData(alarmVoList);
        pagination.setTotalPageSize(decicelist.getTotalPageSize() + areaList.getTotalPageSize());
        return pagination;
    }

    @Override
    public AlarmVo get(AlarmForm alarmForm) throws FrameworkRuntimeException {
        LoginCas loginCas = iAuthCasApi.get();
        String operEid = loginCas.getOperEid();
        AlarmBo alarmBo = BeanUtils.copyProperties(alarmForm, AlarmBo.class);
        alarmBo.setEnterpriseId(operEid);
        AlarmVo alarmVo = iAlarmService.get(alarmBo);
        // 整合多种设备类型后封装成alarmVo 返回
        if ("area".equals(alarmForm.getProductCategory())) {
            AreaModelVo nodeDetails = iAreaService.getNodeDetails(operEid, alarmVo.getDeviceId());
            DeviceVo deviceVo = new DeviceVo();
            deviceVo.setName(nodeDetails.getName());
            deviceVo.setCode(nodeDetails.getAreaCode());
            List<String> fields = new ArrayList<String>();
            fields.add(TYPE_FM_RATE);
            fields.add(TYPE_FM_FLWTOTAL);
            fields.add(TYPE_FM_FLWMEASURE);
            List<DevicePropertyVo> devicePropertys = iDeviceService.getDevicePropertys(deviceVo.getDeviceId(), deviceVo.getCode(), fields,
                    operEid);
            if (devicePropertys != null) {
                deviceVo.setDevicePropertys(devicePropertys);
            }
            alarmVo.setDevice(deviceVo);
        } else {
            // 扩展设备信息
            DeviceForm deviceBo = new DeviceForm();
            deviceBo.setDeviceId(alarmVo.getDeviceId());
            deviceBo.setEnterpriseId(operEid);
            DeviceVo deviceVo = iDeviceFactory.get(deviceBo);
            if (deviceVo == null) {
                return alarmVo;
            }
            if (!CommonConstants.PRODUCT_CATEGORY_SENSOR.equals(deviceVo.getProduct().getCategory().getVal()) &&
                    !CommonConstants.PRODUCT_CATEGORY_HYDRANT.equals(deviceVo.getProduct().getCategory().getVal())) {
                List<String> fields = new ArrayList<String>();
                fields.add(TYPE_FM_RATE);
                fields.add(TYPE_FM_FLWTOTAL);
                fields.add(TYPE_FM_FLWMEASURE);
                List<DevicePropertyVo> devicePropertys = iDeviceService.getDevicePropertys(deviceVo.getDeviceId(), deviceVo.getCode(), fields,
                        operEid);
                if (devicePropertys != null) {
                    deviceVo.setDevicePropertys(devicePropertys);
                }
            }
            alarmVo.setDevice(deviceVo);
        }
        return alarmVo;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
    public AlarmVo edit(AlarmForm alarmForm) throws FrameworkRuntimeException {
        LoginCas loginCas = iAuthCasApi.get();
        String operEid = loginCas.getOperEid();
        String userBy = loginCas.getUserName();
        AlarmBo alarmBo = new AlarmBo();
        alarmBo.setEnterpriseId(operEid);
        alarmBo.setAlarmId(alarmForm.getAlarmId());
        alarmBo.setProcessResult(alarmForm.getProcessResult());
        alarmBo.setCurr(new Date());
        alarmBo.setUserBy(userBy);
        alarmBo.setStatus(Integer.parseInt(alarmForm.getStatus()));
        int count = iAlarmService.edit(alarmBo);
        if (count != 1) {
            logger.error(LogMsg.to("ex", PipeExceptionConstants.ALARM_UPDATE_ERROR, "alarmId", alarmForm.getAlarmId()));
            throw new FrameworkRuntimeException(PipeExceptionConstants.ALARM_UPDATE_ERROR);
        }
        return null;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
    public String add(String name, String des, String sensorId, Integer status, String operEid, String userBy)
            throws FrameworkRuntimeException {
        Date createDate = new Date();
        // 先判断这个设备是否存在未处理的预警信息
        AlarmBo alarmBo = new AlarmBo();
        alarmBo.setEnterpriseId(operEid);
        alarmBo.setDeviceId(sensorId);
        alarmBo.setStatus(0);
        // AlarmVo alarmVo = iAlarmService.get(alarmBo);
        alarmBo.setUserBy(userBy);
        alarmBo.setCurr(createDate);
        alarmBo.setStatus(status);
        // if (alarmVo != null) {
        // // 存在未处理的信息 则修改原数据
        // alarmBo.setAlarmId(alarmVo.getAlarmId());
        // Integer alarmCount = Integer.valueOf(alarmVo.getAlarmCount()) + 1;
        // alarmBo.setAlarmCount(alarmCount);
        // alarmBo.setName(alarmVo.getName() + name);
        // alarmBo.setDes(alarmVo.getDes() + des);
        // iAlarmService.edit(alarmBo);
        // // 发送短信通知
        // AlarmNoticeRuleVo alarmNoticeRuleVo =
        // iCommonService.getALarmNoticeRule(operEid);
        // if (alarmNoticeRuleVo != null && alarmNoticeRuleVo.getAlarmNum() <
        // alarmCount) {
        // iWaterNoticeClient.add(alarmBo, alarmNoticeRuleVo);
        // }
        // } else {
        // 新增数据
        Random rand = new Random();
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        String code = df.format(createDate) + String.valueOf(rand.nextInt(900) + 100);
        alarmBo.setCode(code);
        alarmBo.setAlarmCount(1);
        alarmBo.setDes(des);
        alarmBo.setName(name);
        String alarmId = iAlarmService.add(alarmBo);
        // }
        return null;
    }

    @Override
    public String add(String deviceId, String enterpriseId, Map<String, Object> devicePropertyLogMap) {
        deviceId = "6c56882f-8a24-477d-b492-8e9d7c43993d";
        enterpriseId = "44";
        devicePropertyLogMap.put("flw_rate", "20");
        devicePropertyLogMap.put("flw_total_value", "100");
        iAlarmService.add(deviceId, enterpriseId, devicePropertyLogMap);

        /**
         * 1 结合预警设置 查找设置规则 2 增加预警信息 pls_alarm 3
         */
        // AlarmSettingBo alarmSettingBo = new AlarmSettingBo();
        // alarmSettingBo.setDeviceId(deviceId);
        // alarmSettingBo.setEnterpriseId(enterpriseId);
        // List<AlarmSettingVo> alarmSettingVoList =
        // iAlarmSettingService.gets(alarmSettingBo);
        //
        // if (alarmSettingVoList.isEmpty()) {
        // return null;
        // }
        //
        // StringBuffer stringbuf = new StringBuffer();
        //
        // // 查询当前设备 设置的预警信息
        // for (AlarmSettingVo alarmSettingVo : alarmSettingVoList) {
        // if (devicePropertyLogMap.containsKey(alarmSettingVo.getTag())) {
        // String val = devicePropertyLogMap.get(alarmSettingVo.getTag()).toString();
        // boolean flag = AlarmCalulationUtils.alarmCalulation(alarmSettingVo, val);
        // if (flag) {
        // // 拼接此次报警的异常信息
        // stringbuf.append(alarmSettingVo.getDes()).append(":").append(val).append(",");
        // }
        // }
        // }
        //
        // if (stringbuf.length() > 0) {
        // stringbuf.append("预警时间").append(":").append(devicePropertyLogMap.get("dev_send_date")).append(";");
        // add(stringbuf.toString(), stringbuf.toString(), deviceId, 0, enterpriseId,
        // "system");
        // }
        return null;
    }

    @Override
    public List<DeviceVo> getNearAlarm() {
        LoginCas loginCas = iAuthCasApi.get();
        String operEid = loginCas.getOperEid();
        String userBy = loginCas.getUserName();
        // 查询所有设备的集合
        DeviceBo deviceBo = new DeviceBo();
        deviceBo.setEnterpriseId(operEid);
        deviceBo.setProductCategory(CommonConstants.PRODUCT_CATEGORY_SENSOR);
        List<DeviceVo> deviceVoList = iDeviceService.list(deviceBo);
        // 查询设备属性的集合
        List<String> fieldList = new ArrayList<String>();
        fieldList.add(TYPE_FM_RATE); // 流速
//        fieldList.add(TYPE_FM_FLWMEASURE); // 行度
        fieldList.add(TYPE_FM_FLWTOTAL); // 总行度
//        fieldList.add("dev_send_date");// 发送时间
        fieldList.add(TYPE_PRESSURE_VALUE);// 压力
        fieldList.add(TYPE_WM_QUALITYTURBID);// 浑浊度
        fieldList.add(TYPE_WM_QUALITYOXYGEN);// 含氧值
        fieldList.add(TYPE_WM_QUALITYCHLORINE);// 含氯值
        fieldList.add(TYPE_WM_QUALITYPH);// PH值
        fieldList.add(TYPE_HYDRANT_SLOPE);// 倾斜
        fieldList.add(TYPE_HYDRANT_HIGH_LOW_ALARM);// 高低压报警
        fieldList.add(TYPE_HYDRANT_BUMP);// 碰撞
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

            boolean flag = AlarmCalulationUtils.getAlarmCalulation(alarmSetting, devicePropertyVo, "near");
            if (flag) {
                alarmList.add(deviceVo);
            }
        }
        return alarmList;
    }

    @Override
    public List<DeviceVo> getAlarmByData(String productCategory) {
        LoginCas loginCas = iAuthCasApi.get();
        String operEid = loginCas.getOperEid();
        String userBy = loginCas.getUserName();
        // 查询所有设备的集合
        DeviceBo deviceBo = new DeviceBo();
        deviceBo.setEnterpriseId(operEid);
//        deviceBo.setProductCategory(CommonConstants.PRODUCT_CATEGORY_SENSOR);
        deviceBo.setProductCategory(productCategory);
        List<DeviceVo> deviceVoList = iDeviceService.list(deviceBo);
        // 查询设备属性的集合
        List<String> fieldList = new ArrayList<String>();
        fieldList.add(TYPE_FM_RATE); // 流速
//        fieldList.add(TYPE_FM_FLWMEASURE); // 行度
        fieldList.add(TYPE_FM_FLWTOTAL); // 总行度
//        fieldList.add("dev_send_date");// 发送时间
        fieldList.add(TYPE_PRESSURE_VALUE);// 压力
        fieldList.add(TYPE_WM_QUALITYTURBID);// 浑浊度
        fieldList.add(TYPE_WM_QUALITYOXYGEN);// 含氧值
        fieldList.add(TYPE_WM_QUALITYCHLORINE);// 含氯值
        fieldList.add(TYPE_WM_QUALITYPH);// PH值
        fieldList.add(TYPE_HYDRANT_SLOPE);// 倾斜
        fieldList.add(TYPE_HYDRANT_HIGH_LOW_ALARM);// 高低压报警
        fieldList.add(TYPE_HYDRANT_BUMP);// 碰撞
//        List<DevicePropertyVo> dps = iDeviceService.getDevicePropertyAll(operEid, CommonConstants.DICTIONARY_TYPE_SENSORTYPE, fieldList);
        List<DevicePropertyVo> dps = iDeviceService.getDevicePropertyAll(operEid, productCategory, fieldList);
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
