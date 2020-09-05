package com.dotop.smartwater.project.module.core.water.vo.customize;

import com.dotop.smartwater.dependence.core.common.BaseVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

//表不存在
@Data
@EqualsAndHashCode(callSuper = false)
public class WechatAllocationVo extends BaseVo {

	private String id;
	private String enterprisename;
	private String name;
	private String smsptid;
	private String content;
	private Integer status;
	private String remarks;
	private String createuser;
	private String createtime;
	private String updateuser;
	private String updatetime;
	private Integer printstatus;
	private Integer smstype;
	private String smstypename;

}
