package com.dotop.smartwater.view.server.config;

import com.dotop.smartwater.project.module.core.auth.config.WaterClientConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class ContextConfig {

	@Value("${cas.auth.path}")
	private String casAuthPath;

	@PostConstruct
	public void init() {
		WaterClientConfig.WaterCasUrl = casAuthPath;
	}

}
