package com.dotop.pipe.core.form;

import java.util.Date;
import java.util.List;

import com.dotop.smartwater.dependence.core.common.pipe.BasePipeForm;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class LeakageAnalysisForm extends BasePipeForm {

	// select,选择;area,区域;
	private String type;

	// 流量计对应的字段 暂时可指定为 flwMeasure
	private String field;

	private List<LeakageAnalysisItemForm> items; // 选中的点的集合

	private Date startDate; // 开始时间

	private Date endDate; // 结束时间

}
