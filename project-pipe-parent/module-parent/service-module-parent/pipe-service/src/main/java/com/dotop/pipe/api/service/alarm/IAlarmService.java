package com.dotop.pipe.api.service.alarm;

import java.util.Map;

import com.dotop.pipe.core.bo.alarm.AlarmBo;
import com.dotop.pipe.core.vo.alarm.AlarmVo;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;

// 报警
public interface IAlarmService {

    /**
     * 报警分页查询
     *
     * @param enterpriseId
     * @param page
     * @param pageSize
     * @param string
     * @return
     * @throws FrameworkRuntimeException
     */
    Pagination<AlarmVo> page(AlarmBo alarmBo) throws FrameworkRuntimeException;


    /**
     * 区域报警信息
     *
     * @param alarmBo
     * @return
     * @throws FrameworkRuntimeException
     */
    Pagination<AlarmVo> areaPage(AlarmBo alarmBo) throws FrameworkRuntimeException;

    /**
     * 报警详情查询
     *
     * @param enterpriseId
     * @param alarmId
     * @return
     * @throws FrameworkRuntimeException
     */
    AlarmVo get(AlarmBo alarmBo) throws FrameworkRuntimeException;

    /**
     * 报警处理
     *
     * @param enterpriseId
     * @param alarmId
     * @param status
     * @param processResult
     * @param date
     * @param userBy
     * @return
     */
    int edit(AlarmBo alarmBo) throws FrameworkRuntimeException;

    /**
     * 新增报警
     *
     * @param name
     * @param des
     * @param sensorId
     * @param status
     * @param enterpriseId
     * @param userBy
     * @param code
     * @param createDate
     * @return
     */
    String add(AlarmBo alarmBo) throws FrameworkRuntimeException;

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
     * 查询报警的设备
     *
     * @param operEid
     * @param i
     * @param j
     * @param string
     * @return
     */
    @Deprecated
    Pagination<AlarmVo> listByDevice(String operEid, Integer page, Integer pageSize, String status)
            throws FrameworkRuntimeException;

}
