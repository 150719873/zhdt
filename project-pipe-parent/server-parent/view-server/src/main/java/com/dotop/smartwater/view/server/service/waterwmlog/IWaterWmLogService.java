package com.dotop.smartwater.view.server.service.waterwmlog;


import com.dotop.smartwater.view.server.core.device.form.WaterWmLogForm;
import com.dotop.smartwater.view.server.core.device.vo.WaterWmLogVo;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;

import java.util.List;

/**
 *
 */
public interface IWaterWmLogService {

    void adds(List<WaterWmLogForm> waterWmLogForms) throws FrameworkRuntimeException;

    Pagination<WaterWmLogVo> page(WaterWmLogForm waterWmLogForm) throws FrameworkRuntimeException;
}
