package com.dotop.pipe.server.rest.service.product;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dotop.pipe.core.form.EnterpriseProductMapForm;
import com.dotop.pipe.core.vo.product.EnterpriseProductMapVo;
import com.dotop.pipe.web.api.factory.product.IEnterpriseProductMapFactory;
import com.dotop.smartwater.dependence.core.common.BaseController;
import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.VerificationUtils;

/**
 *
 * @date 2018/10/30.
 */
@RestController
@RequestMapping("/productMap")
public class EnterpriseProductMapController implements BaseController<EnterpriseProductMapForm> {

	private final static Logger logger = LogManager.getLogger(EnterpriseProductMapController.class);

	@Autowired
	private IEnterpriseProductMapFactory iEnterpriseProductMapFactory;

	/**
	 * 添加产品上线
	 * 
	 * @param enterpriseProductMapForm
	 * @return
	 */
	@Override
	@PostMapping(produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
	public String add(@RequestBody EnterpriseProductMapForm enterpriseProductMapForm) {
		logger.info(LogMsg.to("enterpriseProductMapForm", enterpriseProductMapForm));

		String enterpriseId = enterpriseProductMapForm.getEnterpriseId();
		String productId = enterpriseProductMapForm.getProductId();

		// 验证
		VerificationUtils.string("enterpriseId", enterpriseId);
		VerificationUtils.string("productId", productId);
		EnterpriseProductMapVo enterpriseProductMapVo = iEnterpriseProductMapFactory.add(enterpriseProductMapForm);

		return resp("mapId", enterpriseProductMapVo.getMapId());
	}

	/**
	 * 产品上线分页
	 *
	 * @param enterpriseProductMapForm
	 * @return
	 */
	@Override
	@PostMapping(value = "/page", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
	public String page(@RequestBody EnterpriseProductMapForm enterpriseProductMapForm) {
		logger.info(LogMsg.to("enterpriseProductMapForm", enterpriseProductMapForm));
		Integer page = enterpriseProductMapForm.getPage();
		Integer pageSize = enterpriseProductMapForm.getPageSize();
		String enterpriseId = enterpriseProductMapForm.getEnterpriseId();
		// 验证
		VerificationUtils.integer("page", page);
		VerificationUtils.integer("pageSize", pageSize);
		VerificationUtils.string("enterpriseId", enterpriseId, true);

		Pagination<EnterpriseProductMapVo> pagination = iEnterpriseProductMapFactory.page(enterpriseProductMapForm);

		return resp(pagination);
	}

	/**
	 * 删除产品上线
	 *
	 * @param enterpriseProductMapForm
	 * @return
	 */
	@Override
	@DeleteMapping(value = "/{mapId}", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
	public String del(EnterpriseProductMapForm enterpriseProductMapForm) {
		logger.info(LogMsg.to("enterpriseProductMapForm", enterpriseProductMapForm));
		String mapId = enterpriseProductMapForm.getMapId();
		// 验证
		VerificationUtils.string("mapId", mapId);
		iEnterpriseProductMapFactory.del(enterpriseProductMapForm);

		return resp();
	}

	/**
	 * 添加批量产品上线
	 *
	 * @param enterpriseProductMapForm
	 * @return
	 */
	@PostMapping(value = "/multiple", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
	public String addMultiple(@RequestBody EnterpriseProductMapForm enterpriseProductMapForm) {
		logger.info(LogMsg.to("enterpriseProductMapForm", enterpriseProductMapForm));

		String enterpriseId = enterpriseProductMapForm.getEnterpriseId();
		List<String> productIds = enterpriseProductMapForm.getProductIds();

		// 验证
		VerificationUtils.string("enterpriseId", enterpriseId);
		VerificationUtils.strList("productIds", productIds);
		iEnterpriseProductMapFactory.addMultiple(enterpriseProductMapForm);

		return resp();
	}

	/**
	 * 产品上线列表
	 *
	 * @param enterpriseProductMapForm
	 * @return
	 */
	@Override
	@PostMapping(value = "/list", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
	public String list(@RequestBody EnterpriseProductMapForm enterpriseProductMapForm) {
		logger.info(LogMsg.to("enterpriseProductMapForm", enterpriseProductMapForm));
		String enterpriseId = enterpriseProductMapForm.getEnterpriseId();
		// 验证
		VerificationUtils.string("enterpriseId", enterpriseId);

		List<EnterpriseProductMapVo> list = iEnterpriseProductMapFactory.list(enterpriseProductMapForm);

		return resp(list);
	}
}
