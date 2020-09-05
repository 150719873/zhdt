package com.dotop.smartwater.project.third.module.api.factory;

import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.auth.vo.UserVo;
import com.dotop.smartwater.project.third.module.core.third.standard.form.AuthForm;
import com.dotop.smartwater.project.third.module.core.third.standard.form.DataForm;
import com.dotop.smartwater.project.third.module.core.third.standard.vo.UplinkVo;

/**
 *
 */
public interface IStandardDeviceUplinkFactory extends IWaterObtainFactory<DataForm, UplinkVo> {

    @Override
    Pagination<UplinkVo> pageDeviceUplink(DataForm dataForm) throws FrameworkRuntimeException;


    UserVo  cacheLoginInfo(AuthForm authForm) throws FrameworkRuntimeException;

    UserVo isLogin(String ticket) throws FrameworkRuntimeException;
}
