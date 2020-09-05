package com.dotop.smartwater.project.server.water.rest.service.storage;

import com.dotop.smartwater.dependence.core.common.BaseController;
import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.VerificationUtils;
import com.dotop.smartwater.project.module.api.store.IStoreProductFactory;
import com.dotop.smartwater.project.module.core.third.vo.pipe.PipeProductVo;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.module.core.water.enums.OperateTypeEnum;
import com.dotop.smartwater.project.module.core.water.form.StoreProductForm;
import com.dotop.smartwater.project.module.core.water.vo.StoreProductVo;
import com.dotop.smartwater.project.server.water.common.FoundationController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/*
 * @date:2018-11-15 14:38
 * @content: 库存管理
 *
 */
@RestController

@RequestMapping("/store")
public class StoreProductController extends FoundationController implements BaseController<StoreProductForm> {
	private static final Logger LOGGER = LogManager.getLogger(StoreProductController.class);

	@Autowired
	private IStoreProductFactory iStoreProductFactory;

	private static final String PRODUCTNO = "productNo";

	@PostMapping(value = "/getProByCon", produces = GlobalContext.PRODUCES)
	public String getProByCon(@RequestBody StoreProductForm storeProductForm) {
		LOGGER.info(LogMsg.to("msg:", " 分页查询开始", "storeProductForm", storeProductForm));
		Integer page = storeProductForm.getPage();
		Integer pageCount = storeProductForm.getPageCount();
		// 验证
		VerificationUtils.integer("page", page);
		VerificationUtils.integer("pageCount", pageCount);
		// 数据封装
		Pagination<StoreProductVo> pagination = iStoreProductFactory.getProByCon(storeProductForm);
		LOGGER.info(LogMsg.to("msg:", " 分页查询查询结束"));
		return resp(ResultCode.Success, ResultCode.SUCCESS, pagination);
	}

	@PostMapping(value = "/getProductByNo", produces = GlobalContext.PRODUCES)
	public String getProductByNo(@RequestBody StoreProductForm storeProductForm) {
		LOGGER.info(LogMsg.to("msg:", " 查询详情开始", "storeProductForm", storeProductForm));
		// 数据封装
		StoreProductVo storePro = iStoreProductFactory.getProductByNo(storeProductForm);
		LOGGER.info(LogMsg.to("msg:", " 查询详情结束", "storeProductVo", storePro));
		return resp(ResultCode.Success, ResultCode.SUCCESS, storePro);
	}

	@PostMapping(value = "/addProduct", produces = GlobalContext.PRODUCES)
	public String addProduct(@RequestBody StoreProductForm storeProductForm) {
		LOGGER.info(LogMsg.to("msg:", " 新增开始", "storeProductForm", storeProductForm));
		// 数据校验
		String productNo = storeProductForm.getProductNo();
		String name = storeProductForm.getName();
		String model = storeProductForm.getModel();
		String spec = storeProductForm.getSpec();
		String material = storeProductForm.getMaterial();
		String weight = storeProductForm.getWeight();
		String unit = storeProductForm.getUnit();
		double price = storeProductForm.getPrice();
		String category = storeProductForm.getCategory();
		String type = storeProductForm.getType();

		VerificationUtils.string(PRODUCTNO, productNo);
		VerificationUtils.string("name", name);
		VerificationUtils.string("model", model);
		VerificationUtils.string("spec", spec);
		VerificationUtils.string("material", material);
		VerificationUtils.string("weight", weight);
		VerificationUtils.string("unit", unit);
		VerificationUtils.bigDecimal("price", BigDecimal.valueOf(price));
		VerificationUtils.string("category", category);
		VerificationUtils.string("type", type);

		// 验证产品编号唯一性
		int count = iStoreProductFactory.checkProductNo(storeProductForm);
		if (count != 0) {
			return resp(ResultCode.ParamIllegal, "该产品编号已被使用", null);
		}
		// 验证产品名称唯一性
		int _count = iStoreProductFactory.checkProductName(storeProductForm);
		if (_count != 0) {
			return resp(ResultCode.ParamIllegal, "产品名称不可以重复", null);
		}

		// 数据封装
		Integer num = iStoreProductFactory.addProduct(storeProductForm);
		if (num == 1) {
			LOGGER.info(LogMsg.to("msg:", " 新增结束", "storeProductForm", storeProductForm));
			auditLog(OperateTypeEnum.PRODUCT_MANAGEMENT, "新增产品", "产品编号", productNo);
			return resp(ResultCode.Success, ResultCode.SUCCESS, null);
		} else {
			return resp(ResultCode.Fail, "添加产品失败！", null);
		}
	}

	@PostMapping(value = "/editProduct", produces = GlobalContext.PRODUCES)
	public String editProduct(@RequestBody StoreProductForm storeProductForm) {
		LOGGER.info(LogMsg.to("msg:", " 编辑开始", "storeProductForm", storeProductForm));
		// 数据校验
		String productNo = storeProductForm.getProductNo();
		String name = storeProductForm.getName();
		String model = storeProductForm.getModel();
		String spec = storeProductForm.getSpec();
		String material = storeProductForm.getMaterial();
		String weight = storeProductForm.getWeight();
		String unit = storeProductForm.getUnit();
		double price = storeProductForm.getPrice();
		String category = storeProductForm.getCategory();
		String type = storeProductForm.getType();

		VerificationUtils.string(PRODUCTNO, productNo);
		VerificationUtils.string("name", name);
		VerificationUtils.string("model", model);
		VerificationUtils.string("spec", spec);
		VerificationUtils.string("material", material);
		VerificationUtils.string("weight", weight);
		VerificationUtils.string("unit", unit);
		VerificationUtils.bigDecimal("price", BigDecimal.valueOf(price));
		VerificationUtils.string("category", category);
		VerificationUtils.string("type", type);

		// 数据封装
		Integer num = iStoreProductFactory.editProduct(storeProductForm);
		if (num == 1) {
			LOGGER.info(LogMsg.to("msg:", " 编辑结束", "storeProductForm", storeProductForm));
			auditLog(OperateTypeEnum.PRODUCT_MANAGEMENT, "编辑产品", "产品编号", productNo);
			return resp(ResultCode.Success, ResultCode.SUCCESS, null);
		} else {
			return resp(ResultCode.Fail, "编辑产品失败！", null);
		}
	}

	@PostMapping(value = "/changeStatus", produces = GlobalContext.PRODUCES)
	public String changeStatus(@RequestBody StoreProductForm storeProductForm) {
		LOGGER.info(LogMsg.to("msg:", " 修改状态开始", "storeProductForm", storeProductForm));
		String productNo = storeProductForm.getProductNo();
		Integer status = storeProductForm.getStatus();
		VerificationUtils.string(PRODUCTNO, productNo);
		VerificationUtils.integer("status", status);

		// 数据封装
		Integer num = iStoreProductFactory.changeStatus(storeProductForm);
		if (num == 1) {
			LOGGER.info(LogMsg.to("msg:", " 修改状态结束", "storeProductForm", storeProductForm));
			auditLog(OperateTypeEnum.PRODUCT_MANAGEMENT, "修改状态", "产品编号", productNo);
			return resp(ResultCode.Success, ResultCode.SUCCESS, null);
		} else {
			return resp(ResultCode.Fail, "修改产品状态失败", null);
		}
	}

	@PostMapping(value = "/deleteProduct", produces = GlobalContext.PRODUCES)
	public String deleteProduct(@RequestBody StoreProductForm storeProductForm) {
		LOGGER.info(LogMsg.to("msg:", " 删除开始", "storeProductForm", storeProductForm));
		// 数据校验
		String productNo = storeProductForm.getProductNo();
		VerificationUtils.string(PRODUCTNO, productNo);

		// 数据封装
		Integer num = iStoreProductFactory.deleteProduct(storeProductForm);
		if (num == 1) {
			LOGGER.info(LogMsg.to("msg:", " 删除结束"));
			auditLog(OperateTypeEnum.PRODUCT_MANAGEMENT, "删除产品", "产品编号", productNo);
			return resp(ResultCode.Success, ResultCode.SUCCESS, null);
		} else {
			return resp(ResultCode.Fail, "修改产品状态失败", null);
		}
	}

	@PostMapping(value = "/getLinePro", produces = GlobalContext.PRODUCES)
	public String getLinePro(@RequestBody StoreProductForm storeProductForm) {
		LOGGER.info(LogMsg.to("msg:", " 获取上线产品开始", "storeProductForm", storeProductForm));
		// 数据封装
		List<StoreProductVo> listPro = iStoreProductFactory.getLinePro(storeProductForm);
		LOGGER.info(LogMsg.to("msg:", " 获取上线产品结束"));
		return resp(ResultCode.Success, ResultCode.SUCCESS, listPro);
	}

	@PostMapping(value = "/list", produces = GlobalContext.PRODUCES)
	public String list(@RequestBody StoreProductForm storeProductForm) {
		LOGGER.info(LogMsg.to("msg:", " 获取产品列表开始", "storeProductForm", storeProductForm));
		// 数据封装
		List<StoreProductVo> list = iStoreProductFactory.list(storeProductForm);
		LOGGER.info(LogMsg.to("msg:", " 获取产品列表结束"));
		return resp(ResultCode.Success, ResultCode.SUCCESS, list);
	}

	@PostMapping(value = "/getPipeProduct", produces = GlobalContext.PRODUCES)
	public String getPipeProduct(@RequestBody StoreProductForm storeProductForm) {
		LOGGER.info(LogMsg.to("msg:", " 查询管漏产品分页查询开始", "storeProductForm", storeProductForm));
		Integer page = storeProductForm.getPage();
		Integer pageCount = storeProductForm.getPageCount();
		// 验证
		VerificationUtils.integer("page", page);
		VerificationUtils.integer("pageCount", pageCount);
		// 数据封装
		Pagination<PipeProductVo> pagination = iStoreProductFactory.getPipeProduct(storeProductForm);
		LOGGER.info(LogMsg.to("msg:", " 查询管漏产品分页查询结束"));
		return resp(ResultCode.Success, ResultCode.SUCCESS, pagination);
	}
}
