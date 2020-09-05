package com.dotop.pipe.api.service.device;

import com.dotop.pipe.core.bo.report.NightReadingBo;
import com.dotop.pipe.core.vo.report.NightReadingVo;
import com.dotop.smartwater.dependence.core.common.BaseService;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;

import java.util.List;

/**
 * 设备
 *
 *
 */
public interface INightReadingService extends BaseService<NightReadingBo, NightReadingVo> {

    List<NightReadingVo> listDevices(NightReadingBo nightReadingBo) throws FrameworkRuntimeException;
}
