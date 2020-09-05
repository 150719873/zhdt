package com.dotop.smartwater.project.server.water.third;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.client.third.fegin.pipe.IPipeFeginClient;
import com.dotop.smartwater.project.module.core.third.bo.pipe.PipeDeviceBo;
import com.dotop.smartwater.project.module.core.third.bo.pipe.PipeProductBo;
import com.dotop.smartwater.project.module.core.third.vo.pipe.PipeDeviceVo;
import com.dotop.smartwater.project.module.core.third.vo.pipe.PipeProductVo;
import com.dotop.smartwater.project.server.water.WaterServerApplication;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = WaterServerApplication.class)
public class PipeFeginTest {

	private final static Logger logger = LogManager.getLogger(PipeFeginTest.class);

	@Autowired
	private IPipeFeginClient iPipeFeginClient;

	// @Test
	public void productPage() {
		logger.info(LogMsg.to("iPipeFeginClient", iPipeFeginClient));
		PipeProductBo pipeProductBo = new PipeProductBo();
		pipeProductBo.setEnterpriseId("44");
		pipeProductBo.setPage(1);
		pipeProductBo.setPageSize(10);
		Pagination<PipeProductVo> pagination = iPipeFeginClient.productPage(pipeProductBo);
		logger.info(LogMsg.to("pagination", pagination));
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	// @Test
	public void devicePage() {
		logger.info(LogMsg.to("iPipeFeginClient", iPipeFeginClient));
		PipeDeviceBo pipeDeviceBo = new PipeDeviceBo();
		pipeDeviceBo.setEnterpriseId("44");
		pipeDeviceBo.setPage(1);
		pipeDeviceBo.setPageSize(10);
		Pagination<PipeDeviceVo> pagination = iPipeFeginClient.devicePage(pipeDeviceBo);
		logger.info(LogMsg.to("pagination", pagination));
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
