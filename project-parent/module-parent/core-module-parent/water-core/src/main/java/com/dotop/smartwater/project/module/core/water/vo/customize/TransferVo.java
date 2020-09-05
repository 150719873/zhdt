package com.dotop.smartwater.project.module.core.water.vo.customize;

import com.dotop.smartwater.dependence.core.common.BaseVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

// 表不存在
@Data
@EqualsAndHashCode(callSuper = false)
public class TransferVo extends BaseVo {

	private String id;
	private String community;
	private String oldusername;
	private String olduserphone;
	private String username;
	private String userphone;
	private String operateuser;

	private String operatetime;
	private String reason;

}
