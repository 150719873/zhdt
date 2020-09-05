package com.dotop.pipe.api.service.runingdata;

import com.dotop.pipe.core.bo.runingdata.RuningDataBo;
import com.dotop.pipe.core.vo.runingdata.RuningDataVo;
import com.dotop.smartwater.dependence.core.common.BaseService;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;

import java.util.List;

public interface IRuningDataService extends BaseService<RuningDataBo, RuningDataVo> {


    @Override
    RuningDataVo add(RuningDataBo runingDataBo) throws FrameworkRuntimeException;

    @Override
    Pagination<RuningDataVo> page(RuningDataBo runingDataBo) throws FrameworkRuntimeException;

    void makeData(RuningDataBo runingDataBo) throws FrameworkRuntimeException;

    String del(RuningDataBo runingDataBo) throws FrameworkRuntimeException;

    List<RuningDataVo> getRuningTask(RuningDataBo runingDataBo) throws FrameworkRuntimeException;

    @Override
    RuningDataVo edit(RuningDataBo runingDataBo) throws FrameworkRuntimeException;
}
