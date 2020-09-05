package com.dotop.pipe.auth.api.service.auth;

import com.dotop.pipe.auth.core.vo.auth.LoginCas;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;

import java.util.Date;
import java.util.List;

// 权限
public interface IAuthService {

    // 登录鉴权
    public LoginCas authentication(String cas) throws FrameworkRuntimeException;

    // 用户信息
//    public LoginCas get(String userId) throws FrameworkRuntimeException;

    // 用户信息
    public LoginCas get(String userId, String ticket) throws FrameworkRuntimeException;

    // 授权资源
    public boolean authorization(LoginCas loginCas, String mid) throws FrameworkRuntimeException;

    // 刷新缓存
    public void refresh(LoginCas loginCas) throws FrameworkRuntimeException;

    // 登出
    public void loginOut(LoginCas loginCas) throws FrameworkRuntimeException;

    // 生成cas
    public String cas(String userId, String ticket, String modelId) throws FrameworkRuntimeException;

    // 修改企业默认地图
    public int editMap(String mapType, List<String> protocols, String operEid, String userBy, Date curr) throws FrameworkRuntimeException;

}
