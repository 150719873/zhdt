package com.dotop.pipe.api.dao.devicedata;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.dao.DataAccessException;

import com.dotop.pipe.core.form.DeviceForm;
import com.dotop.pipe.core.form.PointForm;

public interface IDeviceDataDao {

	void addPointList(@Param("pointList") List<PointForm> pointList, @Param("operEid") String operEid,
			@Param("userBy") String userBy, @Param("curr") Date curr, @Param("isNotDel") Integer isNotDel,
			@Param("isDel") Integer isDel) throws DataAccessException;

	void addDeviceList(@Param("deviceList") List<DeviceForm> deviceList, @Param("operEid") String operEid,
			@Param("userBy") String userBy, @Param("curr") Date curr, @Param("isNotDel") Integer isNotDel,
			@Param("isDel") Integer isDel) throws DataAccessException;

}
