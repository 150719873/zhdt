package com.dotop.smartwater.project.server.water;

import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.dotop.smartwater.project.module.core.auth.local.AuthCasClient;
import com.dotop.smartwater.project.module.core.auth.vo.UserVo;

//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = WaterServerApplication.class)
public class WaterServerApplicationTests {

	@Autowired
	private WebApplicationContext context;

	public MockMvc mvc;

	// 初始化执行
	@Before
	public void setUp() throws Exception {
		UserVo user = new UserVo();
		user.setAccount("account");
		user.setName("test");
		user.setEnterpriseid("44");

		//admin
		user.setType(UserVo.USER_TYPE_ADMIN);

		AuthCasClient.add(user);
		mvc = MockMvcBuilders.webAppContextSetup(context).build();
	}

}
