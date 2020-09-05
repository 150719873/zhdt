package com.dotop.pipe.web.api.factory.alarm;

import com.dotop.pipe.core.form.AlarmSettingTemplateForm;
import com.dotop.pipe.core.vo.alarm.AlarmSettingTemplateVo;
import com.dotop.smartwater.dependence.core.common.BaseFactory;
import com.dotop.smartwater.dependence.core.pagination.Pagination;

import java.util.List;

public interface IAlarmSettingTemplateFactory extends BaseFactory<AlarmSettingTemplateForm, AlarmSettingTemplateVo> {

    /**
     * 预警模板设置新增
     */
    AlarmSettingTemplateVo add(AlarmSettingTemplateForm alarmSettingForm);

    /**
     * 设备预警模板设置分页查询
     *
     * @param deviceForm
     * @return
     */
    Pagination<AlarmSettingTemplateVo> page(AlarmSettingTemplateForm deviceForm);

    List<AlarmSettingTemplateVo> list(AlarmSettingTemplateForm deviceForm);

    /**
     * 预警设置查询详情接口
     */
    AlarmSettingTemplateVo get(AlarmSettingTemplateForm alarmSettingForm);

    String del(AlarmSettingTemplateForm alarmSettingForm);
}
