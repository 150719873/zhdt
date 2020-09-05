package com.dotop.smartwater.project.server.water.rest.service.revenue;

import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")

public class FileOutputController {

	private static final Logger LOGGER = LogManager.getLogger(FileOutputController.class);

	@Value("${param.revenue.excelTempUrl}")
	private String excelTempUrl;

	@Resource
	private ResourceLoader resourceLoader;

	@GetMapping("/order/{filename}")
	public void downloadManualMeterDemo(HttpServletResponse response, @PathVariable("filename") String filename) {
		InputStream inputStream = null;
		ServletOutputStream servletOutputStream = null;
		try {
			String path = "excel/" + filename;
			org.springframework.core.io.Resource resource = resourceLoader.getResource("classpath:" + path);
			response.setContentType("application/vnd.ms-excel");
			response.addHeader("Cache-Control", "no-cache, no-store, must-revalidate");
			response.addHeader("charset", "utf-8");
			response.addHeader("Pragma", "no-cache");
			String encodeName = URLEncoder.encode(filename, StandardCharsets.UTF_8.toString());
			response.setHeader("Content-Disposition",
					"attachment; filename=\"" + encodeName + "\"; filename*=utf-8''" + encodeName);

			inputStream = resource.getInputStream();
			servletOutputStream = response.getOutputStream();
			IOUtils.copy(inputStream, servletOutputStream);
			response.flushBuffer();
		} catch (Exception e) {
			LOGGER.info("下载文件异常 :", e.getMessage());
		} finally {
			try {
				if (servletOutputStream != null) {
					servletOutputStream.close();
				}
				if (inputStream != null) {
					inputStream.close();
				}
			} catch (Exception e) {
				LOGGER.info("下载文件异常 :", e.getMessage());
			}
		}
	}

	@GetMapping("/accounts/file/{filename}")
	public void downloadTemplate(HttpServletResponse response, @PathVariable("filename") String filename) {
		InputStream inputStream = null;
		ServletOutputStream servletOutputStream = null;
		try {
			String path = "excel/" + filename;
			org.springframework.core.io.Resource resource = resourceLoader.getResource("classpath:" + path);

			response.setContentType("application/vnd.ms-excel");
			response.addHeader("Cache-Control", "no-cache, no-store, must-revalidate");
			response.addHeader("charset", "utf-8");
			response.addHeader("Pragma", "no-cache");
			String encodeName = URLEncoder.encode(filename, StandardCharsets.UTF_8.toString());

			String result = "attachment; filename=\"" + encodeName + "\"; filename*=utf-8''" + encodeName;
			response.setHeader("Content-Disposition", result);

			inputStream = resource.getInputStream();
			servletOutputStream = response.getOutputStream();
			IOUtils.copy(inputStream, servletOutputStream);
			response.flushBuffer();
		} catch (Exception e) {
			LOGGER.info("下载模板异常:", e.getMessage());
		} finally {
			try {
				if (servletOutputStream != null) {
					servletOutputStream.close();
				}
				if (inputStream != null) {
					inputStream.close();
				}
			} catch (Exception e) {
				LOGGER.info("下载模板异常:", e.getMessage());
			}
		}
	}
}
