package com.dotop.pipe.web.factory.patrol;

import com.dotop.pipe.web.api.factory.device.IDeviceFactory;
import com.dotop.pipe.web.api.factory.patrol.IPatrolRouteTaskFactory;
import com.dotop.pipe.api.service.common.ICommonUploadService;
import com.dotop.pipe.api.service.patrol.IPatrolRouteTaskService;
import com.dotop.pipe.auth.cas.web.IAuthCasWeb;
import com.dotop.pipe.auth.core.vo.auth.LoginCas;
import com.dotop.pipe.core.bo.patrol.PatrolRouteTaskBo;
import com.dotop.pipe.core.constants.PipeConstants;
import com.dotop.pipe.core.form.PatrolRouteTaskForm;
import com.dotop.pipe.core.vo.patrol.PatrolRouteTaskVo;
import com.dotop.smartwater.dependence.core.common.RootModel;
import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class PatrolRouteTaskFactoryImpl implements IPatrolRouteTaskFactory {
    private final static Logger logger = LogManager.getLogger(PatrolRouteTaskFactoryImpl.class);

    @Autowired
    private IAuthCasWeb iAuthCasApi;

    @Autowired
    private IPatrolRouteTaskService iPatrolRouteTaskService;

    @Autowired
    private IDeviceFactory iDeviceFactory;

    @Autowired
    private ICommonUploadService iCommonUploadService;

    @Override
    public PatrolRouteTaskVo add(PatrolRouteTaskForm patrolRouteTaskForm) throws FrameworkRuntimeException {
        try {
            LoginCas loginCas = iAuthCasApi.get();
            String operEid = loginCas.getOperEid();
            String userId = loginCas.getUserId();
            String userBy = loginCas.getUserName();
            PatrolRouteTaskBo patrolRouteTaskBo = BeanUtils.copyProperties(patrolRouteTaskForm,
                    PatrolRouteTaskBo.class);
            Date curr = new Date();
            patrolRouteTaskBo.setCurr(curr);
            patrolRouteTaskBo.setUserBy(userBy);
            patrolRouteTaskBo.setEnterpriseId(operEid);
            patrolRouteTaskBo.setIsDel(RootModel.NOT_DEL);
            patrolRouteTaskBo.setStatus(PipeConstants.PATROL_ROUTE_TASK_STATUS_NOTSTART);
            return iPatrolRouteTaskService.add(patrolRouteTaskBo);
        } catch (DataAccessException e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public PatrolRouteTaskVo get(PatrolRouteTaskForm patrolRouteTaskForm) throws FrameworkRuntimeException {
        try {
            PatrolRouteTaskBo patrolRouteTaskBo = BeanUtils.copy(patrolRouteTaskForm, PatrolRouteTaskBo.class);
            patrolRouteTaskBo.setEnterpriseId(iAuthCasApi.get().getOperEid());
            return iPatrolRouteTaskService.get(patrolRouteTaskBo);
        } catch (DataAccessException e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public Pagination<PatrolRouteTaskVo> page(PatrolRouteTaskForm patrolRouteTaskForm) throws FrameworkRuntimeException {
        try {
            PatrolRouteTaskBo patrolRouteTaskBo = BeanUtils.copy(patrolRouteTaskForm, PatrolRouteTaskBo.class);
            patrolRouteTaskBo.setEnterpriseId(iAuthCasApi.get().getOperEid());
            Pagination<PatrolRouteTaskVo> page = iPatrolRouteTaskService.page(patrolRouteTaskBo);
            return page;
        } catch (DataAccessException e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public List<PatrolRouteTaskVo> list(PatrolRouteTaskForm patrolRouteTaskForm) throws FrameworkRuntimeException {
        try {
            PatrolRouteTaskBo patrolRouteTaskBo = BeanUtils.copy(patrolRouteTaskForm, PatrolRouteTaskBo.class);
            if (StringUtils.isBlank(patrolRouteTaskBo.getEnterpriseId())) {
                patrolRouteTaskBo.setEnterpriseId(iAuthCasApi.get().getOperEid());
            }
            return iPatrolRouteTaskService.list(patrolRouteTaskBo);
        } catch (DataAccessException e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public PatrolRouteTaskVo edit(PatrolRouteTaskForm patrolRouteTaskForm) throws FrameworkRuntimeException {
        try {
            Date curr = new Date();
            PatrolRouteTaskBo patrolRouteTaskBo = BeanUtils.copy(patrolRouteTaskForm, PatrolRouteTaskBo.class);
            patrolRouteTaskBo.setEnterpriseId(iAuthCasApi.get().getOperEid());
            patrolRouteTaskBo.setUserBy(iAuthCasApi.get().getUserName());
            patrolRouteTaskBo.setCurr(curr);
            patrolRouteTaskBo.setLastHandlerUserId(iAuthCasApi.get().getUserId());
            patrolRouteTaskBo.setStatus(PipeConstants.PATROL_ROUTE_TASK_STATUS_HANDLED);
            patrolRouteTaskBo.setLastHandleTime(curr);
            // 更新图片信息
            if (patrolRouteTaskForm.getFileIdList() != null && !patrolRouteTaskForm.getFileIdList().isEmpty()) {
                this.iCommonUploadService.updateThirdId(patrolRouteTaskForm.getFileIdList(), patrolRouteTaskForm.getPatrolRouteTaskId());
            }

            return iPatrolRouteTaskService.edit(patrolRouteTaskBo);
        } catch (DataAccessException e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public String del(PatrolRouteTaskForm patrolRouteTaskForm) throws FrameworkRuntimeException {
        try {
            PatrolRouteTaskBo patrolRouteTaskBo = BeanUtils.copy(patrolRouteTaskForm, PatrolRouteTaskBo.class);
            patrolRouteTaskBo.setEnterpriseId(iAuthCasApi.get().getOperEid());
            patrolRouteTaskBo.setIsDel(RootModel.DEL);
            return iPatrolRouteTaskService.del(patrolRouteTaskBo);
        } catch (DataAccessException e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public PatrolRouteTaskVo editStatus(PatrolRouteTaskForm patrolRouteTaskForm) {
        try {
            if (PipeConstants.PATROL_ROUTE_TASK_STATUS_NOTSTART.equals(patrolRouteTaskForm.getStatus())) {
                patrolRouteTaskForm.setStatus(PipeConstants.PATROL_ROUTE_TASK_STATUS_HANDLED);
            }
            PatrolRouteTaskBo patrolRouteTaskBo = BeanUtils.copy(patrolRouteTaskForm, PatrolRouteTaskBo.class);
            patrolRouteTaskBo.setEnterpriseId(iAuthCasApi.get().getOperEid());
            return iPatrolRouteTaskService.editStatus(patrolRouteTaskBo);
        } catch (DataAccessException e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public void batchAdd(List<PatrolRouteTaskForm> tasks) {
        LoginCas loginCas = iAuthCasApi.get();
        String operEid = loginCas.getOperEid();
        String userId = loginCas.getUserId();
        String userBy = loginCas.getUserName();
        List<PatrolRouteTaskBo> taskList = BeanUtils.copy(tasks, PatrolRouteTaskBo.class);
        Date curr = new Date();
        for (PatrolRouteTaskBo patrolRouteTaskBo : taskList) {
            patrolRouteTaskBo.setCurr(curr);
            patrolRouteTaskBo.setUserBy(userBy);
            patrolRouteTaskBo.setEnterpriseId(operEid);
            patrolRouteTaskBo.setIsDel(RootModel.NOT_DEL);
            patrolRouteTaskBo.setStatus(PipeConstants.PATROL_ROUTE_TASK_STATUS_NOTSTART);
        }
        iPatrolRouteTaskService.batchAdd(taskList);
    }

    @Override
    public void batUpdate(List<PatrolRouteTaskForm> tasks) {
        LoginCas loginCas = iAuthCasApi.get();
        String operEid = loginCas.getOperEid();
        String userBy = loginCas.getUserName();
        List<PatrolRouteTaskBo> taskList = BeanUtils.copy(tasks, PatrolRouteTaskBo.class);
        Date curr = new Date();
        for (PatrolRouteTaskBo patrolRouteTaskBo : taskList) {
            patrolRouteTaskBo.setCurr(curr);
            patrolRouteTaskBo.setUserBy(userBy);
            patrolRouteTaskBo.setEnterpriseId(operEid);
            patrolRouteTaskBo.setIsDel(0);
        }
        iPatrolRouteTaskService.batchUpdate(taskList);
    }

    @Override
    public void batDel(List<PatrolRouteTaskForm> tasks) {
        LoginCas loginCas = iAuthCasApi.get();
        String operEid = loginCas.getOperEid();
        String userBy = loginCas.getUserName();
        List<PatrolRouteTaskBo> taskList = BeanUtils.copy(tasks, PatrolRouteTaskBo.class);
        Date curr = new Date();
        for (PatrolRouteTaskBo patrolRouteTaskBo : taskList) {
            patrolRouteTaskBo.setCurr(curr);
            patrolRouteTaskBo.setUserBy(userBy);
            patrolRouteTaskBo.setEnterpriseId(operEid);
            patrolRouteTaskBo.setIsDel(1);
        }
        iPatrolRouteTaskService.batchUpdate(taskList);
    }

    @Override
    public void batchAddWithOutAuth(List<PatrolRouteTaskForm> tasks) {
        String userBy = "system";
        List<PatrolRouteTaskBo> taskList = BeanUtils.copy(tasks, PatrolRouteTaskBo.class);
        Date curr = new Date();
        for (PatrolRouteTaskBo patrolRouteTaskBo : taskList) {
            patrolRouteTaskBo.setCurr(curr);
            patrolRouteTaskBo.setUserBy(userBy);
            patrolRouteTaskBo.setIsDel(RootModel.NOT_DEL);
            patrolRouteTaskBo.setStatus(PipeConstants.PATROL_ROUTE_TASK_STATUS_NOTSTART);
        }
        iPatrolRouteTaskService.batchAdd(taskList);
    }
}
