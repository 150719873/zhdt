package com.dotop.smartwater.project.module.api.wechat;

import java.util.List;
import java.util.Map;

import com.dotop.smartwater.dependence.core.common.BaseFactory;
import com.dotop.smartwater.dependence.core.common.BaseVo;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.project.module.core.water.form.customize.WechatParamForm;
import com.dotop.smartwater.project.module.core.water.vo.OwnerVo;
import com.dotop.smartwater.project.module.core.water.vo.customize.WechatPayTypeShowVo;

public interface IWechatUserFactory extends BaseFactory<WechatParamForm, BaseVo> {

    Map<String, String> setSession(WechatParamForm wechatParamForm);

    /**
     * 获取绑定的所有业主列表
     *
     * @return
     * @throws FrameworkRuntimeException
     */
    List<OwnerVo> getOwnerList();

    /**
     * 查询登录后的绑定信息
     *
     * @return
     * @throws FrameworkRuntimeException
     */
    OwnerVo getOwnerInfo();

    /**
     * 绑定业主信息功能
     *
     * @param wechatParamForm
     * @throws FrameworkRuntimeException
     */
    void blindOwner(WechatParamForm wechatParamForm);

    /**
     * 切换当前的绑定业主
     *
     * @param ownerid
     * @throws FrameworkRuntimeException
     */
    void changeOwner(String ownerid);

    /**
     * 删除绑定关系
     *
     * @param ownerid
     * @throws FrameworkRuntimeException
     */
    void deleteOwnerBlind(String ownerid);

    /**
     * 更新绑定业主的状态
     *
     * @param wechatParamForm
     * @throws FrameworkRuntimeException
     */
    void updateBlindStatus(WechatParamForm wechatParamForm);

    /**
     * 设置自动扣费功能
     *
     * @param wechatParamForm
     * @throws FrameworkRuntimeException
     */
    void setIschargebacks(WechatParamForm wechatParamForm);

    /**
     * 获取不可变的 openid
     *
     * @param code
     * @param enterpriseid
     * @return
     * @throws FrameworkRuntimeException
     */
    Map<String, Object> getWeixinOpenId(String code, String enterpriseid);

    WechatPayTypeShowVo showPayType();
}
