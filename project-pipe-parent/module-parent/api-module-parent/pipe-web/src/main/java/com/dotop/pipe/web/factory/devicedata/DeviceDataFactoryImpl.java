package com.dotop.pipe.web.factory.devicedata;

import com.alibaba.fastjson.JSONObject;
import com.dotop.pipe.web.api.factory.devicedata.IDeviceDataFactory;
import com.dotop.pipe.api.service.area.IAreaService;
import com.dotop.pipe.api.service.device.IDeviceService;
import com.dotop.pipe.api.service.devicedata.IDeviceDataService;
import com.dotop.pipe.api.service.point.IPointMapService;
import com.dotop.pipe.api.service.point.IPointService;
import com.dotop.pipe.api.service.product.IProductService;
import com.dotop.pipe.auth.cas.web.IAuthCasWeb;
import com.dotop.pipe.auth.core.vo.auth.LoginCas;
import com.dotop.pipe.core.constants.CommonConstants;
import com.dotop.pipe.core.form.DeviceForm;
import com.dotop.pipe.core.form.PointForm;
import com.dotop.pipe.core.form.PointMapForm;
import com.dotop.pipe.core.utils.ImportExcelUtils;
import com.dotop.pipe.core.utils.PipeLengthUtils;
import com.dotop.pipe.core.vo.area.AreaModelVo;
import com.dotop.pipe.core.vo.device.DeviceVo;
import com.dotop.pipe.core.vo.point.PointVo;
import com.dotop.pipe.core.vo.product.ProductVo;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 */
@Component("deviceDataFactoryImplImport")
public class DeviceDataFactoryImpl implements IDeviceDataFactory {

    private final static Logger logger = LogManager.getLogger(DeviceDataFactoryImpl.class);

    @Value("${download.error.txt.path:/opt/smartwater/pipe/upload}")
    private String downloadBasePath;

    private int precision = 7;

    @Autowired
    private IDeviceDataService iDeviceDataService;
    @Autowired
    private IAreaService iAreaService;
    @Autowired
    private IProductService iProductService;
    @Autowired
    private IPointService iPointService;
    @Autowired
    private IDeviceService iDeviceService;
    @Autowired
    private IPointMapService iPointMapService;

    @Autowired
    private IAuthCasWeb iAuthCasApi;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
    public String importDate(MultipartFile file) throws FrameworkRuntimeException {
        // 当前用户的信息
        Date curr = new Date();
        LoginCas loginCas = iAuthCasApi.get();
        String operEid = loginCas.getOperEid();
        String userBy = loginCas.getUserName();
        // 查询所有的区域信息 map类型
        Map<String, AreaModelVo> areaMap = iAreaService.mapAll(operEid);
        // 查询所有的产品信息
        Map<String, ProductVo> productMap = iProductService.mapAll(operEid);
        // 查询所有的坐标信息
        Map<String, PointVo> pointMap = iPointService.mapAll(operEid);
        // 设备所有的信息
        Map<String, DeviceVo> deviceMap = iDeviceService.mapAll(operEid);

        boolean areaFlag = true;
        boolean productFlag = true;
        boolean pointFlag = true;
        boolean deviceFlag = true;
        boolean isAddFlag = true; // 如果有一次校验出错 就 设置为false
        boolean isTempFlag;
        // 日志文件输出
        FileOutputStream fileOutputStream = null;
        // 存储坐标相关的信息
        List<PointForm> pointList = new ArrayList<>();
        // 存储产品相关的信息
        List<DeviceForm> deviceList = new ArrayList<>();

        // 存储 pointMap 关联表信息
        List<PointMapForm> pointMapList = new ArrayList<>();

        String fileName = "error" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()).toString() + ".txt";
        String errorFilePath = downloadBasePath + fileName;
        File errorfile = new File(errorFilePath);
        String word;
        try {
            if (!errorfile.exists()) {
                errorfile.createNewFile();
            }
            fileOutputStream = new FileOutputStream(errorfile);
            InputStream is = file.getInputStream();
            ImportExcelUtils importExcelUtils = new ImportExcelUtils();
            // 1 读取sheet页 和title
            importExcelUtils.readExcelTitle(is);
            // 返回所有的sheet页
            Map<String, XSSFSheet> sheetMap = importExcelUtils.getSheetMap();
            // 返回所有的标题
            Map<String, String[]> titleMap = importExcelUtils.getTitleMap();
            // 读取sheet页数据
            for (String sheetName : sheetMap.keySet()) {
                XSSFSheet sheet = sheetMap.get(sheetName);
                // 将sheet页中的数据 分别存放到不同的list中
                // 读取总行数
                int rowNum = sheet.getLastRowNum() + 1;
                // 读取列数
                int colNum = titleMap.get(sheetName).length;

                // 根据列表的名字将数据封装到list中
                for (int curRowNum = 1; curRowNum < rowNum; curRowNum++) {
                    boolean checkFlag1 = true;
                    XSSFRow curRow = sheet.getRow(curRowNum); // 读取行数据
                    String deviceCode = importExcelUtils.getCellFormatValue(curRow.getCell(0)); // 设备编码
                    checkFlag1 = checkCell("deviceCode", deviceCode, fileOutputStream, sheetName, curRowNum); // 校验 不能为空
                    isAddFlag = checkFlag1 == false ? checkFlag1 : isAddFlag;
                    String deviceName = importExcelUtils.getCellFormatValue(curRow.getCell(1)); // 设备名称
                    String deviceDes = importExcelUtils.getCellFormatValue(curRow.getCell(2)); // 设备描述
                    String deviceLength = importExcelUtils.getCellFormatValue(curRow.getCell(3)); // 长度
                    String deviceDepth = importExcelUtils.getCellFormatValue(curRow.getCell(4)); // 深度
                    String devicePipeElevation = importExcelUtils.getCellFormatValue(curRow.getCell(5)); // 管顶高程
                    String deviceGroundElevation = importExcelUtils.getCellFormatValue(curRow.getCell(6)); // 地面高程
                    String deviceVersion = importExcelUtils.getCellFormatValue(curRow.getCell(7)); // 版本
                    String areaName = importExcelUtils.getCellFormatValue(curRow.getCell(8)); // 区域名称
                    checkFlag1 = checkCell("areaName", areaName, fileOutputStream, sheetName, curRowNum); // 校验 不能为空
                    isAddFlag = checkFlag1 == false ? checkFlag1 : isAddFlag;
                    String areaCode = importExcelUtils.getCellFormatValue(curRow.getCell(9)); // 区域编号
                    checkFlag1 = checkCell("areaCode", areaCode, fileOutputStream, sheetName, curRowNum); // 校验 不能为空
                    isAddFlag = checkFlag1 == false ? checkFlag1 : isAddFlag;
                    String productCode = importExcelUtils.getCellFormatValue(curRow.getCell(10)); // 产品编码
                    checkFlag1 = checkCell("productCode", productCode, fileOutputStream, sheetName, curRowNum); // 校验
                    isAddFlag = checkFlag1 == false ? checkFlag1 : isAddFlag;
                    // 不能为空
                    String productCategoryName = importExcelUtils.getCellFormatValue(curRow.getCell(11)); // 产品类型
                    String productCategory = CommonConstants.reverse(productCategoryName);
                    checkFlag1 = checkCell("productCategory", productCategory, fileOutputStream, sheetName, curRowNum); // 校验
                    isAddFlag = checkFlag1 == false ? checkFlag1 : isAddFlag;
                    // 不能为空
                    String pointCode1 = importExcelUtils.getCellFormatValue(curRow.getCell(12)); // 坐标编号1
                    checkFlag1 = checkCell("pointCode1", pointCode1, fileOutputStream, sheetName, curRowNum); // 校验 不能为空
                    isAddFlag = checkFlag1 == false ? checkFlag1 : isAddFlag;
                    String pointLongitude1 = importExcelUtils.getCellFormatValue(curRow.getCell(13)); // 经度1
                    checkFlag1 = checkCell("pointLongitude1", pointLongitude1, fileOutputStream, sheetName, curRowNum); // 校验
                    isAddFlag = checkFlag1 == false ? checkFlag1 : isAddFlag;
                    pointLongitude1 = subNum(pointLongitude1, precision);

                    // 不能为空
                    String pointLatitude1 = importExcelUtils.getCellFormatValue(curRow.getCell(14)); // 纬度1
                    checkFlag1 = checkCell("pointLatitude1", pointLatitude1, fileOutputStream, sheetName, curRowNum); // 校验
                    isAddFlag = checkFlag1 == false ? checkFlag1 : isAddFlag;
                    pointLatitude1 = subNum(pointLatitude1, precision);

                    // 不能为空
                    String pointCode2 = importExcelUtils.getCellFormatValue(curRow.getCell(15)); // 坐标编号2
                    String pointLongitude2 = importExcelUtils.getCellFormatValue(curRow.getCell(16)); // 经度2
                    pointLongitude2 = subNum(pointLongitude2, precision);
                    String pointLatitude2 = importExcelUtils.getCellFormatValue(curRow.getCell(17)); // 纬度2
                    pointLatitude2 = subNum(pointLatitude2, precision);
                    if (!isAddFlag) {
                        continue;
                    }
                    // 设备
                    DeviceForm deviceForm = new DeviceForm();
                    // 设备 坐标 关联表
                    PointMapForm pointMapForm = new PointMapForm();
                    PointMapForm pointMapForm2 = new PointMapForm(); // 管道设备时 需要两个pointForm
                    // 校验区域并记录
                    if (areaMap.containsKey(areaCode)) { // 区域存在
                        deviceForm.setAreaId(areaMap.get(areaCode).getAreaId()); // 封装areaId
                        areaFlag = true;
                    } else { // 区域不存在 记录校验不通过信息
                        areaFlag = false;
                        isAddFlag = false;
                        checkCell("areaCodeExist", areaCode, fileOutputStream, sheetName, curRowNum); // 校验
                    }

                    // 校验产品信息
                    if (productMap.containsKey(productCode)) {
                        ProductVo por = productMap.get(productCode);
                        deviceForm.setProductId(por.getProductId()); // 产品存在
                        deviceForm.setProductCategory(por.getCategory().getVal());
                        deviceForm.setProductType(por.getType().getVal());
                        productFlag = true;
                    } else { // 产品不存在 记录校验不通过信息
                        productFlag = false;
                        isAddFlag = false;
                        checkCell("productCodeExist", productCode, fileOutputStream, sheetName, curRowNum); // 校验
                    }

                    // 校验坐标是否存在
                    if (pointMap.containsKey(pointCode1)) {
                        // 这个坐标点存在 记录这个坐标的id
                        pointFlag = true;
                        String pointId = pointMap.get(pointCode1).getPointId();
                        pointMapForm.setPointId(pointId);
                    } else {
                        // 如果不存在 插入此point
                        pointFlag = false;
                        String pointId1 = UUID.randomUUID().toString();
                        // 创建point对象
                        PointForm pointForm1 = new PointForm(pointId1, pointCode1, new BigDecimal(pointLongitude1),
                                new BigDecimal(pointLatitude1));

                        // 封装pointMap 信息
                        pointMapForm.setPointId(pointId1);
                        // 将到导入的信息添加map
                        pointMap.put(pointCode1, new PointVo(pointId1, pointCode1));
                        // 坐标对象添加到集合中
                        pointList.add(pointForm1);
                    }
                    // 管道设备有两个点连接 需要做两个校验
                    if ("pipe".equals(productCategory)) {
                        isTempFlag = checkCell("pointCode2", pointCode2, fileOutputStream, sheetName, curRowNum); // 校验
                        if (isAddFlag) {
                            isAddFlag = isTempFlag;
                        }

                        if (pointMap.containsKey(pointCode2)) {
                            // 这个坐标点存在 记录这个坐标的id
                            pointFlag = true;
                            String pointId = pointMap.get(pointCode2).getPointId();
                            pointMapForm2.setPointId(pointId);
                        } else {
                            isTempFlag = checkCell("pointLongitude2", pointLongitude2, fileOutputStream, sheetName,
                                    curRowNum); // 校验 不能为空
                            if (isAddFlag) {
                                isAddFlag = isTempFlag;
                            }
                            isTempFlag = checkCell("pointLatitude2", pointLatitude2, fileOutputStream, sheetName,
                                    curRowNum); // 校验 不能为空
                            if (isAddFlag) {
                                isAddFlag = isTempFlag;
                            }
                            if (isAddFlag) { // 校验不等于空通过
                                // 如果不存在 插入此point
                                pointFlag = false;
                                String pointId2 = UUID.randomUUID().toString();
                                // 创建point对象
                                PointForm pointForm2 = new PointForm(pointId2, pointCode2,
                                        new BigDecimal(pointLongitude2), new BigDecimal(pointLatitude2));
                                // 封装pointMap 信息
                                pointMapForm2.setPointId(pointId2);
                                // 将到导入的信息添加map
                                pointMap.put(pointCode2, new PointVo(pointId2, pointCode2));
                                // 坐标对象添加到集合中
                                pointList.add(pointForm2);
                            }
                        }
                        if (StringUtils.isBlank(deviceLength)) {
                            deviceLength = PipeLengthUtils.getPipeLength(pointLongitude1, pointLatitude1,
                                    pointLongitude2, pointLatitude2);
                        }

                    }

                    // 校验设备是否存在
                    if (deviceMap.containsKey(deviceCode)) {
                        // 设备code 存在 拿出这个deviceid
                        deviceFlag = true;
                        String deviceId = deviceMap.get(deviceCode).getDeviceId();
                        pointMapForm.setDeviceId(deviceId);
                        pointMapForm2.setDeviceId(deviceId);
                    } else {
                        deviceFlag = false;
                        deviceForm.setDeviceId(UUID.randomUUID().toString());
                        deviceForm.setCode(deviceCode);
                        deviceForm.setName(deviceName);
                        deviceForm.setDes(deviceDes);
                        deviceForm.setLength(deviceLength);
                        deviceForm.setDepth(deviceDepth);
                        deviceForm.setPipeElevation(devicePipeElevation);
                        deviceForm.setGroundElevation(deviceGroundElevation);
                        deviceForm.setVersion(deviceVersion);
                        deviceForm.setScale("1000");
                        pointMapForm.setDeviceId(deviceForm.getDeviceId());
                        pointMapForm2.setDeviceId(deviceForm.getDeviceId());
                        deviceMap.put(deviceCode, new DeviceVo(deviceForm.getDeviceId(), deviceCode));
                    }

                    // 判断存在不用添加到list 不存在添加
                    if (areaFlag && productFlag) { // 区域和产品 存在
                        // && pointFlag && deviceFlag
                        // if (!pointFlag) { // 不存在
                        pointMapList.add(pointMapForm);
                        if ("pipe".equals(productCategory)) {
                            pointMapList.add(pointMapForm2);
                        }
                        // }
                        if (!deviceFlag) { // 不存在
                            deviceList.add(deviceForm);
                        }
                    }
                }
            }

            if (isAddFlag) {
                // 执行插入sql 1 先插入point 在插入device
                // 校验point device 关联表 pointMap信息 先看是否存在 存在不更新
                for (PointMapForm pointMapForm : pointMapList) {
                    boolean flag = iPointMapService.isExistByDeviceId(operEid, pointMapForm.getPointId(),
                            pointMapForm.getDeviceId());
                    if (!flag) { // 不存在
                        iPointMapService.add(operEid, pointMapForm.getPointId(), pointMapForm.getDeviceId(), curr,
                                userBy);
                    }
                }
                // 判断是否有不存在的信息
                if (pointList != null && pointList.size() > 0) {
                    iDeviceDataService.addPointList(pointList, operEid, userBy, curr);
                }
                if (deviceList != null && deviceList.size() > 0) {
                    iDeviceDataService.addDeviceList(deviceList, operEid, userBy, curr);
                }
                errorfile.delete();
                JSONObject json = new JSONObject();
                json.put("code", "success");
                json.put("msg", null);
                return json.toString();

            } else { // 文件校验不通过
                JSONObject json = new JSONObject();
                json.put("code", "error");
                json.put("msg", fileName);
                return json.toString();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            JSONObject json = new JSONObject();
            json.put("code", "exception");
            json.put("msg", "导入异常");
            return json.toString();
        } catch (IOException e) {
            e.printStackTrace();
            JSONObject json = new JSONObject();
            json.put("code", "exception");
            json.put("msg", "导入异常");
            return json.toString();
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.flush();
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private boolean checkCell(String cellName, String cellValve, FileOutputStream fileOutputStream, String sheetName,
                              int curRowNum) throws UnsupportedEncodingException, IOException {
        String word = null;
        boolean isAddFlag = true;
        switch (cellName) {
            case "deviceCode": // 设备编码
                if (StringUtils.isBlank(cellValve)) { //
                    word = "sheet页名称:" + sheetName + "第" + curRowNum + "行 设备校验出错:设备编码不能为空"
                            + System.getProperty("line.separator");
                    isAddFlag = false;
                }
                break;
            case "areaCode": // 区域编码
                if (StringUtils.isBlank(cellValve)) { //
                    word = "sheet页名称:" + sheetName + "第" + curRowNum + "行 区域信息校验出错:区域编码不能为空"
                            + System.getProperty("line.separator");
                    isAddFlag = false;
                }
                break;
            case "areaCodeExist":
                word = "sheet页:" + sheetName + "第" + curRowNum + "行区域编码校验出错:区域编码" + cellValve + "可能不存在,需要联系管理员添加"
                        + System.getProperty("line.separator");
                break;
            case "areaName": // 区域名称
                if (StringUtils.isBlank(cellValve)) { //
                    word = "sheet页名称:" + sheetName + "第" + curRowNum + "行 区域信息校验出错:区域名称不能为空"
                            + System.getProperty("line.separator");
                    isAddFlag = false;
                }
                break;
            case "productCode": // 产品编码
                if (StringUtils.isBlank(cellValve)) { //
                    word = "sheet页名称:" + sheetName + "第" + curRowNum + "行 产品信息校验出错:产品编码不能为空"
                            + System.getProperty("line.separator");
                    isAddFlag = false;
                }
                break;
            case "productCodeExist":
                word = "sheet页:" + sheetName + "第" + curRowNum + "行 产品校验出错:产品编码" + cellValve + "可能不存在,需要联系管理员添加"
                        + System.getProperty("line.separator");
                break;
            case "productCategoryName": // 产品类型
                if (StringUtils.isBlank(cellValve)) { //
                    word = "sheet页名称:" + sheetName + "第" + curRowNum + "行 产品信息校验出错:产品类型不能为空"
                            + System.getProperty("line.separator");
                    isAddFlag = false;
                }
                break;
            case "pointCode1": // 坐标code
                if (StringUtils.isBlank(cellValve)) { //
                    word = "sheet页名称:" + sheetName + "第" + curRowNum + "行 坐标信息校验出错:坐标编码不能为空"
                            + System.getProperty("line.separator");
                    isAddFlag = false;
                }
                break;
            case "pointCode2": // 坐标code
                if (StringUtils.isBlank(cellValve)) { //
                    word = "sheet页名称:" + sheetName + "第" + curRowNum + "行 坐标信息校验出错:坐标编码不能为空"
                            + System.getProperty("line.separator");
                    isAddFlag = false;
                }
                break;
            case "pointLongitude1": // 坐标code
                if (StringUtils.isBlank(cellValve)) { //
                    word = "sheet页名称:" + sheetName + "第" + curRowNum + "行 坐标信息校验出错:坐标经纬度不能为空"
                            + System.getProperty("line.separator");
                    isAddFlag = false;
                }
                break;
            case "pointLongitude2": // 坐标code
                if (StringUtils.isBlank(cellValve)) { //
                    word = "sheet页名称:" + sheetName + "第" + curRowNum + "行 坐标信息校验出错:坐标经纬度不能为空"
                            + System.getProperty("line.separator");
                    isAddFlag = false;
                }
                break;
            case "pointLatitude1": // 坐标code
                if (StringUtils.isBlank(cellValve)) { //
                    word = "sheet页名称:" + sheetName + "第" + curRowNum + "行 坐标信息校验出错:坐标经纬度不能为空"
                            + System.getProperty("line.separator");
                    isAddFlag = false;
                }
                break;
            case "pointLatitude2": // 坐标code
                if (StringUtils.isBlank(cellValve)) { //
                    word = "sheet页名称:" + sheetName + "第" + curRowNum + "行 坐标信息校验出错:坐标经纬度不能为空"
                            + System.getProperty("line.separator");
                    isAddFlag = false;
                }
                break;
            case "productCategory":
                if (StringUtils.isBlank(cellValve)) { //
                    word = "sheet页名称:" + sheetName + "第" + curRowNum + "行 产品错误:此产品不允许导入"
                            + System.getProperty("line.separator");
                    isAddFlag = false;
                }
                break;
            default:
                break;
        }
        if (StringUtils.isNotBlank(word)) {
            fileOutputStream.write(word.getBytes("UTF-8"));
        }
        return isAddFlag;
    }

    public static void main(String[] args) {
        System.out.println(subNum("144.12345678901234566", 7));
        System.out.println(subNum("144.123", 7));
    }

    private static String subNum(String str, int len) {
        if (StringUtils.isNotBlank(str)) {
            BigDecimal bd = new BigDecimal(str);
            return bd.setScale(len, BigDecimal.ROUND_HALF_EVEN).toString();
            // java.text.DecimalFormat df = new java.text.DecimalFormat("###.#########");
            // return df.format(str) ;
        }
        return null;
    }

}
