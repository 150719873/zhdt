package com.dotop.smartwater.project.module.core.water.bo;

import java.util.Date;
import java.util.List;

import com.dotop.smartwater.dependence.core.common.BaseBo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 

 * @description 表册绑定抄表员Bo
 * @date 2019-10-22 16:42
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class DeviceBookBindBo extends BaseBo {
	//主键
	private String id;
	//表册号
	private String bookNum;
	//抄表员编号
	private String bookUserId;
	//绑定时间
	private Date bindTime;
	
	//业主ID
	private String ownerId;
	//区域ID
	private String areaId;
	//业主编号
	private String ownerNo;
	//水表编号
	private String deviceNo;
	//电话
	private String phone;
	//是否充值
	private Boolean checked;
	//区域IDs
	private List<String> nodeIds;
}
