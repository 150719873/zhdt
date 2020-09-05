package com.dotop.pipe.web.factory.patrol;

import com.dotop.pipe.core.form.*;
import com.dotop.pipe.web.api.factory.patrol.IPatrolRouteFactory;
import com.dotop.pipe.web.api.factory.patrol.IPatrolTaskFactory;
import com.dotop.pipe.web.api.factory.patrol.IPatrolTaskTimerFactory;
import com.dotop.pipe.web.api.factory.user.IUserFactory;
import com.dotop.pipe.api.service.patrol.IPatrolTaskTimerService;
import com.dotop.pipe.auth.cas.web.IAuthCasWeb;
import com.dotop.pipe.auth.core.vo.auth.LoginCas;
import com.dotop.pipe.core.bo.patrol.PatrolTaskTimerBo;
import com.dotop.pipe.core.enums.CycleTemplateEnum;
import com.dotop.pipe.core.vo.patrol.PatrolRouteVo;
import com.dotop.pipe.core.vo.patrol.PatrolTaskTimerVo;
import com.dotop.smartwater.dependence.core.common.RootModel;
import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.BeanUtils;
import com.dotop.smartwater.dependence.core.utils.DateUtils;
import com.dotop.smartwater.dependence.core.utils.UuidUtils;
import com.dotop.smartwater.project.module.core.auth.vo.UserVo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class PatrolTaskTimerFactoryImpl implements IPatrolTaskTimerFactory {
    private final static Logger logger = LogManager.getLogger(PatrolTaskTimerFactoryImpl.class);

    @Autowired
    private IAuthCasWeb iAuthCasApi;

    @Autowired
    private IPatrolTaskTimerService iPatrolTaskTimerService;

    @Autowired
    private IUserFactory iUserFactory;

    @Autowired
    private IPatrolRouteFactory iPatrolRouteFactory;

    @Autowired
    private IPatrolTaskFactory iPatrolTaskFactory;

    @Autowired
    private ApplicationContext context;
    @Autowired
    IPatrolTaskTimerFactory proxy;

    @PostConstruct
    private void init() {
        proxy = context.getBean(IPatrolTaskTimerFactory.class);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
    public PatrolTaskTimerVo add(PatrolTaskTimerForm patrolTaskTimerForm) throws FrameworkRuntimeException {
        try {
            LoginCas loginCas = iAuthCasApi.get();
            String operEid = loginCas.getOperEid();
            String userId = loginCas.getUserId();
            String userBy = loginCas.getUserName();
            PatrolTaskTimerBo patrolTaskTimerBo = BeanUtils.copyProperties(patrolTaskTimerForm,
                    PatrolTaskTimerBo.class);
            Date curr = new Date();
            patrolTaskTimerBo.setCurr(curr);
            patrolTaskTimerBo.setUserBy(userBy);
            patrolTaskTimerBo.setEnterpriseId(operEid);
            patrolTaskTimerBo.setIsDel(RootModel.NOT_DEL);
            // 默认执行
            patrolTaskTimerBo.setStatus(0);
            patrolTaskTimerBo.setRunTimes(0);
            PatrolTaskTimerVo patrolTaskTimerVo = iPatrolTaskTimerService.add(patrolTaskTimerBo);
            // 调用定时任务（单个执行）
            runTimer(patrolTaskTimerForm);
            return patrolTaskTimerVo;
        } catch (DataAccessException e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public PatrolTaskTimerVo get(PatrolTaskTimerForm patrolTaskTimerForm) throws FrameworkRuntimeException {
        try {
            PatrolTaskTimerBo patrolTaskTimerBo = BeanUtils.copy(patrolTaskTimerForm, PatrolTaskTimerBo.class);
            patrolTaskTimerBo.setEnterpriseId(iAuthCasApi.get().getOperEid());
            PatrolTaskTimerVo patrolTaskTimerVo = iPatrolTaskTimerService.get(patrolTaskTimerBo);
            List<UserVo> userList = iUserFactory.getUserList(iAuthCasApi.get().getOperEid());
            List<String> handlerUserIds = patrolTaskTimerVo.getHandlerUserIds();
            // 仅筛选必要信息返回
            List<UserVo> collect = userList.stream().filter(s -> handlerUserIds.indexOf(s.getUserid()) >= 0)
                    .map(s -> {
                        UserVo userVo = new UserVo();
                        userVo.setUserid(s.getUserid());
                        userVo.setName(s.getName());
                        userVo.setEnterpriseid(s.getEnterpriseid());
                        return userVo;
                    }).collect(Collectors.toList());
            patrolTaskTimerVo.setHandlerUserMap(collect);
            return patrolTaskTimerVo;
        } catch (DataAccessException e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public Pagination<PatrolTaskTimerVo> page(PatrolTaskTimerForm patrolTaskTimerForm) throws FrameworkRuntimeException {
        try {
            PatrolTaskTimerBo patrolTaskTimerBo = BeanUtils.copy(patrolTaskTimerForm, PatrolTaskTimerBo.class);
            patrolTaskTimerBo.setEnterpriseId(iAuthCasApi.get().getOperEid());

            Pagination<PatrolTaskTimerVo> page = iPatrolTaskTimerService.page(patrolTaskTimerBo);
            List<PatrolTaskTimerVo> data = page.getData();
            List<UserVo> userList = iUserFactory.getUserList(iAuthCasApi.get().getOperEid());
            for (PatrolTaskTimerVo patrolTaskTimerVo : data) {
                List<String> handlerUserIds = patrolTaskTimerVo.getHandlerUserIds();
                // 仅筛选必要信息返回
                List<UserVo> collect = userList.stream().filter(s -> handlerUserIds.indexOf(s.getUserid()) >= 0)
                        .map(s -> {
                            UserVo userVo = new UserVo();
                            userVo.setUserid(s.getUserid());
                            userVo.setName(s.getName());
                            userVo.setEnterpriseid(s.getEnterpriseid());
                            return userVo;
                        }).collect(Collectors.toList());
                patrolTaskTimerVo.setHandlerUserMap(collect);
            }
            return page;
        } catch (DataAccessException e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public List<PatrolTaskTimerVo> list(PatrolTaskTimerForm patrolTaskTimerForm) throws FrameworkRuntimeException {
        try {
            PatrolTaskTimerBo patrolTaskTimerBo = BeanUtils.copy(patrolTaskTimerForm, PatrolTaskTimerBo.class);
            patrolTaskTimerBo.setEnterpriseId(iAuthCasApi.get().getOperEid());
            return iPatrolTaskTimerService.list(patrolTaskTimerBo);
        } catch (DataAccessException e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public PatrolTaskTimerVo edit(PatrolTaskTimerForm patrolTaskTimerForm) throws FrameworkRuntimeException {
        try {
            Date cuur = new Date();
            PatrolTaskTimerBo patrolTaskTimerBo = BeanUtils.copy(patrolTaskTimerForm, PatrolTaskTimerBo.class);
            patrolTaskTimerBo.setEnterpriseId(iAuthCasApi.get().getOperEid());
            patrolTaskTimerBo.setUserBy(iAuthCasApi.get().getUserName());
            patrolTaskTimerBo.setCurr(cuur);
            return iPatrolTaskTimerService.edit(patrolTaskTimerBo);
        } catch (DataAccessException e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public PatrolTaskTimerVo editWithOutAuth(PatrolTaskTimerForm patrolTaskTimerForm) throws FrameworkRuntimeException {
        try {
            Date cuur = new Date();
            PatrolTaskTimerBo patrolTaskTimerBo = BeanUtils.copy(patrolTaskTimerForm, PatrolTaskTimerBo.class);
            patrolTaskTimerBo.setUserBy("system");
            patrolTaskTimerBo.setCurr(cuur);
            return iPatrolTaskTimerService.edit(patrolTaskTimerBo);
        } catch (DataAccessException e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public String del(PatrolTaskTimerForm patrolTaskTimerForm) throws FrameworkRuntimeException {
        try {
            PatrolTaskTimerBo patrolTaskTimerBo = BeanUtils.copy(patrolTaskTimerForm, PatrolTaskTimerBo.class);
            patrolTaskTimerBo.setEnterpriseId(iAuthCasApi.get().getOperEid());
            patrolTaskTimerBo.setIsDel(RootModel.DEL);
            return iPatrolTaskTimerService.del(patrolTaskTimerBo);
        } catch (DataAccessException e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public PatrolTaskTimerVo editStatus(PatrolTaskTimerForm patrolTaskTimerForm) {
        try {
            if (patrolTaskTimerForm.getStatus() != null) {
                PatrolTaskTimerBo patrolTaskTimerBo = BeanUtils.copy(patrolTaskTimerForm, PatrolTaskTimerBo.class);
                patrolTaskTimerBo.setEnterpriseId(iAuthCasApi.get().getOperEid());
                return iPatrolTaskTimerService.editStatus(patrolTaskTimerBo);
            } else {
                return null;
            }
        } catch (DataAccessException e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public void runTimer(PatrolTaskTimerForm patrolTaskTimerForm) throws FrameworkRuntimeException {
        // 查询列表
        Date now = new Date();
        patrolTaskTimerForm.setEndDate(now);
        patrolTaskTimerForm.setStatus(0);
        patrolTaskTimerForm.setRunTimesFlag(true);
        PatrolTaskTimerBo patrolTaskTimerBo = BeanUtils.copy(patrolTaskTimerForm, PatrolTaskTimerBo.class);
        List<PatrolTaskTimerVo> list = iPatrolTaskTimerService.list(patrolTaskTimerBo);
        // 计算当前时间是否要执行
        for (PatrolTaskTimerVo timerVo : list) {
            try {
                proxy.runTimerForTransactional(timerVo, now);
            } catch (Exception e) {
                logger.error(LogMsg.to(e, e.getMessage()));
            }

        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
    public void runTimerForTransactional(PatrolTaskTimerVo timerVo, Date now) {
        try {
            Date lastTime = timerVo.getLastTime();
            if (lastTime == null || !DateUtils.formatDate(lastTime).equals(DateUtils.formatDate(now))) {
                Date nextDate = CycleTemplateEnum.getNextDate(timerVo.getCycleTemplate(), timerVo.getBasicTime(), timerVo.getCycle(), now);
                if (DateUtils.formatDate(nextDate).equals(DateUtils.formatDate(now))) {
                    //执行定时任务
                    //复制路线
                    Date date = new Date();
                    String dateStr = DateUtils.format(date, "yyyyMMddHHmmss");
                    PatrolRouteForm patrolRouteForm = new PatrolRouteForm();
                    patrolRouteForm.setPatrolRouteId(timerVo.getPatrolRouteId());
                    patrolRouteForm.setEnterpriseId(timerVo.getEnterpriseId());
                    PatrolRouteVo patrolRouteVo = iPatrolRouteFactory.get(patrolRouteForm);
                    PatrolRouteForm copy = BeanUtils.copy(patrolRouteVo, PatrolRouteForm.class);
                    copy.setPatrolRouteId(null);
                    copy.setCode(copy.getCode() + dateStr);
                    copy.setName(copy.getName() + dateStr);
                    copy.setEnterpriseId(timerVo.getEnterpriseId());
                    List<String> taskIds = new ArrayList<>();
                    for (PatrolRouteTaskForm task : copy.getTasks()) {
                        task.setPatrolRouteTaskId(UuidUtils.getUuid());
                        taskIds.add(task.getPatrolRouteTaskId());
                    }
                    copy.setTaskIds(taskIds);
                    List<String> pointIds = new ArrayList<>();
                    for (PatrolRoutePointForm point : copy.getPatrolRoutePoints()) {
                        point.setPatrolRoutePointId(UuidUtils.getUuid());
                        pointIds.add(point.getPatrolRoutePointId());
                    }
                    copy.setPatrolRoutePointIds(pointIds);
                    PatrolRouteVo add = iPatrolRouteFactory.addWithOutAuth(copy);
                    //发起任务
                    PatrolTaskForm patrolTaskForm = new PatrolTaskForm();
                    patrolTaskForm.setPatrolRouteId(add.getPatrolRouteId());
                    patrolTaskForm.setCode(add.getCode());
                    patrolTaskForm.setName(add.getName());
                    patrolTaskForm.setHandlerUserIds(timerVo.getHandlerUserIds());
                    patrolTaskForm.setStartTime(nextDate);
                    patrolTaskForm.setEndTime(DateUtils.day(nextDate, timerVo.getLimit()));
                    patrolTaskForm.setTaskIds(add.getTaskIds());
                    patrolTaskForm.setTasks(copy.getTasks());
                    patrolTaskForm.setReason(add.getReason());
                    patrolTaskForm.setDesc(add.getDesc());
                    patrolTaskForm.setEnterpriseId(timerVo.getEnterpriseId());
                    iPatrolTaskFactory.addWithOutAuth(patrolTaskForm);
                    //设置最后执行时间，已执行次数+1
                    timerVo.setLastTime(now);
                    timerVo.setRunTimes(timerVo.getRunTimes() + 1);
                    PatrolTaskTimerForm copy1 = BeanUtils.copy(timerVo, PatrolTaskTimerForm.class);
                    editWithOutAuth(copy1);
                }
            }
        } catch (Exception e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

}
