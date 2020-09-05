package com.dotop.smartwater.project.module.core.water.vo.customize;

import com.dotop.smartwater.dependence.core.common.BaseVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 打印模板-SQL对应字段
 * 

 *
 */
// 表不存在
@Data
@EqualsAndHashCode(callSuper = false)
public class DesignFieldVo extends BaseVo {

	private String id;
	/* 模板ID */
	private String designid;
	/* 字段 */
	private String field;

}
