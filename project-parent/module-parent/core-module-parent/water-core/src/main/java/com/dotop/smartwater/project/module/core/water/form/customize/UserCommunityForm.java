package com.dotop.smartwater.project.module.core.water.form.customize;

import com.dotop.smartwater.dependence.core.common.BaseForm;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 原名UserCommunityParam
 */
// 表不存在
@Data
@EqualsAndHashCode(callSuper = false)
public class UserCommunityForm extends BaseForm {

	public String userid;

	public String communityids;

}
