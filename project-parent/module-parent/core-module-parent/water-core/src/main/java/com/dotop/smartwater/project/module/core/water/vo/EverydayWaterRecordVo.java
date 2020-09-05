package com.dotop.smartwater.project.module.core.water.vo;

import com.dotop.smartwater.dependence.core.common.BaseVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**

 * @date 2019/2/26.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class EverydayWaterRecordVo extends BaseVo {
	private String communityid;

	private Date ctime;

	private Double water;

	private Double addWater;
}
