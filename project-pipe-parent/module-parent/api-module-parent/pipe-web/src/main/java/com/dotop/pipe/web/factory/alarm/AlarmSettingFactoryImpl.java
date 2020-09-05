package com.dotop.pipe.web.factory.alarm;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.dotop.pipe.api.service.alarm.IAlarmSettingService;
import com.dotop.pipe.api.service.device.IDeviceService;
import com.dotop.pipe.auth.cas.web.IAuthCasWeb;
import com.dotop.pipe.auth.core.vo.auth.LoginCas;
import com.dotop.pipe.core.bo.alarm.AlarmSettingBo;
import com.dotop.pipe.core.bo.device.DeviceBo;
import com.dotop.pipe.core.form.AlarmSettingForm;
import com.dotop.pipe.core.form.AlarmSettingProperties;
import com.dotop.pipe.core.form.DeviceForm;
import com.dotop.pipe.core.utils.ImportExcelUtils;
import com.dotop.pipe.core.vo.alarm.AlarmSettingVo;
import com.dotop.pipe.core.vo.device.DeviceVo;
import com.dotop.pipe.web.api.factory.alarm.IAlarmSettingFactory;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.BeanUtils;

/**
 *
 * @date 2019年2月15日
 */
@Component
public class AlarmSettingFactoryImpl implements IAlarmSettingFactory {

    private static final Logger logger = LogManager.getLogger(AlarmSettingFactoryImpl.class);

    @Autowired
    private IAuthCasWeb iAuthCasApi;

    @Autowired
    private IAlarmSettingService iAlarmSettingService;

    @Autowired
    private IDeviceService iDeviceService;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
    public AlarmSettingVo add(AlarmSettingForm alarmSettingForm) {
        LoginCas loginCas = iAuthCasApi.get();
        String operEid = loginCas.getOperEid();
        String userBy = loginCas.getUserName();
        AlarmSettingBo alarmSettingBo = BeanUtils.copyProperties(alarmSettingForm, AlarmSettingBo.class);
        alarmSettingBo.setEnterpriseId(operEid);
        alarmSettingBo.setUserBy(userBy);
        alarmSettingBo.setCurr(new Date());
        return iAlarmSettingService.add(alarmSettingBo);
    }

    @Override
    public Pagination<DeviceVo> page(DeviceForm deviceForm) {
        LoginCas loginCas = iAuthCasApi.get();
        String operEid = loginCas.getOperEid();
        // 封装参数
        DeviceBo deviceBo = BeanUtils.copyProperties(deviceForm, DeviceBo.class);
        deviceBo.setEnterpriseId(operEid);
        deviceBo.setProductCategory(deviceForm.getProductCategory()); // 产品类别
        if ("area".equals(deviceForm.getProductCategory())) {
            Pagination<DeviceVo> pagination = iAlarmSettingService.areaAlarmSettingPage(deviceBo);
            return pagination;
        } else {
            Pagination<DeviceVo> pagination = iAlarmSettingService.page(deviceBo);
            return pagination;
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
    public String importDate(MultipartFile file) {
        List<AlarmSettingBo> alarmSettingBoList = new ArrayList<>();
        Map<String, AlarmSettingBo> propertiesMap = new HashMap<>();

        InputStream is;
        try {
            is = file.getInputStream();
            ImportExcelUtils importExcelUtils = new ImportExcelUtils();
            // 1 读取sheet页 和title
            importExcelUtils.readExcelTitle(is);
            // 返回所有的sheet页
            Map<String, XSSFSheet> sheetMap = importExcelUtils.getSheetMap();

            // 默认就一个sheet页
            XSSFSheet sheet = sheetMap.get("Sheet1");
            // 读取总行数
            int rowNum = sheet.getLastRowNum() + 1;

            // 根据列表的名字将数据封装到list中
            for (int curRowNum = 1; curRowNum < rowNum; curRowNum++) {
                XSSFRow curRow = sheet.getRow(curRowNum); // 读取行数据
                if (curRow == null) {
                    continue;
                }
                String deviceCode = importExcelUtils.getCellFormatValue(curRow.getCell(0));
                String tag = importExcelUtils.getCellFormatValue(curRow.getCell(1));
                String maxCompare = importExcelUtils.getCellFormatValue(curRow.getCell(2));
                String maxValue = importExcelUtils.getCellFormatValue(curRow.getCell(3));
                String minCompare = importExcelUtils.getCellFormatValue(curRow.getCell(4));
                String minValue = importExcelUtils.getCellFormatValue(curRow.getCell(5));

                AlarmSettingProperties alarmSettingProperties = getByTag(tag);
                if (alarmSettingProperties == null) {
                    continue;
                }
                alarmSettingProperties.setTag(tag);
                alarmSettingProperties.setMaxValue(maxValue);
                alarmSettingProperties.setMinValue(minValue);
                alarmSettingProperties.setMaxCompare(maxCompare);
                alarmSettingProperties.setMinCompare(minCompare);
                if (propertiesMap.containsKey(deviceCode)) {
                    propertiesMap.get(deviceCode).getProperties().add(alarmSettingProperties);
                } else {
                    AlarmSettingBo alarmSettingBo = new AlarmSettingBo();
                    List<AlarmSettingProperties> list = new ArrayList<AlarmSettingProperties>();
                    list.add(alarmSettingProperties);
                    alarmSettingBo.setProperties(list);
                    propertiesMap.put(deviceCode, alarmSettingBo);
                }
            }
            changeData(propertiesMap);
            JSONObject json = new JSONObject();
            json.put("code", "success");
            json.put("msg", null);
            return json.toString();
        } catch (Exception e) {
            e.printStackTrace();
            JSONObject json = new JSONObject();
            json.put("code", "exception");
            json.put("msg", "导入异常");
            return json.toString();
        }
    }

    public AlarmSettingProperties getByTag(String tag) {
        AlarmSettingProperties alarmSettingProperties = new AlarmSettingProperties();
        switch (tag) {
            case "flw_measure":
                alarmSettingProperties.setField("flwMeasure");
                alarmSettingProperties.setDes("行度值异常");
                break;
            case "flw_rate":
                alarmSettingProperties.setField("flwRate");
                alarmSettingProperties.setDes("流量值异常");
                break;
            case "flw_total_value":
                alarmSettingProperties.setField("flwTotalValue");
                alarmSettingProperties.setDes("总行度值异常");
                break;
            case "pressure_value":
                alarmSettingProperties.setField("pressureValue");
                alarmSettingProperties.setDes("压力值异常");
                break;
            case "quality_chlorine":
                alarmSettingProperties.setField("qualityChlorine");
                alarmSettingProperties.setDes("含氯值异常");
                break;
            case "quality_oxygen":
                alarmSettingProperties.setField("qualityOxygen");
                alarmSettingProperties.setDes("含氧值异常");
                break;
            case "quality_ph":
                alarmSettingProperties.setField("qualityPh");
                alarmSettingProperties.setDes("PH值异常");
                break;
            case "quality_turbid":
                alarmSettingProperties.setField("qualityTurbid");
                alarmSettingProperties.setDes("浑浊度异常");
                break;
            default:
                return null;
        }
        return alarmSettingProperties;
    }

    public void changeData(Map<String, AlarmSettingBo> propertiesMap) {
        Date curr = new Date();
        LoginCas loginCas = iAuthCasApi.get();
        String operEid = loginCas.getOperEid();
        String userBy = loginCas.getUserName();

        List<String> keylist = new ArrayList<>(propertiesMap.keySet());
        Map<String, DeviceVo> deviceIdList = iDeviceService.getDeviceIdByCode(keylist);
        for (String deviceCode : keylist) {
            DeviceVo deviceVo = deviceIdList.get(deviceCode);
            if (deviceVo != null) {
                propertiesMap.get(deviceCode).setDeviceId(deviceVo.getDeviceId());
            } else {
                propertiesMap.remove(deviceCode);
            }
        }
        iAlarmSettingService.importData(propertiesMap, userBy, curr, operEid);
    }

    @Override
    public AlarmSettingVo get(AlarmSettingForm alarmSettingForm) {
        LoginCas loginCas = iAuthCasApi.get();
        String operEid = loginCas.getOperEid();
        String userBy = loginCas.getUserName();
        AlarmSettingBo alarmSettingBo = BeanUtils.copyProperties(alarmSettingForm, AlarmSettingBo.class);
        alarmSettingBo.setEnterpriseId(operEid);
        alarmSettingBo.setUserBy(userBy);
        alarmSettingBo.setCurr(new Date());
        return iAlarmSettingService.get(alarmSettingBo);
    }

}
