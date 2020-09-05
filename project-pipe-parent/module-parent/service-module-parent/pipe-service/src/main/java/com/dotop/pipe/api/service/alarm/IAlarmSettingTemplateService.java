package com.dotop.pipe.api.service.alarm;

import com.dotop.pipe.core.bo.alarm.AlarmSettingTemplateBo;
import com.dotop.pipe.core.vo.alarm.AlarmSettingTemplateVo;
import com.dotop.smartwater.dependence.core.common.BaseService;
import com.dotop.smartwater.dependence.core.pagination.Pagination;

import java.util.List;

// 报警
public interface IAlarmSettingTemplateService extends BaseService<AlarmSettingTemplateBo, AlarmSettingTemplateVo> {

    /**
     * 预警值设置 新增
     */
    AlarmSettingTemplateVo add(AlarmSettingTemplateBo alarmSettingTemplateBo);

    /**
     * 查询非区域的预警设置信息
     *
     * @param alarmSettingTemplateBo
     * @return
     */
    Pagination<AlarmSettingTemplateVo> page(AlarmSettingTemplateBo alarmSettingTemplateBo);

    /**
     * 预警设置查询详情
     */
    AlarmSettingTemplateVo get(AlarmSettingTemplateBo alarmSettingTemplateBo);

    String del(AlarmSettingTemplateBo alarmSettingTemplateBo);

    List<AlarmSettingTemplateVo> lists(AlarmSettingTemplateBo alarmSettingTemplateBo);

}
