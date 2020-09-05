package com.dotop.smartwater.project.module.api.wechat.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dotop.smartwater.dependence.cache.StringValueCache;
import com.dotop.smartwater.dependence.cache.api.AbstractValueCache;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.project.module.api.wechat.IWechatUserFactory;
import com.dotop.smartwater.project.module.client.third.http.emailSms.HttpClientHelper;
import com.dotop.smartwater.project.module.core.auth.wechat.WechatAuthClient;
import com.dotop.smartwater.project.module.core.auth.wechat.WechatUser;
import com.dotop.smartwater.project.module.core.third.constants.CommonConstant;
import com.dotop.smartwater.project.module.core.water.bo.OrderBo;
import com.dotop.smartwater.project.module.core.water.bo.OwnerBo;
import com.dotop.smartwater.project.module.core.water.constants.CacheKey;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.module.core.water.constants.WaterConstants;
import com.dotop.smartwater.project.module.core.water.form.customize.WechatParamForm;
import com.dotop.smartwater.project.module.core.water.vo.DeviceVo;
import com.dotop.smartwater.project.module.core.water.vo.OwnerVo;
import com.dotop.smartwater.project.module.core.water.vo.customize.WechatPayTypeShowVo;
import com.dotop.smartwater.project.module.core.water.vo.customize.WechatPublicSettingVo;
import com.dotop.smartwater.project.module.service.device.IDeviceService;
import com.dotop.smartwater.project.module.service.revenue.IOrderService;
import com.dotop.smartwater.project.module.service.revenue.IOwnerService;
import com.dotop.smartwater.project.module.service.revenue.IPayTypeService;
import com.dotop.smartwater.project.module.service.tool.IWechatPublicSettingService;
import com.dotop.smartwater.project.module.service.wechat.IWechatUserService;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.*;

/**
 * 与用户信息相关
 *

 * @date 2019年3月22日
 */
@Component
public class WechatUserFactoryImpl implements IWechatUserFactory {

    private static final Logger logger = LogManager.getLogger(WechatUserFactoryImpl.class);

    public static final String OPEND_ID = "oG4dAwwugz3ymitrP7mtm-kSKu1M";

    public static final String ENTERPRISE_ID = "44";

    public static final String OWNER_ID = "11174*****";
    public static final String token = "token";

    @Autowired
    private IWechatPublicSettingService iWechatPublicSettingService;

    @Autowired
    private IOwnerService iOwnerService;

    @Autowired
    private IWechatUserService iWechatUserService;

    @Autowired
    private IDeviceService iDeviceService;

    @Autowired
    private IPayTypeService iPayTypeService;

    @Autowired
    private IOrderService iOrderService;

    @Autowired
    protected AbstractValueCache<WechatPublicSettingVo> avc;
    @Autowired
    protected StringValueCache svc;

    @Override
    public Map<String, String> setSession(WechatParamForm wechatParamForm) {
        String wechatSession = UUID.randomUUID().toString();
        String code = wechatParamForm.getCode();
        String ownerid = null;
        Map<String, String> map = new HashMap<String, String>();

        // 测试本地开发特定数值：999999跳过请求微信获取参数,设置默认的开发参数
        if (code.equals("999999")) {
            WechatPublicSettingVo weixinConfig = new WechatPublicSettingVo();
            weixinConfig.setAppid("wx39fbb31d6723056e");
            weixinConfig.setAppsecret("a9397026b7f6279415f24907c965035e");
            weixinConfig.setDomain("http://zhtest.eastcomiot.com");
            weixinConfig.setEnterpriseid("46");
            weixinConfig.setGatewayauthorizecode(
                    "https://open.weixin.qq.com/connect/oauth2/authorize?appid={0}&redirect_uri={1}&response_type=code&scope=snsapi_base&state=jiutong#wechat_redirect");
            weixinConfig.setGatewayopenidbycode(
                    "https://api.weixin.qq.com/sns/oauth2/access_token?appid={0}&secret={1}&code={2}&grant_type=authorization_code");
            weixinConfig.setMchid("1509215371");
            weixinConfig.setOrderqueryurl("https://api.mch.weixin.qq.com/pay/orderquery");
            weixinConfig.setPaysecret("AD4654E53FC4E643E4EEE2750FA8554A");
            weixinConfig.setRequestreturnurl("https://api.weixin.qq.com/cgi-bin/user/info");
            weixinConfig.setUnifiedorderurl("https://api.mch.weixin.qq.com/pay/unifiedorder");
            weixinConfig.setWechatpublicid("1");
            List<OwnerVo> list = iWechatUserService.getOwnerList(OPEND_ID, ENTERPRISE_ID);
            // 查找默认账户 测试数据 不需要抛出异常 需要指定用户id
            if (list == null || list.size() == 0) {
                //  throw new FrameworkRuntimeException(ResultCode.WECHAT_NOT_OWNER_ERROR, "账户不存在 请先绑定账户");
                //  ownerid = OWNER_ID;
            } else {
                for (OwnerVo ownerVo : list) {
                    if (ownerVo.getIsdefault() == WaterConstants.WECHAT_OWNER_IS_DEFAULT) {
                        ownerid = ownerVo.getOwnerid();
                        break;
                    }
                }
                if (ownerid == null) {
                    ownerid = list.get(0).getOwnerid();
                }
            }
            setRedisValue(wechatSession, OPEND_ID, ownerid, ENTERPRISE_ID, weixinConfig);
            map.put(token, wechatSession);
            map.put("ownerid", ownerid);
            return map;
        }

        String enterpriseid = wechatParamForm.getEnterpriseid();

        if (StringUtils.isEmpty(code)) {
            throw new FrameworkRuntimeException(ResultCode.ParamIllegal, "微信code为空");
        }
        if (StringUtils.isNotBlank(code)) {
            Map<String, Object> mapConfig = this.getWeixinOpenId(code, enterpriseid);
            String openId = (String) mapConfig.get(CommonConstant.WEIXIN_OPEN_ID);
            WechatPublicSettingVo weixinConfig =
                    (WechatPublicSettingVo) mapConfig.get(CommonConstant.WECHAT_CONFIG);
            logger.debug("openId: {}", openId);
            if (openId != null) {
                List<OwnerVo> list = iWechatUserService.getOwnerList(openId, enterpriseid);
                if (list != null && list.size() > 0) {
                    for (OwnerVo ownerVo : list) {
                        if (ownerVo.getIsdefault() == WaterConstants.WECHAT_OWNER_IS_DEFAULT) {
                            // 指定默认的用户
                            ownerid = ownerVo.getOwnerid();
                            break;
                        }
                    }
                    if (ownerid == null) { // 如果没有默认用户 则拿其中一个座位默认用户
                        ownerid = list.get(0).getOwnerid();
                    }
                } else {
                    // ownerid = OWNER_ID;
                    // throw new FrameworkRuntimeException(ResultCode.WECHAT_NOT_OWNER_ERROR, "账户不存在 请先绑定账户");
                }
                setRedisValue(wechatSession, openId, ownerid, enterpriseid, weixinConfig);
                map.put(token, wechatSession);
                map.put("ownerid", ownerid);
            }
        }
        return map;
    }

    private void setRedisValue(
            String session,
            String openid,
            String ownerid,
            String enterpriseid,
            WechatPublicSettingVo weixinConfig) {
        if (openid != null) {
            svc.set(CacheKey.WaterWechatOpenid + session, openid);
        }

        if (ownerid != null) {
            svc.set(CacheKey.WaterWechatOwnerid + session, ownerid);
        }

        if (enterpriseid != null) {
            svc.set(CacheKey.WaterWechatEnterpriseid + session, enterpriseid);
        }

        if (weixinConfig != null) {
            avc.set(CacheKey.WaterWechatConfig + session, weixinConfig);
        }
    }

    @Override
    public Map<String, Object> getWeixinOpenId(String code, String enterpriseid) {
        Map<String, Object> map = new HashMap<String, Object>();
        String openId = null;
        WechatPublicSettingVo weixinConfig = null;
        try {
            weixinConfig = iWechatPublicSettingService.getByenterpriseId(enterpriseid);
            if (weixinConfig == null) {
                throw new FrameworkRuntimeException(ResultCode.ParamIllegal, "水司没有配置微信信息");
            }
            String url =
                    MessageFormat.format(
                            weixinConfig.getGatewayopenidbycode(),
                            weixinConfig.getAppid(),
                            weixinConfig.getAppsecret(),
                            code);
            String tokenInfo = HttpClientHelper.sendGetRequest(url);
            JSONObject jsonObject = JSON.parseObject(tokenInfo);
            openId = jsonObject.getString(CommonConstant.WEIXIN_OPEN_ID);
        } catch (Exception e) {
            logger.warn("", e);
        }
        map.put(CommonConstant.WEIXIN_OPEN_ID, openId);
        map.put(CommonConstant.WECHAT_CONFIG, weixinConfig);
        return map;
    }

    @Override
    public WechatPayTypeShowVo showPayType() {
        WechatUser wechatUser = WechatAuthClient.get();
        OwnerBo ownerBo = new OwnerBo();
        if (wechatUser != null) {
            String ownerId = wechatUser.getOwnerid();
            String enterpriseid = wechatUser.getEnterpriseid();
            String openid = wechatUser.getOpenid();

            ownerBo.setOwnerid(ownerId);
            ownerBo.setOpenid(openid);
            ownerBo.setEnterpriseid(enterpriseid);
        } else{
            return null;
        }

        OwnerVo ownerVo = iWechatUserService.getOwner(ownerBo);
        if (ownerVo != null) {
            WechatPayTypeShowVo vo = new WechatPayTypeShowVo();
            vo.setPayTypeVo(iPayTypeService.get(ownerVo.getPaytypeid()));

            OrderBo orderBo = new OrderBo();
            orderBo.setOwnerid(ownerVo.getOwnerid());
            orderBo.setEnterpriseid(ownerVo.getEnterpriseid());
            vo.setOrderVoList(iOrderService.orderListByWeChat(orderBo));

            return vo;
        }
        return null;
    }

    @Override
    public List<OwnerVo> getOwnerList() throws FrameworkRuntimeException {
        WechatUser wechatUser = WechatAuthClient.get();
        String ownerId = wechatUser.getOwnerid();
        String enterpriseid = wechatUser.getEnterpriseid();
        String openid = wechatUser.getOpenid();
        // 获取一个openid 下绑定的所有业主信息 并判断是否和当前的 业主id 相同 相同没有编辑为当前用户
        List<OwnerVo> list = iWechatUserService.getOwnerList(openid, enterpriseid);
        for (OwnerVo ownerVo : list) {
            if (ownerId.equals(ownerVo.getOwnerid())) {
                ownerVo.setCurrentowner(true);
            }
        }
        return list;
    }

    @Override
    public OwnerVo getOwnerInfo() throws FrameworkRuntimeException {
        WechatUser wechatUser = WechatAuthClient.get();
        String ownerId = wechatUser.getOwnerid();
        String enterpriseid = wechatUser.getEnterpriseid();
        String openid = wechatUser.getOpenid();
        OwnerBo ownerBo = new OwnerBo();
        ownerBo.setOwnerid(ownerId);
        ownerBo.setOpenid(openid);
        ownerBo.setEnterpriseid(enterpriseid);
        OwnerVo ownerVo = iWechatUserService.getOwner(ownerBo);

        if (ownerVo != null && ownerVo.getDevid() != null) {
            // 查询水表读数
            DeviceVo deviceVo = iDeviceService.getDevById(ownerVo.getDevid());
            if (deviceVo == null) {
                ownerVo.setWater(null);
            } else {
                ownerVo.setWater(deviceVo.getWater());
            }
        } else {
            ownerVo.setWater(null);
        }
        return ownerVo;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
    public void blindOwner(WechatParamForm wechatParamForm) throws FrameworkRuntimeException {
        WechatUser wechatUser = WechatAuthClient.get();
        String enterpriseid = wechatUser.getEnterpriseid();
        String openid = wechatUser.getOpenid();
        String ownerid = wechatUser.getOwnerid();
        String token = wechatUser.getToken();
        // 正常情况下应该只有一个匹配的
        List<OwnerVo> ownerList =
                iWechatUserService.getOwnerUserByMsg(
                        wechatParamForm.getUsermsg(), wechatParamForm.getUsername(), enterpriseid);
        OwnerVo ownerVo = null;
        if (ownerList == null || ownerList.size() == 0) {
            throw new FrameworkRuntimeException(ResultCode.WECHATBLINDERROR, "用户不存在");
        } else {
            ownerVo = ownerList.get(0);
            // 校验匹配的业主是否正确
            if (ownerVo == null) {
                throw new FrameworkRuntimeException(ResultCode.WECHAT_NOT_OWNER_ERROR, "未有绑定业主");
            } else if (ownerVo.getStatus().intValue() == 0) {
                throw new FrameworkRuntimeException(ResultCode.WECHAT_OWNER_NOT_EXIST_ERROR, "业主已经过户或者销户");
            }
        }
        // 查询当前openid 绑定的所有业主
        List<OwnerVo> ownerPoList = iWechatUserService.getOwnerList(openid, enterpriseid);
        for (OwnerVo po : ownerPoList) {
            if (po.getUserno().equals(ownerVo.getUserno())) {
                throw new FrameworkRuntimeException(ResultCode.WECHATBLINDERROR, "业主已经绑定过");
            }
        }
        // 判断是否设置为默认账号
        if ((wechatParamForm.getIsdefaultblind() != null
                && wechatParamForm.getIsdefaultblind().intValue()
                == WaterConstants.WECHAT_OWNER_IS_DEFAULT) || StringUtils.isBlank(ownerid)) {
            // 更新其他账号为非默认账号
            iWechatUserService.updateWechatIsdefault(openid);
            // 绑定当前账号是默认账号
            iWechatUserService.blindOwner(
                    openid, ownerVo.getOwnerid(), WaterConstants.WECHAT_OWNER_IS_DEFAULT);
            svc.set(CacheKey.WaterWechatOwnerid + token, ownerVo.getOwnerid());
        } else {
            // 绑定当前账号为非默认账号
            iWechatUserService.blindOwner(
                    openid, ownerVo.getOwnerid(), WaterConstants.WECHAT_OWNER_IS_NOT_DEFAULT);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
    public void changeOwner(String owneridPar) throws FrameworkRuntimeException {
        WechatUser wechatUser = WechatAuthClient.get();
        String enterpriseid = wechatUser.getEnterpriseid();
        String openid = wechatUser.getOpenid();
        List<OwnerVo> list = iWechatUserService.getOwnerList(openid, enterpriseid);
        for (OwnerVo po : list) {
            if (owneridPar.equals(po.getOwnerid())) {
                svc.set(CacheKey.WaterWechatOwnerid + wechatUser.getToken(), owneridPar);
            }
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
    public void deleteOwnerBlind(String owneridPar) throws FrameworkRuntimeException {
        WechatUser wechatUser = WechatAuthClient.get();
        String enterpriseid = wechatUser.getEnterpriseid();
        String openid = wechatUser.getOpenid();
        List<OwnerVo> ownerVoList = iWechatUserService.getOwnerList(openid, enterpriseid);
        for (OwnerVo ownerVo : ownerVoList) {
            // 删除操作 查看操作对象是否是默认账号 如果是默认账号不予许删除
            if (owneridPar.equals(ownerVo.getOwnerid())) {
                if (ownerVo.getIsdefault() != null
                        && ownerVo.getIsdefault().intValue() == WaterConstants.WECHAT_OWNER_IS_DEFAULT) {
                    // 默认账号不予许删除
                    throw new FrameworkRuntimeException(ResultCode.WECHAT_OWNER_NOT_EXIST_ERROR, "默认账号不能删除");
                } else {
                    iWechatUserService.deleteOwnerBlind(openid, owneridPar);
                }
            }
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
    public void updateBlindStatus(WechatParamForm wechatParamForm) throws FrameworkRuntimeException {
        WechatUser wechatUser = WechatAuthClient.get();
        String openid = wechatUser.getOpenid();
        if (wechatParamForm.getIsdefaultblind() == null || wechatParamForm.getIsdefaultblind() == 1) {
            OwnerBo ownerBo1 = new OwnerBo();
            ownerBo1.setOwnerid(wechatParamForm.getOwnerid());
            // 检验业主是否存在
            OwnerVo owner = iOwnerService.findByOwnerId(ownerBo1);
            if (owner == null) {
                throw new FrameworkRuntimeException(ResultCode.WECHAT_NOT_OWNER_ERROR, "该用户不存在");
                // 校验当前登录业主是否过户或者销户
            } else if (owner.getStatus().intValue() != 1) {
                throw new FrameworkRuntimeException(ResultCode.WECHAT_OWNER_NOT_EXIST_ERROR, "该用户已经过户或者销户");
            }

            // 设置为不是默认账号
            iWechatUserService.updateWechatIsdefault(openid);
            // 设置指定的业主为默认账号
            iWechatUserService.updateDefaultBlindStatus(
                    wechatParamForm.getOwnerid(), openid, WaterConstants.WECHAT_OWNER_IS_DEFAULT);
            // 默认账号加入缓存中
            svc.set(CacheKey.WaterWechatOwnerid + wechatUser.getToken(), wechatParamForm.getOwnerid());
        } else {
            iWechatUserService.updateDefaultBlindStatus(
                    wechatParamForm.getOwnerid(), openid, WaterConstants.WECHAT_OWNER_IS_NOT_DEFAULT);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
    public void setIschargebacks(WechatParamForm wechatParamForm) throws FrameworkRuntimeException {
        if (wechatParamForm.getIschargebacks() == 1 || wechatParamForm.getIschargebacks() == 0) {
            OwnerBo ownerBo1 = new OwnerBo();
            ownerBo1.setOwnerid(wechatParamForm.getOwnerid());
            // 检验业主是否存在
            OwnerVo owner = iOwnerService.findByOwnerId(ownerBo1);
            if (owner == null) {
                throw new FrameworkRuntimeException(ResultCode.WECHAT_NOT_OWNER_ERROR, "该用户不存在");
                // 校验当前登录业主是否过户或者销户
            } else if (owner.getStatus().intValue() != 1) {
                throw new FrameworkRuntimeException(ResultCode.WECHAT_OWNER_NOT_EXIST_ERROR, "该用户已经过户或者销户");
            }
            owner.setIschargebacks(wechatParamForm.getIschargebacks());
            int updateRecord =
                    iWechatUserService.setIschargebacks(
                            owner.getOwnerid(), wechatParamForm.getIschargebacks());
        } else {
            throw new FrameworkRuntimeException(ResultCode.WECHAT_NOT_OWNER_ERROR, "数据异常");
        }
    }
}
