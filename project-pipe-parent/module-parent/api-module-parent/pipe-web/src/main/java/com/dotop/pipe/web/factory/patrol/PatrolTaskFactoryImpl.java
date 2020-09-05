package com.dotop.pipe.web.factory.patrol;

import com.dotop.pipe.web.api.factory.device.IDeviceFactory;
import com.dotop.pipe.web.api.factory.patrol.IPatrolRouteFactory;
import com.dotop.pipe.web.api.factory.patrol.IPatrolRouteTaskFactory;
import com.dotop.pipe.web.api.factory.patrol.IPatrolTaskFactory;
import com.dotop.pipe.web.api.factory.user.IUserFactory;
import com.dotop.pipe.api.service.patrol.IPatrolTaskService;
import com.dotop.pipe.auth.cas.web.IAuthCasWeb;
import com.dotop.pipe.auth.core.vo.auth.LoginCas;
import com.dotop.pipe.core.bo.patrol.PatrolTaskBo;
import com.dotop.pipe.core.constants.PipeConstants;
import com.dotop.pipe.core.form.PatrolRouteForm;
import com.dotop.pipe.core.form.PatrolRouteTaskForm;
import com.dotop.pipe.core.form.PatrolTaskForm;
import com.dotop.pipe.core.vo.patrol.PatrolRouteTaskVo;
import com.dotop.pipe.core.vo.patrol.PatrolRouteVo;
import com.dotop.pipe.core.vo.patrol.PatrolTaskVo;
import com.dotop.smartwater.dependence.core.common.RootModel;
import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.BeanUtils;
import com.dotop.smartwater.project.module.core.auth.vo.UserVo;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class PatrolTaskFactoryImpl implements IPatrolTaskFactory {
    private final static Logger logger = LogManager.getLogger(PatrolTaskFactoryImpl.class);

    @Autowired
    private IAuthCasWeb iAuthCasApi;

    @Autowired
    private IPatrolTaskService iPatrolTaskService;

    @Autowired
    private IDeviceFactory iDeviceFactory;

    @Autowired
    private IPatrolRouteTaskFactory iPatrolRouteTaskFactory;
    @Autowired
    private IUserFactory iUserFactory;

    @Autowired
    private IPatrolRouteFactory iPatrolRouteFactory;

    @Override
    public PatrolTaskVo add(PatrolTaskForm patrolTaskForm) throws FrameworkRuntimeException {
        try {
            LoginCas loginCas = iAuthCasApi.get();
            String operEid = loginCas.getOperEid();
            String userId = loginCas.getUserId();
            String userBy = loginCas.getUserName();
            PatrolTaskBo patrolTaskBo = BeanUtils.copyProperties(patrolTaskForm,
                    PatrolTaskBo.class);
            Date curr = new Date();
            patrolTaskBo.setCurr(curr);
            patrolTaskBo.setUserBy(userBy);
            patrolTaskBo.setEnterpriseId(operEid);
            patrolTaskBo.setIsDel(RootModel.NOT_DEL);
            patrolTaskBo.setStatus(PipeConstants.PATROL_TASK_STATUS_NOTSTART);
            PatrolTaskVo patrolTaskVo = iPatrolTaskService.add(patrolTaskBo);
            PatrolRouteForm patrolRouteForm = new PatrolRouteForm();
            patrolRouteForm.setPatrolRouteId(patrolTaskForm.getPatrolRouteId());
            PatrolRouteVo patrolRouteVo = iPatrolRouteFactory.get(patrolRouteForm);
            patrolRouteVo.setPatrolTaskId(patrolTaskVo.getPatrolTaskId());
            iPatrolRouteFactory.edit(BeanUtils.copy(patrolRouteVo, PatrolRouteForm.class));
            return patrolTaskVo;
        } catch (DataAccessException e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public PatrolTaskVo addWithOutAuth(PatrolTaskForm patrolTaskForm) throws FrameworkRuntimeException {
        try {
            String userBy = "system";
            PatrolTaskBo patrolTaskBo = BeanUtils.copyProperties(patrolTaskForm,
                    PatrolTaskBo.class);
            Date curr = new Date();
            patrolTaskBo.setCurr(curr);
            patrolTaskBo.setUserBy(userBy);
            patrolTaskBo.setIsDel(RootModel.NOT_DEL);
            patrolTaskBo.setStatus(PipeConstants.PATROL_TASK_STATUS_NOTSTART);
            PatrolTaskVo patrolTaskVo = iPatrolTaskService.add(patrolTaskBo);
            PatrolRouteForm patrolRouteForm = new PatrolRouteForm();
            patrolRouteForm.setPatrolRouteId(patrolTaskForm.getPatrolRouteId());
            patrolRouteForm.setEnterpriseId(patrolTaskForm.getEnterpriseId());
            PatrolRouteVo patrolRouteVo = iPatrolRouteFactory.get(patrolRouteForm);
            patrolRouteVo.setPatrolTaskId(patrolTaskVo.getPatrolTaskId());
            patrolRouteVo.setEnterpriseId(patrolTaskForm.getEnterpriseId());
            iPatrolRouteFactory.editWithOutAuth(BeanUtils.copy(patrolRouteVo, PatrolRouteForm.class));
            return patrolTaskVo;
        } catch (DataAccessException e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public PatrolTaskVo get(PatrolTaskForm patrolTaskForm) throws FrameworkRuntimeException {
        try {
            PatrolTaskBo patrolTaskBo = BeanUtils.copy(patrolTaskForm, PatrolTaskBo.class);
            patrolTaskBo.setEnterpriseId(iAuthCasApi.get().getOperEid());
            PatrolTaskVo patrolTaskVo = iPatrolTaskService.get(patrolTaskBo);
            List<PatrolTaskBo> batchList = new ArrayList<>();
            Date curr = new Date();
            List<UserVo> userList = iUserFactory.getUserList(iAuthCasApi.get().getOperEid());
            if (patrolTaskVo.getStatus() != PipeConstants.PATROL_TASK_STATUS_OVERTIME) {
                if (patrolTaskVo.getEndTime().getTime() < curr.getTime()) {
                    patrolTaskVo.setStatus(PipeConstants.PATROL_TASK_STATUS_OVERTIME);
                    PatrolTaskBo patrolTaskBoTemp = BeanUtils.copy(patrolTaskVo, PatrolTaskBo.class);
                    batchList.add(patrolTaskBoTemp);
                }
            }
            List<String> taskIds = patrolTaskVo.getTaskIds();
            if (taskIds != null && taskIds.size() > 0) {
                PatrolRouteTaskForm patrolRouteTaskForm = new PatrolRouteTaskForm();
                patrolRouteTaskForm.setPatrolRouteTaskIds(taskIds);
                List<PatrolRouteTaskVo> patrolRouteTaskList = iPatrolRouteTaskFactory.list(patrolRouteTaskForm);
                List<PatrolRouteTaskVo> collect = patrolRouteTaskList.stream().sorted(Comparator.comparingInt(s -> Integer.parseInt(s.getSort()))).collect(Collectors.toList());
                patrolTaskVo.setTasks(collect);
            }
            List<UserVo> tempUserList = new ArrayList<>();
            List<String> handlerUserIds = patrolTaskVo.getHandlerUserIds();
            if (handlerUserIds != null && handlerUserIds.size() > 0) {
                for (String handlerUserId : handlerUserIds) {
                    for (UserVo userVo : userList) {
                        if (handlerUserId.equals(userVo.getUserid())) {
                            tempUserList.add(userVo);
                        }
                    }
                }
                String tempStr = "";
                for (int i = 0; i < tempUserList.size(); i++) {
                    tempStr += tempUserList.get(i).getName() + ", ";
                }
                if (tempStr.length() >= 2) {
                    tempStr = tempStr.substring(0, tempStr.length() - 2);
                }
                patrolTaskVo.setShowUser(tempStr);

            }
            if (!batchList.isEmpty()) {
                iPatrolTaskService.batchUpdateStatus(batchList, iAuthCasApi.get().getOperEid(), PipeConstants.PATROL_TASK_STATUS_OVERTIME);
            }
            return patrolTaskVo;
        } catch (DataAccessException e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public Pagination<PatrolTaskVo> page(PatrolTaskForm patrolTaskForm) throws FrameworkRuntimeException {
        try {
            PatrolTaskBo patrolTaskBo = BeanUtils.copy(patrolTaskForm, PatrolTaskBo.class);
            patrolTaskBo.setEnterpriseId(iAuthCasApi.get().getOperEid());

            Pagination<PatrolTaskVo> page = iPatrolTaskService.page(patrolTaskBo);
            List<PatrolTaskVo> data = page.getData();
            Date curr = new Date();
            List<PatrolTaskBo> batchList = new ArrayList<>();
            List<UserVo> userList = iUserFactory.getUserList(iAuthCasApi.get().getOperEid());
            for (PatrolTaskVo patrolTaskVo : data) {
                if (patrolTaskVo.getStatus() != PipeConstants.PATROL_TASK_STATUS_OVERTIME) {
                    if (patrolTaskVo.getEndTime().getTime() < curr.getTime()) {
                        patrolTaskVo.setStatus(PipeConstants.PATROL_TASK_STATUS_OVERTIME);
                        PatrolTaskBo patrolTaskBoTemp = BeanUtils.copy(patrolTaskVo, PatrolTaskBo.class);
                        batchList.add(patrolTaskBoTemp);
                    }
                }
                List<String> taskIds = patrolTaskVo.getTaskIds();
                if (taskIds != null && taskIds.size() > 0) {
                    PatrolRouteTaskForm patrolRouteTaskForm = new PatrolRouteTaskForm();
                    patrolRouteTaskForm.setPatrolRouteTaskIds(taskIds);
                    List<PatrolRouteTaskVo> patrolRouteTaskList = iPatrolRouteTaskFactory.list(patrolRouteTaskForm);
                    // 任务点排序
                    List<PatrolRouteTaskVo> collect = patrolRouteTaskList.stream().sorted(Comparator.comparingInt(s -> Integer.parseInt(s.getSort()))).collect(Collectors.toList());
                    patrolTaskVo.setTasks(collect);
                }
                String patrolRouteId = patrolTaskVo.getPatrolRouteId();
                if (StringUtils.isNotBlank(patrolRouteId)) {
                    PatrolRouteForm patrolRouteForm = new PatrolRouteForm();
                    patrolRouteForm.setPatrolRouteId(patrolRouteId);
                    PatrolRouteVo patrolRouteVo = iPatrolRouteFactory.get(patrolRouteForm);
                    patrolTaskVo.setPatrolRoute(patrolRouteVo);
                }

                List<UserVo> tempUserList = new ArrayList<>();
                List<String> handlerUserIds = patrolTaskVo.getHandlerUserIds();
                if (handlerUserIds != null && handlerUserIds.size() > 0) {
                    for (String handlerUserId : handlerUserIds) {
                        for (UserVo userVo : userList) {
                            if (handlerUserId.equals(userVo.getUserid())) {
                                tempUserList.add(userVo);
                            }
                        }
                    }
                    String tempStr = "";
                    for (int i = 0; i < tempUserList.size(); i++) {
                        tempStr += tempUserList.get(i).getName() + ", ";
                    }
                    if (tempStr.length() >= 2) {
                        tempStr = tempStr.substring(0, tempStr.length() - 2);
                    }
                    patrolTaskVo.setShowUser(tempStr);

                }


            }
            if (!batchList.isEmpty()) {
                iPatrolTaskService.batchUpdateStatus(batchList, iAuthCasApi.get().getOperEid(), PipeConstants.PATROL_TASK_STATUS_OVERTIME);
            }
            return page;
        } catch (DataAccessException e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public List<PatrolTaskVo> list(PatrolTaskForm patrolTaskForm) throws FrameworkRuntimeException {
        try {
            PatrolTaskBo patrolTaskBo = BeanUtils.copy(patrolTaskForm, PatrolTaskBo.class);
            patrolTaskBo.setEnterpriseId(iAuthCasApi.get().getOperEid());
            return iPatrolTaskService.list(patrolTaskBo);
        } catch (DataAccessException e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public PatrolTaskVo edit(PatrolTaskForm patrolTaskForm) throws FrameworkRuntimeException {
        try {
            Date cuur = new Date();
            PatrolTaskBo patrolTaskBo = BeanUtils.copy(patrolTaskForm, PatrolTaskBo.class);
            patrolTaskBo.setEnterpriseId(iAuthCasApi.get().getOperEid());
            patrolTaskBo.setUserBy(iAuthCasApi.get().getUserName());
            patrolTaskBo.setCompletionTime(cuur);
            patrolTaskBo.setCurr(cuur);

            return iPatrolTaskService.edit(patrolTaskBo);
        } catch (DataAccessException e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public String del(PatrolTaskForm patrolTaskForm) throws FrameworkRuntimeException {
        try {
            PatrolTaskBo patrolTaskBo = BeanUtils.copy(patrolTaskForm, PatrolTaskBo.class);
            patrolTaskBo.setEnterpriseId(iAuthCasApi.get().getOperEid());
            patrolTaskBo.setIsDel(RootModel.DEL);
            return iPatrolTaskService.del(patrolTaskBo);
        } catch (DataAccessException e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public PatrolTaskVo editStatus(PatrolTaskForm patrolTaskForm) {
        try {
            if (patrolTaskForm.getStatus() != null) {
                PatrolTaskBo patrolTaskBo = BeanUtils.copy(patrolTaskForm, PatrolTaskBo.class);
                patrolTaskBo.setEnterpriseId(iAuthCasApi.get().getOperEid());
                return iPatrolTaskService.editStatus(patrolTaskBo);
            } else {
                return null;
            }
        } catch (DataAccessException e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }


}
