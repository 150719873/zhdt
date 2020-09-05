package com.dotop.smartwater.project.module.core.third.utils.oss;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.aliyun.oss.HttpMethod;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.GeneratePresignedUrlRequest;
import com.aliyun.oss.model.ObjectMetadata;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.utils.JSONUtils;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;

/**
 * 阿里云 OSS文件类
 *

 */
@Component
public class OssUtil {

    private static final Logger log = LoggerFactory.getLogger(OssUtil.class);

    @Value("${oss.AccessKeyId:LTAIDJlp3dfxuYv7}")
    private String AccessKeyId;

    @Value("${oss.AccessKeySecret:pngzcNcsLwBtibN92tePKjanM5MlWL}")
    private String AccessKeySecret;

    @Value("${oss.endpoint:oss-cn-shanghai.aliyuncs.com}")
    private String endpoint;

    @Value("${oss.bucketName:resource-iot}")
    private String bucketName;

    @Value("${oss.fileDir:water-cas/}")
    private String fileDir;
    
    @Value("${param.revenue.uploadUrl:/opt/smartwater/water-upload/upload/}")
    private String uploadUrl;
	
	/**
     	* 最大文件长度20M
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
            log.info(LogMsg.to("uploadPath", uploadPath));
            // 如果目录不存在则创建
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }
            log.info(LogMsg.to("uploadPath", uploadPath));
            log.info(LogMsg.to("filename", filename));
            String filePath = uploadPath + filename;
            System.out.println("******************filePath: " + filePath);
            Files.write(Paths.get(filePath), array);
            return uploadPath + filename;
        } catch (Exception e) {
        	log.error("AjaxUploadFile error", e);
            throw new FrameworkRuntimeException(ResultCode.Fail, e.getMessage());
        }
	}

    public String upLoadImg(byte[] array, String filename, String contentType) {
        OSSClient client = new OSSClient(endpoint, AccessKeyId, AccessKeySecret);
        try {
            Date now = new Date();
            ObjectMetadata objectMeta = new ObjectMetadata();
            objectMeta.setContentLength(array.length);
            objectMeta.setContentType(contentType);
            ByteArrayInputStream is = new ByteArrayInputStream(array);
            client.putObject(bucketName, fileDir + filename, is, objectMeta);

            // 设过期日期(10年)
            Date expiration = DateUtils.addDays(now, 3650);

            // 生成图片链接签名字串
            GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(bucketName, fileDir + filename,
                    HttpMethod.GET);
            request.setExpiration(expiration);
            URL signedUrl = client.generatePresignedUrl(request); // 生成URL签名(HTTP GET请求)

            String url = signedUrl.toString();
            return url.split("\\?")[0];
        } catch (OSSException e) {
            log.error("error uploadImage", e);
            return null;
        } finally {
            client.shutdown();
        }

    }
    
    

    public void delImg(String filename) {
        OSSClient client = new OSSClient(endpoint, AccessKeyId, AccessKeySecret);
        try {
            client.deleteObject(bucketName, fileDir + filename);
        } catch (OSSException e) {
            log.error("error uploadImage", e);
        } finally {
            client.shutdown();
        }
    }

    /**
     * Description: 判断OSS服务文件上传时文件的contentType
     *
     * @param FilenameExtension 文件后缀
     * @return String
     */
    public String getContentType(String FilenameExtension) {
        if (FilenameExtension.equalsIgnoreCase(".bmp")) {
            return "image/bmp";
        }
        if (FilenameExtension.equalsIgnoreCase(".gif")) {
            return "image/gif";
        }
        if (FilenameExtension.equalsIgnoreCase(".jpeg") || FilenameExtension.equalsIgnoreCase(".jpg")
                || FilenameExtension.equalsIgnoreCase(".png")) {
            return "image/jpeg";
        }
        if (FilenameExtension.equalsIgnoreCase(".html")) {
            return "text/html";
        }
        if (FilenameExtension.equalsIgnoreCase(".txt")) {
            return "text/plain";
        }
        if (FilenameExtension.equalsIgnoreCase(".vsd")) {
            return "application/vnd.visio";
        }
        if (FilenameExtension.equalsIgnoreCase(".pptx") || FilenameExtension.equalsIgnoreCase(".ppt")) {
            return "application/vnd.ms-powerpoint";
        }
        if (FilenameExtension.equalsIgnoreCase(".docx") || FilenameExtension.equalsIgnoreCase(".doc")) {
            return "application/msword";
        }
        if (FilenameExtension.equalsIgnoreCase(".xml")) {
            return "text/xml";
        }
        if (FilenameExtension.equalsIgnoreCase(".apk")) {
        	return "application/vnd.android.package-archive";
        }
        if(FilenameExtension.equalsIgnoreCase(".xls") || FilenameExtension.equalsIgnoreCase(".xlsx")) {
        	return "application/vnd.ms-excel";
        }
        return FilenameExtension;
    }

    public  String getOssPrefix() {
        return "http://" + bucketName + "." + endpoint + "/" + fileDir;
    }
    
    public byte[] getImgByte(String path) {
    	System.out.println("path.substring(1,5): " + path.substring(1, 5));
    	if(path != null && path != "" && !"http".equals(path.substring(1,5)) && !"http".equals(path.substring(0,4))) {
    		// path是指欲下载的文件的路径。
    		// 以流的形式下载文件。
            InputStream fis;
			try {
				if("\"".equals(path.substring(0))) {
					fis = new BufferedInputStream(new FileInputStream(path.split("\"")[1]));
				}else {
					fis = new BufferedInputStream(new FileInputStream(path));
				}
				byte[] buffer = new byte[fis.available()];
	            fis.read(buffer);
	            fis.close();
	            System.out.println("byte[]: " + JSONUtils.toJSONString(buffer));
	            return buffer;
			} catch (IOException ex) {
				// TODO Auto-generated catch block
				ex.printStackTrace();
			}
    	}
    	return null;
    }
}
