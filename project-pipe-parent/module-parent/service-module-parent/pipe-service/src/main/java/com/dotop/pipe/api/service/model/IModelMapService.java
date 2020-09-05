package com.dotop.pipe.api.service.model;

import com.dotop.pipe.core.vo.model.ModelMapVo;
import com.dotop.smartwater.dependence.core.common.BaseBo;
import com.dotop.smartwater.dependence.core.common.BaseService;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;

// 模型与设备关系
public interface IModelMapService extends BaseService<BaseBo, ModelMapVo> {

	// 获取模型与设备关系
	public ModelMapVo getByDeviceId(String enterpriseId, String deviceId) throws FrameworkRuntimeException;
}
