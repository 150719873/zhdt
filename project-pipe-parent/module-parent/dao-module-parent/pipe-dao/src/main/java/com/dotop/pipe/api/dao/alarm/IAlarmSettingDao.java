package com.dotop.pipe.api.dao.alarm;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.dao.DataAccessException;

import com.dotop.pipe.core.bo.alarm.AlarmSettingBo;
import com.dotop.pipe.core.dto.alarm.AlarmSettingDto;
import com.dotop.pipe.core.dto.decive.DeviceDto;
import com.dotop.pipe.core.vo.alarm.AlarmSettingVo;
import com.dotop.pipe.core.vo.device.DeviceVo;
import com.dotop.smartwater.dependence.core.common.BaseDao;

public interface IAlarmSettingDao extends BaseDao<AlarmSettingDto, AlarmSettingVo> {

    void add(AlarmSettingDto alarmSettingDto) throws DataAccessException;

    Integer del(AlarmSettingDto alarmSettingDto) throws DataAccessException;

    List<AlarmSettingVo> gets(AlarmSettingDto alarmSettingDto) throws DataAccessException;

    /**
     * 设备和预警关联的 分页查询
     *
     * @param deviceDto
     * @return
     */
    List<DeviceVo> page(DeviceDto deviceDto);

    /**
     * 区域预警列表
     *
     * @param deviceDto
     * @return
     */
    List<DeviceVo> areaAlarmSettingPage(DeviceDto deviceDto);

    void delList(List<AlarmSettingBo> list);

    void addList(@Param("list") List<AlarmSettingBo> list, @Param("userBy") String userBy, @Param("curr") Date curr,
                 @Param("enterpriseId") String enterpriseId, @Param("isDel") Integer isDel);

}
