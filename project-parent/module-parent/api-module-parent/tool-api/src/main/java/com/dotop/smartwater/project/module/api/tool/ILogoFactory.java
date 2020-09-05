package com.dotop.smartwater.project.module.api.tool;

import org.springframework.web.multipart.MultipartFile;

import com.dotop.smartwater.project.module.core.auth.vo.LogoVo;

public interface ILogoFactory {

	LogoVo getLogo() ;

	String uploadCompanyLogo(MultipartFile file) ;

	void delCompanyLogo() ;
}
