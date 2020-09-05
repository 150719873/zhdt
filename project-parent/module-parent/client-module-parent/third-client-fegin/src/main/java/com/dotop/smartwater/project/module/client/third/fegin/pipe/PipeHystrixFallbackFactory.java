package com.dotop.smartwater.project.module.client.third.fegin.pipe;

import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.third.bo.pipe.PipeDeviceBo;
import com.dotop.smartwater.project.module.core.third.bo.pipe.PipeProductBo;
import com.dotop.smartwater.project.module.core.third.bo.pipe.PipeWorkOrder;
import com.dotop.smartwater.project.module.core.third.vo.pipe.PipeDeviceVo;
import com.dotop.smartwater.project.module.core.third.vo.pipe.PipeProductVo;
import feign.hystrix.FallbackFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

/**

 */
public class PipeHystrixFallbackFactory implements FallbackFactory<IPipeFeginClient> {

	private static final Logger LOGGER = LogManager.getLogger(PipeHystrixFallbackFactory.class);

	@Override
	public IPipeFeginClient create(Throwable ex) {
		return new IPipeFeginClient() {
			@Override
			public Pagination<PipeProductVo> productPage(PipeProductBo pipeProductBo) {
				LOGGER.error(LogMsg.to("ex", ex));
				LOGGER.error(LogMsg.to("productBo", pipeProductBo));
				return new Pagination<>(pipeProductBo.getPage(), pipeProductBo.getPageSize(), new ArrayList<>(), 0);
			}

			@Override
			public Pagination<PipeDeviceVo> devicePage(PipeDeviceBo pipeDeviceBo) {
				LOGGER.error(LogMsg.to("ex", ex));
				LOGGER.error(LogMsg.to("deviceBo", pipeDeviceBo));
				return new Pagination<>(pipeDeviceBo.getPage(), pipeDeviceBo.getPageSize(), new ArrayList<>(), 0);
			}

			@Override
			public void edit(PipeWorkOrder pipeWorkOrder) {
				LOGGER.error(LogMsg.to("ex", ex, "device", pipeWorkOrder));
			}


		};
	}

}
