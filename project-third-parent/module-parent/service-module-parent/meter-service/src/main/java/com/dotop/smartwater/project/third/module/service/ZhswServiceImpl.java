package com.dotop.smartwater.project.third.module.service;

import com.alibaba.fastjson.JSON;
import com.dotop.smartwater.dependence.cache.StringValueCache;
import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.utils.JSONUtils;
import com.dotop.smartwater.project.module.core.auth.vo.UserVo;
import com.dotop.smartwater.project.third.module.api.service.IZhswService;
import com.dotop.smartwater.project.third.module.core.third.zhsw.form.ClientForm;
import com.dotop.smartwater.project.third.module.core.third.zhsw.form.MeterRecordForm;
import com.dotop.smartwater.project.third.module.core.utils.httpclient.HttpClientUtils;
import com.dotop.smartwater.project.third.module.core.water.form.DockingForm;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

@Component
public class ZhswServiceImpl implements IZhswService {

    @Autowired
    private StringValueCache svc;

    private final static Logger LOGGER = LogManager.getLogger(ZhswServiceImpl.class);
    private static final long DEFAULT_CACHE_TIMEOUT = 6000L;
    public static final String CACHE_KEY = "zhsw:login:";

    @Override
    public  void addData(DockingForm dockingForm, DockingForm loginDockingForm, List<ClientForm> list) {
        String url = dockingForm.getHost() + dockingForm.getUrl();
        for (ClientForm clientForm : list) {
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("wcode", clientForm.getWcode()));
            modifyData(url, params, loginDockingForm);
        }

    }

    @Override
    public void updateData(DockingForm dockingForm, DockingForm loginDockingForm, List<MeterRecordForm> list) throws FrameworkRuntimeException {
        String url = dockingForm.getHost() + dockingForm.getUrl();
        for (MeterRecordForm meterRecordForm : list) {
            try {
                List<NameValuePair> params = new ArrayList<>();
                params.add(new BasicNameValuePair("wcode", meterRecordForm.getWcode()));
                params.add(new BasicNameValuePair("current_time", String.valueOf(meterRecordForm.getCurrent_time())));
                params.add(new BasicNameValuePair("current_num", String.valueOf(meterRecordForm.getCurrent_num())));
                modifyData(url, params, loginDockingForm);
            } catch (Exception e) {
                LOGGER.error(LogMsg.to(e));
            }
        }

    }


    private UserVo login(DockingForm loginDockingForm) {
        String loginUrl = loginDockingForm.getHost() + loginDockingForm.getUrl();
        String username = loginDockingForm.getUsername();
        String password = loginDockingForm.getPassword();

        String loginKey = CACHE_KEY + username + loginDockingForm.getEnterpriseid();
        String str = svc.get(loginKey);
        if (StringUtils.isNoneBlank(str)) {
            return JSONUtils.parseObject(str, UserVo.class);
        }

        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("app_id", username));
        params.add(new BasicNameValuePair("app_secret", password));
        String result = HttpClientUtils.post(loginUrl, params);
        String token = JSON.parseObject(JSON.parseObject(result).getString("data")).getString("access_token");

        UserVo userVo = new UserVo();
        userVo.setAccount(username);
        userVo.setTicket(token);
        userVo.setEnterpriseid(loginDockingForm.getEnterpriseid());
        svc.set(loginKey, JSON.toJSONString(userVo), DEFAULT_CACHE_TIMEOUT);
        return userVo;
    }

    private  void modifyData(String url, List<NameValuePair> params, DockingForm loginDockingForm) throws FrameworkRuntimeException {
        UserVo userVo = login(loginDockingForm);
        if (userVo.getTicket() != null) {
            params.add(new BasicNameValuePair("access_token", userVo.getTicket()));
            String result = HttpClientUtils.post(url, params);
            String resultCode = JSON.parseObject(result).getString("code");
            if ("0".equals(resultCode)) {
                String value = params.get(0).getValue();
                LOGGER.error(LogMsg.to("全景智慧水务更新失败的水表号：", value));
            }
        } else {
            throw new FrameworkRuntimeException(BaseExceptionConstants.BASE_ERROR,"登录状态异常");
        }


    }
}
