package com.dotop.smartwater.project.auth.rest.service;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.dotop.smartwater.dependence.core.common.BaseController;
import com.dotop.smartwater.dependence.core.common.BaseForm;
import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.project.module.client.third.IOssService;
import com.dotop.smartwater.project.module.core.third.utils.oss.OssUtil;
import com.dotop.smartwater.project.module.core.water.constants.OssPathCode;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.module.core.water.form.FilePathForm;

/**
 * 文件上传Controller

 * @date 2019-08-23 13:52
 *
 */
@RestController

@RequestMapping("/uploadFile")
public class UploadFileController implements BaseController<BaseForm> {
	
	private static final Logger LOGGER = LogManager.getLogger(UploadFileController.class);
	
	@Resource
	private IOssService iOssService;
	
	@Resource
	private OssUtil ossUtil;
	
	@PostMapping(value = "/getImgByte", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
	public String getImgByte(@RequestBody FilePathForm filePathForm) {
		
		LOGGER.info(LogMsg.to("msg:", "获取图片文件流开始"));
		String path = filePathForm.getPath();
		// 数据校验
		if(path == null || path == "") {
			return resp(ResultCode.Fail, "找不到该路径", null);
		}
		byte[] bytes = iOssService.getImgByte(path);
		if (bytes != null) {
			LOGGER.info(LogMsg.to("msg:", "获取图片文件流结束", bytes));
			return resp(ResultCode.Success, ResultCode.SUCCESS, bytes);
		} else {
			return resp(ResultCode.Success, "获取图片文件流失败", null);
		}
	}
	
	/**
	 * 上传数据及保存文件
	 */
	@PostMapping("/uploadFile")
	public String uploadFile(HttpServletRequest request, @RequestParam("userid") String userId) {

		List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles("file");
		if (1 != files.size()) {
			return resp(ResultCode.ParamIllegal, "没有找到上传文件", null);
		}
		MultipartFile file;
		for (MultipartFile file1 : files) {
			file = file1;
			if (!file.isEmpty()) {
				try {
					byte[] bytes = file.getBytes();

					String filename = file.getOriginalFilename();
					String suffix = filename.substring(filename.lastIndexOf('.'));
					String contentType = ossUtil.getContentType(suffix);

					String url = iOssService.upLoad(bytes, OssPathCode.FILE, OssPathCode.VERSION_CONTROLLER, filename, contentType, userId);
					return resp(ResultCode.Success, ResultCode.SUCCESS, url);
				} catch (Exception e) {
					LOGGER.error("上传附件失败", e);
					return resp(ResultCode.Fail, e.getMessage(), null);
				}
			} else {
				return resp(ResultCode.Fail, "空文件！", null);
			}
		}
		return resp(ResultCode.Fail, "上传附件出现了未知的错误", null);
	}
}