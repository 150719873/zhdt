package com.dotop.smartwater.project.module.dao.tool;

import com.dotop.smartwater.project.module.core.water.dto.customize.WechatTemplateDto;
import com.dotop.smartwater.project.module.core.water.vo.WechatTemplateVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface IWechatTemplateDao {

	/**
	 * 新增模板绑定
	 *
	 * @param wechatTemplate
	 * @return
	 */
	int add(WechatTemplateDto wechatTemplate);

	/**
	 * 分页查询
	 *
	 * @param wechatTemplate
	 * @return
	 * @
	 */
	List<WechatTemplateVo> list(WechatTemplateDto wechatTemplate);

	/**
	 * 其他方法调用
	 *
	 * @param enterpriseid
	 * @param smsType
	 * @return
	 */
	WechatTemplateVo getByEnterpriseidAndsmsType(@Param("enterpriseid") String enterpriseid,
	                                             @Param("smsType") Integer smsType);

	/**
	 * 是否已存在
	 *
	 * @param wechatTemplateDto
	 * @return
	 * @
	 */
	boolean isExist(WechatTemplateDto wechatTemplateDto);

	/**
	 * 查询微信模板详情
	 *
	 * @param wechatTemplateDto
	 * @return
	 * @
	 */
	WechatTemplateVo get(WechatTemplateDto wechatTemplateDto);

	/**
	 * 编辑
	 *
	 * @param wechatTemplateDto
	 * @
	 */
	void edit(WechatTemplateDto wechatTemplateDto);
}
