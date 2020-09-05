package com.dotop.smartwater.project.module.core.water.vo;

import java.util.Date;

import com.dotop.smartwater.dependence.core.common.BaseVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 出库管理VO
 * 

 * @date 2018-11-30 下午 15:30
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class OutStorageVo extends BaseVo {

	private String recordNo; // 记录号

	private String repoNo; // 仓库编号

	private String repoName; // 仓库名称

	private String productNo; // 产品编号

	private String name; // 产品名称

	private Integer quantity; // 产品数量

	private String unit; // 单位

	private double price; // 产品价格

	private double total; // 总价

	private Date outstorageDate; // 出库日期

	private String outDateStr; // 出库日期Str

	private Date outDatePlus; // 出库时间结尾

	private String outDatePlusStr; //

	private String outUserId; // 入库人ID

	private String outUsername; // 入库人姓名

	private String remark; // 备注

	// private Long enterpriseId; // 企业ID

	private Integer status; // 状态 1-已保存 0-已出库 -1-申请中 -2-审核中 -3-审核失败

	private StoreProductVo product; // 产品类

	private StorageVo storage; // 入库类

}
