package com.dotop.smartwater.project.module.core.water.vo.customize;

import java.util.List;

import com.dotop.smartwater.dependence.core.common.BaseVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 同menu
 */
// 表不存在
@Data
@EqualsAndHashCode(callSuper = false)
public class NodeTreeVo extends BaseVo {

	private String title;

	private String key;

	private Boolean checked;

	private Boolean selected;

	private String parentid;

	private List<NodeTreeVo> children;

}
