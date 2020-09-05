package com.dotop.smartwater.project.module.core.water.bo;

import java.util.Date;

import com.dotop.smartwater.dependence.core.common.BaseBo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 库存-产品VO
 * 

 * @date 2018-11-20 下午17:00
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class StoreProductBo extends BaseBo {

	private String recordNo;

	private String productId;//

	private String proUrlImg1;// 图片1

	private String proUrlImg2;

	private String proUrlImg3;

	private String productNo;// 产品编号

	private String name;// 名称

	private String intro;// 简介

	private String model;// 型号

	private String spec;// 规格

	private String material;// 材质
	//材质名称
	private String materialName;
	//材质对应字段value
	private String materialValue;

	private String weight;// 重量

	private String unit;// 单位

	private double price;// 单价
	
	private Integer useYear;//使用年限
	
	private double limitValue;//极限值

	private String describe;// 描述

	private Integer status;// 状态

	// private String statusClass;//状态按钮样式

	// private Long enterpriseId;// 企业ID

	private String enterprisename; // 企业名称

	private String vender;// 厂家

	private String produced;// 产地

	private String contacts;// 联系人

	private String phone;// 联系电话
	//口径
	private Integer caliber;
	//类别
	private String category;
	//类别名称
	private String categoryName;
	//类别对应字典value
	private String categoryValue;
	//类型
	private String type;
	//类型名称
	private String typeName;
	//类型对应字典value
	private String typeValue;

	private Date createTime;// 创建时间

	private String createTimeStr; // 创建时间字符串

	private String createUserId;// 创建人ID

	private String createUsername;// 创建人

	private Date updateTime;// 修改时间

	private String updateTimeStr; // 修改时间字符串

	private String updateUserId; // 创建人ID

	private String updateUsername;// 创建人

}
