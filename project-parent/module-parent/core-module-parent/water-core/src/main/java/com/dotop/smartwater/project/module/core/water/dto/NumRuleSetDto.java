package com.dotop.smartwater.project.module.core.water.dto;

import com.dotop.smartwater.dependence.core.common.BaseDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 实体类
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class NumRuleSetDto extends BaseDto {

	private String id;

	private Integer ruleid;

	/**号码前缀*/
	private String title;

	/**时间戳格式*/
	private String timesformat;

	/**记录当前的最大值*/
	private String max_value;

	/**1启用 0停用*/
	private Integer status;

	private Date ctime;

	private String name;

}
