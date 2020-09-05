package com.dotop.smartwater.project.server.water.rest.service.operation;

import com.dotop.smartwater.dependence.core.utils.JSONUtils;
import com.dotop.smartwater.project.module.core.auth.config.WaterClientConfig;
import com.dotop.smartwater.project.module.core.auth.local.AuthCasClient;
import com.dotop.smartwater.project.module.core.auth.vo.UserVo;
import com.dotop.smartwater.project.module.core.water.form.OperationLogForm;
import com.dotop.smartwater.project.server.water.WaterServerApplication;
import com.dotop.water.tool.service.AppInf;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
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

/**

 * @date 2019/3/5.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = WaterServerApplication.class)
public class OperationLogControllerTest {
	@Autowired
	private WebApplicationContext context;

	private MockMvc mvc;

	@Before
	public void setUp() throws Exception {
		mvc = MockMvcBuilders.webAppContextSetup(context).build();
		WaterClientConfig.WaterCasUrl = "http://localhost:39999/water-cas";
		String userid = "177";
		String ticket = "cc01491a-32ba-4c38-ba7a-da10091c22b6";
		UserVo user = AppInf.getUserBaseInfo(userid, ticket);
		user.setTicket(ticket);
		AuthCasClient.add(user);
	}

	@Test
	public void testGet() throws Exception {
		OperationLogForm param = new OperationLogForm();
		mvc.perform(
				//指定接口
				MockMvcRequestBuilders.post("/operationLog/get")
						.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
						.content(JSONUtils.toJSONString(param))
				//判定状态
		).andExpect(MockMvcResultMatchers.status().isOk())
				//打印结果
				.andDo(MockMvcResultHandlers.print())
				//跟预期是否一致
				.andExpect(MockMvcResultMatchers.content().string(Matchers.notNullValue()));
	}

	@Test
	public void testPage() throws Exception {
		OperationLogForm param = new OperationLogForm();
		mvc.perform(
				//指定接口
				MockMvcRequestBuilders.post("/operationLog/page")
						.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
						.content(JSONUtils.toJSONString(param))
				//判定状态
		).andExpect(MockMvcResultMatchers.status().isOk())
				//打印结果
				.andDo(MockMvcResultHandlers.print())
				//跟预期是否一致
				.andExpect(MockMvcResultMatchers.content().string(Matchers.notNullValue()));
	}

	@Test
	public void testEdit() throws Exception {
		OperationLogForm param = new OperationLogForm();
		param.setTitle("test create");
		param.setDesc("test create");
		mvc.perform(
				//指定接口
				MockMvcRequestBuilders.post("/operationLog/edit")
						.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
						.content(JSONUtils.toJSONString(param))
				//判定状态
		).andExpect(MockMvcResultMatchers.status().isOk())
				//打印结果
				.andDo(MockMvcResultHandlers.print())
				//跟预期是否一致
				.andExpect(MockMvcResultMatchers.content().string(Matchers.notNullValue()));
	}

}