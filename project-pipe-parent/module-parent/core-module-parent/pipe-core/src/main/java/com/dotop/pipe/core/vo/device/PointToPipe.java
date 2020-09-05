package com.dotop.pipe.core.vo.device;

import com.dotop.pipe.core.vo.point.PointVo;

import lombok.Data;

/**
 * 连通性分析 算法映射类
 * 
 *
 *
 */
@Data
public class PointToPipe {

	public PointToPipe() {

	}

	public PointToPipe(String pointId, String pipeId) {
		this.pointId = pointId;
		this.pipeId = pipeId;
	}

	private String pipeId;
	private String pointId;
	private DeviceVo deviceVo;
	private PointVo pointVo;
}
