package com.dotop.smartwater.project.third.meterread.webservice.service.water;

import com.dotop.smartwater.dependence.core.common.BaseService;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.water.bo.OwnerBo;
import com.dotop.smartwater.project.module.core.water.bo.OwnerRecordBo;
import com.dotop.smartwater.project.module.core.water.vo.OwnerRecordVo;
import com.dotop.smartwater.project.module.core.water.vo.OwnerVo;

import java.util.List;

/**
 * 对接水务系统的业主数据接口
 *
 *
 */
public interface IWaterOwnerService extends BaseService<OwnerBo, OwnerVo> {

    /**
     * 根据条件查询水司的业主信息，提取业主的ownerid、水表编号、业主名称、是否带阀；条件包括分页和水司；
     *
     * @param owernBo
     * @return
     * @throws FrameworkRuntimeException
     */
    @Override
    List<OwnerVo> list(OwnerBo owernBo) throws FrameworkRuntimeException;


    /**
     * 根据条件查询水司的业主信息，提取业主的ownerid、水表编号、业主名称、是否带阀；条件包括业主id和水司；
     *
     * @param owernBo
     * @return
     * @throws FrameworkRuntimeException
     */
    @Override
    OwnerVo get(OwnerBo owernBo) throws FrameworkRuntimeException;

    /**
     * 根据条件查询水司的业主信息，提取业主的ownerid、水表编号、业主名称、是否带阀；条件包括分页和水司；
     */
    @Override
    Pagination<OwnerVo> page(OwnerBo owernBo) throws FrameworkRuntimeException;


    /**
     * 换表记录
     */
    List<OwnerRecordVo> ownerRecords(OwnerRecordBo ownerRecordBo) throws FrameworkRuntimeException;

}
