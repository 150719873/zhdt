package com.dotop.pipe.server.rest.service.patrol;

import com.dotop.pipe.core.form.PatrolRouteTaskForm;
import com.dotop.pipe.core.vo.patrol.PatrolRouteTaskVo;
import com.dotop.pipe.web.api.factory.patrol.IPatrolRouteTaskFactory;
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
@RequestMapping("/patrolroutetask")
public class PatrolRouteTaskController implements BaseController<PatrolRouteTaskForm> {

    private static final Logger logger = LogManager.getLogger(PatrolRouteTaskController.class);

    @Resource
    private IPatrolRouteTaskFactory iPatrolRouteTaskFactory;



    @Override
    @PostMapping(value = "/get", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    public String get(@RequestBody PatrolRouteTaskForm patrolRouteTaskForm) throws FrameworkRuntimeException {
        PatrolRouteTaskVo patrolRouteTaskVo = iPatrolRouteTaskFactory.get(patrolRouteTaskForm);
        return resp(patrolRouteTaskVo);

    }

    @Override
    @PostMapping(value = "/page", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    public String page(@RequestBody PatrolRouteTaskForm patrolRouteTaskForm) throws FrameworkRuntimeException {
        Pagination<PatrolRouteTaskVo> page = iPatrolRouteTaskFactory.page(patrolRouteTaskForm);
        return resp(page);
    }

    @Override
    @PostMapping(value = "/add", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    public String add(@RequestBody PatrolRouteTaskForm patrolRouteTaskForm) throws FrameworkRuntimeException {
        PatrolRouteTaskVo patrolRouteTaskVo = iPatrolRouteTaskFactory.add(patrolRouteTaskForm);
        return resp(patrolRouteTaskVo);
    }

    @Override
    @PostMapping(value = "/del", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    public String del(@RequestBody PatrolRouteTaskForm patrolRouteTaskForm) throws FrameworkRuntimeException {
        String del = iPatrolRouteTaskFactory.del(patrolRouteTaskForm);
        return resp(del);
    }

    @Override
    @PostMapping(value = "/edit", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    public String edit(@RequestBody PatrolRouteTaskForm patrolRouteTaskForm) throws FrameworkRuntimeException {
        PatrolRouteTaskVo patrolRouteTaskVo = iPatrolRouteTaskFactory.edit(patrolRouteTaskForm);
        return resp(patrolRouteTaskVo);
    }

    @PostMapping(value = "/edit/status", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    public String editStatus(@RequestBody PatrolRouteTaskForm patrolRouteTaskForm) throws FrameworkRuntimeException {
        PatrolRouteTaskVo patrolRouteTaskVo = iPatrolRouteTaskFactory.editStatus(patrolRouteTaskForm);
        return resp(patrolRouteTaskVo);
    }


    @Override
    @PostMapping(value = "/list", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    public String list(@RequestBody PatrolRouteTaskForm patrolRouteTaskForm) throws FrameworkRuntimeException {
        List<PatrolRouteTaskVo> list = iPatrolRouteTaskFactory.list(patrolRouteTaskForm);
        return resp(list);
    }

}
