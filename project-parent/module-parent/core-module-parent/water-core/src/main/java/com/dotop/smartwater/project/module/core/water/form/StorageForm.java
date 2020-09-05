package com.dotop.smartwater.project.module.core.water.form;

import java.util.Date;

import com.dotop.smartwater.dependence.core.common.BaseForm;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 
 * 库存管理-产品入库VO
 * 

 * @date 2018-11-27 上午 10:55
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class StorageForm extends BaseForm {

	private String recordNo; // 记录号
	
	private String repoNo; // 仓库编号

	private String repoName; // 仓库名称

	private String productNo; // 产品编号

	private String name;

	private Integer quantity; // 产品数量

	private Integer stock; // 库存（只记录入库数量，不增减）

	private String unit; // 单位

	private double price; // 产品价格

	private double total; // 总价

	private Date productionDate; // 生产日期

	private String proDateStr; // 生产日期Str

	private Date storageDate; // 入库日期

	private String storDateStr; // 入库日期Str

	private Date storDatePlus; // 入库时间结尾

	private String storDatePlusStr; //

	private String vender; // 厂家

	private String storageUserId; // 入库人ID

	private String storageUsername; // 入库人姓名

	private Date effectiveDate; // 有效日期

	private String effDateStr; // 有效日期Str

	private String remark; // 备注

	// private Long enterpriseId; // 企业ID

	private Integer status; // 状态

	private StoreProductForm product; // 产品类

}
