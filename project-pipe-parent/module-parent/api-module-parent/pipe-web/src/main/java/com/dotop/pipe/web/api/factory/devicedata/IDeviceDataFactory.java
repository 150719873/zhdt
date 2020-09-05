package com.dotop.pipe.web.api.factory.devicedata;

import org.springframework.web.multipart.MultipartFile;

import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;

public interface IDeviceDataFactory {

	public String importDate(MultipartFile file) throws FrameworkRuntimeException;

}
