package com.dotop.pipe.web.factory.workorder;

import com.dotop.pipe.web.api.factory.device.IDeviceFactory;
import com.dotop.pipe.web.api.factory.workorder.IWorkOrderFactory;
import com.dotop.pipe.api.service.workorder.IWorkOrderService;
import com.dotop.pipe.auth.cas.web.IAuthCasWeb;
import com.dotop.pipe.auth.core.vo.auth.LoginCas;
import com.dotop.pipe.core.bo.workOrder.WorkOrderBo;
import com.dotop.pipe.core.constants.PipeConstants;
import com.dotop.pipe.core.constants.WorkOrderConstants;
import com.dotop.pipe.core.form.WorkOrderForm;
import com.dotop.pipe.core.vo.workorder.WorkOrderVo;
import com.dotop.smartwater.dependence.core.common.RootModel;
import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.BeanUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class WorkOrderFactoryImpl implements IWorkOrderFactory {
    private final static Logger logger = LogManager.getLogger(WorkOrderFactoryImpl.class);

    @Autowired
    private IAuthCasWeb iAuthCasApi;

    @Autowired
    private IWorkOrderService iWorkOrderService;

    @Autowired
    private IDeviceFactory iDeviceFactory;

    @Override
    public WorkOrderVo add(WorkOrderForm workOrderForm) throws FrameworkRuntimeException {
        try {
            LoginCas loginCas = iAuthCasApi.get();
            String operEid = loginCas.getOperEid();
            String userId = loginCas.getUserId();
            String userBy = loginCas.getUserName();
            WorkOrderBo workOrderBo = BeanUtils.copyProperties(workOrderForm,
                    WorkOrderBo.class);
            Date curr = new Date();
            workOrderBo.setInitiationTime(curr);
            workOrderBo.setStatus(WorkOrderConstants.ORDER_STATUS_UNTREATED);
            workOrderBo.setCurr(curr);
            workOrderBo.setUserBy(userBy);
            workOrderBo.setEnterpriseId(operEid);
            workOrderBo.setIsDel(RootModel.NOT_DEL);
            return iWorkOrderService.add(workOrderBo);
        }
        catch (DataAccessException e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public WorkOrderVo get(WorkOrderForm workOrderForm) throws FrameworkRuntimeException {
        try {
            WorkOrderBo workOrderBo = BeanUtils.copy(workOrderForm, WorkOrderBo.class);
            workOrderBo.setEnterpriseId(iAuthCasApi.get().getOperEid());
            return iWorkOrderService.get(workOrderBo);
        } catch (DataAccessException e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public Pagination<WorkOrderVo> page(WorkOrderForm workOrderForm) throws FrameworkRuntimeException {
        try {
            WorkOrderBo workOrderBo = BeanUtils.copy(workOrderForm, WorkOrderBo.class);
            workOrderBo.setEnterpriseId(iAuthCasApi.get().getOperEid());
            Pagination<WorkOrderVo> page = iWorkOrderService.page(workOrderBo);
            return page;
        } catch (DataAccessException e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public List<WorkOrderVo> list(WorkOrderForm workOrderForm) throws FrameworkRuntimeException {
        try {
            WorkOrderBo workOrderBo = BeanUtils.copy(workOrderForm, WorkOrderBo.class);
            workOrderBo.setEnterpriseId(iAuthCasApi.get().getOperEid());
            return iWorkOrderService.list(workOrderBo);
        } catch (DataAccessException e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public WorkOrderVo edit(WorkOrderForm workOrderForm) throws FrameworkRuntimeException {
        try {
            WorkOrderBo workOrderBo = BeanUtils.copy(workOrderForm, WorkOrderBo.class);
            return iWorkOrderService.edit(workOrderBo);
        } catch (DataAccessException e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public String del(WorkOrderForm workOrderForm) throws FrameworkRuntimeException {
        try {
            WorkOrderBo workOrderBo = BeanUtils.copy(workOrderForm, WorkOrderBo.class);
            workOrderBo.setEnterpriseId(iAuthCasApi.get().getOperEid());
            workOrderBo.setIsDel(RootModel.DEL);
            return iWorkOrderService.del(workOrderBo);
        } catch (DataAccessException e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public WorkOrderVo editStatus(WorkOrderForm workOrderForm) {
        try {
            if (PipeConstants.PIPE_UNTREATED_STATUS.equals(workOrderForm.getStatus())) {
                workOrderForm.setStatus(PipeConstants.PIPE_HANDLED_STATUS);
            }
            WorkOrderBo workOrderBo = BeanUtils.copy(workOrderForm, WorkOrderBo.class);
            workOrderBo.setEnterpriseId(iAuthCasApi.get().getOperEid());
            return iWorkOrderService.editStatus(workOrderBo);
        } catch (DataAccessException e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public WorkOrderVo ifPass(WorkOrderForm workOrderForm) {
        try {
            WorkOrderBo workOrderBo = BeanUtils.copy(workOrderForm, WorkOrderBo.class);
            workOrderBo.setEnterpriseId(iAuthCasApi.get().getOperEid());
            workOrderBo.setUserBy(iAuthCasApi.get().getUserName());
            workOrderBo.setCurr(new Date());
            return iWorkOrderService.edit(workOrderBo);
        } catch (DataAccessException e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }
}
