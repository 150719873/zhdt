package com.dotop.smartwater.project.server.water.rest.service.storage;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.dotop.smartwater.dependence.core.common.BaseController;
import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.project.module.client.third.IOssService;
import com.dotop.smartwater.project.module.core.third.utils.oss.OssUtil;
import com.dotop.smartwater.project.module.core.water.constants.OssPathCode;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.module.core.water.form.StoreProductForm;
import com.github.pagehelper.util.StringUtil;

@RestController

@RequestMapping("/store/ProductImg")
public class StoreProductImgController implements BaseController<StoreProductForm> {
	private static final Logger LOG = LoggerFactory.getLogger(StoreProductImgController.class);
	private static final int MAX_FILE_SIZE = 2097152; // 1MB

	@Resource
	private OssUtil ossUtil;
	
	@Resource
	private IOssService iOssService;

	/**
	 * 上传数据及保存文件
	 */
	@PostMapping(value = "/getImgUrl", produces = GlobalContext.PRODUCES)
	public String getImgUrl(HttpServletRequest request, @RequestParam("userid") String userId) {

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
					if (bytes.length > MAX_FILE_SIZE) {
						return resp(ResultCode.Fail, "上传的文件过大", null);
					}

					String filename = file.getOriginalFilename();
					String suffix = filename.substring(filename.lastIndexOf('.'));
					if (StringUtil.isNotEmpty(filename)) {
						String contentType = ossUtil.getContentType(suffix);
						if (StringUtil.isNotEmpty(contentType)) {
//							filename = DigestUtils.md5Hex(bytes);
							String url = iOssService.upLoad(bytes, OssPathCode.IMAGE, OssPathCode.STORE_PRODUCT, filename, contentType, userId);
							return resp(ResultCode.Success, "Success", url);
						} else {
							return resp(ResultCode.NO_GET_FIEL_FORAMT,
									ResultCode.getMessage(ResultCode.NO_GET_FIEL_FORAMT), null);
						}
					} else {
						return resp(ResultCode.NO_GET_FIEL_FORAMT, ResultCode.getMessage(ResultCode.NO_GET_FIEL_FORAMT), null);
					}
				} catch (Exception e) {
					LOG.error("上传产品图片出错", e);
					return resp(ResultCode.Fail, e.getMessage(), null);
				}
			} else {
				return resp(ResultCode.Fail, "空文件！", null);
			}
		}
		return resp(ResultCode.Fail, "上传图片出现了未知的错误", null);

	}
}
