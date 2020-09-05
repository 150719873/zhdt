package com.dotop.smartwater.project.module.core.water.vo;

import com.dotop.smartwater.dependence.core.common.BaseVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户操作记录

 * @date 2019/2/25.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class UserOperationRecordVo extends BaseVo {
	/**
	 * id
	 */
	private String id;
	/**
	 * 操作人账号
	 */
	private String operateuser;
	/**
	 * 操作
	 */
	private String operate;
	/**
	 * 操作时间
	 */
	private String operatetime;
	/**
	 * 用户id
	 */
	private String userid;
	/**
	 * 操作人名称
	 */
	private String operateusername;

	/**
	 * 操作人ip
	 */
	private String ip;

	/**
	 * 操作类型
	 */
	private String operatetype;
}
