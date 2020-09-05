package com.dotop.pipe.server.rest.service.patrol;

import com.dotop.pipe.core.form.PatrolTaskTimerForm;
import com.dotop.pipe.core.vo.patrol.PatrolTaskTimerVo;
import com.dotop.pipe.web.api.factory.patrol.IPatrolTaskTimerFactory;
import com.dotop.smartwater.dependence.core.common.BaseController;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;


@RestController
@RequestMapping("/patroltasktimer")
public class PatrolTaskTimerController implements BaseController<PatrolTaskTimerForm> {

    private static final Logger logger = LogManager.getLogger(PatrolTaskTimerController.class);

    @Resource
    private IPatrolTaskTimerFactory iPatrolTaskTimerFactory;


    @Override
    @PostMapping(value = "/get", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    public String get(@RequestBody PatrolTaskTimerForm patrolTaskForm) throws FrameworkRuntimeException {
        PatrolTaskTimerVo patrolTaskVo = iPatrolTaskTimerFactory.get(patrolTaskForm);
        return resp(patrolTaskVo);
    }

    @Override
    @PostMapping(value = "/page", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    public String page(@RequestBody PatrolTaskTimerForm patrolTaskForm) throws FrameworkRuntimeException {
        Pagination<PatrolTaskTimerVo> page = iPatrolTaskTimerFactory.page(patrolTaskForm);
        return resp(page);
    }

    @Override
    @PostMapping(value = "/add", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    public String add(@RequestBody PatrolTaskTimerForm patrolTaskForm) throws FrameworkRuntimeException {
        PatrolTaskTimerVo patrolTaskVo = iPatrolTaskTimerFactory.add(patrolTaskForm);
        return resp(patrolTaskVo);
    }

    @Override
    @PostMapping(value = "/del", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    public String del(@RequestBody PatrolTaskTimerForm patrolTaskForm) throws FrameworkRuntimeException {
        String del = iPatrolTaskTimerFactory.del(patrolTaskForm);
        return resp(del);
    }

    @Override
    @PostMapping(value = "/edit", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    public String edit(@RequestBody PatrolTaskTimerForm patrolTaskForm) throws FrameworkRuntimeException {
        PatrolTaskTimerVo patrolTaskVo = iPatrolTaskTimerFactory.edit(patrolTaskForm);
        return resp(patrolTaskVo);
    }

    @PostMapping(value = "/edit/status", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    public String editStatus(@RequestBody PatrolTaskTimerForm patrolTaskForm) throws FrameworkRuntimeException {
        PatrolTaskTimerVo patrolTaskVo = iPatrolTaskTimerFactory.editStatus(patrolTaskForm);
        return resp(patrolTaskVo);
    }


    @Override
    @PostMapping(value = "/list", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    public String list(@RequestBody PatrolTaskTimerForm patrolTaskForm) throws FrameworkRuntimeException {
        List<PatrolTaskTimerVo> list = iPatrolTaskTimerFactory.list(patrolTaskForm);
        return resp(list);
    }

    /**
     * 手动执行定时器
     *
     * @param patrolTaskForm
     * @return
     * @throws FrameworkRuntimeException
     */
    @PostMapping(value = "/runTimer", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    public String runTimer(@RequestBody PatrolTaskTimerForm patrolTaskForm) throws FrameworkRuntimeException {
        iPatrolTaskTimerFactory.runTimer(patrolTaskForm);
        return resp();
    }
}
