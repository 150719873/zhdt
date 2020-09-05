package com.dotop.smartwater.project.third.module.factory;

import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.BeanUtils;
import com.dotop.smartwater.dependence.core.utils.DateUtils;
import com.dotop.smartwater.project.module.core.auth.vo.UserVo;
import com.dotop.smartwater.project.third.module.api.factory.IMeterDockingFactory;
import com.dotop.smartwater.project.third.module.api.factory.IStandardDeviceUplinkFactory;
import com.dotop.smartwater.project.third.module.api.service.IMeterDockingService;
import com.dotop.smartwater.project.third.module.api.service.IStandardDeviceUplinkService;
import com.dotop.smartwater.project.third.module.api.service.IWaterDeviceUplinkService;
import com.dotop.smartwater.project.third.module.core.constants.DockingConstants;
import com.dotop.smartwater.project.third.module.core.third.standard.form.AuthForm;
import com.dotop.smartwater.project.third.module.core.third.standard.form.DataForm;
import com.dotop.smartwater.project.third.module.core.third.standard.vo.UplinkVo;
import com.dotop.smartwater.project.third.module.core.water.bo.DeviceUplinkBo;
import com.dotop.smartwater.project.third.module.core.water.bo.DockingBo;
import com.dotop.smartwater.project.third.module.core.water.vo.DeviceUplinkVo;
import com.dotop.smartwater.project.third.module.core.water.vo.DockingVo;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
@Component
public class StandardDeviceUplinkFactoryImpl implements IStandardDeviceUplinkFactory {

    private final static Logger LOGGER = LogManager.getLogger(StandardDeviceUplinkFactoryImpl.class);
//    public static final String CACHE_KEY = "standard:login:";

    @Autowired
    private IMeterDockingFactory iMeterDockingFactory;

    @Autowired
    private IWaterDeviceUplinkService iWaterDeviceUplinkService;

    @Autowired
    private IStandardDeviceUplinkService iStandardDeviceUplinkService;

    @Autowired
    private IMeterDockingService iMeterDockingService;


    @Override
    public Pagination<UplinkVo> pageDeviceUplink(DataForm dataForm) throws FrameworkRuntimeException {
        try {
            // TODO 根据月份或水表号查询当前时间的最新读数
            DeviceUplinkBo deviceUplinkBo = new DeviceUplinkBo();
            // TODO 登录缓存获取
            deviceUplinkBo.setEnterpriseid(dataForm.getEnterpriseid());
            if (dataForm.getDevno() != null) {
                deviceUplinkBo.setDevno(dataForm.getDevno());
            }
            deviceUplinkBo.setYearMonth(dataForm.getMonth());
            Pagination<DeviceUplinkVo> ownerVoPagination = iStandardDeviceUplinkService.pageUplink(deviceUplinkBo, dataForm.getPage(), dataForm.getPageCount());
            List<DeviceUplinkVo> deviceUplinks = ownerVoPagination.getData();
            long totalPageSize = ownerVoPagination.getTotalPageSize();
            List<UplinkVo> uplinks = new ArrayList<>();
            deviceUplinks.forEach(deviceUplink -> {
                UplinkVo uplink = new UplinkVo();
                uplink.setUplinkTime(DateUtils.formatDatetime(deviceUplink.getUplinkDate()));
                uplink.setWater(deviceUplink.getWater() == null ? "0" : String.valueOf(deviceUplink.getWater()));
                uplink.setDevno(deviceUplink.getDevno());
                // 电磁流量计
                uplink.setFactory(deviceUplink.getFactory());
                uplink.setFlowRate(deviceUplink.getFlowRate());
                uplink.setPressure(deviceUplink.getPressure());
                uplink.setTotalWorkTime(deviceUplink.getTotalWorkTime());
                uplink.setTemperature(deviceUplink.getTemperature());
                uplink.setInternalClock(deviceUplink.getInternalClock());
                String anhydrousAbnormal=deviceUplink.getAnhydrousAbnormal();
                String disconnectionAbnormal = deviceUplink.getDisconnectionAbnormal();
                if(StringUtils.isNotEmpty(anhydrousAbnormal)||StringUtils.isNotEmpty(disconnectionAbnormal)){
                    uplink.setStatus("正常");
                    if("1".equals(anhydrousAbnormal)){
                        uplink.setStatus("无水");
                    }else if("1".equals(disconnectionAbnormal)){
                        uplink.setStatus("断线");
                    }
                }
                uplinks.add(uplink);
            });
            Pagination<UplinkVo> pagination = new Pagination<>(dataForm.getPage(), dataForm.getPageCount(), uplinks, totalPageSize);
            return pagination;
        } catch (Exception e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.REQUEST_ERROR);
        }
    }

    @Override
    public UserVo cacheLoginInfo(AuthForm authForm) throws FrameworkRuntimeException {

        try {
            DockingBo dockingBo = new DockingBo();
            dockingBo.setUsername(authForm.getUsername());
            dockingBo.setCode(authForm.getCode());
            dockingBo.setPassword(authForm.getPassword());
            dockingBo.setType(DockingConstants.REMOTE_API);
            DockingVo dockingVo = iMeterDockingService.get(dockingBo);
            if (dockingVo != null) {
                DockingBo copy = BeanUtils.copy(dockingVo, DockingBo.class);
                return iStandardDeviceUplinkService.cacheLoginInfo(copy);
            } else {
                return null;
            }

        } catch (Exception e) {
            LOGGER.error(LogMsg.to(e));
            return null;
        }

    }

    @Override
    public UserVo isLogin(String ticket) throws FrameworkRuntimeException {
        UserVo login = iStandardDeviceUplinkService.isLogin(ticket);
        if (login != null) {
            return login;
        } else {
            throw new FrameworkRuntimeException(BaseExceptionConstants.REQUEST_ERROR, "登录异常");
        }
    }


}
