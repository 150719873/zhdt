package com.dotop.pipe.server.rest.service.patrol;

import com.dotop.pipe.core.form.PatrolRouteForm;
import com.dotop.pipe.core.vo.patrol.PatrolRouteVo;
import com.dotop.pipe.web.api.factory.patrol.IPatrolRouteFactory;
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
@RequestMapping("/patrolroute")
public class PatrolRouteController implements BaseController<PatrolRouteForm> {

    private static final Logger logger = LogManager.getLogger(PatrolRouteController.class);

    @Resource
    private IPatrolRouteFactory iPatrolRouteFactory;


    @Override
    @PostMapping(value = "/get", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    public String get(@RequestBody PatrolRouteForm patrolRouteForm) throws FrameworkRuntimeException {
        PatrolRouteVo patrolRouteVo = iPatrolRouteFactory.get(patrolRouteForm);
        return resp(patrolRouteVo);

    }

    @Override
    @PostMapping(value = "/page", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    public String page(@RequestBody PatrolRouteForm patrolRouteForm) throws FrameworkRuntimeException {
        Pagination<PatrolRouteVo> page = iPatrolRouteFactory.page(patrolRouteForm);
        return resp(page);
    }

    @Override
    @PostMapping(value = "/add", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    public String add(@RequestBody PatrolRouteForm patrolRouteForm) throws FrameworkRuntimeException {
        PatrolRouteVo patrolRouteVo = iPatrolRouteFactory.add(patrolRouteForm);
        return resp(patrolRouteVo);
    }

    @Override
    @PostMapping(value = "/del", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    public String del(@RequestBody PatrolRouteForm patrolRouteForm) throws FrameworkRuntimeException {
        String del = iPatrolRouteFactory.del(patrolRouteForm);
        return resp(del);
    }

    @Override
    @PostMapping(value = "/edit", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    public String edit(@RequestBody PatrolRouteForm patrolRouteForm) throws FrameworkRuntimeException {
        PatrolRouteVo patrolRouteVo = iPatrolRouteFactory.edit(patrolRouteForm);
        return resp(patrolRouteVo);
    }

    @Override
    @PostMapping(value = "/list", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    public String list(@RequestBody PatrolRouteForm patrolRouteForm) throws FrameworkRuntimeException {
        List<PatrolRouteVo> list = iPatrolRouteFactory.list(patrolRouteForm);
        return resp(list);
    }

}
