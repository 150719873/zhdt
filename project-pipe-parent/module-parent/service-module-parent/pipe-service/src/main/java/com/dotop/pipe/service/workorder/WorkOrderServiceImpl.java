package com.dotop.pipe.service.workorder;

import com.alibaba.fastjson.JSONObject;
import com.dotop.pipe.api.dao.brustpipe.IBrustPipeDao;
import com.dotop.pipe.api.dao.workorder.IWorkOrderDao;
import com.dotop.pipe.api.service.workorder.IWorkOrderService;
import com.dotop.pipe.core.bo.workOrder.WorkOrderBo;
import com.dotop.pipe.core.dto.brustpipe.BrustPipeDto;
import com.dotop.pipe.core.dto.workorder.WorkOrderDto;
import com.dotop.pipe.core.vo.workorder.WorkOrderVo;
import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.BeanUtils;
import com.dotop.smartwater.dependence.core.utils.UuidUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WorkOrderServiceImpl implements IWorkOrderService {

    private static final Logger logger = LogManager.getLogger(WorkOrderServiceImpl.class);

    @Autowired
    private IWorkOrderDao iWorkOrderDao;

    @Autowired
    private IBrustPipeDao iBrustPipeDao;

    @Override
    public WorkOrderVo add(WorkOrderBo workOrderBo) throws FrameworkRuntimeException {
        try {
            // 参数转换
            WorkOrderDto workOrderDto = BeanUtils.copy(workOrderBo, WorkOrderDto.class);
            if (workOrderDto.getRecordData() != null) {
                WorkOrderVo byRecordData = iWorkOrderDao.getByRecordData(workOrderDto);
                if (byRecordData != null) {
                    throw new FrameworkRuntimeException(BaseExceptionConstants.DUPLICATE_KEY_ERROR, "该条信息已经发起过工单了");
                }
            }
            if (workOrderDto.getWorkOrderId() == null) {
                workOrderDto.setWorkOrderId(UuidUtils.getUuid());
                iWorkOrderDao.add(workOrderDto);
            } else {
                iWorkOrderDao.edit(workOrderDto);
            }
            WorkOrderVo workOrderVo = BeanUtils.copy(workOrderDto, WorkOrderVo.class);
            return workOrderVo;
        } catch (DataAccessException e) {
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public WorkOrderVo get(WorkOrderBo workOrderBo) throws FrameworkRuntimeException {
        try {
            // 参数转换
            WorkOrderDto workOrderDto = BeanUtils.copy(workOrderBo, WorkOrderDto.class);
            WorkOrderVo workOrderVo = iWorkOrderDao.get(workOrderDto);

            return workOrderVo;
        } catch (DataAccessException e) {
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public Pagination<WorkOrderVo> page(WorkOrderBo workOrderBo) throws FrameworkRuntimeException {
        try {
            // 参数转换
            WorkOrderDto workOrderDto = BeanUtils.copy(workOrderBo, WorkOrderDto.class);

            // 操作数据
            Page<Object> pageHelper = PageHelper.startPage(workOrderBo.getPage(), workOrderBo.getPageCount());
            List<WorkOrderVo> list = iWorkOrderDao.list(workOrderDto);
            // 拼接数据返回
            return new Pagination<>(workOrderBo.getPage(), workOrderBo.getPageCount(), list, pageHelper.getTotal());
        } catch (DataAccessException e) {
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public List<WorkOrderVo> list(WorkOrderBo workOrderBo) throws FrameworkRuntimeException {
        try {
            // 参数转换
            WorkOrderDto workOrderDto = BeanUtils.copy(workOrderBo, WorkOrderDto.class);
            // 操作数据
            List<WorkOrderVo> list = iWorkOrderDao.list(workOrderDto);
            return list;
        } catch (DataAccessException e) {
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public WorkOrderVo edit(WorkOrderBo workOrderBo) throws FrameworkRuntimeException {
        try {
            // 参数转换
            WorkOrderDto workOrderDto = BeanUtils.copy(workOrderBo, WorkOrderDto.class);
            iWorkOrderDao.edit(workOrderDto);
            WorkOrderVo workOrderVo = BeanUtils.copy(workOrderDto, WorkOrderVo.class);
            if (workOrderVo != null && "1".equals(workOrderVo.getStatus())) {
                WorkOrderVo tempOrderVo = iWorkOrderDao.get(workOrderDto);
                if (tempOrderVo.getRecordData() != null) {
                    JSONObject jsonObject = com.alibaba.fastjson.JSON.parseObject(tempOrderVo.getRecordData());
                    if ("brustPipe".equals(jsonObject.get("type")) && jsonObject.get("id") != null) {
                        BrustPipeDto brustPipeDto = new BrustPipeDto();
                        brustPipeDto.setBrustPipeId(jsonObject.get("id").toString());
                        brustPipeDto.setStatus("1"); // 已完成
                        brustPipeDto.setEnterpriseId(workOrderBo.getEnterpriseId());
                        iBrustPipeDao.edit(brustPipeDto);
                    }
                }
            }
            return workOrderVo;
        } catch (DataAccessException e) {
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public String del(WorkOrderBo workOrderBo) throws FrameworkRuntimeException {
        try {
            // 参数转换
            WorkOrderDto workOrderDto = BeanUtils.copy(workOrderBo, WorkOrderDto.class);
            iWorkOrderDao.del(workOrderDto);
            return null;
        } catch (DataAccessException e) {
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }

    @Override
    public WorkOrderVo editStatus(WorkOrderBo workOrderBo) {
        try {
            // 参数转换
            WorkOrderDto workOrderDto = BeanUtils.copy(workOrderBo, WorkOrderDto.class);
            iWorkOrderDao.editStatus(workOrderDto);
            WorkOrderVo workOrderVo = BeanUtils.copy(workOrderDto, WorkOrderVo.class);
            return workOrderVo;
        } catch (DataAccessException e) {
            throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
        }
    }
}
