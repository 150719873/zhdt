package com.dotop.smartwater.project.module.core.water.form;

import java.util.Date;

import com.dotop.smartwater.dependence.core.common.BaseForm;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class SmsSetupForm extends BaseForm {

	private String id;
	private String name;// 平台名称
	private String code;// 标识
	private String sign;// 签名
	private String mkey;
	private String mkeysecret;
	private Integer status;// 状态，0-启用，1-禁用
	private String remarks;// 备注
	private Integer delflag;// 删除标识，0-正常，1-删除
	private String createuser;// 创建用户
	private Date createtime;// 创建时间
	private String updateuser;// 修改用户
	private Date updatetime;// 修改时间
	
	public static final int STATUS_ENABLE = 0;
	public static final int STATUS_DISABLE = 1;

	public static final String CODE_ALIYUN = "aliyun";

}
