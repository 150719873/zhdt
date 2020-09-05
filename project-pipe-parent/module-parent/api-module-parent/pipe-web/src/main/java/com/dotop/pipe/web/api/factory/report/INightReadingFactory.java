package com.dotop.pipe.web.api.factory.report;

import com.dotop.pipe.core.form.NightReadingForm;
import com.dotop.pipe.core.vo.report.NightReadingVo;
import com.dotop.smartwater.dependence.core.common.BaseFactory;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;

import java.util.List;

public interface INightReadingFactory extends BaseFactory<NightReadingForm, NightReadingVo> {

    List<NightReadingVo> listByDevices(NightReadingForm nightReadingForm) throws FrameworkRuntimeException;
}
