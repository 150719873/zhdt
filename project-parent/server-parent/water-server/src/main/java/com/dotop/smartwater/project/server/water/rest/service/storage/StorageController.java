package com.dotop.smartwater.project.server.water.rest.service.storage;

import java.io.BufferedOutputStream;
import java.io.File;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dotop.smartwater.project.module.core.water.enums.OperateTypeEnum;
import com.dotop.smartwater.project.server.water.common.FoundationController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.dotop.smartwater.dependence.core.common.BaseController;
import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.VerificationUtils;
import com.dotop.smartwater.project.module.api.store.IStorageFactory;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.module.core.water.form.StorageForm;
import com.dotop.smartwater.project.module.core.water.utils.FileUtil;
import com.dotop.smartwater.project.module.core.water.vo.StorageVo;
import com.dotop.smartwater.project.module.core.water.vo.StoreProductVo;

/**
 * 入库管理
 * 

 * @date 2018-11-27 上午 10:53
 *
 */
@RestController

@RequestMapping("/store")
public class StorageController extends FoundationController implements BaseController<StorageForm> {
	private static final Logger LOGGER = LogManager.getLogger(StorageController.class);

	@Autowired
	private IStorageFactory iStorageFactory;

	private static final String DATAFORMAT = "YYYYMMDDHHMMSS";

	private static final String RECORDNO = "recordNo";

	private static final int MIN = 10;

	@PostMapping(value = "/getStorageByCon", produces = GlobalContext.PRODUCES)
	public String getStorageByCon(@RequestBody StorageForm storageForm) {
		LOGGER.info(LogMsg.to("msg:", " 分页查询开始", storageForm));
		Integer page = storageForm.getPage();
		Integer pageCount = storageForm.getPageCount();
		// 验证
		VerificationUtils.integer("page", page);
		VerificationUtils.integer("pageCount", pageCount);
		// 数据封装
		Pagination<StorageVo> pagination = iStorageFactory.getStorageByCon(storageForm);
		LOGGER.info(LogMsg.to("msg:", " 分页查询查询结束"));
		return resp(ResultCode.Success, ResultCode.SUCCESS, pagination);
	}

	@PostMapping(value = "/getStorageByNo", produces = GlobalContext.PRODUCES)
	public String getStorageByNo(@RequestBody StorageForm storageForm) {
		LOGGER.info(LogMsg.to("msg:", " 查询入库产品详情开始", storageForm));
		// 校验入库记录编号
		String recordNo = storageForm.getRecordNo();
		VerificationUtils.string(RECORDNO, recordNo);

		// 数据封装
		StorageVo storage = iStorageFactory.getStorageByNo(storageForm);
		LOGGER.info(LogMsg.to("msg:", " 查询入库产品详情结束"));
		return resp(ResultCode.Success, ResultCode.SUCCESS, storage);
	}

	@PostMapping(value = "/addStorage", produces = GlobalContext.PRODUCES)
	public String addStorage(@RequestBody StorageForm storageForm) {
		LOGGER.info(LogMsg.to("msg:", " 新增入库开始", storageForm));
		// 数据校验
		String repoNo = storageForm.getRepoNo();
		String repoName = storageForm.getRepoName();
		Integer quantity = storageForm.getQuantity();
		double total = storageForm.getTotal();
		VerificationUtils.string("repoNo", repoNo);
		VerificationUtils.string("repoName", repoName);
		VerificationUtils.integer("quantity", quantity);
		VerificationUtils.bigDecimal("total", BigDecimal.valueOf(total));

		// 数据封装
		Integer num = iStorageFactory.addStorage(storageForm);
		if (num == 1) {
			LOGGER.info(LogMsg.to("msg:", " 新增入库结束"));
			auditLog(OperateTypeEnum.STORAGE_MANAGEMENT,"新增入库","仓库编号",repoNo);
			return resp(ResultCode.Success, ResultCode.SUCCESS, null);
		} else {
			return resp(ResultCode.Fail, "添加失败！", null);
		}
	}

	@PostMapping(value = "/editStorage", produces = GlobalContext.PRODUCES)
	public String editStorage(@RequestBody StorageForm storageForm) {
		LOGGER.info(LogMsg.to("msg:", " 编辑入库信息开始", storageForm));
		// 数据校验
		String repoNo = storageForm.getRepoNo();
		String repoName = storageForm.getRepoName();
		Integer quantity = storageForm.getQuantity();
		double total = storageForm.getTotal();
		VerificationUtils.string("repoNo", repoNo);
		VerificationUtils.string("repoName", repoName);
		VerificationUtils.integer("quantity", quantity);
		VerificationUtils.bigDecimal("total", BigDecimal.valueOf(total));

		// 数据封装
		Integer num = iStorageFactory.editStorage(storageForm);
		if (num == 1) {
			LOGGER.info(LogMsg.to("msg:", " 编辑入库信息结束"));
			auditLog(OperateTypeEnum.STORAGE_MANAGEMENT,"编辑入库信息","仓库编号",repoNo);
			return resp(ResultCode.Success, ResultCode.SUCCESS, null);
		} else {
			return resp(ResultCode.Fail, "编辑失败！", null);
		}
	}

	@PostMapping(value = "/deleteStorage", produces = GlobalContext.PRODUCES)
	public String deleteStorage(@RequestBody StorageForm storageForm) {
		LOGGER.info(LogMsg.to("msg:", " 删除入库信息开始", storageForm));
		// 数据校验
		String recordNo = storageForm.getRecordNo();
		VerificationUtils.string(RECORDNO, recordNo);

		// 数据封装
		Integer num = iStorageFactory.deleteStorage(storageForm);
		if (num == 1) {
			LOGGER.info(LogMsg.to("msg:", " 删除入库信息结束"));
			auditLog(OperateTypeEnum.STORAGE_MANAGEMENT,"删除入库信息","记录号",recordNo);
			return resp(ResultCode.Success, ResultCode.SUCCESS, null);
		} else {
			return resp(ResultCode.Fail, "删除失败！", null);
		}
	}

	@PostMapping(value = "/confirmStorage", produces = GlobalContext.PRODUCES)
	public String confirmStorage(@RequestBody StorageForm storageForm) {
		// 数据校验
		String recordNo = storageForm.getRecordNo();
		VerificationUtils.string(RECORDNO, recordNo);

		// 数据封装
		Integer num = iStorageFactory.confirmStorage(storageForm);
		if (num == 1) {
			return resp(ResultCode.Success, ResultCode.SUCCESS, null);
		} else {
			return resp(ResultCode.Fail, "失败！", null);
		}
	}

	@PostMapping(value = "/getProIn", produces = GlobalContext.PRODUCES)
	public String getProIn(@RequestBody StorageForm storageForm) {
		LOGGER.info(LogMsg.to("msg:", " 查询所有已入库产品开始", storageForm));
		// 数据封装
		List<StoreProductVo> listStor = iStorageFactory.getProIn(storageForm);
		LOGGER.info(LogMsg.to("msg:", " 查询所有已入库产品结束"));
		return resp(ResultCode.Success, ResultCode.SUCCESS, listStor);
	}

	@PostMapping(value = "/exportStorage", produces = GlobalContext.PRODUCES)
	public void exportStorage(HttpServletRequest request, HttpServletResponse response,
			@RequestBody StorageForm storageForm) {
		LOGGER.info(LogMsg.to("msg:", "导出 入库产品信息开始", "sotrageForm", storageForm));
		try (ServletOutputStream output = response.getOutputStream();
				BufferedOutputStream buff = new BufferedOutputStream(output)) {
			SimpleDateFormat format = new SimpleDateFormat(DATAFORMAT);
			String date = format.format(new Date());
			String path = request.getServletContext().getRealPath("/");
			File baseDir = new File(path, "StorageExcel" + date);
			System.out.println("****path: " + path);
			if (!baseDir.exists() && baseDir.mkdirs()) {
				FileUtil.deleteFiles(baseDir, MIN);
				String fileName = URLEncoder.encode("入库数据查询", StandardCharsets.UTF_8.name()) + "_" + date + ".xls";
				String tempFileName = File.separator + "StorageExcel" + date + File.separator + fileName;
				tempFileName = path + tempFileName;
				String filePath = tempFileName;
				response.setContentType(GlobalContext.OCTET);
				response.setHeader("Content-Disposition", "filename=" + fileName);
				iStorageFactory.exportStorage(tempFileName, storageForm);
				buff.write(FileUtil.readFile(filePath));
				buff.flush();
				LOGGER.info(LogMsg.to("msg:", "导出 入库产品信息结束"));
			}
		} catch (Exception e) {
			LOGGER.error("store/exportStorage", e);
		}
	}
}
