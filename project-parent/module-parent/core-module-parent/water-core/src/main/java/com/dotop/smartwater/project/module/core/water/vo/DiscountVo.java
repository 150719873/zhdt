package com.dotop.smartwater.project.module.core.water.vo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.dotop.smartwater.dependence.core.common.BaseVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 优惠活动
 * 

 *
 */
// 表存在
@Data
@EqualsAndHashCode(callSuper = false)
public class DiscountVo extends BaseVo {

	private String id;
	/* 活动名称 */
	private String name;
	/* 活动是否有效 */
	private int isvalid;
	/* 活动方式 1-代金券 */
	private int type;
	/* 有效天数 */
	private int validityday;
	/* 欠费是否生成代金券 */
	private int isconpon;
	/* 是否默认 */
	private int isdefault;
	/* 活动开始时间 */
	private String starttime;
	/* 活动截止时间 */
	private String endtime;
	/* 备注 */
	private String remark;
	/* 创建用户 */
	private String createuser;
	/* 创建用户 */
	private String username;
	/* 用户名称 */
	private String account;

	/* 创建时间 */
	private String createtime;

	public Integer getIsvalid() {
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		int isv = 0;
		try {
			isv = (sf.parse(endtime).getTime() < (new Date()).getTime()) ? 0 : 1;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return isv;
	}

}
