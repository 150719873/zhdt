package com.dotop.pipe.data.receiver.api.factory;

import com.alibaba.fastjson.JSONObject;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;

public interface IReceiveFactory {

	public void handle(String sensorCode, JSONObject rdata) throws FrameworkRuntimeException;
}
