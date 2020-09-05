package com.dotop.smartwater.project.module.core.auth.sms;

public class TemplateRelate {
	
	private Long id;

	private Long templateid ;//短信模板id
	
	private Long enterpriseid;//企业ID
	
	private Integer smstype;//0-业主开户,1-业主销户,2-业主换表,3-业主过户,4-缴费成功,5-错账处理,6-生成账单,7-催缴,8-充值成功
	
	private String smstypename;
	
	private Integer printstatus;//0-不自动打印，1-自动打印
	
	private Integer templatetype;//1-短信，2-微信消息
	
	private String bindtime;

	public Long getId() {
		return id;
	}

	public String getBindtime() {
		return bindtime;
	}

	public void setBindtime(String bindtime) {
		this.bindtime = bindtime;
	}

	public String getSmstypename() {
		return smstypename;
	}


	public void setSmstypename(String smstypename) {
		this.smstypename = smstypename;
	}


	public void setId(Long id) {
		this.id = id;
	}
	

	public Long getTemplateid() {
		return templateid;
	}

	public void setTemplateid(Long templateid) {
		this.templateid = templateid;
	}

	public Integer getTemplatetype() {
		return templatetype;
	}

	public void setTemplatetype(Integer templatetype) {
		this.templatetype = templatetype;
	}

	public Long getEnterpriseid() {
		return enterpriseid;
	}

	public void setEnterpriseid(Long enterpriseid) {
		this.enterpriseid = enterpriseid;
	}

	public Integer getSmstype() {
		return smstype;
	}

	public void setSmstype(Integer smstype) {
		this.smstype = smstype;
	}

	public Integer getPrintstatus() {
		return printstatus;
	}

	public void setPrintstatus(Integer printstatus) {
		this.printstatus = printstatus;
	}
	

}
