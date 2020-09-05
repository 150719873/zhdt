package com.dotop.smartwater.project.module.core.water.dto;

import java.util.Date;
import java.util.List;

import com.dotop.smartwater.dependence.core.common.BaseDto;
import com.dotop.smartwater.project.module.core.water.model.SortModel;
import com.dotop.smartwater.project.module.core.water.model.SortModel;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**

 */
@Data
@EqualsAndHashCode(callSuper = true)
public class OwnerDto extends BaseDto {
	/** 表册号 */
	private String bookNum;

	private String devno;

	private String deveui;

	private String ownerid;

	private String userno;

	private String username;

	private String useraddr;

	private String userphone;

	private Integer status;

	private String remark;

	private String devid;

	private String pricetypeid;

	private String communityid;

	private String createuser;

	private Date createtime;

	private String installmonth;

	private Double alreadypay;

	private String communityno;
	/** 区域名称 */
	private String communityname;
	/** 上期抄表时间 */
	private String upreadtime;
	/** 上期读数 */
	private String upreadwater;

	/** 收费种类ID */
	private String paytypeid;
	/** 收费种类 */
	private String paytypename;
	/** 水表用途 ID */
	private String purposeid;
	/** 水表用途 */
	private String purposename;

	/** 用水减免 */
	private String reduceid;

	private String reducename;

	/** 证件类型，1-身份证 2-护照 */
	private Integer cardtype;
	/** 证件号 */
	private String cardid;
	/** 是否自动扣费，1-扣费 0-不扣费 */
	private Integer ischargebacks;
	/** 设备类型 */
	private String modelid;

	private String modelname;

	private String enterpriseid;

	private Integer devStatus;

	private String tradeno;
	private String readtime;
	private String orderdate;
	private Integer timeinterval;
	private Double arrears = 0.0;

	// 水表读数
	private Double water;

	private String uplinktime;

	/** 此参数用于列表查询时根据分配区域读取 */
	private List<String> nodeIds;

	/** 关键字查询 */
	private String keywords;

	/** 是否在线 */
	private Integer isonline;

	private String openid;

	private String province;
	private String city;
	private String district;
	private String building;
	private String unit;
	private String room;
	private String box;

	private String ownerType;
	
	private String barCode;
	// 是否开户  1-开户 0-不开户
 	private String isOpen;
 	
 	/**排序实体类*/
	private List<SortModel> sortList;
}
