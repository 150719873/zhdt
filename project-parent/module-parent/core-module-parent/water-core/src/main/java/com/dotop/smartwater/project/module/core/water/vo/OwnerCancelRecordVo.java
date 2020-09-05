package com.dotop.smartwater.project.module.core.water.vo;

import com.dotop.smartwater.dependence.core.common.BaseVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**

 * @date 2019/2/27.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class OwnerCancelRecordVo extends BaseVo {

	private String id;

	private String communityid;

	private String ownerid;

	private String operateuser;

	private Date operatetime;
}
