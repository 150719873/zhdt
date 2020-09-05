package com.dotop.smartwater.project.module.core.auth.vo;

import java.util.List;

import com.dotop.smartwater.dependence.core.common.BaseVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class UserAreaVo extends BaseVo {
	public String userid;

	public List<String> areaids;
}
