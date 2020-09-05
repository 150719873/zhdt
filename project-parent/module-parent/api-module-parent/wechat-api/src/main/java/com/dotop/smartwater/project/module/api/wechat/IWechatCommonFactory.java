package com.dotop.smartwater.project.module.api.wechat;

import com.dotop.smartwater.dependence.core.common.BaseFactory;
import com.dotop.smartwater.dependence.core.common.BaseForm;
import com.dotop.smartwater.dependence.core.common.BaseVo;
import com.dotop.smartwater.project.module.core.third.bo.wechat.WechatOrderBo;

public interface IWechatCommonFactory extends BaseFactory<BaseForm, BaseVo> {

	public int updateOrderRecord(WechatOrderBo wechatOrder, String ownerid);
}
