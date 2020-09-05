package com.dotop.pipe.api.service.alarm;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.dotop.pipe.core.bo.alarm.AlarmSettingBo;
import com.dotop.pipe.core.bo.device.DeviceBo;
import com.dotop.pipe.core.vo.alarm.AlarmSettingVo;
import com.dotop.pipe.core.vo.device.DeviceVo;
import com.dotop.smartwater.dependence.core.common.BaseService;
import com.dotop.smartwater.dependence.core.pagination.Pagination;

// 报警
public interface IAlarmSettingService extends BaseService<AlarmSettingBo, AlarmSettingVo> {

    /**
     * 预警值设置 新增
     */
    AlarmSettingVo add(AlarmSettingBo alarmSettingBo);

    List<AlarmSettingVo> gets(AlarmSettingBo alarmSettingBo);

    /**
     * 查询非区域的预警设置信息
     *
     * @param deviceBo
     * @return
     */
    Pagination<DeviceVo> page(DeviceBo deviceBo);

    /**
     * 查询区域的 预警设置信息
     *
     * @param deviceBo
     * @return
     */
    Pagination<DeviceVo> areaAlarmSettingPage(DeviceBo deviceBo);

    /**
     * 预警设置查询详情
     */
    AlarmSettingVo get(AlarmSettingBo alarmSettingBo);

    void importData(Map<String, AlarmSettingBo> propertiesMap, String userBy, Date curr, String enterpriseId);
}
