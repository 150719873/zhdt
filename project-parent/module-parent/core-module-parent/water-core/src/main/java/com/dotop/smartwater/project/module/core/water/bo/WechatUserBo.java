package com.dotop.smartwater.project.module.core.water.bo;

import com.dotop.smartwater.dependence.core.common.BaseBo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 微信报装用户
 * 

 * @date 2019年3月31日
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class WechatUserBo extends BaseBo {
	/** 用户ID */
	private String id;
	/** OPENID */
	private String openid;
	/** 姓名 */
	private String name;
	/** 头像 */
	private String headUrl;
	/** 手机号 */
	private String phone;
	/** 身份证号 */
	private String cardid;
	/** 验证码 */
	private String code;
	/** 验证码生成时间 */
	private String codeTime;
	/** 来源 */
	private String source;
}
