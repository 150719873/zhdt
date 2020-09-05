package com.dotop.smartwater.project.module.core.auth.form;

import com.dotop.smartwater.dependence.core.common.BaseForm;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class AreaForm extends BaseForm {
	
    private String id;

    private String pId;

    private String name;
    
    //区域编号
  	private String code;
}
