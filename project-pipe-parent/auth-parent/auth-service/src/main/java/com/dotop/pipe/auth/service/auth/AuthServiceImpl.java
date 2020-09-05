package com.dotop.pipe.auth.service.auth;

import com.dotop.pipe.auth.cas.web.IAuthCasWeb;
import com.dotop.pipe.auth.core.vo.auth.LoginCas;
import com.dotop.pipe.auth.api.cache.auth.IAuthCache;
import com.dotop.pipe.auth.api.dao.enterprise.IEnterpriseDao;
import com.dotop.pipe.auth.api.service.auth.IAuthService;
import com.dotop.pipe.auth.core.exception.AuthExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class AuthServiceImpl implements IAuthService {

    private Logger logger = LogManager.getLogger(AuthServiceImpl.class);

    @Autowired
    private IAuthCasWeb iAuthCasWeb;

    @Autowired
    private IAuthCache iAuthCache;

    @Autowired
    private IEnterpriseDao iEnterpriseDao;

    @Override
    public LoginCas authentication(String cas) throws FrameworkRuntimeException {
        // 鉴权校验是否有权限登录管漏系统，鉴权成功后建立token保存关系
        LoginCas loginCas = iAuthCasWeb.authentication(cas);
        return loginCas;
    }

//    @Override
//    public LoginCas get(String userId) throws FrameworkRuntimeException {
//        LoginCas loginCas = iAuthCache.get(userId);
//        if (loginCas == null) {
//            logger.error(LogMsg.to("ex", AuthExceptionConstants.ACCOUNT_OFFLINE, "userId", userId));
//            throw new FrameworkRuntimeException(AuthExceptionConstants.ACCOUNT_OFFLINE,
//                    AuthExceptionConstants.getMessage(AuthExceptionConstants.ACCOUNT_OFFLINE));
//        }
//        return loginCas;
//    }

    @Override
    public LoginCas get(String userId, String ticket) throws FrameworkRuntimeException {
        LoginCas loginCas = iAuthCache.get(userId);
        if (loginCas != null && StringUtils.isNotBlank(loginCas.getTicket()) && loginCas.getTicket().equals(ticket)) {
            return loginCas;
        }
        logger.error(LogMsg.to("ex", AuthExceptionConstants.ACCOUNT_OFFLINE, "userId", userId));
        throw new FrameworkRuntimeException(AuthExceptionConstants.ACCOUNT_OFFLINE,
                AuthExceptionConstants.getMessage(AuthExceptionConstants.ACCOUNT_OFFLINE));
    }

    @Override
    public boolean authorization(LoginCas loginCas, String mid) throws FrameworkRuntimeException {
        boolean auth = iAuthCasWeb.authorization(loginCas, mid);
        return auth;
    }

    @Override
    public void refresh(LoginCas loginCas) throws FrameworkRuntimeException {
        // 缓存
        iAuthCache.set(loginCas);
    }

    @Override
    public void loginOut(LoginCas loginCas) throws FrameworkRuntimeException {
        iAuthCache.del(loginCas);
        iAuthCasWeb.loginOut(loginCas);
    }

    @Override
    public String cas(String userId, String ticket, String modelId) throws FrameworkRuntimeException {
        String cas = iAuthCasWeb.cas(userId, ticket, modelId);
        return cas;
    }

    @Override
    public int editMap(String mapType, List<String> protocols, String operEid, String userBy, Date curr) throws FrameworkRuntimeException {
        try {
            return iEnterpriseDao.editMap(mapType, protocols, operEid, userBy, curr);
        } catch (FrameworkRuntimeException e) {
            logger.error(LogMsg.to(e));
            throw e;
        } catch (DataAccessException e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        } catch (Throwable e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.BASE_ERROR, e.getMessage(), e);
        }
    }

}
