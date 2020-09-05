package com.dotop.smartwater.project.module.core.auth.vo;

import com.dotop.smartwater.dependence.core.common.BaseVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class UserOperationRecordVo extends BaseVo {
	private String id;
	private String operateuser;
	private String operate;
	private String operatetime;
	private String userid;
	private String enterpriseid;
	private String operateusername;
}
