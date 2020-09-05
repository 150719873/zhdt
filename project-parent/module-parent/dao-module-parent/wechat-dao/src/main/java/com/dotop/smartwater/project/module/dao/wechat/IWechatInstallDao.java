package com.dotop.smartwater.project.module.dao.wechat;

import com.dotop.smartwater.dependence.core.common.BaseDao;
import com.dotop.smartwater.project.module.core.water.dto.WechatUserDto;
import com.dotop.smartwater.project.module.core.water.vo.WechatUserVo;

public interface IWechatInstallDao extends BaseDao<WechatUserDto, WechatUserVo> {

	int update(WechatUserDto dto);

	int save(WechatUserDto dto);

	@Override
	WechatUserVo get(WechatUserDto dto);

}
