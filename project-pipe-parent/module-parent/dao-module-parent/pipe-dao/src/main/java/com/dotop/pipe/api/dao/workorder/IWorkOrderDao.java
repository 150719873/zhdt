package com.dotop.pipe.api.dao.workorder;

import com.dotop.pipe.core.dto.workorder.WorkOrderDto;
import com.dotop.pipe.core.vo.workorder.WorkOrderVo;
import com.dotop.smartwater.dependence.core.common.BaseDao;

import java.util.List;

public interface IWorkOrderDao extends BaseDao<WorkOrderDto, WorkOrderVo> {
    /**
     * 修改状态
     *
     * @param workOrderDto
     */
    void editStatus(WorkOrderDto workOrderDto);


    /**
     * 新增
     *
     * @param workOrderDto
     */
    @Override
    void add(WorkOrderDto workOrderDto);

    /**
     * 查询全部数据
     *
     * @param workOrderDto
     * @return
     */
    @Override
    List<WorkOrderVo> list(WorkOrderDto workOrderDto);

    /**
     * 删除
     *
     * @param workOrderDto
     * @return
     */
    @Override
    Integer del(WorkOrderDto workOrderDto);

    /**
     * 更新
     *
     * @param workOrderDto
     * @return
     */
    @Override
    Integer edit(WorkOrderDto workOrderDto);

    /**
     * 获取某一条数据
     *
     * @param workOrderDto
     * @return
     */
    @Override
    WorkOrderVo get(WorkOrderDto workOrderDto);

    WorkOrderVo getByRecordData(WorkOrderDto workOrderDto);

}
