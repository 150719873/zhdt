package com.dotop.smartwater.project.module.core.water.vo;
/**

 * @date 2018/11/21.
 */

import java.util.List;

import com.dotop.smartwater.dependence.core.common.BaseVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class TreeNode<T> extends BaseVo {
	protected String title;
	protected String key;
	protected boolean isLeaf;
	protected boolean isExpanded;
	protected boolean isSelectable;
	protected List<T> children;
	protected T parentNode;
}
