package com.dotop.smartwater.project.server.water.rest.service.tool;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import com.dotop.smartwater.dependence.core.common.BaseController;
import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.VerificationUtils;
import com.dotop.smartwater.project.module.api.tool.IDataBackupFactory;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.module.core.water.form.DataBackupForm;
import com.dotop.smartwater.project.module.core.water.vo.DataBackupVo;

@RestController()

@RequestMapping("/dataBackup")
public class DataBackupController implements BaseController<DataBackupForm> {

	private static final Logger LOGGER = LogManager.getLogger(DataBackupController.class);

	@Autowired
	private IDataBackupFactory iDataBackupFactory;

	@Value("${druid.dbbackup.outputpath}")
	private String downloadBasePath;

	@Override
	@PostMapping(value = "/add", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
	public String add(@RequestBody DataBackupForm dataBackupForm) {

		LOGGER.info(LogMsg.to("msg:", "新增功能开始", "dataBackupForm", dataBackupForm));
		// 文件名称
		String fileName = dataBackupForm.getFileName();
		// 文件路径
		String fileSrc = dataBackupForm.getFileSrc();
		// 备份类型 (手动备份 自动备份)
		String backupType = dataBackupForm.getBackupType();
		// 校验
		VerificationUtils.string("fileName", fileName);
		VerificationUtils.string("fileSrc", fileSrc);
		VerificationUtils.string("backupType", backupType);
		DataBackupVo dataBackupVo = iDataBackupFactory.add(dataBackupForm);
		LOGGER.info(LogMsg.to("msg:", "新增功能结束", "dataBackupVo", dataBackupVo));
		return resp(ResultCode.Success, ResultCode.SUCCESS, dataBackupVo);
	}

	@Override
	@PostMapping(value = "/page", produces = GlobalContext.PRODUCES)
	public String page(@RequestBody DataBackupForm dataBackupForm) {
		LOGGER.info(LogMsg.to("msg:", " 分页查询开始", "deviceForm", dataBackupForm));
		Integer page = dataBackupForm.getPage();
		Integer pageCount = dataBackupForm.getPageCount();
		// 验证
		VerificationUtils.integer("page", page);
		VerificationUtils.integer("pageCount", pageCount);
		Pagination<DataBackupVo> pagination = iDataBackupFactory.page(dataBackupForm);
		LOGGER.info(LogMsg.to("msg:", " 分页查询查询结束"));
		return resp(ResultCode.Success, ResultCode.SUCCESS, pagination);
	}

	/**
	 * 删除记录
	 */
	@Override
	@PostMapping(value = "/del", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
	public String del(@RequestBody DataBackupForm dataBackupForm) {
		LOGGER.info(LogMsg.to("msg:", "删除内容开始", "dataBackupForm", dataBackupForm));
		// id
		String backupId = dataBackupForm.getBackupId();
		// 校验
		VerificationUtils.string("backupId", backupId);
		String id = iDataBackupFactory.del(dataBackupForm);
		LOGGER.info(LogMsg.to("msg:", "删除内容结束", "backupId", id));
		return resp(ResultCode.Success, ResultCode.SUCCESS, id);
	}

	@PostMapping(value = "/get", produces = GlobalContext.PRODUCES)
	public void exportExcelOne(@RequestBody DataBackupForm dataBackupForm, HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		DataBackupVo dataBackupVo = iDataBackupFactory.get(dataBackupForm);
		String fileName = dataBackupVo.getFileName();
		String src = dataBackupVo.getFileSrc();
		response.setContentType(GlobalContext.OCTET);
		response.setHeader("Content-Disposition", "filename=" + fileName);
		if (fileName != null) {
			// 设置文件路径
			String filePath = downloadBasePath + File.separator + src + File.separator;
			File file = new File(filePath, FilenameUtils.getName(fileName));
			if (file.exists()) {
				try (FileInputStream fileInputStream = new FileInputStream(file);
						BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
						OutputStream outputStream = response.getOutputStream()) {
					byte[] b = new byte[bufferedInputStream.available()];
					while (bufferedInputStream.read(b) > 0) {
						outputStream.write(b);
						outputStream.flush();
					}
				} catch (Exception e) {
					LOGGER.error("获取文件异常", e.getMessage());
				}
			}
		}
	}

	/**
	 * 数据备份 手动 KJR
	 ***/
	@PostMapping(value = "/backupDb", produces = GlobalContext.PRODUCES)
	public String backupDb() {
		iDataBackupFactory.backupDb();
		return resp(ResultCode.Success, ResultCode.SUCCESS, null);
	}

}
