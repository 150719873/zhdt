package com.dotop.smartwater.project.module.core.water.vo.customize;

import com.dotop.smartwater.dependence.core.common.BaseVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

//表不存在
@Data
@EqualsAndHashCode(callSuper = false)
public class StatisticsWaterVo extends BaseVo {
	private String searchDate;
	private String communityid;
	private String ctime;
	private String communityname;
	private String water;
	private String communityids;

}
