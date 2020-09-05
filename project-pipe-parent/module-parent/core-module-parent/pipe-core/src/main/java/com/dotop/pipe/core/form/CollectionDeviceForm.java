package com.dotop.pipe.core.form;

import com.dotop.smartwater.dependence.core.common.pipe.BasePipeForm;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CollectionDeviceForm extends BasePipeForm {
	private String id;
	private String deviceId;
	private String userId;
	private String type;
}
