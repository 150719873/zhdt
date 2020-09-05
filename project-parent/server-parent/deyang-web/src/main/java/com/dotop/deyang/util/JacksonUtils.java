package com.dotop.deyang.util;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

public class JacksonUtils {
	
	private static ObjectMapper mapper;
	 private static final Logger log = LoggerFactory.getLogger(JacksonUtils.class);
	/**
     * 获取ObjectMapper实例
     * @param_createNew 方式：true，新实例；false,存在的mapper实例
     * @return
     */
    private static synchronized ObjectMapper getMapperInstance() {
       if (mapper == null) {
            mapper = new ObjectMapper();
          //如果json中有新增的字段并且是YourClass类中不存在的，则会转换错误
            //需要加上如下语句
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
       }
        return mapper;
    }
	
	public static <T> String SerializeToString(T object){
		if(object == null) {
			return null;
		}
        try {
			return getMapperInstance().writeValueAsString(object);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return null;
		}
    }

	/**
	 * 将字符串转list对象
	 *
	 * @param <T>
	 * @param jsonStr
	 * @param cls
	 * @return
	 */
	public static <T> List<T> str2list(String jsonStr, Class<T> cls) {
		if(StrUtil.isBlank(jsonStr)){
			return null;
		}
		List<T> objList = null;
		try {
			JavaType t = getMapperInstance().getTypeFactory().constructParametricType(
					List.class, cls);
			objList = getMapperInstance().readValue(jsonStr, t);
		}catch (JsonParseException e) {
			log.error("JsonParseException",e.getCause());
		} catch (JsonMappingException e) {
			log.error("JsonMappingException",e.getCause());
		} catch (IOException e) {
			log.error("IOException",e.getCause());
		}
		return objList;
	}

	public static <T> byte[] SerializeToByte(T object){
		if(object == null){
			return null;
		}
        try {
			return getMapperInstance().writeValueAsBytes(object);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return null;
		}
    }
	
    public static <T> T ByteToObj(byte[] data, Class<T> interfaceClass) {  	
        return JsonToObj(new String(data), interfaceClass);
    } 
	
	public static <T> T JsonToObj(String jsonStr, Class<T> interfaceClass) {  
		if(StrUtil.isBlank(jsonStr)) {
			return null;
		}
        try {
			return getMapperInstance().readValue(jsonStr, interfaceClass);
		} catch (JsonParseException e) {
			log.error("JsonParseException",e.getCause());
		} catch (JsonMappingException e) {
			log.error("JsonMappingException",e.getCause());
		} catch (IOException e) {
			log.error("IOException",e.getCause());
		}  
        return null;
    }

}
