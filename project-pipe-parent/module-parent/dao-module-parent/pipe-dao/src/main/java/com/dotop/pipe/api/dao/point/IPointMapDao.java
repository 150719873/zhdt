package com.dotop.pipe.api.dao.point;

import java.util.Date;
import java.util.List;

import com.dotop.pipe.core.form.PointForm;
import org.apache.ibatis.annotations.Param;
import org.springframework.dao.DataAccessException;

import com.dotop.pipe.core.dto.point.PointMapDto;
import com.dotop.pipe.core.vo.point.PointMapVo;
import com.dotop.smartwater.dependence.core.common.BaseDao;

// 坐标与设备关系
public interface IPointMapDao extends BaseDao<PointMapDto, PointMapVo> {

	// 新增坐标与设备关系
	public void add(PointMapDto pointMapDto) throws DataAccessException;

	// 删除坐标与设备关系
	public Integer del(PointMapDto pointMapDto) throws DataAccessException;

	// 批量删除坐标与设备关系
	// public Integer dels(PointMapDto pointMapDto) throws DataAccessException;

	// 是否存在坐标关系
	public Boolean isExist(PointMapDto pointMapDto) throws DataAccessException;

	public Boolean isExistByDeviceId(PointMapDto pointMapDto) throws DataAccessException;

	public void addList(@Param("operEid") String operEid, @Param("customizeId") String customizeId,
			@Param("curr") Date curr, @Param("userBy") String userBy, @Param("isDel") Integer isDel,
			@Param("points") List<PointForm> points);

	public void delTables(@Param("operEid") String enterpriseId, @Param("deviceId") String deviceId,
			@Param("curr") Date curr, @Param("userBy") String userBy, @Param("isDel") Integer isDel);

	@Override
	List<PointMapVo> list(PointMapDto pointMapDto) throws DataAccessException;
}
