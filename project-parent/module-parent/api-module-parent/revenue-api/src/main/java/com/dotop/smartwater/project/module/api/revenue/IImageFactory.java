package com.dotop.smartwater.project.module.api.revenue;

import com.dotop.smartwater.dependence.core.common.BaseFactory;
import com.dotop.smartwater.project.module.core.auth.vo.LogoVo;
import com.dotop.smartwater.project.module.core.water.form.DeviceForm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 图片处理

 * @date 2019年2月25日
 */
public interface IImageFactory extends BaseFactory<DeviceForm, LogoVo> {

	/**
	 * 保存logo
	 * @param request 请求
	 * @param userid 用户ID
	 */
	void logo(HttpServletRequest request, String userid);

	/**
	 * 获取logo Url
	 * @param response 响应
	 * @param userid 用户ID
	 */
	void getLogo(HttpServletResponse response, String userid);

}
