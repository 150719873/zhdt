package com.dotop.smartwater.project.third.module.service;

import com.alibaba.fastjson.JSON;
import com.dotop.smartwater.project.third.module.api.dao.IWaterDeviceUplinkDao;
import com.dotop.smartwater.dependence.cache.StringValueCache;
import com.dotop.smartwater.dependence.core.common.RootModel;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.BeanUtils;
import com.dotop.smartwater.dependence.core.utils.JSONUtils;
import com.dotop.smartwater.dependence.core.utils.UuidUtils;
import com.dotop.smartwater.project.module.core.auth.vo.UserVo;
import com.dotop.smartwater.project.third.module.api.service.IStandardDeviceUplinkService;
import com.dotop.smartwater.project.third.module.core.water.bo.DeviceUplinkBo;
import com.dotop.smartwater.project.third.module.core.water.bo.DockingBo;
import com.dotop.smartwater.project.third.module.core.water.dto.DeviceUplinkDto;
import com.dotop.smartwater.project.third.module.core.water.vo.DeviceUplinkVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 *
 */
@Component
public class StandardDeviceUplinkServiceImpl implements IStandardDeviceUplinkService {


    @Autowired
    private StringValueCache svc;
    @Autowired
    private IWaterDeviceUplinkDao iWaterDeviceUplinkDao;


    private static final long DEFAULT_CACHE_TIMEOUT = 7200L;

    private static final String CACHE_KEY = "standard:login:";


    @Override
    public UserVo cacheLoginInfo(DockingBo dockingBo) throws FrameworkRuntimeException {

        String ticket = UuidUtils.getUuid();
        String loginKey = CACHE_KEY + ticket;

        UserVo userVo = new UserVo();
        userVo.setAccount(dockingBo.getUsername());
        userVo.setTicket(ticket);
        userVo.setPassword(dockingBo.getPassword());
        userVo.setEnterpriseid(dockingBo.getEnterpriseid());
        svc.set(loginKey, JSON.toJSONString(userVo), DEFAULT_CACHE_TIMEOUT);
        return userVo;

    }

    @Override
    public Pagination<DeviceUplinkVo> pageUplink(DeviceUplinkBo deviceUplinkBo, Integer page, Integer pageCount) throws FrameworkRuntimeException {
        DeviceUplinkDto deviceUplinkDto = BeanUtils.copy(deviceUplinkBo, DeviceUplinkDto.class);
        deviceUplinkDto.setIsDel(RootModel.NOT_DEL);
        Page<Object> resultPage = PageHelper.startPage(page, pageCount);
        List<DeviceUplinkVo> deviceUplinkVos = iWaterDeviceUplinkDao.listDegrees(deviceUplinkDto);
        return  new Pagination<>(page, pageCount, deviceUplinkVos, resultPage.getTotal());
    }

    @Override
    public UserVo isLogin(String ticket) throws FrameworkRuntimeException {
        String loginKey = CACHE_KEY + ticket;
        String str = svc.get(loginKey);
        if (StringUtils.isNoneBlank(str)) {
            if (JSONUtils.parseObject(str, UserVo.class).getTicket().equals(ticket)) {
                return JSONUtils.parseObject(str, UserVo.class);
            } else {
                return null;
            }

        } else {
            return null;
        }
    }
}
