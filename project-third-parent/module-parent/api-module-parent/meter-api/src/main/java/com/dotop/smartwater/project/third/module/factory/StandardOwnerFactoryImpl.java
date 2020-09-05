package com.dotop.smartwater.project.third.module.factory;

import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.third.module.api.factory.IStandardOwnerFactory;
import com.dotop.smartwater.project.third.module.api.service.IMeterDockingService;
import com.dotop.smartwater.project.third.module.api.service.IStandardDeviceService;
import com.dotop.smartwater.project.third.module.api.service.IStandardDeviceUplinkService;
import com.dotop.smartwater.project.third.module.api.service.IStandardOwnerService;
import com.dotop.smartwater.project.third.module.core.third.standard.form.DataForm;
import com.dotop.smartwater.project.third.module.core.third.standard.vo.StandardOwnerVo;
import com.dotop.smartwater.project.third.module.core.water.bo.DeviceUplinkBo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 */
@Component
public class StandardOwnerFactoryImpl implements IStandardOwnerFactory {

    private final static Logger LOGGER = LogManager.getLogger(StandardOwnerFactoryImpl.class);



    @Autowired
    private IStandardDeviceUplinkService iStandardDeviceUplinkService;

    @Autowired
    private IMeterDockingService iMeterDockingService;

    @Autowired
    private IStandardDeviceService iStandardDeviceService;

    @Autowired
    private IStandardOwnerService iStandardOwnerService;




    @Override
    public Pagination<StandardOwnerVo> pageOwner(DataForm dataForm) throws FrameworkRuntimeException {
        try {
            DeviceUplinkBo deviceUplinkBo = new DeviceUplinkBo();
            deviceUplinkBo.setEnterpriseid(dataForm.getEnterpriseid());
            deviceUplinkBo.setStartMonth(dataForm.getStartMonth());
            deviceUplinkBo.setEndMonth(dataForm.getEndMonth());
            Pagination<StandardOwnerVo> ownerPagination = iStandardOwnerService.pageOwner(deviceUplinkBo, dataForm.getPage(), dataForm.getPageCount());
            return ownerPagination;
        } catch (Exception e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.REQUEST_ERROR);
        }
    }


}
