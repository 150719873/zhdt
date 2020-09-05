package com.dotop.pipe.server.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.http.MediaType;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;

//@Configuration
public class WebMvcConfig {

	// @Bean
	public HttpMessageConverters fastJsonHttpMessageConverters() {
		// 创建FastJson信息转换对象
		FastJsonHttpMessageConverter fastJsonHttpMessageConverter = new FastJsonHttpMessageConverter();

		// 创建Fastjosn对象并设定序列化规则
		FastJsonConfig fastJsonConfig = new FastJsonConfig();
		// fastJsonConfig.setSerializerFeatures(SerializerFeature.PrettyFormat,
		// SerializerFeature.DisableCircularReferenceDetect);
		fastJsonConfig.setSerializerFeatures(SerializerFeature.PrettyFormat,
				SerializerFeature.WriteNonStringKeyAsString);
		// fastJsonConfig.setDateFormat("yyyy-MM-dd HH:mm:ss");
		fastJsonHttpMessageConverter.setFastJsonConfig(fastJsonConfig);

		// 中文乱码解决方案
		List<MediaType> mediaTypes = new ArrayList<>();
		mediaTypes.add(MediaType.APPLICATION_JSON_UTF8);// 设定json格式且编码为UTF-8
		fastJsonHttpMessageConverter.setSupportedMediaTypes(mediaTypes);

		return new HttpMessageConverters(fastJsonHttpMessageConverter);

	}
}
