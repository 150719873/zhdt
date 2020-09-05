package com.dotop.pipe.web.api.factory.workorder;


import com.dotop.pipe.core.form.WorkOrderForm;
import com.dotop.pipe.core.vo.workorder.WorkOrderVo;
import com.dotop.smartwater.dependence.core.common.BaseFactory;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;

import java.util.List;

public interface IWorkOrderFactory extends BaseFactory<WorkOrderForm, WorkOrderVo> {

    @Override
    WorkOrderVo add(WorkOrderForm workOrderForm) throws FrameworkRuntimeException;

    @Override
    WorkOrderVo get(WorkOrderForm workOrderForm) throws FrameworkRuntimeException;

    @Override
    Pagination<WorkOrderVo> page(WorkOrderForm workOrderForm) throws FrameworkRuntimeException;

    @Override
    List<WorkOrderVo> list(WorkOrderForm workOrderForm) throws FrameworkRuntimeException;

    @Override
    WorkOrderVo edit(WorkOrderForm workOrderForm) throws FrameworkRuntimeException;

    @Override
    String del(WorkOrderForm workOrderForm) throws FrameworkRuntimeException;

    WorkOrderVo editStatus(WorkOrderForm workOrderForm);

    WorkOrderVo ifPass(WorkOrderForm workOrderForm);
}
