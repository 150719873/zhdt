package com.dotop.smartwater.view.server.service.workorder.impl;

import com.dotop.pipe.core.bo.workOrder.WorkOrderBo;
import com.dotop.pipe.core.dto.workorder.WorkOrderDto;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.utils.BeanUtils;
import com.dotop.smartwater.view.server.core.workorder.vo.WorkOrderSummaryVo;
import com.dotop.smartwater.view.server.dao.pipe.workorder.IWorkOrderSummaryDao;
import com.dotop.smartwater.view.server.service.workorder.IWorkOrderSummaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WorkOrderSummaryServiceImpl implements IWorkOrderSummaryService {

    @Autowired
    private IWorkOrderSummaryDao workOrderSummaryDao;

    @Override
    public List<WorkOrderSummaryVo> workOrderProcessing(WorkOrderBo workOrderBo) throws FrameworkRuntimeException {
        WorkOrderDto workOrderDto = BeanUtils.copyProperties(workOrderBo, WorkOrderDto.class);
        return  workOrderSummaryDao.workOrderProcessing(workOrderDto);
    }
}
