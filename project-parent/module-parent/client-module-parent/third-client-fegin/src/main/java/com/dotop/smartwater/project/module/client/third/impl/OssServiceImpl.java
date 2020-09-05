package com.dotop.smartwater.project.module.client.third.impl;

import java.util.Date;

import javax.annotation.Resource;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;

import com.dotop.smartwater.dependence.core.utils.UuidUtils;
import com.dotop.smartwater.project.module.client.third.IOssService;
import com.dotop.smartwater.project.module.core.third.utils.oss.OssUtil;
import com.dotop.smartwater.project.module.core.water.dto.FileRecordDto;
import com.dotop.smartwater.project.module.dao.tool.IFileRecordDao;

/**


 */
@Service
public class OssServiceImpl implements IOssService {

	@Resource
	private OssUtil ossUtil;
	
	@Resource
	private IFileRecordDao iFileRecordDao;

	private static final String SPLIT_STR = "/";

	@Override
	public String upLoad(byte[] array, String type, String model, String filename, String contentType, String userId) {
//		filename = type + SPLIT_STR + model + SPLIT_STR + filename;
		String url = ossUtil.uploadFile(array, filename, contentType);
		if(url != null && !"".equals(url)) {
			Long fileSize = Long.parseLong(String.valueOf(array.length));
			// 参数转换
			FileRecordDto fileRecordDto = new FileRecordDto();
			fileRecordDto.setFileRecordId(UuidUtils.getUuid());
			fileRecordDto.setFileName(filename);
			fileRecordDto.setFileSize(fileSize);
			fileRecordDto.setFileType(contentType);
			fileRecordDto.setFileSource(model);
			fileRecordDto.setFileAddress(url);
			fileRecordDto.setUploadTime(new Date());
			fileRecordDto.setUploadUserId(userId);
			iFileRecordDao.insertRecord(fileRecordDto);
		}
		return url;
	}
	
	@Override
	public String upLoad(byte[] array, String type, String model, String filename, String contentType) {
		String url = ossUtil.uploadFile(array, filename, contentType);
		if(url != null && !"".equals(url)) {
			Long fileSize = Long.parseLong(String.valueOf(array.length));
			// 参数转换
			FileRecordDto fileRecordDto = new FileRecordDto();
			fileRecordDto.setFileRecordId(UuidUtils.getUuid());
			fileRecordDto.setFileName(filename);
			fileRecordDto.setFileSize(fileSize);
			fileRecordDto.setFileType(contentType);
			fileRecordDto.setFileSource(model);
			fileRecordDto.setFileAddress(url);
			fileRecordDto.setUploadTime(new Date());
			fileRecordDto.setUploadUserId("");
			iFileRecordDao.insertRecord(fileRecordDto);
		}
		return url;
	}

	@Override
	public void del(String type, String model, String filename) {
		filename = type + SPLIT_STR + model + SPLIT_STR + filename;
		ossUtil.delImg(filename);
	}

	@Override
	public String upLoadV2(byte[] array, String type, String model, String filename, String contentType) {
		String md5 = DigestUtils.md5Hex(array);
		filename = type + SPLIT_STR + model + SPLIT_STR + md5 + "#" + filename;
		return ossUtil.upLoadImg(array, filename, contentType);
	}

	@Override
	public String getOssPrefix() {
		return ossUtil.getOssPrefix();
	}

	@Override
	public String getContentType(String filename) {
		return ossUtil.getContentType(filename);
	}

	@Override
	public byte[] getImgByte(String path) {
		// TODO Auto-generated method stub
		return ossUtil.getImgByte(path);
	}

}
