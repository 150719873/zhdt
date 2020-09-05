package com.dotop.pipe.service.mark;

import com.dotop.pipe.api.dao.mark.IMarkDao;
import com.dotop.pipe.api.service.mark.IMarkService;
import com.dotop.pipe.core.bo.mark.MarkBo;
import com.dotop.pipe.core.dto.mark.MarkDto;
import com.dotop.pipe.core.vo.mark.MarkVo;
import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.BeanUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MarkServiceImpl  implements IMarkService {

	private final static Logger logger = LogManager.getLogger(MarkServiceImpl.class);

	@Autowired
	private IMarkDao iMarkDao;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public MarkVo add(MarkBo markBo) throws FrameworkRuntimeException {
		try {
			MarkDto markDto = BeanUtils.copy(markBo, MarkDto.class);
			iMarkDao.add(markDto);
			return BeanUtils.copy(markBo, MarkVo.class);
		} catch (DataAccessException e) {
			logger.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public MarkVo get(MarkBo markBo) throws FrameworkRuntimeException {
		try {
			MarkDto markDto = BeanUtils.copy(markBo, MarkDto.class);
			return iMarkDao.get(markDto);
		} catch (DataAccessException e) {
			logger.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public Pagination<MarkVo> page(MarkBo markBo) throws FrameworkRuntimeException {
		MarkDto markDto = BeanUtils.copy(markBo, MarkDto.class);
		// 操作数据
		Page<Object> pageHelper = PageHelper.startPage(markBo.getPage(), markBo.getPageCount());
		List<MarkVo> list = iMarkDao.list(markDto);
		// 拼接数据返回
		return new Pagination<>(markBo.getPage(), markBo.getPageCount(), list, pageHelper.getTotal());
	}

	@Override
	public List<MarkVo> list(MarkBo markBo) throws FrameworkRuntimeException {
		try {
			MarkDto markDto = BeanUtils.copy(markBo, MarkDto.class);
			return iMarkDao.list(markDto);
		} catch (DataAccessException e) {
			logger.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public MarkVo edit(MarkBo markBo) throws FrameworkRuntimeException {
		try {
			MarkDto markDto = BeanUtils.copy(markBo, MarkDto.class);
			Integer count = iMarkDao.edit(markDto);
			if(count != 1) {
				throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, "更新失败");
			}
			return BeanUtils.copy(markBo, MarkVo.class);
		} catch (DataAccessException e) {
			logger.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public String del(MarkBo markBo) throws FrameworkRuntimeException {
		try {
			MarkDto markDto = BeanUtils.copy(markBo, MarkDto.class);
			Integer count = iMarkDao.del(markDto);
			if(count != 1) {
				throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, "删除失败");
			}
			return "success";
		} catch (DataAccessException e) {
			logger.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}
}
