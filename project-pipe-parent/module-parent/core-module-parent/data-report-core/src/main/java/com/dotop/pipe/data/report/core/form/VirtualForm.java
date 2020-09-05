package com.dotop.pipe.data.report.core.form;

import java.util.Date;
import java.util.List;

import lombok.Data;

/**
 *
 * @date 2018/11/16.
 */
@Data
public class VirtualForm {

	/**
	 * 正向设备id
	 */
	private List<String> positiveIds;

	/**
	 * 反向设备id
	 */
	private List<String> reverseIds;
	/**
	 * 开始时间
	 */
	private Date startDate;

	/**
	 * 结束时间
	 */
	private Date endDate;
}
