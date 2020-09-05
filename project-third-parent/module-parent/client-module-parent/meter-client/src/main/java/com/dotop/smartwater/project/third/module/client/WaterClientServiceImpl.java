package com.dotop.smartwater.project.third.module.client;

import com.alibaba.fastjson.TypeReference;
import com.dotop.smartwater.project.third.module.client.fegin.ICasFeginClient;
import com.dotop.smartwater.project.third.module.client.fegin.IWaterFeginClient;
import com.dotop.smartwater.project.third.module.core.water.bo.*;
import com.dotop.smartwater.dependence.cache.StringValueCache;
import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.utils.JSONObjects;
import com.dotop.smartwater.dependence.core.utils.JSONUtils;
import com.dotop.smartwater.project.module.core.auth.form.UserForm;
import com.dotop.smartwater.project.module.core.auth.vo.UserVo;
import com.dotop.smartwater.project.module.core.third.form.middleware.WaterDownLoadForm;
import com.dotop.smartwater.project.module.core.water.constants.DeviceCode;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.module.core.water.form.DeviceForm;
import com.dotop.smartwater.project.module.core.water.form.DeviceUplinkForm;
import com.dotop.smartwater.project.module.core.water.form.OwnerChangeForm;
import com.dotop.smartwater.project.module.core.water.form.OwnerForm;
import com.dotop.smartwater.project.third.module.core.water.bo.*;
import com.dotop.smartwater.project.third.module.core.water.vo.CommandVo;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 */
@Component
public class WaterClientServiceImpl implements IWaterClientService {

    private final static Logger LOGGER = LogManager.getLogger(WaterClientServiceImpl.class);

    private static final Integer PAGESIZE = 100;

    private static final long DEFAULT_CACHE_TIMEOUT = 1800L;

    public static final String CACHE_KEY = "meter:water:login:";

    @Autowired
    private StringValueCache svc;

    @Autowired
    private IWaterFeginClient iWaterFeginClient;

    @Autowired
    private ICasFeginClient iCasFeginClient;

    private String getData(String result) throws FrameworkRuntimeException {
        JSONObjects jsonObjects = JSONUtils.parseObject(result);
        String code = jsonObjects.getString("code");
        if (ResultCode.Success.equals(code)) {
            return jsonObjects.getString("data");
        }
        String msg = jsonObjects.getString("msg");
        throw new FrameworkRuntimeException(BaseExceptionConstants.REQUEST_ERROR, msg);
    }

    private UserVo login(DockingBo dockingBo) throws FrameworkRuntimeException {
        String enterpriseid = dockingBo.getEnterpriseid();
        String waterHost = dockingBo.getWaterHost();
        String waterUsername = dockingBo.getWaterUsername();
        String waterPassword = dockingBo.getWaterPassword();
        if (StringUtils.isBlank(enterpriseid) || StringUtils.isBlank(waterHost) || StringUtils.isBlank(waterUsername) || StringUtils.isBlank(waterPassword)) {
            throw new FrameworkRuntimeException(BaseExceptionConstants.REQUEST_ERROR);
        }
        return login(enterpriseid, waterHost, waterUsername, waterPassword);
    }

    private UserVo login(String enterpriseid, String website, String account, String password) throws FrameworkRuntimeException {
        String str = svc.get(CACHE_KEY + enterpriseid + ":" + account);
        if (StringUtils.isNotBlank(str)) {
            return JSONUtils.parseObject(str, UserVo.class);
        }
        UserForm userForm = new UserForm();
        userForm.setWebsite(website);
        userForm.setAccount(account);
        userForm.setPassword(password);
        String result = iCasFeginClient.login(userForm);
        // 结果校验
        String data = getData(result);
        JSONObjects map = JSONUtils.parseObject(data);
        String userid = map.getString("userid");
        String ticket = map.getString("ticket");
        // 组装缓存参数
        UserVo userVo = new UserVo();
        userVo.setUserid(userid);
        userVo.setTicket(ticket);
        userVo.setAccount(account);
        svc.set(CACHE_KEY + enterpriseid + ":" + account, JSONUtils.toJSONString(userVo), DEFAULT_CACHE_TIMEOUT);
        return userVo;
    }

    @Override
    public String ownerAdds(List<OwnerBo> owners, DockingBo dockingBo) throws FrameworkRuntimeException {
        if (owners == null || owners.isEmpty() || dockingBo == null) {
            LOGGER.error("请求账号密码有误");
            return null;
        }
        // 登录
        UserVo login = login(dockingBo);
        // 组装参数
        List<OwnerForm> listForm = new ArrayList<>();
        OwnerForm ownerForm;
        for (OwnerBo ownerBo : owners) {
            ownerForm = new OwnerForm();
            // 必填
            ownerForm.setOwnerid(ownerBo.getOwnerid());
            ownerForm.setUsername(ownerBo.getUsername());
            ownerForm.setUserno(ownerBo.getUserno());
            // 非必填
            ownerForm.setUseraddr(ownerBo.getUseraddr());
            ownerForm.setUserphone(ownerBo.getUserphone());
            ownerForm.setCardid(ownerBo.getCardid());
            ownerForm.setEnterpriseid(dockingBo.getEnterpriseid());
            listForm.add(ownerForm);
        }
        String result = iWaterFeginClient.ownerAdds(listForm, login.getTicket(), login.getUserid());
        //   结果校验
        return getData(result);
    }

    @Override
    public String ownerEdits(List<OwnerBo> owners, DockingBo dockingBo) throws FrameworkRuntimeException {
        if (owners == null || owners.isEmpty() || dockingBo == null) {
            LOGGER.error("请求账号密码有误");
            return null;
        }
        // 登录
        UserVo login = login(dockingBo);
        // 组装参数
        List<OwnerForm> listForm = new ArrayList<>();
        OwnerForm ownerForm;
        for (OwnerBo ownerBo : owners) {
            ownerForm = new OwnerForm();
            // 必填
            ownerForm.setOwnerid(ownerBo.getOwnerid());
            // 非必填
            ownerForm.setUsername(ownerBo.getUsername());
            ownerForm.setUserno(ownerBo.getUserno());
            ownerForm.setUseraddr(ownerBo.getUseraddr());
            ownerForm.setUserphone(ownerBo.getUserphone());
            ownerForm.setCardid(ownerBo.getCardid());
            ownerForm.setEnterpriseid(dockingBo.getEnterpriseid());
            listForm.add(ownerForm);
        }
        String result = iWaterFeginClient.ownerEdits(listForm, login.getTicket(), login.getUserid());
        //   结果校验
        return getData(result);
    }

    @Override
    public String ownerChanges(List<OwnerChangeBo> ownerChanges, DockingBo dockingBo) throws FrameworkRuntimeException {
        if (ownerChanges == null || ownerChanges.isEmpty() || dockingBo == null) {
            LOGGER.error("请求账号密码有误");
            return null;
        }
        // 登录
        UserVo login = login(dockingBo);
        List<OwnerChangeForm> listForm = new ArrayList<>();
        OwnerChangeForm ownerChangeForm;
        for (OwnerChangeBo ownerChange : ownerChanges) {
            ownerChangeForm = new OwnerChangeForm();
            ownerChangeForm.setOwnerid(ownerChange.getOwnerid());
            ownerChangeForm.setDevid(ownerChange.getDevid());
            ownerChangeForm.setOldDevid(ownerChange.getOldDevid());
            ownerChangeForm.setEnterpriseid(dockingBo.getEnterpriseid());
            listForm.add(ownerChangeForm);
        }
        String result = iWaterFeginClient.ownerChanges(listForm, login.getTicket(), login.getUserid());
        //   结果校验
        return getData(result);
    }

    @Override
    public String deviceAdds(List<DeviceBo> devices, DockingBo dockingBo) throws FrameworkRuntimeException {
        if (devices == null || devices.isEmpty() || dockingBo == null) {
            LOGGER.error("请求账号密码有误");
            throw new FrameworkRuntimeException(BaseExceptionConstants.REQUEST_ERROR);
        }
        // 登录
        UserVo login = login(dockingBo);
        // 组装参数
        List<DeviceForm> listForm = new ArrayList<>();
        DeviceForm deviceForm;
        for (DeviceBo devicebo : devices) {
            deviceForm = new DeviceForm();
            // 必填
            deviceForm.setDevno(devicebo.getDevno());
            deviceForm.setDevid(devicebo.getDevid());
            deviceForm.setDeveui(devicebo.getDeveui());

            deviceForm.setTypeid(DeviceCode.DEVICE_TYPE_ELECTRONIC);
            deviceForm.setMode(dockingBo.getMode());
            deviceForm.setKind(DeviceCode.DEVICE_KIND_REAL);
            deviceForm.setFactory(dockingBo.getFactory());
            deviceForm.setProductName(dockingBo.getProductName());

            deviceForm.setBeginvalue(devicebo.getBeginvalue());
            deviceForm.setWater(devicebo.getWater());

            // 非必填
            deviceForm.setImsi(devicebo.getImsi());
            deviceForm.setDevaddr(devicebo.getDevaddr());
            deviceForm.setCaliber(devicebo.getCaliber());

            deviceForm.setTapstatus(devicebo.getTapstatus());
            deviceForm.setTaptype(devicebo.getTaptype());
            // TODO 安装时间
            // deviceForm.setCtime(devicebo.getCtime());
            deviceForm.setEnterpriseid(dockingBo.getEnterpriseid());
            listForm.add(deviceForm);
        }
        String result = iWaterFeginClient.deviceAdds(listForm, login.getTicket(), login.getUserid());
        //   结果校验
        return getData(result);
    }

    @Override
    public String deviceEdits(List<DeviceBo> devices, DockingBo dockingBo) throws FrameworkRuntimeException {
        if (devices == null || devices.isEmpty() || dockingBo == null) {
            LOGGER.error("请求账号密码有误");
            throw new FrameworkRuntimeException(BaseExceptionConstants.REQUEST_ERROR);
        }
        // 登录
        UserVo login = login(dockingBo);
        // 组装参数
        List<DeviceForm> listForm = new ArrayList<>();
        DeviceForm deviceForm;
        for (DeviceBo devicebo : devices) {
            deviceForm = new DeviceForm();
            // 必填
            deviceForm.setDevid(devicebo.getDevid());
            // 非必填
            deviceForm.setDevno(devicebo.getDevno());
//            deviceForm.setDeveui(devicebo.getDeveui());
            deviceForm.setImsi(devicebo.getImsi());
            deviceForm.setDevaddr(devicebo.getDevaddr());
            deviceForm.setCaliber(devicebo.getCaliber());
            deviceForm.setTapstatus(devicebo.getTapstatus());
            deviceForm.setTaptype(devicebo.getTaptype());
            deviceForm.setTapstatus(devicebo.getTapstatus());
            deviceForm.setTaptype(devicebo.getTaptype());
            deviceForm.setImsi(devicebo.getImsi());
            deviceForm.setEnterpriseid(dockingBo.getEnterpriseid());
            listForm.add(deviceForm);
        }
        // 循环
        List<DeviceForm> subList;
        int total = listForm.size();
        int cycleTotal = total % PAGESIZE == 0 ? (total / PAGESIZE) : (total / PAGESIZE) + 1;
        for (int i = 0; i < cycleTotal; i++) {
            // 循环
            if ((i + 1) * PAGESIZE > total) {
                subList = listForm.subList(i * PAGESIZE, total);
            } else {
                subList = listForm.subList(i * PAGESIZE, (i + 1) * PAGESIZE);
            }
            LOGGER.info(LogMsg.to("method", "cyclelTotal", "cycleTotal", cycleTotal, "from", (i * PAGESIZE), "to", (i + 1) * PAGESIZE, "subList", subList.size()));
            String result = iWaterFeginClient.deviceEdits(subList, login.getTicket(), login.getUserid());
            LOGGER.info(LogMsg.to("method", "uplinks", "cycleTotal", cycleTotal, "from", (i * PAGESIZE), "to", (i + 1) * PAGESIZE, "subList", subList.size(), "result", result));
            //   结果校验
            getData(result);
        }
        return null;
    }

    @Override
    public String uplinks(List<DeviceUplinkBo> waters, DockingBo dockingBo) throws FrameworkRuntimeException {
        if (waters == null || waters.isEmpty() || dockingBo == null) {
            LOGGER.error("请求账号密码有误");
            throw new FrameworkRuntimeException(BaseExceptionConstants.REQUEST_ERROR);
        }
        // 登录
        UserVo login = login(dockingBo);
        // 组装水务平台参数
        List<DeviceUplinkForm> subList;
        DeviceUplinkForm du;
        List<DeviceUplinkForm> ldu = new ArrayList<>();
        for (DeviceUplinkBo water : waters) {
            du = new DeviceUplinkForm();
            du.setDevid(water.getDevid());
            du.setDeveui(water.getDeveui());
            du.setWater(String.valueOf(water.getWater()));
            du.setTapstatus(water.getTapstatus());
            du.setIccid(water.getIccid());
            du.setUrl(water.getUrl());
            du.setRxtime(water.getUplinkDate());
            if (StringUtils.isNotBlank(water.getUplinkData())) {
                du.setUplinkData(water.getUplinkData());
            }
            if (StringUtils.isNotBlank(water.getRssi())) {
                du.setRssi(water.getRssi());
            }
            if (StringUtils.isNotBlank(water.getLsnr())) {
                du.setLsnr(Double.valueOf(water.getLsnr()));
            }
            du.setEnterpriseid(dockingBo.getEnterpriseid());
            ldu.add(du);
            if (StringUtils.isNotBlank(water.getUrl())) {
                LOGGER.info(LogMsg.to("method", "cyclelTotal", "du", du));
            }
            if ("350310000000747".equals(water.getDeveui())) {
                LOGGER.info(LogMsg.to("method", "cyclelTotal", "du", du));
            }
        }
        // 循环
        int total = ldu.size();
        int cycleTotal = total % PAGESIZE == 0 ? (total / PAGESIZE) : (total / PAGESIZE) + 1;
        for (int i = 0; i < cycleTotal; i++) {
            // 循环
            if ((i + 1) * PAGESIZE > total) {
                subList = ldu.subList(i * PAGESIZE, total);
            } else {
                subList = ldu.subList(i * PAGESIZE, (i + 1) * PAGESIZE);
            }
            LOGGER.info(LogMsg.to("method", "cyclelTotal", "cycleTotal", cycleTotal, "from", (i * PAGESIZE), "to", (i + 1) * PAGESIZE, "subList", subList.size()));
            String result = iWaterFeginClient.uplinks(subList, login.getTicket(), login.getUserid());
            LOGGER.info(LogMsg.to("method", "uplinks", "cycleTotal", cycleTotal, "from", (i * PAGESIZE), "to", (i + 1) * PAGESIZE, "subList", subList.size(), "result", result));
            //   结果校验
            getData(result);
        }
        return null;
    }

    @Override
    public CommandVo downlink(CommandBo command, String ticket, DockingBo dockingBo) throws FrameworkRuntimeException {
        if (command == null || dockingBo == null) {
            LOGGER.error("请求账号密码有误");
            throw new FrameworkRuntimeException(BaseExceptionConstants.REQUEST_ERROR);
        }
        // 登录
        UserVo login = login(dockingBo);
        WaterDownLoadForm dd = new WaterDownLoadForm();
        dd.setDevid(command.getDevid());
        dd.setCommand(command.getCommand());
        dd.setCycle(command.getCycle());
        dd.setWater(String.valueOf(command.getWater()));
        String result = iWaterFeginClient.downlink(dd, login.getTicket(), login.getUserid());
        LOGGER.info(LogMsg.to("下发命令结果", result));
        //   结果校验
        String data = getData(result);
        return JSONUtils.parseObject(data, CommandVo.class);
    }

    @Override
    public String downlinkEdit(CommandBo command, String ticket, DockingBo dockingBo) throws FrameworkRuntimeException {
        if (command == null || dockingBo == null) {
            LOGGER.error("请求账号密码有误");
            throw new FrameworkRuntimeException(BaseExceptionConstants.REQUEST_ERROR);
        }
        // 登录
        UserVo login = login(dockingBo);

        CommandBo dd = new CommandBo();
        dd.setDevid(command.getDevid());
        dd.setClientid(command.getClientid());
        dd.setResult(command.getResult());
//      thirdid
//      result
        String result = iWaterFeginClient.downlinkEdit(dd, login.getTicket(), login.getUserid());
        Map<String, String> resultMap = JSONUtils.parseObject(result, new TypeReference<Map<String, String>>() {
        });
        //   结果校验
        return resultMap.get("result");
    }
}
