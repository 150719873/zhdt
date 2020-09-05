package com.dotop.smartwater.view.server.service.workorder;


import com.dotop.pipe.core.bo.workOrder.WorkOrderBo;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.view.server.core.workorder.vo.WorkOrderSummaryVo;

import java.util.List;

/**
 *
 */
public interface IWorkOrderSummaryService {


    List<WorkOrderSummaryVo> workOrderProcessing(WorkOrderBo workOrderBo) throws FrameworkRuntimeException;

}
