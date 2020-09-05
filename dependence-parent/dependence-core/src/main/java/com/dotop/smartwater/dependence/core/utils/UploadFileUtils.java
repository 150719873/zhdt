package com.dotop.smartwater.dependence.core.utils;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.multipart.MultipartFile;

import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.vo.UploadFileVo;

/**
 * 

 * @date 2019年5月8日
 * @description 文件上传工具类
 */
public class UploadFileUtils {

	private static final Logger logger = LogManager.getLogger(UploadFileUtils.class);

	private UploadFileUtils() {
		super();
	}

	public static final UploadFileVo write(String uploadBasePath, String filePath, MultipartFile upload)
			throws FrameworkRuntimeException {
		File file = null;
		File fileTemp = null;

		UploadFileVo uploadFile = new UploadFileVo();
		upload.getContentType();
		// 文件保存
		String fileName = upload.getOriginalFilename();
		String fileNameTemp = fileName;
		try {
			// 文件保存
			File dir = new File(uploadBasePath, filePath);
			if (!dir.exists() && !dir.isDirectory()) {
				dir.mkdirs();
			}
			// 临时文件保存
			File dirTemp = new File(uploadBasePath + "_temp", filePath);
			if (!dirTemp.exists() && !dirTemp.isDirectory()) {
				dirTemp.mkdirs();
			}
			// 使用逻辑，当使用文件不存在的时候，重新将数据库的jar生成到系统中,读取时判断文件大小用于是否刷新文件
			// 保存文件做记录

			file = new File(dir, fileName);
			fileTemp = new File(dirTemp, fileNameTemp);
			if (file.isFile()) {
				file.delete();
			}
			// 复制到临时文件
			upload.transferTo(fileTemp);
			// 从临时文件复制到正式文件
			// 如果想要对一个已打开的文件使用 FileCopy 语句，则会产生错误。
			// Files.copy(new FileInputStream(fileTemp),
			// Paths.get(file.getPath()), StandardCopyOption.REPLACE_EXISTING);
			FileUtils.copyFile(fileTemp, file);
			// FileUtils.writeToFile(new FileInputStream(fileTemp), file);
			// 删除临时文件
			if (fileTemp.isFile()) {
				file.delete();
			}
		} catch (IllegalStateException | IOException e) {
			logger.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.ILLEGAL_EXCEPTION, e);
		}
		long length = file.length();
		uploadFile.setFileName(fileName);
		uploadFile.setFilePath(filePath);
		uploadFile.setLength(length);
		uploadFile.setFile(file);
		return uploadFile;
	}
}
