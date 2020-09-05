package com.dotop.pipe.server.config;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import com.dotop.smartwater.project.module.core.auth.config.WaterClientConfig;

@Configuration
public class ContextConfig {

	@Value("${cas.auth.path}")
	private String casAuthPath;

	@PostConstruct
	public void init() {
		WaterClientConfig.WaterCasUrl = casAuthPath;
	}

}
