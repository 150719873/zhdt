package com.dotop.smartwater.project.module.service.tool.impl;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dotop.smartwater.dependence.core.common.RootModel;
import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.UuidUtils;
import com.dotop.smartwater.project.module.core.water.bo.DataBackupBo;
import com.dotop.smartwater.project.module.core.water.dto.DataBackupDto;
import com.dotop.smartwater.project.module.core.water.vo.DataBackupVo;
import com.dotop.smartwater.project.module.dao.tool.IDataBackupDao;
import com.dotop.smartwater.project.module.service.tool.IDataBackupService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

/**
 * 

 * @date 2019年2月23日
 */
@Service
public class DataBackupServiceImpl implements IDataBackupService {

	private static final Logger LOGGER = LogManager.getLogger(DataBackupServiceImpl.class);

	@Autowired
	private IDataBackupDao iDataBackupDao;

	@Override
	public Pagination<DataBackupVo> page(DataBackupBo dataBackupBo) {
		try {
			Integer isNotDel = RootModel.NOT_DEL;
			// 参数转换
			DataBackupDto dataBackupDto = new DataBackupDto();
			BeanUtils.copyProperties(dataBackupBo, dataBackupDto);
			dataBackupDto.setIsDel(isNotDel);
			Page<Object> pageHelper = PageHelper.startPage(dataBackupBo.getPage(), dataBackupBo.getPageCount());
			List<DataBackupVo> list = iDataBackupDao.list(dataBackupDto);
			Pagination<DataBackupVo> pagination = new Pagination<>(dataBackupBo.getPageCount(), dataBackupBo.getPage());
			pagination.setData(list);
			pagination.setTotalPageSize(pageHelper.getTotal());
			return pagination;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public DataBackupVo get(DataBackupBo dataBackupBo) {
		try {
			Integer isNotDel = RootModel.NOT_DEL;
			// 参数转换
			DataBackupDto dataBackupDto = new DataBackupDto();

			dataBackupDto.setEnterpriseid(dataBackupBo.getEnterpriseid());
			dataBackupDto.setIsDel(isNotDel);
			dataBackupDto.setBackupId(dataBackupBo.getBackupId());
			return iDataBackupDao.get(dataBackupDto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public DataBackupVo add(DataBackupBo dataBackupBo) {
		try {
			Integer isNotDel = RootModel.NOT_DEL;
			String backupId = UuidUtils.getUuid();
			// 参数转换
			DataBackupDto dataBackupDto = new DataBackupDto();
			BeanUtils.copyProperties(dataBackupBo, dataBackupDto);
			dataBackupDto.setBackupId(backupId);
			dataBackupDto.setIsDel(isNotDel);
			iDataBackupDao.add(dataBackupDto);

			DataBackupVo dataBackupVo = new DataBackupVo();
			dataBackupVo.setBackupId(backupId);
			return dataBackupVo;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public String del(DataBackupBo dataBackupBo) {
		try {
			Integer isDel = RootModel.DEL;
			Integer isNotDel = RootModel.NOT_DEL;
			// 参数转换
			DataBackupDto dataBackupDto = new DataBackupDto();
			dataBackupDto.setBackupId(dataBackupBo.getBackupId());
			dataBackupDto.setEnterpriseid(dataBackupBo.getEnterpriseid());
			dataBackupDto.setIsDel(isNotDel);
			dataBackupDto.setNewIsDel(isDel);
			iDataBackupDao.del(dataBackupDto);
			return null;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}
}
