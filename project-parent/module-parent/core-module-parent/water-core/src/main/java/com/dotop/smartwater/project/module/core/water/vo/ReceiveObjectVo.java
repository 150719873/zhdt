package com.dotop.smartwater.project.module.core.water.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 * 通知接收对象
 * 

 * @date 2019-03-19 16:20
 *
 */
//表不存在
@Data
@EqualsAndHashCode(callSuper = false)
public class ReceiveObjectVo {
	private String id;
	private String name;
	private String contact;//电话号码/邮箱
}
