package com.dotop.smartwater.view.server.service.area;

import com.dotop.smartwater.view.server.core.area.form.AreaForm;
import com.dotop.smartwater.view.server.core.area.vo.AreaVo;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;

import java.util.List;

/**
 *
 */
public interface IAreaService {

    List<AreaVo> listDma(AreaForm areaForm) throws FrameworkRuntimeException;
}
