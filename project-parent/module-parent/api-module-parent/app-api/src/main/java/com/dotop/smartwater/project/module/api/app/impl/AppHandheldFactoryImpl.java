package com.dotop.smartwater.project.module.api.app.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dotop.smartwater.dependence.cache.StringValueCache;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.DateUtils;
import com.dotop.smartwater.project.module.api.app.IAppHandheldFactory;
import com.dotop.smartwater.project.module.cache.app.IAppUserCache;
import com.dotop.smartwater.project.module.client.third.http.IHttpTransfer;
import com.dotop.smartwater.project.module.core.app.vo.AppHandheldVo;
import com.dotop.smartwater.project.module.core.auth.bo.UserLoraBo;
import com.dotop.smartwater.project.module.core.auth.local.AuthCasClient;
import com.dotop.smartwater.project.module.core.auth.vo.EnterpriseVo;
import com.dotop.smartwater.project.module.core.auth.vo.SettlementVo;
import com.dotop.smartwater.project.module.core.auth.vo.UserLoraVo;
import com.dotop.smartwater.project.module.core.auth.vo.UserParamVo;
import com.dotop.smartwater.project.module.core.auth.vo.UserVo;
import com.dotop.smartwater.project.module.core.third.form.iot.DeviceReplaceForm;
import com.dotop.smartwater.project.module.core.third.form.iot.MeterDataForm;
import com.dotop.smartwater.project.module.core.third.form.iot.UserEntryForm;
import com.dotop.smartwater.project.module.core.water.bo.DeviceBatchRelationBo;
import com.dotop.smartwater.project.module.core.water.bo.DeviceBo;
import com.dotop.smartwater.project.module.core.water.bo.DeviceChangeBo;
import com.dotop.smartwater.project.module.core.water.bo.DeviceUplinkBo;
import com.dotop.smartwater.project.module.core.water.bo.OwnerBo;
import com.dotop.smartwater.project.module.core.water.config.Config;
import com.dotop.smartwater.project.module.core.water.constants.AppCode;
import com.dotop.smartwater.project.module.core.water.constants.CacheKey;
import com.dotop.smartwater.project.module.core.water.constants.DeviceCode;
import com.dotop.smartwater.project.module.core.water.constants.IotCode;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.module.core.water.constants.WaterConstants;
import com.dotop.smartwater.project.module.core.water.form.DeviceChangeForm;
import com.dotop.smartwater.project.module.core.water.vo.DeviceChangeVo;
import com.dotop.smartwater.project.module.core.water.vo.DeviceVo;
import com.dotop.smartwater.project.module.core.water.vo.OwnerVo;
import com.dotop.smartwater.project.module.core.water.vo.customize.IotMsgEntityVo;
import com.dotop.smartwater.project.module.service.device.IDeviceService;
import com.dotop.smartwater.project.module.service.device.IDeviceUplinkService;
import com.dotop.smartwater.project.module.service.revenue.IOwnerService;
import com.dotop.smartwater.project.module.service.tool.IDeviceBatchService;
import com.dotop.smartwater.project.module.service.tool.ISettlementService;
import com.dotop.smartwater.project.module.service.tool.IUserLoraService;
import com.dotop.water.tool.service.AppInf;

@Component
public class AppHandheldFactoryImpl implements IAppHandheldFactory {

    private final static Logger logger = LogManager.getLogger(AppHandheldFactoryImpl.class);

    @Autowired
    private IHttpTransfer iHttpTransfer;

    @Autowired
    private StringValueCache svc;

    @Autowired
    private IAppUserCache iAppUserCache;

    @Autowired
    private IOwnerService iOwnerService;

    @Resource
    private IUserLoraService iUserLoraService;

    @Autowired
    private IDeviceUplinkService iDeviceUplinkService;

    @Autowired
    private IDeviceService iDeviceService;
    
    @Autowired
    private ISettlementService iSettlementService;
    
    @Autowired
    private IDeviceBatchService deviceBatchService;

    // 获取水司
    @Override
    public AppHandheldVo getEnterprise() {
        String res = null;
        logger.info("[getEnterprise] request :{}", "");
        try {
            List<EnterpriseVo> list = AppInf.getAppEnterprises();
            if (list.size() > 0) {
                StringBuilder ja = new StringBuilder("[");
                JSONObject jo = new JSONObject();
                for (int i = 0; i < list.size(); i++) {
                    jo.put("id", list.get(i).getEnterpriseid());
                    jo.put("name", list.get(i).getName());
                    ja.append(jo);
                    if (i < list.size() - 1) {
                        ja.append(",");
                    }
                }
                res = ja.toString();
                return new AppHandheldVo(AppCode.SUCCESS, "success", res);
            }
            return new AppHandheldVo(AppCode.SUCCESS, "success", null);
        } catch (Exception e) {
            logger.info("exception : {}", e.getMessage());
            logger.error("login error", e);
            return new AppHandheldVo(AppCode.SERVICE_ERROR, e.getMessage(), null);
        } finally {
            logger.info("[getEnterprise] return json : {}", res);
        }
    }

    // 登录
    @Override
    public AppHandheldVo login(UserEntryForm userEntryForm) {
        String res = null;
        try {
            UserEntryForm data = userEntryForm;

            UserParamVo u = AppInf.appLogin(data.getAccount(), data.getPassword(), userEntryForm.getEnterpriseid());
            if (u == null) {
                return new AppHandheldVo(AppCode.ACCOUNT_NOT_EXIST, "账号密码不匹配!", null);
            }
            // 用户信息写入缓存
            iAppUserCache.setUserParamVo(u.getUserid(), u);

            String token = u.getTicket();
            JSONObject jo = new JSONObject();
            jo.put("userid", u.getUserid());
            jo.put("token", token);
            jo.put("expired", Config.TOKEN_TIME);
            jo.put("calibration", u.getCalibration());

            res = jo.toString();
            return new AppHandheldVo(AppCode.SUCCESS, "登录成功!", res);

        } catch (Exception e) {
            logger.info("exception : {}", e.getMessage());
            logger.error("login error", e);
            return new AppHandheldVo(AppCode.SERVICE_ERROR, e.getMessage(), null);
        } finally {
            logger.info("[login] return json : {}", res);
        }

    }

    // 换表记录
    public Pagination<DeviceChangeVo> replacePage(DeviceChangeForm form) {
    	DeviceChangeBo bo = new DeviceChangeBo();
		BeanUtils.copyProperties(form, bo);
		UserVo user = AuthCasClient.getUser();
		bo.setCreateById(user.getUserid());
		return iDeviceService.replacePage(bo);
    }
    
    // 更换水表
    @Override
    public AppHandheldVo replace(DeviceReplaceForm form) {
    	try {
    		UserVo user = AuthCasClient.getUser();
            if (user == null) {
                return new AppHandheldVo(AppCode.Token_Expired, "用户信息超时,请重新登陆", null);
            }
    		
    		// 获取原水表信息
    		DeviceVo device = iDeviceService.findByDevEUI(form.getScDeveui());
    		if (device == null) {
    			return new AppHandheldVo(AppCode.DEVICE_NOT_EXIST, "未找到原水表信息", null);
    		}
    		
    		// 获取新水表信息
    		DeviceVo newDevice = iDeviceService.findByDevEUI(form.getNwDeveui());
    		
    		
    		// 获取IOT通讯秘钥
            UserLoraVo ul = iUserLoraService.findByEnterpriseId(user.getEnterpriseid());
            if (ul == null) {
            	return new AppHandheldVo(AppCode.NOT_SET_IOTACCOUNT, "未关联Iot账号,请联系管理员", null);
            } 
			
            String IotToken = svc.get(CacheKey.WaterIotToken + user.getEnterpriseid());
            if (IotToken == null) {
                UserLoraBo userLoraBo = new UserLoraBo();
                BeanUtils.copyProperties(ul, userLoraBo);
                IotMsgEntityVo authResult = iHttpTransfer.getLoginInfo(userLoraBo);
                if (authResult != null && AppCode.IotSucceccCode.equals(authResult.getCode())) {
                    Map map = (Map) JSON.parse(authResult.getData().toString());
                    IotToken = (String) (map.get("token"));
                    svc.set(CacheKey.WaterIotToken + ul.getEnterpriseid(), IotToken, 1800L);
                } else {
                    return new AppHandheldVo(AppCode.SERVICE_ERROR, "获取Iot令牌出现异常，请联系管理员", null);
                }
            }
            
    		
    		// 验证新设备是否已绑定-已绑定
    		if (newDevice != null && !StringUtils.isEmpty(newDevice.getDeveui())) {
    			//已开户
    			if (!StringUtils.isEmpty(newDevice.getUserno())) {						
    				return new AppHandheldVo(AppCode.DEVICE_IS_BAND_OWNER, "新设备已开户，无法更换", null);
    			} 																
				
    			//验证通讯方式是否一致
				if (device.getMode().equals(newDevice.getMode())) {					
					// 更换新水表编号、读数
					newDevice.setDevno(device.getDevno());
					newDevice.setWater(form.getNwWater());
					newDevice.setEnterpriseid(device.getEnterpriseid());
					DeviceBo bo = new DeviceBo(); 
					BeanUtils.copyProperties(newDevice, bo);
					bo.setPid(device.getPid());
					bo.setDeviceParId(device.getDeviceParId());
					bo.setBatchNumber(device.getBatchNumber());
					bo.setProductId(device.getProductId());
					iDeviceService.update(bo);
					
					// 更新原设备绑定的业主信息
					OwnerVo owner = new OwnerVo();
					owner.setDevid(newDevice.getDevid());
					owner.setOwnerid(device.getOwnerid());
					iOwnerService.updateDevid(owner);
					
					// 删除原设备信息(水务)
                    iDeviceService.deleteById(device.getDevid());
					
					// 在IOT平台删除设备
                    DeviceBo deviceBo = new DeviceBo();
                    BeanUtils.copyProperties(device, deviceBo);
                    UserLoraBo userLoraBo = new UserLoraBo();
                    BeanUtils.copyProperties(ul, userLoraBo);
                    
                    IotMsgEntityVo iotMsgEntity = iHttpTransfer.delDevice(IotToken, deviceBo, userLoraBo);
                    if (iotMsgEntity != null && AppCode.IotSucceccCode.equals(iotMsgEntity.getCode())) {
                        logger.info(device.getName() + "从IOT平台删除");
                    } else {
                        if (iotMsgEntity != null && AppCode.IotDeviceNotExixt.equals(iotMsgEntity.getCode())) {
                            logger.info(device.getName() + "从IOT平台删除:" + AppCode.IotDeviceNotExixt);
                        } else {
                            throw new FrameworkRuntimeException(ResultCode.Fail,
                            		device.getName() + "设备不能从IOT平台删除:" + iotMsgEntity.getCode());
                        }
                    }
					
				} else {
					return new AppHandheldVo(AppCode.DISTINCT_DEVICE, "不同类设备无法更换", null);
				}
    		} else {																//新水表未注册             	
            	DeviceBo deviceBo = new DeviceBo();
            	deviceBo.setDeveui(form.getNwDeveui());
            	deviceBo.setDevno(device.getDevno());
            	deviceBo.setName("auto"+device.getDevno());
            	deviceBo.setAccesstime(new Date());
            	deviceBo.setBeginvalue(device.getBeginvalue());
            	deviceBo.setFlag(device.getFlag());
            	deviceBo.setStatus(device.getStatus());
            	deviceBo.setExplain(device.getExplain());
            	deviceBo.setMode(device.getMode());
            	deviceBo.setImsi(form.getImsi());
            	deviceBo.setEnterpriseid(device.getEnterpriseid());
            	deviceBo.setTaptype(device.getTaptype());
            	deviceBo.setTapstatus(device.getTapstatus());
            	deviceBo.setTypeid(device.getTypeid());
            	deviceBo.setKind(device.getKind());
            	deviceBo.setBatchNumber(device.getBatchNumber());
            	deviceBo.setDeviceParId(device.getDeviceParId());
            	
            	deviceBo.setDeviceParId(device.getDeviceParId());
            	deviceBo.setPid(device.getPid());
            	deviceBo.setProductId(device.getProductId());
            	deviceBo.setCaliber(device.getCaliber());
            	deviceBo.setNfcInitPwd(form.getNfcInitPwd());
            	deviceBo.setNfcComPwd(form.getNfcComPwd());
            	deviceBo.setNfcTag(form.getNfcTag());
            	deviceBo.setUnit(device.getUnit());
            	deviceBo.setSensorType(device.getSensorType());
            	deviceBo.setWater(form.getNwWater());
            	
            	 UserLoraBo userLoraBo = new UserLoraBo();
                 BeanUtils.copyProperties(ul, userLoraBo);
                 IotMsgEntityVo iotMsgEntity = iHttpTransfer.addDevice(IotToken, deviceBo, userLoraBo);
                 if (iotMsgEntity != null && AppCode.IotSucceccCode.equals(iotMsgEntity.getCode())) {
                     logger.info(deviceBo.getDevno() + "成功添加进IOT平台");
                     DeviceVo regDev = iDeviceService.add(deviceBo);
                     
                     // 更新原设备绑定的业主信息
                     OwnerVo owner = new OwnerVo();
                     owner.setDevid(regDev.getDevid());
                     owner.setOwnerid(device.getOwnerid());
                     iOwnerService.updateDevid(owner);
                     
                     // 删除原设备信息(水务)
                     iDeviceService.deleteById(device.getDevid());
                     
                     // 在IOT平台删除原设备
                     BeanUtils.copyProperties(device, deviceBo);
                     BeanUtils.copyProperties(ul, userLoraBo);
                     
                     iotMsgEntity = iHttpTransfer.delDevice(IotToken, deviceBo, userLoraBo);
                     if (iotMsgEntity != null && AppCode.IotSucceccCode.equals(iotMsgEntity.getCode())) {
                         logger.info(device.getName() + "从IOT平台删除");
                     } else {
                         if (iotMsgEntity != null && AppCode.IotDeviceNotExixt.equals(iotMsgEntity.getCode())) {
                             logger.info(device.getName() + "从IOT平台删除:" + AppCode.IotDeviceNotExixt);
                         } else {
                             throw new FrameworkRuntimeException(ResultCode.Fail,
                             		device.getName() + "设备不能从IOT平台删除:" + iotMsgEntity.getCode());
                         }
                     }
                     
                     
                     
                 } else {
                     if (iotMsgEntity != null && iotMsgEntity.getCode() != null) {
                         return new AppHandheldVo(AppCode.SERVICE_ERROR, IotCode.getMessage(iotMsgEntity.getCode()), null);
                     }
                 }
    		}
    		
    		// 新增换表记录
            DeviceChangeBo deviceChangeBo = new DeviceChangeBo();
            deviceChangeBo.setScDeveui(device.getDeveui());
            deviceChangeBo.setScDevno(device.getDevno());
            deviceChangeBo.setScMode(device.getMode());
            deviceChangeBo.setScWater(device.getWater() != null ? ""+device.getWater() : null);
            deviceChangeBo.setNwDeveui(form.getNwDeveui());
            deviceChangeBo.setNwDevno(device.getDevno());
            deviceChangeBo.setNwWater(form.getNwWater());
            
            deviceChangeBo.setUserno(device.getUserno());
            deviceChangeBo.setUsername(device.getUsername());
            deviceChangeBo.setNwWater(form.getNwWater());
            deviceChangeBo.setUseraddr(device.getUseraddr());
            deviceChangeBo.setReason(form.getReason());
            deviceChangeBo.setCreateById(user.getUserid());
            deviceChangeBo.setUserBy(user.getName());
            deviceChangeBo.setCurr(new Date());
            deviceChangeBo.setEnterpriseid(user.getEnterpriseid());
            iDeviceService.insertDeviceChangeRecord(deviceChangeBo);
            
            return new AppHandheldVo(IotCode.Success, "水表已更换!", null);
        } catch (Exception e) {
            logger.error("device replace error", e);
            return new AppHandheldVo(AppCode.SERVICE_ERROR, e.getMessage(), null);
        }
    }
    
    
    // 接收 手持机 设备eui , 水表id 和 水表 初始读数
    @Override
    public AppHandheldVo register(MeterDataForm data) {
        try {
            //验证设备是否绑定
        	DeviceVo d1 = iDeviceService.findByDevNo(data.getDevno());
        	if(d1 != null){
                return new AppHandheldVo(AppCode.DEVNO_IS_EXIST, "水表编号已存在!", null);
            }

            d1 = iDeviceService.findByDevEUI(data.getDeveui());
            if (d1 != null && !StringUtils.isEmpty(d1.getDevno())) {
                return new AppHandheldVo(AppCode.DEVEUI_IS_BAND, "设备已绑定!", null);
            }

            if (d1 != null && data.getDevno().equals(d1.getDevno())) {
                return new AppHandheldVo(AppCode.DEVNO_IS_EXIST, "水表编号已存在!", null);
            }
            
            UserVo user = AuthCasClient.getUser();
            if (user == null) {
                return new AppHandheldVo(AppCode.Token_Expired, "用户信息超时,请重新登陆", null);
            }

            // 获取IOT通讯秘钥
            UserLoraVo ul = iUserLoraService.findByEnterpriseId(user.getEnterpriseid());
            if (ul == null) {
                return new AppHandheldVo(AppCode.NOT_SET_IOTACCOUNT, "未关联Iot账号,请联系管理员", null);
            }

            String IotToken = svc.get(CacheKey.WaterIotToken + user.getEnterpriseid());
            if (IotToken == null) {
                UserLoraBo userLoraBo = new UserLoraBo();
                BeanUtils.copyProperties(ul, userLoraBo);
                IotMsgEntityVo authResult = iHttpTransfer.getLoginInfo(userLoraBo);
                if (authResult != null && AppCode.IotSucceccCode.equals(authResult.getCode())) {
                    Map map = (Map) JSON.parse(authResult.getData().toString());
                    IotToken = (String) (map.get("token"));
                    svc.set(CacheKey.WaterIotToken + ul.getEnterpriseid(), IotToken, 1800L);
                } else {
                    return new AppHandheldVo(AppCode.SERVICE_ERROR, "获取Iot令牌出现异常，请联系管理员", null);
                }
            }

            DeviceBo d3 = new DeviceBo();
            d3.setDeveui(data.getDeveui());
            d3.setDevno(data.getDevno());
            d3.setName("auto"+data.getDevno());
            d3.setAccesstime(new Date());
            d3.setBeginvalue(Double.valueOf(data.getMeter()));
            d3.setFlag(WaterConstants.DEVICE_FLAG_NEW);
            d3.setStatus(WaterConstants.DEVICE_STATUS_NOACTIVE);
            d3.setExplain("未激活");
            d3.setMode(data.getMode());
            d3.setImsi(data.getImsi());
            d3.setEnterpriseid(user.getEnterpriseid());
            d3.setBindType(data.getBindType());

            // 水表类型
            if (data.getTapType() != null) {
                d3.setTaptype(data.getTapType());
            } else {
            	d3.setTaptype(WaterConstants.DEVICE_TAP_TYPE_WITH_TAP);
            }
            
            // 阀门状态
            if (data.getTapStatus() != null) {
                d3.setTapstatus(data.getTapStatus());
            }
            // 版本
            if (data.getVersion() != null) {
                d3.setVersion(data.getVersion());
            }
            // 洗阀周期
            if (data.getTapcycle() != null) {
                d3.setTapcycle(data.getTapcycle());
            }
            // 电量
            if (data.getBattery() != null) {
                d3.setBattery(data.getBattery());
            }
            // RSSI
            if (data.getRssi() != null) {
                d3.setRssi(data.getRssi());
            }

            /** APP绑定一定为实表和电子表 */
            d3.setTypeid(DeviceCode.DEVICE_TYPE_ELECTRONIC);
            d3.setKind(DeviceCode.DEVICE_KIND_REAL);

            /** 批次号、设备类型ID 、产品信息、口径*/
            d3.setBatchNumber(data.getBatchNumber());
            d3.setDeviceParId(data.getDeviceParId());
            d3.setPid(DeviceCode.DEVICE_PARENT);
            d3.setProductId(data.getProductId());
            d3.setCaliber(data.getCaliber());
            
            //NFC通讯密码记录
            d3.setNfcInitPwd(data.getNfcInitPwd());
            d3.setNfcComPwd(data.getNfcComPwd());
            d3.setNfcTag(data.getNfcTag());
            
            //计量单位和传感器类型
            d3.setUnit(data.getUnit());
            d3.setSensorType(data.getSensorType());
            
            DeviceBo device = new DeviceBo();
            device.setDeveui(data.getDeveui());
            device.setMode(data.getMode());
            // 获取样品IOT账号通讯秘钥
            SettlementVo settlementVo = iSettlementService.getSettlement(user.getEnterpriseid());
            if(settlementVo != null) {
            	UserLoraBo userLoraBo1 = new UserLoraBo();
                userLoraBo1.setEnterpriseid(user.getEnterpriseid());
                userLoraBo1.setAccount(settlementVo.getIotAccount());
                userLoraBo1.setPassword(settlementVo.getIotPassword());
                userLoraBo1.setAppeui(settlementVo.getAppEui());
                // 在样品IOT账号删除该设备
                IotMsgEntityVo iotMsgEntity1 = iHttpTransfer.delDevice(IotToken, device, userLoraBo1);
                if (iotMsgEntity1 != null && AppCode.IotSucceccCode.equals(iotMsgEntity1.getCode())) {
                    logger.info(d3.getDevno() + "成功从样品IOT账号删除掉设备");
                } else if (iotMsgEntity1 != null && AppCode.IotDeviceNotExixt.equals(iotMsgEntity1.getCode())){
                	logger.info(d3.getDevno() + "样品账号下无此设备");
                } 
            }
            
            UserLoraBo userLoraBo = new UserLoraBo();
            BeanUtils.copyProperties(ul, userLoraBo);
            IotMsgEntityVo iotMsgEntity = iHttpTransfer.addDevice(IotToken, d3, userLoraBo);
            if (iotMsgEntity != null && AppCode.IotSucceccCode.equals(iotMsgEntity.getCode())) {
                logger.info(d3.getDevno() + "成功添加进IOT平台");
                DeviceVo deviceVo = iDeviceService.add(d3);
                
                // 如果绑定时类型为"生产",则需要更新设备批次数据
                if (d3.getBindType() != null && d3.getBindType().equals(DeviceCode.DEVICE_BIND_PRODUCT)) {
                	DeviceBatchRelationBo batchBo = new DeviceBatchRelationBo();
                	batchBo.setSerialNumber(d3.getBatchNumber());
                	batchBo.setDevid(deviceVo.getDevid());
                	batchBo.setEnterpriseid(user.getEnterpriseid());
                	deviceBatchService.updateBatch(batchBo);
                }
                
                return new AppHandheldVo(IotCode.Success, "水表绑定成功!", null);
            } else {
                if (iotMsgEntity != null && iotMsgEntity.getCode() != null) {
                    return new AppHandheldVo(AppCode.SERVICE_ERROR, IotCode.getMessage(iotMsgEntity.getCode()), null);
                }
            }

            return new AppHandheldVo("-1", "绑定异常，请联系管理员!", null);
        } catch (Exception e) {
            logger.info("exception :" + e.getMessage());
            logger.error("register error", e);
            return new AppHandheldVo(AppCode.SERVICE_ERROR, e.getMessage(), null);
        }
    }

    // 获取水表是否已绑定
    @Override
    public AppHandheldVo isBind(MeterDataForm meterDataForm) {
        try {
            MeterDataForm data = meterDataForm;
            DeviceVo d = iDeviceService.findByDevEUI(data.getDeveui());
            if (d == null) {
                return new AppHandheldVo(AppCode.DEVICE_NOT_EXIST, "设备不存在!", null);
            } else {
                if (StringUtils.isEmpty(d.getDevno())) {
                    return new AppHandheldVo(AppCode.DEVICE_NOT_EXIST, "设备不存在!", null);
                } else {
                    JSONObject jo = new JSONObject();
                    jo.put("devno", d.getDevno());
                    jo.put("water", d.getWater());
                    jo.put("deviceParId", d.getDeviceParId());
                    jo.put("nfcInitPwd", d.getNfcInitPwd());
                    jo.put("nfcComPwd", d.getNfcComPwd());
                    jo.put("nfcTag", d.getNfcTag());
                    // 看看有没有绑定已开户的用户，有不能解绑，需要销户后才可以解绑
                    OwnerBo ownerBo = new OwnerBo();
                    ownerBo.setKeywords(d.getDevno());
                    OwnerVo ownerVo = iOwnerService.findOwnerByDevNo(ownerBo);
                    if (ownerVo != null) {
                        return new AppHandheldVo(AppCode.DEVICE_IS_BAND_OWNER, "设备已绑定!", jo.toString());
                    }
                    return new AppHandheldVo(AppCode.SUCCESS, "设备未绑定!", jo.toString());
                }
            }
        } catch (Exception e) {
            logger.info("exception :" + e.getMessage());
            logger.error("isBind error", e);
            return new AppHandheldVo(AppCode.SERVICE_ERROR, e.getMessage(), null);
        }
    }

    // 接收 手持机 手动抄表 上传数据 设备eui 和 水表读数
    @Override
    public AppHandheldVo manualMeterRead(MeterDataForm meterDataForm) {
        try {
            MeterDataForm data = meterDataForm;

            if (!AppInf.verification(data.getUserid(), data.getToken())) {
                return new AppHandheldVo(AppCode.TOKEN_ERROR, "token 错误!", null);
            }
            DeviceVo d = iDeviceService.findByDevEUI(data.getDeveui());
            if (d == null) {
                return new AppHandheldVo(AppCode.DEVICE_NOT_EXIST, "设备不存在!", null);
            }
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            d.setWater(Double.valueOf(data.getMeter()));
            d.setUplinktime(sdf.format(date));

            DeviceBo ddo = new DeviceBo();
            BeanUtils.copyProperties(d, ddo);
            iDeviceService.update(ddo);

            DeviceUplinkBo up = new DeviceUplinkBo();
            up.setConfirmed(false);
            up.setUplinkData("手持机手动抄表");
            up.setDeveui(d.getDeveui());
            up.setDevid(d.getDevid());
            up.setRxtime(date);
            up.setWater(data.getMeter());
            up.setDate(DateUtils.formatDatetime(date));
            iDeviceUplinkService.add(up);

        } catch (Exception e) {
            logger.info("exception :" + e.getMessage());
            logger.error("manualMeterRead error", e);
            return new AppHandheldVo(AppCode.SERVICE_ERROR, e.getMessage(), null);
        }
        return new AppHandheldVo(AppCode.SUCCESS, "手动抄表上传数据成功!", null);
    }

    // 解除水表与表号的绑定
    @Override
    public AppHandheldVo unBind(MeterDataForm meterDataForm) {
        try {
            MeterDataForm data = meterDataForm;

            DeviceVo d = iDeviceService.findByDevEUI(data.getDeveui());
            if (d == null) {
                return new AppHandheldVo(AppCode.DEVICE_NOT_EXIST, "设备不存在!", null);
            } else {
                if (StringUtils.isEmpty(d.getDevno())) {
                    return new AppHandheldVo(AppCode.DEVICE_NOT_BAND, "设备未绑定!", d.getWater());
                } else {
                    // 看看有没有绑定已开户的用户，有不能解绑，需要销户后才可以解绑
                    OwnerBo ownerBo = new OwnerBo();
                    ownerBo.setDevno(d.getDevno());
                    OwnerVo ownerVo = iOwnerService.findOwnerByDevNo(ownerBo);
                    if (ownerVo != null) {
                        return new AppHandheldVo(AppCode.DEVICE_IS_BAND_OWNER, "水表已开户，不能解绑!", null);
                    }
                    // 让IOT与LPC取消关联
                    UserVo user = AuthCasClient.getUser();
                    if (d.getEnterpriseid().equals(user.getEnterpriseid())) {
                        String WaterIotToken = svc.get(CacheKey.WaterIotToken + user.getEnterpriseid());
                        UserLoraVo ul = iUserLoraService.findByEnterpriseId(user.getEnterpriseid());
                        if (ul == null) {
                            return new AppHandheldVo(AppCode.NOT_SET_IOTACCOUNT, "没在系统设置水司关联的IOT账号", null);
                        }
                        if (WaterIotToken == null) {
                            UserLoraBo userLoraBo = new UserLoraBo();
                            BeanUtils.copyProperties(ul, userLoraBo);
                            IotMsgEntityVo authResult = iHttpTransfer.getLoginInfo(userLoraBo);
                            if (authResult != null && AppCode.IotSucceccCode.equals(authResult.getCode())) {
                                Map map = (Map) JSON.parse(authResult.getData().toString());
                                WaterIotToken = (String) (map.get("token"));
                                svc.set(CacheKey.WaterIotToken + ul.getEnterpriseid(), WaterIotToken, 1800L);
                            } else {
                                return new AppHandheldVo(AppCode.SERVICE_ERROR, "获取IOT Token错误", null);
                            }
                        }
                        if (d != null) {
                            DeviceBo ddo = new DeviceBo();
                            BeanUtils.copyProperties(d, ddo);
                            UserLoraBo userLoraBo = new UserLoraBo();
                            BeanUtils.copyProperties(ul, userLoraBo);
                            IotMsgEntityVo iotMsgEntity = iHttpTransfer.cleanDevice(WaterIotToken, ddo, userLoraBo);
                            if (iotMsgEntity != null && AppCode.IotSucceccCode.equals(iotMsgEntity.getCode())) {
                                logger.info(d.getName() + "从IOT平台删除");
                            } else {
                                if (iotMsgEntity != null && AppCode.IotDeviceNotExixt.equals(iotMsgEntity.getCode())) {
                                    logger.info(d.getName() + "从IOT平台删除:" + AppCode.IotDeviceNotExixt);
                                } else {
                                    if (iotMsgEntity != null && iotMsgEntity.getCode() != null) {
                                        return new AppHandheldVo(AppCode.SERVICE_ERROR,
                                                d.getName() + "设备不能从IOT平台删除:" + iotMsgEntity.getCode(), null);
                                    }

                                }
                            }
                        }
                    } else {
                        // 解绑时若EnterpriseId不同,在旧账号IOT解绑
                        String tokenOld = svc.get(CacheKey.WaterIotToken + d.getEnterpriseid());
                        UserLoraVo ulOld = iUserLoraService.findByEnterpriseId(user.getEnterpriseid());
                        UserLoraBo userLoraBo = new UserLoraBo();
                        BeanUtils.copyProperties(ulOld, userLoraBo);
                        if (tokenOld == null) {
                            IotMsgEntityVo authResult = iHttpTransfer.getLoginInfo(userLoraBo);
                            if (authResult != null && AppCode.IotSucceccCode.equals(authResult.getCode())) {
                                Map map = (Map) JSON.parse(authResult.getData().toString());
                                tokenOld = (String) (map.get("token"));
                                svc.set(CacheKey.WaterIotToken + d.getEnterpriseid(), tokenOld, 1800L);
                            } else {
                                return new AppHandheldVo(AppCode.SERVICE_ERROR, "获取旧IOT Token错误", null);
                            }
                        }
                        // 先在IOT解绑
                        DeviceBo ddo = new DeviceBo();
                        BeanUtils.copyProperties(d, ddo);
                        IotMsgEntityVo iotMsgEntity = iHttpTransfer.cleanDevice(tokenOld, ddo, userLoraBo);
                        if (iotMsgEntity != null && AppCode.IotSucceccCode.equals(iotMsgEntity.getCode())) {
                            logger.info("[解绑]:" + d.getName() + "从IOT平台删除");
                        } else {
                            return new AppHandheldVo(AppCode.SERVICE_ERROR,
                                    d.getName() + "设备不能从IOT平台删除[解绑]:" + iotMsgEntity.getCode(), null);
                        }
                    }
                    iDeviceService.deleteById(d.getDevid());
                    return new AppHandheldVo(AppCode.SUCCESS, "设备解绑成功!", null);
                }
            }
        } catch (Exception e) {
            logger.info("exception :" + e.getMessage());
            logger.error("unbind error", e);
            return new AppHandheldVo(AppCode.SERVICE_ERROR, e.getMessage(), null);
        }
    }

}
