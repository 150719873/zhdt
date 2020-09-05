package com.dotop.smartwater.project.module.core.water.bo;

import java.math.BigDecimal;
import java.util.Date;

import com.dotop.smartwater.dependence.core.common.BaseBo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 同 order_preview
 */
// 表存在
@Data
@EqualsAndHashCode(callSuper = false)
public class OrderPreviewBo extends BaseBo {
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

	/* 减免id */
	private String reduceid;

	/* 水费用途id */
	private String purposeid;

	// 区域集合
	private String cids;

	public void setTradeno(String tradeno) {
		if (tradeno != null) {
			this.tradeno = tradeno.trim();
		} else {
			this.tradeno = null;
		}
	}

	public void setYear(String year) {
		if (year != null) {
			this.year = year.trim();
		} else {
			this.year = null;
		}
	}

	public void setMonth(String month) {
		if (month != null) {
			this.month = month.trim();
		} else {
			this.month = null;
		}
	}

	public void setCommunityno(String communityno) {
		if (communityno != null) {
			this.communityno = communityno.trim();
		}else{
			this.communityno = null;
		}
	}

	public void setCommunityname(String communityname) {
		if (communityname != null) {
			this.communityname = communityname.trim();
		}else{
			this.communityname = null;
		}
	}

	public void setUserno(String userno) {
		if (userno != null) {
			this.userno = userno.trim();
		} else {
			this.userno = null;
		}
	}

	public void setUsername(String username) {
		if (username != null) {
			this.username = username.trim();
		} else {
			this.username = null;
		}
	}

	public void setPhone(String phone) {
		if (phone != null) {
			this.phone = phone.trim();
		} else {
			this.phone = null;
		}
	}

	public void setAddr(String addr) {
		if (addr != null) {
			this.addr = addr.trim();
		} else {
			this.addr = null;
		}
	}

	public void setPricetypename(String pricetypename) {
		if (pricetypename != null) {
			this.pricetypename = pricetypename.trim();
		} else {
			this.pricetypename = null;
		}
	}

	public void setDeveui(String deveui) {
		if (deveui != null) {
			this.deveui = deveui.trim();
		} else {
			this.deveui = null;
		}
	}

	public void setDevno(String devno) {
		if (devno != null) {
			this.devno = devno.trim();
		} else {
			this.devno = null;
		}
	}

	public void setExplain(String explain) {
		if (explain != null) {
			this.explain = explain.trim();
		} else {
			this.explain = null;
		}
	}

	public void setPayno(String payno) {
		if (payno != null) {
			this.payno = payno.trim();
		} else {
			this.payno = null;
		}
	}

	public void setOperatorname(String operatorname) {
		if (operatorname != null) {
			this.operatorname = operatorname.trim();
		} else {
			this.operatorname = null;
		}
	}

	public void setDescribe(String describe) {
		if (describe != null) {
			this.describe = describe.trim();
		} else {
			this.describe = null;
		}
	}

	public void setGenerateusername(String generateusername) {
		if (generateusername != null) {
			this.generateusername = generateusername.trim();
		} else {
			this.generateusername = null;
		}
	}

}