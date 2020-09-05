package com.dotop.smartwater.project.module.core.water.bo.customize;

import com.dotop.smartwater.dependence.core.common.BaseBo;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**

 * @date 2019/2/28.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class StatisticsWaterBo extends BaseBo {
	private String searchDate;
	private String communityid;
	private String ctime;
	private String communityname;
	private String water;
	private String communityids;
}
