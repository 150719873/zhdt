package com.dotop.pipe.core.vo.device;

import java.util.List;

import com.dotop.pipe.core.vo.area.AreaModelVo;
import com.dotop.smartwater.dependence.core.common.pipe.BasePipeVo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class DeviceUpDownStreamVo extends BasePipeVo {

	/**
	 * 关联关系id
	 */
	private String id;

	/**
	 * 当前设备
	 */
	private DeviceVo deviceVo;

	/**
	 * 设备 的父关联关系集合
	 */

	private List<DeviceVo> deviceParentList;

	/**
	 * 上游数量
	 */
	private Integer parentCount;

	/**
	 * 设备的 子关联关系集合
	 */
	private List<DeviceVo> deviceChildList;

	/**
	 * 下游数量
	 */
	private Integer childrenCount;
	
	private AreaModelVo area;
}
