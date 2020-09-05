package com.dotop.smartwater.project.module.core.water.vo;

import com.dotop.smartwater.dependence.core.common.BaseVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @program: project-parent
 * @description: 销户记录

 * @create: 2019-02-25 19:02
 **/
@Data
@EqualsAndHashCode(callSuper = true)
public class OwnerRecordVo extends BaseVo {
	private String id;
	private String communityid;
	// 类型 1-销户 2-过户 3-换表
	private Integer type;
	private String ownerid;
	private String username;
	private String oldownerid;
	private String oldusername;
	private String olduserphone;
	private String devid;
	private String devno;
	private Double devnum;
	private String olddevid;
	private String olddevno;
	private Double olddevnum;
	private Double oldalreadypay;
	private String operateuser;
	private String operatename;
	private String operatetime;
	private String reason;
	private String descr;
}
