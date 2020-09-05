package com.dotop.smartwater.project.module.core.auth.sms;

import java.util.Date;

import com.dotop.smartwater.dependence.core.common.BaseVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class SmsTemplateVo extends BaseVo {

	private String id;
//	private String enterpriseid;// 企业ID
	private String enterprisename;
	private String name;// 名称
	private String smsptid;// 短信平台对应的模板标识
	private String code;// 模板标识
	private String content;// 模板内容
	private Integer status;// 状态，0-启用，1-禁用
	private String remarks;// 备注
	private Integer delflag;// 删除标识，0-正常，1-删除
	private String createuser;// 创建用户
	private Date createtime;// 创建时间
	private String updateuser;// 修改用户
	private Date updatetime;// 修改时间
	private Integer printstatus;
	private Integer smstype;// 1-业主开户,2-业主销户,3-业主换表,4-业主过户,5-缴费成功,6-错账处理,,7-生成账单,,8-充值成功

	public static final int STATUS_ENABLE = 0;
	public static final int STATUS_DISABLE = 1;
	
	private String smstypename;
	private Date bindtime;

//	@Override
//	public String toString() {
//		return "SmsTemplate [id=" + id + ", enterpriseid=" + enterpriseid + ", name=" + name + ", smsptid=" + smsptid
//				+ ", code=" + code + ", content=" + content + ", status=" + status + ", remarks=" + remarks
//				+ ", delflag=" + delflag + ", createuser=" + createuser + ", createtime=" + createtime + ", updateuser="
//				+ updateuser + ", updatetime=" + updatetime + "]";
//	}

}
