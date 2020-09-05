package com.dotop.smartwater.project.module.core.auth.vo;

import java.util.List;

import com.dotop.smartwater.dependence.core.common.BaseVo;

import lombok.Data;
import lombok.EqualsAndHashCode;
@Data
@EqualsAndHashCode(callSuper = false)
public class AreaListVo extends BaseVo {
	private String enterpriseid;

	private List<AreaVo> list;
}
