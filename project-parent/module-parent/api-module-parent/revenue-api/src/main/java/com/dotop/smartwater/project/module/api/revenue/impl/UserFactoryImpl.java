package com.dotop.smartwater.project.module.api.revenue.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dotop.smartwater.project.module.api.revenue.IUserFactory;
import com.dotop.smartwater.project.module.core.water.constants.WaterConstants;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dotop.smartwater.dependence.cache.api.AbstractValueCache;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.utils.JSONObjects;
import com.dotop.smartwater.dependence.core.utils.JSONUtils;
import com.dotop.smartwater.project.module.core.auth.bo.UserLoginBo;
import com.dotop.smartwater.project.module.core.auth.config.WaterClientConfig;
import com.dotop.smartwater.project.module.core.auth.constants.AuthResultCode;
import com.dotop.smartwater.project.module.core.auth.form.UserForm;
import com.dotop.smartwater.project.module.core.auth.form.UserLoginForm;
import com.dotop.smartwater.project.module.core.auth.local.AuthCasClient;
import com.dotop.smartwater.project.module.core.auth.utils.CipherEncryptors;
import com.dotop.smartwater.project.module.core.auth.vo.EnterpriseVo;
import com.dotop.smartwater.project.module.core.auth.vo.SettlementVo;
import com.dotop.smartwater.project.module.core.auth.vo.UserInfoVo;
import com.dotop.smartwater.project.module.core.auth.vo.UserLoginVo;
import com.dotop.smartwater.project.module.core.auth.vo.UserVo;
import com.dotop.smartwater.project.module.core.third.form.iot.UserEntryForm;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.module.service.device.IDeviceWarningService;
import com.dotop.smartwater.project.module.service.tool.ISettlementService;
import com.dotop.water.tool.exception.BusinessException;
import com.dotop.water.tool.service.AppInf;
import com.dotop.water.tool.service.BaseInf;
import com.dotop.water.tool.util.DataUtil;
import com.dotop.water.tool.util.HttpUtil;

/**
 * 用户控制
 *

 * @date 2019年2月27日
 */
@Component
public class UserFactoryImpl implements IUserFactory {

    private static final Logger logger = LoggerFactory.getLogger(UserFactoryImpl.class);

    @Autowired
    protected AbstractValueCache<String> avc;

    @Autowired
    protected AbstractValueCache<UserVo> avcH;

    @Autowired
    private IDeviceWarningService iDeviceWarningService;

    @Autowired
    private ISettlementService iSettlementService;

    /**
     * 注销用户
     */
    @Override
    public void logout(String userid, String ticket) throws FrameworkRuntimeException {
        if(userid == null || ticket == null){
            return;
        }

        /*
         * 业务逻辑 1 cas认证中心推出登录 2 本地缓存删除记录
         */
        try {
            BaseInf.webLogout(userid, ticket);
            avcH.del(userid);
        } catch (FrameworkRuntimeException ex) {
            if (AuthResultCode.UserNotLogin == ex.getCode()) {
            }
            {
                return;
            }
        }

    }

    /**
     * 用户登录
     */
    @Override
    public String login(UserEntryForm userEntryForm) throws FrameworkRuntimeException {
        String casKey = null;
        try {
            casKey = HttpUtil.post(WaterClientConfig.WaterCasUrl + "/auth/water/test/water_login", null,
                    JSONUtils.toJSONBytes(userEntryForm));
            JSONObjects parseObject = JSONUtils.parseObject(casKey);
            return parseObject.getString("data");

        } catch (IOException e) {
            e.printStackTrace();
            throw new FrameworkRuntimeException(ResultCode.Fail, "第三方接口登录异常");
        }
    }

    /**
     * 认证中心过来的登录跳转
     */
    @Override
    public Map<String, Object> childrenLogin(Map<String, String> map) throws FrameworkRuntimeException {
        try {
            String data = map.get(WaterClientConfig.CasKey);
            if (StringUtils.isBlank(data)) {
                throw new FrameworkRuntimeException(ResultCode.Fail, "缺少Key!");
            }
            if (data == null){
                return null;
            }
            String dataString = CipherEncryptors.decrypt(WaterClientConfig.WaterCasKey1, WaterClientConfig.WaterCasKey2,
                    data);
            UserLoginVo noLoginUser = JSONUtils.parseObject(dataString, UserLoginVo.class);
            if (noLoginUser == null) {
                throw new FrameworkRuntimeException(ResultCode.Fail, "验证Key失败!");
            }

            UserInfoVo userInfoVo = BaseInf.getUserInfo(noLoginUser.getUserid(), noLoginUser.getTicket(),
                    noLoginUser.getModelid());

            String ticket = noLoginUser.getTicket();
            // RedisUtil.setUserToken(noLoginUser.getUserid(), ticket);
            // 登录信息放入缓存
            avc.set(noLoginUser.getUserid(), JSONUtils.toJSONString(ticket));

            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("userid", noLoginUser.getUserid());
            resultMap.put("roleid", userInfoVo.getUserParamVo().getRoleid());
            resultMap.put("ticket", ticket);
            resultMap.put("username", userInfoVo.getUserParamVo().getAccount());
            resultMap.put("name", userInfoVo.getUserParamVo().getName());
            resultMap.put("invalidtime", System.currentTimeMillis() + WaterConstants.LOGIN_TIME);
            resultMap.put("menuid", noLoginUser.getModelid());
            EnterpriseVo enterprise = userInfoVo.getEnterprise();
            if (enterprise != null) {
                resultMap.put("companyName", enterprise.getName());
                resultMap.put("enterpriseid", enterprise.getEnterpriseid());
                resultMap.put("userManual", enterprise.getUserManual());
                resultMap.put("workApp", enterprise.getWorkApp());
                resultMap.put("bindApp", enterprise.getBindApp());
            }

            SettlementVo settlement = iSettlementService.getSettlement(enterprise.getEnterpriseid());
            if (settlement != null) {
                if (StringUtils.isNotBlank(settlement.getAlias())) {
                    resultMap.put("title", settlement.getAlias());
                }


                if (settlement.getDefaultDay() != null) {
            		resultMap.put("defaultDay", settlement.getDefaultDay());
            	}
                
                if (settlement.getIsHelp() != null) {
                    resultMap.put("isHelp", settlement.getIsHelp());
                } else {
                    resultMap.put("isHelp", "1");
                }
            } else {
                resultMap.put("title", "");
                resultMap.put("isHelp", "1");//是否显示帮助文档
            }
			
			/*if (StringUtils.isNotBlank(userInfoVo.getAlias())) {
				resultMap.put("title", userInfoVo.getAlias());
			}*/

            // TODO 两个Vo 使用哪个 吗 smartWater中的Vo缺少字段 EnterpriseVo;
            UserVo user = new UserVo();
            EnterpriseVo e = userInfoVo.getEnterprise();
            BeanUtils.copyProperties(userInfoVo.getUserParamVo(), user);
            // TODO 原系统把企业相关的信息 也放到缓存中
            user.setEnterprise(e);
            avcH.set(user.getUserid(), user);
            resultMap.put("logoUrl", userInfoVo.getLogoUrl());
            resultMap.put("topMenuList", userInfoVo.getTopMenuList());

            // TODO long to string
            resultMap.put("warningCount", iDeviceWarningService.getDeviceWarningCount(user.getEnterpriseid()));

            if (userInfoVo.getPermission() != null && userInfoVo.getPermission().size() > 0) {
                List<String> permission = new ArrayList<>();
                for (String key : userInfoVo.getPermission().keySet()) {
                    permission.add(key);
                }
                resultMap.put("permission", permission);
            }

            resultMap.put("businessPermission", userInfoVo.getBusinessPermission());
            return resultMap;
        } catch (BusinessException ex) {
            logger.error("children_login", ex.getErrorCode());
            throw new FrameworkRuntimeException("children_login", ex.getMessage());
        }
    }

    /**
     * 获取CasKey
     */
    @Override
    public String getCasKey(UserLoginForm userLogin) throws FrameworkRuntimeException {
        UserInfoVo userVo = BaseInf.getUserInfo(userLogin.getUserid(), userLogin.getTicket(),
                userLogin.getModelid());

        if (userVo == null) {
            throw new FrameworkRuntimeException(ResultCode.AccountInvalid, "验证失败,请重新登录");
        }

        //回调CAS
        if ("0".equals(userLogin.getModelid())) {
            UserLoginBo userLoginBo = new UserLoginBo();
            BeanUtils.copyProperties(userLogin, userLoginBo);
            return DataUtil.generateCasKey(userLoginBo);
        }

        if (userVo.getPermission() != null && userVo.getPermission().size() > 0
                && userVo.getPermission().get(userLogin.getModelid()) != null) {

            UserLoginBo userLoginBo = new UserLoginBo();
            BeanUtils.copyProperties(userLogin, userLoginBo);
            return DataUtil.generateCasKey(userLoginBo);
        } else {
            throw new FrameworkRuntimeException(ResultCode.AccountInvalid, "用户没有权限访问");
        }
    }

    /**
     * 检验用户是否失效
     */
    @Override
    public String checkUserInvalid(UserForm user) throws FrameworkRuntimeException {
        if (user.getUserid() != null && !("0".equals(user.getUserid())) && !StringUtils.isBlank(user.getTicket())) {
            UserVo userData = AuthCasClient.getUser();
            if (userData == null) {
                throw new FrameworkRuntimeException(ResultCode.AccountInvalid, "用户已失效");
            }
        } else {
            throw new FrameworkRuntimeException(ResultCode.AccountInvalid, "用户已失效");
        }
        return null;
    }

    /**
     * 获取所有企业
     */
    @Override
    public List<EnterpriseVo> loginEnterprise() throws FrameworkRuntimeException {
        List<EnterpriseVo> list = AppInf.getAppEnterprises();
        return list;
    }

}
