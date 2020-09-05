package com.dotop.smartwater.view.server.api.factory.view.impl;

import com.alibaba.fastjson.JSONObject;
import com.dotop.smartwater.view.server.core.maintain.form.MaintainLogForm;
import com.dotop.smartwater.view.server.core.maintain.vo.MaintainLogVo;
import com.dotop.smartwater.view.server.service.maintain.IMaintainService;
import com.dotop.pipe.auth.cas.web.IAuthCasWeb;
import com.dotop.pipe.auth.core.vo.auth.LoginCas;
import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.BeanUtils;
import com.dotop.smartwater.dependence.core.utils.DateUtils;
import com.dotop.smartwater.dependence.core.utils.UuidUtils;
import com.dotop.smartwater.view.server.api.factory.view.ISecurityFactory;
import com.dotop.smartwater.view.server.api.factory.view.IWaterFactoryViewFactory;
import com.dotop.smartwater.view.server.core.device.form.DeviceForm;
import com.dotop.smartwater.view.server.core.device.form.DeviceSummaryForm;
import com.dotop.smartwater.view.server.core.device.vo.DeviceSummaryVo;
import com.dotop.smartwater.view.server.core.device.vo.DeviceVo;
import com.dotop.smartwater.view.server.service.device.IDeviceService;
import com.dotop.smartwater.view.server.service.device.IDeviceSummaryService;
import com.dotop.smartwater.view.server.utils.CalculationUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static com.dotop.smartwater.dependence.core.common.RootModel.NOT_DEL;
import static com.dotop.smartwater.view.server.constants.ViewConstants.*;

@Component
public class WaterFactoryViewFactory implements IWaterFactoryViewFactory {

    private final Logger logger = LoggerFactory.getLogger(ViewFactoryImpl.class);

    @Autowired
    private IAuthCasWeb iAuthCasApi;

    @Autowired
    private IDeviceService iDeviceService;

    @Autowired
    private IDeviceSummaryService iDeviceSummaryService;

    @Autowired
    private IMaintainService iMaintainService;

    @Autowired
    private ISecurityFactory iSecurityFactory;

    @Override
    public String waterFactoryUpdateTask(DeviceSummaryForm deviceSummaryForm) throws FrameworkRuntimeException {
        try {
            Date curr = new Date();
            deviceSummaryForm.setSummaryDate(curr);
            deviceSummaryForm.setIsDel(NOT_DEL);
            if (StringUtils.isBlank(deviceSummaryForm.getEnterpriseId())) {
                return "更新失败,需要企业id";
            }
            // 查询水厂数据
            DeviceForm deviceForm = new DeviceForm();
            deviceForm.setEnterpriseId(deviceSummaryForm.getEnterpriseId());
            deviceForm.setProductCategory(WATER_FACTORY);
            deviceForm.setIsDel(NOT_DEL);
            List<DeviceVo> waterFactoryList = iDeviceService.list(deviceForm);
            if (waterFactoryList == null || waterFactoryList.isEmpty()) {
                return "水厂数据不存在";
            } else {
                // 查询今天是否有数据 有数据则删除
                if (deviceSummaryForm.getDataDensity().equals(DATA_DENSITY_DAY)) {
                    // 每天一条数据
                    List<DeviceSummaryForm> list = this.calculationSummary(waterFactoryList, curr, deviceSummaryForm.getEnterpriseId());
                    // 历史数据添加到历史表
                    iDeviceSummaryService.adds(list);
                    // 添加到当前数据表
                    List<DeviceSummaryForm> list1 = this.calculationCuur(waterFactoryList, deviceSummaryForm.getEnterpriseId());
                    list1.addAll(list);
                    iDeviceSummaryService.addCuurs(list1);

                } else if (deviceSummaryForm.getDataDensity().equals(DATA_DENSITY_HOUR)) {
                    Date tempDate = new Date();
                    List<DeviceSummaryForm> list = this.calculationHour(waterFactoryList, tempDate, deviceSummaryForm.getEnterpriseId(), 1);
                    iDeviceSummaryService.adds(list);
                    // 更新最新数据表
                    iDeviceSummaryService.addCuurs(list);
                }

//                Date curr = new Date();
//                deviceSummaryForm.setSummaryCategory(WATER_FACTORY);
//                deviceSummaryForm.setSummaryType(WATER_FACTORY_USED_ELECTRICITY);
//                deviceSummaryForm.setStartDate(curr);
//                deviceSummaryForm.setIsDel(NOT_DEL);
//                List<DeviceSummaryVo> list = iDeviceSummaryService.listGroup(deviceSummaryForm);
//                if (list != null && !list.isEmpty()) {
//                    iDeviceSummaryService.delByType(Arrays.asList(WATER_FACTORY_USED_ELECTRICITY, WATER_FACTORY_TEMPERATURE, WATER_FACTORY_CHLORINE, WATER_FACTORY_IN_FM, WATER_FACTORY_OUT_FM), curr);
//                }
//                deviceSummaryForm.setSummaryCategory(null);
//                deviceSummaryForm.setSummaryType(null);
//                deviceSummaryForm.setStartDate(null);
//
//                // 生成历史数据
//                iDeviceSummaryService.adds(this.calculationSummary(waterFactoryList, curr, deviceSummaryForm.getEnterpriseId()));
//                // 先删掉
//                iDeviceSummaryService.delByType(Arrays.asList(WATER_FACTORY_PM, WATER_FACTORY_IN_PM, WATER_FACTORY_OUT_PM, WATER_FACTORY_IN_TURBID,
//                        WATER_FACTORY_OUT_TURBID, WATER_FACTORY_TURBID, WATER_FACTORY_DRUG, WATER_FACTORY_IN_PH, WATER_FACTORY_OUT_PH, WATER_FACTORY_PH,
//                        WATER_FACTORY_IN_OXYGEN, WATER_FACTORY_OUT_OXYGEN
//                ), null);
//                // 生成当天的数据
//                iDeviceSummaryService.adds(this.calculationCuur(waterFactoryList, deviceSummaryForm.getEnterpriseId()));
            }
            return "更新数据成功";
        } catch (Exception e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.UNDEFINED_ERROR, e.getMessage(), e);
        }
    }

    /**
     * 初始化数据
     *
     * @param deviceSummaryForm
     * @return
     * @throws FrameworkRuntimeException
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
    public String init(DeviceSummaryForm deviceSummaryForm) throws FrameworkRuntimeException {
        try {
            // 初始化数据
            LoginCas loginCas = iAuthCasApi.get();
            // 查询水厂数据
            DeviceForm deviceForm = new DeviceForm();
            deviceForm.setEnterpriseId(loginCas.getEnterpriseId());
            deviceForm.setProductCategory(WATER_FACTORY);
            deviceForm.setIsDel(NOT_DEL);
            List<DeviceVo> waterFactoryList = iDeviceService.list(deviceForm);
            if (waterFactoryList == null || waterFactoryList.isEmpty()) {
                return "水厂数据不存在";
            } else {
                // 理论上 只调用一次 调用之前 删除残余数据
//                iDeviceSummaryService.delByType(Arrays.asList(WATER_FACTORY_PM, WATER_FACTORY_IN_PM,
//                        WATER_FACTORY_OUT_PM, WATER_FACTORY_IN_TURBID, WATER_FACTORY_OUT_TURBID, WATER_FACTORY_TURBID, WATER_FACTORY_DRUG,
//                        WATER_FACTORY_USED_ELECTRICITY, WATER_FACTORY_OUT_FM, WATER_FACTORY_IN_PH, WATER_FACTORY_OUT_PH, WATER_FACTORY_PH,
//                        WATER_FACTORY_IN_OXYGEN, WATER_FACTORY_OUT_OXYGEN, WATER_FACTORY_TEMPERATURE, WATER_FACTORY_CHLORINE, WATER_FACTORY_IN_FM), null);
                Date curr = new Date();
                // 24个月之前的数据
                Date tempDate = DateUtils.day(curr, -720);
                //  Date yesterday = DateUtils.day(curr, -1);

                //  每天一条
                List<DeviceSummaryForm> list = new ArrayList();
                while (tempDate.getTime() <= curr.getTime()) {
                    list = this.calculationSummary(waterFactoryList, tempDate, loginCas.getEnterpriseId());
                    iDeviceSummaryService.adds(list);
                    tempDate = DateUtils.day(tempDate, 1);
                }
                iDeviceSummaryService.addCuurs(list);

                tempDate = DateUtils.day(curr, -90);
                List<DeviceSummaryForm> list1 = new ArrayList();
                while (tempDate.getTime() <= curr.getTime()) {
                    list1 = this.calculationHour(waterFactoryList, tempDate, loginCas.getEnterpriseId(), 24);
                    iDeviceSummaryService.adds(list1);
                    tempDate = DateUtils.day(tempDate, 1);
                }
                iDeviceSummaryService.addCuurs(list1.subList(list1.size() - 1 - waterFactoryList.size() * 3, list1.size() - 1));

                // 把最后一条更新到最新数据表
                iDeviceSummaryService.addCuurs(this.calculationCuur(waterFactoryList, loginCas.getEnterpriseId()));
                // 初始化安防开关
                iSecurityFactory.init(loginCas.getEnterpriseId());
            }
            return "初始化数据成功";
        } catch (Exception e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.UNDEFINED_ERROR, e.getMessage(), e);
        }
    }

    @Override
    public Map<String, Map<String, DeviceSummaryVo>> getCurr(DeviceSummaryForm deviceSummaryForm) throws FrameworkRuntimeException {
        LoginCas loginCas = iAuthCasApi.get();
        deviceSummaryForm.setIsDel(NOT_DEL);
        deviceSummaryForm.setEnterpriseId(loginCas.getEnterpriseId());
        deviceSummaryForm.setDataDensity(DATA_DENSITY_DAY);
        List<DeviceSummaryVo> list = iDeviceSummaryService.getCurr(deviceSummaryForm);
        if (list != null && !list.isEmpty()) {
            //按照类型分组
            Map<String, List<DeviceSummaryVo>> map = list.stream().collect(Collectors.groupingBy(DeviceSummaryVo::getSummaryCategory));
            Map<String, Map<String, DeviceSummaryVo>> map1 = new HashMap<>();
            for (String key : map.keySet()) {
                map1.put(key, map.get(key).stream().collect(Collectors.toMap(DeviceSummaryVo::getSummaryType, a -> a, (k1, k2) -> k1)));
            }
            return map1;
        } else {
            return new HashMap<>();
        }
    }

    /**
     * 以日期分组统计  年 月 周 分组
     *
     * @param deviceSummaryForm
     * @throws FrameworkRuntimeException
     */
    @Override
    public List<DeviceSummaryVo> listGroup(DeviceSummaryForm deviceSummaryForm) throws FrameworkRuntimeException {
        LoginCas loginCas = iAuthCasApi.get();
        deviceSummaryForm.setIsDel(NOT_DEL);
        deviceSummaryForm.setEnterpriseId(loginCas.getEnterpriseId());
        // deviceSummaryForm.setDataDensity(DATA_DENSITY_DAY);
        List<DeviceSummaryVo> list = iDeviceSummaryService.listGroup(deviceSummaryForm);
//        List<DeviceSummaryVo> unique = list.stream().collect(
//                Collectors.collectingAndThen(
//                        Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(DeviceSummaryVo::getSummaryDate))), ArrayList::new)
//        );
        return list;
    }

    @Override
    public List<DeviceVo> devicelist(DeviceForm deviceForm) throws FrameworkRuntimeException {
        LoginCas loginCas = iAuthCasApi.get();
        deviceForm.setProductCategory(WATER_FACTORY);
        deviceForm.setIsDel(NOT_DEL);
        deviceForm.setEnterpriseId(loginCas.getEnterpriseId());
        List<DeviceVo> waterFactoryList = iDeviceService.list(deviceForm);
        return waterFactoryList;
    }

    @Override
    public Pagination<Map<String, DeviceSummaryVo>> pagePower(DeviceSummaryForm deviceForm) throws FrameworkRuntimeException {
//        Pagination<DeviceSummaryVo> pagination = iDeviceSummaryService.pagePower(deviceBo);
        LoginCas loginCas = iAuthCasApi.get();
        deviceForm.setIsDel(NOT_DEL);
        deviceForm.setEnterpriseId(loginCas.getEnterpriseId());
        deviceForm.setSummaryType(WATER_FACTORY_USED_ELECTRICITY);
        Pagination<Map<String, DeviceSummaryVo>> pagination = iDeviceSummaryService.pagePower(deviceForm);
        return pagination;
    }

    /**
     * 设备设施保养记录
     *
     * @param maintainLogForm
     * @return
     * @throws FrameworkRuntimeException
     */
    @Override
    public Pagination<MaintainLogVo> pageMaintain(MaintainLogForm maintainLogForm) throws FrameworkRuntimeException {
        LoginCas loginCas = iAuthCasApi.get();
        maintainLogForm.setIsDel(NOT_DEL);
        maintainLogForm.setEnterpriseId(loginCas.getEnterpriseId());
        Pagination<MaintainLogVo> pagination = iMaintainService.pageMaintain(maintainLogForm);
        return pagination;
    }

    @Override
    public Map<String, List<DeviceSummaryVo>> inOutWaterList(DeviceSummaryForm deviceSummaryForm) throws FrameworkRuntimeException {
        LoginCas loginCas = iAuthCasApi.get();
        deviceSummaryForm.setIsDel(NOT_DEL);
        deviceSummaryForm.setEnterpriseId(loginCas.getEnterpriseId());
        deviceSummaryForm.setDataDensity(DATA_DENSITY_DAY);
        deviceSummaryForm.setSummaryCategory(WATER_FACTORY);
        // 控制时间 多查出一天 计算当天用水量
//        if (StringUtils.isNotBlank(deviceSummaryForm.getGroup()) && deviceSummaryForm.getStartDate() != null) {
//            switch (deviceSummaryForm.getGroup()) {
//                case "year":
//                    deviceSummaryForm.setStartDate(DateUtils.year(deviceSummaryForm.getStartDate(), -1));
//                    break;
//                case "month":
//                    deviceSummaryForm.setStartDate(DateUtils.month(deviceSummaryForm.getStartDate(), -1));
//                    break;
//                case "week":
//                    DateTime dateTime = new DateTime(deviceSummaryForm.getStartDate());
//                    deviceSummaryForm.setStartDate(dateTime.withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0).plusWeeks(-1).toDate());
//                    break;
//                case "day":
//                    deviceSummaryForm.setStartDate(DateUtils.day(deviceSummaryForm.getStartDate(), -1));
//                    break;
//            }
//        }
//        List<DeviceSummaryVo> inWaterList = new ArrayList<>();
//        List<DeviceSummaryVo> outWaterList = new ArrayList<>();
//        if (StringUtils.isEmpty(deviceSummaryForm.getDeviceId()) && !"day".equals(deviceSummaryForm.getGroup())) {
//            // 查询进水
//            deviceSummaryForm.setSummaryType(WATER_FACTORY_IN_WATER);
//            inWaterList = iDeviceSummaryService.inOutWaterListGroup(deviceSummaryForm);
//            // 查询出水
//            deviceSummaryForm.setSummaryType(WATER_FACTORY_OUT_WATER);
//            outWaterList = iDeviceSummaryService.inOutWaterListGroup(deviceSummaryForm);
//        } else {
//            // 查询进水
//            deviceSummaryForm.setSummaryType(WATER_FACTORY_IN_WATER);
//            inWaterList = iDeviceSummaryService.listGroup(deviceSummaryForm);
//            // 查询出水
//            deviceSummaryForm.setSummaryType(WATER_FACTORY_OUT_WATER);
//            outWaterList = iDeviceSummaryService.listGroup(deviceSummaryForm);
//        }

//
        Map<String, List<DeviceSummaryVo>> map = new HashMap<>();
        List<DeviceSummaryVo> proportionList = new ArrayList<>();
        List<DeviceSummaryVo> inWaterListTemp = new ArrayList<>();
        List<DeviceSummaryVo> outWaterListTemp = new ArrayList<>();
//
//        if (inWaterList.size() >= 2 && outWaterList.size() >= 2) {
//            // 计算 比例  自用率 等于 （进水-出水）/ 进水
//            for (int i = 0; i < inWaterList.size() - 1; i++) {
//                // 前一天的用水量
//                BigDecimal inOld = new BigDecimal(inWaterList.get(i + 1).getMaxval().toString());
//                BigDecimal outOld = new BigDecimal(outWaterList.get(i + 1).getMaxval().toString());
//
//                // 当天用水量
//                DeviceSummaryVo inWater = inWaterList.get(i);
//                DeviceSummaryVo outWater = outWaterList.get(i);
//                BigDecimal in = new BigDecimal(inWater.getMaxval().toString());
//                BigDecimal out = new BigDecimal(outWater.getMaxval().toString());
//
//                DeviceSummaryVo inWaterTemp = BeanUtils.copyProperties(inWater, DeviceSummaryVo.class);
//                BigDecimal inWaterCurr = in.subtract(inOld);
//                inWaterTemp.setVal(inWaterCurr.doubleValue());
//                inWaterListTemp.add(inWaterTemp);
//
//                DeviceSummaryVo outWaterTemp = BeanUtils.copyProperties(outWater, DeviceSummaryVo.class);
//                BigDecimal outWaterCurr = out.subtract(outOld);
//                outWaterTemp.setVal(outWaterCurr.doubleValue());
//                outWaterListTemp.add(outWaterTemp);
//
//                BigDecimal result = inWaterCurr.subtract(outWaterCurr).divide(inWaterCurr, 4);
//                result.setScale(4, BigDecimal.ROUND_HALF_UP);
//                DeviceSummaryVo deviceSummaryVo = new DeviceSummaryVo();
//                deviceSummaryVo.setVal(Double.valueOf(result.toString()));
//                deviceSummaryVo.setSummaryDate(inWater.getSummaryDate());
//                proportionList.add(deviceSummaryVo);
//            }

        deviceSummaryForm.setSummaryType(WATER_FACTORY_IN_WATER_CURR);
        List<DeviceSummaryVo> inWaterList = iDeviceSummaryService.listGroup(deviceSummaryForm);
        // 查询出水
        deviceSummaryForm.setSummaryType(WATER_FACTORY_OUT_WATER_CURR);
        List<DeviceSummaryVo> outWaterList = iDeviceSummaryService.listGroup(deviceSummaryForm);

        for (int i = 0; i < inWaterList.size(); i++) {
            DeviceSummaryVo in = inWaterList.get(i);
            in.setVal(Double.valueOf(in.getSumval()));
            DeviceSummaryVo out = outWaterList.get(i);
            out.setVal(Double.valueOf(out.getSumval()));

            BigDecimal inWaterCurr = new BigDecimal(in.getSumval());
            BigDecimal outWaterCurr = new BigDecimal(out.getSumval());

            BigDecimal result = inWaterCurr.subtract(outWaterCurr).divide(inWaterCurr, 4);
            result.setScale(4, BigDecimal.ROUND_HALF_UP);
            DeviceSummaryVo deviceSummaryVo = new DeviceSummaryVo();
            deviceSummaryVo.setVal(Double.valueOf(result.toString()));
            deviceSummaryVo.setSummaryDate(in.getSummaryDate());
            proportionList.add(deviceSummaryVo);
        }
//
//        for (DeviceSummaryVo temp : inWaterList) {
//            temp.setVal(Double.valueOf(temp.getSumval()));
//        }
//
//        for (DeviceSummaryVo temp : outWaterList) {
//            temp.setVal(Double.valueOf(temp.getSumval()));
//        }

        DeviceSummaryForm deviceSummaryForm1 = new DeviceSummaryForm();
        deviceSummaryForm1.setSummaryCategory(WATER_FACTORY);
        deviceSummaryForm1.setSummaryType(WATER_FACTORY_IN_WATER_CURR);
        DeviceSummaryVo inSummaryVo = this.getSummary(deviceSummaryForm1);
        inSummaryVo.setVal(Double.valueOf(inSummaryVo.getSumval()));

        DeviceSummaryForm deviceSummaryForm2 = new DeviceSummaryForm();
        deviceSummaryForm2.setSummaryCategory(WATER_FACTORY);
        deviceSummaryForm2.setSummaryType(WATER_FACTORY_OUT_WATER_CURR);
        DeviceSummaryVo outSummaryVo = this.getSummary(deviceSummaryForm2);
        outSummaryVo.setVal(Double.valueOf(outSummaryVo.getSumval()));

        Map<String, Map<String, DeviceSummaryVo>> curr = this.getCurr(new DeviceSummaryForm());
        DeviceSummaryVo inWaterCurr = curr.get(WATER_FACTORY).get(WATER_FACTORY_IN_WATER_CURR);
        inWaterCurr.setVal(Double.valueOf(inWaterCurr.getSumval()));

        DeviceSummaryVo outWaterCurr = curr.get(WATER_FACTORY).get(WATER_FACTORY_OUT_WATER_CURR);
        outWaterCurr.setVal(Double.valueOf(outWaterCurr.getSumval()));


        map.put("inWater", inWaterList);
        map.put("inWaterCurr", Arrays.asList(inWaterCurr));
        map.put("inSumWater", Arrays.asList(inSummaryVo));

        map.put("outWater", outWaterList);
        map.put("outWaterCurr", Arrays.asList(outWaterCurr));
        map.put("outSumWater", Arrays.asList(outSummaryVo));
        map.put("proportion", proportionList);
// }
        return map;
    }

    @Override
    public Map<String, List<DeviceSummaryVo>> getMapByTypes(DeviceSummaryForm deviceSummaryForm) throws FrameworkRuntimeException {
        LoginCas loginCas = iAuthCasApi.get();
        deviceSummaryForm.setIsDel(NOT_DEL);
        deviceSummaryForm.setEnterpriseId(loginCas.getEnterpriseId());
        // deviceSummaryForm.setDataDensity(DATA_DENSITY_DAY);
        Map<String, List<DeviceSummaryVo>> map = new HashMap<>();
        if (deviceSummaryForm.getSummaryTypes() != null && !deviceSummaryForm.getSummaryTypes().isEmpty()) {
            for (String type : deviceSummaryForm.getSummaryTypes()) {
                deviceSummaryForm.setSummaryType(type);
                List<DeviceSummaryVo> list = iDeviceSummaryService.listGroup(deviceSummaryForm);
                map.put(type, list);
            }
        }
        return map;
    }

    @Override
    public Pagination<Map<String, DeviceSummaryVo>> pageInOutWater(DeviceSummaryForm deviceForm) throws FrameworkRuntimeException {
        LoginCas loginCas = iAuthCasApi.get();
        deviceForm.setIsDel(NOT_DEL);
        deviceForm.setEnterpriseId(loginCas.getEnterpriseId());
        deviceForm.setDataDensity(DATA_DENSITY_DAY);
        deviceForm.setSummaryCategory(WATER_FACTORY);
        // 控制时间 多查出一天 计算当天用水量
//        if (StringUtils.isNotBlank(deviceForm.getGroup()) && deviceForm.getStartDate() != null) {
//            switch (deviceForm.getGroup()) {
//                case "year":
//                    deviceForm.setStartDate(DateUtils.year(deviceForm.getStartDate(), -1));
//                    break;
//                case "month":
//                    deviceForm.setStartDate(DateUtils.month(deviceForm.getStartDate(), -1));
//                    break;
//                case "week":
//                    DateTime dateTime = new DateTime(deviceForm.getStartDate());
//                    deviceForm.setStartDate(dateTime.withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0).plusWeeks(-1).toDate());
//                    break;
//                case "day":
//                    deviceForm.setStartDate(DateUtils.day(deviceForm.getStartDate(), -1));
//                    break;
//            }
//        }
        Pagination<Map<String, DeviceSummaryVo>> pagination = iDeviceSummaryService.pageInOutWater(deviceForm);
        return pagination;
    }

    @Override
    public DeviceSummaryVo getSummary(DeviceSummaryForm deviceSummaryForm) throws FrameworkRuntimeException {
        LoginCas loginCas = iAuthCasApi.get();
        deviceSummaryForm.setIsDel(NOT_DEL);
        deviceSummaryForm.setEnterpriseId(loginCas.getEnterpriseId());
        return iDeviceSummaryService.getSummary(deviceSummaryForm);
    }


    /**
     * 计算当天数据
     *
     * @param waterFactoryList
     * @return
     */
    private List<DeviceSummaryForm> calculationCuur(List<DeviceVo> waterFactoryList, String enterpriseId) {
        if (waterFactoryList.isEmpty()) {
            return null;
        }
        List<DeviceSummaryForm> deviceSummaryFormList = new ArrayList<>();
        Date curr = new Date();
        DeviceSummaryForm defaultForm = new DeviceSummaryForm();
        defaultForm.setUserBy("system");
        defaultForm.setSummaryDate(curr);
        defaultForm.setCurr(new Date());
        defaultForm.setIsDel(NOT_DEL);
        defaultForm.setEnterpriseId(enterpriseId);
        defaultForm.setVal(0D);
        defaultForm.setPressureValue(0D);
        defaultForm.setDataDensity(DATA_DENSITY_DAY);
        for (DeviceVo deviceVo : waterFactoryList) {
            defaultForm.setDeviceId(deviceVo.getDeviceId());
            // 计算压力
            deviceSummaryFormList.addAll(this.calculationByType(defaultForm, WATER_FACTORY_PM));
            deviceSummaryFormList.addAll(this.calculationByType(defaultForm, WATER_FACTORY_IN_PM));
            deviceSummaryFormList.addAll(this.calculationByType(defaultForm, WATER_FACTORY_OUT_PM));
            // 浊度
            deviceSummaryFormList.addAll(this.calculationByType(defaultForm, WATER_FACTORY_IN_TURBID));
            deviceSummaryFormList.addAll(this.calculationByType(defaultForm, WATER_FACTORY_OUT_TURBID));
            deviceSummaryFormList.addAll(this.calculationByType(defaultForm, WATER_FACTORY_TURBID));
            // 药物余量
            deviceSummaryFormList.addAll(this.calculationByType(defaultForm, WATER_FACTORY_DRUG));
            // PH
            deviceSummaryFormList.addAll(this.calculationByType(defaultForm, WATER_FACTORY_PH));
            deviceSummaryFormList.addAll(this.calculationByType(defaultForm, WATER_FACTORY_IN_PH));
            deviceSummaryFormList.addAll(this.calculationByType(defaultForm, WATER_FACTORY_OUT_PH));
            // 含氧量
            deviceSummaryFormList.addAll(this.calculationByType(defaultForm, WATER_FACTORY_IN_OXYGEN));
            deviceSummaryFormList.addAll(this.calculationByType(defaultForm, WATER_FACTORY_OUT_OXYGEN));
            // 含氯量
            deviceSummaryFormList.addAll(this.calculationByType(defaultForm, WATER_FACTORY_CHLORINE));
            // 温度
            deviceSummaryFormList.addAll(this.calculationByType(defaultForm, WATER_FACTORY_TEMPERATURE, WATER_FACTORY));

        }
        return deviceSummaryFormList;
    }

    /**
     * 计算历史数据 每天一条数据
     *
     * @param waterFactoryList
     * @param tempDate
     * @param enterpriseId
     * @return
     */
    private List<DeviceSummaryForm> calculationSummary(List<DeviceVo> waterFactoryList, Date tempDate, String enterpriseId) {
        if (waterFactoryList.isEmpty()) {
            return null;
        }
        List<DeviceSummaryForm> deviceSummaryFormList = new ArrayList<>();
        DeviceSummaryForm defaultForm = new DeviceSummaryForm();
        defaultForm.setUserBy("system");
        defaultForm.setSummaryDate(tempDate);
        defaultForm.setCurr(new Date());
        defaultForm.setIsDel(NOT_DEL);
        defaultForm.setEnterpriseId(enterpriseId);
        defaultForm.setVal(0D);
        defaultForm.setPressureValue(0D);
        defaultForm.setDataDensity(DATA_DENSITY_DAY);
        for (DeviceVo deviceVo : waterFactoryList) {
            defaultForm.setDeviceId(deviceVo.getDeviceId());
            // 需要历史数据的种类明细
            // 计算电量
            deviceSummaryFormList.addAll(this.calculationElectricity(defaultForm));
            // 流量
            deviceSummaryFormList.addAll(this.calculationByType(defaultForm, WATER_FACTORY_IN_FM, WATER_FACTORY));
            deviceSummaryFormList.addAll(this.calculationByType(defaultForm, WATER_FACTORY_OUT_FM, WATER_FACTORY));
            deviceSummaryFormList.addAll(this.calculationByType(defaultForm, WATER_FACTORY_IN_WATER_CURR, WATER_FACTORY));
//            // 温度
//            deviceSummaryFormList.addAll(this.calculationByType(defaultForm, WATER_FACTORY_TEMPERATURE, WATER_FACTORY_MEDICINE_POND));
//            // 氯气
            deviceSummaryFormList.addAll(this.calculationByType(defaultForm, WATER_FACTORY_CHLORINE, WATER_FACTORY_MEDICINE_POND));
        }
        return deviceSummaryFormList;
    }

    /**
     * 计算历史数据 每小时
     *
     * @param waterFactoryList
     * @param tempDate
     * @param enterpriseId
     * @param hour
     * @return
     */
    private List<DeviceSummaryForm> calculationHour(List<DeviceVo> waterFactoryList, Date tempDate, String enterpriseId, int hour) {
        if (waterFactoryList.isEmpty()) {
            return null;
        }
        List<DeviceSummaryForm> deviceSummaryFormList = new ArrayList<>();
        Date curr = new Date();
        while (hour > 0) {
            DeviceSummaryForm defaultForm = new DeviceSummaryForm();
            defaultForm.setUserBy("system");
            defaultForm.setSummaryDate(tempDate);
            defaultForm.setCurr(new Date());
            defaultForm.setIsDel(NOT_DEL);
            defaultForm.setEnterpriseId(enterpriseId);
            defaultForm.setVal(0D);
            defaultForm.setPressureValue(0D);
            defaultForm.setDataDensity(DATA_DENSITY_HOUR);
            for (DeviceVo deviceVo : waterFactoryList) {
                defaultForm.setDeviceId(deviceVo.getDeviceId());
                // 温度
                deviceSummaryFormList.addAll(this.calculationByType(defaultForm, WATER_FACTORY_TEMPERATURE, WATER_FACTORY_MEDICINE_POND));
                // 压力
                deviceSummaryFormList.addAll(this.calculationByType(defaultForm, WATER_FACTORY_PM, WATER_FACTORY_MEDICINE_POND));
                // 药物余量
                deviceSummaryFormList.addAll(this.calculationByType(defaultForm, WATER_FACTORY_DRUG, WATER_FACTORY_MEDICINE_POND));
            }
            tempDate = DateUtils.hour(tempDate, 1);
            if (tempDate.getTime() > curr.getTime()) {
                break;
            }
            hour = hour - 1;
        }
        return deviceSummaryFormList;
    }


    /**
     * 计算电量 特殊计算
     *
     * @param defaultForm
     * @return
     */
    private List<DeviceSummaryForm> calculationElectricity(DeviceSummaryForm defaultForm) {
        List<DeviceSummaryForm> list = new ArrayList<>();
        Random random = new Random();
        defaultForm.setSummaryType(WATER_FACTORY_USED_ELECTRICITY);
        // 沉淀池
        DeviceSummaryForm precipitatePond = BeanUtils.copyProperties(defaultForm, DeviceSummaryForm.class);
        precipitatePond.setSummaryCategory(WATER_FACTORY_PRECIPITATE_POND);
        Double precipitatePondval = (10 + random.nextDouble() * 50);
        precipitatePond.setVal(CalculationUtils.doubleFix(precipitatePondval, 2));
        precipitatePond.setId(UuidUtils.getUuid());
        list.add(precipitatePond);
        // 药池
        DeviceSummaryForm medicinePond = BeanUtils.copyProperties(defaultForm, DeviceSummaryForm.class);
        medicinePond.setSummaryCategory(WATER_FACTORY_MEDICINE_POND);
        Double medicinePondval = (10 + random.nextDouble() * 50);
        medicinePond.setVal(CalculationUtils.doubleFix(medicinePondval, 2));
        medicinePond.setId(UuidUtils.getUuid());
        list.add(medicinePond);
        // 过滤池
        DeviceSummaryForm filterPond = BeanUtils.copyProperties(defaultForm, DeviceSummaryForm.class);
        filterPond.setSummaryCategory(WATER_FACTORY_FILTER_POND);
        Double filterPondval = (10 + random.nextDouble() * 50);
        filterPond.setVal(CalculationUtils.doubleFix(filterPondval, 2));
        filterPond.setId(UuidUtils.getUuid());
        list.add(filterPond);

        // 高压泵池
        DeviceSummaryForm highPumpPond = BeanUtils.copyProperties(defaultForm, DeviceSummaryForm.class);
        highPumpPond.setSummaryCategory(WATER_FACTORY_HIGH_PUMP_POND);
        Double highPumpPondval = (10 + random.nextDouble() * 50);
        highPumpPond.setVal(CalculationUtils.doubleFix(highPumpPondval, 2));
        highPumpPond.setId(UuidUtils.getUuid());
        list.add(highPumpPond);

        // 每个水厂生成其他电量的和
        DeviceSummaryForm water = BeanUtils.copyProperties(defaultForm, DeviceSummaryForm.class);
        water.setSummaryCategory(WATER_FACTORY);
        water.setVal(CalculationUtils.doubleFix(precipitatePondval + medicinePondval + filterPondval + highPumpPondval, 2));
        water.setId(UuidUtils.getUuid());
        list.add(water);
        return list;
    }


    /**
     * 根据类型计算参数
     *
     * @param defaultForm
     * @return
     */
    private List<DeviceSummaryForm> calculationByType(DeviceSummaryForm defaultForm, String type) {
        List<DeviceSummaryForm> list = new ArrayList<>();
        Random random = new Random();
        defaultForm.setSummaryType(type);
        // 循环有几种池子
        for (Map.Entry<String, Map<String, String>> m : water_factory_properties.entrySet()) {
            Map<String, String> properties = m.getValue();
            // System.out.println("key:" + m.getKey() + " value:" + m.getValue());
            if (properties.containsKey(type)) {
                String property = properties.get(type);
                // 计算随机值
                JSONObject rddata = com.alibaba.fastjson.JSON.parseObject(property);
                String max = rddata.getString("max");
                String min = rddata.getString("min");
                // 沉淀池
                DeviceSummaryForm deviceSummaryForm = BeanUtils.copyProperties(defaultForm, DeviceSummaryForm.class);
                deviceSummaryForm.setSummaryCategory(m.getKey());
//                Double val = (Double.valueOf(min) + random.nextDouble() * Double.valueOf(max));
                Double val = (Double.valueOf(min) + random.nextDouble() * (Double.valueOf(max) - Double.valueOf(min)));
                deviceSummaryForm.setVal(CalculationUtils.doubleFix(val, 2));
                deviceSummaryForm.setId(UuidUtils.getUuid());
                list.add(deviceSummaryForm);
            }
        }
        return list;
    }

    private List<DeviceSummaryForm> calculationByType(DeviceSummaryForm defaultForm, String type, String category) {
        List<DeviceSummaryForm> list = new ArrayList<>();
        Random random = new Random();
        defaultForm.setSummaryType(type);
        if (StringUtils.isBlank(category)) {
            return list;
        }
        // 循环有几种池子
        if (water_factory_properties.containsKey(category)) {
            String property = water_factory_properties.get(category).get(type);
            // 计算随机值
            JSONObject rddata = com.alibaba.fastjson.JSON.parseObject(property);
            String max = rddata.getString("max");
            String min = rddata.getString("min");
            // 沉淀池
            DeviceSummaryForm deviceSummaryForm = BeanUtils.copyProperties(defaultForm, DeviceSummaryForm.class);
            deviceSummaryForm.setSummaryCategory(category);
            Double val = (Double.valueOf(min) + random.nextDouble() * (Double.valueOf(max) - Double.valueOf(min)));
            deviceSummaryForm.setVal(CalculationUtils.doubleFix(val, 2));
            deviceSummaryForm.setId(UuidUtils.getUuid());
            if (WATER_FACTORY_IN_WATER_CURR.equals(type)) {
                DeviceSummaryForm temp = BeanUtils.copyProperties(defaultForm, DeviceSummaryForm.class);
                temp.setSummaryCategory(category);
                temp.setSummaryType(WATER_FACTORY_OUT_WATER_CURR);
                // 进水-减去一个数 = 出水 控制在100 以内
                temp.setVal(CalculationUtils.doubleFix(val - random.nextDouble() * 100, 2));
                temp.setId(UuidUtils.getUuid());
                list.add(temp);
            }
            list.add(deviceSummaryForm);
        }
        return list;
    }
}