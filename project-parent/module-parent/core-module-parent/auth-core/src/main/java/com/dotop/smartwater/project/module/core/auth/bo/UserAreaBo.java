package com.dotop.smartwater.project.module.core.auth.bo;

import java.util.List;

import com.dotop.smartwater.dependence.core.common.BaseBo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class UserAreaBo extends BaseBo {
	public String userid;

	public List<String> areaids;
}
