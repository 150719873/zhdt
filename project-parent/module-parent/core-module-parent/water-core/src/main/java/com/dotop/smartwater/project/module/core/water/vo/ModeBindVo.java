package com.dotop.smartwater.project.module.core.water.vo;

import com.dotop.smartwater.dependence.core.common.BaseVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**

 * @description 通讯方式配置Vo
 * @date 2019年10月17日 15:45
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ModeBindVo extends BaseVo {
	
	//mode值为该值时默认将通讯方式配置清空
	public static final String DEFAULT_WIPE_DATA = "default_wipe_data";
	
	// 主键
	private String id;
	// 通讯方式（字典ID）
	private String mode;
	// 通讯方式名称
	private String modeName;
}
