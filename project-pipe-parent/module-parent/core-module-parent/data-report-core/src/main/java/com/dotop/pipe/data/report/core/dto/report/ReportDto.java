package com.dotop.pipe.data.report.core.dto.report;

import java.util.Date;
import java.util.List;
import java.util.Set;

import com.dotop.pipe.core.enums.DateTypeEnum;
import com.dotop.pipe.core.enums.FieldTypeEnum;
import com.dotop.smartwater.dependence.core.common.pipe.BasePipeDto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *
 * @date 2018/11/2.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ReportDto extends BasePipeDto {
	/**
	 * 所属区域id集合
	 */
	private List<String> areaIds;
	/**
	 * 设备id集合
	 */
	private List<String> deviceIds;
	/**
	 * 日期类型
	 */
	private DateTypeEnum dateType;
	/**
	 * 数据类型
	 */
	private FieldTypeEnum[] fieldType;
	/**
	 * 开始时间
	 */
	private Date startDate;

	/**
	 * 结束时间
	 */
	private Date endDate;

	private String ctime; // = "201811,201812";

	private Set<String> ctimes;
}
