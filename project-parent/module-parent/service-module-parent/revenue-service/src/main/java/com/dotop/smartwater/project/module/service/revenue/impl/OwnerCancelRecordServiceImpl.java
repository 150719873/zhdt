package com.dotop.smartwater.project.module.service.revenue.impl;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.water.bo.customize.QueryBo;
import com.dotop.smartwater.project.module.core.water.dto.customize.QueryDto;
import com.dotop.smartwater.project.module.core.water.vo.OwnerCancelRecordVo;
import com.dotop.smartwater.project.module.dao.revenue.IOwnerCancelRecordDao;
import com.dotop.smartwater.project.module.service.revenue.IOwnerCancelRecordService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

/**

 * @date 2019/2/27.
 */
@Service
public class OwnerCancelRecordServiceImpl implements IOwnerCancelRecordService {

	private static final Logger LOGGER = LogManager.getLogger(OwnerCancelRecordServiceImpl.class);

	@Autowired
	private IOwnerCancelRecordDao iOwnerCancelRecordDao;

	@Override
	public Pagination<OwnerCancelRecordVo> findByPage(QueryBo queryBo) {
		try {
			// 参数转换
			QueryDto queryDto = new QueryDto();
			BeanUtils.copyProperties(queryBo, queryDto);
			// 操作数据
			Page<Object> pageHelper = PageHelper.startPage(queryBo.getPage(), queryBo.getPageCount());
			List<OwnerCancelRecordVo> list = iOwnerCancelRecordDao.getList(queryDto);
			// 拼接数据返回
			return new Pagination<>(queryBo.getPage(), queryBo.getPageCount(), list, pageHelper.getTotal());
		}   catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}
}
