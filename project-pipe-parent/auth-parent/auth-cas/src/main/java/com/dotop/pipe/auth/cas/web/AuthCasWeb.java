package com.dotop.pipe.auth.cas.web;

import com.dotop.pipe.auth.core.vo.auth.LoginCas;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class AuthCasWeb implements IAuthCasWeb {

    private Logger logger = LogManager.getLogger(AuthCasWeb.class);

    @Override
    public LoginCas authentication(String cas) throws FrameworkRuntimeException {
        return AuthCasClient.authentication(cas);
    }

    @Override
    public boolean authorization(LoginCas loginCas, String mid) throws FrameworkRuntimeException {
        return AuthCasClient.authorization(loginCas, mid);
    }

    @Override
    public LoginCas get() throws FrameworkRuntimeException {
        return AuthCasClient.get();
    }

    @Override
    public LoginCas get(boolean flag) throws FrameworkRuntimeException {
        return AuthCasClient.get(flag);
    }

    @Override
    public void loginOut(LoginCas loginCas) throws FrameworkRuntimeException {
        AuthCasClient.loginOut(loginCas);
    }

    @Override
    public String cas(String userId, String ticket, String modelId) throws FrameworkRuntimeException {
        return AuthCasClient.cas(userId,ticket,modelId);
    }
}
