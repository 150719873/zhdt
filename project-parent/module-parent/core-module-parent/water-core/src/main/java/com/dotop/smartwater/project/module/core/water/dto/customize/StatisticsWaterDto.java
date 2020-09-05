package com.dotop.smartwater.project.module.core.water.dto.customize;

import com.dotop.smartwater.dependence.core.common.BaseDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**

 * @date 2019/3/1.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class StatisticsWaterDto extends BaseDto {
	private String searchDate;
	private String communityid;
	private String ctime;
	private String communityname;
	private String water;
	private String communityids;
}
