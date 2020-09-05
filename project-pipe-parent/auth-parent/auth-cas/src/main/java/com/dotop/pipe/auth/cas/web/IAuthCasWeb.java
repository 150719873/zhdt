package com.dotop.pipe.auth.cas.web;

import com.dotop.pipe.auth.core.vo.auth.LoginCas;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;

// 权限
public interface IAuthCasWeb {

    // cas认证中心认证
    public LoginCas authentication(String cas) throws FrameworkRuntimeException;

    // 授权验证
    public boolean authorization(LoginCas loginCas, String mid) throws FrameworkRuntimeException;

    // // cas认证中心认证
    // public LoginCas authentication(String userId, String ticket) throws
    // FrameworkRuntimeException;
    //
    // // cas认证中心认证
    // public LoginCas authentication(String userId, String ticket, String mid)
    // throws FrameworkRuntimeException;

    // 获取本地登录信息
    public LoginCas get() throws FrameworkRuntimeException;

    // 获取本地登录信息
    public LoginCas get(boolean flag) throws FrameworkRuntimeException;

    // // 获取权限资源
    // public List<MenuCas> getPermissions(String userId, String ticket, String
    // modelId) throws FrameworkRuntimeException;
    //
    // // 获取dotop水务权限资源
    // public List<MenuCas> getPlatformPermissions(String userId, String ticket)
    // throws FrameworkRuntimeException;
    //
    // // 获取dotop水司权限资源
    // public List<MenuCas> getPermissions(String userId, String ticket) throws
    // FrameworkRuntimeException;

    // 登出
    public void loginOut(LoginCas loginCas) throws FrameworkRuntimeException;

    // 生成cas
    public String cas(String userId, String ticket, String modelId) throws FrameworkRuntimeException;

}
