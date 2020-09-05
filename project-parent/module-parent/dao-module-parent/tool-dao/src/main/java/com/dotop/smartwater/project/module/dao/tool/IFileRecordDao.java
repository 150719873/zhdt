package com.dotop.smartwater.project.module.dao.tool;

import com.dotop.smartwater.dependence.core.common.BaseDao;
import com.dotop.smartwater.project.module.core.water.dto.FileRecordDto;
import com.dotop.smartwater.project.module.core.water.vo.FileRecordVo;

/**
 * 上传文件记录Dao

 * @date 2019-08-21 16:07
 *
 */

public interface IFileRecordDao extends BaseDao<FileRecordDto, FileRecordVo>  {
	
	Integer insertRecord(FileRecordDto fileRecordDto);
}

