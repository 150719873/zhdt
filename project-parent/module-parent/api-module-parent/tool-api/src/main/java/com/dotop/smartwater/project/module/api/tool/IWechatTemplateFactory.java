package com.dotop.smartwater.project.module.api.tool;

import com.dotop.smartwater.dependence.core.common.BaseFactory;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.water.form.customize.WechatTemplateForm;
import com.dotop.smartwater.project.module.core.water.vo.WechatTemplateVo;

/**
 * 微信接入公共配置
 * 

 * @date 2019年4月1日
 */
public interface IWechatTemplateFactory extends BaseFactory<WechatTemplateForm, WechatTemplateVo> {

	/**
	 * 分页查询
	 */
	@Override
	Pagination<WechatTemplateVo> page(WechatTemplateForm wechatTemplateForm);

	/**
	 * 查询详情
	 */
	@Override
	WechatTemplateVo get(WechatTemplateForm wechatTemplateForm);

	/**
	 * 新增
	 */
	@Override
	WechatTemplateVo add(WechatTemplateForm wechatTemplateForm);

	/**
	 * 修改
	 */
	@Override
	WechatTemplateVo edit(WechatTemplateForm wechatTemplateForm);

	/**
	 * 删除
	 */
	@Override
	String del(WechatTemplateForm wechatTemplateForm);

	/**
	 * 禁用
	 * @Param wechatTemplateForm 对象
	 * @param wechatTemplateForm
	 */
	WechatTemplateVo disable(WechatTemplateForm wechatTemplateForm);

	/**
	 * 启用
	 * @Param wechatTemplateForm 对象
	 * @param wechatTemplateForm
	 */
	WechatTemplateVo enable(WechatTemplateForm wechatTemplateForm);

}
