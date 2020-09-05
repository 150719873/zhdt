package com.dotop.smartwater.project.module.api.wechat;

import javax.servlet.http.HttpServletRequest;

import com.dotop.smartwater.dependence.core.common.BaseFactory;
import com.dotop.smartwater.dependence.core.common.BaseForm;
import com.dotop.smartwater.dependence.core.common.BaseVo;

public interface IWechatReturnUrlFactory extends BaseFactory<BaseForm, BaseVo> {

	void get(HttpServletRequest request);

}
