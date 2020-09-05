package com.dotop.smartwater.project.module.service.wechat;

import com.dotop.smartwater.dependence.core.common.BaseService;
import com.dotop.smartwater.project.module.core.water.bo.WechatUserBo;
import com.dotop.smartwater.project.module.core.water.vo.WechatUserVo;

public interface IWechatInstallService extends BaseService<WechatUserBo, WechatUserVo> {

	boolean update(WechatUserBo bo);

	WechatUserVo save(WechatUserBo bo);

	@Override
	WechatUserVo get(WechatUserBo bo);

}
