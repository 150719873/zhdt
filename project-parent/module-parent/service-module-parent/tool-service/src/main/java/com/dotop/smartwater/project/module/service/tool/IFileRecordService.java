package com.dotop.smartwater.project.module.service.tool;

import com.dotop.smartwater.dependence.core.common.BaseService;
import com.dotop.smartwater.project.module.core.water.bo.FileRecordBo;
import com.dotop.smartwater.project.module.core.water.vo.FileRecordVo;

/**
 * 上传文件记录Service

 * @date 2019-08-21 15:58
 *
 */
public interface IFileRecordService extends BaseService<FileRecordBo, FileRecordVo> {
	
	/**
	 * 新增上传文件记录
	 * @param fileRecordBo
	 * @return
	 */
	Integer insertRecord(FileRecordBo fileRecordBo);
}
