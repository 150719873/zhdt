//package com.dotop.pipe.server.rest.service.factory;
//
//import java.util.List;
//
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.DeleteMapping;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.PutMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.dotop.pipe.core.form.FactoryForm;
//import com.dotop.pipe.core.vo.factory.FactoryVo;
//import com.dotop.pipe.web.api.factory.factory.IFactoryFactory;
//import com.dotop.smartwater.dependence.core.common.BaseController;
//import com.dotop.smartwater.dependence.core.global.GlobalContext;
//import com.dotop.smartwater.dependence.core.log.LogMsg;
//import com.dotop.smartwater.dependence.core.pagination.Pagination;
//import com.dotop.smartwater.dependence.core.utils.VerificationUtils;
//
///**
// *
// * @date 2018/10/25.
// */
//@RestController
//@RequestMapping("/factory")
//public class FactoryController implements BaseController<FactoryForm> {
//
//	private final static Logger logger = LogManager.getLogger(FactoryController.class);
//
//	@Autowired
//	private IFactoryFactory iFactoryFactory;
//
//	/**
//	 * 添加厂商
//	 *
//	 * @param factoryForm
//	 * @return
//	 */
//	@Override
//	@PostMapping(produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
//	public String add(@RequestBody FactoryForm factoryForm) {
//		logger.info(LogMsg.to("factoryForm", factoryForm));
//
//		String code = factoryForm.getCode();
//		String name = factoryForm.getName();
//		String des = factoryForm.getDes();
//		// 验证
//		VerificationUtils.string("code", code);
//		VerificationUtils.string("name", name);
//		VerificationUtils.string("des", des, true, 100);
//		FactoryVo factoryVo = iFactoryFactory.add(factoryForm);
//
//		return resp("factoryId", factoryVo.getFactoryId());
//	}
//
//	/**
//	 * 查询厂商
//	 *
//	 * @param factoryForm
//	 * @return
//	 */
//	@Override
//	@GetMapping(value = "/{factoryId}", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
//	public String get(FactoryForm factoryForm) {
//		logger.info(LogMsg.to("factoryForm", factoryForm));
//		String factoryId = factoryForm.getFactoryId();
//		// 验证
//		VerificationUtils.string("factoryId", factoryId);
//		FactoryVo factoryVo = iFactoryFactory.get(factoryForm);
//
//		return resp(factoryVo);
//	}
//
//	/**
//	 * 列出厂商
//	 *
//	 * @param factoryForm
//	 * @return
//	 */
//	@Override
//	@GetMapping(value = "/list", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
//	public String list(FactoryForm factoryForm) {
//		logger.info(LogMsg.to("factoryForm", factoryForm));
//		// 验证
//		List<FactoryVo> list = iFactoryFactory.list(factoryForm);
//
//		return resp(list);
//	}
//
//	/**
//	 * 厂商分页
//	 *
//	 * @param factoryForm
//	 * @return
//	 */
//	@Override
//	@GetMapping(value = "/page/{page}/{pageSize}", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
//	public String page(FactoryForm factoryForm) {
//		logger.info(LogMsg.to("factoryForm", factoryForm));
//		Integer page = factoryForm.getPage();
//		Integer pageSize = factoryForm.getPageSize();
//		// 验证
//		VerificationUtils.integer("page", page);
//		VerificationUtils.integer("pageSize", pageSize);
//		Pagination<FactoryVo> pagination = iFactoryFactory.page(factoryForm);
//
//		return resp(pagination);
//	}
//
//	/**
//	 * 更新厂商
//	 *
//	 * @param factoryForm
//	 * @return
//	 */
//	@Override
//	@PutMapping(produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
//	public String edit(@RequestBody FactoryForm factoryForm) {
//		logger.info(LogMsg.to("factoryForm", factoryForm));
//
//		String factoryId = factoryForm.getFactoryId();
//		String code = factoryForm.getCode();
//		String name = factoryForm.getName();
//		String des = factoryForm.getDes();
//		// 验证
//		VerificationUtils.string("factoryId", factoryId);
//		VerificationUtils.string("code", code, true);
//		VerificationUtils.string("name", name, true);
//		VerificationUtils.string("des", des, true, 100);
//		iFactoryFactory.edit(factoryForm);
//		return resp();
//	}
//
//	/**
//	 * 删除厂商
//	 *
//	 * @param factoryForm
//	 * @return
//	 */
//	@Override
//	@DeleteMapping(value = "/{factoryId}", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
//	public String del(FactoryForm factoryForm) {
//		logger.info(LogMsg.to("factoryForm", factoryForm));
//		String factoryId = factoryForm.getFactoryId();
//		// 验证
//		VerificationUtils.string("factoryId", factoryId);
//		iFactoryFactory.del(factoryForm);
//
//		return resp();
//	}
//}
