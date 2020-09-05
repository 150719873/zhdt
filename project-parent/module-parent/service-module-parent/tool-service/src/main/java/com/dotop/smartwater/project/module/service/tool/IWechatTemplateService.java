package com.dotop.smartwater.project.module.service.tool;

import com.dotop.smartwater.dependence.core.common.BaseService;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.water.bo.customize.WechatTemplateBo;
import com.dotop.smartwater.project.module.core.water.vo.WechatTemplateVo;

/**
 * 

 * @date 2018年7月30日 下午2:02:03
 * @version 1.0.0
 */
public interface IWechatTemplateService extends BaseService<WechatTemplateBo, WechatTemplateVo> {

	/**
	 * 
	 * 新增
	 */
	@Override
	WechatTemplateVo add(WechatTemplateBo wechatTemplateBo);

	/**
	 * 微信模板分页查询
	 * 
	 * @param wechatSettingParamBo
	 * @return
	 */
	@Override
	Pagination<WechatTemplateVo> page(WechatTemplateBo wechatTemplateBo);

	/**
	 * 编辑
	 */
	@Override
	WechatTemplateVo edit(WechatTemplateBo wechatTemplateBo);

	/**
	 * 查询详情
	 */
	@Override
	WechatTemplateVo get(WechatTemplateBo wechatTemplateBo);

	/**
	 * 别处有调用
	 * 
	 * @param enterpriseid
	 * @param smsType
	 * @return @
	 */
	WechatTemplateVo getByEnterpriseidAndsmsType(String enterpriseid, Integer smsType);

	WechatTemplateVo getWeChatModelInfo(String enterpriseId, Integer smsType);

	/**
	 * 校验是否存在
	 * 
	 * @param wechatPublicSettingBo
	 * @return @
	 */
	@Override
	boolean isExist(WechatTemplateBo wechatTemplateBo);

}
