package com.dotop.smartwater.project.server.water.rest.service.tool;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.dotop.smartwater.dependence.core.common.BaseController;
import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.UuidUtils;
import com.dotop.smartwater.dependence.core.utils.VerificationUtils;
import com.dotop.smartwater.project.module.api.tool.IAppVersionFactory;
import com.dotop.smartwater.project.module.client.third.IOssService;
import com.dotop.smartwater.project.module.core.third.utils.oss.OssUtil;
import com.dotop.smartwater.project.module.core.water.constants.OssPathCode;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.module.core.water.enums.OperateTypeEnum;
import com.dotop.smartwater.project.module.core.water.form.AppVersionForm;
import com.dotop.smartwater.project.module.core.water.utils.QrCodeUtil;
import com.dotop.smartwater.project.module.core.water.vo.AppVersionMd5Vo;
import com.dotop.smartwater.project.module.core.water.vo.AppVersionVo;
import com.dotop.smartwater.project.server.water.common.FoundationController;

/**
 * App版本控制
 * 

 * @date 2019年3月5日 15:45
 *
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/appVersion")
public class AppVersionController extends FoundationController implements BaseController<AppVersionForm> {

	private static final Logger LOGGER = LogManager.getLogger(AppVersionController.class);

	@Autowired
	private IAppVersionFactory iAppVersionFactory;

	@Autowired
	private IOssService iOssService;
	
	@Autowired
	private OssUtil ossUtil;
	
	@Value("${param.revenue.uploadUrl}")
    private String uploadUrl;

	@Override
	@PostMapping(value = "/add", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
	public String add(@RequestBody AppVersionForm appVersionForm) {
		LOGGER.info(LogMsg.to("msg:", "新增版本功能开始", "appVersionForm", appVersionForm));
		String code = appVersionForm.getCode();
		String name = appVersionForm.getName();
		String versionNo = appVersionForm.getVersionNo();
//		Integer versionCode = appVersionForm.getVersionCode();
		Map<String, String> accessMap = appVersionForm.getAccessMap();
		String upgrade = appVersionForm.getUpgrade();
		String qrCode = appVersionForm.getQrCode();
		// 验证
		VerificationUtils.string("code", code);
		VerificationUtils.string("name", name);
		VerificationUtils.string("versionNo", versionNo);
		VerificationUtils.obj("accessMap", accessMap);		
//		VerificationUtils.integer("versionCode", versionCode);
		VerificationUtils.string("upgrade", upgrade);
		if(qrCode == null || "".equals(qrCode)) {
			return resp(ResultCode.Fail, "qrCode不能为空", null);
		}

		try {
			System.out.println("**********qrCode: " + qrCode);
			// 将AppUrl转换成二维码图片
			byte[] bytes = QrCodeUtil.createQrCodeImage(qrCode);
			String filename = UuidUtils.getUuid() + ".jpg";
			// 将生成的二维码图片上传到阿里云并返回Url
			String url = iOssService.upLoad(bytes, OssPathCode.IMAGE, OssPathCode.VERSION_CONTROLLER, filename, "image/jpeg");
			appVersionForm.setQrCode(url);
		} catch (Exception e) {
			LOGGER.info("新增异常：", e.getMessage());
		}
		auditLog(OperateTypeEnum.APPVERSION_CONTROl, "新增APP版本", "APP数据", appVersionForm);
		String message = iAppVersionFactory.addApp(appVersionForm);
		LOGGER.info(LogMsg.to("msg:", "新增版本功能结束", "appVersionForm", appVersionForm));
		if(message == "success") {
			return resp(ResultCode.Success, ResultCode.SUCCESS, null);
		}else {
			return resp(ResultCode.Fail, message, null);
		}
		
	}

	@PostMapping(value = "/page", produces = GlobalContext.PRODUCES)
	public String page(@RequestBody AppVersionForm appVersionForm) {
		LOGGER.info(LogMsg.to("msg:", " 分页查询开始", "appVersionForm", appVersionForm));
		Integer page = appVersionForm.getPage();
		Integer pageCount = appVersionForm.getPageCount();
		// 验证
		VerificationUtils.integer("page", page);
		VerificationUtils.integer("pageCount", pageCount);
		Pagination<AppVersionVo> pagination = iAppVersionFactory.page(appVersionForm);
		LOGGER.info(LogMsg.to("msg:", " 分页查询查询结束"));
		return resp(ResultCode.Success, ResultCode.SUCCESS, pagination);
	}

	@Override
	@PostMapping(value = "/get", produces = GlobalContext.PRODUCES)
	public String get(@RequestBody AppVersionForm appVersionForm) {
		LOGGER.info(LogMsg.to("msg:", " 查询历史版本开始", "appVersionForm", appVersionForm));
		String code = appVersionForm.getCode();
		// 验证
		VerificationUtils.string("code", code);
		List<AppVersionVo> list = iAppVersionFactory.getVersions(appVersionForm);
		LOGGER.info(LogMsg.to("msg:", " 查询历史版本结束"));
		return resp(ResultCode.Success, ResultCode.SUCCESS, list);
	}
	
	@PostMapping(value = "/getApp", produces = GlobalContext.PRODUCES)
	public String getApp(@RequestBody AppVersionForm appVersionForm) {
		LOGGER.info(LogMsg.to("msg:", " 查询App详情开始", "appVersionForm", appVersionForm));
		String id = appVersionForm.getId();
		// 验证
		VerificationUtils.string("id", id);
		AppVersionVo app = iAppVersionFactory.getApp(appVersionForm);
		LOGGER.info(LogMsg.to("msg:", "查询App详情结束"));
		return resp(ResultCode.Success, ResultCode.SUCCESS, app);
	}

	@PostMapping(value = "/getAppName", produces = GlobalContext.PRODUCES)
	public String getAppName() {
		LOGGER.info(LogMsg.to("msg:", " 查询详情开始", null));

		List<AppVersionVo> list = iAppVersionFactory.getAppName();
		LOGGER.info(LogMsg.to("msg:", " 查询详情结束"));
		return resp(ResultCode.Success, ResultCode.SUCCESS, list);
	}

	@PostMapping(value = "/update", produces = GlobalContext.PRODUCES)
	public String update(@RequestBody AppVersionForm appVersionForm) {
		LOGGER.info(LogMsg.to("msg:", " 修改信息开始", "appVersionForm", appVersionForm));
		String id = appVersionForm.getId();
		Boolean sign = appVersionForm.getSign();
		// 验证
		VerificationUtils.string("id", id);
		if(sign == null) {
			return resp(ResultCode.Fail, "必填字段不能为空", null);
		}
		if(appVersionForm.getAccessMap() != null && (appVersionForm.getQrCode() != null && !"".equals(appVersionForm.getQrCode()))) {
			try {
				System.out.println("**********qrCode: " + appVersionForm.getQrCode());
				// 将AppUrl转换成二维码图片
				byte[] bytes = QrCodeUtil.createQrCodeImage(appVersionForm.getQrCode());
				String filename = UuidUtils.getUuid() + ".jpg";
				// 将生成的二维码图片上传到阿里云并返回Url
				String url = iOssService.upLoad(bytes, OssPathCode.IMAGE, OssPathCode.VERSION_CONTROLLER, filename, "image/jpeg");
				appVersionForm.setQrCode(url);
			} catch (Exception e) {
				LOGGER.info("新增异常：", e.getMessage());
			}
		}

		iAppVersionFactory.revise(appVersionForm);
		LOGGER.info(LogMsg.to("msg:", " 修改信息结束"));
		return resp(ResultCode.Success, ResultCode.SUCCESS, null);
	}

	@Override
	@PostMapping(value = "/del", produces = GlobalContext.PRODUCES, consumes = GlobalContext.CONSUMES)
	public String del(@RequestBody AppVersionForm appVersionForm) {
		LOGGER.info(LogMsg.to("msg:", "删除内容开始", "appVersionForm", appVersionForm));
		String id = appVersionForm.getId();
		VerificationUtils.string("id", id);
		auditLog(OperateTypeEnum.APPVERSION_CONTROl, "删除APP版本", "版本ID", id);
		String versionId = iAppVersionFactory.del(appVersionForm);
		LOGGER.info(LogMsg.to("msg:", "删除内容结束", "versionId", versionId));
		return resp(ResultCode.Success, ResultCode.SUCCESS, versionId);
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
//					filename = DigestUtils.md5Hex(filename);
					String md5Key = DigestUtils.md5Hex(bytes);
					String suffix = filename.substring(filename.lastIndexOf('.'));
					String contentType = ossUtil.getContentType(suffix);

					String url = iOssService.upLoad(bytes, OssPathCode.FILE, OssPathCode.VERSION_CONTROLLER, filename, contentType, userId);
					AppVersionMd5Vo resulte = new AppVersionMd5Vo();
					resulte.setUrl(url);
					resulte.setMd5Key(md5Key);
					return resp(ResultCode.Success, ResultCode.SUCCESS, resulte);
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
	
	/**
	 * 上传图片及保存文件
	 */
	@PostMapping("/uploadImg")
	public String uploadImg(HttpServletRequest request, @RequestParam("userid") String userId) {
		List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles("file");
		if (0 >= files.size()) {
			return resp(ResultCode.ParamIllegal, "没有找到上传文件", null);
		}

		MultipartFile file;
//		for (MultipartFile file1 : files) {
//			file = file1;
			file = files.get(files.size()-1);
			if (!file.isEmpty()) {
				try {
					byte[] bytes = file.getBytes();

					String filename = file.getOriginalFilename();
//					filename = DigestUtils.md5Hex(filename);
					String suffix = filename.substring(filename.lastIndexOf('.'));
					String contentType = ossUtil.getContentType(suffix);

					String url = iOssService.upLoad(bytes, OssPathCode.IMAGE, OssPathCode.VERSION_CONTROLLER, filename, contentType, userId);
					return resp(ResultCode.Success, ResultCode.SUCCESS, url);
				} catch (Exception e) {
					LOGGER.error("上传附件失败", e);
					return resp(ResultCode.Fail, e.getMessage(), null);
				}
			} else {
				return resp(ResultCode.Fail, "空文件！", null);
			}
//		}
	}

	/**
	 * 下载文件
	 * 
	 * @param response
	 * @param access
	 * @throws Exception
	 */
	@PostMapping(value = "/download1", produces = GlobalContext.PRODUCES)
	public void download(HttpServletResponse response, @RequestBody Map<String, String> access) throws Exception {

		LOGGER.info(LogMsg.to("msg:", "下载文件开始", "accessMap", access));
		String fileName = URLEncoder.encode(access.get("name"), StandardCharsets.UTF_8.name());// 设置文件名，根据业务需要替换成要下载的文件名
		String urlString = access.get("url");

		BufferedInputStream bis = null;
		// 响应二进制流
		response.setContentType(GlobalContext.OCTET);
		// 设置response响应头，真实文件名重命名，就是在这里设置，设置编码
		response.setHeader("Content-Disposition", "filename=" + fileName);
		try (ServletOutputStream output = response.getOutputStream();
				BufferedOutputStream buff = new BufferedOutputStream(output)) {
			// 根据网络文件地址创建URL
			URL url = new URL(urlString);
			// 获取此路径的连接
			URLConnection conn = url.openConnection();
			long fileLength = conn.getContentLengthLong();// 获取文件大小

			bis = new BufferedInputStream(conn.getInputStream());// 构造读取流
			byte[] bytes = new byte[(int) fileLength];
			bis.read(bytes);
			bis.close();
			buff.write(bytes);
			buff.flush();
			LOGGER.info(LogMsg.to("msg:", "下载文件结束"));
		} catch (IOException e) {
			LOGGER.error("文件下载失败！", e);
			throw new Exception("文件下载失败！");
		} finally {
			try {
				if (null != bis) {
					bis.close();
				}
			} catch (IOException e) {
				LOGGER.error("文件下载失败！", e);
				throw new Exception("文件下载失败！");
			}
		}
	}
	
	@PostMapping(value = "/download", produces = GlobalContext.PRODUCES)
	public void downloadFile(HttpServletResponse response, @RequestBody Map<String, String> access) throws Exception {
		String fileName = URLEncoder.encode(access.get("name"), StandardCharsets.UTF_8.name());// 设置文件名，根据业务需要替换成要下载的文件名
		String urlString = access.get("url");
		try {
            // path是指欲下载的文件的路径。
            File file = new File(urlString);
 
            // 以流的形式下载文件。
            InputStream fis = new BufferedInputStream(new FileInputStream(urlString));
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();
            // 清空response
            response.reset();
            // 设置response的Header
            response.addHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes()));
            response.addHeader("Content-Length", "" + file.length());
            OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/octet-stream");
            toClient.write(buffer);
            toClient.flush();
            toClient.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
	@RequestMapping(value="/downApp", method= RequestMethod.GET)
//	@PostMapping(value = "/downApp", produces = GlobalContext.PRODUCES)
	public String downApp(HttpServletResponse response, @RequestParam("name") String name, @RequestParam("url") String url) throws Exception {
		if(StringUtils.isBlank(name) || StringUtils.isBlank(url)) {
			return resp(ResultCode.Fail, "获取文件名称或路径失败", null);
		}
		String fileName = URLEncoder.encode(name.substring(1, name.length()-1), StandardCharsets.UTF_8.name());// 设置文件名，根据业务需要替换成要下载的文件名
		String urlString = uploadUrl + name.substring(1, name.length()-1);
		System.out.println("name: " + new String(fileName.getBytes()));
		System.out.println("url: " + urlString);
		try {
			// path是指欲下载的文件的路径。
            File file = new File(urlString);
            // 以流的形式下载文件
            InputStream fis = new BufferedInputStream(new FileInputStream(urlString));
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();
            // 清空response
            response.reset();
            response.setHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes()));
            response.addHeader("Content-Length", "" + file.length());
            // 设置相应类型application/octet-stream
         	response.setContentType("application/x-msdownload");
            OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
	        toClient.write(buffer);
	        toClient.flush();
	        toClient.close();		
            return resp(ResultCode.SUCCESS, "下载成功", null);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
		return resp(ResultCode.Fail, "下载失败", null);
    }
}
