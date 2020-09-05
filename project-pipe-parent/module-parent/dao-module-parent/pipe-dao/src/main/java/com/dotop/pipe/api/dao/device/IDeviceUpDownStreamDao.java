package com.dotop.pipe.api.dao.device;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.dotop.pipe.core.dto.decive.DeviceUpDownStreamDto;
import com.dotop.pipe.core.vo.device.DeviceUpDownStreamVo;
import com.dotop.pipe.core.vo.device.DeviceVo;
import com.dotop.smartwater.dependence.core.common.BaseDao;

public interface IDeviceUpDownStreamDao extends BaseDao<DeviceUpDownStreamDto, DeviceUpDownStreamVo> {

    public void add(DeviceUpDownStreamDto deviceUpDownStreamDto) throws DataAccessException;

    public void editAlarmProperty(DeviceUpDownStreamDto deviceUpDownStreamDto) throws DataAccessException;

    Integer del(DeviceUpDownStreamDto deviceUpDownStreamDto);

    List<DeviceVo> getParent(DeviceUpDownStreamDto deviceUpDownStreamDto);

    List<DeviceVo> getChild(DeviceUpDownStreamDto deviceUpDownStreamDto);

    List<DeviceUpDownStreamVo> list(DeviceUpDownStreamDto deviceUpDownStreamDto);

    DeviceVo getForecast(DeviceUpDownStreamDto deviceUpDownStreamDto);
}
