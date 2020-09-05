package com.dotop.smartwater.project.third.module.api.service;

import com.dotop.smartwater.project.third.module.core.water.vo.DeviceUplinkVo;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.auth.vo.UserVo;
import com.dotop.smartwater.project.third.module.core.water.bo.DeviceUplinkBo;
import com.dotop.smartwater.project.third.module.core.water.bo.DockingBo;

/**
 *
 */
public interface IStandardDeviceUplinkService {

    UserVo cacheLoginInfo(DockingBo dockingBo) throws FrameworkRuntimeException;

    Pagination<DeviceUplinkVo> pageUplink(DeviceUplinkBo deviceUplinkBo, Integer page, Integer pageCount) throws FrameworkRuntimeException;

    UserVo isLogin(String ticket) throws FrameworkRuntimeException;
}
