package com.dotop.smartwater.project.module.core.water.vo;

import java.util.Date;

import com.dotop.smartwater.dependence.core.common.BaseVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 

 * @description 表册绑定抄表员Vo
 * @date 2019-10-22 16:42
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class DeviceBookBindVo extends BaseVo {
	
	//mode值为该值时默认将抄表员清空
	public static final String DEFAULT_WIPE_DATA = "default_wipe_data";
		
	//主键
	private String id;
	//表册号
	private String bookNum;
	//抄表员编号
	private String bookUserId;
	//绑定时间
	private Date bindTime;
	//工号
	private String workNum;
	//姓名
	private String name;
	//电话
	private String phone;
	
	//业主ID
	private String ownerId;
	//区域ID
	private String areaName;
	//业主编号
	private String ownerNo;
	//水表编号
	private String deviceNo;
	//是否选中
	private Boolean checked;
}
