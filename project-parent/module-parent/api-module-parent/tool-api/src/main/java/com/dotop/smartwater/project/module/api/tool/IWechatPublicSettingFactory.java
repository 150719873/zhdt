package com.dotop.smartwater.project.module.api.tool;

import com.dotop.smartwater.dependence.core.common.BaseFactory;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.water.form.WechatPublicSettingForm;
import com.dotop.smartwater.project.module.core.water.vo.customize.WechatPublicSettingVo;

/**
 * 微信接入公共配置
 * 

 * @date 2019年4月1日
 */
public interface IWechatPublicSettingFactory extends BaseFactory<WechatPublicSettingForm, WechatPublicSettingVo> {

	@Override
	Pagination<WechatPublicSettingVo> page(WechatPublicSettingForm wechatPublicSettingForm);

	@Override
	WechatPublicSettingVo get(WechatPublicSettingForm wechatPublicSettingForm);

	@Override
	WechatPublicSettingVo add(WechatPublicSettingForm wechatPublicSettingForm);

	@Override
	WechatPublicSettingVo edit(WechatPublicSettingForm wechatPublicSettingForm);

	@Override
	String del(WechatPublicSettingForm wechatPublicSettingForm);

}
