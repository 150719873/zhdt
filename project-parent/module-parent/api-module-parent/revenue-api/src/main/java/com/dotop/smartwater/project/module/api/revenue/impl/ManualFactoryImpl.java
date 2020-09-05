package com.dotop.smartwater.project.module.api.revenue.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

import com.dotop.smartwater.project.module.api.revenue.IManualFactory;
import com.dotop.smartwater.project.module.core.water.utils.ParamUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.utils.DateUtils;
import com.dotop.smartwater.dependence.core.utils.UuidUtils;
import com.dotop.smartwater.project.module.core.auth.local.AuthCasClient;
import com.dotop.smartwater.project.module.core.auth.vo.UserVo;
import com.dotop.smartwater.project.module.core.water.bo.DeviceBo;
import com.dotop.smartwater.project.module.core.water.bo.DeviceUplinkBo;
import com.dotop.smartwater.project.module.core.water.bo.ModeBindBo;
import com.dotop.smartwater.project.module.core.water.bo.StoreProductBo;
import com.dotop.smartwater.project.module.core.water.config.Config;
import com.dotop.smartwater.project.module.core.water.constants.DeviceCode;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.module.core.water.constants.WaterConstants;
import com.dotop.smartwater.project.module.core.water.form.customize.ImportFileForm;
import com.dotop.smartwater.project.module.core.water.utils.ExcelUtil;
import com.dotop.smartwater.project.module.core.water.vo.DeviceVo;
import com.dotop.smartwater.project.module.core.water.vo.ModeBindVo;
import com.dotop.smartwater.project.module.core.water.vo.OwnerVo;
import com.dotop.smartwater.project.module.core.water.vo.StoreProductVo;
import com.dotop.smartwater.project.module.service.device.IDeviceService;
import com.dotop.smartwater.project.module.service.device.IDeviceUplinkService;
import com.dotop.smartwater.project.module.service.revenue.IOwnerService;
import com.dotop.smartwater.project.module.service.store.IStoreProductService;
import com.dotop.smartwater.project.module.service.tool.IDictionaryChildService;
import com.dotop.smartwater.project.module.service.tool.IModeConfigureService;

/**
 * 设备修改
 *

 * @date 2019年2月26日
 */
@Component
public class ManualFactoryImpl implements IManualFactory {

    private static final Logger logger = LoggerFactory.getLogger(ManualFactoryImpl.class);

    // 上传配置
    @Value("${ajaxUpload.tempUrl}")
    private String tempUrl;
    @Value("${ajaxUpload.MEMORY_THRESHOLD}")
    private int MEMORY_THRESHOLD;
    @Value("${ajaxUpload.MAX_FILE_SIZE}")
    private int MAX_FILE_SIZE;
    @Value("${ajaxUpload.MAX_REQUEST_SIZE}")
    private int MAX_REQUEST_SIZE;

    @Value("${param.revenue.excelTempUrl}")
    private String excelTempUrl;

    @Autowired
    private IDeviceService iDeviceService;

    @Autowired
    private IOwnerService iOwnerService;
    @Autowired
    private IDeviceUplinkService iDeviceUplinkService;

    @Autowired
    private IStoreProductService iStoreProductService;

    @Autowired
    public IDictionaryChildService iDictionaryChildService;

    @Autowired
    private IModeConfigureService iModeConfigureService;

    private POIFSFileSystem fs;
    private HSSFWorkbook wb;
    private HSSFSheet sheet;
    private HSSFRow row;


    //通讯方式唯一编码
    public static final String MODE_VALUE = "28,300001";

    private String[][] readExcelContent(InputStream is, int num) throws IOException {
        fs = new POIFSFileSystem(is);
        wb = new HSSFWorkbook(fs);
        sheet = wb.getSheetAt(0);
        // 获取总长度
        int rowNum = sheet.getPhysicalNumberOfRows();
        // System.out.println("Excel行数:"+rowNum);
        row = sheet.getRow(0);
        String[][] strArray = new String[rowNum][num];
        for (int i = 1; i < rowNum; i++) {
            String str = "";
            row = sheet.getRow(i);
            for (int j = 0; j < num; j++) {
                str = "";
                str += ExcelUtil.getCellFormatValue(row.getCell((short) j)).trim();
                if (StringUtils.isNotBlank(str)) {
                    strArray[i][j] = str;
                }
            }
        }
        is.close();
        return strArray;
    }

    /**
     * /批量导入抄表-导入
     */
    @Override
    public List<String> manualImport(ImportFileForm importfile) {
        UserVo user = AuthCasClient.getUser();
        if (user == null) {
            throw new FrameworkRuntimeException(ResultCode.Fail, "请重新登录");
        }

        List<String> result = new ArrayList<>();
        try {
            String filename = importfile.getFilename();
            String path = excelTempUrl + File.separator + filename;
            File file = new File(path);
            if (file.exists()) {
                FileInputStream is = new FileInputStream(file);
                String[][] map = readExcelContent(is, 4);
                for (int i = 1; i < map.length; i++) {
                    String devno = map[i][0];
                    String meter = map[i][1];
                    String uplinktime = map[i][2];
                    String remark = map[i][3];

                    if (StringUtils.isBlank(devno) || StringUtils.isBlank(meter) || StringUtils.isBlank(uplinktime)
                            || StringUtils.isBlank(remark)) {
                        continue;
                    }


                    if (StringUtils.isBlank(devno)) {
                        result.add("第" + (i + 1) + "行:水表编号为空");
                        continue;
                    }
                    if (StringUtils.isBlank(meter)) {
                        result.add("第" + (i + 1) + "行:水表读数为空");
                        continue;
                    }

                    DeviceVo d = iDeviceService.findByDevNo(devno);
                    if (d == null) {
                        result.add("第" + (i + 1) + "行:未找到水表");
                        continue;
                    }

                    // 不是传统表不能手动抄表
                    if (!DeviceCode.DEVICE_TYPE_TRADITIONAL.equals(d.getTypeid())) {
                        result.add("第" + (i + 1) + "行:不是传统表不能手动抄表");
                        continue;
                    }

                    OwnerVo owner = iOwnerService.getOwnerUserByDevId(d.getDevid());
                    if (owner != null) {
                        if (owner.getUpreadtime() != null && owner.getUpreadwater() != null) {
                            if (Double.parseDouble(meter) < Double.parseDouble(owner.getUpreadwater())) {
                                result.add("第" + (i + 1) + "行:抄表读数不能少于最近一期账单读数");
                                continue;
                            }
                        }
                    }

                    if (d.getBeginvalue() != null && d.getBeginvalue() > Double.parseDouble(meter)) {
                        result.add("第" + (i + 1) + "行:抄表读数不能少于水表初始读数");
                        continue;
                    }

                    Date date = new Date();
                    d.setWater(Double.valueOf(meter));
                    if (!StringUtils.isBlank(uplinktime)) {
                        d.setUplinktime(uplinktime);
                    } else {
                        d.setUplinktime(DateUtils.formatDatetime(date));
                    }

                    DeviceBo deviceBo = new DeviceBo();
                    BeanUtils.copyProperties(d, deviceBo);
                    iDeviceService.update(deviceBo);
                    DeviceUplinkBo up = new DeviceUplinkBo();
                    up.setConfirmed(false);
                    up.setUplinkData(remark);
                    up.setDeveui(d.getDeveui());
                    up.setDevid(d.getDevid());
                    if (!StringUtils.isBlank(uplinktime)) {
                        up.setRxtime(DateUtils.parseDatetime(uplinktime));
                    } else {
                        up.setRxtime(date);
                    }
                    up.setWater(meter);
                    up.setDate(DateUtils.format(date, DateUtils.YYYYMM));
                    iDeviceUplinkService.add(up);
                }
                return result;
            } else {
                return result;
            }
        } catch (Exception ex) {
            logger.error("import", ex.getMessage());
            throw new FrameworkRuntimeException(ResultCode.Fail, ex.getMessage());
        }
    }

    @Override
    public List<String> deviceImport(ImportFileForm importfile) {
        UserVo user = AuthCasClient.getUser();
        if (user == null) {
            throw new FrameworkRuntimeException(ResultCode.Fail, "请重新登录");
        }

        try {
            String filename = importfile.getFilename();
            String path = excelTempUrl + File.separator + filename;
            File file = new File(path);
            if (file.exists()) {
                FileInputStream is = new FileInputStream(file);
                String[][] data = readExcelContent(is, 10);

                // 产品类型
                StoreProductBo storeProductBo = new StoreProductBo();
                storeProductBo.setEnterpriseid(user.getEnterpriseid());
                List<StoreProductVo> productList = iStoreProductService.getLinePro(storeProductBo);
                if (productList == null || productList.isEmpty()) {
                    throw new FrameworkRuntimeException(ResultCode.ParamIllegal, "请先添加产品编号");
                }
                
                ModeBindBo modeBindBo = new ModeBindBo();
                modeBindBo.setEnterpriseid(user.getEnterpriseid());
                List<ModeBindVo> modeVoList = iModeConfigureService.listModeConfigure(modeBindBo);
                if (modeVoList == null || modeVoList.isEmpty()) {
                    throw new FrameworkRuntimeException(ResultCode.ParamIllegal, "该水司未配置任何通讯方式");
                }

                Map<String, String> modeMap =
                        modeVoList.stream().collect(
                                Collectors.toMap(x -> x.getModeName(), x -> x.getMode(), (oldValue, newValue) -> oldValue));


                Map<String, StoreProductVo> productMap =
                        productList.stream().collect(
                                Collectors.toMap(x -> x.getProductNo(), x -> x, (oldValue, newValue) -> oldValue));


                HashSet<String> devnos = new HashSet<>();
                List<String> devEuis = new ArrayList<>();
                for (int i = 2; i < data.length; i++) {
                    if(StringUtils.isNotBlank(data[i][3])){
                        devnos.add(data[i][3]);
                    }
                    if(StringUtils.isNotBlank(data[i][4])){
                        devEuis.add(data[i][4]);
                    }
                }
                if(devnos.size() != data.length-2){
                    throw new FrameworkRuntimeException(ResultCode.Fail, "excel有相同的水表号");
                }

                Map<String, String> checkMap =
                        devEuis.stream().collect(
                                Collectors.toMap(x -> x, x -> x, (oldValue, newValue) -> oldValue));

                if(checkMap.size() != devEuis.size()){
                    throw new FrameworkRuntimeException(ResultCode.Fail, "excel有相同的EUI");
                }

                // 解析数据
                List<DeviceBo> list = new ArrayList<DeviceBo>();
                for (int i = 2; i < data.length; i++) {
                    String check = ParamUtil.checkDeviceImportParam(i, data, modeMap, productMap);
                    if (check != null) {
                        throw new FrameworkRuntimeException(ResultCode.RUNNINGERROR, check);
                    }
                    String factory = data[i][0];
                    String productNo = data[i][1];
                    String typeName = data[i][2];
                    String devno = data[i][3];
                    String deveui = data[i][4];
                    String imsi = data[i][5];
                    String modeName = data[i][6];
                    String taptypeName = data[i][7];
                    Double beginWater = Double.parseDouble(data[i][8]);
                    Double currentWater = Double.parseDouble(data[i][9]);

                    DeviceVo d = iDeviceService.findByDevNo(devno);
                    if (d != null) {
                        throw new FrameworkRuntimeException(ResultCode.ParamIllegal,
                                "第" + (i + 1) + "行:" + devno + "该水表号已经存在");
                    }

                    //封装数据
                    DeviceBo deviceBo = new DeviceBo();
                    deviceBo.setDevid(UuidUtils.getUuid());

                    //远传表
                    if (typeName.equals(ParamUtil.WATER_REMOTE)) {

                        if(iDeviceService.findByDevEUI(deveui) != null){
                            throw new FrameworkRuntimeException(ResultCode.ParamIllegal,
                                    "第" + (i + 1) + "行:" + devno + "该水表EUI已经存在");
                        }

                        deviceBo.setTypeid(DeviceCode.DEVICE_TYPE_ELECTRONIC);

                        //获取通讯方式
                        deviceBo.setMode(modeMap.get(modeName));

                        deviceBo.setDeveui(deveui);
                        deviceBo.setStatus(WaterConstants.DEVICE_STATUS_NOACTIVE);
                        deviceBo.setExplain("未激活");

                    } else {    //传统表
                        deviceBo.setTypeid(DeviceCode.DEVICE_TYPE_TRADITIONAL);
                        if (StringUtils.isBlank(deveui)) {
                            // 构造传统表的EUI
                            deviceBo.setDeveui("" + Config.Generator.nextId());
                        } else {
                            deviceBo.setDeveui(deveui);
                        }
                        deviceBo.setStatus(DeviceCode.DEVICE_STATUS_OFFLINE);
                        deviceBo.setExplain("离线");
                    }


                    deviceBo.setKind(DeviceCode.DEVICE_KIND_REAL);
                    deviceBo.setDevno(devno);
                    deviceBo.setImsi(imsi);
                    deviceBo.setWater(currentWater);
                    deviceBo.setBeginvalue(beginWater);
                    deviceBo.setFactory(factory);

                    if (!StringUtils.isBlank(taptypeName) && taptypeName.equals("是")) {
                        deviceBo.setTaptype(DeviceCode.DEVICE_TAP_TYPE_WITH_TAP);
                    } else if (!StringUtils.isBlank(taptypeName) && taptypeName.equals("否")) {
                        deviceBo.setTaptype(DeviceCode.DEVICE_TAP_TYPE_NO_TAP);
                    }

                    deviceBo.setAccesstime(new Date());
                    deviceBo.setFlag(DeviceCode.DEVICE_FLAG_EDIT);
                    deviceBo.setEnterpriseid(user.getEnterpriseid());
                    deviceBo.setPid(DeviceCode.DEVICE_PARENT);

                    StoreProductVo product = productMap.get(productNo);
                    deviceBo.setProductId(product.getProductId());
                    deviceBo.setCaliber("" + product.getCaliber());

                    list.add(deviceBo);
                }

                // 批量插入
                iDeviceService.batchAdd(list);
                return null;

            } else {
                throw new FrameworkRuntimeException(ResultCode.Fail, "找不到文件");
            }
        } catch (Exception ex) {
            logger.error("deviceImport", ex.getMessage());
            throw new FrameworkRuntimeException(ResultCode.Fail, ex.getMessage());
        }
    }


}
