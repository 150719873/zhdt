package com.dotop.smartwater.project.module.core.water.vo.customize;

import com.dotop.smartwater.dependence.core.common.BaseVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 
 * @Title: CommunityDeviceCount.java
 * @Package com.dotop.water.dc.model.query
 * @Description: 当前水司下每个小区设备统计Vo

 * @date 2018年5月23日 下午3:43:25
 */
// 表不存在
@Data
@EqualsAndHashCode(callSuper = false)
public class CommunityDeviceCountVo extends BaseVo {

	private String communityid;// 小区id

	private Integer count;// 设备数量

	private String name;// 小区名称

}
