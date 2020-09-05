package com.dotop.smartwater.project.module.core.water.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.dotop.smartwater.dependence.core.common.BaseDto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 同 order_preview
 *
 */
// 表存在
@Data
@EqualsAndHashCode(callSuper = false)
public class OrderPreviewDto extends BaseDto {
	private String ownerid;

	private String tradeno;

	private String year;

	private String month;

	private String communityid;

	private String communityno;

	private String communityname;

	private String userno;

	private String username;

	private String phone;

	private String cardid;

	private String addr;

	private String pricetypeid;

	private String pricetypename;

	private String deveui;

	private String devno;

	private Integer devstatus;

	private String explain;

	private Integer tapstatus;

	private Integer taptype;

	private String upreadtime;

	private Double upreadwater;

	private String readtime;

	private Double readwater;

	private Integer day;

	private Double water;

	private String payno;

	private BigDecimal original;

	private Double amount;

	private Double balance;

	private Double realamount;

	private Integer paytype;

	private Integer paystatus;

	private Date paytime;

	private String operatorid;

	private String operatorname;

	private Date operatortime;

	private Integer tradestatus;

	private String describe;

	private Integer errortype;

	private String generateuserid;

	private String generateusername;

	private String generatetime;
	//用于查询，查询半年内 
	private String generateTimeEnd;

	/* 减免id */
	private String reduceid;

	/* 水费用途id */
	private String purposeid;

	// 区域集合
	private String cids;

	public void setTradeno(String tradeno) {
		this.tradeno = tradeno == null ? null : tradeno.trim();
	}

	public void setYear(String year) {
		this.year = year == null ? null : year.trim();
	}

	public void setMonth(String month) {
		this.month = month == null ? null : month.trim();
	}

	public void setCommunityno(String communityno) {
		this.communityno = communityno == null ? null : communityno.trim();
	}

	public void setCommunityname(String communityname) {
		this.communityname = communityname == null ? null : communityname.trim();
	}

	public void setUserno(String userno) {
		this.userno = userno == null ? null : userno.trim();
	}

	public void setUsername(String username) {
		this.username = username == null ? null : username.trim();
	}

	public void setPhone(String phone) {
		this.phone = phone == null ? null : phone.trim();
	}

	public void setAddr(String addr) {
		this.addr = addr == null ? null : addr.trim();
	}

	public void setPricetypename(String pricetypename) {
		this.pricetypename = pricetypename == null ? null : pricetypename.trim();
	}

	public void setDeveui(String deveui) {
		this.deveui = deveui == null ? null : deveui.trim();
	}

	public void setDevno(String devno) {
		this.devno = devno == null ? null : devno.trim();
	}

	public void setExplain(String explain) {
		this.explain = explain == null ? null : explain.trim();
	}

	public void setPayno(String payno) {
		this.payno = payno == null ? null : payno.trim();
	}

	public void setOperatorname(String operatorname) {
		this.operatorname = operatorname == null ? null : operatorname.trim();
	}

	public void setDescribe(String describe) {
		this.describe = describe == null ? null : describe.trim();
	}

	public void setGenerateusername(String generateusername) {
		this.generateusername = generateusername == null ? null : generateusername.trim();
	}

}