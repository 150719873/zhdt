package com.dotop.pipe.server.rest.service.patrol;

import com.dotop.pipe.core.form.PatrolRouteTemplateForm;
import com.dotop.pipe.core.vo.patrol.PatrolRouteTemplateVo;
import com.dotop.pipe.web.api.factory.patrol.IPatrolRouteTemplateFactory;
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

/**
 *
 */
@RestController
@RequestMapping("/patrolroutetemplate")
public class PatrolRouteTemplateController implements BaseController<PatrolRouteTemplateForm> {

    private static final Logger logger = LogManager.getLogger(PatrolRouteTemplateController.class);

    @Resource
    private IPatrolRouteTemplateFactory iPatrolRouteTemplateFactory;


    @Override
    @PostMapping(value = "/get", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    public String get(@RequestBody PatrolRouteTemplateForm patrolRouteTemplateForm) throws FrameworkRuntimeException {
        PatrolRouteTemplateVo patrolRouteTemplateVo = iPatrolRouteTemplateFactory.get(patrolRouteTemplateForm);
        return resp(patrolRouteTemplateVo);

    }

    @Override
    @PostMapping(value = "/page", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    public String page(@RequestBody PatrolRouteTemplateForm patrolRouteTemplateForm) throws FrameworkRuntimeException {
        Pagination<PatrolRouteTemplateVo> page = iPatrolRouteTemplateFactory.page(patrolRouteTemplateForm);
        return resp(page);
    }

    @Override
    @PostMapping(value = "/add", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    public String add(@RequestBody PatrolRouteTemplateForm patrolRouteTemplateForm) throws FrameworkRuntimeException {
        PatrolRouteTemplateVo patrolRouteTemplateVo = iPatrolRouteTemplateFactory.add(patrolRouteTemplateForm);
        return resp(patrolRouteTemplateVo);
    }

    @Override
    @PostMapping(value = "/del", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    public String del(@RequestBody PatrolRouteTemplateForm patrolRouteTemplateForm) throws FrameworkRuntimeException {
        String del = iPatrolRouteTemplateFactory.del(patrolRouteTemplateForm);
        return resp(del);
    }

    @Override
    @PostMapping(value = "/edit", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    public String edit(@RequestBody PatrolRouteTemplateForm patrolRouteTemplateForm) throws FrameworkRuntimeException {
        PatrolRouteTemplateVo patrolRouteTemplateVo = iPatrolRouteTemplateFactory.edit(patrolRouteTemplateForm);
        return resp(patrolRouteTemplateVo);
    }
}
