package com.dotop.pipe.core.vo.device;

import java.util.List;

import com.dotop.pipe.core.vo.point.PointVo;

import lombok.Data;

/**
 * 连通性分析 算法映射类
 * 
 *
 *
 */
@Data
public class DijkstraVo {
	private List<List<DeviceVo>> paths;  //经过的
	private List<List<PointVo>>  points; // 经过的点的集合
}
