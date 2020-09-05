package com.dotop.pipe.core.form;

import java.util.Date;
import java.util.List;

import com.dotop.pipe.core.enums.DateTypeEnum;
import com.dotop.pipe.core.enums.FieldTypeEnum;
import com.dotop.smartwater.dependence.core.common.pipe.BasePipeForm;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 *
 * @date 2018/11/12.
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class DeviceDataForm extends BasePipeForm {
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
}
