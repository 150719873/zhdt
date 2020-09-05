package com.dotop.pipe.web.api.factory.runingdata;

import com.dotop.pipe.core.form.RuningDataForm;
import com.dotop.pipe.core.vo.runingdata.RuningDataVo;
import com.dotop.smartwater.dependence.core.common.BaseFactory;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;

import java.util.List;

public interface IRuningDataFactory extends BaseFactory<RuningDataForm, RuningDataVo> {

    @Override
    RuningDataVo add(RuningDataForm runingDataForm) throws FrameworkRuntimeException;

    @Override
    Pagination<RuningDataVo> page(RuningDataForm runingDataForm) throws FrameworkRuntimeException;

    void makeData(RuningDataForm runingDataForm) throws FrameworkRuntimeException;

    String del(RuningDataForm runingDataForm) throws FrameworkRuntimeException;

    List<RuningDataVo> getRuningTask(RuningDataForm runingDataForm) throws FrameworkRuntimeException;


    @Override
    RuningDataVo edit(RuningDataForm runingDataForm) throws FrameworkRuntimeException;
}
