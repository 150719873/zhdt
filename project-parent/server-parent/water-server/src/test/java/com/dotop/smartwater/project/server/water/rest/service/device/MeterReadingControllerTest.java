package com.dotop.smartwater.project.server.water.rest.service.device;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.dotop.smartwater.dependence.core.utils.JSONUtils;
import com.dotop.smartwater.project.module.core.auth.config.WaterClientConfig;
import com.dotop.smartwater.project.module.core.auth.local.AuthCasClient;
import com.dotop.smartwater.project.module.core.auth.vo.UserParamVo;
import com.dotop.smartwater.project.module.core.auth.vo.UserVo;
import com.dotop.smartwater.project.module.core.water.form.MeterReadingTaskForm;
import com.dotop.smartwater.project.server.water.WaterServerApplication;
import com.dotop.water.tool.service.AppInf;

/**

 * @date 2019/3/5.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = WaterServerApplication.class)
public class MeterReadingControllerTest {
	@Autowired
	private WebApplicationContext context;

	private MockMvc mvc;

	@Before
	public void setUp() throws Exception {
		mvc = MockMvcBuilders.webAppContextSetup(context).build();
		WaterClientConfig.WaterCasUrl = "http://localhost:39999/water-cas";
		UserParamVo user = AppInf.appLogin("user1", "123456", "44");
		UserVo u = new UserVo();
		BeanUtils.copyProperties(user, u);
		AuthCasClient.add(u);
	}

	@Test
	public void testGet() throws Exception {
		MeterReadingTaskForm param = new MeterReadingTaskForm();
		mvc.perform(
				// 指定接口
				MockMvcRequestBuilders.post("/meterReading/get").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
						.content(JSONUtils.toJSONString(param))
		// 判定状态
		).andExpect(MockMvcResultMatchers.status().isOk())
				// 打印结果
				.andDo(MockMvcResultHandlers.print())
				// 跟预期是否一致
				.andExpect(MockMvcResultMatchers.content().string(Matchers.notNullValue()));
	}

	@Test
	public void testPage() throws Exception {
		MeterReadingTaskForm param = new MeterReadingTaskForm();
		mvc.perform(
				// 指定接口
				MockMvcRequestBuilders.post("/meterReading/page").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
						.content(JSONUtils.toJSONString(param))
		// 判定状态
		).andExpect(MockMvcResultMatchers.status().isOk())
				// 打印结果
				.andDo(MockMvcResultHandlers.print())
				// 跟预期是否一致
				.andExpect(MockMvcResultMatchers.content().string(Matchers.notNullValue()));
	}

	@Test
	public void testEdit() throws Exception {
		MeterReadingTaskForm param = new MeterReadingTaskForm();
		param.setBatchId("001");
		mvc.perform(
				// 指定接口
				MockMvcRequestBuilders.post("/meterReading/edit").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
						.content(JSONUtils.toJSONString(param))
		// 判定状态
		).andExpect(MockMvcResultMatchers.status().isOk())
				// 打印结果
				.andDo(MockMvcResultHandlers.print())
				// 跟预期是否一致
				.andExpect(MockMvcResultMatchers.content().string(Matchers.notNullValue()));
	}

}