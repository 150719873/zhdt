package com.dotop.smartwater.project.module.core.auth.vo;

import lombok.Data;

/**
 * 创建菜单返回对象
 * 

 *
 **/
@Data
public class WechatMessageVo {

	public int errcode;
	public String errmsg;
	public String msgid;
}
