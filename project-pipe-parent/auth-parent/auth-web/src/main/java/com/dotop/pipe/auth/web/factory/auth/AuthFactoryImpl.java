package com.dotop.pipe.auth.web.factory.auth;

import com.dotop.pipe.auth.api.service.auth.IAuthService;
import com.dotop.pipe.auth.api.service.enterprise.IEnterpriseService;
import com.dotop.pipe.auth.cas.web.IAuthCasWeb;
import com.dotop.pipe.auth.core.vo.auth.LoginCas;
import com.dotop.pipe.auth.core.vo.enterprise.EnterpriseVo;
import com.dotop.pipe.auth.web.api.factory.auth.IAuthFactory;
import com.dotop.pipe.auth.web.api.factory.enterprise.IEnterpriseFactory;
import com.dotop.pipe.auth.core.constants.CasConstants;
import com.dotop.pipe.core.exception.PipeExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class AuthFactoryImpl implements IAuthFactory {

    @Autowired
    private IAuthService iAuthService;

    @Autowired
    private IAuthCasWeb iAuthCasApi;

    @Autowired
    private IEnterpriseFactory iEnterpriseFactory;

    @Autowired
    private IEnterpriseService iEnterpriseService;

    @Override
    public LoginCas authentication(String cas) throws FrameworkRuntimeException {
        // 校验cas登录
        LoginCas loginCas = iAuthService.authentication(cas);
        // dotop水务平台
        if (CasConstants.isAdmin(loginCas)) {
            loginCas.setEnterpriseId(null);
            loginCas.setOperEid(null);
        } else {
            // 初始化企业
            iEnterpriseFactory.init(loginCas);
            // 转换eid
            String eid = loginCas.getEid();
            EnterpriseVo enterprise = iEnterpriseService.getDb(eid);
            String enterpriseId = enterprise.getEnterpriseId();
            loginCas.setEnterpriseId(enterpriseId);
            loginCas.setOperEid(enterpriseId);
            loginCas.setMapType(enterprise.getMapType());
            loginCas.setProtocols(enterprise.getProtocols());
        }
        // 刷新缓存
        iAuthService.refresh(loginCas);
        return loginCas;
    }

    @Override
    public void loginOut() throws FrameworkRuntimeException {
        LoginCas loginCas = iAuthCasApi.get();
        iAuthService.loginOut(loginCas);
    }

    @Override
    public String cas(String modelId) throws FrameworkRuntimeException {
        LoginCas loginCas = iAuthCasApi.get();
        String userId = loginCas.getUserId();
        String ticket = loginCas.getTicket();
        String cas = iAuthService.cas(userId, ticket, modelId);
        return cas;
    }

    @Override
    public String editMap(String mapType, List<String> protocols) throws FrameworkRuntimeException {
        LoginCas loginCas = iAuthCasApi.get();
        String operEid = loginCas.getOperEid();
        String userBy = loginCas.getUserName();
        Date curr = new Date();
        int count = iAuthService.editMap(mapType, protocols, operEid, userBy, curr);
        if (count != 1) {
            throw new FrameworkRuntimeException("修改企业底图出错", PipeExceptionConstants.getMessage("修改企业底图出错"));
        }
        return null;
    }
}
