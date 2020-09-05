package com.dotop.smartwater.project.module.service.operation.impl;

import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.utils.UuidUtils;
import com.dotop.smartwater.project.module.core.water.bo.UserOperationRecordBo;
import com.dotop.smartwater.project.module.core.water.dto.UserOperationRecordDto;
import com.dotop.smartwater.project.module.core.water.vo.UserOperationRecordVo;
import com.dotop.smartwater.project.module.dao.operation.IUserOperationRecordDao;
import com.dotop.smartwater.project.module.service.operation.IUserOperationRecordService;

import java.util.List;

/**

 * @date 2019/2/25.
 */
@Component
public class UserOperationRecordServiceImpl implements IUserOperationRecordService {

	private static final Logger LOGGER = LogManager.getLogger(UserOperationRecordServiceImpl.class);

	@Autowired
	private IUserOperationRecordDao iUserOperationRecordDao;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public UserOperationRecordVo add(UserOperationRecordBo userOperationRecordBo) {
		try {
			// 参数转换
			UserOperationRecordDto userOperationRecordDto = new UserOperationRecordDto();
			BeanUtils.copyProperties(userOperationRecordBo, userOperationRecordDto);
			// 操作数据
			userOperationRecordDto.setId(UuidUtils.getUuid());

			// 拼接数据返回
			iUserOperationRecordDao.add(userOperationRecordDto);
			UserOperationRecordVo userOperationRecordVo = new UserOperationRecordVo();
			BeanUtils.copyProperties(userOperationRecordDto, userOperationRecordVo);
			return userOperationRecordVo;
		} catch (FrameworkRuntimeException e) {
			LOGGER.error(LogMsg.to(e));
			throw e;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public Pagination<UserOperationRecordVo> page(UserOperationRecordBo userOperationRecordBo) {
		try {
			// 参数转换
			UserOperationRecordDto userOperationRecordDto = new UserOperationRecordDto();
			BeanUtils.copyProperties(userOperationRecordBo, userOperationRecordDto);
			// 操作数据
			Page<Object> pageHelper = PageHelper.startPage(userOperationRecordBo.getPage(), userOperationRecordBo.getPageCount());
			List<UserOperationRecordVo> list = iUserOperationRecordDao.list(userOperationRecordDto);
			return new Pagination<>(userOperationRecordBo.getPage(), userOperationRecordBo.getPageCount(),
					list, pageHelper.getTotal());
		} catch (FrameworkRuntimeException e) {
			LOGGER.error(LogMsg.to(e));
			throw e;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}
}
