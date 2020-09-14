package com.dotop.smartwater.project.third.concentrator.rest.service;

import com.dotop.smartwater.project.third.concentrator.api.ICollectorFactory;
import com.dotop.smartwater.project.third.concentrator.core.vo.CollectorVo;
import com.dotop.smartwater.dependence.core.common.BaseController;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.third.concentrator.core.form.CollectorForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 采集器入口层辑接口
 *
 *
 */
@RestController
@RequestMapping("/collector")
public class CollectorController implements BaseController<CollectorForm> {

    @Autowired
    private ICollectorFactory iCollectorFactory;

    /*@Autowired
    private IAreaFactory iAreaFactory;*/

    @Override
    @PostMapping(value = "/get", produces = GlobalContext.PRODUCES)
    public String get(@RequestBody CollectorForm collectorForm) throws FrameworkRuntimeException {
        CollectorVo collectorVo = iCollectorFactory.get(collectorForm);
        return resp(ResultCode.Success, ResultCode.SUCCESS, collectorVo);

    }

    @Override
    @PostMapping(value = "/page", produces = GlobalContext.PRODUCES)
    public String page(@RequestBody CollectorForm collectorForm) throws FrameworkRuntimeException {
        Pagination<CollectorVo> page = iCollectorFactory.page(collectorForm);
        return resp(ResultCode.Success, ResultCode.SUCCESS, page);
    }

    @Override
    @PostMapping(value = "/add", produces = GlobalContext.PRODUCES)
    public String add(@RequestBody CollectorForm collectorForm) throws FrameworkRuntimeException {
        CollectorVo collectorVo = iCollectorFactory.add(collectorForm);
        return resp(ResultCode.Success, ResultCode.SUCCESS, collectorVo);
    }

    @Override
    @PostMapping(value = "/del", produces = GlobalContext.PRODUCES)
    public String del(@RequestBody CollectorForm collectorForm) throws FrameworkRuntimeException {
        String del = iCollectorFactory.del(collectorForm);
        return resp(ResultCode.Success, ResultCode.SUCCESS, del);
    }

    @Override
    @PostMapping(value = "/edit", produces = GlobalContext.PRODUCES)
    public String edit(@RequestBody CollectorForm collectorForm) throws FrameworkRuntimeException {
        CollectorVo collectorVo = iCollectorFactory.edit(collectorForm);
        return resp(ResultCode.Success, ResultCode.SUCCESS, collectorVo);
    }

    @PostMapping(value = "/edit/status", produces = GlobalContext.PRODUCES)
    public String editStatus(@RequestBody CollectorForm collectorForm) throws FrameworkRuntimeException {
        CollectorVo collectorVo = iCollectorFactory.editStatus(collectorForm);
        return resp(ResultCode.Success, ResultCode.SUCCESS, collectorVo);
    }

    /**
     * 采集器档案分页。条件：集中器id
     */
    @PostMapping(value = "/page/archive", produces = GlobalContext.PRODUCES)
    public String pageArchive(@RequestBody CollectorForm collectorForm) throws FrameworkRuntimeException {
        Pagination<CollectorVo> collectorVoPagination = iCollectorFactory.pageArchive(collectorForm);
        return resp(ResultCode.Success, ResultCode.SUCCESS, collectorVoPagination);
    }

    @Override
    @PostMapping(value = "/list", produces = GlobalContext.PRODUCES)
    public String list(@RequestBody CollectorForm collectorForm) throws FrameworkRuntimeException {
        List<CollectorVo> list = iCollectorFactory.list(collectorForm);
        return resp(ResultCode.Success, ResultCode.SUCCESS, list);
    }

    @PostMapping(value = "/getAreaList", produces = GlobalContext.PRODUCES)
    public String getAreaList(@RequestBody CollectorForm collectorForm) throws FrameworkRuntimeException {
        List<CollectorVo> list = iCollectorFactory.list(collectorForm);

        return resp(ResultCode.Success, ResultCode.SUCCESS, list);
    }
}
