package com.dotop.smartwater.project.third.module.factory;

import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.utils.BeanUtils;
import com.dotop.smartwater.project.third.module.api.factory.IMeterDockingFactory;
import com.dotop.smartwater.project.third.module.api.service.IMeterDockingService;
import com.dotop.smartwater.project.third.module.core.water.bo.DockingBo;
import com.dotop.smartwater.project.third.module.core.water.form.DockingForm;
import com.dotop.smartwater.project.third.module.core.water.vo.DockingVo;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 *
 */
@Component
public class MeterDockingFactoryImpl implements IMeterDockingFactory {

    private final static Logger LOGGER = LogManager.getLogger(MeterDockingFactoryImpl.class);

    @Autowired
    private IMeterDockingService iMeterDockingService;

    @Override
    public List<DockingVo> list(DockingForm dockingForm) throws FrameworkRuntimeException {
        DockingBo dockingBo = BeanUtils.copy(dockingForm, DockingBo.class);
        return iMeterDockingService.list(dockingBo);
    }

    @Override
    public DockingVo get(DockingForm dockingForm) throws FrameworkRuntimeException {
        DockingBo dockingBo = BeanUtils.copy(dockingForm, DockingBo.class);
        return iMeterDockingService.get(dockingBo);
    }

    @Override
    public boolean isRealUser(DockingForm dockingForm) throws FrameworkRuntimeException {
        if (StringUtils.isBlank(dockingForm.getUsername())
                || StringUtils.isBlank(dockingForm.getPassword())
                || StringUtils.isBlank(dockingForm.getCode())) {
            throw new FrameworkRuntimeException(BaseExceptionConstants.BASE_ERROR, "校验信息不足");
        }
        try {
            return get(dockingForm) != null;
        } catch (Exception e) {
            LOGGER.error(LogMsg.to(e));
            return false;
        }
    }
}
