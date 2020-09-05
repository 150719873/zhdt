package com.dotop.smartwater.project.module.core.auth.vo;

import com.dotop.smartwater.dependence.core.common.BaseVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class AreaVo extends BaseVo {

	private String enterpriseid;

	private String id;

	private String pId;

	private String name;
	//区域编号
	private String code;

	// public Long getpId() {
	// return pId;
	// }
	//
	// public void setpId(Long pId) {
	// this.pId = pId;
	// }

}