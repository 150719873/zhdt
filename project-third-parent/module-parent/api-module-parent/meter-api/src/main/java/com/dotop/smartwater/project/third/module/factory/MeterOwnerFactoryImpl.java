package com.dotop.smartwater.project.third.module.factory;

import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.utils.BeanUtils;
import com.dotop.smartwater.project.third.module.api.factory.IMeterOwnerFactory;
import com.dotop.smartwater.project.third.module.api.service.IMeterOwnerService;
import com.dotop.smartwater.project.third.module.api.service.IWaterOwnerService;
import com.dotop.smartwater.project.third.module.core.water.bo.OwnerBo;
import com.dotop.smartwater.project.third.module.core.water.form.OwnerForm;
import com.dotop.smartwater.project.third.module.core.water.vo.OwnerVo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 *
 */
@Component
public class MeterOwnerFactoryImpl implements IMeterOwnerFactory {

    private final static Logger LOGGER = LogManager.getLogger(MeterOwnerFactoryImpl.class);

    @Autowired
    private IMeterOwnerService iMeterOwnerService;
    @Autowired
    private IWaterOwnerService iWaterOwnerService;

    @Override
    public List<OwnerVo> list(OwnerForm ownerForm) throws FrameworkRuntimeException {
        OwnerBo ownerBo = BeanUtils.copy(ownerForm, OwnerBo.class);
        return iMeterOwnerService.list(ownerBo);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
    public void adds(List<OwnerForm> ownerForms) throws FrameworkRuntimeException {
        List<OwnerBo> ownerBos = BeanUtils.copy(ownerForms, OwnerBo.class);
        iMeterOwnerService.adds(ownerBos);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
    public void edits(List<OwnerForm> ownerForms, String enterpriseid) throws FrameworkRuntimeException {
        List<OwnerBo> ownerBos = BeanUtils.copy(ownerForms, OwnerBo.class);
        iMeterOwnerService.edits(ownerBos, enterpriseid);
    }


    @Override
    public OwnerVo get(OwnerForm ownerForm) throws FrameworkRuntimeException {
        OwnerBo ownerBo = BeanUtils.copy(ownerForm, OwnerBo.class);
        return iMeterOwnerService.get(ownerBo);
    }

    @Override
    public List<OwnerForm> check(List<OwnerForm> ownerForms) throws FrameworkRuntimeException {
        List<OwnerVo> list = iWaterOwnerService.list(new OwnerBo());
        Map<String, OwnerVo> collect = list.stream().collect(Collectors.toMap(OwnerVo::getUserno, s -> s));
        //重复的list，需要日志记录错误
        List<OwnerForm> repeat = new ArrayList<>();
        //不重复的list
        List<OwnerForm> noRepeat = new ArrayList<>();
        Set<String> hashSet = new HashSet<>();
        ownerForms.forEach(ownerForm -> {
            OwnerVo check = collect.get(ownerForm.getUserno());
            if (check != null) {
                //如果userno也相同
                if (ownerForm.getEnterpriseid().equals(check.getEnterpriseid()) && hashSet.add(ownerForm.getUserno())) {
                    noRepeat.add(ownerForm);
                } else {
                    repeat.add(ownerForm);
                }
            } else if (hashSet.add(ownerForm.getUserno())){
                noRepeat.add(ownerForm);
            } else {
                repeat.add(ownerForm);
            }
        });
        List<OwnerForm> finalNoRepeat = new ArrayList<>();
        for (OwnerForm ownerForm : noRepeat) {
            if (ownerForm.getUserno() != null && ownerForm.getUserno().length() > 0  && ownerForm.getUsername() != null && ownerForm.getUsername().length() > 0) {
                finalNoRepeat.add(ownerForm);
            }
        }
        if (!repeat.isEmpty()) {
            LOGGER.error(LogMsg.to("ownerForms,重复的业主", repeat));
        }
        return finalNoRepeat;
    }
}
