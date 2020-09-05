package com.dotop.smartwater.view.server.api.factory.view;

import com.dotop.smartwater.view.server.core.area.form.AreaForm;
import com.dotop.smartwater.view.server.core.area.vo.AreaVo;
import com.dotop.smartwater.dependence.core.common.BaseFactory;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;

import java.util.List;

/**
 *
 */
public interface IAreaFactory extends BaseFactory<AreaForm, AreaVo> {

    /**
     * 查询dma分区
     * @param areaForm
     * @return
     * @throws FrameworkRuntimeException
     */
    List<AreaVo> listDma(AreaForm areaForm) throws FrameworkRuntimeException;
}
