package com.dotop.smartwater.project.module.core.water.vo;

import com.dotop.smartwater.dependence.core.common.BaseVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 报装-合同
 * 

 * @date 2019年3月8日
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class InstallContractVo extends BaseVo {
	/** 主键 */
	private String id;
	/** 单号 */
	private String number;
	/** 合同编号 */
	private String contractNo;
	/** 合同名称 */
	private String contractName;
	/** 签订人 */
	private String contractUsername;
	/** 签订时间 */
	private String contractTime;
	/** 签订状态 */
	private String signStatus;
	/** 合同类别ID */
	private String typeId;
	/** 合同类别名称 */
	private String typeName;
	/** 合同金额 */
	private String amount;
	/** 产品型号ID */
	private String modelId;
	/** 产品型号名称 */
	private String modelName;
	/** 水表种类ID */
	private String kindId;
	/** 水表种类名称 */
	private String kindName;
	/** 上传附件 */
	private String uploadFile;
}
