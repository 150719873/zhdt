package com.dotop.smartwater.project.module.api.revenue.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.project.module.api.revenue.IImageFactory;
import com.dotop.smartwater.project.module.core.auth.bo.LogoBo;
import com.dotop.smartwater.project.module.core.auth.local.AuthCasClient;
import com.dotop.smartwater.project.module.core.auth.vo.LogoVo;
import com.dotop.smartwater.project.module.core.auth.vo.UserVo;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.module.core.water.utils.ImageUtil;
import com.dotop.smartwater.project.module.service.revenue.ILogoService;

@Component
public class ImageFactoryImpl implements IImageFactory {

	private static final Logger logger = LoggerFactory.getLogger(ImageFactoryImpl.class);
	// 上传配置
	@Value("${ajaxUpload.tempUrl}")
	private String tempUrl;
	@Value("${ajaxUpload.MEMORY_THRESHOLD}")
	private int MEMORY_THRESHOLD;
	@Value("${ajaxUpload.MAX_FILE_SIZE}")
	private int MAX_FILE_SIZE;
	@Value("${ajaxUpload.MAX_REQUEST_SIZE}")
	private int MAX_REQUEST_SIZE;

	@Autowired
	private ILogoService iLogoService;

	/**
	 * 上传图片
	 */
	public void logo(HttpServletRequest request, String userid) throws FrameworkRuntimeException {

		UserVo user = AuthCasClient.getUser();
		if (user == null) {
			throw new FrameworkRuntimeException(ResultCode.Fail, "无用户信息");
		}

		// 配置上传参数
		DiskFileItemFactory factory = new DiskFileItemFactory();
		try {
			// 检测是否为多媒体上传
			if (!ServletFileUpload.isMultipartContent(request)) {
				throw new FrameworkRuntimeException(ResultCode.Fail, "Error: must include enctype=multipart/form-data");
			}

			// 设置内存临界值 - 超过后将产生临时文件并存储于临时目录中
			factory.setSizeThreshold(MEMORY_THRESHOLD);
			// 设置临时存储目录
			factory.setRepository(new File(System.getProperty("java.io.tmpdir")));

			ServletFileUpload upload = new ServletFileUpload(factory);

			// 设置最大文件上传值
			upload.setFileSizeMax(MAX_FILE_SIZE);

			// 设置最大请求值 (包含文件和表单数据)
			upload.setSizeMax(MAX_REQUEST_SIZE);

			// 构造临时路径来存储上传的文件
			// 这个路径相对当前应用的目录
			String uploadPath = request.getServletContext().getRealPath(tempUrl);

			// 如果目录不存在则创建
			File uploadDir = new File(uploadPath);
			if (!uploadDir.exists()) {
				uploadDir.mkdir();
			}

			// 解析请求的内容提取文件数据
			List<FileItem> formItems = upload.parseRequest(request);

			if (formItems != null && formItems.size() > 0) {
				// 迭代表单数据
				for (FileItem item : formItems) {
					// 处理不在表单中的字段
					if (!item.isFormField()) {
						String fileName = item.getName();
						String filePath = uploadPath + File.separator + fileName;
						File storeFile = new File(filePath);

						if (storeFile.isDirectory()) {// 判断文件的存在性
							System.out.println(fileName + "文件存在!");
							storeFile.delete();
						}
						// 在控制台输出文件的上传路径
						System.out.println(filePath);
						// 保存文件到硬盘
						item.write(storeFile);
						byte[] bFile = readBytesFromFile(storeFile);
						LogoBo logo = new LogoBo();
						logo.setContent(bFile);
						logo.setEnterpriseid(user.getEnterpriseid());
						logo.setName(item.getName());

						// 设置后缀
						String[] data = item.getName().split("\\.");
						String suffix = data[data.length - 1];
						logo.setStat(suffix);
						iLogoService.add(logo);
					}
				}

			}

			logger.error("upload logo fail: formItems.size() " + formItems.size());
		} catch (Exception ex) {
			logger.error("error", ex);
			logger.error("logo", ex.getMessage());
			throw new FrameworkRuntimeException(ResultCode.Fail, ex.getMessage());
		} finally {
			factory = null;
			try {
				request.getInputStream().close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	private static byte[] readBytesFromFile(File file) {

		FileInputStream fileInputStream = null;
		byte[] bytesArray = null;

		try {
			bytesArray = new byte[(int) file.length()];
			// read file into bytes[]
			fileInputStream = new FileInputStream(file);
			fileInputStream.read(bytesArray);

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fileInputStream != null) {
				try {
					fileInputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
		return bytesArray;

	}

	@Override
	public void getLogo(HttpServletResponse response, String userid) throws FrameworkRuntimeException {
		UserVo user = AuthCasClient.getUser();
		if (user == null) {
			throw new FrameworkRuntimeException(ResultCode.Fail, "无用户信息");
		}

		LogoBo logoBo = new LogoBo();
		logoBo.setEnterpriseid(user.getEnterpriseid());
		LogoVo logo = iLogoService.get(logoBo);
		if (logo == null || logo.getContent().length == 0)
			throw new FrameworkRuntimeException(ResultCode.Fail, "无logo");
		try {
			if ("png".equals(logo.getStat())) {
				ImageIO.write(ImageUtil.resizePng(logo.getContent(), 140, 32, false), "png",
						response.getOutputStream());
			} else {
				ImageIO.write(ImageUtil.resizeJPG(logo.getContent(), 140, 32, false), "jpeg",
						response.getOutputStream());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
