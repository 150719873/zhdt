package com.dotop.smartwater.project.module.core.water.vo.customize;

import com.dotop.smartwater.dependence.core.common.BaseVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 
 * @Title: CommunityOwnpay.java
 * @Package com.dotop.water.dc.model.query
 * @Description: 统计当前水司下每个小区欠费情况Vo

 * @date 2018年5月24日 下午2:09:31
 */
// 表不存在
@Data
@EqualsAndHashCode(callSuper = false)
public class CommunityOwnpayVo extends BaseVo {

	private Double ownpay;// 小区欠费金额

	private String communityid;// 小区id

	private String communityname;// 小区名称

}
