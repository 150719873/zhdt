package com.dotop.smartwater.project.module.core.water.vo;

import java.util.List;

import com.dotop.smartwater.dependence.core.common.BaseVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class OwnerVo extends BaseVo {
	/** 表册号 */
	private String bookNum;

	private String devno;
	
	private String ownerType;
	
	private String ownerTypeName;

	private Double beginvalue;

	private String ownerid;

	private String userno;

	private String username;

	private String useraddr;

	private String userphone;

	private String remark;

	private String typename;

	private String pricetypeid;

	private String communityid;

	private String communityname;
	
	private String communityCode;

	private String cardtype;

	private String cardid;

	private String createtime;

	private String installmonth;

	private String purposename;

	private String paytypename;

	private String modelname;

	private Double alreadypay;

	private Integer status;
	
	private String statusText;

	private String devid;

	private Double water;

	private Integer tapstatus;

	private String uplinktime;

	private String modelid;

	private String tradeno;
	private String readtime;
	private String orderdate;
	private Integer timeinterval;
	// 未缴金额(未缴账单金额)
	private Double arrears = 0.0;
	// 欠费金额(未缴账单金额减去账户余额)
	private Double owe = 0.0;
	
	// 未缴账单数量
	private int noPayNumber;
	
	private String paytypeid;
	private String upreadtime;
	private String upreadwater;

	// -----------------------------

	private String deveui;

	private String createuser;

	private String communityno;

	/** 水表用途 ID */
	private String purposeid;

	/** 用水减免 */
	private String reduceid;
	private String reducename;
	private Double rvalue;
	private Integer runit;

	/** 是否自动扣费，1-扣费 0-不扣费 */
	private Integer ischargebacks;

	private Integer devStatus;

	/** 此参数用于列表查询时根据分配区域读取 */
	private List<String> nodeIds;

	/** 关键字查询 */
	private String keywords;

	/** 是否在线 */
	private Integer isonline;

	private OwnerExtVo ownerExtVo;

	/** 微信綁定的公众号id */
	private String openid;

	private Integer isdefault;// 绑定微信是否默认

	private Boolean currentowner;// 是否当前业主，ture是，false否，null否
	//是否带阀 0 否 1 是
	private Integer taptype;

	// 口径
	private String caliber;

	private String province;
	private String city;
	private String district;
	private String building;
	private String unit;
	private String room;
	private String box;
	
	// 抄表人员编号
	private String meterUserCode;
	
	//二维码条码内容
	private String barCode;
}
