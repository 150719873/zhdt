package com.dotop.smartwater.project.module.api.revenue.impl;

import com.alibaba.fastjson.JSON;
import com.dotop.smartwater.dependence.cache.api.AbstractValueCache;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.utils.DateUtils;
import com.dotop.smartwater.dependence.core.utils.JSONUtils;
import com.dotop.smartwater.project.module.api.revenue.IAjaxUploadFactory;
import com.dotop.smartwater.project.module.client.third.http.IHttpTransfer;
import com.dotop.smartwater.project.module.core.auth.bo.UserLoraBo;
import com.dotop.smartwater.project.module.core.auth.local.AuthCasClient;
import com.dotop.smartwater.project.module.core.auth.vo.AreaNodeVo;
import com.dotop.smartwater.project.module.core.auth.vo.UserLoraVo;
import com.dotop.smartwater.project.module.core.auth.vo.UserVo;
import com.dotop.smartwater.project.module.core.water.bo.*;
import com.dotop.smartwater.project.module.core.water.constants.*;
import com.dotop.smartwater.project.module.core.water.form.customize.ImportFileForm;
import com.dotop.smartwater.project.module.core.water.utils.ExcelUtil;
import com.dotop.smartwater.project.module.core.water.utils.FileUtil;
import com.dotop.smartwater.project.module.core.water.utils.ParamUtil;
import com.dotop.smartwater.project.module.core.water.vo.*;
import com.dotop.smartwater.project.module.core.water.vo.customize.IotMsgEntityVo;
import com.dotop.smartwater.project.module.service.device.IDeviceBookManagementService;
import com.dotop.smartwater.project.module.service.device.IDeviceService;
import com.dotop.smartwater.project.module.service.revenue.IOwnerService;
import com.dotop.smartwater.project.module.service.revenue.IPayTypeService;
import com.dotop.smartwater.project.module.service.store.IStoreProductService;
import com.dotop.smartwater.project.module.service.tool.IUserLoraService;
import com.dotop.water.tool.service.BaseInf;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 报装 上传下载功能
 *

 * @date 2019年2月25日
 */
@Component
public class AjaxUploadFactoryImpl implements IAjaxUploadFactory {

    private final static Logger logger = LogManager.getLogger(AjaxUploadFactoryImpl.class);
    //    private Random rand = SecureRandom.getInstanceStrong();
    private Random rand = new SecureRandom();
    // 上传配置
    @Value("${param.revenue.excelTempUrl}")
    private String excelTempUrl;

    // 2MB
    /**
     * 最大文件长度
     */
    private static final int MAX_FILE_SIZE = 2097152;

    @Autowired
    private IHttpTransfer iHttpTransfer;

    @Autowired
    private IOwnerService iOwnerService;

    @Autowired
    private IDeviceService iDeviceService;

    @Autowired
    private IPayTypeService iPayTypeService;

    @Autowired
    private AbstractValueCache<String> avc;

    @Autowired
    private IStoreProductService iStoreProductService;

    @Autowired
    private IDeviceBookManagementService iDeviceBookManagementService;

    @Autowired
    private IUserLoraService iUserLoraService;

    public AjaxUploadFactoryImpl() throws NoSuchAlgorithmException {
    }

    @Override
    public String upload(HttpServletRequest request) throws FrameworkRuntimeException {
        // MultipartHttpServletRequest request1 = (MultipartHttpServletRequest)request;
        // 配置上传参数
        List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles("file");
        if (1 != files.size()) {
            throw new FrameworkRuntimeException(ResultCode.ParamIllegal, "没有找到上传文件");
        }

        try {
            MultipartFile file;
            for (MultipartFile file1 : files) {
                file = file1;
                if (!file.isEmpty()) {
                    try {
                        logger.info(LogMsg.to("file", file.getSize()));
                        byte[] bytes = file.getBytes();
                        if (bytes.length > MAX_FILE_SIZE) {
                            throw new FrameworkRuntimeException(ResultCode.Fail, "上传的文件过大");
                        }

                        // 构造临时路径来存储上传的文件
                        // 这个路径相对当前应用的目录
                        String uploadPath = excelTempUrl;
                        logger.info(LogMsg.to("uploadPath", uploadPath));
                        // 如果目录不存在则创建
                        File uploadDir = new File(uploadPath);
                        if (!uploadDir.exists()) {
                            uploadDir.mkdir();
                        }
                        logger.info(LogMsg.to("uploadPath", uploadPath));
                        FileUtil.deleteFiles(uploadDir, 10);
                        logger.info(LogMsg.to("deleteFiles", "deleteFiles"));
                        logger.info(LogMsg.to("file", file));
                        logger.info(LogMsg.to("file", file.getOriginalFilename()));
                        String filename = newFileName(file.getOriginalFilename());
                        logger.info(LogMsg.to("filename", filename));
                        String suffix = filename.substring(filename.lastIndexOf(".") + 1);
                        if (!"xls".equals(suffix)) {
                            throw new FrameworkRuntimeException(ResultCode.Fail, "上传的文件后缀不是xls");
                        }
                        String filePath = uploadPath + File.separator + filename;
                        logger.info(LogMsg.to("filePath", "filePath"));
                        Files.write(Paths.get(filePath), bytes);
                        return filename;
                    } catch (Exception e) {
                        logger.error("AjaxUploadFile error", e);
                        throw new FrameworkRuntimeException(ResultCode.Fail, e.getMessage());
                    }
                } else {
                    throw new FrameworkRuntimeException(ResultCode.Fail, "空文件！");
                }
            }
            logger.error("upload fail: formItems.size() " + 0);
            throw new FrameworkRuntimeException(ResultCode.Fail, "upload fail: formItems.size() ");
        } catch (Exception e) {
            throw new FrameworkRuntimeException(ResultCode.Fail, "upload fail: " + e.getMessage());
        }
    }

    private String newFileName(String oldname) {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        String date = format.format(new Date());
        return date + rand.nextInt(10000) + "_" + oldname;
    }

    /**
     * 报装管理-业主报装-导入
     */
    @Override
    public void importOwner(HttpServletRequest request, ImportFileForm importfile) throws FrameworkRuntimeException {
        UserVo user = AuthCasClient.getUser();
        if (user == null) {
            throw new FrameworkRuntimeException(ResultCode.TimeOut, "登录超时,请重新登录");
        }
        
        // 收费种类
        Map<String, PayTypeVo> priceType = iPayTypeService.getPayTypeMap(user.getEnterpriseid());
        /*if (priceType == null || priceType.size() == 0) {
            throw new FrameworkRuntimeException(ResultCode.ParamIllegal, "请先添加价格类型");
        }*/
       
        //区域编号
        Map<String, AreaNodeVo> areaMap = BaseInf.getAreaMaps(user.getUserid(), user.getTicket());
        if (areaMap == null || areaMap.size() == 0) {
            throw new FrameworkRuntimeException(ResultCode.ParamIllegal, "请先添加区域");
        }
        Map<String, String> areaNoteMap = new HashMap<>(areaMap.size());
        for (AreaNodeVo vo : areaMap.values()) {
            areaNoteMap.put(vo.getCode().toUpperCase(), vo.getKey());
        }

        //表册号
        DeviceBookManagementBo deviceBookManagementBo = new DeviceBookManagementBo();
        deviceBookManagementBo.setEnterpriseid(user.getEnterpriseid());
        List<DeviceBookManagementVo> deviceBookManagementVos = iDeviceBookManagementService.list(deviceBookManagementBo);

        Map<String, String> deviceBookMap;
        if (deviceBookManagementVos == null || deviceBookManagementVos.size() == 0) {
            deviceBookMap = new HashMap<>(1);
        } else {
            deviceBookMap = deviceBookManagementVos.stream().collect(
                    Collectors.toMap(x -> x.getBookNum(), x -> x.getBookNum(), (oldValue, newValue) -> oldValue));
        }

        try {
            String filename = importfile.getFilename();
            if (StringUtils.isBlank(filename)) {
                throw new FrameworkRuntimeException(ResultCode.ParamIllegal, "没有上传文件");
            }
            
            String path = excelTempUrl + File.separator + filename;
            File file = new File(path);
            if (file.exists()) {
                FileInputStream is = new FileInputStream(file);
                String[][] map = ExcelUtil.readExcelContent(is);

                if (map == null || map.length <= 2) {
                    throw new FrameworkRuntimeException(ResultCode.ParamIllegal, "excel里面没有业主数据");
                }

                List<OwnerBo> list = new ArrayList<>();
                Date createtime = new Date();
                Set<String> set = new HashSet<>();
                int checkUserNo = 0;
                Set<String> devNoSet = new HashSet<>();
                int devNoCount = 0;

                // 校验是否已被使用
                Map<String, String> DevNoMap = new HashMap<>();
                Map<String, DeviceVo> deviceMap = new HashMap<>();
                for (int i = 2; i < map.length; i++) {
                    String devNo = map[i][8];
                    String userNo = map[i][2];
                    if (StringUtils.isNotBlank(devNo)) {
                        devNoSet.add(devNo);
                        devNoCount ++;
                    }
                    
                    OwnerBo owner1 = new OwnerBo();
                    owner1.setDevno(devNo);
                    OwnerVo owner2 = iOwnerService.findOwnerByDevNo(owner1);
                    if (owner2 != null) {
                        throw new FrameworkRuntimeException(ResultCode.RUNNINGERROR, "第" + (i+1) + "行 :" +
                                devNo + "水表编号已经被使用");
                    }
                    
                    if (StringUtils.isNotBlank(userNo)) {
                        set.add(userNo);
                        checkUserNo ++;
                    }
                }
               
                if (checkUserNo != set.size()) {
                    throw new FrameworkRuntimeException(ResultCode.RUNNINGERROR, "excel中有相同的业主编号了");
                }

                if (devNoCount != devNoSet.size()) {
                    throw new FrameworkRuntimeException(ResultCode.RUNNINGERROR, "excel中有相同的水表号");
                }
                
                List<String> userNos = dataCheck(set);
                if (userNos != null && userNos.size() > 0) {
                    throw new FrameworkRuntimeException(ResultCode.RUNNINGERROR,
                            "系统中已经有和excel中相同的业主编号:" + JSONUtils.toJSONString(userNos));
                }

                List<String> devNos = devNoCheck(devNoSet);
                if (userNos != null && devNos.size() > 0) {
                    throw new FrameworkRuntimeException(ResultCode.RUNNINGERROR,
                            "系统中已经有和excel中相同的水表号:" + JSONUtils.toJSONString(devNos));
                }
                
                
                List<DeviceVo> deviceList = iOwnerService.findDevByDevNos(devNoSet);
                if (deviceList != null && deviceList.size() > 0) {
                	for (DeviceVo device : deviceList) {
                    	DevNoMap.put(device.getDevno(), device.getDevid());
                    	deviceMap.put(device.getDevno(), device);
                    }	
                }
                

                // 前面2行不要
                for (int i = 2; i < map.length; i++) {
                    // 过滤空行
                    if (StringUtils.isBlank(map[i][0]) && StringUtils.isBlank(map[i][2])
                            && StringUtils.isBlank(map[i][3])
                            && StringUtils.isBlank(map[i][6])) {
                        continue;
                    }

                    String check = ParamUtil.checkOwnerImportParam(i,map,areaNoteMap,priceType,DevNoMap);
                    if(check != null){
                        throw new FrameworkRuntimeException(ResultCode.RUNNINGERROR, check);
                    }

                    String areaNo = map[i][0].toUpperCase();
                    String bookNum = map[i][1];
                    String ownerNo = map[i][2];
                    String ownerName = map[i][3];
                    String ownerPhone = map[i][4];
                    String ownerIdCard = map[i][5];
                    String ownerAddress = map[i][6];
                    String isOpen = map[i][7];
                    String devNo = map[i][8];
                    String payName = map[i][9];

                    String areaID = areaNoteMap.get(areaNo);

                    OwnerBo owner = new OwnerBo();
                    owner.setCommunityid(areaID);
                    owner.setUsername(ownerName);
                    owner.setUserno(ownerNo);
                    owner.setUserphone(ownerPhone);
                    owner.setUseraddr(ownerAddress);

                    owner.setCardtype(1);
                    owner.setCardid(ownerIdCard);
                    owner.setCreatetime(createtime);
                    owner.setStatus(WaterConstants.OWNER_STATUS_UNOPRN);
                    owner.setCreateuser(user.getUserid());
                    owner.setAlreadypay(0.0);
                    owner.setEnterpriseid(user.getEnterpriseid());

                    //设置表册号
                    owner.setBookNum(deviceBookMap.get(bookNum));
                    owner.setDevid(DevNoMap.get(devNo));
                    owner.setDevno(devNo);
                    
                    if (priceType != null && priceType.get(payName) != null) {
                    	owner.setPaytypeid(priceType.get(payName).getTypeid());	
                    }
                    
                    
                    if (isOpen.trim().equals("是")) {
                        owner.setInstallmonth(DateUtils.format(createtime, DateUtils.YYYY_MM));
                        owner.setStatus(WaterConstants.OWNER_STATUS_CREATE);
                    } else {
                        owner.setStatus(WaterConstants.OWNER_STATUS_UNOPRN);
                    }

                    // 设备迁移到指定账号下
                    if (StringUtils.isNotBlank(owner.getDevno()) && StringUtils.isNotBlank(devNo)) {
                		migrateDevice(deviceMap.get(devNo) ,user);
                    }
                    list.add(owner);
                }

                // 测性能
                iOwnerService.webaddList(list);
                is.close();
            } else {
                throw new FrameworkRuntimeException(ResultCode.RUNNINGERROR, "找不到上传的Excel");
            }

        } catch (Exception e) {
            logger.info("import_owner", e);
            throw new FrameworkRuntimeException(ResultCode.Fail, e.getMessage());
        }
    }
    
    
    public void migrateDevice(DeviceVo device ,UserVo user) {
    	// 如果水司ID不相同，则迁移设备
    	if (!device.getEnterpriseid().equals(user.getEnterpriseid())) {
            UserLoraVo ul = iUserLoraService.findByEnterpriseId(user.getEnterpriseid());
            if (ul == null || StringUtils.isBlank(ul.getAccount())) {
                throw new FrameworkRuntimeException(ResultCode.RUNNINGERROR,
                        device.getDevno() + "未找到当前水司对应IOT账号");
            }

            // 开户时若EnterpriseId不同,先在旧账号IOT解绑,再新绑定新账号下的IOT平台,让IOT与LPC做关联
            String token = avc.get(CacheKey.WaterIotToken + device.getEnterpriseid());
            UserLoraVo ulOld = iUserLoraService.findByEnterpriseId(device.getEnterpriseid());
            if (ulOld == null || StringUtils.isBlank(ulOld.getAccount())) {
                throw new FrameworkRuntimeException(ResultCode.RUNNINGERROR,
                        device.getDevno() + "未找到原水司IOT对应账号");
            }
            
            //IOT账号不相同，则解绑设备后重新注册
            if (!ul.getAccount().equals(ulOld.getAccount())) {	
            	
            	// 自己的通讯方式则需要切换IOT
            	if (device.getMode() != null && (device.getMode().equals("28,300001,1") || device.getMode().equals("28,300001,2")
                  		 || device.getMode().equals("28,300001,3") || device.getMode().equals("28,300001,10"))) {
            		UserLoraBo userLoraBo = new UserLoraBo();
                    BeanUtils.copyProperties(ulOld, userLoraBo);
                    
                    if (token == null) {
                        IotMsgEntityVo authResult = iHttpTransfer.getLoginInfo(userLoraBo);
                        if (authResult != null && AppCode.IotSucceccCode.equals(authResult.getCode())) {
                            Map map1 = (Map) JSON.parse(authResult.getData().toString());
                            token = (String) (map1.get("token"));
                            avc.set(CacheKey.WaterIotToken + device.getEnterpriseid(), token);
                        } else {
                            throw new FrameworkRuntimeException(ResultCode.RUNNINGERROR,
                                    device.getDevno() + "获取旧IOT Token错误");
                        }
                    }

                    DeviceBo deviceBo = new DeviceBo();
                    BeanUtils.copyProperties(device, deviceBo);
                    
                    IotMsgEntityVo iotMsgEntity = null;
                    // Lora表在IOT平台智能调用删除接口，NB表调解绑接口
                    if (device.getMode().equals("28,300001,1")) {
                    	iotMsgEntity = iHttpTransfer.cleanDevice(token, deviceBo, userLoraBo);	
                    } else {
                    	iotMsgEntity = iHttpTransfer.delDevice(token, deviceBo, userLoraBo);
                    }
                    
                    if (iotMsgEntity != null && AppCode.IotSucceccCode.equals(iotMsgEntity.getCode())) {
                        logger.info("[开户导入]:{} 从IOT平台删除", device.getName());
                    } else {
                        throw new FrameworkRuntimeException(ResultCode.RUNNINGERROR, device.getDevno()
                                + "设备不能从IOT平台删除[开户导入]:" + iotMsgEntity.getCode());
                    }

                    userLoraBo = new UserLoraBo();
                    BeanUtils.copyProperties(ul, userLoraBo);

                    // 再在IOT绑定
                    String tokenNew = avc.get(CacheKey.WaterIotToken + ul.getEnterpriseid());
                    if (tokenNew == null) {
                        IotMsgEntityVo authResult = iHttpTransfer.getLoginInfo(userLoraBo);
                        if (authResult != null && AppCode.IotSucceccCode.equals(authResult.getCode())) {
                            Map map1 = (Map) JSON.parse(authResult.getData().toString());
                            tokenNew = (String) (map1.get("token"));
                            avc.set(CacheKey.WaterIotToken + ul.getEnterpriseid(), tokenNew);
                        } else {
                            throw new FrameworkRuntimeException(ResultCode.RUNNINGERROR,
                                    device.getDevno() + "获取新IOT Token错误");
                        }
                    }

                    // 创建id
                    deviceBo.setDevid(UUID.randomUUID().toString());
                    deviceBo.setName("auto"+deviceBo.getDevno());
                    iotMsgEntity = iHttpTransfer.addDevice(tokenNew, deviceBo, userLoraBo);
                    if (iotMsgEntity != null && AppCode.IotSucceccCode.equals(iotMsgEntity.getCode())) {
                        logger.info("{} 成功添加进IOT平台[开户导入]", device.getDevno());
                    } else {
                        throw new FrameworkRuntimeException(ResultCode.RUNNINGERROR, device.getDevno()
                                + "不能添加进IOT平台[开户导入],错误码:" + iotMsgEntity.getCode());
                    }
            	}
            }

            // 更新水表公司ID
            device.setEnterpriseid(user.getEnterpriseid());
            DeviceBo deviceBo1 = new DeviceBo();
            BeanUtils.copyProperties(device, deviceBo1);
            iDeviceService.updateByPrimaryKeySelective(deviceBo1);
        }
    } 
    

    // 验证业主编号唯一性
    private List<String> dataCheck(Set<String> userNoSet) {
        return iOwnerService.userNoCheck(userNoSet);
    }

    // 验证水表唯一性
    private List<String> devNoCheck(Set<String> devNoSet) {
        return iOwnerService.devNoCheck(devNoSet);
    }
}
