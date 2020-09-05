package com.dotop.smartwater.project.third.concentrator.api.impl;

import com.dotop.smartwater.project.third.concentrator.core.bo.CollectorBo;
import com.dotop.smartwater.project.third.concentrator.core.vo.CollectorVo;
import com.dotop.smartwater.project.third.concentrator.service.ICollectorService;
import com.dotop.smartwater.project.third.concentrator.service.IConcentratorDeviceService;
import com.dotop.smartwater.dependence.core.common.RootModel;
import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.BeanUtils;
import com.dotop.smartwater.project.module.core.auth.local.IAuthCasClient;
import com.dotop.smartwater.project.third.concentrator.api.ICollectorFactory;
import com.dotop.smartwater.project.third.concentrator.api.IConcentratorFactory;
import com.dotop.smartwater.project.third.concentrator.core.constants.ConcentratorConstants;
import com.dotop.smartwater.project.third.concentrator.core.form.CollectorForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.util.List;

/**
 * 采集器业务逻辑接口
 *
 *
 */
@Component
public class CollectorFactoryImpl implements ICollectorFactory, IAuthCasClient {
    private static final Logger LOGGER = LogManager.getLogger(CollectorFactoryImpl.class);

    @Autowired
    private ICollectorService iCollectorService;
    @Autowired
    private IConcentratorDeviceService iConcentratorDeviceService;
    @Autowired
    IConcentratorFactory iConcentratorFactory;


    /**
     * 新增一个采集器的设备
     *
     * @param collectorForm
     * @return
     * @throws FrameworkRuntimeException
     */
    @Override
    public CollectorVo add(CollectorForm collectorForm) throws FrameworkRuntimeException {
        try {
            // String enterpriseid = getEnterpriseid();
            CollectorBo collectorBo = BeanUtils.copy(collectorForm, CollectorBo.class);
            collectorBo.setEnterpriseid(getEnterpriseid());
            collectorBo.setUserBy(getAccount());
            collectorBo.setCurr(getCurr());
            collectorBo.setIsDel(RootModel.NOT_DEL);
            iConcentratorFactory.needReordering(getEnterpriseid(), collectorForm.getConcentrator().getId());
            return iCollectorService.add(collectorBo);
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }

    }

    @Override
    public CollectorVo get(CollectorForm collectorForm) throws FrameworkRuntimeException {
        try {
            CollectorBo collectorBo = BeanUtils.copy(collectorForm, CollectorBo.class);
            collectorBo.setEnterpriseid(getEnterpriseid());
            return iCollectorService.get(collectorBo);
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public Pagination<CollectorVo> page(CollectorForm collectorForm) throws FrameworkRuntimeException {
        try {
            CollectorBo collectorBo = BeanUtils.copy(collectorForm, CollectorBo.class);
            String enterpriseid = getEnterpriseid();
            collectorBo.setEnterpriseid(enterpriseid);
            return iCollectorService.page(collectorBo);
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public List<CollectorVo> list(CollectorForm collectorForm) throws FrameworkRuntimeException {
        try {
            CollectorBo collectorBo = BeanUtils.copy(collectorForm, CollectorBo.class);
            collectorBo.setEnterpriseid(getEnterpriseid());
            return iCollectorService.list(collectorBo);
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public CollectorVo edit(CollectorForm collectorForm) throws FrameworkRuntimeException {
        try {
            CollectorBo collectorBo = BeanUtils.copy(collectorForm, CollectorBo.class);
            collectorBo.setEnterpriseid(getEnterpriseid());
            collectorBo.setUserBy(getAccount());
            collectorBo.setCurr(getCurr());
            iConcentratorFactory.needReordering(getEnterpriseid(), collectorForm.getConcentrator().getId());
            return iCollectorService.edit(collectorBo);
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public CollectorVo editStatus(CollectorForm collectorForm) throws FrameworkRuntimeException {
        try {
            if ("DISABLE".equals(collectorForm.getStatus()) || collectorForm.getStatus()==null ) {
                collectorForm.setStatus(ConcentratorConstants.STATUS_USE);
            }else {
                collectorForm.setStatus(ConcentratorConstants.STATUS_DISABLE);
            }
            CollectorBo collectorBo = BeanUtils.copy(collectorForm, CollectorBo.class);
            collectorBo.setEnterpriseid(getEnterpriseid());
            return iCollectorService.editStatus(collectorBo);
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public String del(CollectorForm collectorForm) throws FrameworkRuntimeException {
        try {
            CollectorBo collectorBo = BeanUtils.copy(collectorForm, CollectorBo.class);
            collectorBo.setEnterpriseid(getEnterpriseid());
            iConcentratorFactory.needReordering(getEnterpriseid(), collectorForm.getConcentrator().getId());
            return iCollectorService.del(collectorBo);
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public Pagination<CollectorVo> pageArchive(CollectorForm collectorForm) throws FrameworkRuntimeException {

        try {
            CollectorBo collectorBo = BeanUtils.copy(collectorForm, CollectorBo.class);
            collectorBo.setEnterpriseid(getEnterpriseid());
            Pagination<CollectorVo> page = iCollectorService.pageArchive(collectorBo);
            for (CollectorVo collectorVo : page.getData()) {
                //查询水表挂载数
                Integer deviceMountAmount = iConcentratorDeviceService.countCollectorDevice(getEnterpriseid(), collectorVo.getId(), null);
                collectorVo.setDeviceMountAmount(deviceMountAmount);
            }
            return page;
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public CollectorVo getByCode(CollectorForm collectorForm) throws FrameworkRuntimeException {
        try {
            CollectorBo collectorBo = BeanUtils.copy(collectorForm, CollectorBo.class);
            collectorBo.setEnterpriseid(getEnterpriseid());
            return iCollectorService.getByCode(collectorBo);
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public boolean isExist(CollectorForm collectorForm) throws FrameworkRuntimeException {


        try {
            CollectorBo collectorBo = BeanUtils.copy(collectorForm, CollectorBo.class);
            collectorBo.setEnterpriseid(getEnterpriseid());
            Boolean exist = iCollectorService.isExist(collectorBo);
            return exist;
        } catch (DataAccessException e) {
            LOGGER.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }


}
