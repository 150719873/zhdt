package com.dotop.smartwater.project.module.core.third.vo.wechat;

import lombok.Data;

/**
 * 

 * @date 2018年7月17日 下午2:50:52
 * @version 1.0.0
 */
@Data
public class WechatOwnerVo {

	private String ownerid;

	private String openid;

	// 微信公众号绑定的是否是默认账号,1-是，0-否 null-否
	private Integer isdefault;

}
