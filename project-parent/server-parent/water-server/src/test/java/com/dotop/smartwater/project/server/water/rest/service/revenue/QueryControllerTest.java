package com.dotop.smartwater.project.server.water.rest.service.revenue;

import com.dotop.smartwater.dependence.core.utils.JSONUtils;
import com.dotop.smartwater.project.module.core.auth.config.WaterClientConfig;
import com.dotop.smartwater.project.module.core.auth.local.AuthCasClient;
import com.dotop.smartwater.project.module.core.auth.vo.UserVo;
import com.dotop.smartwater.project.module.core.water.form.OwnerForm;
import com.dotop.smartwater.project.module.core.water.form.customize.QueryForm;
import com.dotop.smartwater.project.module.core.water.form.customize.StatisticsWaterForm;
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

import java.util.ArrayList;
import java.util.List;

/**

 * @date 2019/3/1.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = WaterServerApplication.class)
public class QueryControllerTest {

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
	public void testGetDataTotal() throws Exception {
		mvc.perform(
				//指定接口
				MockMvcRequestBuilders.post("/Query/getDataTotal")
						.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
				//判定状态
		).andExpect(MockMvcResultMatchers.status().isOk())
				//打印结果
				.andDo(MockMvcResultHandlers.print())
				//跟预期是否一致
				.andExpect(MockMvcResultMatchers.content().string(Matchers.notNullValue()));
	}

	@Test
	public void testOriginal() throws Exception {
		QueryForm param = new QueryForm();
		param.setPage(1);
		param.setPageCount(10);
		List<String> nodes = new ArrayList<>();
		nodes.add("44");
		nodes.add("106");
		nodes.add("107");
		nodes.add("100007");
		nodes.add("100007");
		nodes.add("100010");
		nodes.add("100011");
		param.setNodeIds(nodes);
		mvc.perform(
				//指定接口
				MockMvcRequestBuilders.post("/Query/original")
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
	public void testExportOriginal() throws Exception {
		mvc.perform(
				//指定接口
				MockMvcRequestBuilders.post("/Query/exportOriginal")
						.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
				//判定状态
		).andExpect(MockMvcResultMatchers.status().isOk())
				//打印结果
				.andDo(MockMvcResultHandlers.print())
				//跟预期是否一致
				.andExpect(MockMvcResultMatchers.content().string(Matchers.notNullValue()));
	}

	@Test
	public void testTransfer() throws Exception {
		mvc.perform(
				//指定接口
				MockMvcRequestBuilders.post("/Query/transfer")
						.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
				//判定状态
		).andExpect(MockMvcResultMatchers.status().isOk())
				//打印结果
				.andDo(MockMvcResultHandlers.print())
				//跟预期是否一致
				.andExpect(MockMvcResultMatchers.content().string(Matchers.notNullValue()));
	}

	@Test
	public void testCancel() throws Exception {
		QueryForm param = new QueryForm();
		param.setPage(1);
		param.setPageCount(10);
		mvc.perform(
				//指定接口
				MockMvcRequestBuilders.post("/Query/cancel")
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
	public void testGetLowBattery() throws Exception {
		QueryForm param = new QueryForm();
		param.setPage(1);
		param.setPageCount(10);

		mvc.perform(
				//指定接口
				MockMvcRequestBuilders.post("/Query/getLowBattery")
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
	public void testExportLowBattery() throws Exception {
		mvc.perform(
				//指定接口
				MockMvcRequestBuilders.post("/Query/exportLowBattery")
						.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
				//判定状态
		).andExpect(MockMvcResultMatchers.status().isOk())
				//打印结果
				.andDo(MockMvcResultHandlers.print())
				//跟预期是否一致
				.andExpect(MockMvcResultMatchers.content().string(Matchers.notNullValue()));
	}

	@Test
	public void testDailyGetStatisticsWater() throws Exception {
		StatisticsWaterForm param = new StatisticsWaterForm();

		mvc.perform(
				//指定接口
				MockMvcRequestBuilders.post("/Query/daily_getStatisticsWater")
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
	public void testMonthGetStatisticsWater() throws Exception {
		StatisticsWaterForm param = new StatisticsWaterForm();

		mvc.perform(
				//指定接口
				MockMvcRequestBuilders.post("/Query/month_getStatisticsWater")
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
	public void testOwnerinfo() throws Exception {
		OwnerForm param = new OwnerForm();
		param.setUserno("DXSS01");
		mvc.perform(
				//指定接口
				MockMvcRequestBuilders.post("/Query/ownerinfo")
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