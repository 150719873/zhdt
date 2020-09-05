package com.dotop.pipe.web.api.factory.alarm;

import com.dotop.pipe.core.form.AlarmForm;
import com.dotop.pipe.core.form.AreaForm;
import com.dotop.pipe.core.vo.alarm.AlarmVo;
import com.dotop.pipe.core.vo.device.DeviceVo;
import com.dotop.smartwater.dependence.core.common.BaseFactory;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;

import java.util.List;
import java.util.Map;

public interface IAlarmFactory extends BaseFactory<AreaForm, AlarmVo> {

    /**
     * 报警分页查询
     *
     * @param page
     * @param pageSize
     * @return
     * @throws FrameworkRuntimeException
     */
    Pagination<AlarmVo> page(AlarmForm alarmForm) throws FrameworkRuntimeException;

    /**
     * 报警分页查询
     *
     * @param page
     * @param pageSize
     * @return
     * @throws FrameworkRuntimeException
     */
    Pagination<AlarmVo> list(AlarmForm alarmForm) throws FrameworkRuntimeException;

    /**
     * 报警详情查询
     *
     * @param alarmId
     * @return
     * @throws FrameworkRuntimeException
     */
    AlarmVo get(AlarmForm alarmForm) throws FrameworkRuntimeException;

    /**
     * 报警处理
     *
     * @param sensorForm
     * @return
     * @throws FrameworkRuntimeException
     */
    AlarmVo edit(AlarmForm alarmForm) throws FrameworkRuntimeException;

    /**
     * 新增报警
     *
     * @param name     警报名称
     * @param des      警报描述
     * @param sensorId 传感器id
     * @param status   警报状态
     * @param userBy
     * @return
     * @throws FrameworkRuntimeException
     */
    String add(String name, String des, String sensorId, Integer status, String operEid, String userBy)
            throws FrameworkRuntimeException;

    /**
     * 警告处理 结合 预警设置
     *
     * @param deviceId
     * @param enterpriseId
     * @param devicePropertyLogMap
     * @return
     */
    String add(String deviceId, String enterpriseId, Map<String, Object> devicePropertyLogMap);

    /**
     * 获取即将报警的设备
     *
     * @return
     */
    List<DeviceVo> getNearAlarm();

    /**
     * 根据设备的最新数据值 和设置的预警值范围  查找预警的设备
     *
     * @return
     */
    List<DeviceVo> getAlarmByData(String productCategory);
}
