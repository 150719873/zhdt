package com.dotop.smartwater.project.module.core.water.vo.customize;

import com.dotop.smartwater.dependence.core.common.BaseVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 销户记录查询 CancelExportCreator
 * 

 *
 */
// 表不存在
@Data
@EqualsAndHashCode(callSuper = false)
public class CancelVo extends BaseVo {

	private String id;
	private String community;
	private String useraddr;
	private String userphone;
	private String username;
	private String userno;
	private String operateuser;
	private String type;
	private String devno;
	private String operatetime;

}
