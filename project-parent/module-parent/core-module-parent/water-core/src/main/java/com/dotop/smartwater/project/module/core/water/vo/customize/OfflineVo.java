package com.dotop.smartwater.project.module.core.water.vo.customize;

import com.dotop.smartwater.dependence.core.common.BaseVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 
 */
// 表不存在
@Data
@EqualsAndHashCode(callSuper = false)
public class OfflineVo extends BaseVo {

	private String id;
	private String userno;
	private String username;
	private String useraddr;
	private String explain;
	private String devno;
	private String water;
	private String phone;
	private String status;
	private String uplinktime;
	private String rssi;
	private Double lsnr;
	private String type;

}
