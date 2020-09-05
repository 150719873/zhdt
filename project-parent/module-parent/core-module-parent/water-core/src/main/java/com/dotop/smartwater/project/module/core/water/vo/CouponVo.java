package com.dotop.smartwater.project.module.core.water.vo;

import java.util.Date;

import com.dotop.smartwater.dependence.core.common.BaseVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

//表存在
@Data
@EqualsAndHashCode(callSuper = false)
public class CouponVo extends BaseVo {

	private String couponid; // 优惠券ID
	private String no; // 优惠券编号
	private String name; // 优惠券名称
	private String communityid; // 社区ID
	private String communityname; // 社区名称
	private Integer type; // 优惠券类型，1:代金券,2:抵扣券
	private String discountid; // 活动ID
	private String discountname; // 活动名称
	private Date starttime; // 活动开始时间
	private Date endtime; // 活动结束时间
	private Double facevalue; // 面值，保留小数点后两位
	private Integer unit; // 面值单位，1:元,2:吨
	private String bill; // 账单ID
	private Integer status; // 状态，0:有效(正常状态),1:不可用(未到活动开始时间),2:已失效,3:已使用，4：已过期
	private String userid; // 用户ID
	private String userno; // 用户编号
	private String username; // 用户名称
	private String remarks; // 备注
	private String createuser; // 创建用户
	private String createtime; // 创建时间
	private String updateuser; // 最后修改用户
	private String updatetime; // 最后修改时间
	private String delflag; // 逻辑删除标识，0:正常，1:删除
	// public CouponVo() {
	// super();
	// }

	// public CouponVo(CouponVo couponVo) {
	// BeanUtils.copyProperties(couponVo, this);
	// this.setCreateuser(couponVo.getCreateusername());
	// this.setUpdateuser(couponVo.getUpdateusername());
	//
	// SimpleDateFormat validityFormat = new SimpleDateFormat("yyyy-MM-dd");
	// this.setStarttime(validityFormat.format(couponVo.getStarttime()));
	// this.setEndtime(validityFormat.format(couponVo.getCreatetime()));
	//
	// SimpleDateFormat updateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	// this.setUpdatetime(updateFormat.format(couponVo.getUpdatetime()));
	// this.setCreatetime(updateFormat.format(couponVo.getCreatetime()));
	// }

}
