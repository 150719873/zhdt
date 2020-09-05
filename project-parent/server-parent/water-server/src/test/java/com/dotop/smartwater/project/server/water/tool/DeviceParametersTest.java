package com.dotop.smartwater.project.server.water.tool;

import java.util.HashMap;
import java.util.Map;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.alibaba.fastjson.JSONObject;
import com.dotop.smartwater.project.server.water.WaterServerApplicationTests;

public class DeviceParametersTest {

//	@Test
//	public void testHello() throws Exception {
//
//		Map<String, Object> map = new HashMap<>();
//		map.put("serialNumber", "1112");
//		map.put("deviceName", "LOra带阀");
//		map.put("mode", "NB电信");
//		map.put("valveStatus", "1");
//		map.put("valveType", "1");
//		map.put("unit", "1000L");
//		map.put("sensorType", "电信");
//		map.put("enterpriseId", "123");
//		mvc.perform(
//				// 指定接口
//				MockMvcRequestBuilders.post("/deviceParameters/add").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
//						.content(JSONObject.toJSONString(map))
//		// 判定状态
//		).andExpect(MockMvcResultMatchers.status().isOk())
//				// 打印结果
//				.andDo(MockMvcResultHandlers.print())
//				// 跟预期是否一致
//				.andExpect(MockMvcResultMatchers.content().string(Matchers.notNullValue()));
//	}
}
