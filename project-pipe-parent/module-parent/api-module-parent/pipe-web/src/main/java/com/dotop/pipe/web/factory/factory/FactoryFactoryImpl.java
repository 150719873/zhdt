//package com.dotop.pipe.web.factory.factory;
//
//import java.util.Date;
//import java.util.List;
//
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//import org.springframework.transaction.annotation.Propagation;
//import org.springframework.transaction.annotation.Transactional;
//
//import com.dotop.pipe.api.service.factory.IFactoryService;
//import com.dotop.pipe.api.service.product.IProductService;
//import com.dotop.pipe.auth.cas.web.IAuthCasWeb;
//import com.dotop.pipe.auth.core.vo.auth.LoginCas;
//import com.dotop.pipe.core.exception.PipeExceptionConstants;
//import com.dotop.pipe.core.form.FactoryForm;
//import com.dotop.pipe.core.vo.factory.FactoryVo;
//import com.dotop.pipe.core.vo.product.ProductVo;
//import com.dotop.pipe.web.api.factory.factory.IFactoryFactory;
//import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
//import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
//import com.dotop.smartwater.dependence.core.log.LogMsg;
//import com.dotop.smartwater.dependence.core.pagination.Pagination;
//
///**
// *
// * @date 2018/10/25.
// */
//@Component
//public class FactoryFactoryImpl implements IFactoryFactory {
//
//	private final static Logger logger = LogManager.getLogger(FactoryFactoryImpl.class);
//
//	@Autowired
//	private IAuthCasWeb iAuthCasApi;
//
//	@Autowired
//	private IFactoryService iFactoryService;
//
//	@Autowired
//	private IProductService iProductService;
//
//	@Override
//	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
//	public FactoryVo add(FactoryForm factoryForm) throws FrameworkRuntimeException {
//		Date curr = new Date();
//		LoginCas loginCas = iAuthCasApi.get();
//		String userBy = loginCas.getUserName();
//		// 所需参数
//		String code = factoryForm.getCode();
//		String name = factoryForm.getName();
//		String des = factoryForm.getDes();
//
//		// 检验code是否唯一
//		FactoryVo factory = iFactoryService.getByCode(code);
//		if (factory != null) {
//			logger.error(LogMsg.to("ex", BaseExceptionConstants.DUPLICATE_KEY_ERROR, "code", code));
//			throw new FrameworkRuntimeException(BaseExceptionConstants.DUPLICATE_KEY_ERROR);
//		}
//
//		return iFactoryService.add(code, name, des, curr, userBy);
//	}
//
//	@Override
//	public FactoryVo get(FactoryForm factoryForm) throws FrameworkRuntimeException {
//		// 所需参数
//		String factoryId = factoryForm.getFactoryId();
//		return iFactoryService.get(factoryId);
//	}
//
//	@Override
//	public Pagination<FactoryVo> page(FactoryForm factoryForm) throws FrameworkRuntimeException {
//		// 所需参数
//		Integer page = factoryForm.getPage();
//		Integer pageSize = factoryForm.getPageSize();
//		return iFactoryService.page(page, pageSize);
//	}
//
//	@Override
//	public List<FactoryVo> list(FactoryForm factoryForm) throws FrameworkRuntimeException {
//		return iFactoryService.list();
//	}
//
//	@Override
//	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
//	public FactoryVo edit(FactoryForm factoryForm) throws FrameworkRuntimeException {
//		Date curr = new Date();
//		LoginCas loginCas = iAuthCasApi.get();
//		String userBy = loginCas.getUserName();
//		// 所需参数
//		String factoryId = factoryForm.getFactoryId();
//		String code = factoryForm.getCode();
//		String name = factoryForm.getName();
//		String des = factoryForm.getDes();
//		// 更新节点
//		iFactoryService.edit(factoryId, code, name, des, curr, userBy);
//		return null;
//	}
//
//	@Override
//	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
//	public String del(FactoryForm factoryForm) throws FrameworkRuntimeException {
//		Date curr = new Date();
//		LoginCas loginCas = iAuthCasApi.get();
//		String userBy = loginCas.getUserName();
//		// 所需参数
//		String factoryId = factoryForm.getFactoryId();
//		// 检验是否存在使用中的坐标，如果存在不允许删除
//		List<ProductVo> list = iProductService.list(factoryId, null, null, null);
//		if (list != null && list.size() > 0) {
//			logger.error(LogMsg.to("ex", PipeExceptionConstants.FACTORY_USED));
//			throw new FrameworkRuntimeException(PipeExceptionConstants.FACTORY_USED,
//					PipeExceptionConstants.getMessage(PipeExceptionConstants.FACTORY_USED));
//		}
//		// 删除节点
//		iFactoryService.del(factoryId, curr, userBy);
//		return null;
//	}
//}
