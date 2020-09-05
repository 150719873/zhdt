package com.dotop.pipe.api.service.point;

import java.util.Date;
import java.util.List;

import com.dotop.pipe.core.dto.point.PointMapDto;
import com.dotop.pipe.core.form.PointForm;
import com.dotop.pipe.core.vo.point.PointMapVo;
import com.dotop.smartwater.dependence.core.common.BaseBo;
import com.dotop.smartwater.dependence.core.common.BaseService;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;

// 坐标与设备关系
public interface IPointMapService extends BaseService<BaseBo, PointMapVo> {

	// 新增坐标与设备关系
	public void add(String enterpriseId, String pointId, String deviceId, Date curr, String userBy)
			throws FrameworkRuntimeException;

	// 删除坐标与设备关系
	public void del(String enterpriseId, String deviceId, String pointId, Date curr, String userBy)
			throws FrameworkRuntimeException;

	// 批量删除坐标与设备关系
	public void del(String enterpriseId, List<String> deviceIds, List<String> pointIds, Date curr, String userBy)
			throws FrameworkRuntimeException;

	// 是否存在坐标关系(用于判断坐标是否可以被删除)
	public Boolean isExist(String enterpriseId, String pointId) throws FrameworkRuntimeException;

	// 判断坐标和设备id是否存在
	public Boolean isExistByDeviceId(String enterpriseId, String pointId, String deviceId)
			throws FrameworkRuntimeException;

	public void addList(String operEid, String customizeId, Date curr, String userBy, List<PointForm> points);

	/**
	 * 多表关联删除数据
	 * 
	 * @param operEid
	 * @param areaId
	 * @param curr
	 * @param userBy
	 */
	public void delTables(String enterpriseId, String deviceId, Date curr, String userBy);

	List<PointMapVo> list(PointMapDto pointMapDto) throws FrameworkRuntimeException;
}
