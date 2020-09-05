package com.dotop.smartwater.project.server.water.rest.service.device;

import com.dotop.smartwater.project.server.water.WaterServerApplication;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

/**

 * @date 2019/3/1.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = WaterServerApplication.class)
public class DeviceControllerTest {
	@Autowired
	private WebApplicationContext context;

	private MockMvc mvc;

//	@Before
//	public void setUp() throws Exception {
//		mvc = MockMvcBuilders.webAppContextSetup(context).build();
//		WaterClientConfig.WaterCasUrl = "http://localhost:39999/water-cas";
//
//		UserParamVo user = AppInf.AppLogin("user1", "123456","44");
//		UserVo u = new UserVo();
//		BeanUtils.copyProperties(user,u);
//		AuthCasClient.add(u);
//	}

//	@Test
//	public void testClose() throws Exception {
//		DeviceForm param = new DeviceForm();
//		mvc.perform(
//				//指定接口
//				MockMvcRequestBuilders.post("/Device/Close")
//						.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
//						.content(JSONUtils.toJSONString(param))
//				//判定状态
//		).andExpect(MockMvcResultMatchers.status().isOk())
//				//打印结果
//				.andDo(MockMvcResultHandlers.print())
//				//跟预期是否一致
//				.andExpect(MockMvcResultMatchers.content().string(Matchers.notNullValue()));
//	}

//	@Test
//	public void testOpen() throws Exception {
//		DeviceForm param = new DeviceForm();
//		mvc.perform(
//				//指定接口
//				MockMvcRequestBuilders.post("/Device/Open")
//						.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
//						.content(JSONUtils.toJSONString(param))
//				//判定状态
//		).andExpect(MockMvcResultMatchers.status().isOk())
//				//打印结果
//				.andDo(MockMvcResultHandlers.print())
//				//跟预期是否一致
//				.andExpect(MockMvcResultMatchers.content().string(Matchers.notNullValue()));
//	}

//	@Test
//	public void testGetDeviceWater() throws Exception {
//		DeviceForm param = new DeviceForm();
//		mvc.perform(
//				//指定接口
//				MockMvcRequestBuilders.post("/Device/getDeviceWater")
//						.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
//						.content(JSONUtils.toJSONString(param))
//				//判定状态
//		).andExpect(MockMvcResultMatchers.status().isOk())
//				//打印结果
//				.andDo(MockMvcResultHandlers.print())
//				//跟预期是否一致
//				.andExpect(MockMvcResultMatchers.content().string(Matchers.notNullValue()));
//	}

//	@Test
//	public void testGetDeviceDetail() throws Exception {
//		DeviceForm param = new DeviceForm();
//		mvc.perform(
//				//指定接口
//				MockMvcRequestBuilders.post("/Device/getDeviceDetail")
//						.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
//						.content(JSONUtils.toJSONString(param))
//				//判定状态
//		).andExpect(MockMvcResultMatchers.status().isOk())
//				//打印结果
//				.andDo(MockMvcResultHandlers.print())
//				//跟预期是否一致
//				.andExpect(MockMvcResultMatchers.content().string(Matchers.notNullValue()));
//	}

//	@Test
//	public void testGetDeviceInfo() throws Exception {
//		DeviceForm param = new DeviceForm();
//		param.setDeveui("ED0201186A00004D");
//		mvc.perform(
//				//指定接口
//				MockMvcRequestBuilders.post("/Device/getDeviceInfo")
//						.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
//						.content(JSONUtils.toJSONString(param))
//				//判定状态
//		).andExpect(MockMvcResultMatchers.status().isOk())
//				//打印结果
//				.andDo(MockMvcResultHandlers.print())
//				//跟预期是否一致
//				.andExpect(MockMvcResultMatchers.content().string(Matchers.notNullValue()));
//	}

//	@Test
//	public void testGetUpCorrectionDatas() throws Exception {
//		Map<String, String>  param = new HashMap<>(16);
//		mvc.perform(
//				//指定接口
//				MockMvcRequestBuilders.post("/Device/getUpCorrectionDatas")
//						.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
//						.content(JSONUtils.toJSONString(param))
//				//判定状态
//		).andExpect(MockMvcResultMatchers.status().isOk())
//				//打印结果
//				.andDo(MockMvcResultHandlers.print())
//				//跟预期是否一致
//				.andExpect(MockMvcResultMatchers.content().string(Matchers.notNullValue()));
//	}

//	@Test
//	public void testGetDownCorrectionDatas() throws Exception {
//		Map<String, String>  param = new HashMap<>(16);
//		mvc.perform(
//				//指定接口
//				MockMvcRequestBuilders.post("/Device/getDownCorrectionDatas")
//						.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
//						.content(JSONUtils.toJSONString(param))
//				//判定状态
//		).andExpect(MockMvcResultMatchers.status().isOk())
//				//打印结果
//				.andDo(MockMvcResultHandlers.print())
//				//跟预期是否一致
//				.andExpect(MockMvcResultMatchers.content().string(Matchers.notNullValue()));
//	}

//	@Test
//	public void testDownCorrectionOper() throws Exception {
//		Map<String, String>  param = new HashMap<>(16);
//		mvc.perform(
//				//指定接口
//				MockMvcRequestBuilders.post("/Device/downCorrectionOper")
//						.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
//						.content(JSONUtils.toJSONString(param))
//				//判定状态
//		).andExpect(MockMvcResultMatchers.status().isOk())
//				//打印结果
//				.andDo(MockMvcResultHandlers.print())
//				//跟预期是否一致
//				.andExpect(MockMvcResultMatchers.content().string(Matchers.notNullValue()));
//	}

//	@Test
//	public void testDownRest() throws Exception {
//		Map<String, String>  param = new HashMap<>(16);
//		mvc.perform(
//				//指定接口
//				MockMvcRequestBuilders.post("/Device/downRest")
//						.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
//						.content(JSONUtils.toJSONString(param))
//				//判定状态
//		).andExpect(MockMvcResultMatchers.status().isOk())
//				//打印结果
//				.andDo(MockMvcResultHandlers.print())
//				//跟预期是否一致
//				.andExpect(MockMvcResultMatchers.content().string(Matchers.notNullValue()));
//	}

}