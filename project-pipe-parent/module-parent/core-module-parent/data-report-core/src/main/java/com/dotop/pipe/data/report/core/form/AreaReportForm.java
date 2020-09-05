package com.dotop.pipe.data.report.core.form;

import com.dotop.pipe.core.enums.DateTypeEnum;
import com.dotop.smartwater.dependence.core.common.pipe.BasePipeForm;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Date;
import java.util.List;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class AreaReportForm extends BasePipeForm {

	// 开始时间
	private Date startDate;
	// 结束时间
	private Date endDate;
	// 区域id
	private String areaId;
	private List<String> areaIds;
	// 请求的时间类型
	private DateTypeEnum dateType;
}
