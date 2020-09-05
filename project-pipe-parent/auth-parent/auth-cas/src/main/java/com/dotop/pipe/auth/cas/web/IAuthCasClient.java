package com.dotop.pipe.auth.cas.web;

import com.dotop.pipe.auth.core.vo.auth.LoginCas;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;

import java.util.Date;

/**
 *
 */
public interface IAuthCasClient {

    default LoginCas getLoginCas() throws FrameworkRuntimeException {
        return AuthCasClient.get();
    }

    default String getUserId() throws FrameworkRuntimeException {
        return getLoginCas().getUserId();
    }

    default String getEnterpriseId() throws FrameworkRuntimeException {
        return getLoginCas().getEnterpriseId();
    }

    default String getUserBy() throws FrameworkRuntimeException {
        return getLoginCas().getUserName();
    }

    default Date getCurr() throws FrameworkRuntimeException {
        return AuthCasClient.getCurr();
    }

}
