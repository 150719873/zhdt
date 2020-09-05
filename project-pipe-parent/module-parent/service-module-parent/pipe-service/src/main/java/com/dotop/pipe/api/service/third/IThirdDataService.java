package com.dotop.pipe.api.service.third;

import com.dotop.pipe.core.bo.third.ThirdDataBo;
import com.dotop.pipe.core.vo.third.ThirdDataVo;
import com.dotop.smartwater.dependence.core.common.BaseService;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;

import java.util.List;

/**
 *
 */
public interface IThirdDataService extends BaseService<ThirdDataBo, ThirdDataVo> {

    @Override
    List<ThirdDataVo> list(ThirdDataBo thirdDataBo) throws FrameworkRuntimeException;

    ThirdDataVo get(String enterpriseId, String deviceId, String deviceCode) throws FrameworkRuntimeException;

    @Override
    ThirdDataVo add(ThirdDataBo thirdDataBo) throws FrameworkRuntimeException;

    @Override
    ThirdDataVo edit(ThirdDataBo thirdDataBo) throws FrameworkRuntimeException;
}
