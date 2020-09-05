package com.dotop.smartwater.project.module.core.water.vo;

import com.dotop.smartwater.dependence.core.common.BaseVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class FlowStatusVo extends BaseVo {
//	DELETE(-1 , "已删除");
//	REVOKE(0 , "已撤销");
//	APPLY(1 , "已申请");
//	HANG(2 , "已挂起");
//	HANDLE(3 , "处理中");
//	CLOSE(4 , "已关闭");
//	FILE(5 , "已归档");
	
	public final static int DELETE = -1;
	public final static int REVOKE = 0;
	public final static int APPLY = 1;
	public final static int HANG = 2;
	public final static int HANDLE = 3;
	public final static int CLOSE = 4;
	public final static int FILE = 5;
	
	private int val;
	
	private String text;
}
