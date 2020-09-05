package com.dotop.smartwater.project.auth.config;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;

/**
 * 

 * @date 2019年5月9日
 * @description response消息体转换
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		List<MediaType> mediaTypes = new ArrayList<>();
		mediaTypes.add(MediaType.APPLICATION_JSON_UTF8);

		StringHttpMessageConverter stringHttpMessageConverter = new StringHttpMessageConverter(StandardCharsets.UTF_8);
		stringHttpMessageConverter.setSupportedMediaTypes(mediaTypes);
		converters.add(0, stringHttpMessageConverter);

		FastJsonHttpMessageConverter fastJsonHttpMessageConverter = new FastJsonHttpMessageConverter();
		FastJsonConfig fastJsonConfig = new FastJsonConfig();
		fastJsonConfig.setSerializerFeatures(SerializerFeature.PrettyFormat, SerializerFeature.WriteNullStringAsEmpty);
		fastJsonHttpMessageConverter.setFastJsonConfig(fastJsonConfig);
		fastJsonHttpMessageConverter.setSupportedMediaTypes(mediaTypes);

		converters.add(1, fastJsonHttpMessageConverter);
		System.out.println(converters);
	}

}
