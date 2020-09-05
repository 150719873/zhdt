package com.dotop.smartwater.project.module.core.auth.vo;

import com.dotop.smartwater.dependence.core.common.BaseVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class AreaNodeVo extends BaseVo {
	private String key;
	private String code;
	private String pId;
	private String title;
	private boolean isLeaf;
	private List<AreaNodeVo> children;

	//兼容新版的功能 KJR
	private boolean expanded = false;
	private String systype;

	private boolean checked;

	public void setIsLeaf(boolean isLeaf){
		this.isLeaf = isLeaf;
	}

	public boolean getIsLeaf(){
		return this.isLeaf;
	}
}
