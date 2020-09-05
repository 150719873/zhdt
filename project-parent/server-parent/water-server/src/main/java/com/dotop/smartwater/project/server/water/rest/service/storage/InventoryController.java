package com.dotop.smartwater.project.server.water.rest.service.storage;

import java.io.BufferedOutputStream;
import java.io.File;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.dotop.smartwater.dependence.core.common.BaseController;
import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.VerificationUtils;
import com.dotop.smartwater.project.module.api.store.IInventoryFactory;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.module.core.water.form.InventoryForm;
import com.dotop.smartwater.project.module.core.water.utils.FileUtil;
import com.dotop.smartwater.project.module.core.water.vo.InventoryVo;

/**
 * 库存盘点
 * 

 * @date 2018-12-5 下午 19:16
 *
 */
@RestController

@RequestMapping("/store")
public class InventoryController implements BaseController<InventoryForm> {
	private static final Logger LOGGER = LogManager.getLogger(InventoryController.class);

	@Autowired
	private IInventoryFactory iInventoryFactory;

	private static final String DATAFORMAT = "YYYYMMDDHHMMSS";

	private static final int MIN = 10;

	@PostMapping(value = "/getInventoryByCon", produces = GlobalContext.PRODUCES)
	public String getInventoryByCon(@RequestBody InventoryForm inventoryForm) {
		LOGGER.info(LogMsg.to("msg:", " 分页查询开始", "inventoryForm", inventoryForm));
		Integer page = inventoryForm.getPage();
		Integer pageCount = inventoryForm.getPageCount();
		// 验证
		VerificationUtils.integer("page", page);
		VerificationUtils.integer("pageCount", pageCount);

		// 数据封装
		Pagination<InventoryVo> pagination = iInventoryFactory.getInventoryByCon(inventoryForm);
		LOGGER.info(LogMsg.to("msg:", " 分页查询结束"));
		return resp(ResultCode.Success, ResultCode.SUCCESS, pagination);
	}
	
	@PostMapping(value = "/getInventory", produces = GlobalContext.PRODUCES)
	public String getInventory(@RequestBody InventoryForm inventoryForm) {
		LOGGER.info(LogMsg.to("msg:", "获取库存余量开始", "inventoryForm", inventoryForm));

		// 数据封装
		InventoryVo inventory = iInventoryFactory.getInventory(inventoryForm);
		LOGGER.info(LogMsg.to("msg:", "获取库存余量结束"));
		return resp(ResultCode.Success, ResultCode.SUCCESS, inventory);
	}

	@PostMapping(value = "/exportInventory", produces = GlobalContext.PRODUCES)
	public void exportInventory(ServletRequest request, HttpServletResponse response,
			@RequestBody InventoryForm inventoryForm) {
		LOGGER.info(LogMsg.to("msg:", "导出库存盘点信息开始", "inventoryForm", inventoryForm));
		try (ServletOutputStream output = response.getOutputStream();
				BufferedOutputStream buff = new BufferedOutputStream(output)) {
			SimpleDateFormat format = new SimpleDateFormat(DATAFORMAT);
			String date = format.format(new Date());
			String path = request.getServletContext().getRealPath("/");
			File baseDir = new File(path, "InventoryExcel" + date);
			if (!baseDir.exists() && baseDir.mkdirs()) {
				FileUtil.deleteFiles(baseDir, MIN);
				String fileName = URLEncoder.encode("库存盘点数据查询", StandardCharsets.UTF_8.name()) + "_" + date + ".xls";
				String tempFileName = File.separator + "InventoryExcel" + date + File.separator + fileName;
				tempFileName = path + tempFileName;
				String filePath = tempFileName;
				response.setContentType(GlobalContext.OCTET);
				response.setHeader("Content-Disposition", "filename=" + fileName);
				iInventoryFactory.exportInventory(tempFileName, inventoryForm);
				buff.write(FileUtil.readFile(filePath));
				buff.flush();
				LOGGER.info(LogMsg.to("msg:", "导出库存盘点结束"));
			}
		} catch (Exception e) {
			LOGGER.error("store/exportInventory", e.getMessage());
		}
	}
}
