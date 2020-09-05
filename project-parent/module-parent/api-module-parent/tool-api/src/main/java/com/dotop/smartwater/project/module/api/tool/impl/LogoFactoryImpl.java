package com.dotop.smartwater.project.module.api.tool.impl;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.project.module.api.tool.ILogoFactory;
import com.dotop.smartwater.project.module.client.third.IOssService;
import com.dotop.smartwater.project.module.core.auth.bo.LogoBo;
import com.dotop.smartwater.project.module.core.auth.local.IAuthCasClient;
import com.dotop.smartwater.project.module.core.auth.vo.LogoVo;
import com.dotop.smartwater.project.module.core.water.constants.OssPathCode;
import com.dotop.smartwater.project.module.service.revenue.ILogoService;

@Component
public class LogoFactoryImpl implements ILogoFactory, IAuthCasClient {

	private static final Logger logger = LogManager.getLogger(LogoFactoryImpl.class);

	@Autowired
	private ILogoService iLogoService;

	@Autowired
	private IOssService iOssService;

	@Override
	public LogoVo getLogo()  {
		LogoBo logoBo = new LogoBo();
		logoBo.setEnterpriseid(getEnterpriseid());
		LogoVo logoVo = iLogoService.get(logoBo);
		if(logoVo != null && !StringUtils.isBlank(logoVo.getOssurl())) {
			//获取logo数据流
			logoVo.setContent(iOssService.getImgByte(logoVo.getOssurl()));
		}		
		return logoVo;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public String uploadCompanyLogo(MultipartFile file)  {
		try {
			String filename = file.getOriginalFilename();
			String contentType = iOssService.getContentType(filename.substring(filename.lastIndexOf('.')));
//			filename = DigestUtils.md5Hex(file.getBytes());
			String url = iOssService.upLoad(file.getBytes(), OssPathCode.IMAGE, OssPathCode.COMPANY_LOGO, filename,
					contentType);
			LogoBo logoBo = new LogoBo();
			logoBo.setOssurl(url);
			logoBo.setEnterpriseid(getEnterpriseid());
			logoBo.setName(filename);
			// 设置后缀
			logoBo.setStat(contentType);
			iLogoService.add(logoBo);
			return url;
		} catch (IOException e) {
			logger.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.BASE_ERROR, "文件上传异常");
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public void delCompanyLogo()  {
		LogoBo logoBo = new LogoBo();
		logoBo.setEnterpriseid(getEnterpriseid());
		LogoVo logoVo = iLogoService.get(logoBo);
		if (logoVo == null) {
			return;
		}
		if (logoVo.getOssurl() != null && logoVo.getName() != null) {
			iOssService.del(OssPathCode.IMAGE, OssPathCode.COMPANY_LOGO, logoVo.getName());
		}
		iLogoService.del(logoBo);
	}
}
