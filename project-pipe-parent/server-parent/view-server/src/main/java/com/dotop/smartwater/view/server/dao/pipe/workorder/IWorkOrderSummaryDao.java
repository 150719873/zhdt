package com.dotop.smartwater.view.server.dao.pipe.workorder;


import com.dotop.pipe.core.dto.workorder.WorkOrderDto;
import com.dotop.smartwater.view.server.core.workorder.vo.WorkOrderSummaryVo;

import java.util.List;

/**
 *
 */
public interface IWorkOrderSummaryDao {


    List<WorkOrderSummaryVo> workOrderProcessing(WorkOrderDto WorkOrderDto);

}
