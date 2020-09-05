package com.dotop.pipe.web.api.factory.report;

import com.dotop.pipe.core.vo.report.DeviceAnalysisVo;
import com.dotop.smartwater.dependence.core.common.BaseFactory;
import com.dotop.smartwater.dependence.core.common.BaseForm;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;

public interface IDeviceAnalysisFactory extends BaseFactory<BaseForm, DeviceAnalysisVo> {

	public DeviceAnalysisVo statuss(BaseForm baseForm) throws FrameworkRuntimeException;

	public DeviceAnalysisVo propertys(BaseForm baseForm) throws FrameworkRuntimeException;

}
