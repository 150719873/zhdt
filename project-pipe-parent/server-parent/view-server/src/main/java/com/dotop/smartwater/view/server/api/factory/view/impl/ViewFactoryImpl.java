package com.dotop.smartwater.view.server.api.factory.view.impl;

import com.dotop.smartwater.view.server.core.area.form.AreaForm;
import com.dotop.smartwater.view.server.core.area.vo.AreaVo;
import com.dotop.smartwater.view.server.service.brustpipe.IBrustPipeOperationsService;
import com.dotop.smartwater.view.server.service.device.IDeviceDataService;
import com.dotop.smartwater.view.server.service.device.IDeviceService;
import com.dotop.smartwater.view.server.service.device.IDeviceSummaryService;
import com.dotop.smartwater.view.server.service.device.IWaterMeterService;
import com.dotop.smartwater.view.server.service.workorder.IWorkOrderSummaryService;
import com.dotop.pipe.auth.cas.web.IAuthCasWeb;
import com.dotop.pipe.auth.core.vo.auth.LoginCas;
import com.dotop.pipe.core.bo.alarm.AlarmBo;
import com.dotop.pipe.core.bo.brustpipe.BrustPipeBo;
import com.dotop.pipe.core.bo.device.DeviceBo;
import com.dotop.pipe.core.bo.workOrder.WorkOrderBo;
import com.dotop.pipe.core.form.AlarmForm;
import com.dotop.pipe.core.form.BrustPipeForm;
import com.dotop.pipe.core.form.WorkOrderForm;
import com.dotop.pipe.core.vo.alarm.AlarmVo;
import com.dotop.pipe.core.vo.brustpipe.BrustPipeVo;
import com.dotop.pipe.core.vo.device.DevicePropertyVo;
import com.dotop.smartwater.dependence.core.common.RootModel;
import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.BeanUtils;
import com.dotop.smartwater.dependence.core.utils.DateUtils;
import com.dotop.smartwater.dependence.core.utils.UuidUtils;
import com.dotop.smartwater.view.server.api.factory.view.IViewFactory;
import com.dotop.smartwater.view.server.api.factory.view.IWaterFactoryViewFactory;
import com.dotop.smartwater.view.server.core.brustpipe.vo.Brust;
import com.dotop.smartwater.view.server.core.brustpipe.vo.BrustPipeOperationsVo;
import com.dotop.smartwater.view.server.core.device.form.DeviceDataForm;
import com.dotop.smartwater.view.server.core.device.form.DeviceForm;
import com.dotop.smartwater.view.server.core.device.form.DeviceSummaryForm;
import com.dotop.smartwater.view.server.core.device.vo.DeviceDataVo;
import com.dotop.smartwater.view.server.core.device.vo.DeviceSummaryVo;
import com.dotop.smartwater.view.server.core.device.vo.DeviceVo;
import com.dotop.smartwater.view.server.core.owner.form.OwnerForm;
import com.dotop.smartwater.view.server.core.workorder.vo.WorkOrderSummaryVo;
import com.dotop.smartwater.view.server.service.alarm.IAlarmSummaryService;
import com.dotop.smartwater.view.server.service.area.IAreaService;
import com.dotop.smartwater.view.server.service.owner.IOwnerService;
import com.dotop.smartwater.view.server.utils.CalculationUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static com.dotop.pipe.core.constants.CommonConstants.*;
import static com.dotop.smartwater.dependence.core.common.RootModel.NOT_DEL;
import static com.dotop.smartwater.view.server.constants.ViewConstants.*;

/**
 *
 */
@Component
public class ViewFactoryImpl implements IViewFactory {

    private final Logger logger = LoggerFactory.getLogger(ViewFactoryImpl.class);

    @Autowired
    private IDeviceDataService iDeviceDataService;
    @Autowired
    private IDeviceSummaryService iDeviceSummaryService;
    @Autowired
    private IDeviceService ivDeviceService;
    @Autowired
    private IOwnerService iOwnerService;
    @Autowired
    private IAreaService iAreaService;
    @Autowired
    private IAuthCasWeb iAuthCasApi;
    @Autowired
    private IWorkOrderSummaryService iWorkOrderSummaryService;

    @Autowired
    private IAlarmSummaryService iAlarmSummaryService;

    @Autowired
    private IBrustPipeOperationsService iBrustPipeOperationsService;

    @Autowired
    private IWaterMeterService waterMeterService;

    @Autowired
    private IWaterFactoryViewFactory iWaterFactoryViewFactory;

    @Override
    public List<DeviceSummaryVo> list(DeviceSummaryForm deviceSummaryForm) throws FrameworkRuntimeException {
        deviceSummaryForm.setIsDel(NOT_DEL);
        List<DeviceSummaryVo> list;
        if ("week".equals(deviceSummaryForm.getGroup())) {
            Date summaryDate = deviceSummaryForm.getSummaryDate();
            deviceSummaryForm.setSummaryDate(null);
            Date date = DateUtils.parseDate(DateUtils.formatDate(summaryDate));
            deviceSummaryForm.setEndDate(DateUtils.day(date, 1));
            deviceSummaryForm.setStartDate(DateUtils.day(date, -6));
            return iDeviceSummaryService.listFactoryNotGroup(deviceSummaryForm);
        }
        if (WATER_FACTORY.equals(deviceSummaryForm.getSummaryCategory())) {
            list = iDeviceSummaryService.listFactory(deviceSummaryForm);
        } else {
            list = iDeviceSummaryService.list(deviceSummaryForm);
        }
        return list;
    }

    @Override
    public List<DeviceSummaryVo> listCurr(DeviceSummaryForm deviceSummaryForm) throws FrameworkRuntimeException {
        deviceSummaryForm.setIsDel(NOT_DEL);
        if ("week".equals(deviceSummaryForm.getGroup())) {
            Date summaryDate = deviceSummaryForm.getSummaryDate();
            deviceSummaryForm.setSummaryDate(null);
            Date date = DateUtils.parseDate(DateUtils.formatDate(summaryDate));
            deviceSummaryForm.setEndDate(DateUtils.day(date, 1));
            deviceSummaryForm.setStartDate(DateUtils.day(date, -6));
        }
        List<DeviceSummaryVo> list;
        if (WATER_FACTORY.equals(deviceSummaryForm.getSummaryCategory())) {
            list = iDeviceSummaryService.listFactoryCurr(deviceSummaryForm);
        } else {
            list = iDeviceSummaryService.listCurr(deviceSummaryForm);
        }
        return list;
    }

    @Override
    public Pagination<DeviceSummaryVo> pageFactory(DeviceSummaryForm deviceSummaryForm) throws FrameworkRuntimeException {
        try {
            Integer isDel = RootModel.NOT_DEL;
            deviceSummaryForm.setIsDel(isDel);
            if (deviceSummaryForm.getSummaryDate() == null) {
                deviceSummaryForm.setSummaryDate(new Date());
            }
            // 查询summary水厂分页
            deviceSummaryForm.setDataDensity(DATA_DENSITY_DAY);
            deviceSummaryForm.setSummaryCategory(WATER_FACTORY);
            deviceSummaryForm.setSummaryType(WATER_FACTORY_IN_WATER);
            Page<Object> pageHelper = PageHelper.startPage(deviceSummaryForm.getPage(), deviceSummaryForm.getPageSize());
            List<DeviceSummaryVo> list = iDeviceSummaryService.listFactory(deviceSummaryForm);
            Pagination<DeviceSummaryVo> pagination = new Pagination<>(deviceSummaryForm.getPageSize(), deviceSummaryForm.getPage());
            pagination.setData(list);
            pagination.setTotalPageSize(pageHelper.getTotal());
            for (DeviceSummaryVo deviceSummaryVo : pagination.getData()) {
                DeviceSummaryForm search = new DeviceSummaryForm();
                search.setDeviceId(deviceSummaryVo.getDeviceId());
                Map<String, Map<String, DeviceSummaryVo>> curr = iWaterFactoryViewFactory.getCurr(search);
                deviceSummaryVo.setCurrData(curr);
            }
            return pagination;
        } catch (Exception e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.UNDEFINED_ERROR, e.getMessage(), e);
        }
    }

    @Override
    public Boolean isInit(DeviceSummaryForm deviceSummaryForm) throws FrameworkRuntimeException {
        deviceSummaryForm.setIsDel(NOT_DEL);
        deviceSummaryForm.setDataDensity(DATA_DENSITY_MONTH);
        return iDeviceSummaryService.isInit(deviceSummaryForm);
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
        synchronized (this) {
            try {
                //--------------------初始化用水----------------------
                LoginCas loginCas = iAuthCasApi.get();
                DeviceDataForm deviceDataForm = new DeviceDataForm();
                if (deviceSummaryForm.getSummaryDate() == null) {
                    deviceSummaryForm.setSummaryDate(new Date());
                }
                deviceSummaryForm.setUserBy(loginCas.getUserName());
                deviceSummaryForm.setCurr(new Date());
                deviceSummaryForm.setIsDel(NOT_DEL);
                deviceSummaryForm.setEnterpriseId(loginCas.getEnterpriseId());
                deviceSummaryForm.setVal(0D);
                deviceSummaryForm.setPressureValue(0D);
                deviceSummaryForm.setDataDensity(DATA_DENSITY_MONTH);
                if (isInit(deviceSummaryForm)) {
                    return "初始化失败，请先删除历史数据";
                }
                
                deviceDataForm.setEnterpriseId(loginCas.getEnterpriseId());
                deviceDataForm.setSearchDate(deviceSummaryForm.getSummaryDate());
                deviceDataForm.setIsDel(NOT_DEL);
                List<DeviceDataVo> deviceDataVos = iDeviceDataService.list(deviceDataForm);
                Random random = new Random();
                List<DeviceSummaryForm> deviceSummaryForms = this.calculationSummary(deviceDataVos, new ArrayList<>(), deviceSummaryForm, random, INIT_MONTHS);
                iDeviceSummaryService.adds(deviceSummaryForms);

                iWaterFactoryViewFactory.init(deviceSummaryForm);
                return "初始化数据成功";
            } catch (Exception e) {
                logger.error(LogMsg.to(e));
                throw new FrameworkRuntimeException(BaseExceptionConstants.UNDEFINED_ERROR, e.getMessage(), e);
            }
        }
    }

    /**
     * 递推所有信息
     *
     * @param deviceDataVos
     * @param deviceSummaryForms
     * @param defaultForm
     * @param random
     * @param months
     * @return
     */
    private List<DeviceSummaryForm> calculationSummary(List<DeviceDataVo> deviceDataVos,
                                                       List<DeviceSummaryForm> deviceSummaryForms, DeviceSummaryForm defaultForm, Random random, Integer months) {
        if (deviceDataVos.isEmpty()) {
            return deviceSummaryForms;
        }
        Date tempDate = DateUtils.month(deviceDataVos.get(0).getDevSendDate(), months - INIT_MONTHS);
        if (months == -1) {
            tempDate = defaultForm.getSummaryDate();
        }
        final Date summaryDate = tempDate;
        List<DeviceSummaryForm> newForms = new ArrayList<>();
        //总用水量
        DeviceSummaryForm waterSupply = BeanUtils.copy(defaultForm, DeviceSummaryForm.class);
        waterSupply.setSummaryCategory(WATER_SUPPLY);
        waterSupply.setSummaryType(WATER_SUPPLY);
        waterSupply.setSummaryDate(summaryDate);
        waterSupply.setId(UuidUtils.getUuid());
        //商业用水
        DeviceSummaryForm commercial = BeanUtils.copy(defaultForm, DeviceSummaryForm.class);
        commercial.setSummaryCategory(WATER_SUPPLY);
        commercial.setSummaryType(COMMERCIAL_WATER);
        commercial.setSummaryDate(summaryDate);
        commercial.setId(UuidUtils.getUuid());
        //工业用水
        DeviceSummaryForm industrial = BeanUtils.copy(defaultForm, DeviceSummaryForm.class);
        industrial.setSummaryCategory(WATER_SUPPLY);
        industrial.setSummaryType(INDUSTRIAL_WATER);
        industrial.setSummaryDate(summaryDate);
        industrial.setId(UuidUtils.getUuid());
        //居民用水
        DeviceSummaryForm residential = BeanUtils.copy(defaultForm, DeviceSummaryForm.class);
        residential.setSummaryCategory(WATER_SUPPLY);
        residential.setSummaryType(RESIDENTIAL_WATER);
        residential.setSummaryDate(summaryDate);
        residential.setId(UuidUtils.getUuid());
        //终端总数
        DeviceSummaryForm terminalForm = BeanUtils.copy(defaultForm, DeviceSummaryForm.class);
        terminalForm.setId(UuidUtils.getUuid());
        terminalForm.setSummaryCategory(TERMINAL_NUMBER);
        terminalForm.setSummaryType(TERMINAL_NUMBER);
        terminalForm.setSummaryDate(summaryDate);
        //流量计数量
        DeviceSummaryForm fmForm = BeanUtils.copy(defaultForm, DeviceSummaryForm.class);
        fmForm.setId(UuidUtils.getUuid());
        fmForm.setSummaryCategory(TERMINAL_NUMBER);
        fmForm.setSummaryType(FM_NUMBER);
        fmForm.setSummaryDate(summaryDate);
        //压力计数量
        DeviceSummaryForm pmForm = BeanUtils.copy(defaultForm, DeviceSummaryForm.class);
        pmForm.setId(UuidUtils.getUuid());
        pmForm.setSummaryCategory(TERMINAL_NUMBER);
        pmForm.setSummaryType(PM_NUMBER);
        pmForm.setSummaryDate(summaryDate);
        //水质计数量
        DeviceSummaryForm wmForm = BeanUtils.copy(defaultForm, DeviceSummaryForm.class);
        wmForm.setId(UuidUtils.getUuid());
        wmForm.setSummaryCategory(TERMINAL_NUMBER);
        wmForm.setSummaryType(WM_NUMBER);
        wmForm.setSummaryDate(summaryDate);
        //--------------------初始化用户---------------------
        //用户数量
        DeviceSummaryForm ownerForm = BeanUtils.copy(defaultForm, DeviceSummaryForm.class);
        ownerForm.setId(UuidUtils.getUuid());
        ownerForm.setSummaryCategory(OWNER_NUMBER);
        ownerForm.setSummaryType(OWNER_NUMBER);
        ownerForm.setSummaryDate(summaryDate);
        //普通用户
        DeviceSummaryForm ordinaryForm = BeanUtils.copy(defaultForm, DeviceSummaryForm.class);
        ordinaryForm.setId(UuidUtils.getUuid());
        ordinaryForm.setSummaryCategory(OWNER_NUMBER);
        ordinaryForm.setSummaryType(ORDINARY_NUMBER);
        ordinaryForm.setSummaryDate(summaryDate);
        //商业用户
        DeviceSummaryForm commercialForm = BeanUtils.copy(defaultForm, DeviceSummaryForm.class);
        commercialForm.setId(UuidUtils.getUuid());
        commercialForm.setSummaryCategory(OWNER_NUMBER);
        commercialForm.setSummaryType(COMMERCIAL_NUMBER);
        commercialForm.setSummaryDate(summaryDate);
        //工业用户
        DeviceSummaryForm industrialForm = BeanUtils.copy(defaultForm, DeviceSummaryForm.class);
        industrialForm.setId(UuidUtils.getUuid());
        industrialForm.setSummaryCategory(OWNER_NUMBER);
        industrialForm.setSummaryType(INDUSTRIAL_NUMBER);
        industrialForm.setSummaryDate(summaryDate);
        //管网长度
        DeviceSummaryForm pipeForm = BeanUtils.copy(defaultForm, DeviceSummaryForm.class);
        pipeForm.setId(UuidUtils.getUuid());
        pipeForm.setSummaryCategory(PIPE_LENGTH);
        pipeForm.setSummaryType(PIPE_LENGTH);
        pipeForm.setSummaryDate(summaryDate);
        //水厂出水量
        List<DeviceSummaryForm> outWaterList = new ArrayList<>();
        DeviceSummaryForm outWaterForm = BeanUtils.copy(defaultForm, DeviceSummaryForm.class);
        outWaterForm.setSummaryCategory(WATER_FACTORY);
        outWaterForm.setSummaryType(WATER_FACTORY_OUT_WATER);
        outWaterForm.setDataDensity(DATA_DENSITY_DAY);

        //水厂出水量
        List<DeviceSummaryForm> inWaterList = new ArrayList<>();
        Map<String, DeviceSummaryForm> minDayFormMap = new HashMap<>();
        //递规时deviceSummaryForms不为空
        if (!deviceSummaryForms.isEmpty()) {
            for (DeviceSummaryForm perForm : deviceSummaryForms) {
                if (WATER_FACTORY_OUT_WATER.equals(perForm.getSummaryType()) && WATER_FACTORY.equals(perForm.getSummaryCategory())) {
                    //只需要取到上个月最早一天的数据，以相同的系数递推
                    DeviceSummaryForm deviceSummaryForm = minDayFormMap.get(perForm.getDeviceId());
                    if (deviceSummaryForm != null) {
                        if (deviceSummaryForm.getSummaryDate().getTime() > perForm.getSummaryDate().getTime()) {
                            minDayFormMap.put(perForm.getDeviceId(), perForm);
                        }
                    } else {
                        minDayFormMap.put(perForm.getDeviceId(), perForm);
                    }
                }
            }
            double sum = 0d;
            for (DeviceSummaryForm minForm : minDayFormMap.values()) {
                double v = minForm.getVal() * (0.94 + random.nextDouble() / 20);
                sum = sum + v;
                Date perDay = DateUtils.day(minForm.getSummaryDate(), -1);
                DeviceSummaryForm factory = BeanUtils.copy(outWaterForm, DeviceSummaryForm.class);
                factory.setVal(v);
                factory.setSummaryDate(perDay);
                factory.setDeviceId(minForm.getDeviceId());
                factory.setId(UuidUtils.getUuid());
                outWaterList.add(factory);
                // 递推本月数据(若不为更新任务，则需要递推)
                if (months != -1) {
                    DateTime lastDate = new DateTime(perDay);
                    int day = lastDate.getDayOfMonth();
                    double value = v;
                    while (day != 1) {
                        Date date = DateUtils.day(lastDate.toDate(), -1);
                        lastDate = new DateTime(date);
                        day = lastDate.getDayOfMonth();
                        DeviceSummaryForm factory1 = BeanUtils.copy(outWaterForm, DeviceSummaryForm.class);
                        value = value * (0.94 + random.nextDouble() / 20);
                        factory1.setVal(value);
                        factory1.setSummaryDate(date);
                        factory1.setDeviceId(minForm.getDeviceId());
                        factory1.setId(UuidUtils.getUuid());
                        outWaterList.add(factory1);
                    }
                }
            }
            waterSupply.setVal(sum);
            deviceSummaryForms.forEach(perForm -> {
                if (WATER_SUPPLY.equals(perForm.getSummaryType())) {
                    Double waterSupplyAll = waterSupply.getVal();
                    waterSupply.setVal(CalculationUtils.doubleFix(waterSupplyAll, 2));
                    commercial.setVal(CalculationUtils.doubleFix(waterSupplyAll * COMMERCIAL_WATER_COEFFICIENT, 2));
                    industrial.setVal(CalculationUtils.doubleFix(waterSupplyAll * INDUSTRIAL_WATER_COEFFICIENT, 2));
                    residential.setVal(CalculationUtils.doubleFix(waterSupplyAll * RESIDENTIAL_WATER_COEFFICIENT, 2));
                } else if (TERMINAL_NUMBER.equals(perForm.getSummaryType())) {
                    terminalForm.setVal(0D);
                    List<DeviceSummaryForm> collect = deviceSummaryForms.stream().filter(t -> TERMINAL_NUMBER.equals(t.getSummaryCategory())).collect(Collectors.toList());
                    collect.forEach(t -> {
                        Double value = t.getVal() * (0.9 + random.nextDouble() / 10);
                        if (FM_NUMBER.equals(t.getSummaryType())) {
                            fmForm.setVal(CalculationUtils.doubleFix(value, 0));
                            terminalForm.setVal(CalculationUtils.doubleFix(fmForm.getVal() + terminalForm.getVal(), 0));
                        } else if (PM_NUMBER.equals(t.getSummaryType())) {
                            pmForm.setVal(CalculationUtils.doubleFix(value, 0));
                            terminalForm.setVal(CalculationUtils.doubleFix(pmForm.getVal() + terminalForm.getVal(), 0));
                        } else if (WM_NUMBER.equals(t.getSummaryType())) {
                            wmForm.setVal(CalculationUtils.doubleFix(value, 0));
                            terminalForm.setVal(CalculationUtils.doubleFix(wmForm.getVal() + terminalForm.getVal(), 0));
                        }
                    });
                } else if (OWNER_NUMBER.equals(perForm.getSummaryType())) {
                    Double all = perForm.getVal() * (0.9 + random.nextDouble() / 10);
                    ownerForm.setVal(CalculationUtils.doubleFix(all, 0));
                    ordinaryForm.setVal(CalculationUtils.doubleFix(all * ORDINARY_COEFFICIENT, 0));
                    commercialForm.setVal(CalculationUtils.doubleFix(all * COMMERCIAL_COEFFICIENT, 0));
                    industrialForm.setVal(CalculationUtils.doubleFix(all - ordinaryForm.getVal() - commercialForm.getVal(), 0));
                } else if (PIPE_LENGTH.equals(perForm.getSummaryType())) {
                    Double pipeLength = perForm.getVal() * (0.9 + random.nextDouble() / 10);
                    pipeForm.setVal(CalculationUtils.doubleFix(pipeLength, 2));
                } else if (DMA_WATER.equals(perForm.getSummaryType())) {
                    Double all = perForm.getVal() * (0.9 + random.nextDouble() / 10);
                    DeviceSummaryForm areaForm = BeanUtils.copy(defaultForm, DeviceSummaryForm.class);
                    areaForm.setId(UuidUtils.getUuid());
                    areaForm.setSummaryCategory(AREA_WATER);
                    areaForm.setSummaryType(DMA_WATER);
                    areaForm.setName(perForm.getName());
                    areaForm.setAreaCode(perForm.getAreaCode());
                    areaForm.setVal(CalculationUtils.doubleFix(all, 2));
                    areaForm.setSummaryDate(summaryDate);
                    newForms.add(areaForm);
                } else if (SENSOR_FM.equals(perForm.getSummaryType())) {
                    DeviceSummaryForm fm = BeanUtils.copy(defaultForm, DeviceSummaryForm.class);
                    fm.setId(UuidUtils.getUuid());
                    fm.setSummaryCategory(SENSOR);
                    fm.setSummaryType(SENSOR_FM);
                    fm.setSummaryDate(summaryDate);
                    Double all = perForm.getVal() * (0.9 + random.nextDouble() / 10);
                    fm.setVal(CalculationUtils.doubleFix(all, 2));
                    fm.setDeviceId(perForm.getDeviceId());
                    newForms.add(fm);
                } else if (SENSOR_PM.equals(perForm.getSummaryType())) {
                    DeviceSummaryForm pm = BeanUtils.copy(defaultForm, DeviceSummaryForm.class);
                    pm.setId(UuidUtils.getUuid());
                    pm.setSummaryCategory(SENSOR);
                    pm.setSummaryType(SENSOR_PM);
                    pm.setSummaryDate(summaryDate);
                    Double all = perForm.getVal() * (0.9 + random.nextDouble() / 10);
                    pm.setVal(CalculationUtils.doubleFix(all, 2));
                    Double pressure = perForm.getPressureValue() * (0.75 + random.nextDouble() / 2);
                    pm.setPressureValue(CalculationUtils.doubleFix(pressure, 2));
                    pm.setDeviceId(perForm.getDeviceId());
                    newForms.add(pm);
                }
            });
        } else {
            //--------------初始化供水量----------------
            //----------------传感器------------------------
            Double all = 0D;
            for (DeviceDataVo deviceDataVo : deviceDataVos) {
                if (PRODUCT_CATEGORY_WATER_FACTORY.equals(deviceDataVo.getProductType())) {
                    double v = Double.parseDouble(deviceDataVo.getFlwTotalValue());
                    all = all + v;
                    // 当前天数据
                    DeviceSummaryForm factory = BeanUtils.copy(outWaterForm, DeviceSummaryForm.class);
                    factory.setVal(v);
                    factory.setDeviceId(deviceDataVo.getDeviceId());
                    factory.setId(UuidUtils.getUuid());
                    outWaterList.add(factory);
                    // 递推本月数据(若不为更新任务，则需要递推)
                    if (months != -1) {
                        DateTime lastDate = new DateTime(defaultForm.getSummaryDate());
                        int day = lastDate.getDayOfMonth();
                        double value = v;
                        while (day != 1) {
                            Date date = DateUtils.day(lastDate.toDate(), -1);
                            lastDate = new DateTime(date);
                            day = lastDate.getDayOfMonth();
                            DeviceSummaryForm factory1 = BeanUtils.copy(outWaterForm, DeviceSummaryForm.class);
                            value = value * (0.9 + random.nextDouble() / 10);
                            factory1.setVal(value);
                            factory1.setSummaryDate(date);
                            factory1.setDeviceId(deviceDataVo.getDeviceId());
                            factory1.setId(UuidUtils.getUuid());
                            outWaterList.add(factory1);
                        }
                    }
                }
                if (DICTIONARY_SENSORTYPE_FM.equals(deviceDataVo.getProductType())) {
                    DeviceSummaryForm fm = BeanUtils.copy(defaultForm, DeviceSummaryForm.class);
                    fm.setId(UuidUtils.getUuid());
                    fm.setSummaryCategory(SENSOR);
                    fm.setSummaryType(SENSOR_FM);
                    fm.setSummaryDate(summaryDate);
                    fm.setVal(CalculationUtils.doubleFix(deviceDataVo.getFlwMeasure() == null ? 0D : Double.parseDouble(deviceDataVo.getFlwMeasure()), 2));
                    fm.setDeviceId(deviceDataVo.getDeviceId());
                    newForms.add(fm);
                } else if (DICTIONARY_SENSORTYPE_PM.equals(deviceDataVo.getProductType())) {
                    DeviceSummaryForm pm = BeanUtils.copy(defaultForm, DeviceSummaryForm.class);
                    pm.setId(UuidUtils.getUuid());
                    pm.setSummaryCategory(SENSOR);
                    pm.setSummaryType(SENSOR_PM);
                    pm.setSummaryDate(summaryDate);
                    pm.setVal(CalculationUtils.doubleFix(deviceDataVo.getFlwMeasure() == null ? 0D : Double.parseDouble(deviceDataVo.getFlwMeasure()), 2));
                    pm.setPressureValue(CalculationUtils.doubleFix(Double.parseDouble(deviceDataVo.getPressureValue()), 2));
                    pm.setDeviceId(deviceDataVo.getDeviceId());
                    newForms.add(pm);
                }
            }
            waterSupply.setVal(CalculationUtils.doubleFix(all, 2));
            commercial.setVal(CalculationUtils.doubleFix(all * COMMERCIAL_WATER_COEFFICIENT, 2));
            industrial.setVal(CalculationUtils.doubleFix(all * INDUSTRIAL_WATER_COEFFICIENT, 2));
            residential.setVal(CalculationUtils.doubleFix(all * RESIDENTIAL_WATER_COEFFICIENT, 2));
            //--------------------初始化终端---------------------
            DeviceForm deviceForm = new DeviceForm();
            deviceForm.setEnterpriseId(defaultForm.getEnterpriseId());
            deviceForm.setIsDel(NOT_DEL);
            List<DeviceVo> deviceVos = ivDeviceService.list(deviceForm);
            for (DeviceVo deviceVo : deviceVos) {
                switch (deviceVo.getProductType()) {
                    case DICTIONARY_SENSORTYPE_FM:
                        fmForm.setVal(fmForm.getVal() + 1);
                        terminalForm.setVal(terminalForm.getVal() + 1);
                        break;
                    case DICTIONARY_SENSORTYPE_PM:
                        pmForm.setVal(pmForm.getVal() + 1);
                        terminalForm.setVal(terminalForm.getVal() + 1);
                        break;
                    case DICTIONARY_SENSORTYPE_WM:
                        wmForm.setVal(wmForm.getVal() + 1);
                        terminalForm.setVal(terminalForm.getVal() + 1);
                        break;
                    default:
                        break;
                }
            }
            OwnerForm search = new OwnerForm();
            search.setEnterpriseId(defaultForm.getEnterpriseId());
            search.setIsDel(NOT_DEL);
            Integer ownerCount = iOwnerService.count(search);
            ownerForm.setVal(ownerCount.doubleValue());
            ordinaryForm.setVal(CalculationUtils.doubleFix(ownerCount * ORDINARY_COEFFICIENT, 0));
            commercialForm.setVal(CalculationUtils.doubleFix(ownerCount * COMMERCIAL_COEFFICIENT, 0));
            industrialForm.setVal(CalculationUtils.doubleFix(ownerCount - ordinaryForm.getVal() - commercialForm.getVal(), 0));
            //--------------------初始化管道长度---------------------
            deviceForm.setProductCategory(PRODUCT_CATEGORY_PIPE);
            Double pipeLength = ivDeviceService.countLength(deviceForm);
            pipeForm.setVal(CalculationUtils.doubleFix(pipeLength, 2));

            //--------------------初始化区域用水量---------------------
            AreaForm searchArea = new AreaForm();
            searchArea.setEnterpriseId(defaultForm.getEnterpriseId());
            searchArea.setIsDel(NOT_DEL);
            List<AreaVo> areaVos = iAreaService.listDma(searchArea);
            areaVos.forEach(areaVo -> {
                DeviceSummaryForm areaForm = BeanUtils.copy(defaultForm, DeviceSummaryForm.class);
                areaForm.setId(UuidUtils.getUuid());
                areaForm.setSummaryCategory(AREA_WATER);
                areaForm.setSummaryType(DMA_WATER);
                areaForm.setName(areaVo.getName());
                areaForm.setSummaryDate(summaryDate);
                areaForm.setAreaCode(areaVo.getAreaCode());
                if (areaVo.getDeviceData() != null && areaVo.getDeviceData().getFlwTotalValue() != null) {
                    areaForm.setVal(CalculationUtils.doubleFix(Double.parseDouble(areaVo.getDeviceData().getFlwTotalValue()), 2));
                }
                newForms.add(areaForm);
            });
        }
        // outWaterList是从大到小排列
        for (int i = 0; i < outWaterList.size(); i++) {
            DeviceSummaryForm inWaterForm = BeanUtils.copy(outWaterList.get(i), DeviceSummaryForm.class);
            inWaterForm.setSummaryCategory(WATER_FACTORY);
            inWaterForm.setSummaryType(WATER_FACTORY_IN_WATER);
            inWaterForm.setDataDensity(DATA_DENSITY_DAY);
            Double val = outWaterList.get(i).getVal();
            val = val * 1.1;
            if (i < outWaterList.size() && i > 0) {
                // 浮动值
                double n = 0.05 * random.nextDouble();
                double newVal = val +  outWaterList.get(i).getVal() * n;
                double nextVal  = inWaterList.get(i - 1).getVal();
                if (newVal < nextVal && nextVal - newVal > outWaterList.get(i-1).getVal() - outWaterList.get(i).getVal()) {
                    val = newVal;
                }
            }
            inWaterForm.setVal(val);
            inWaterForm.setId(UuidUtils.getUuid());
            inWaterList.add(inWaterForm);
        }
        //用水
        newForms.add(waterSupply);
        newForms.add(commercial);
        newForms.add(industrial);
        newForms.add(residential);
        //终端
        newForms.add(terminalForm);
        newForms.add(fmForm);
        newForms.add(pmForm);
        newForms.add(wmForm);
        //用户
        newForms.add(ownerForm);
        newForms.add(ordinaryForm);
        newForms.add(commercialForm);
        newForms.add(industrialForm);
        //管网
        newForms.add(pipeForm);
        //水厂出水
        newForms.addAll(outWaterList);
        newForms.addAll(inWaterList);
        months--;
        if (months > 0) {
            deviceSummaryForms.addAll(calculationSummary(deviceDataVos, newForms, defaultForm, random, months));
        } else {
            deviceSummaryForms.addAll(newForms);
        }

        return deviceSummaryForms;
    }

    /**
     * 更新本月数据,需要传企业id,使用最新的数据进行更新
     *
     * @return
     * @throws FrameworkRuntimeException
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
    public String updateTask(DeviceSummaryForm deviceSummaryForm) throws FrameworkRuntimeException {
        try {
            deviceSummaryForm.setSummaryDate(new Date());
            deviceSummaryForm.setDataDensity(DATA_DENSITY_MONTH);
            Random random = new Random();
            if (StringUtils.isBlank(deviceSummaryForm.getEnterpriseId())) {
                return "更新失败,需要企业id";
            }
            if (!isInit(deviceSummaryForm)) {
                return "更新失败，请先初始化数据";
            }
            List<DeviceSummaryForm> updateForms = new ArrayList<>();
            List<DeviceSummaryForm> addForms = new ArrayList<>();
            List<DeviceSummaryForm> delForms = new ArrayList<>();
            //查询需要更新的数据
            DeviceDataForm deviceDataForm = new DeviceDataForm();
            deviceDataForm.setEnterpriseId(deviceSummaryForm.getEnterpriseId());
            deviceDataForm.setSearchDate(deviceSummaryForm.getSummaryDate());
            deviceDataForm.setIsDel(NOT_DEL);
            List<DeviceDataVo> deviceDataVos = iDeviceDataService.list(deviceDataForm);
            if (deviceDataVos.isEmpty()) {
                logger.info("无数据需要更新");
                return "更新数据成功";
            }
            //查询当月数据
            List<DeviceSummaryVo> deviceSummaryVos = this.list(deviceSummaryForm);
            DeviceSummaryForm defaultForm = new DeviceSummaryForm();
            defaultForm.setUserBy("system");
            defaultForm.setSummaryDate(deviceSummaryForm.getSummaryDate());
            defaultForm.setCurr(new Date());
            defaultForm.setIsDel(NOT_DEL);
            defaultForm.setEnterpriseId(deviceSummaryForm.getEnterpriseId());
            defaultForm.setVal(0D);
            defaultForm.setPressureValue(0D);
            defaultForm.setDataDensity(DATA_DENSITY_MONTH);
            //为空时需要新增
            if (deviceSummaryVos.isEmpty()) {
                addForms.addAll(this.calculationSummary(deviceDataVos, new ArrayList<>(), defaultForm, random, -1));
            } else {//需要更新
                //供水量
                List<DeviceSummaryVo> temp = deviceSummaryVos.stream().filter(t -> WATER_SUPPLY.equals(t.getSummaryCategory())).collect(Collectors.toList());
                List<DeviceSummaryForm> waterSupplyList = BeanUtils.copy(temp, DeviceSummaryForm.class);
                Double all = 0D;
                for (DeviceDataVo deviceDataVo : deviceDataVos) {
                    if (PRODUCT_CATEGORY_WATER_FACTORY.equals(deviceDataVo.getProductType())) {
                        double v = Double.parseDouble(deviceDataVo.getFlwTotalValue());
                        all = all + v;
                        // 当前天数据
                        DeviceSummaryForm searchFactory = new DeviceSummaryForm();
                        searchFactory.setDeviceId(deviceDataVo.getDeviceId());
                        searchFactory.setSummaryDate(defaultForm.getSummaryDate());
                        searchFactory.setDataDensity(DATA_DENSITY_DAY);
                        searchFactory.setEnterpriseId(defaultForm.getEnterpriseId());
                        searchFactory.setSummaryCategory(WATER_FACTORY);
                        searchFactory.setSummaryType(WATER_FACTORY_OUT_WATER);
                        List<DeviceSummaryVo> list = this.list(searchFactory);
                        if (list.isEmpty()) {
                            DeviceSummaryForm factory = BeanUtils.copy(defaultForm, DeviceSummaryForm.class);
                            factory.setVal(v);
                            factory.setDeviceId(deviceDataVo.getDeviceId());
                            factory.setId(UuidUtils.getUuid());
                            factory.setSummaryCategory(WATER_FACTORY);
                            factory.setSummaryType(WATER_FACTORY_OUT_WATER);
                            factory.setDataDensity(DATA_DENSITY_DAY);
                            addForms.add(factory);
                        } else {
                            DeviceSummaryForm factory = BeanUtils.copy(list.get(0), DeviceSummaryForm.class);
                            if (factory.getVal() != v) {
                                factory.setVal(v);
                                factory.setUserBy(defaultForm.getUserBy());
                                factory.setCurr(defaultForm.getCurr());
                                updateForms.add(factory);
                            }
                        }
                        DeviceSummaryForm searchFactory1 = new DeviceSummaryForm();
                        searchFactory1.setDeviceId(deviceDataVo.getDeviceId());
                        searchFactory1.setSummaryDate(defaultForm.getSummaryDate());
                        searchFactory1.setDataDensity(DATA_DENSITY_DAY);
                        searchFactory1.setEnterpriseId(defaultForm.getEnterpriseId());
                        searchFactory1.setSummaryCategory(WATER_FACTORY);
                        searchFactory1.setSummaryType(WATER_FACTORY_IN_WATER);
                        List<DeviceSummaryVo> list1 = this.list(searchFactory);
                        if (list1.isEmpty()) {
                            DeviceSummaryForm factory = BeanUtils.copy(defaultForm, DeviceSummaryForm.class);
                            factory.setVal(v * (1 + random.nextDouble() / 10));
                            factory.setDeviceId(deviceDataVo.getDeviceId());
                            factory.setId(UuidUtils.getUuid());
                            factory.setSummaryCategory(WATER_FACTORY);
                            factory.setSummaryType(WATER_FACTORY_IN_WATER);
                            factory.setDataDensity(DATA_DENSITY_DAY);
                            addForms.add(factory);
                        } else if (!list.isEmpty() && list.get(0).getVal() != v) {
                            // inWater 有更新
                            DeviceSummaryForm factory = BeanUtils.copy(list.get(0), DeviceSummaryForm.class);
                            factory.setVal(v * (1 + random.nextDouble() / 10));
                            factory.setUserBy(defaultForm.getUserBy());
                            factory.setCurr(defaultForm.getCurr());
                            updateForms.add(factory);
                        }
                    }
                }
                for (DeviceSummaryForm oldForm : waterSupplyList) {
                    oldForm.setSummaryDate(deviceSummaryForm.getSummaryDate());
                    oldForm.setUserBy(defaultForm.getUserBy());
                    oldForm.setCurr(defaultForm.getCurr());
                    switch (oldForm.getSummaryType()) {
                        case WATER_SUPPLY:
                            oldForm.setVal(CalculationUtils.doubleFix(all, 2));
                            break;
                        case COMMERCIAL_WATER:
                            oldForm.setVal(CalculationUtils.doubleFix(all * COMMERCIAL_WATER_COEFFICIENT, 2));
                            break;
                        case INDUSTRIAL_WATER:
                            oldForm.setVal(CalculationUtils.doubleFix(all * INDUSTRIAL_WATER_COEFFICIENT, 2));
                            break;
                        case RESIDENTIAL_WATER:
                            oldForm.setVal(CalculationUtils.doubleFix(all * RESIDENTIAL_WATER_COEFFICIENT, 2));
                            break;
                        default:
                            break;
                    }
                    updateForms.add(oldForm);
                }
                //传感器
                List<DeviceSummaryVo> tempSensor = deviceSummaryVos.stream().filter(t -> SENSOR.equals(t.getSummaryCategory())).collect(Collectors.toList());
                List<DeviceSummaryForm> sensorList = BeanUtils.copy(tempSensor, DeviceSummaryForm.class);
                Map<String, DeviceSummaryForm> sensorSummaryMap = sensorList.stream().collect(Collectors.toMap(DeviceSummaryForm::getDeviceId, s -> s));
                Map<String, DeviceDataVo> deviceDataVoMap = deviceDataVos.stream().filter(deviceDataVo ->
                        DICTIONARY_TYPE_SENSORTYPE.equals(deviceDataVo.getProductCategory())
                                && (DICTIONARY_SENSORTYPE_FM.equals(deviceDataVo.getProductType()) || DICTIONARY_SENSORTYPE_PM.equals(deviceDataVo.getProductType())))
                        .collect(Collectors.toMap(DeviceDataVo::getDeviceId, s -> s));
                for (DeviceDataVo deviceDataVo : deviceDataVos) {
                    if (sensorSummaryMap.get(deviceDataVo.getDeviceId()) == null) {
                        if (DICTIONARY_SENSORTYPE_FM.equals(deviceDataVo.getProductType())) {
                            DeviceSummaryForm fm = BeanUtils.copy(defaultForm, DeviceSummaryForm.class);
                            fm.setId(UuidUtils.getUuid());
                            fm.setSummaryCategory(SENSOR);
                            fm.setSummaryType(SENSOR_FM);
                            fm.setVal(CalculationUtils.doubleFix(deviceDataVo.getFlwMeasure() == null ? 0D : Double.parseDouble(deviceDataVo.getFlwMeasure()), 2));
                            fm.setDeviceId(deviceDataVo.getDeviceId());
                            addForms.add(fm);
                        } else if (DICTIONARY_SENSORTYPE_PM.equals(deviceDataVo.getProductType())) {
                            DeviceSummaryForm pm = BeanUtils.copy(defaultForm, DeviceSummaryForm.class);
                            pm.setId(UuidUtils.getUuid());
                            pm.setSummaryCategory(SENSOR);
                            pm.setSummaryType(SENSOR_PM);
                            pm.setVal(CalculationUtils.doubleFix(deviceDataVo.getFlwMeasure() == null ? 0D : Double.parseDouble(deviceDataVo.getFlwMeasure()), 2));
                            pm.setPressureValue(CalculationUtils.doubleFix(Double.parseDouble(deviceDataVo.getPressureValue()), 2));
                            pm.setDeviceId(deviceDataVo.getDeviceId());
                            addForms.add(pm);
                        }
                    } else {
                        DeviceSummaryForm oldForm = sensorSummaryMap.get(deviceDataVo.getDeviceId());
                        oldForm.setSummaryDate(deviceSummaryForm.getSummaryDate());
                        oldForm.setUserBy(defaultForm.getUserBy());
                        oldForm.setCurr(defaultForm.getCurr());
                        switch (oldForm.getSummaryType()) {
                            case SENSOR_FM:
                                oldForm.setVal(CalculationUtils.doubleFix(Double.parseDouble(deviceDataVo.getFlwTotalValue() == null ? "0" : deviceDataVo.getFlwTotalValue()), 2));
                                break;
                            case SENSOR_PM:
                                oldForm.setVal(CalculationUtils.doubleFix(Double.parseDouble(deviceDataVo.getFlwTotalValue() == null ? "0" : deviceDataVo.getFlwTotalValue()), 2));
                                oldForm.setPressureValue(CalculationUtils.doubleFix(Double.parseDouble(deviceDataVo.getPressureValue() == null ? "0" : deviceDataVo.getPressureValue()), 2));
                                break;
                            default:
                                break;
                        }
                        updateForms.add(oldForm);
                    }
                }
                for (DeviceSummaryForm oldForm : sensorList) {
                    if (deviceDataVoMap.get(oldForm.getDeviceId()) == null) {
                        delForms.add(oldForm);
                    }
                }
                //终端数量
                List<DeviceSummaryVo> temp1 = deviceSummaryVos.stream().filter(t -> TERMINAL_NUMBER.equals(t.getSummaryCategory())).collect(Collectors.toList());
                List<DeviceSummaryForm> terminalList = BeanUtils.copy(temp1, DeviceSummaryForm.class);
                DeviceForm deviceForm = new DeviceForm();
                deviceForm.setIsDel(NOT_DEL);
                deviceForm.setEnterpriseId(deviceSummaryForm.getEnterpriseId());
                List<DeviceVo> deviceVos = ivDeviceService.list(deviceForm);
                Double ter = 0D;
                Double fm = 0D;
                Double pm = 0D;
                Double wm = 0D;
                for (DeviceVo deviceVo : deviceVos) {
                    switch (deviceVo.getProductType()) {
                        case DICTIONARY_SENSORTYPE_FM:
                            fm++;
                            ter++;
                            break;
                        case DICTIONARY_SENSORTYPE_PM:
                            pm++;
                            ter++;
                            break;
                        case DICTIONARY_SENSORTYPE_WM:
                            wm++;
                            ter++;
                            break;
                        default:
                            break;
                    }
                }
                for (DeviceSummaryForm oldForm : terminalList) {
                    oldForm.setSummaryDate(deviceSummaryForm.getSummaryDate());
                    oldForm.setUserBy(defaultForm.getUserBy());
                    oldForm.setCurr(defaultForm.getCurr());
                    switch (oldForm.getSummaryType()) {
                        case TERMINAL_NUMBER:
                            oldForm.setVal(CalculationUtils.doubleFix(ter, 0));
                            break;
                        case FM_NUMBER:
                            oldForm.setVal(CalculationUtils.doubleFix(fm, 0));
                            break;
                        case PM_NUMBER:
                            oldForm.setVal(CalculationUtils.doubleFix(pm, 0));
                            break;
                        case WM_NUMBER:
                            oldForm.setVal(CalculationUtils.doubleFix(wm, 0));
                            break;
                        default:
                            break;
                    }
                    updateForms.add(oldForm);
                }
                //用户数量
                OwnerForm search = new OwnerForm();
                search.setEnterpriseId(deviceSummaryForm.getEnterpriseId());
                search.setIsDel(NOT_DEL);
                Integer ownerCount = iOwnerService.count(search);
                List<DeviceSummaryVo> temp2 = deviceSummaryVos.stream().filter(t -> OWNER_NUMBER.equals(t.getSummaryCategory())).collect(Collectors.toList());
                List<DeviceSummaryForm> ownerList = BeanUtils.copy(temp2, DeviceSummaryForm.class);
                for (DeviceSummaryForm oldForm : ownerList) {
                    oldForm.setSummaryDate(deviceSummaryForm.getSummaryDate());
                    oldForm.setUserBy(defaultForm.getUserBy());
                    oldForm.setCurr(defaultForm.getCurr());
                    switch (oldForm.getSummaryType()) {
                        case OWNER_NUMBER:
                            oldForm.setVal(CalculationUtils.doubleFix(ownerCount * 1D, 0));
                            break;
                        case ORDINARY_NUMBER:
                            oldForm.setVal(CalculationUtils.doubleFix(ownerCount * ORDINARY_COEFFICIENT, 0));
                            break;
                        case COMMERCIAL_NUMBER:
                            oldForm.setVal(CalculationUtils.doubleFix(ownerCount * COMMERCIAL_COEFFICIENT, 0));
                            break;
                        case INDUSTRIAL_NUMBER:
                            oldForm.setVal(CalculationUtils.doubleFix(
                                    ownerCount - CalculationUtils.doubleFix(ownerCount * ORDINARY_COEFFICIENT, 0)
                                            - CalculationUtils.doubleFix(ownerCount * COMMERCIAL_COEFFICIENT, 0), 0));
                            break;
                        default:
                            break;
                    }
                    updateForms.add(oldForm);
                }
                //管道长度
                deviceForm.setProductCategory(PRODUCT_CATEGORY_PIPE);
                Double pipeLength = ivDeviceService.countLength(deviceForm);
                List<DeviceSummaryVo> temp3 = deviceSummaryVos.stream().filter(t -> PIPE_LENGTH.equals(t.getSummaryCategory())).collect(Collectors.toList());
                DeviceSummaryForm pipeForm = BeanUtils.copy(temp3, DeviceSummaryForm.class).get(0);
                pipeForm.setSummaryDate(deviceSummaryForm.getSummaryDate());
                pipeForm.setUserBy(defaultForm.getUserBy());
                pipeForm.setCurr(defaultForm.getCurr());
                pipeForm.setVal(CalculationUtils.doubleFix(pipeLength, 2));
                updateForms.add(pipeForm);
                //区域用水量
                List<DeviceSummaryVo> temp4 = deviceSummaryVos.stream().filter(t -> AREA_WATER.equals(t.getSummaryCategory())).collect(Collectors.toList());
                List<DeviceSummaryForm> areaWaterList = BeanUtils.copy(temp4, DeviceSummaryForm.class);
                if (!areaWaterList.isEmpty()) {
                    AreaForm searchArea = new AreaForm();
                    searchArea.setEnterpriseId(deviceSummaryForm.getEnterpriseId());
                    searchArea.setIsDel(NOT_DEL);
                    List<AreaVo> areaVos = iAreaService.listDma(searchArea);
                    Map<String, DeviceSummaryForm> areaWaterMap = areaWaterList.stream().collect(Collectors.toMap(DeviceSummaryForm::getAreaCode, s -> s));
                    Map<String, AreaVo> areaMap = areaVos.stream().collect(Collectors.toMap(AreaVo::getAreaCode, s -> s));
                    areaVos.forEach(areaVo -> {
                        if (areaWaterMap.get(areaVo.getAreaCode()) == null) {
                            DeviceSummaryForm areaForm = BeanUtils.copy(defaultForm, DeviceSummaryForm.class);
                            areaForm.setId(UuidUtils.getUuid());
                            areaForm.setSummaryCategory(AREA_WATER);
                            areaForm.setSummaryType(DMA_WATER);
                            areaForm.setName(areaVo.getName());
                            areaForm.setAreaCode(areaVo.getAreaCode());
                            if (areaVo.getDeviceData() != null && areaVo.getDeviceData().getFlwTotalValue() != null) {
                                areaForm.setVal(CalculationUtils.doubleFix(Double.parseDouble(areaVo.getDeviceData().getFlwTotalValue()), 2));
                            }
                            addForms.add(areaForm);
                        } else {
                            DeviceSummaryForm oldForm = areaWaterMap.get(areaVo.getAreaCode());
                            oldForm.setSummaryDate(deviceSummaryForm.getSummaryDate());
                            oldForm.setUserBy(defaultForm.getUserBy());
                            oldForm.setCurr(defaultForm.getCurr());
                            if (areaVo.getDeviceData() != null && areaVo.getDeviceData().getFlwTotalValue() != null) {
                                oldForm.setVal(CalculationUtils.doubleFix(Double.parseDouble(areaVo.getDeviceData().getFlwTotalValue()), 2));
                            }
                            updateForms.add(oldForm);
                        }
                    });
                    List<DeviceSummaryForm> deletes = areaWaterList.stream().filter(t -> areaMap.get(t.getAreaCode()) == null).collect(Collectors.toList());
                    delForms.addAll(deletes);
                }
            }
            if (!addForms.isEmpty()) {
                iDeviceSummaryService.adds(addForms);
            }
            if (!updateForms.isEmpty()) {
                iDeviceSummaryService.edits(updateForms);
            }
            if (!delForms.isEmpty()) {
                iDeviceSummaryService.dels(delForms);
            }
            return "更新数据成功";
        } catch (Exception e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.UNDEFINED_ERROR, e.getMessage(), e);
        }
    }

    @Override
    public List<WorkOrderSummaryVo> workOrderProcessing(WorkOrderForm workOrderForm) {
        String enterpriseId = iAuthCasApi.get().getOperEid();
        WorkOrderBo workOrderBo = BeanUtils.copyProperties(workOrderForm, WorkOrderBo.class);
        workOrderBo.setEnterpriseId(enterpriseId);
        workOrderBo.setIsDel(RootModel.NOT_DEL);
        List<WorkOrderSummaryVo> workOrderSummaryVos = iWorkOrderSummaryService.workOrderProcessing(workOrderBo);
        return workOrderSummaryVos;
    }

    @Override
    public Pagination<AlarmVo> pageAlarm(AlarmForm alarmForm) throws FrameworkRuntimeException {
        LoginCas loginCas = iAuthCasApi.get();
        String operEid = loginCas.getOperEid();
        Integer page = alarmForm.getPage();
        Integer pageSize = alarmForm.getPageSize();
        AlarmBo alarmBo = BeanUtils.copyProperties(alarmForm, AlarmBo.class);
        alarmBo.setEnterpriseId(operEid);
        if (!"all".equals(alarmForm.getStatus())) {
            alarmBo.setStatus(Integer.parseInt(alarmForm.getStatus()));
        }
        Pagination<AlarmVo> pagination = iAlarmSummaryService.pageAlarm(alarmBo);
        return pagination;
    }

    @Override
    public BrustPipeOperationsVo brustPipe(com.dotop.pipe.core.form.DeviceForm deviceForm) throws FrameworkRuntimeException {
        LoginCas loginCas = iAuthCasApi.get();
        String operEid = loginCas.getOperEid();
        DeviceBo deviceBo = BeanUtils.copyProperties(deviceForm, DeviceBo.class);
        deviceBo.setEnterpriseId(operEid);
        BrustPipeOperationsVo brustPipeOperationsVo = iBrustPipeOperationsService.brustPipe(deviceBo);
        return brustPipeOperationsVo;
    }

    @Override
    public List<Brust> brustPipeList(BrustPipeForm brustPipeForm) throws FrameworkRuntimeException {
        String operEid = iAuthCasApi.get().getOperEid();
        BrustPipeBo brustPipeBo = BeanUtils.copy(brustPipeForm, BrustPipeBo.class);
        brustPipeBo.setEnterpriseid(operEid);
        List<BrustPipeVo> brustPipelist = iBrustPipeOperationsService.brustPipeList(brustPipeBo);
        List<com.dotop.pipe.core.vo.device.DeviceVo> deviceList = new ArrayList<>();
        for (BrustPipeVo brustPipeVo : brustPipelist) {
            List<String> deviceIds = brustPipeVo.getDeviceIds();
            if (deviceIds != null && deviceIds.size() > 0) {
                DeviceForm deviceForm = new DeviceForm();
                deviceForm.setDeviceIds(deviceIds);
                deviceForm.setProductType(null);
                deviceForm.setLimit(5000);
                deviceForm.setEnterpriseId(operEid);
                List<com.dotop.pipe.core.vo.device.DeviceVo> list = listDevice(deviceForm);
                for (com.dotop.pipe.core.vo.device.DeviceVo deviceVo : list) {
                    deviceVo.setLastDate(brustPipeVo.getLastDate());
                    deviceList.add(deviceVo);
                }
                brustPipeVo.setDeviceList(list);
            }
        }
        System.out.println(deviceList);
        for (int i = 0; i < deviceList.size(); i++) {
            for (int j = deviceList.size() - 1; j > i; j--) {
                if (deviceList.get(i).getCode().equals(deviceList.get(j).getCode())) {
                    deviceList.remove(j);
                }
            }
        }
        List<Brust> brustList = new ArrayList<>();
        for (com.dotop.pipe.core.vo.device.DeviceVo deviceVo : deviceList) {
            Brust brust = new Brust();
            brust.setDate(deviceVo.getLastDate());
            brust.setName("管道(" + deviceVo.getCode() + ")爆裂!");
            brust.setId(deviceVo.getDeviceId());
            brustList.add(brust);
        }

        return brustList;
    }

    private List<com.dotop.pipe.core.vo.device.DeviceVo> listDevice(DeviceForm deviceForm) throws FrameworkRuntimeException {

        // 封装参数
        DeviceBo deviceBo = BeanUtils.copyProperties(deviceForm, DeviceBo.class);
        deviceBo.setEnterpriseId(deviceForm.getEnterpriseId());
        List<com.dotop.pipe.core.vo.device.DeviceVo> list = iBrustPipeOperationsService.listDevice(deviceBo);
        return list;
    }

    @Override
    public DevicePropertyVo waterMeterData(com.dotop.pipe.core.form.DeviceForm deviceForm) throws FrameworkRuntimeException {
        LoginCas loginCas = iAuthCasApi.get();
        String operEid = loginCas.getOperEid();
        DeviceBo deviceBo = BeanUtils.copyProperties(deviceForm, DeviceBo.class);
        deviceBo.setEnterpriseId(operEid);
        DevicePropertyVo devicePropertyVo = waterMeterService.waterMeterData(deviceBo);
        return devicePropertyVo;
    }

    @Override
    public List<String> brustPipeUnHandler(com.dotop.pipe.core.form.DeviceForm deviceForm) throws FrameworkRuntimeException {
        LoginCas loginCas = iAuthCasApi.get();
        String operEid = loginCas.getOperEid();
        DeviceBo deviceBo = BeanUtils.copyProperties(deviceForm, DeviceBo.class);
        deviceBo.setEnterpriseId(operEid);
        List<String> list = waterMeterService.brustPipeUnHandler(deviceBo);
        return list;
    }
}
