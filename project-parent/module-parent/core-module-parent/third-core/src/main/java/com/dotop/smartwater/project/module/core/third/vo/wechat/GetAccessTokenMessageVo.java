package com.dotop.smartwater.project.module.core.third.vo.wechat;

import lombok.Data;

/**
 * 获取AccessToken返回对象
 * 
 */
@Data
public class GetAccessTokenMessageVo {

	public String access_token;
	public Long expires_in;

}