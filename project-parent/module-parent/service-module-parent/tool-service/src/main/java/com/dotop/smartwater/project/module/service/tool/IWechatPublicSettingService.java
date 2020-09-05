package com.dotop.smartwater.project.module.service.tool;

import com.dotop.smartwater.dependence.core.common.BaseService;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.water.bo.WechatPublicSettingBo;
import com.dotop.smartwater.project.module.core.water.vo.customize.WechatPublicSettingVo;

/**
 * 

 * @date 2018年7月30日 下午2:02:03
 * @version 1.0.0
 */
public interface IWechatPublicSettingService extends BaseService<WechatPublicSettingBo, WechatPublicSettingVo> {

	@Override
	WechatPublicSettingVo add(WechatPublicSettingBo wechatPublicSetting);

	/**
	 * 分页查询
	 * 
	 * @param wechatSettingParamBo
	 * @return
	 */
	@Override
	Pagination<WechatPublicSettingVo> page(WechatPublicSettingBo wechatPublicSettingBo);

	/**
	 * 删除
	 */
	@Override
	String del(WechatPublicSettingBo wechatPublicSettingBo);

	/**
	 * 编辑
	 */
	@Override
	WechatPublicSettingVo edit(WechatPublicSettingBo wechatPublicSetting);

	// 查询详情
	@Override
	WechatPublicSettingVo get(WechatPublicSettingBo wechatPublicSettingBo);

	@Override
	boolean isExist(WechatPublicSettingBo wechatPublicSettingBo);

	WechatPublicSettingVo getByenterpriseId(String enterpriseid);
}
