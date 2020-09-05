package com.dotop.pipe.server.rest.service.patrol;

import com.dotop.pipe.core.form.PatrolTaskForm;
import com.dotop.pipe.core.vo.patrol.PatrolTaskVo;
import com.dotop.pipe.web.api.factory.patrol.IPatrolTaskFactory;
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
@RequestMapping("/patroltask")
public class PatrolTaskController implements BaseController<PatrolTaskForm> {

    private static final Logger logger = LogManager.getLogger(PatrolTaskController.class);

    @Resource
    private IPatrolTaskFactory iPatrolTaskFactory;



    @Override
    @PostMapping(value = "/get", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    public String get(@RequestBody PatrolTaskForm patrolTaskForm) throws FrameworkRuntimeException {
        PatrolTaskVo patrolTaskVo = iPatrolTaskFactory.get(patrolTaskForm);
        return resp(patrolTaskVo);
    }

    @Override
    @PostMapping(value = "/page", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    public String page(@RequestBody PatrolTaskForm patrolTaskForm) throws FrameworkRuntimeException {
        Pagination<PatrolTaskVo> page = iPatrolTaskFactory.page(patrolTaskForm);
        return resp(page);
    }

    @Override
    @PostMapping(value = "/add", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    public String add(@RequestBody PatrolTaskForm patrolTaskForm) throws FrameworkRuntimeException {
        PatrolTaskVo patrolTaskVo = iPatrolTaskFactory.add(patrolTaskForm);
        return resp(patrolTaskVo);
    }

    @Override
    @PostMapping(value = "/del", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    public String del(@RequestBody PatrolTaskForm patrolTaskForm) throws FrameworkRuntimeException {
        String del = iPatrolTaskFactory.del(patrolTaskForm);
        return resp(del);
    }

    @Override
    @PostMapping(value = "/edit", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    public String edit(@RequestBody PatrolTaskForm patrolTaskForm) throws FrameworkRuntimeException {
        PatrolTaskVo patrolTaskVo = iPatrolTaskFactory.edit(patrolTaskForm);
        return resp(patrolTaskVo);
    }

    @PostMapping(value = "/edit/status", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    public String editStatus(@RequestBody PatrolTaskForm patrolTaskForm) throws FrameworkRuntimeException {
        PatrolTaskVo patrolTaskVo = iPatrolTaskFactory.editStatus(patrolTaskForm);
        return resp(patrolTaskVo);
    }


    @Override
    @PostMapping(value = "/list", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    public String list(@RequestBody PatrolTaskForm patrolTaskForm) throws FrameworkRuntimeException {
        List<PatrolTaskVo> list = iPatrolTaskFactory.list(patrolTaskForm);
        return resp(list);
    }

}
