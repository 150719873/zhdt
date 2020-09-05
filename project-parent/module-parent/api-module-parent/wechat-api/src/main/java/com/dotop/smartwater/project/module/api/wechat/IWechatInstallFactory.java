package com.dotop.smartwater.project.module.api.wechat;

import java.util.Map;

import com.dotop.smartwater.dependence.core.common.BaseFactory;
import com.dotop.smartwater.project.module.core.water.form.WechatUserForm;
import com.dotop.smartwater.project.module.core.water.vo.WechatUserVo;

public interface IWechatInstallFactory extends BaseFactory<WechatUserForm, WechatUserVo> {

	boolean update(WechatUserForm form);

	Map<String, String> login(WechatUserForm form);

	WechatUserVo save(WechatUserForm form);

}
