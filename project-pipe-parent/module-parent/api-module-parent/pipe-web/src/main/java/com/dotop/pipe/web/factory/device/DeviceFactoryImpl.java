package com.dotop.pipe.web.factory.device;

import com.dotop.pipe.core.vo.device.*;
import com.dotop.pipe.web.api.factory.device.IDeviceFactory;
import com.dotop.pipe.api.client.IWaterPipeClient;
import com.dotop.pipe.api.service.alarm.IAlarmSettingService;
import com.dotop.pipe.api.service.area.IAreaService;
import com.dotop.pipe.api.service.device.IDeviceMappingService;
import com.dotop.pipe.api.service.device.IDeviceService;
import com.dotop.pipe.api.service.historylog.IHistoryLogService;
import com.dotop.pipe.api.service.point.IPointMapService;
import com.dotop.pipe.api.service.point.IPointService;
import com.dotop.pipe.api.service.product.IProductService;
import com.dotop.pipe.api.service.third.IThirdMapService;
import com.dotop.pipe.auth.cas.web.IAuthCasWeb;
import com.dotop.pipe.auth.core.constants.CasConstants;
import com.dotop.pipe.auth.core.vo.auth.LoginCas;
import com.dotop.pipe.core.bo.alarm.AlarmSettingBo;
import com.dotop.pipe.core.bo.device.DeviceBo;
import com.dotop.pipe.core.bo.product.ProductBo;
import com.dotop.pipe.core.constants.CommonConstants;
import com.dotop.pipe.core.constants.PropertyConstants;
import com.dotop.pipe.core.dto.historylog.ChangeDto;
import com.dotop.pipe.core.enums.FieldTypeEnum;
import com.dotop.pipe.core.exception.PipeExceptionConstants;
import com.dotop.pipe.core.form.DeviceDataForm;
import com.dotop.pipe.core.form.DeviceExtForm;
import com.dotop.pipe.core.form.DeviceForm;
import com.dotop.pipe.core.form.PointForm;
import com.dotop.pipe.core.utils.DijkstraForthTUtils;
import com.dotop.pipe.core.utils.HistoryLogUtils;
import com.dotop.pipe.core.utils.PipeLengthUtils;
import com.dotop.pipe.core.vo.alarm.AlarmSettingVo;
import com.dotop.pipe.core.vo.area.AreaModelVo;
import com.dotop.pipe.core.vo.point.PointVo;
import com.dotop.pipe.core.vo.product.ProductVo;
import com.dotop.pipe.core.vo.third.ThirdMapVo;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;

import static com.dotop.pipe.core.constants.CommonConstants.PRODUCT_CATEGORY_CUSTOMIZE_DEVICE;
import static com.dotop.pipe.core.constants.CommonConstants.PRODUCT_CATEGORY_REGION;

@Component
public class DeviceFactoryImpl implements IDeviceFactory {

    private final static Logger logger = LogManager.getLogger(DeviceFactoryImpl.class);

    @Value("${flag.thirdFlag:false}")
    private Boolean thirdFlag = false;

    @Autowired
    private IAuthCasWeb iAuthCasApi;

    @Autowired
    private IDeviceService iDeviceService;

    @Autowired
    private IAreaService iAreaService;

    @Autowired
    private IHistoryLogService iHistoryLogService;

    @Autowired
    private IPointMapService iPointMapService;

    @Autowired
    private IPointService iPointService;

    // @Autowired
    // private IBridgeFactory iBridgeFactory;

    @Autowired
    private IThirdMapService iThirdMapService;

    @Autowired
    private IProductService iProductService;

    @Autowired
    private IDeviceMappingService iDeviceMapService;

    @Autowired
    private IWaterPipeClient iWaterPipeClient;

    @Autowired
    private IAlarmSettingService iAlarmSettingService;

    @Override
    public Pagination<DeviceVo> page(DeviceForm deviceForm) throws FrameworkRuntimeException {
        LoginCas loginCas = iAuthCasApi.get();
        String operEid = loginCas.getOperEid();
        // 封装参数
        DeviceBo deviceBo = BeanUtils.copyProperties(deviceForm, DeviceBo.class);
        deviceBo.setEnterpriseId(operEid);
        deviceBo.setProductCategory(deviceForm.getProductCategory()); // 产品类别
        Pagination<DeviceVo> pagination = iDeviceService.page(deviceBo);
        return pagination;
    }

    @Override
    public List<DeviceVo> list(DeviceForm deviceForm) throws FrameworkRuntimeException {
        LoginCas loginCas = iAuthCasApi.get();
        String operEid = loginCas.getOperEid();
        // 封装参数
        DeviceBo deviceBo = BeanUtils.copyProperties(deviceForm, DeviceBo.class);
        deviceBo.setEnterpriseId(operEid);
        List<DeviceVo> list = iDeviceService.list(deviceBo);
        if (PRODUCT_CATEGORY_CUSTOMIZE_DEVICE.equals(deviceForm.getProductCategory())
                || PRODUCT_CATEGORY_REGION.equals(deviceForm.getProductCategory())) {
            // 自定义设备类型 才会排序
            for (DeviceVo deviceVo : list) {
                deviceVo.getPoints().sort(Comparator.comparing(PointVo::sortBycode));
            }
        }
        return list;
    }

    @Override
    public DeviceVo get(DeviceForm deviceForm) throws FrameworkRuntimeException {
        LoginCas loginCas = iAuthCasApi.get();
        String operEid = loginCas.getOperEid();
        // 封装参数
        DeviceBo deviceBo = new DeviceBo();
        deviceBo.setEnterpriseId(operEid);
        deviceBo.setDeviceId(deviceForm.getDeviceId());
        DeviceVo deviceVo = iDeviceService.get(deviceBo);
        List<String> fields = new ArrayList<String>();
        String deviceId = deviceVo.getDeviceId();
        String code = deviceVo.getCode();
        if (CommonConstants.PRODUCT_CATEGORY_SENSOR.equals(deviceVo.getProductCategory())) {
            // DevicePropertyVo deviceProperty = null;
            // List<DevicePropertyVo> devicePropertys = new ArrayList<DevicePropertyVo>();
            // deviceVo.setDevicePropertys(devicePropertys);
            if (CommonConstants.DICTIONARY_SENSORTYPE_FM.equals(deviceVo.getProduct().getType().getVal())) { // 流量计
                fields.add(PropertyConstants.TYPE_FM_RATE);
                fields.add(PropertyConstants.TYPE_FM_FLWTOTAL);
                fields.add(PropertyConstants.TYPE_FM_FLWMEASURE);
            } else if (CommonConstants.DICTIONARY_SENSORTYPE_PM.equals(deviceVo.getProduct().getType().getVal())) { // 压力计
                fields.add(PropertyConstants.TYPE_PRESSURE_VALUE);
                fields.add(PropertyConstants.TYPE_FM_RATE);
                fields.add(PropertyConstants.TYPE_FM_FLWTOTAL);
                fields.add(PropertyConstants.TYPE_FM_FLWMEASURE);
            } else if (CommonConstants.DICTIONARY_SENSORTYPE_WM.equals(deviceVo.getProduct().getType().getVal())) { // 水质计
                fields.add(PropertyConstants.TYPE_WM_QUALITYTEMONE);
                fields.add(PropertyConstants.TYPE_WM_QUALITYTEMTWO);
                fields.add(PropertyConstants.TYPE_WM_QUALITYTEMTHREE);
                fields.add(PropertyConstants.TYPE_WM_QUALITYTEMFOUR);
                fields.add(PropertyConstants.TYPE_WM_QUALITYCHLORINE);
                fields.add(PropertyConstants.TYPE_WM_QUALITYOXYGEN);
                fields.add(PropertyConstants.TYPE_WM_QUALITYPH);
                fields.add(PropertyConstants.TYPE_WM_QUALITYTURBID);
            }
        } else if (CommonConstants.PRODUCT_CATEGORY_HYDRANT.equals(deviceVo.getProductCategory())) {
            fields.add(PropertyConstants.TYPE_FM_FLWTOTAL);
            fields.add(PropertyConstants.TYPE_PRESSURE_VALUE);
            fields.add(PropertyConstants.TYPE_HYDRANT_BUMP);
            fields.add(PropertyConstants.TYPE_HYDRANT_SLOPE);
            fields.add(PropertyConstants.TYPE_HYDRANT_HIGH_LOW_ALARM);
        }

        if (fields.size() > 0) {
            /*
             * List<DevicePropertyVo> devicePropertys =
             * iDeviceService.getDevicePropertys(deviceId, code, fields, operEid);
             */
            List<DevicePropertyVo> devicePropertys = iDeviceService.getDevicePropertys(deviceId, code, fields,
                    operEid);
            if (devicePropertys != null) {
                // 设置实时数据
                deviceVo.setDevicePropertys(devicePropertys);
                //
                AlarmSettingBo alarmSettingBo = new AlarmSettingBo();
                alarmSettingBo.setEnterpriseId(operEid);
                alarmSettingBo.setDeviceId(deviceVo.getDeviceId());
                List<AlarmSettingVo> alarmSettingVoList = iAlarmSettingService.gets(alarmSettingBo);
                deviceVo.setAlarmSettingVos(alarmSettingVoList);
            }
            // 如果是传感器 并且是流量计 或者压力计 时 查询传感器的属性
        }
        return deviceVo;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
    public List<DeviceVo> addPipes(DeviceForm deviceForm) throws FrameworkRuntimeException {

        Date curr = new Date();
        LoginCas loginCas = iAuthCasApi.get();
        String operEid = loginCas.getOperEid();
        String userBy = loginCas.getUserName();
        String code = deviceForm.getCode();
        String areaId = deviceForm.getAreaId();
        String productId = deviceForm.getProductId();
        String scale = deviceForm.getScale();
        List<DeviceExtForm> pipeLists = deviceForm.getDeviceExts();

        ProductVo productVo = iProductService.get(new ProductBo(CommonConstants.PRODUCT_CATEGORY_PIPE, productId, null));
        AreaModelVo areaVo = iAreaService.getNodeDetails(operEid, areaId);
        List<DeviceVo> list = new ArrayList<DeviceVo>();
        String deviceId = null;
        // 存储点的集合
        Map<String, PointVo> pointMap = new HashMap<>();
        for (DeviceExtForm deviceExtForm : pipeLists) {
            DeviceBo deviceBo = BeanUtils.copyProperties(deviceForm, DeviceBo.class);
            deviceBo.setEnterpriseId(operEid);
            deviceBo.setUserBy(userBy);
            deviceBo.setCurr(curr);
            deviceBo.setCode(deviceExtForm.getCode());
            deviceBo.setName(deviceExtForm.getName());
            deviceBo.setDes(deviceExtForm.getDes());
            deviceBo.setLength(deviceExtForm.getLength());
            deviceBo.setProductCategory(CommonConstants.PRODUCT_CATEGORY_PIPE);
            // 校验 code 是否存在
            Boolean localFlag = iDeviceService.isExist(deviceBo);
            if (localFlag) {
                logger.error(LogMsg.to("ex", PipeExceptionConstants.DEVICE_CODE_EXIST, "msg",
                        PipeExceptionConstants.getMessage(PipeExceptionConstants.DEVICE_CODE_EXIST)));
                throw new FrameworkRuntimeException(PipeExceptionConstants.DEVICE_CODE_EXIST,
                        PipeExceptionConstants.getMessage(PipeExceptionConstants.DEVICE_CODE_EXIST));
            }
            DeviceVo deviceVo = iDeviceService.add(deviceBo);
            // TODO 补充参数，可以先作为校验判断是否存在
            deviceVo.setCode(deviceExtForm.getCode());
            deviceVo.setArea(areaVo);
            deviceVo.setProduct(productVo);
            deviceVo.setName(deviceBo.getName());
            deviceVo.setScale(scale);
            list.add(deviceVo);
            deviceId = deviceVo.getDeviceId();

            // 添加区域
            // 添加坐标与坐标关系
            for (PointForm point : deviceExtForm.getPoints()) {
                String pointId = point.getPointId();
                String pointCode = point.getCode();
                String pointName = null;
                BigDecimal pointLongitude = null;
                BigDecimal pointLatitude = null;
                String breakPipeId = point.getBreakPipeId();
                if (StringUtils.isEmpty(pointId)) {
                    if (pointMap.containsKey(pointCode)) {
                        PointVo tempPoint = pointMap.get(pointCode);
                        // 如果包含
                        pointCode = tempPoint.getCode();
                        pointName = tempPoint.getName();
                        pointLongitude = tempPoint.getLongitude();
                        pointLatitude = tempPoint.getLatitude();
                        pointId = tempPoint.getPointId();
                    } else {
                        // 不存在坐标，需要创建
                        pointCode = point.getCode();
                        pointName = point.getName();
                        pointLongitude = point.getLongitude();
                        pointLatitude = point.getLatitude();
                        if (iPointService.getByCode(operEid, pointCode) != null) {
                            logger.error(LogMsg.to("ex", PipeExceptionConstants.POINT_CODE_EXIST, "code", pointCode));
                            throw new FrameworkRuntimeException(PipeExceptionConstants.POINT_CODE_EXIST);
                        }
                        PointVo addPoint = iPointService.add(operEid, pointCode, pointName, null, pointLongitude,
                                pointLatitude, null, curr, userBy);
                        pointId = addPoint.getPointId();
                        pointMap.put(pointCode, addPoint);
                        // 是否需要分割管道
                        if (StringUtils.isNotEmpty(breakPipeId)) {
                            breakPipe(operEid, curr, userBy, list, breakPipeId, pointId, pointCode, pointLongitude,
                                    pointLatitude);
                        }
                    }
                } else {
                    // 存在坐标，不需要创建
                    PointVo pe = iPointService.get(operEid, pointId);
                    pointCode = pe.getCode();
                    pointName = pe.getName();
                    pointLongitude = pe.getLongitude();
                    pointLatitude = pe.getLatitude();
                }
                // 添加坐标关系
                iPointMapService.add(operEid, pointId, deviceId, curr, userBy);
                List<PointVo> points = deviceVo.getPoints();
                if (points == null) {
                    points = new ArrayList<PointVo>();
                    deviceVo.setPoints(points);
                }
                points.add(new PointVo(pointId, pointCode, pointLongitude, pointLatitude));
            }
        }
        return list;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
    public List<DeviceVo> adds(DeviceForm deviceForm) throws FrameworkRuntimeException {
        Date curr = new Date();
        LoginCas loginCas = iAuthCasApi.get();
        String operEid = loginCas.getOperEid();
        String userBy = loginCas.getUserName();
        String code = deviceForm.getCode();
        String areaId = deviceForm.getAreaId();
        String scale = deviceForm.getScale();
        String productId = deviceForm.getProductId();
        String productCategory = deviceForm.getProductCategory();
        String productType = deviceForm.getProductType();
        String protocol = deviceForm.getProtocol();
        // TODO 新增管道长度前端传递
        // String[] pointIds = deviceForm.getPointIds();

        // 封装参数
        DeviceBo deviceBo = BeanUtils.copyProperties(deviceForm, DeviceBo.class);
        deviceBo.setEnterpriseId(operEid);
        deviceBo.setUserBy(userBy);
        deviceBo.setCurr(curr);
        // 校验 code 是否存在
        Boolean localFlag = iDeviceService.isExist(deviceBo);
        if (localFlag) {
            logger.error(LogMsg.to("ex", PipeExceptionConstants.DEVICE_CODE_EXIST, "msg",
                    PipeExceptionConstants.getMessage(PipeExceptionConstants.DEVICE_CODE_EXIST)));
            throw new FrameworkRuntimeException(PipeExceptionConstants.DEVICE_CODE_EXIST,
                    PipeExceptionConstants.getMessage(PipeExceptionConstants.DEVICE_CODE_EXIST));
        }
        // TODO 整合第三方传感器 改动
        /*
         * // 添加 DeviceVo deviceVo = iDeviceService.add(deviceBo); String deviceId =
         * deviceVo.getDeviceId(); // // 添加区域 // iAreaMapService.add(operEid, areaId,
         * deviceId, curr, userBy); // 添加坐标 for (String pointId : pointIds) {
         * iPointMapService.add(operEid, pointId, deviceId, curr, userBy); }
         */

        // 设施是否地图设备被使用
        if (!CommonConstants.isWaterProduct(productCategory)) {
            DeviceBo otherDeviceBo = new DeviceBo();
            otherDeviceBo.setProductId(productId);
            otherDeviceBo.setEnterpriseId(operEid);
            Boolean otherFlag = iDeviceService.isExist(otherDeviceBo);
            if (otherFlag) {
                logger.error(LogMsg.to("ex", PipeExceptionConstants.DEVICE_PRODUCT_EXIST, "msg",
                        PipeExceptionConstants.getMessage(PipeExceptionConstants.DEVICE_PRODUCT_EXIST)));
                throw new FrameworkRuntimeException(PipeExceptionConstants.DEVICE_PRODUCT_EXIST,
                        PipeExceptionConstants.getMessage(PipeExceptionConstants.DEVICE_PRODUCT_EXIST));
            }
        }

        List<DeviceVo> list = new ArrayList<>();
        DeviceVo deviceVo = null;
        String deviceId = null;

        /**
         * 1 有很多字段没传过来 需要根据产品id 查询产品内容
         */
        ProductVo productVo = iProductService.get(new ProductBo(productCategory, productId, null, operEid));

        // if (thirdFlag && "sensor".equals(productVo.getCategory().getVal())
        // &&
        // CommonConstants.DICTIONARY_FACTORYCODE_KBL.equals(productVo.getFactory().getCode()))
        // { // 判断是传感器
        // // 封装信息 第三方使用
        // Map<String, Object> params = new HashMap<String, Object>();
        // params.put("rangecode", "0001"); // 位置
        // params.put("areacode", "0001001");
        // params.put("longitude", "114.07900429980464");
        // params.put("latitude", "22.553374000000000");
        // params.put("sensorCode", deviceForm.getCode());
        // params.put("sensorDes", deviceForm.getDes());
        // params.put("sensorType", productVo.getType().getVal());
        // // TODO 确认 productVo.getFactory().getCode()
        // // 校验第三方是否存在
        // String thirdFlag =
        // iBridgeFactory.isExistSensor(productVo.getFactory().getCode(), params);
        // JSONObject thirdFlagObject = JSONUtils.parseObject(thirdFlag); //
        // key{thirdId,sensorCode,sensorType}
        // String thirdIdFlag = thirdFlagObject.getString("thirdId");
        // // 本地thirdMap 校验
        // Boolean thirdMapFlag = iThirdMapService.isExistByThirdCode(operEid,
        // deviceForm.getCode());
        //
        // // 1 都不存在 创建全新的传感器 第三方创建 本地创建 thirdMap 创建
        // if (!localFlag && StringUtils.isBlank(thirdIdFlag)) {
        // if (thirdMapFlag) {
        // // 情况分析: 第三方和本地都删掉了code的 传感器 但是thirdMap 没有删除 （本地不存在可用的 thirdMap 即使存在也没用）
        // // 删掉
        // iThirdMapService.delByCode(operEid, deviceForm.getCode(), curr, userBy);
        // }
        // // 本地创建传感器
        // deviceVo = iDeviceService.add(deviceBo);
        //
        // // 第三方创建传感器
        // String thirdResp =
        // iBridgeFactory.createSensor(productVo.getFactory().getCode(), params);
        // JSONObject parseObject = JSONUtils.parseObject(thirdResp);
        // String thirdId = parseObject.getString("thirdId");
        //
        // if (StringUtils.isBlank(thirdId)) {
        // logger.error(
        // LogMsg.to("ex", PipeExceptionConstants.SENSOR_THIRD_CREATE_ERROR, "msg",
        // "第三方创建传感器失败"));
        // throw new
        // FrameworkRuntimeException(PipeExceptionConstants.SENSOR_THIRD_CREATE_ERROR);
        // } else {
        // // thirdMap 创建映射关系
        // iThirdMapService.add(operEid, deviceVo.getDeviceId(), deviceForm.getCode(),
        // thirdId,
        // deviceForm.getCode(), curr, userBy, productVo.getType().getVal());
        // }
        // }
        //
        // // 2 本地不存在 第三方存在
        // if (!localFlag && StringUtils.isNotBlank(thirdIdFlag)) {
        // if (thirdMapFlag) {
        // // 情况分析: 第三方改了传感器的code 但本地没有改过 即 本地有一个传感器id 和 第三方 id 一一对应 但是code 不一致
        // logger.error(LogMsg.to("ex", PipeExceptionConstants.SENSOR_CODE_EXIST, "msg",
        // "code第三方存在且本地在使用"));
        // throw new
        // FrameworkRuntimeException(PipeExceptionConstants.SENSOR_CODE_EXIST);
        // } else {
        // // 情况分析: thirdMap 不存在 创建新的一一对应关系
        // // 第三方存在 则只在 本地创建传感器 并关联第三方已经存在的
        // // sensorId = iSensorService.add(sensorForm, operEid, userBy, curr);
        // deviceVo = iDeviceService.add(deviceBo);
        // iThirdMapService.add(operEid, deviceVo.getDeviceId(), deviceForm.getCode(),
        // thirdIdFlag,
        // deviceForm.getCode(), curr, userBy, productVo.getType().getVal());
        // }
        // }
        //
        // } else {
        deviceVo = iDeviceService.add(deviceBo);
        deviceId = deviceVo.getDeviceId();
        // 20200309 新增通信协议
        if (CommonConstants.PRODUCT_CATEGORY_SENSOR.equals(productCategory) && !"standard".equals(protocol)) {
            iThirdMapService.add(operEid, deviceId, code, deviceId, code,
                    curr, userBy, productType, protocol);
        }


        // }
        // TODO 补充参数，可以先作为校验判断是否存在
        deviceVo.setCode(code);
        AreaModelVo areaVo = iAreaService.getNodeDetails(operEid, areaId);
        deviceVo.setArea(areaVo);
        deviceVo.setProduct(productVo);
        deviceVo.setName(deviceBo.getName());
        deviceVo.setScale(scale);
        list.add(deviceVo);

        // 添加区域
        // 添加坐标与坐标关系
        for (PointForm point : deviceForm.getPoints()) {
            String pointId = point.getPointId();
            String pointCode = null;
            String pointName = null;
            BigDecimal pointLongitude = null;
            BigDecimal pointLatitude = null;
            String breakPipeId = point.getBreakPipeId();
            if (StringUtils.isEmpty(pointId)) {
                // 不存在坐标，需要创建
                pointCode = point.getCode();
                pointName = point.getName();
                pointLongitude = point.getLongitude();
                pointLatitude = point.getLatitude();
                // if (iPointService.getByCode(operEid, pointCode) != null) {
                // logger.error(LogMsg.to("ex", PipeExceptionConstants.POINT_CODE_EXIST, "code",
                // pointCode));
                // throw new FrameworkRuntimeException(PipeExceptionConstants.POINT_CODE_EXIST);
                // }
                PointVo addPoint = iPointService.add(operEid, pointCode, pointName, null, pointLongitude, pointLatitude,
                        null, curr, userBy);
                pointId = addPoint.getPointId();
                // 是否需要分割管道
                if (StringUtils.isNotEmpty(breakPipeId)) {
                    breakPipe(operEid, curr, userBy, list, breakPipeId, pointId, pointCode, pointLongitude,
                            pointLatitude);
                }
            } else {
                // 存在坐标，不需要创建
                PointVo pe = iPointService.get(operEid, pointId);
                pointCode = pe.getCode();
                pointName = pe.getName();
                pointLongitude = pe.getLongitude();
                pointLatitude = pe.getLatitude();
            }
            // 添加坐标关系
            iPointMapService.add(operEid, pointId, deviceId, curr, userBy);
            List<PointVo> points = deviceVo.getPoints();
            if (points == null) {
                points = new ArrayList<PointVo>();
                deviceVo.setPoints(points);
            }
            points.add(new PointVo(pointId, pointCode, pointLongitude, pointLatitude));
        }
        // 20200309 新增通信协议
        if (CommonConstants.PRODUCT_CATEGORY_SENSOR.equals(productCategory) && !"standard".equals(protocol)) {
            iWaterPipeClient.deviceSubscribeBind(operEid, code);
        }

        return list;
    }

    private void breakPipe(String enterpriseId, Date curr, String userBy, List<DeviceVo> list, String breakPipeId,
                           String newPointId, String newPointCode, BigDecimal newPointLongitude, BigDecimal newPointLatitude)
            throws FrameworkRuntimeException {
        DeviceBo deviceBo = new DeviceBo();
        deviceBo.setDeviceId(breakPipeId);
        deviceBo.setEnterpriseId(enterpriseId);
        // 获取原有管道
        DeviceVo oriPipe = iDeviceService.get(deviceBo);
        // 获取原有管道坐标
        List<PointVo> oriPipePoints = oriPipe.getPoints();
        String pointId0 = oriPipePoints.get(0).getPointId();
        String pointCode0 = oriPipePoints.get(0).getCode();
        BigDecimal pointLongitude0 = oriPipePoints.get(0).getLongitude();
        BigDecimal pointLatitude0 = oriPipePoints.get(0).getLatitude();
        String pointId1 = oriPipePoints.get(1).getPointId();
        String pointCode1 = oriPipePoints.get(1).getCode();
        BigDecimal pointLongitude1 = oriPipePoints.get(1).getLongitude();
        BigDecimal pointLatitude1 = oriPipePoints.get(1).getLatitude();
        // 删除原管道某一坐标
        iPointMapService.del(enterpriseId, oriPipe.getDeviceId(), pointId0, curr, userBy);
        // 用新坐标补充原有管道坐标
        // String newPointId = deviceVo.getPoints().get(0).getPointId();
        // String newPointCode = deviceVo.getPoints().get(0).getCode();
        // BigDecimal newPointLongitude = deviceVo.getPoints().get(0).getLongitude();
        // BigDecimal newPointLatitude = deviceVo.getPoints().get(0).getLatitude();
        iPointMapService.add(enterpriseId, newPointId, oriPipe.getDeviceId(), curr, userBy);

        // 修改旧管为创建新管道
        deviceBo = new DeviceBo();
        deviceBo.setProductId(oriPipe.getProduct().getProductId());
        deviceBo.setAreaId(oriPipe.getArea().getAreaId());
        deviceBo.setEnterpriseId(oriPipe.getEnterpriseId());
        deviceBo.setUserBy(userBy);
        deviceBo.setCurr(curr);
        deviceBo.setProductCategory(oriPipe.getProductCategory());
        deviceBo.setProductType(oriPipe.getProductType());
        deviceBo.setScale(oriPipe.getScale());
        // 修改旧管长度
        String oriPipeLength = PipeLengthUtils.getPipeLength(newPointLongitude.toString(), newPointLatitude.toString(),
                pointLongitude1.toString(), pointLatitude1.toString());
        deviceBo.setLength(oriPipeLength);
        Boolean localFlag = true;
        StringBuffer strBuf = new StringBuffer("_");
        String orgCode = ""; // 记录原管道修改后的code值
        // 修改原管道code
        do {
            deviceBo.setCode(oriPipe.getCode() + strBuf.toString() + "1");// TODO 系统分配常量
            deviceBo.setDeviceId(oriPipe.getDeviceId());
            localFlag = iDeviceService.isExist(deviceBo);
            if (localFlag) {
                strBuf = strBuf.append("_");
            } else {
                // 修改原管道信息code值为code_1 如果存在 改成code__1
                this.iDeviceService.edit(deviceBo);
                orgCode = deviceBo.getCode();
            }
        } while (localFlag); // 验证码存在是一直循环校验 直到 码不存在

        // 创建新管道修改新增管道code
        DeviceVo newPipe = null;
        oriPipeLength = PipeLengthUtils.getPipeLength(newPointLongitude.toString(), newPointLatitude.toString(),
                pointLongitude0.toString(), pointLatitude0.toString());
        deviceBo.setLength(oriPipeLength);
        do {
            deviceBo.setCode(oriPipe.getCode() + strBuf.toString() + "2");// TODO 系统分配常量
            deviceBo.setName(oriPipe.getName() + strBuf.toString() + "2");
            deviceBo.setDes(oriPipe.getName() + strBuf.toString() + "2");
            // 判断长度是否大于36 并且系统不存在
            localFlag = iDeviceService.isExist(deviceBo);
            if (localFlag) {
                strBuf = strBuf.append("_");
            } else {
                newPipe = iDeviceService.add(deviceBo);
                newPipe.setCode(deviceBo.getCode());
                newPipe.setName(deviceBo.getName());
                newPipe.setDes(deviceBo.getDes());
            }
        } while (localFlag); // 验证码存在是一直循环校验 直到 码不存在

        // DeviceVo newPipe = iDeviceService.add(deviceBo);
        // 新管道用新坐标和某一坐标
        iPointMapService.add(enterpriseId, pointId0, newPipe.getDeviceId(), curr, userBy);
        iPointMapService.add(enterpriseId, newPointId, newPipe.getDeviceId(), curr, userBy);
        // 放置坐标
        oriPipePoints = new ArrayList<PointVo>();
        oriPipePoints.add(new PointVo(pointId1, pointCode1, pointLongitude1, pointLatitude1));
        oriPipePoints.add(new PointVo(newPointId, newPointCode, newPointLongitude, newPointLatitude));
        oriPipe.setPoints(oriPipePoints);
        oriPipe.setProduct(oriPipe.getProduct());
        oriPipe.setArea(oriPipe.getArea());
        oriPipe.setCode(orgCode);// 更新返回值
        list.add(oriPipe);
        List<PointVo> newPipePoints = new ArrayList<PointVo>();
        newPipePoints.add(new PointVo(pointId0, pointCode0, pointLongitude0, pointLatitude0));
        newPipePoints.add(new PointVo(newPointId, newPointCode, newPointLongitude, newPointLatitude));
        newPipe.setPoints(newPipePoints);
        newPipe.setProduct(oriPipe.getProduct());
        newPipe.setArea(oriPipe.getArea());
        list.add(newPipe);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
    public List<DeviceVo> editReturnList(DeviceForm deviceForm) throws FrameworkRuntimeException {
        Date curr = new Date();
        LoginCas loginCas = iAuthCasApi.get();
        String operEid = loginCas.getOperEid();
        String userBy = loginCas.getUserName();
        String deviceId = deviceForm.getDeviceId();
        String code = deviceForm.getCode();
        String protocol = deviceForm.getProtocol();
        // String areaId = deviceForm.getAreaId();
        // 封装参数
        DeviceBo deviceBo = BeanUtils.copyProperties(deviceForm, DeviceBo.class);
        deviceBo.setEnterpriseId(operEid);
        deviceBo.setUserBy(userBy);
        deviceBo.setCurr(curr);

        /**
         * 记录 设备 code 更改的日志 逻辑 1 校验 code 和 原 code 是否一致 一致则没有修改 不必记录 2 校验 code 和 原 code
         * 不一致 则校验code 是否存在 存在则报错 code已经存在 3 校验 code 和 原 code 不一致 则校验code 不存在 记录设备变更历史
         *
         */

        // 查询原设备
        deviceBo.setCode(null);// 新code不参与条件
        DeviceVo oldDeviceVo = iDeviceService.get(deviceBo);
        // if (!oldDeviceVo.getCode().equals(deviceForm.getCode())) { // 暂时只有修改 code 时
        // 记录log

        deviceBo.setCode(deviceForm.getCode());// 新code参与更新
        Boolean localFlag = iDeviceService.isExist(deviceBo);
        ProductVo productVo = oldDeviceVo.getProduct();
        if (localFlag && !oldDeviceVo.getCode().equals(deviceForm.getCode())) { // code有更改 并且新code 存在
            logger.error(LogMsg.to("ex", PipeExceptionConstants.DEVICE_CODE_EXIST, "msg",
                    PipeExceptionConstants.getMessage(PipeExceptionConstants.DEVICE_CODE_EXIST)));
            throw new FrameworkRuntimeException(PipeExceptionConstants.DEVICE_CODE_EXIST,
                    PipeExceptionConstants.getMessage(PipeExceptionConstants.DEVICE_CODE_EXIST));
        } else {
            // 编辑
            iDeviceService.edit(deviceBo);
            // 20200309 更新通信协议
            String productCategory = oldDeviceVo.getProductCategory();
            String productType = oldDeviceVo.getProductType();
            if (CommonConstants.PRODUCT_CATEGORY_SENSOR.equals(productCategory)) {
                // 新修改为非标准协议
                if (!"standard".equals(oldDeviceVo.getProtocol()) && !"standard".equals(protocol) && protocol.equals(oldDeviceVo.getProtocol()) && code.equals(oldDeviceVo.getCode())) {
                    // 非标准，协议相同，code相同，订阅不处理
                } else if (!"standard".equals(oldDeviceVo.getProtocol()) && !"standard".equals(protocol) && protocol.equals(oldDeviceVo.getProtocol()) && !code.equals(oldDeviceVo.getCode())) {
                    // 非标准，协议相同，code不相同，删除订阅，新增订阅
                    ThirdMapVo thirdMapVo = iThirdMapService.get(operEid, deviceId);
                    iWaterPipeClient.deviceSubscribeDel(operEid, oldDeviceVo.getCode());
                    if (thirdMapVo == null) {
                        iThirdMapService.add(operEid, deviceId, code, deviceId, code,
                                curr, userBy, productType, protocol);
                        iWaterPipeClient.deviceSubscribeBind(operEid, code);
                    } else {
                        iThirdMapService.edit(operEid, thirdMapVo.getMapId(), code, code, curr, userBy, productType, protocol);
                        iWaterPipeClient.deviceSubscribeBind(operEid, code);
                    }
                } else if (!"standard".equals(oldDeviceVo.getProtocol()) && !"standard".equals(protocol) && !protocol.equals(oldDeviceVo.getProtocol()) && code.equals(oldDeviceVo.getCode())) {
                    // 非标准，协议不相同，code相同，订阅不处理
                    ThirdMapVo thirdMapVo = iThirdMapService.get(operEid, deviceId);
                    if (thirdMapVo == null) {
                        iThirdMapService.add(operEid, deviceId, code, deviceId, code,
                                curr, userBy, productType, protocol);
                    } else {
                        iThirdMapService.edit(operEid, thirdMapVo.getMapId(), code, code, curr, userBy, productType, protocol);
                    }
                } else if ("standard".equals(oldDeviceVo.getProtocol()) && !"standard".equals(protocol) && code.equals(oldDeviceVo.getCode())) {
                    // 旧为标准，新为非标准，code相同
                    ThirdMapVo thirdMapVo = iThirdMapService.get(operEid, deviceId);
                    if (thirdMapVo == null) {
                        iThirdMapService.add(operEid, deviceId, code, deviceId, code,
                                curr, userBy, productType, protocol);
                        iWaterPipeClient.deviceSubscribeBind(operEid, code);
                    } else {
                        iThirdMapService.edit(operEid, thirdMapVo.getMapId(), code, code, curr, userBy, productType, protocol);
                        iWaterPipeClient.deviceSubscribeBind(operEid, code);
                    }
                } else if (!"standard".equals(oldDeviceVo.getProtocol()) && "standard".equals(protocol) && code.equals(oldDeviceVo.getCode())) {
                    // 旧为非标准，新为标准，code相同
                    ThirdMapVo thirdMapVo = iThirdMapService.get(operEid, deviceId);
                    iWaterPipeClient.deviceSubscribeDel(operEid, oldDeviceVo.getCode());
                    if (thirdMapVo == null) {
                        iThirdMapService.del(operEid, deviceId, curr, userBy);
                    }

                } else if ("standard".equals(oldDeviceVo.getProtocol()) && "standard".equals(protocol)) {
                    // 旧为标准，新为标准，不处理
                }
            }

            if (PRODUCT_CATEGORY_CUSTOMIZE_DEVICE.equals(oldDeviceVo.getProductCategory())
                    || PRODUCT_CATEGORY_REGION.equals(oldDeviceVo.getProductCategory())) { // 自定义设备
                // if (productVo.getCategory().getVal().equals("customize_device")
                // || productVo.getCategory().getVal().equals("region")) { // 自定义设备
                if (deviceForm.getPoints() != null && !deviceForm.getPoints().isEmpty()) {
                    // 由于区域描边的点的集合比较多 不操作更新 先删除 再添加新的数据
                    iPointMapService.delTables(operEid, deviceForm.getDeviceId(), curr, userBy);
                    // 添加坐标关系
                    iPointMapService.addList(operEid, deviceForm.getDeviceId(), curr, userBy, deviceForm.getPoints());
                    iPointService.addList(operEid, curr, userBy, deviceForm.getPoints());
                }
            }
            DeviceVo newDeviceVo = iDeviceService.get(deviceBo);

            // 查询修改后的设备信息 用两个Vo做比较
            List<ChangeDto> list = HistoryLogUtils.compareForDevice(newDeviceVo, oldDeviceVo, new HashSet<String>() {
                private static final long serialVersionUID = 1L;

                {
                    add("deviceId");
                    add("enterpriseId");
                    add("productType");
                    add("installDate");
                    add("laying");
                    add("points");
                    add("pointIds");
                    add("areaId");
                    add("devicePropertys");
                }
            });
            if (list != null && list.size() > 0) {
                iHistoryLogService.add(list, operEid, userBy, deviceForm.getDeviceId());
            }
        }

        // if (thirdFlag &&
        // CommonConstants.PRODUCT_CATEGORY_SENSOR.equals(productVo.getCategory().getVal())
        // &&
        // CommonConstants.DICTIONARY_FACTORYCODE_KBL.equals(productVo.getFactory().getCode()))
        // { // TODO
        // // 判断是sensor
        // // 和kbl
        // // 修改传感器 1 oldCode 2 newCode
        // // oldCode 和newCode 是否相同 相同则不必操作第三方数据
        // if (oldDeviceVo.getCode().equals(deviceForm.getCode())) { // 没有修改code
        // return null;
        // }
        //
        // // 获取thirdMap 中的thirdId
        // ThirdMapVo thirdMapVo = iThirdMapService.get(operEid,
        // deviceForm.getDeviceId());
        //
        // // 封装信息 第三方使用
        // Map<String, Object> params = new HashMap<String, Object>();
        // params.put("sensorType", productVo.getType().getVal());
        // params.put("sensorCode", deviceForm.getCode());
        //
        // // 本地校验 newCode是否存在
        // Boolean thirdMapFlag = iThirdMapService.isExistByThirdCode(operEid,
        // deviceForm.getCode());
        // if (thirdMapFlag) {
        // // newCode 在thirdMap中有用
        // logger.error(LogMsg.to("ex", PipeExceptionConstants.SENSOR_CODE_EXIST, "msg",
        // "本地thirdMap存在"));
        // throw new
        // FrameworkRuntimeException(PipeExceptionConstants.SENSOR_THIRD_CREATE_ERROR);
        // }
        //
        // // newCode 校验第三方是否存在
        // String newCodeThirdFlag =
        // iBridgeFactory.isExistSensor(productVo.getFactory().getCode(), params);
        // JSONObject newCodeObject = JSONUtils.parseObject(newCodeThirdFlag); //
        // key{thirdId,sensorCode,sensorType}
        // String newCodeThirdIdFlag = newCodeObject.getString("thirdId");
        //
        // params.put("thirdId", thirdMapVo.getThirdId()); // 没有id时 用code 校验
        // // params.put("sensorCode", sensorForm.getOldCode()); // oldCode
        // // oldCode 校验第三方是否存在
        // String oldCodeThirdFlag =
        // iBridgeFactory.isExistSensor(productVo.getFactory().getCode(), params);
        // JSONObject oldCodeObject = JSONUtils.parseObject(oldCodeThirdFlag); //
        // key{thirdId,sensorCode,sensorType}
        // String oldCodeThirdIdFlag = oldCodeObject.getString("thirdId");
        //
        // if (StringUtils.isBlank(oldCodeThirdIdFlag)) {
        // // 1 oldCode 第三方不存在 创建新的传感器
        // // 2 newCode 第三方是否存在
        // if (StringUtils.isBlank(newCodeThirdIdFlag)) {
        // // oldCode newCode 第三方不存在 创建传感器 newCode
        // String thirdResp =
        // iBridgeFactory.createSensor(productVo.getFactory().getCode(), params);
        // JSONObject parseObject = JSONUtils.parseObject(thirdResp);
        // String thirdId = parseObject.getString("thirdId");
        // if (StringUtils.isBlank(thirdId)) {
        // logger.error(
        // LogMsg.to("ex", PipeExceptionConstants.SENSOR_THIRD_CREATE_ERROR, "msg",
        // "第三方创建传感器失败"));
        // throw new
        // FrameworkRuntimeException(PipeExceptionConstants.SENSOR_THIRD_CREATE_ERROR);
        // }
        // // newCode 本地不存在
        // // 删除oldCode 添加newCode关联
        // iThirdMapService.del(operEid, deviceForm.getDeviceId(), curr, userBy);
        // iThirdMapService.add(operEid, deviceForm.getDeviceId(), deviceForm.getCode(),
        // thirdId,
        // deviceForm.getCode(), curr, userBy, productVo.getType().getVal());
        // } else {
        // // newCode 第三方存在
        // logger.error(LogMsg.to("ex", PipeExceptionConstants.SENSOR_UPDATE_ERROR,
        // "msg", "新的code 第三方存在"));
        // throw new
        // FrameworkRuntimeException(PipeExceptionConstants.SENSOR_UPDATE_ERROR);
        // }
        // } else {
        // // oldCode第三方存在
        // // 校验新的newCode 第三方是否存在
        // if (StringUtils.isBlank(newCodeThirdIdFlag)) {
        // // oldCode 存在 newCode 不存在 （正常情况）
        // // 本地thirdMap 不存在 (正常情况)
        // iBridgeFactory.editSensor(productVo.getFactory().getCode(), params);
        // iThirdMapService.del(operEid, deviceForm.getDeviceId(), curr, userBy);
        // iThirdMapService.add(operEid, deviceForm.getDeviceId(), deviceForm.getCode(),
        // thirdMapVo.getThirdId(), deviceForm.getCode(), curr, userBy,
        // productVo.getType().getVal());
        // } else {
        // // oldCode第三方存在 newCode 第三方存在
        // logger.error(LogMsg.to("ex", PipeExceptionConstants.SENSOR_UPDATE_ERROR,
        // "msg", "新的code 第三方存在"));
        // throw new
        // FrameworkRuntimeException(PipeExceptionConstants.SENSOR_UPDATE_ERROR);
        // }
        // }
        // }
        return null;
    }

    @Override
    public List<DeviceVo> editCoordinate(DeviceForm deviceForm) throws FrameworkRuntimeException {

        Date curr = new Date();
        LoginCas loginCas = iAuthCasApi.get();
        String operEid = loginCas.getOperEid();
        String userBy = loginCas.getUserName();
        // String areaId = deviceForm.getAreaId();
        // String deviceId = deviceForm.getDeviceId();
        if (deviceForm.getLength() != null) { // 长度不为空时 修改长度
            // 封装参数
            DeviceBo deviceBo = BeanUtils.copyProperties(deviceForm, DeviceBo.class);
            deviceBo.setEnterpriseId(operEid);
            deviceBo.setUserBy(userBy);
            deviceBo.setCurr(curr);
            // 编辑
            iDeviceService.edit(deviceBo);
        }

        DeviceVo deviceVo = new DeviceVo();
        List<DeviceVo> list = new ArrayList<>();
        list.add(deviceVo);
        List<PointVo> pointsList = new ArrayList<>();
        // 修改坐标逻辑处理
        /**
         * 1 判断坐标是否存在 如果不存在 新建坐标 1） 修改point_map 中的关联关系 2 如果存在 获取存在的id 1）修改point_map
         * 中的关联关系
         */
        Map<String, String> orgPoint = new HashMap<String, String>(); // 原有的坐标
        Map<String, PointForm> newPointForm = new HashMap<>(); // 新坐标
        Map<String, String> noChange = new HashMap<>(); // 不需要修改的坐标
        // 把新的坐标信息 放入集合中
        for (PointForm pointForm : deviceForm.getPoints()) {
            if (StringUtils.isNotBlank(pointForm.getPointId())) {
                newPointForm.put(pointForm.getPointId(), pointForm);
            }
        }
        // 把原坐标放入集合中
        for (String pointId : deviceForm.getPointIds()) {
            orgPoint.put(pointId, pointId);
        }

        for (PointForm pointForm : deviceForm.getPoints()) {
            for (String pointId : deviceForm.getPointIds()) {
                // 如果新增的坐标 在 原有坐标中出现 则从map 集合中移除
                if (pointId.equals(pointForm.getPointId())) {
                    newPointForm.remove(pointForm.getPointId());
                    orgPoint.remove(pointId);
                    pointsList.add(new PointVo(pointForm.getPointId(), pointForm.getCode(), pointForm.getLongitude(),
                            pointForm.getLatitude())); // 用来返回给前端
                }
            }
        }

        // 执行删除原坐标
        for (String orgPointId : orgPoint.keySet()) {
            System.out.println("删除的坐标id = " + orgPointId);
            this.iPointMapService.del(operEid, deviceForm.getDeviceId(), orgPointId, curr, userBy);
        }

        // 新增的坐标
        for (PointForm point : deviceForm.getPoints()) {

            if (StringUtils.isBlank(point.getPointId())) {
                // point为空时肯定是新建的

                if (StringUtils.isBlank(point.getPointId())
                        && iPointService.getByCode(operEid, point.getCode()) != null) {

                    logger.error(LogMsg.to("ex", PipeExceptionConstants.POINT_CODE_EXIST, "code", point.getCode()));
                    throw new FrameworkRuntimeException(PipeExceptionConstants.POINT_CODE_EXIST);
                }

                // 新建坐标时判断是否是分割管道
                String breakPipeId = point.getBreakPipeId();
                // 新建坐标
                PointVo newPointVo = this.iPointService.add(operEid, point.getCode(), null, null, point.getLongitude(),
                        point.getLatitude(), null, curr, userBy);
                this.iPointMapService.add(operEid, newPointVo.getPointId(), deviceForm.getDeviceId(), curr, userBy);
                pointsList.add(newPointVo);

                if (StringUtils.isNotBlank(breakPipeId)) { // 分割管道id 不等于空
                    // 3 绑定关联关系
                    breakPipe(operEid, curr, userBy, list, breakPipeId, newPointVo.getPointId(), point.getCode(),
                            point.getLongitude(), point.getLatitude());
                }
            } else if (StringUtils.isNotBlank(point.getPointId()) && newPointForm.containsKey(point.getPointId())) {
                // 或者 (不为空且在map中存在的 map中存在表示不和设备原有的id重复)
                // 添加关联关系
                this.iPointMapService.add(operEid, point.getPointId(), deviceForm.getDeviceId(), curr, userBy);
                pointsList.add(
                        new PointVo(point.getPointId(), point.getCode(), point.getLongitude(), point.getLatitude())); // 用来返回给前端
            }
        }
        list.get(0).setPoints(pointsList);
        return list;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
    public String del(DeviceForm deviceForm) throws FrameworkRuntimeException {
        Date curr = new Date();
        LoginCas loginCas = iAuthCasApi.get();
        String operEid = loginCas.getOperEid();
        String userBy = loginCas.getUserName();
        String deviceId = deviceForm.getDeviceId();
        // 封装参数
        DeviceBo deviceBo = new DeviceBo();
        deviceBo.setEnterpriseId(operEid);
        deviceBo.setDeviceId(deviceForm.getDeviceId());
        deviceBo.setUserBy(userBy);
        deviceBo.setCurr(curr);

        // 查询要删除的设备的详情
        DeviceVo deviceVo = iDeviceService.get(deviceBo);

        // 删除
        iDeviceService.del(deviceBo);
        // 删除坐标与的关系
        iPointMapService.del(operEid, deviceId, null, curr, userBy);
        ProductVo productVo = deviceVo.getProduct();
        // 20200309 新增通信协议
        if (CommonConstants.PRODUCT_CATEGORY_SENSOR.equals(deviceVo.getProductCategory()) && !"standard".equals(deviceVo.getProtocol())) {
            ThirdMapVo thirdMapVo = iThirdMapService.get(operEid, deviceId);
            if (thirdMapVo != null) {
                iThirdMapService.del(operEid, deviceId, curr, userBy);
            }
            iWaterPipeClient.deviceSubscribeDel(operEid, deviceVo.getCode());
        }

        // if (thirdFlag &&
        // CommonConstants.PRODUCT_CATEGORY_SENSOR.equals(productVo.getCategory().getVal())
        // &&
        // CommonConstants.DICTIONARY_FACTORYCODE_KBL.equals(productVo.getFactory().getCode()))
        // {
        // // 获取thirdMap 中的thirdId
        // ThirdMapVo thirdMapVo = iThirdMapService.get(operEid,
        // deviceForm.getDeviceId());
        // if (thirdMapVo == null) {
        // logger.error(LogMsg.to("ex",
        // PipeExceptionConstants.SENSOR_THIRD_MAP_NOT_EXIST, "operEid", operEid,
        // "deviceId", deviceForm.getDeviceId(), "msg", "本地thirdMap 不存在 "));
        // throw new
        // FrameworkRuntimeException(PipeExceptionConstants.SENSOR_THIRD_MAP_NOT_EXIST);
        // }
        // // 封装信息 第三方使用
        // Map<String, Object> params = new HashMap<String, Object>();
        // params.put("sensorCode", deviceForm.getCode());
        // params.put("sensorType", productVo.getType().getVal());
        // params.put("thirdId", thirdMapVo.getThirdId());
        // // 删除第三方
        // iBridgeFactory.deleteSensor(productVo.getFactory().getCode(), params);
        // // 删除thirdMap
        // iThirdMapService.del(operEid, deviceForm.getDeviceId(), curr, userBy);
        // }
        return null;
    }

    @Override
    public List<DeviceFieldVo> getDeviceField(String deviceCode) throws FrameworkRuntimeException {
        LoginCas loginCas = iAuthCasApi.get();
        String operEid = loginCas.getOperEid();
        return iDeviceService.getDeviceField(operEid, deviceCode);
    }

    @Override
    public Map<String, List<DevicePropertyVo>> getDeviceRealTime(DeviceDataForm deviceDataForm)
            throws FrameworkRuntimeException {
        LoginCas loginCas = iAuthCasApi.get();
        String operEid = loginCas.getOperEid();
        // 所需参数
        List<String> deviceIds = deviceDataForm.getDeviceIds();
        FieldTypeEnum[] fieldTypeEna = deviceDataForm.getFieldType();
        List<DevicePropertyVo> list = iDeviceService.getDeviceRealTime(deviceIds, fieldTypeEna, operEid);
        return listToMap(list);
    }

    private Map<String, List<DevicePropertyVo>> listToMap(List<DevicePropertyVo> list) {
        Map<String, List<DevicePropertyVo>> groupMap = new HashMap<>(16);
        if (CollectionUtils.isEmpty(list)) {
            return groupMap;
        }
        for (DevicePropertyVo vo : list) {
            if (groupMap.containsKey(vo.getField())) {
                groupMap.get(vo.getField()).add(vo);
            } else {
                List<DevicePropertyVo> reportVoList = new ArrayList<>();
                reportVoList.add(vo);
                groupMap.put(vo.getField(), reportVoList);
            }
        }
        return groupMap;
    }

    @Override
    public List<DeviceVo> getFmDevice() throws FrameworkRuntimeException {
        LoginCas loginCas = iAuthCasApi.get();
        String operEid = loginCas.getOperEid();
        return iDeviceService.getFmDevice(operEid);
    }

    @Override
    public Pagination<ProductVo> getDeviceCount(DeviceForm deviceForm) throws FrameworkRuntimeException {
        LoginCas loginCas = iAuthCasApi.get();
        String operEid = loginCas.getOperEid();
        boolean flag = CasConstants.isAdmin(loginCas);
        if (flag) { // 水务平台人员
            // String enterpriseId = deviceForm.getEnterpriseId();
            // if (StringUtils.isEmpty(enterpriseId)) {
            // operEid = null;
            // } else {
            // operEid = enterpriseId;
            // }
            operEid = deviceForm.getEnterpriseId();
        }
        DeviceBo deviceBo = new DeviceBo();
        deviceBo.setEnterpriseId(operEid);
        deviceBo.setProductCategory(deviceForm.getProductCategory());
        deviceBo.setPage(deviceForm.getPage());
        deviceBo.setPageSize(deviceForm.getPageSize());
        Pagination<ProductVo> list = iDeviceService.getDeviceCount(deviceBo);
        return list;
    }

    @Override
    public DeviceVo mergePipe(DeviceForm deviceForm) throws FrameworkRuntimeException {
        LoginCas loginCas = iAuthCasApi.get();
        String operEid = loginCas.getOperEid();
        String userBy = loginCas.getUserName();
        Date curr = new Date();
        // 批量删除管道: TODO
        DeviceBo deviceBo = new DeviceBo();
        deviceBo.setEnterpriseId(operEid);
        deviceBo.setUserBy(userBy);
        deviceBo.setCurr(curr);
        List<String> mergePipeIds = deviceForm.getMergePipeIds();
        // 删除管道和点的关联关系
        this.iPointMapService.del(operEid, mergePipeIds, null, curr, userBy);
        // 取出其中的第一个管道id
        String deviceId = mergePipeIds.get(0);
        mergePipeIds.remove(0);
        deviceBo.setDeviceIds(mergePipeIds);
        this.iDeviceService.del(deviceBo); // 批量删除 不包含第一个管道id

        // TODO 修改合并后的管道的长度
        List<PointForm> pointList = deviceForm.getPoints();
        PointForm point1 = deviceForm.getPoints().get(0);
        PointForm point2 = deviceForm.getPoints().get(1);
        String pointId1 = point1.getPointId();
        String longitude1 = point1.getLongitude().toString();
        String latitude1 = point1.getLatitude().toString();

        String pointId2 = point2.getPointId();
        String longitude2 = point2.getLongitude().toString();
        String latitude2 = point2.getLatitude().toString();

        String pipeLength = PipeLengthUtils.getPipeLength(longitude1, latitude1, longitude2, latitude2);

        deviceBo.setDeviceIds(null);// 参数置空
        deviceBo.setDeviceId(deviceId); // 设置id
        deviceBo.setLength(pipeLength);// 设置管道长度
        this.iDeviceService.edit(deviceBo);

        // String pointId1 = deviceForm.getPointIds().get(0);
        // String pointId2 = deviceForm.getPointIds().get(1);

        this.iPointMapService.add(operEid, pointId1, deviceId, curr, userBy);
        this.iPointMapService.add(operEid, pointId2, deviceId, curr, userBy);
        // 查询修改后的设备
        deviceBo.setDeviceId(deviceId);
        DeviceVo deviceVo = this.iDeviceService.get(deviceBo);
        return deviceVo;
    }

    @Override
    public Pagination<DeviceVo> pageBind(DeviceForm deviceForm) throws FrameworkRuntimeException {
        LoginCas loginCas = iAuthCasApi.get();
        String operEid = loginCas.getOperEid();
        // 封装参数
        DeviceBo deviceBo = new DeviceBo();
        deviceBo.setPage(deviceForm.getPage());
        deviceBo.setPageSize(deviceForm.getPageSize());
        deviceBo.setEnterpriseId(operEid);
        deviceBo.setProductCategory(deviceForm.getProductCategory()); // 产品类别
        deviceBo.setAreaId(deviceForm.getAreaId());
        deviceBo.setCode(deviceForm.getCode());
        deviceBo.setName(deviceForm.getName());
        deviceBo.setDeviceId(deviceForm.getDeviceId());
        deviceBo.setBindStatus(deviceForm.getBindStatus());
        Pagination<DeviceVo> pagination = iDeviceService.pageBind(deviceBo);
        return pagination;
    }

    @Override
    public void bindAdd(DeviceForm deviceForm) {
        LoginCas loginCas = iAuthCasApi.get();
        String operEid = loginCas.getOperEid();
        String userBy = loginCas.getUserName();
        Date curr = new Date();
        DeviceBo deviceBo = BeanUtils.copyProperties(deviceForm, DeviceBo.class);
        deviceBo.setEnterpriseId(operEid);
        deviceBo.setUserBy(userBy);
        deviceBo.setCurr(curr);
        iDeviceMapService.add(deviceBo);
    }

    @Override
    public void bindDel(DeviceForm deviceForm) {
        LoginCas loginCas = iAuthCasApi.get();
        String operEid = loginCas.getOperEid();
        String userBy = loginCas.getUserName();
        Date curr = new Date();
        DeviceBo deviceBo = BeanUtils.copyProperties(deviceForm, DeviceBo.class);
        deviceBo.setEnterpriseId(operEid);
        deviceBo.setUserBy(userBy);
        deviceBo.setCurr(curr);
        iDeviceMapService.del(deviceBo);
    }

    @Override
    public void editScales(List<DeviceForm> deviceForms) {
        LoginCas loginCas = iAuthCasApi.get();
        String operEid = loginCas.getOperEid();
        String userBy = loginCas.getUserName();
        Date curr = new Date();
        List<DeviceBo> deviceBos = BeanUtils.copy(deviceForms, DeviceBo.class);
        deviceBos.forEach(deviceBo -> {
            deviceBo.setEnterpriseId(operEid);
            deviceBo.setUserBy(userBy);
            deviceBo.setCurr(curr);
        });
        iDeviceService.editScales(deviceBos);
    }

    @Override
    public DijkstraVo connectedCal(DeviceForm deviceForm) {
        LoginCas loginCas = iAuthCasApi.get();
        String operEid = loginCas.getOperEid();
        // 封装参数
        DeviceBo deviceBo = BeanUtils.copyProperties(deviceForm, DeviceBo.class);
        deviceBo.setEnterpriseId(operEid);
        List<DeviceVo> list = iDeviceService.list(deviceBo);
        Map<String, List<PointToPipe>> matrixMap = DijkstraForthTUtils.toChange(list);
        DijkstraVo paths = DijkstraForthTUtils.allPathBetweenTwoPoints(matrixMap,
                deviceForm.getPointIds().get(0), deviceForm.getPointIds().get(1));
        return paths;
    }

    @Override
    public List<DeviceVo> listForApp(DeviceForm deviceForm) throws FrameworkRuntimeException {
        LoginCas loginCas = iAuthCasApi.get();
        String operEid = loginCas.getOperEid();
        // 封装参数
        DeviceBo deviceBo = BeanUtils.copyProperties(deviceForm, DeviceBo.class);
        deviceBo.setEnterpriseId(operEid);
        List<DeviceVo> list = iDeviceService.listForApp(deviceBo);
        if (PRODUCT_CATEGORY_CUSTOMIZE_DEVICE.equals(deviceForm.getProductCategory())
                || PRODUCT_CATEGORY_REGION.equals(deviceForm.getProductCategory())) {
            // 自定义设备类型 才会排序
            for (DeviceVo deviceVo : list) {
                deviceVo.getPoints().sort(Comparator.comparing(PointVo::sortBycode));
            }
        }
        return list;
    }
}
