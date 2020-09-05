package com.dotop.smartwater.project.module.core.water.vo.customize;

import com.dotop.smartwater.dependence.core.common.BaseVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 业主水表信息-账单专用
 */
// 表不存在
@Data
@EqualsAndHashCode(callSuper = false)
public class OwnerDeviceVo extends BaseVo {

	private String ownerid;
	/* 业主编号 */
	private String userno;
	/* 业主名称 */
	private String username;
	/* 用户编号 */
	private String userphone;

	private String cardid;

	/* 用户地址 */
	private String useraddr;
	/* 业主状态 */
	private String status;
	/* 收费ID */
	private String paytypeid;

	/* 所属区域ID */
	private String communityid;

	/* 设备EUI */
	private String deveui;
	/* 设备编号 */
	private String devno;
	/* 设备状态 */
	private Integer devStatus;
	/* 状态说明 */
	private String explain;

	/* 水表类型:0:不带阀 1:带阀 */
	private Integer taptype;
	/* 阀门状态 */
	private Integer tapstatus;

	/* 水表初始读数 */
	private Double beginvalue;

	/* 小区名 */
	private String comname;

	/* 小区编号 */
	private String comno;

	/* 收费种类名 */
	private String typename;

	private String devid;

	private String reduceid;

	private String purposeid;

	/* 上级水表ID */
	private String pid;

	private Date uplinktime;

	@Override
	public String toString() {
		return "OwnerDevice [userno=" + userno + ", username=" + username + ", userphone=" + userphone + ", useraddr="
				+ useraddr + ", status=" + status + ", paytypeid=" + paytypeid + ", communityid=" + communityid
				+ ", deveui=" + deveui + ", devno=" + devno + ", devStatus=" + devStatus + ", explain=" + explain + "]";
	}

}
