package com.dotop.smartwater.project.module.core.third.utils.oss;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;

import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;

/**
 * 上传文件到本地服务器Util

 * @date 2019-08-22 09:33
 *
 */
public class UploadUtil {
	
	private final static Logger logger = LogManager.getLogger(UploadUtil.class);
	
	@Value("${param.revenue.uploadUrl:/opt/smartwater/water-upload/upload/}")
    private String uploadUrl;
	
	/**
     * 最大文件长度2M
     */
    private static final int MAX_FILE_SIZE = 20971520;
	
	public String uploadFile(byte[] array, String filename, String contentType) {
		           
        try {    
            if (array.length > MAX_FILE_SIZE) {
                throw new FrameworkRuntimeException(ResultCode.Fail, "上传的文件过大");
            }
            // 构造永久路径来存储上传的文件
            // 这个路径相对当前应用的目录
            String uploadPath = uploadUrl;
            logger.info(LogMsg.to("uploadPath", uploadPath));
            // 如果目录不存在则创建
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }
            logger.info(LogMsg.to("uploadPath", uploadPath));
            logger.info(LogMsg.to("filename", filename));
            String filePath = uploadPath + File.separator + filename;
            Files.write(Paths.get(filePath), array);
            return uploadPath + filename;
        } catch (Exception e) {
            logger.error("AjaxUploadFile error", e);
            throw new FrameworkRuntimeException(ResultCode.Fail, e.getMessage());
        }
	}
	
}
