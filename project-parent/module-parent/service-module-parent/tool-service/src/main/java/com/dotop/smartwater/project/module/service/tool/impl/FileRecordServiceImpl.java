package com.dotop.smartwater.project.module.service.tool.impl;

import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.utils.UuidUtils;
import com.dotop.smartwater.project.module.core.auth.local.AuthCasClient;
import com.dotop.smartwater.project.module.core.auth.vo.UserVo;
import com.dotop.smartwater.project.module.core.water.bo.FileRecordBo;
import com.dotop.smartwater.project.module.core.water.dto.FileRecordDto;
import com.dotop.smartwater.project.module.dao.tool.IFileRecordDao;
import com.dotop.smartwater.project.module.service.tool.IFileRecordService;

/**
 * 上传文件记录ServiceImpl

 * @date 2019-08-21 15:58
 *
 */
@Service
public class FileRecordServiceImpl implements IFileRecordService {

	private static final Logger LOGGER = LogManager.getLogger(AppVersionServiceImpl.class);
	
	@Autowired
	private IFileRecordDao iFileRecordDao;
	
	@Override
	public Integer insertRecord(FileRecordBo fileRecordBo) {
		// TODO Auto-generated method stub
		try {
			UserVo user = AuthCasClient.getUser();
			// 参数转换
			FileRecordDto fileRecordDto = new FileRecordDto();
			BeanUtils.copyProperties(fileRecordBo, fileRecordDto);
			fileRecordDto.setFileRecordId(UuidUtils.getUuid());
			fileRecordDto.setUploadTime(new Date());
			fileRecordDto.setUploadUserId(user.getUserid());
			return iFileRecordDao.insertRecord(fileRecordDto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

}
