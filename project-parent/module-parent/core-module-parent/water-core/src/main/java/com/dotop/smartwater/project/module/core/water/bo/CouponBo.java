package com.dotop.smartwater.project.module.core.water.bo;

import java.util.Date;
import java.util.List;

import com.dotop.smartwater.dependence.core.common.BaseBo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 优惠券
 */
// 表存在
@Data
@EqualsAndHashCode(callSuper = false)
public class CouponBo extends BaseBo {

	private String couponid; // 优惠券ID
	private String no; // 优惠券编号
	private String name; // 优惠券名称
	private String communityid; // 社区ID
	private String communityname; // 社区名称
	private Integer type; // 优惠券类型，1:代金券,2:抵扣券
	private String discountid; // 活动ID
	private String discountname; // 活动名称
	private Date starttime; // 开始时间
	private Date endtime; // 结束时间
	private Double facevalue; // 面值，保留小数点后两位
	private Integer unit; // 面值单位，1:元,2:吨
	private String bill; // 账单ID
	private Integer status; // 状态，0:未使用(正常状态),1:不可用(未到开始时间),2:已失效,3:已使用，4：已过期
	private String userid; // 用户ID
	private String userno; // 用户编号
	private String username; // 用户名称
	private String remarks; // 备注
	private Integer delflag; // 逻辑删除标识，0:正常，1:删除
	private String createuser; // 创建用户ID
	private String createusername; // 创建用户姓名
	private Date createtime; // 创建时间
	private String updateuser; // 最后修改用户
	private String updateusername; // 最后修改用户姓名
	private Date updatetime; // 最后修改时间

	private Integer addType;
	private String communityids;
	private String usernos;

	// 复合对象(查询用)
	private List<Integer> statuss;

}
