package com.dotop.smartwater.project.module.client.third.fegin.pipe;

import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.third.bo.pipe.PipeDeviceBo;
import com.dotop.smartwater.project.module.core.third.bo.pipe.PipeProductBo;
import com.dotop.smartwater.project.module.core.third.bo.pipe.PipeWorkOrder;
import com.dotop.smartwater.project.module.core.third.vo.pipe.PipeDeviceVo;
import com.dotop.smartwater.project.module.core.third.vo.pipe.PipeProductVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**

 */
@FeignClient(name = "pipe-server", fallbackFactory = PipeHystrixFallbackFactory.class, path = "/pipe-server/")
public interface IPipeFeginClient {

	/**
	 * 产品分页
	 *
	 * @param pipeProductBo
	 * @return
	 */
	@PostMapping(value = "/third/product/page", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
	Pagination<PipeProductVo> productPage(@RequestBody PipeProductBo pipeProductBo);

	/**
	 * 设备分页
	 *
	 * @param pipeDeviceBo
	 * @return
	 */
	@PostMapping(value = "/third/device/page", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
	Pagination<PipeDeviceVo> devicePage(@RequestBody PipeDeviceBo pipeDeviceBo);


	@PostMapping(value = "/workorder/edit", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
	void edit(@RequestBody PipeWorkOrder pipeWorkOrder);
}
