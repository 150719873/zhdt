package com.dotop.smartwater.project.module.core.water.form.customize;

import java.util.List;

import com.dotop.smartwater.dependence.core.common.BaseForm;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 批量修改业主信息(收费种类、用途、型号等) batchUpdateOwner 原名BatchOwnerParam
 * 
 */
// 表不存在
@Data
@EqualsAndHashCode(callSuper = false)
public class BatchOwnerForm extends BaseForm {

	/**
	 * 区域id communityid
	 */
	private List<String> nodeIds;

	private Boolean ischargebacks;

	private String paytypeid;

	private String purposeid;

	private String reduceid;

	private String modelid;

}