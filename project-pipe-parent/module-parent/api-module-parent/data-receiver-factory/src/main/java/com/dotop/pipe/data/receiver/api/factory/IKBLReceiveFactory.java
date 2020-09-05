package com.dotop.pipe.data.receiver.api.factory;

import com.alibaba.fastjson.JSONObject;

/**
 * 通用推送数据接口
 */
public interface IKBLReceiveFactory {

	void handle(String sensorCode, JSONObject rdata);
}
