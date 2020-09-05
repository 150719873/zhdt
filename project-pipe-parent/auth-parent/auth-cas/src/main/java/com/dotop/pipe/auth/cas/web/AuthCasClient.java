package com.dotop.pipe.auth.cas.web;

import com.dotop.pipe.auth.core.local.Local;
import com.dotop.pipe.auth.core.vo.auth.TopInfoCas;
import com.dotop.pipe.auth.core.constants.CasConstants;
import com.dotop.pipe.auth.core.exception.AuthExceptionConstants;
import com.dotop.pipe.auth.core.vo.auth.LoginCas;
import com.dotop.pipe.auth.core.vo.auth.PermissionCas;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.project.module.core.auth.bo.UserLoginBo;
import com.dotop.smartwater.project.module.core.auth.constants.AuthResultCode;
import com.dotop.smartwater.project.module.core.auth.vo.*;
import com.dotop.water.tool.exception.BusinessException;
import com.dotop.water.tool.service.BaseInf;
import com.dotop.water.tool.util.DataUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.util.*;

/**
 *
 */
public final class AuthCasClient {

    private static Logger logger = LogManager.getLogger(AuthCasClient.class);

    public static LoginCas authentication(String cas) throws FrameworkRuntimeException {

        // 解密
        UserLoginVo userLogin = DataUtil.getUserLogin(cas);
        if (userLogin == null) {
            logger.error(LogMsg.to("ex", AuthExceptionConstants.ACCOUNT_AUTH_FAIL, "cas", cas));
            throw new FrameworkRuntimeException(AuthExceptionConstants.ACCOUNT_AUTH_FAIL,
                    AuthExceptionConstants.getMessage(AuthExceptionConstants.ACCOUNT_AUTH_FAIL));
        }
        String userId = userLogin.getUserid();
        String ticket = userLogin.getTicket();
        String modelId = userLogin.getModelid();
        UserInfoVo userInfo = null;
        try {
            // 鉴权
            userInfo = BaseInf.getUserInfo(userId, ticket, modelId);
        } catch (BusinessException e) {
            logger.error(
                    LogMsg.to("ex", AuthExceptionConstants.ACCOUNT_AUTH_FAIL, "cas", cas, "casCode", e.getErrorCode()));
            throw new FrameworkRuntimeException(AuthExceptionConstants.ACCOUNT_AUTH_FAIL,
                    AuthExceptionConstants.getMessage(AuthExceptionConstants.ACCOUNT_AUTH_FAIL));
        }
        UserParamVo user = userInfo.getUserParamVo();
        Map<String, MenuVo> permissions = userInfo.getPermission();
        EnterpriseVo enterprise = userInfo.getEnterprise();
        List<MenuVo> topMenuList = userInfo.getTopMenuList();
        String alias = userInfo.getAlias();
        String logoUrl = userInfo.getLogoUrl();
        if (user == null || permissions == null || enterprise == null || topMenuList == null) {
            logger.error(LogMsg.to("ex", AuthExceptionConstants.ACCOUNT_NO_AUTH, "userId", userId, "ticket", ticket,
                    "modelId", modelId));
            throw new FrameworkRuntimeException(AuthExceptionConstants.ACCOUNT_NO_AUTH,
                    AuthExceptionConstants.getMessage(AuthExceptionConstants.ACCOUNT_NO_AUTH));
        }

        // 组装用户
        LoginCas loginCas = new LoginCas();
        loginCas.setAlias(alias);
        loginCas.setLogoUrl(logoUrl);
        loginCas.setUserId(String.valueOf(userId));
        loginCas.setTicket(ticket);
        // 不简单设计
        // loginCas.setEnterpriseId(String.valueOf(user.getEnterpriseid()));
        loginCas.setEid(String.valueOf(user.getEnterpriseid()));
        loginCas.setType(CasConstants.casType((user.getType())));
        loginCas.setEnterpriseName(enterprise.getName());
        loginCas.setUserName(user.getName());
        loginCas.setAccount(user.getAccount());

        // 组装权限
        List<PermissionCas> list = new ArrayList<PermissionCas>();
        PermissionCas permissionCas;
        Iterator<String> iterator = permissions.keySet().iterator();
        while (iterator.hasNext()) {
            String mid = iterator.next();
            MenuVo menu = permissions.get(mid);
            permissionCas = new PermissionCas();
            permissionCas.setMenuId(String.valueOf(menu.getMenuid()));
            permissionCas.setName(menu.getName());
            permissionCas.setModelId(String.valueOf(menu.getModelid()));
            permissionCas.setParentId(String.valueOf(menu.getParentid()));
            list.add(permissionCas);
        }
        loginCas.setPermissions(list);

        // 组装企业
        String cent = enterprise.getCenter();
        String exte = enterprise.getExtent();
        String license = enterprise.getLicense();
        if (StringUtils.isNotBlank(cent) && StringUtils.isNotBlank(exte)) {
            // dotop水务平台必须有原始坐标
            String[] cents = cent.split(",");
            if (cents.length == 2) {
                BigDecimal longitude = new BigDecimal(cents[0].trim());
                BigDecimal latitude = new BigDecimal(cents[1].trim());
                loginCas.setCenter(new BigDecimal[]{longitude, latitude});
            }
            String[] extes = exte.split(",");
            if (extes.length == 4) {
                BigDecimal minLongitude = new BigDecimal(extes[0].trim());
                BigDecimal minLatitude = new BigDecimal(extes[1].trim());
                BigDecimal maxLongitude = new BigDecimal(extes[2].trim());
                BigDecimal maxLatitude = new BigDecimal(extes[3].trim());
                loginCas.setExtent(new BigDecimal[]{minLongitude, minLatitude, maxLongitude, maxLatitude});
            }
            // BigDecimal[] center = { new BigDecimal("114.0800"), new BigDecimal("22.5431")
            // };
            // loginCas.setCenter(center);
            // BigDecimal[] extent = { new BigDecimal("114.06"), new BigDecimal("22.5"), new
            // BigDecimal("114.10"),
            // new BigDecimal("22.6") };
            // loginCas.setExtent(extent);
        }
        loginCas.setLicense(license);

        // 组装头部
        List<TopInfoCas> topInfos = new ArrayList<TopInfoCas>();
        TopInfoCas topInfoCas;
        for (MenuVo m : topMenuList) {
            topInfoCas = new TopInfoCas();
            topInfoCas.setName(m.getName());
            topInfoCas.setUri(m.getUri());
            topInfoCas.setIconuri(m.getIconuri());
            topInfoCas.setIntroduction(m.getIntroduction());
            topInfoCas.setLevel(m.getLevel());
            topInfoCas.setType(m.getType());
            topInfoCas.setIsselect(m.getIsselect());
            topInfoCas.setModelid(m.getModelid());
            topInfoCas.setSystype(m.getSystype());
            topInfos.add(topInfoCas);
        }
        loginCas.setTopInfos(topInfos);
        logger.debug(LogMsg.to("topInfos", topInfos));
        return loginCas;
    }

    public static boolean authorization(LoginCas loginCas, String mid) throws FrameworkRuntimeException {
        String userId = loginCas.getUserId();
        String ticket = loginCas.getTicket();
        try {
            Integer webIsPass = BaseInf.webIsPass(userId, ticket, mid);
            if (webIsPass != null && webIsPass == 1) {
                return true;
            }
            return false;
        } catch (BusinessException e) {
            if (AuthResultCode.TimeOut == e.getErrorCode()) {
                logger.error(LogMsg.to("ex", AuthExceptionConstants.ACCOUNT_TICKET_TIMEOUT, "loginCas", loginCas, "mid",
                        mid, "casCode", e.getErrorCode()));
                throw new FrameworkRuntimeException(AuthExceptionConstants.ACCOUNT_TICKET_TIMEOUT,
                        AuthExceptionConstants.getMessage(AuthExceptionConstants.ACCOUNT_TICKET_TIMEOUT));
            } else {
                logger.error(LogMsg.to("ex", AuthExceptionConstants.ACCOUNT_NO_AUTH, "loginCas", loginCas, "mid", mid,
                        "casCode", e.getErrorCode()));
                throw new FrameworkRuntimeException(AuthExceptionConstants.ACCOUNT_NO_AUTH,
                        AuthExceptionConstants.getMessage(AuthExceptionConstants.ACCOUNT_NO_AUTH));
            }
        }
    }

    public static LoginCas get() throws FrameworkRuntimeException {
        LoginCas loginCas = Local.getLoginCas();
        if (loginCas == null) {
            logger.error(LogMsg.to("ex", AuthExceptionConstants.ACCOUNT_OFFLINE));
            throw new FrameworkRuntimeException(AuthExceptionConstants.ACCOUNT_OFFLINE,
                    AuthExceptionConstants.getMessage(AuthExceptionConstants.ACCOUNT_OFFLINE));
        }
        return loginCas;
    }

    public static LoginCas get(boolean flag) throws FrameworkRuntimeException {
        if (flag) {
            return get();
        }
        return Local.getLoginCas();
    }

    public static void loginOut(LoginCas loginCas) throws FrameworkRuntimeException {
        try {
            String userId = loginCas.getUserId();
            String ticket = loginCas.getTicket();
            BaseInf.webLogout(userId, ticket);
        } catch (BusinessException e) {
            logger.error(LogMsg.to("ex", AuthExceptionConstants.ACCOUNT_AUTH_FAIL, "loginCas", loginCas, "casCode",
                    e.getErrorCode()));
        }
    }

    public static String cas(String userId, String ticket, String modelId) throws FrameworkRuntimeException {
        UserLoginBo userLogin = new UserLoginBo();
        userLogin.setUserid(userId);
        userLogin.setTicket(ticket);
        userLogin.setModelid(modelId);
        String cas = DataUtil.generateCasKey(userLogin);
        return cas;
    }

    public final static Date getCurr() throws FrameworkRuntimeException {
        return Local.getCurr();
    }
}
