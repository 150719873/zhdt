package com.dotop.pipe.core.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dotop.pipe.core.vo.device.DeviceVo;
import com.dotop.pipe.core.vo.device.DijkstraVo;
import com.dotop.pipe.core.vo.device.PointToPipe;
import com.dotop.pipe.core.vo.point.PointVo;

public class DijkstraForthTUtils {

	/**
	 * 把管网数据转换成类似邻接矩阵格式
	 * 
	 * @return
	 */
	public static Map<String, List<PointToPipe>> toChange(List<DeviceVo> list) {
		Map<String, List<PointToPipe>> nodeMap = new HashMap<String, List<PointToPipe>>();
		for (DeviceVo deviceVo : list) {
			String pointId1 = deviceVo.getPoints().get(0).getPointId();
			String pointId2 = deviceVo.getPoints().get(1).getPointId();
			String pipeId = deviceVo.getDeviceId();
			if (nodeMap.containsKey(pointId1)) {
				PointToPipe pointToPipe = new PointToPipe(pointId2, pipeId);
				pointToPipe.setDeviceVo(deviceVo);
				pointToPipe.setPointVo(deviceVo.getPoints().get(1));
				nodeMap.get(pointId1).add(pointToPipe);
			} else {
				PointToPipe pointToPipe = new PointToPipe(pointId2, pipeId);
				pointToPipe.setDeviceVo(deviceVo);
				pointToPipe.setPointVo(deviceVo.getPoints().get(1));
				List _list = new ArrayList<PointToPipe>();
				_list.add(pointToPipe);
				nodeMap.put(pointId1, _list);
			}

			if (nodeMap.containsKey(pointId2)) {
				PointToPipe pointToPipe = new PointToPipe(pointId1, pipeId);
				pointToPipe.setDeviceVo(deviceVo);
				pointToPipe.setPointVo(deviceVo.getPoints().get(0));
				nodeMap.get(pointId2).add(pointToPipe);
			} else {
				PointToPipe pointToPipe = new PointToPipe(pointId1, pipeId);
				pointToPipe.setDeviceVo(deviceVo);
				pointToPipe.setPointVo(deviceVo.getPoints().get(0));
				List _list = new ArrayList<PointToPipe>();
				_list.add(pointToPipe);
				nodeMap.put(pointId2, _list);
			}

		}
		return nodeMap;

	}

	public static DijkstraVo allPathBetweenTwoPoints(Map<String, List<PointToPipe>> pipeOneToMany,
			String _startPointId, String _endPointId) {
		// 访问过的点
		List<String> visitPointIdList = new ArrayList<>();
		// 访问过的边的集合
		List<String> visitAllPipeList = new ArrayList<>();
		// 所有可能的路径
		List<List<String>> paths = new ArrayList<>();
		allPathsBetweenTwoPointsSearch(pipeOneToMany, _startPointId, _endPointId, visitPointIdList, visitAllPipeList,
				paths);
		// System.out.println(paths);

		List<List<DeviceVo>> pipePaths = new ArrayList<>();
		List<List<PointVo>> points = new ArrayList<>();
		
		// 遍历所有的点 找出最短的三条路径
		for (List<String> _path : paths) {
			// 存放某条路的路径
			List<DeviceVo> tempPath = new ArrayList<>();
			List<PointVo> tempPoints = new ArrayList<>();
			_path.add(_endPointId);
			if (_path.size() > 1) {
				Double length = 0.0;
				for (int i = 1; i < _path.size(); i++) {
					// 根据点找到pipe
					String point1 = _path.get(i - 1);
					String point2 = _path.get(i);
					for (PointToPipe _value : pipeOneToMany.get(point1)) {
						if (point2.equals(_value.getPointId())) {
							// 长度
							tempPath.add(_value.getDeviceVo());
							tempPoints.add(_value.getPointVo());
							if (_value.getDeviceVo().getLength() != null
									&& !"".equals(_value.getDeviceVo().getLength())) {
								length = length + Double.valueOf(_value.getDeviceVo().getLength());
							} else {
								DeviceVo device = _value.getDeviceVo();
								String oriPipeLength = PipeLengthUtils.getPipeLength(
										device.getPoints().get(0).getLongitude().toString(),
										device.getPoints().get(0).getLatitude().toString(),
										device.getPoints().get(1).getLongitude().toString(),
										device.getPoints().get(1).getLatitude().toString());
								length = length + Double.valueOf(oriPipeLength);
							}
							break;
						}
					}
				}
				if (length == 0.0) {
					continue;
				}

				// System.out.println(String.valueOf(length));
				DeviceVo deviceVo = new DeviceVo();
				deviceVo.setLength(String.valueOf(length));
				tempPath.add(0, deviceVo);
				if (pipePaths.size() == 0) {
					pipePaths.add(tempPath);
					points.add(tempPoints);
				} else {
					for (int j = 0; j < pipePaths.size() && j < 3; j++) {
						Double _length = Double.valueOf(pipePaths.get(j).get(0).getLength());
						if (length < _length) {
							pipePaths.add(j, tempPath);
							points.add(j,tempPoints);
							break;
						} else {
							if (pipePaths.size() == 1) {
								pipePaths.add(tempPath);
								points.add(tempPoints);
								break;
							}
							if (pipePaths.size() == 2 && j == 1) {
								// 插入排名第三的
								pipePaths.add(tempPath);
								points.add(tempPoints);
								break;
							}
						}

					}
				}
			}
		}
		// System.out.println(pipePaths);
		DijkstraVo dijkstraVo = new DijkstraVo();
		dijkstraVo.setPaths(pipePaths);
		dijkstraVo.setPoints(points);
		return dijkstraVo;
	}

	public static void allPathsBetweenTwoPointsSearch(Map<String, List<PointToPipe>> pipeOneToMany,
			String _startPointId, String _endPointId, List<String> _visitPointIdList, List<String> _visitAllPipeList,
			List<List<String>> _paths) {

		// System.out.println(_visitPointIdList);
		List<PointToPipe> pipeList = pipeOneToMany.get(_startPointId);

		if (pipeList == null || pipeList.isEmpty()) {
			return;
		}
		_visitPointIdList.add(_startPointId); // 起始点进栈

		for (int i = 0; i < pipeList.size(); i++) {
			// 看点有没有进栈
			PointToPipe _value = pipeList.get(i);
			if (_visitPointIdList.size() > 20) {
				_visitPointIdList.remove(_visitPointIdList.size() - 1);
				return;
			}
			boolean pointIndex = _visitPointIdList.contains(_value.getPointId());
			if (pointIndex) {
				if (i == pipeList.size() - 1) {
					// 表示集合中的点或者边都已经遍历过了 没有新的点遍历 此时需要将以遍历过的点出栈 出栈顶第一个
					_visitPointIdList.remove(_visitPointIdList.size() - 1);
					// System.out.println("此点存在 出栈"+_value.getPointId());
					// System.out.println(_visitPointIdList);
					return;
				} else {
					continue;
				}
			}
			// 访问的边进栈
			_visitAllPipeList.add(_value.getPipeId());
			if (_endPointId.equals(_value.getPointId())) {
				// 代表找到了终点 结束此次循环
				List<String> _pathList = new ArrayList<>();
				_pathList.addAll(_visitPointIdList);
				_paths.add(_pathList);
				// System.out.println(_visitPointIdList);
				if (i == pipeList.size() - 1) {
					// 此次遍历已经没有未访问过的节点 退出此次遍历 栈顶数据出栈
					_visitPointIdList.remove(_visitPointIdList.size() - 1);
					return;
				} else { // 找到了一条边
					continue;
				}
			} else {
				// 没有找到终点 继续深度优先遍历 开始递归遍历 此时的__value.pointId 作为新遍历的 _startPointId
				allPathsBetweenTwoPointsSearch(pipeOneToMany, _value.getPointId(), _endPointId, _visitPointIdList,
						_visitAllPipeList, _paths);
				// 栈顶出栈
				if (i == pipeList.size() - 1) {
					_visitPointIdList.remove(_visitPointIdList.size() - 1);
				}
			}
		}
	}
}
