package com.dotop.pipe.web.factory.alarm;

import com.dotop.pipe.api.service.alarm.IAlarmNoticeRuleService;
import com.dotop.pipe.auth.cas.web.IAuthCasWeb;
import com.dotop.pipe.auth.core.vo.auth.LoginCas;
import com.dotop.pipe.core.bo.alarm.AlarmNoticeBo;
import com.dotop.pipe.core.form.AlarmNoticeForm;
import com.dotop.pipe.core.vo.alarm.AlarmNoticeRuleVo;
import com.dotop.pipe.web.api.factory.alarm.IAlarmNoticeRuleFactory;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.BeanUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 *
 * @date 2019年2月15日
 */
@Component
public class AlarmNoticeRuleFactoryImpl implements IAlarmNoticeRuleFactory {

    private final static Logger logger = LogManager.getLogger(AlarmNoticeRuleFactoryImpl.class);

    @Autowired
    private IAuthCasWeb iAuthCasApi;

    @Autowired
    private IAlarmNoticeRuleService iAlarmNoticeRuleService;

    @Override
    public Pagination<AlarmNoticeRuleVo> page(AlarmNoticeForm alarmForm) throws FrameworkRuntimeException {
        LoginCas loginCas = iAuthCasApi.get();
        String operEid = loginCas.getOperEid();
        AlarmNoticeBo alarmBo = BeanUtils.copyProperties(alarmForm, AlarmNoticeBo.class);
        alarmBo.setEnterpriseId(operEid);
        Pagination<AlarmNoticeRuleVo> pagination = iAlarmNoticeRuleService.page(alarmBo);
        return pagination;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
    public AlarmNoticeRuleVo add(AlarmNoticeForm alarmNoticeForm)
            throws FrameworkRuntimeException {
        LoginCas loginCas = iAuthCasApi.get();
        String operEid = loginCas.getOperEid();
        Date createDate = new Date();
        AlarmNoticeBo alarmNoticeBo = BeanUtils.copyProperties(alarmNoticeForm, AlarmNoticeBo.class);
        alarmNoticeBo.setCurr(createDate);
        alarmNoticeBo.setEnterpriseId(operEid);
        alarmNoticeBo.setUserBy(loginCas.getUserName());
        boolean flag = iAlarmNoticeRuleService.isExist(alarmNoticeBo);
        if (flag) {
            iAlarmNoticeRuleService.edit(alarmNoticeBo);
        } else {
            iAlarmNoticeRuleService.add(alarmNoticeBo);
        }
        return null;
    }

    @Override
    public String del(AlarmNoticeForm alarmNoticeForm) throws FrameworkRuntimeException {

        LoginCas loginCas = iAuthCasApi.get();
        String operEid = loginCas.getOperEid();
        Date createDate = new Date();
        AlarmNoticeBo alarmNoticeBo = BeanUtils.copyProperties(alarmNoticeForm, AlarmNoticeBo.class);
        alarmNoticeBo.setCurr(createDate);
        alarmNoticeBo.setEnterpriseId(operEid);
        alarmNoticeBo.setUserBy(loginCas.getUserName());
        iAlarmNoticeRuleService.del(alarmNoticeBo);
        return null;
    }

    /**
     * 告警通知日志分页查询
     *
     * @param alarmForm
     * @return
     * @throws FrameworkRuntimeException
     */
    @Override
    public Pagination<AlarmNoticeRuleVo> logPage(AlarmNoticeForm alarmForm) throws FrameworkRuntimeException {
        LoginCas loginCas = iAuthCasApi.get();
        String operEid = loginCas.getOperEid();
        AlarmNoticeBo alarmBo = BeanUtils.copyProperties(alarmForm, AlarmNoticeBo.class);
        alarmBo.setEnterpriseId(operEid);
        Pagination<AlarmNoticeRuleVo> pagination = iAlarmNoticeRuleService.logPage(alarmBo);
        return pagination;
    }

}
