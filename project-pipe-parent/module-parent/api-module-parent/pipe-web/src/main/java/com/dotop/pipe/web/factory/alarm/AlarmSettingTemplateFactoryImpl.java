package com.dotop.pipe.web.factory.alarm;

import com.dotop.pipe.api.service.alarm.IAlarmSettingTemplateService;
import com.dotop.pipe.auth.cas.web.IAuthCasWeb;
import com.dotop.pipe.auth.core.vo.auth.LoginCas;
import com.dotop.pipe.core.bo.alarm.AlarmSettingTemplateBo;
import com.dotop.pipe.core.form.AlarmSettingTemplateForm;
import com.dotop.pipe.core.vo.alarm.AlarmSettingTemplateVo;
import com.dotop.pipe.web.api.factory.alarm.IAlarmSettingTemplateFactory;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.BeanUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 *
 * @date 2019年2月15日
 */
@Component
public class AlarmSettingTemplateFactoryImpl implements IAlarmSettingTemplateFactory {

    private static final Logger logger = LogManager.getLogger(AlarmSettingTemplateFactoryImpl.class);

    @Autowired
    private IAuthCasWeb iAuthCasApi;

    @Autowired
    private IAlarmSettingTemplateService iAlarmSettingService;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
    public AlarmSettingTemplateVo add(AlarmSettingTemplateForm alarmSettingForm) {
        LoginCas loginCas = iAuthCasApi.get();
        String operEid = loginCas.getOperEid();
        String userBy = loginCas.getUserName();
        AlarmSettingTemplateBo alarmSettingBo = BeanUtils.copyProperties(alarmSettingForm, AlarmSettingTemplateBo.class);
        alarmSettingBo.setEnterpriseId(operEid);
        alarmSettingBo.setUserBy(userBy);
        alarmSettingBo.setCurr(new Date());
        return iAlarmSettingService.add(alarmSettingBo);
    }

    @Override
    public Pagination<AlarmSettingTemplateVo> page(AlarmSettingTemplateForm alarmSettingForm) {
        LoginCas loginCas = iAuthCasApi.get();
        String operEid = loginCas.getOperEid();
        // 封装参数
        AlarmSettingTemplateBo alarmSettingTemplateBo = BeanUtils.copyProperties(alarmSettingForm, AlarmSettingTemplateBo.class);
        alarmSettingTemplateBo.setEnterpriseId(operEid);
        // TODO
        Pagination<AlarmSettingTemplateVo> pagination = iAlarmSettingService.page(alarmSettingTemplateBo);
        return pagination;
    }

    @Override
    public List<AlarmSettingTemplateVo> list(AlarmSettingTemplateForm alarmSettingForm) {
        LoginCas loginCas = iAuthCasApi.get();
        String operEid = loginCas.getOperEid();
        // 封装参数
        AlarmSettingTemplateBo alarmSettingTemplateBo = BeanUtils.copyProperties(alarmSettingForm, AlarmSettingTemplateBo.class);
        alarmSettingTemplateBo.setEnterpriseId(operEid);
        // TODO
        List<AlarmSettingTemplateVo> pagination = iAlarmSettingService.lists(alarmSettingTemplateBo);
        return pagination;
    }

    @Override
    public AlarmSettingTemplateVo get(AlarmSettingTemplateForm alarmSettingForm) {
        LoginCas loginCas = iAuthCasApi.get();
        String operEid = loginCas.getOperEid();
        String userBy = loginCas.getUserName();
        AlarmSettingTemplateBo alarmSettingTemplateBo = BeanUtils.copyProperties(alarmSettingForm, AlarmSettingTemplateBo.class);
        alarmSettingTemplateBo.setEnterpriseId(operEid);
        alarmSettingTemplateBo.setUserBy(userBy);
        alarmSettingTemplateBo.setCurr(new Date());
        return iAlarmSettingService.get(alarmSettingTemplateBo);
    }

    @Override
    public String del(AlarmSettingTemplateForm alarmSettingForm) {
        LoginCas loginCas = iAuthCasApi.get();
        String operEid = loginCas.getOperEid();
        String userBy = loginCas.getUserName();
        AlarmSettingTemplateBo alarmSettingTemplateBo = BeanUtils.copyProperties(alarmSettingForm, AlarmSettingTemplateBo.class);
        alarmSettingTemplateBo.setEnterpriseId(operEid);
        alarmSettingTemplateBo.setUserBy(userBy);
        alarmSettingTemplateBo.setCurr(new Date());
        return iAlarmSettingService.del(alarmSettingTemplateBo);
    }


}
