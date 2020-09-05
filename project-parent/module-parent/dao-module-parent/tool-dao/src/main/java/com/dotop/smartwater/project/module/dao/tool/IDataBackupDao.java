package com.dotop.smartwater.project.module.dao.tool;

import com.dotop.smartwater.dependence.core.common.BaseDao;
import com.dotop.smartwater.project.module.core.water.dto.DataBackupDto;
import com.dotop.smartwater.project.module.core.water.vo.DataBackupVo;

import java.util.List;

public interface IDataBackupDao extends BaseDao<DataBackupDto, DataBackupVo> {

	@Override
	List<DataBackupVo> list(DataBackupDto dataBackupDto);

	@Override
	DataBackupVo get(DataBackupDto dataBackupDto);

	@Override
	void add(DataBackupDto dataBackupDto);

	@Override
	Integer del(DataBackupDto dataBackupDto);
}
