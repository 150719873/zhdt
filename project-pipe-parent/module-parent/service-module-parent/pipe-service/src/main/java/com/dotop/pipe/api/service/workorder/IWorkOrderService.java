package com.dotop.pipe.api.service.workorder;


import com.dotop.pipe.core.bo.workOrder.WorkOrderBo;
import com.dotop.pipe.core.vo.workorder.WorkOrderVo;
import com.dotop.smartwater.dependence.core.common.BaseService;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;

import java.util.List;

/**
 * 爆管管理
 *
 */
public interface IWorkOrderService extends BaseService<WorkOrderBo, WorkOrderVo> {


    @Override
    WorkOrderVo add(WorkOrderBo workOrderBo) throws FrameworkRuntimeException;

    @Override
    WorkOrderVo get(WorkOrderBo workOrderBo) throws FrameworkRuntimeException;

    @Override
    Pagination<WorkOrderVo> page(WorkOrderBo workOrderBo) throws FrameworkRuntimeException;

    @Override
    List<WorkOrderVo> list(WorkOrderBo workOrderBo) throws FrameworkRuntimeException;

    @Override
    WorkOrderVo edit(WorkOrderBo workOrderBo) throws FrameworkRuntimeException;

    @Override
    String del(WorkOrderBo workOrderBo) throws FrameworkRuntimeException;

    WorkOrderVo editStatus(WorkOrderBo workOrderBo);


}