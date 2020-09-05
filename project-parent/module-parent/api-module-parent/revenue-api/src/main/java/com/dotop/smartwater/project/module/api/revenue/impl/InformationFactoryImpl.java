package com.dotop.smartwater.project.module.api.revenue.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.dotop.smartwater.project.module.api.revenue.IInformationFactory;
import com.dotop.smartwater.project.module.core.water.constants.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.dotop.smartwater.dependence.cache.api.AbstractValueCache;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.client.third.http.IHttpTransfer;
import com.dotop.smartwater.project.module.core.auth.bo.UserLoraBo;
import com.dotop.smartwater.project.module.core.auth.local.AuthCasClient;
import com.dotop.smartwater.project.module.core.auth.vo.UserLoraVo;
import com.dotop.smartwater.project.module.core.auth.vo.UserVo;
import com.dotop.smartwater.project.module.core.water.bo.DeviceBo;
import com.dotop.smartwater.project.module.core.water.bo.customize.QueryParamBo;
import com.dotop.smartwater.project.module.core.water.form.DeviceForm;
import com.dotop.smartwater.project.module.core.water.vo.DeviceVo;
import com.dotop.smartwater.project.module.core.water.vo.OwnerVo;
import com.dotop.smartwater.project.module.core.water.vo.customize.IotMsgEntityVo;
import com.dotop.smartwater.project.module.core.water.vo.customize.OriginalVo;
import com.dotop.smartwater.project.module.service.device.IDeviceService;
import com.dotop.smartwater.project.module.service.device.IDeviceUplinkService;
import com.dotop.smartwater.project.module.service.revenue.IOwnerService;
import com.dotop.smartwater.project.module.service.tool.IUserLoraService;

/**
 * 设备修改
 *

 * @date 2019年2月26日
 */
@Component
public class InformationFactoryImpl implements IInformationFactory {

    private static final Logger logger = LoggerFactory.getLogger(InformationFactoryImpl.class);

    @Value("${param.revenue.excelTempUrl}")
    private String excelTempUrl;

    @Autowired
    private IDeviceService iDeviceService;

    @Autowired
    private IDeviceUplinkService iDeviceUplinkService;

    @Autowired
    private IOwnerService iOwnerService;

    @Autowired
    protected AbstractValueCache<String> avc;

    @Autowired
    private IHttpTransfer iHttpTransfer;

    @Autowired
    private IUserLoraService iUserLoraService;

    //TODO 2.5.3 集中器版本
	/*@Autowired
	private IConcentratorFeginClient iConcentratorFeginClient;*/

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
    public DeviceVo edit(DeviceForm deviceForm) throws FrameworkRuntimeException {

        UserVo user = AuthCasClient.getUser();
        if (user == null) {
            throw new FrameworkRuntimeException(ResultCode.Fail, "无用户信息");
        }
        DeviceVo deviceVo = iDeviceService.findByDevEUI(deviceForm.getDeveui());

        if (deviceVo != null) {

            DeviceVo d = iDeviceService.findByDevNo(deviceForm.getDevno());
            if (d != null && !deviceVo.getDeveui().equals(d.getDeveui())) {
                throw new FrameworkRuntimeException(ResultCode.Fail, "该水表号已经被使用");
            }

            //判断上级水表号
            if (StringUtils.isNotBlank(deviceForm.getParentDevNo())) {
                if (deviceForm.getParentDevNo().equals(deviceForm.getDevno())) {
                    throw new FrameworkRuntimeException(ResultCode.Fail, "上级水表号不能为自身编号");
                }

                DeviceVo dvo = iDeviceService.findByDevNo(deviceForm.getParentDevNo());
                if (dvo == null) {
                    throw new FrameworkRuntimeException(ResultCode.Fail, "上级水表号不存在");
                }
                deviceVo.setPid(dvo.getDevid());
            }


            deviceVo.setDevno(deviceForm.getDevno());
            deviceVo.setDevaddr(deviceForm.getDevaddr());
            deviceVo.setTypeid(deviceForm.getTypeid());
            deviceVo.setName(deviceForm.getName());

            DeviceBo deviceBo = new DeviceBo();
            BeanUtils.copyProperties(deviceVo, deviceBo);
            iDeviceService.update(deviceBo);

        } else {
            throw new FrameworkRuntimeException(ResultCode.NOTFINDWATER, "水表编号为" + deviceForm.getDevno() + "不存在!");
        }
        return deviceVo;
    }

    /**
     * 删除设备信息
     */
    @Override
    public String del(DeviceForm deviceForm) throws FrameworkRuntimeException {
        UserVo user = AuthCasClient.getUser();
        if (user == null) {
            throw new FrameworkRuntimeException(ResultCode.TimeOut, "登录超时,请重新登录");
        }
        DeviceVo deviceVo = iDeviceService.findByDevEUI(deviceForm.getDeveui());

        if (deviceVo != null) {

            // 查询是否有下级水表,有则先删除下级水表 KJR
            List<DeviceVo> list = iDeviceService.findChildrenById(deviceVo.getDevid(), user.getEnterpriseid());
            if (list != null && list.size() > 0) {
                throw new FrameworkRuntimeException(ResultCode.Fail, "请先删除下级水表");
            }

            OwnerVo ownerVo = iOwnerService.findByDevId(deviceVo.getDevid());
            // 如果设备没有绑定业主或者业主已销户或未开户，则可以删除水表信息
            if (ownerVo == null || ownerVo.getStatus().equals(WaterConstants.OWNER_STATUS_UNOPRN)
                    || ownerVo.getStatus().equals(WaterConstants.OWNER_STATUS_DELETE)) {
                String mode = String.valueOf(WaterConstants.DEVICE_MODE_MBUS);
                // 远传表才要解除与IOT的绑定
                if (deviceVo.getMode() != null && (deviceVo.getMode().equals("28,300001,1") || deviceVo.getMode().equals("28,300001,2")
               		 || deviceVo.getMode().equals("28,300001,3") || deviceVo.getMode().equals("28,300001,10"))) {
                    String token = avc.get(CacheKey.WaterIotToken + user.getEnterpriseid());
                    UserLoraVo ul = iUserLoraService.findByEnterpriseId(user.getEnterpriseid());
                    UserLoraBo userLoraBo = new UserLoraBo();
                    BeanUtils.copyProperties(ul, userLoraBo);

                    if (ul == null) {
                        throw new FrameworkRuntimeException(ResultCode.Fail, "没在系统设置水司关联的IOT账号");
                    }
                    if (token == null) {
                        IotMsgEntityVo authResult = iHttpTransfer.getLoginInfo(userLoraBo);
                        if (authResult != null && AppCode.IotSucceccCode.equals(authResult.getCode())) {
                            Map map = (Map) JSON.parse(authResult.getData().toString());
                            token = (String) (map.get("token"));
                            avc.set(CacheKey.WaterIotToken + ul.getEnterpriseid(), token);
                        } else {
                            throw new FrameworkRuntimeException(ResultCode.Fail, "获取IOT Token错误");
                        }
                    }

                    // 参数转换
                    DeviceBo deviceBo = new DeviceBo();
                    BeanUtils.copyProperties(deviceVo, deviceBo);
                    IotMsgEntityVo iotMsgEntity = null;
                    
                    // Lora表在IOT平台智能调用删除接口，NB表调解绑接口
                    if (deviceVo.getMode().equals("28,300001,1")) {
                    	iotMsgEntity = iHttpTransfer.cleanDevice(token, deviceBo, userLoraBo);	
                    } else {
                    	iotMsgEntity = iHttpTransfer.delDevice(token, deviceBo, userLoraBo);
                    }
                    
                    if (iotMsgEntity != null && AppCode.IotSucceccCode.equals(iotMsgEntity.getCode())) {
                        logger.info(deviceVo.getDevno() + "从IOT平台删除");
                    } else {
                        if (iotMsgEntity != null && AppCode.IotDeviceNotExixt.equals(iotMsgEntity.getCode())) {
                            logger.info(deviceVo.getDevno() + "从IOT平台删除:" + IotCode.getMessage(AppCode.IotDeviceNotExixt));
                        } else {
                            throw new FrameworkRuntimeException(ResultCode.Fail,
                                    deviceVo.getDevno() + "设备不能从IOT平台删除:" + IotCode.getMessage(iotMsgEntity.getCode()));
                        }
                    }
                }

                iDeviceService.deleteById(deviceVo.getDevid());
            } else {
                throw new FrameworkRuntimeException(ResultCode.ParamIllegal, "水表使用中不能删除!");
            }
        } else {
            throw new FrameworkRuntimeException(ResultCode.NOTFINDWATER, "水表编号为" + deviceForm.getDevno() + "不存在!");
        }
        return deviceVo.getDevid();
    }

    @Override
    public List<DeviceVo> getUplinkData(DeviceForm deviceForm) throws FrameworkRuntimeException {
        SimpleDateFormat sf = new SimpleDateFormat("yyyyMM");
        List<DeviceVo> list = iDeviceUplinkService.getUplinkData(deviceForm.getDeveui(), sf.format(new Date()));
        return list;
    }

    @Override
    public Pagination<DeviceVo> queryDevice(DeviceForm deviceForm) throws FrameworkRuntimeException {
        UserVo user = AuthCasClient.getUser();
        if (user == null) {
            throw new FrameworkRuntimeException(ResultCode.Fail, "用户没有登陆");
        }
        if (deviceForm.getCommunityid() == null) {
            //不是最高级管理员,获取分配的区域
            /*if(WaterConstants.USER_TYPE_ENTERPRISE_NORMAL == user.getType()){
                List<AreaNodeVo> list = BaseInf.getAreaList(user.getUserid(), user.getTicket());
                if(list.size() == 0){
                    throw new FrameworkRuntimeException(ResultCode.Fail, "用户角色没分配区域");
                }else{
                    List<String> areaIds = new ArrayList<>();
                    for(AreaNodeVo area : list){
                        areaIds.add(area.getKey());
                    }
                    deviceForm.setNodeIds(areaIds);
                }
            }else{
                deviceForm.setNodeIds(null);
            }*/
        	deviceForm.setNodeIds(null);
        }
        deviceForm.setCommunityid(null);

        DeviceBo deviceBo = new DeviceBo();
        BeanUtils.copyProperties(deviceForm, deviceBo);
        deviceBo.setEnterpriseid(user.getEnterpriseid());
        // 只显示总表
        deviceBo.setPid(DeviceCode.DEVICE_PARENT);
        Pagination<DeviceVo> pagination = iDeviceService.findBypage(deviceBo);
        List<DeviceVo> list = pagination.getData();
        if (list != null && list.size() != 0) {
            recursion(list, user.getEnterpriseid());
        }
        return pagination;
    }
    
    
    public Pagination<DeviceVo> getMonitor(DeviceForm deviceForm) {
    	UserVo user = AuthCasClient.getUser();
        if (user == null) {
            throw new FrameworkRuntimeException(ResultCode.Fail, "用户没有登陆");
        }
        // 当企业ID等于CommunityId,相当于查询整个水司下的设备
        if (user.getEnterpriseid().equals(deviceForm.getCommunityid())) {
            deviceForm.setNodeIds(null);
        }
        deviceForm.setCommunityid(null);

        DeviceBo deviceBo = new DeviceBo();
        BeanUtils.copyProperties(deviceForm, deviceBo);
        deviceBo.setEnterpriseid(user.getEnterpriseid());
        // 只显示总表
        deviceBo.setPid(DeviceCode.DEVICE_PARENT);
        Pagination<DeviceVo> pagination = iDeviceService.findBypage(deviceBo);
        List<DeviceVo> list = pagination.getData();
        if (list != null && list.size() != 0) {
            recursion(list, user.getEnterpriseid());
        }

        if (pagination.getData() != null && pagination.getData().size() > 0) {
        	// 获取结果中一个月内上报数据情况
            QueryParamBo param = new QueryParamBo();
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat sMonth = new SimpleDateFormat("yyyyMM");
            SimpleDateFormat monthDay = new SimpleDateFormat("MMdd");
            Date date = new Date();
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            c.add(Calendar.DAY_OF_MONTH, 1);
            param.setEnd(sf.format(c.getTime()));
            param.setEndMonth(sMonth.format(date));
            param.setSystime(sMonth.format(date));
            int endMonth = date.getMonth();

            Date date1 = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date1);
            calendar.add(Calendar.MONTH, -1);	//减去一个月的时间
            param.setStart(sf.format(calendar.getTime()));
            param.setStartMonth(sMonth.format(calendar.getTime()));
            int startMonth = calendar.getTime().getMonth();
            
            String deveuis = "";
            for (DeviceVo device : pagination.getData()) {
            	if (deveuis.equals("")) deveuis = "'" + device.getDeveui() + "'"; else deveuis += ",'" + device.getDeveui() + "'";
            }
            param.setDeveuis(deveuis);
            
            List<OriginalVo> originals = new ArrayList<OriginalVo>();
            if (startMonth == endMonth) {
            	originals = iDeviceUplinkService.findUplinkData(param);	
            } else {
            	originals = iDeviceUplinkService.findCrossMonthUplinkData(param);
            }
            
            if (originals.size() > 0) {
            	for (DeviceVo device : list) {
            		List<OriginalVo> uplinks = new ArrayList<OriginalVo>(); 
            		for (OriginalVo original : originals) {
            			if (device.getDeveui() != null && original.getDeveui() != null 
            					&& device.getDeveui().toUpperCase().equals(original.getDeveui().toUpperCase())) {
            				try {
								original.setMonthDay(monthDay.format(sf.parse(original.getRxtime())));
							} catch (ParseException e) {
								e.printStackTrace();
							}
            				uplinks.add(original);
            			}
            		}
            		device.setOriginals(uplinks);
            	}
            }
        }
        
        return pagination;
    }
    
    

    /**
     * 递归
     *
     * @param list
     * @param enterpriseId
     */
    private void recursion(List<DeviceVo> list, String enterpriseId) {
        for (DeviceVo deviceVo : list) {
            DeviceBo dBo = new DeviceBo();
            dBo.setPid(deviceVo.getDevid());
            dBo.setEnterpriseid(enterpriseId);
            deviceVo.setModeCode(ModeConstants.ModeMap.get(DictionaryCode.getChildValue(deviceVo.getMode())));
            List<DeviceVo> li = iDeviceService.list(dBo);
            if (li != null && li.size() != 0) {
                deviceVo.setChildren(li);
                recursion(li, enterpriseId);
            }
        }
    }

    /**
     * 获取时间间隔中的每一天
     */
	@Override
	public List<String> getDate(DeviceForm deviceForm) {
		SimpleDateFormat sf = new SimpleDateFormat("MMdd");
		List<String> dates = new ArrayList<String>();
		
		Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, -1);	//减去一个月的时间
        dates.add(sf.format(calendar.getTime()));
        
        Calendar calEnd = Calendar.getInstance();
        calEnd.setTime(date);
        
        while (date.after(calendar.getTime()))  {
        	calendar.add(Calendar.DAY_OF_MONTH, 1);
        	dates.add(sf.format(calendar.getTime()));
        }
		return dates;
	}
}
