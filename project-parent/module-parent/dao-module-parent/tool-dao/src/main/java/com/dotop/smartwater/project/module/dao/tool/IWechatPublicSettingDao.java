package com.dotop.smartwater.project.module.dao.tool;

import com.dotop.smartwater.project.module.core.water.dto.WechatPublicSettingDto;
import com.dotop.smartwater.project.module.core.water.vo.customize.WechatPublicSettingVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface IWechatPublicSettingDao {

	/**
	 * 保存功能
	 *
	 * @param wechatPublicSetting
	 * @return
	 * @
	 */
	void add(WechatPublicSettingDto wechatPublicSetting);

	/**
	 * 查询微信配置详情
	 *
	 * @param wechatPublicSettingDto
	 * @return
	 * @
	 */
	WechatPublicSettingVo get(WechatPublicSettingDto wechatPublicSettingDto);

	/**
	 * 微信接入配置 分页查询
	 *
	 * @return
	 */
	List<WechatPublicSettingVo> list(WechatPublicSettingDto wechatPublicSettingDto);

	/**
	 * 删除功能
	 *
	 * @param wechatPublicSettingDto
	 * @return
	 * @
	 */
	int del(WechatPublicSettingDto wechatPublicSettingDto);

	/**
	 * 编辑功能
	 *
	 * @param wechatPublicSetting
	 * @return
	 */
	int edit(WechatPublicSettingDto wechatPublicSetting);

	WechatPublicSettingVo detail(@Param("wechatpublicid") String wechatpublicid);

	boolean isExist(WechatPublicSettingDto wechatPublicSettingDto);
}
