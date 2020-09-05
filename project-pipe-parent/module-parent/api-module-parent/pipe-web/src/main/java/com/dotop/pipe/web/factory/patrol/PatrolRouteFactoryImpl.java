package com.dotop.pipe.web.factory.patrol;

import com.dotop.pipe.web.api.factory.patrol.IPatrolRouteFactory;
import com.dotop.pipe.web.api.factory.patrol.IPatrolRoutePointFactory;
import com.dotop.pipe.web.api.factory.patrol.IPatrolRouteTaskFactory;
import com.dotop.pipe.web.api.factory.patrol.IPatrolTaskTimerFactory;
import com.dotop.pipe.api.service.patrol.IPatrolRouteService;
import com.dotop.pipe.auth.cas.web.IAuthCasWeb;
import com.dotop.pipe.auth.core.vo.auth.LoginCas;
import com.dotop.pipe.core.bo.patrol.PatrolRouteBo;
import com.dotop.pipe.core.form.PatrolRouteForm;
import com.dotop.pipe.core.form.PatrolRoutePointForm;
import com.dotop.pipe.core.form.PatrolRouteTaskForm;
import com.dotop.pipe.core.form.PatrolTaskTimerForm;
import com.dotop.pipe.core.vo.patrol.PatrolRoutePointVo;
import com.dotop.pipe.core.vo.patrol.PatrolRouteTaskVo;
import com.dotop.pipe.core.vo.patrol.PatrolRouteVo;
import com.dotop.pipe.core.vo.patrol.PatrolTaskTimerVo;
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
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class PatrolRouteFactoryImpl implements IPatrolRouteFactory {
    private final static Logger logger = LogManager.getLogger(PatrolRouteFactoryImpl.class);

    @Autowired
    private IAuthCasWeb iAuthCasApi;

    @Autowired
    private IPatrolRouteService iPatrolRouteService;

    @Autowired
    private IPatrolRouteTaskFactory iPatrolRouteTaskFactory;

    @Autowired
    private IPatrolRoutePointFactory iPatrolRoutePointFactory;

    @Autowired
    private IPatrolTaskTimerFactory iPatrolTaskTimerFactory;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
    public PatrolRouteVo add(PatrolRouteForm patrolRouteForm) throws FrameworkRuntimeException {
        try {
            LoginCas loginCas = iAuthCasApi.get();
            String operEid = loginCas.getOperEid();
            String userBy = loginCas.getUserName();
            if (!patrolRouteForm.getTasks().isEmpty()) {
                iPatrolRouteTaskFactory.batchAdd(patrolRouteForm.getTasks());
            }
            if (!patrolRouteForm.getPatrolRoutePoints().isEmpty()) {
                iPatrolRoutePointFactory.batchAdd(patrolRouteForm.getPatrolRoutePoints());
            }
            PatrolRouteBo patrolRouteBo = BeanUtils.copyProperties(patrolRouteForm,
                    PatrolRouteBo.class);

            Date curr = new Date();
            patrolRouteBo.setCurr(curr);
            patrolRouteBo.setUserBy(userBy);
            patrolRouteBo.setEnterpriseId(operEid);
            patrolRouteBo.setIsDel(RootModel.NOT_DEL);
            return iPatrolRouteService.add(patrolRouteBo);
        } catch (DataAccessException e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
    public PatrolRouteVo addWithOutAuth(PatrolRouteForm patrolRouteForm) throws FrameworkRuntimeException {
        try {
            String userBy = "system";
            if (!patrolRouteForm.getPatrolRoutePoints().isEmpty()) {
                iPatrolRoutePointFactory.batchAddWithOutAuth(patrolRouteForm.getPatrolRoutePoints());
            }
            if (!patrolRouteForm.getTasks().isEmpty()) {
                iPatrolRouteTaskFactory.batchAddWithOutAuth(patrolRouteForm.getTasks());
            }
            PatrolRouteBo patrolRouteBo = BeanUtils.copyProperties(patrolRouteForm,
                    PatrolRouteBo.class);

            Date curr = new Date();
            patrolRouteBo.setCurr(curr);
            patrolRouteBo.setUserBy(userBy);
            patrolRouteBo.setIsDel(RootModel.NOT_DEL);
            return iPatrolRouteService.add(patrolRouteBo);
        } catch (DataAccessException e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public PatrolRouteVo get(PatrolRouteForm patrolRouteForm) throws FrameworkRuntimeException {
        try {
            PatrolRouteBo patrolRouteBo = BeanUtils.copy(patrolRouteForm, PatrolRouteBo.class);
            if (StringUtils.isBlank(patrolRouteBo.getEnterpriseId())) {
                patrolRouteBo.setEnterpriseId(iAuthCasApi.get().getOperEid());
            }
            PatrolRouteVo patrolRouteVo = iPatrolRouteService.get(patrolRouteBo);
            List<String> taskIds = patrolRouteVo.getTaskIds();
            if (taskIds != null && !taskIds.isEmpty()) {
                PatrolRouteTaskForm patrolRouteTaskForm = new PatrolRouteTaskForm();
                patrolRouteTaskForm.setPatrolRouteTaskIds(taskIds);
                patrolRouteTaskForm.setEnterpriseId(patrolRouteBo.getEnterpriseId());
                List<PatrolRouteTaskVo> patrolRouteTaskList = iPatrolRouteTaskFactory.list(patrolRouteTaskForm);
                // 任务点排序
                List<PatrolRouteTaskVo> collect = patrolRouteTaskList.stream().sorted(Comparator.comparingInt(s -> Integer.parseInt(s.getSort()))).collect(Collectors.toList());
                patrolRouteVo.setTasks(collect);
            }
            List<String> patrolRoutePointIds = patrolRouteVo.getPatrolRoutePointIds();
            if (patrolRoutePointIds != null && !patrolRoutePointIds.isEmpty()) {
                PatrolRoutePointForm patrolRoutePointForm = new PatrolRoutePointForm();
                patrolRoutePointForm.setPatrolRoutePointIds(patrolRoutePointIds);
                patrolRoutePointForm.setEnterpriseId(patrolRouteBo.getEnterpriseId());
                List<PatrolRoutePointVo> patrolRoutePointList = iPatrolRoutePointFactory.list(patrolRoutePointForm);
                // 路线点排序
                List<PatrolRoutePointVo> sort = new ArrayList<>();
                if (patrolRoutePointList != null) {
                    patrolRoutePointIds.forEach((id) -> {
                        patrolRoutePointList.forEach((p) -> {
                            if (id.equals(p.getPatrolRoutePointId())) {
                                sort.add(p);
                            }
                        });
                    });
                }
                patrolRouteVo.setPatrolRoutePoints(sort);
            }
            return patrolRouteVo;
        } catch (DataAccessException e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public Pagination<PatrolRouteVo> page(PatrolRouteForm patrolRouteForm) throws FrameworkRuntimeException {
        try {
            PatrolRouteBo patrolRouteBo = BeanUtils.copy(patrolRouteForm, PatrolRouteBo.class);
            patrolRouteBo.setEnterpriseId(iAuthCasApi.get().getOperEid());
            Pagination<PatrolRouteVo> page = iPatrolRouteService.page(patrolRouteBo);
            List<PatrolRouteVo> data = page.getData();
            for (PatrolRouteVo patrolRouteVo : data) {
                List<String> taskIds = patrolRouteVo.getTaskIds();
                if (taskIds != null && !taskIds.isEmpty()) {
                    PatrolRouteTaskForm patrolRouteTaskForm = new PatrolRouteTaskForm();
                    patrolRouteTaskForm.setPatrolRouteTaskIds(taskIds);
                    List<PatrolRouteTaskVo> patrolRouteTaskList = iPatrolRouteTaskFactory.list(patrolRouteTaskForm);
                    // 任务点排序
                    List<PatrolRouteTaskVo> collect = patrolRouteTaskList.stream().sorted(Comparator.comparingInt(s -> Integer.parseInt(s.getSort()))).collect(Collectors.toList());
                    patrolRouteVo.setTasks(collect);
                }
                List<String> patrolRoutePointIds = patrolRouteVo.getPatrolRoutePointIds();
                if (patrolRoutePointIds != null && !patrolRoutePointIds.isEmpty()) {
                    PatrolRoutePointForm patrolRoutePointForm = new PatrolRoutePointForm();
                    patrolRoutePointForm.setPatrolRoutePointIds(patrolRoutePointIds);
                    List<PatrolRoutePointVo> patrolRoutePointList = iPatrolRoutePointFactory.list(patrolRoutePointForm);
                    // 路线点排序
                    List<PatrolRoutePointVo> sort = new ArrayList<>();
                    if (patrolRoutePointList != null) {
                        patrolRoutePointIds.forEach((id) -> {
                            patrolRoutePointList.forEach((p) -> {
                                if (id.equals(p.getPatrolRoutePointId())) {
                                    sort.add(p);
                                }
                            });
                        });
                    }
                    patrolRouteVo.setPatrolRoutePoints(sort);
                }
            }
            return page;
        } catch (DataAccessException e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public List<PatrolRouteVo> list(PatrolRouteForm patrolRouteForm) throws FrameworkRuntimeException {
        try {
            PatrolRouteBo patrolRouteBo = BeanUtils.copy(patrolRouteForm, PatrolRouteBo.class);
            patrolRouteBo.setEnterpriseId(iAuthCasApi.get().getOperEid());
            return iPatrolRouteService.list(patrolRouteBo);
        } catch (DataAccessException e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
    public PatrolRouteVo edit(PatrolRouteForm patrolRouteForm) throws FrameworkRuntimeException {
        try {
            //新任务点与旧任务点求差集，得出新增任务
            PatrolRouteVo patrolRouteVo = get(patrolRouteForm);
            List<String> taskIds = patrolRouteVo.getTaskIds();
            List<PatrolRouteTaskForm> newTasks = patrolRouteForm.getTasks();
            List<PatrolRouteTaskForm> patrolRouteTaskForms = newTasks.stream().filter(s -> taskIds.indexOf(s.getPatrolRouteTaskId()) == -1).collect(Collectors.toList());
            if (!patrolRouteTaskForms.isEmpty()) {
                iPatrolRouteTaskFactory.batchAdd(patrolRouteTaskForms);
            }
            //新路线点与旧路线点求差集，得出新路线
            List<String> patrolRoutePointIds = patrolRouteVo.getPatrolRoutePointIds();
            List<PatrolRoutePointForm> newPoints = patrolRouteForm.getPatrolRoutePoints();
            List<PatrolRoutePointForm> patrolRoutePointForms = newPoints.stream().filter(s -> patrolRoutePointIds.indexOf(s.getPatrolRoutePointId()) == -1).collect(Collectors.toList());
            if (!patrolRoutePointForms.isEmpty()) {
                iPatrolRoutePointFactory.batchAdd(patrolRoutePointForms);
            }
            PatrolRouteBo patrolRouteBo = BeanUtils.copy(patrolRouteForm, PatrolRouteBo.class);
            patrolRouteBo.setEnterpriseId(iAuthCasApi.get().getOperEid());
            patrolRouteBo.setUserBy(iAuthCasApi.get().getUserName());
            patrolRouteBo.setCurr(new Date());
            //与旧任务点求交集，且数据发生改变，则更新
            List<PatrolRouteTaskVo> oldTask = patrolRouteVo.getTasks();
            List<PatrolRouteTaskForm> tasks = patrolRouteForm.getTasks();
            if (tasks.isEmpty()) {
                patrolRouteBo.setTaskIds(new ArrayList<>());
                // 删除旧任务点
                if (!oldTask.isEmpty()) {
                    iPatrolRouteTaskFactory.batDel(BeanUtils.copy(oldTask, PatrolRouteTaskForm.class));
                }
            }
            List<PatrolRouteTaskForm> updateTasks = tasks.stream().filter(s ->
                    taskIds.indexOf(s.getPatrolRouteTaskId()) >= 0
                    && oldTask.stream().anyMatch(k ->!s.getLongitude().equals(k.getLongitude())
                    || !s.getLatitude().equals(k.getLatitude()) || !s.getTaskTitle().equals(k.getTaskTitle())
                    || !s.getDesc().equals(k.getDesc()) || !s.getSort().equals(k.getSort())
            )).collect(Collectors.toList());
            if (!updateTasks.isEmpty()) {
                iPatrolRouteTaskFactory.batUpdate(updateTasks);
            }
            //与旧路线点求交集，且数据发生改变，则删除旧点添加新点
            if (!newPoints.isEmpty()) {
                List<PatrolRoutePointForm> pointForms = patrolRoutePointIds.stream().filter(s -> newPoints.stream().noneMatch(t -> t.getPatrolRoutePointId().equals(s))).map(s -> {
                    PatrolRoutePointForm p = new PatrolRoutePointForm();
                    p.setPatrolRoutePointId(s);
                    return p;
                }).collect(Collectors.toList());
                if (!pointForms.isEmpty()) {
                    iPatrolRoutePointFactory.batchDel(pointForms);
                }
            }
            return iPatrolRouteService.edit(patrolRouteBo);
        } catch (DataAccessException e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public PatrolRouteVo editWithOutAuth(PatrolRouteForm patrolRouteForm) throws FrameworkRuntimeException {
        try {
            //新任务点与旧任务点求差集，得出新增任务
            PatrolRouteVo patrolRouteVo = get(patrolRouteForm);
            List<String> taskIds = patrolRouteVo.getTaskIds();
            List<PatrolRouteTaskForm> newTasks = patrolRouteForm.getTasks();
            List<PatrolRouteTaskForm> patrolRouteTaskForms = newTasks.stream().filter(s -> taskIds.indexOf(s.getPatrolRouteTaskId()) == -1).collect(Collectors.toList());
            if (!patrolRouteTaskForms.isEmpty()) {
                iPatrolRouteTaskFactory.batchAddWithOutAuth(patrolRouteTaskForms);
            }
            PatrolRouteBo patrolRouteBo = BeanUtils.copy(patrolRouteForm, PatrolRouteBo.class);
            patrolRouteBo.setUserBy("system");
            patrolRouteBo.setCurr(new Date());
            return iPatrolRouteService.edit(patrolRouteBo);
        } catch (DataAccessException e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
    public String del(PatrolRouteForm patrolRouteForm) throws FrameworkRuntimeException {
        try {
            PatrolRouteBo patrolRouteBo = BeanUtils.copy(patrolRouteForm, PatrolRouteBo.class);
            patrolRouteBo.setEnterpriseId(iAuthCasApi.get().getOperEid());
            patrolRouteBo.setIsDel(RootModel.DEL);
            PatrolRouteVo patrolRouteVo = get(patrolRouteForm);
            if (StringUtils.isNotBlank(patrolRouteVo.getPatrolTaskId())) {
                throw new FrameworkRuntimeException("该路线已绑定了任务！");
            }
            PatrolTaskTimerForm patrolTaskTimerForm = new PatrolTaskTimerForm();
            patrolTaskTimerForm.setPatrolRouteId(patrolRouteForm.getPatrolRouteId());
            List<PatrolTaskTimerVo> list = iPatrolTaskTimerFactory.list(patrolTaskTimerForm);
            if (!list.isEmpty()) {
                throw new FrameworkRuntimeException("该路线已绑定了计划任务！");
            }
            List<PatrolRoutePointForm> collect = patrolRouteVo.getPatrolRoutePointIds().stream().map(s -> {
                PatrolRoutePointForm p = new PatrolRoutePointForm();
                p.setPatrolRoutePointId(s);
                return p;
            }).collect(Collectors.toList());
            List<PatrolRouteTaskForm> collect2 = patrolRouteVo.getTaskIds().stream().map(s -> {
                PatrolRouteTaskForm t = new PatrolRouteTaskForm();
                t.setPatrolRouteTaskId(s);
                return t;
            }).collect(Collectors.toList());
            if (!collect.isEmpty()) {
                iPatrolRoutePointFactory.batchDel(collect);
            }
            if (!collect2.isEmpty()) {
                iPatrolRouteTaskFactory.batDel(collect2);
            }
            return iPatrolRouteService.del(patrolRouteBo);
        } catch (DataAccessException e) {
            logger.error(LogMsg.to(e));
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }
}
