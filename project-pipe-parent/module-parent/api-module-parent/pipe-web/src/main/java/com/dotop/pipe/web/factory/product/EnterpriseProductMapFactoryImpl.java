package com.dotop.pipe.web.factory.product;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.dotop.pipe.web.api.factory.product.IEnterpriseProductMapFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dotop.pipe.api.service.product.IEnterpriseProductMapService;
import com.dotop.pipe.auth.cas.web.IAuthCasWeb;
import com.dotop.pipe.auth.core.vo.auth.LoginCas;
import com.dotop.pipe.core.form.EnterpriseProductMapForm;
import com.dotop.pipe.core.vo.product.EnterpriseProductMapVo;
import com.dotop.pipe.core.vo.product.ProductVo;
import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;

/**
 *
 * @date 2018/10/30.
 */
@Component
public class EnterpriseProductMapFactoryImpl implements IEnterpriseProductMapFactory {

	private final static Logger logger = LogManager.getLogger(EnterpriseProductMapFactoryImpl.class);

	@Autowired
	private IAuthCasWeb iAuthCasApi;

	@Autowired
	private IEnterpriseProductMapService iEnterpriseProductMapService;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public EnterpriseProductMapVo add(EnterpriseProductMapForm enterpriseProductMapForm)
			throws FrameworkRuntimeException {
		Date curr = new Date();
		LoginCas loginCas = iAuthCasApi.get();
		String userBy = loginCas.getUserName();
		// 所需参数
		String enterpriseId = enterpriseProductMapForm.getEnterpriseId();
		String productId = enterpriseProductMapForm.getProductId();

		// 检验code是否唯一
		EnterpriseProductMapVo vo = iEnterpriseProductMapService.get(enterpriseId, productId);
		if (vo != null) {
			logger.error(LogMsg.to("ex", BaseExceptionConstants.DUPLICATE_KEY_ERROR, "productId", productId));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DUPLICATE_KEY_ERROR);
		}

		return iEnterpriseProductMapService.add(enterpriseId, productId, curr, userBy);
	}

	@Override
	public Pagination<EnterpriseProductMapVo> page(EnterpriseProductMapForm enterpriseProductMapForm)
			throws FrameworkRuntimeException {
		// 所需参数
		Integer page = enterpriseProductMapForm.getPage();
		Integer pageSize = enterpriseProductMapForm.getPageSize();
		String enterpriseId = enterpriseProductMapForm.getEnterpriseId();
		return iEnterpriseProductMapService.page(page, pageSize, enterpriseId);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public String del(EnterpriseProductMapForm enterpriseProductMapForm) throws FrameworkRuntimeException {
		Date curr = new Date();
		LoginCas loginCas = iAuthCasApi.get();
		String userBy = loginCas.getUserName();
		// 所需参数
		String mapId = enterpriseProductMapForm.getMapId();
		// 删除节点
		iEnterpriseProductMapService.del(mapId, null, null, curr, userBy);
		return null;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public void addMultiple(EnterpriseProductMapForm enterpriseProductMapForm) throws FrameworkRuntimeException {
		Date curr = new Date();
		LoginCas loginCas = iAuthCasApi.get();
		String userBy = loginCas.getUserName();
		// 所需参数
		String enterpriseId = enterpriseProductMapForm.getEnterpriseId();
		List<String> productIds = enterpriseProductMapForm.getProductIds();
		List<EnterpriseProductMapVo> epms = iEnterpriseProductMapService.list(enterpriseId);

		// 拥有的productIds在集合中，代表不需要创建
		Map<String, ProductVo> productMapExist = epms.stream().map(EnterpriseProductMapVo::getProduct)
				.filter(p -> productIds.contains(p.getProductId()))
				.collect(Collectors.toMap(ProductVo::getProductId, a -> a, (k1, k2) -> k1));
		Set<String> productIdsExist = productMapExist.keySet();
		logger.debug(LogMsg.to("msg", "存在不需要创建", "productIds", productIdsExist));
		// 不存在已经拥有的集合，代表需要创建
		List<String> productIdsNew = productIds.stream().filter(id -> !productMapExist.keySet().contains(id))
				.collect(Collectors.toList());
		logger.debug(LogMsg.to("msg", "不存在需要创建", "productIds", productIdsNew));
		// 拥有的productIds不在集合中，代表需要删除
		Map<String, ProductVo> productMapNoExist = epms.stream().map(EnterpriseProductMapVo::getProduct)
				.filter(p -> !productIds.contains(p.getProductId()))
				.collect(Collectors.toMap(ProductVo::getProductId, a -> a, (k1, k2) -> k1));
		Set<String> productIdsNoExist = productMapNoExist.keySet();
		logger.debug(LogMsg.to("msg", "存在需要删除", "productIds", productIdsNoExist));

		// 新建
		productIdsNew.forEach(productId -> {
			logger.debug(LogMsg.to("msg", "新建", "productId", productId));
			iEnterpriseProductMapService.add(enterpriseId, productId, curr, userBy);
		});

		// 删除
		productIdsNoExist.forEach(productId -> {
			logger.debug(LogMsg.to("msg", "删除", "productId", productId));
			iEnterpriseProductMapService.del(null, enterpriseId, productId, curr, userBy);
		});

		// iEnterpriseProductMapService.add(enterpriseId, productId, curr, userBy);

		// 检验code是否唯一
		// EnterpriseProductMapVo vo = iEnterpriseProductMapService.get(enterpriseId,
		// productId);
		// if (vo != null) {
		// logger.error(LogMsg.to("ex", BaseExceptionConstants.DUPLICATE_KEY_ERROR,
		// "productId", productId));
		// throw new
		// FrameworkRuntimeException(BaseExceptionConstants.DUPLICATE_KEY_ERROR);
		// }
		// iEnterpriseProductMapService.add(enterpriseId, productId, curr, userBy);
	}

	@Override
	public List<EnterpriseProductMapVo> list(EnterpriseProductMapForm enterpriseProductMapForm)
			throws FrameworkRuntimeException {
		// 所需参数
		String enterpriseId = enterpriseProductMapForm.getEnterpriseId();
		return iEnterpriseProductMapService.list(enterpriseId);
	}
}
