package com.dotop.smartwater.project.module.core.water.bo;

import com.dotop.smartwater.dependence.core.common.BaseBo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 报装-用户档案
 * 

 * @date 2019年3月8日
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class InstallUserBo extends BaseBo {
	/** 主键 */
	private String id;
	/** 单号 */
	private String number;
	/** 用户编号 */
	private String userNo;
	/** 用户名称 */
	private String userName;
	/** 手机号 */
	private String userPhone;
	/** 区域ID */
	private String communityId;
	/** 区域名称 */
	private String communityName;
	/** 产品型号ID */
	private String modelId;
	/** 产品型号名称 */
	private String modelName;
	/** 收费种类ID */
	private String paytypeId;
	/** 收费种类ID */
	private String paytypeName;
	/** 水表用途 */
	private String purposeId;
	/** 水表用途 */
	private String purposeName;
	/** 水表种类ID */
	private String kindId;
	/** 水表种类名称 */
	private String kindName;
	/** 水表号 */
	private String devno;
	/** 地址 */
	private String addr;
	/** 状态 （0-已同步 1-已导入 2-导入失败） */
	private String status;
	/** 状态说明 */
	private String explan;

}
