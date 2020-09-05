package com.dotop.pipe.server.rest.service.workorder;

import com.dotop.pipe.core.form.WorkOrderForm;
import com.dotop.pipe.core.vo.workorder.WorkOrderVo;
import com.dotop.pipe.web.api.factory.workorder.IWorkOrderFactory;
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
@RequestMapping("/workorder")
public class WorkOrderController  implements BaseController<WorkOrderForm> {

    private static final Logger logger = LogManager.getLogger(WorkOrderController.class);

    @Resource
    private IWorkOrderFactory iWorkOrderFactory;



    @Override
    @PostMapping(value = "/get", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    public String get(@RequestBody WorkOrderForm workOrderForm) throws FrameworkRuntimeException {
        WorkOrderVo workOrderVo = iWorkOrderFactory.get(workOrderForm);
        return resp(workOrderVo);

    }

    @Override
    @PostMapping(value = "/page", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    public String page(@RequestBody WorkOrderForm workOrderForm) throws FrameworkRuntimeException {
        Pagination<WorkOrderVo> page = iWorkOrderFactory.page(workOrderForm);
        return resp(page);
    }

    @Override
    @PostMapping(value = "/add", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    public String add(@RequestBody WorkOrderForm workOrderForm) throws FrameworkRuntimeException {
        WorkOrderVo workOrderVo = iWorkOrderFactory.add(workOrderForm);
        return resp(workOrderVo);
    }

    @Override
    @PostMapping(value = "/del", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    public String del(@RequestBody WorkOrderForm workOrderForm) throws FrameworkRuntimeException {
        String del = iWorkOrderFactory.del(workOrderForm);
        return resp(del);
    }

    @Override
    @PostMapping(value = "/edit", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    public String edit(@RequestBody WorkOrderForm workOrderForm) throws FrameworkRuntimeException {
        WorkOrderVo workOrderVo = iWorkOrderFactory.edit(workOrderForm);
        return resp(workOrderVo);
    }

    @PostMapping(value = "/edit/status", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    public String editStatus(@RequestBody WorkOrderForm workOrderForm) throws FrameworkRuntimeException {
        WorkOrderVo workOrderVo = iWorkOrderFactory.editStatus(workOrderForm);
        return resp(workOrderVo);
    }


    @Override
    @PostMapping(value = "/list", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    public String list(@RequestBody WorkOrderForm workOrderForm) throws FrameworkRuntimeException {
        List<WorkOrderVo> list = iWorkOrderFactory.list(workOrderForm);
        return resp(list);
    }


    @PostMapping(value = "/ifPass", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
    public String ifPass(@RequestBody WorkOrderForm workOrderForm) throws FrameworkRuntimeException {
         WorkOrderVo workOrderVo = iWorkOrderFactory.ifPass(workOrderForm);
        return resp(workOrderVo);
    }

}
