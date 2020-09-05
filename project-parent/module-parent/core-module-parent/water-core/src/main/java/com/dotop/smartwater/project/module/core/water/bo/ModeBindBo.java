package com.dotop.smartwater.project.module.core.water.bo;

import com.dotop.smartwater.dependence.core.common.BaseBo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**

 * @description 通讯方式配置Bo
 * @date 2019年10月17日 15:45
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ModeBindBo extends BaseBo {
	// 主键
	private String id;
	// 通讯方式（字典ID）
	private String mode;
	// 通讯方式名称
	private String modeName;
	// 父节点
	private String pid;
}
