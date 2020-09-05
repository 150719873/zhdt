package com.dotop.smartwater.project.module.service.revenue;

import com.dotop.smartwater.dependence.core.common.BaseService;
import com.dotop.smartwater.project.module.core.auth.bo.LogoBo;
import com.dotop.smartwater.project.module.core.auth.vo.LogoVo;

/**
 * 
 * 

 * @date 2019年2月25日
 */
public interface ILogoService extends BaseService<LogoBo, LogoVo> {

	/**
	 * 添加图片
	 */
	@Override
	LogoVo add(LogoBo logoBo);

	/**
	 * 查询图片
	 */
	@Override
	LogoVo get(LogoBo logoBo);

	/**
	 * 删除图片
	 */
	@Override
	String del(LogoBo logoBo);

}
