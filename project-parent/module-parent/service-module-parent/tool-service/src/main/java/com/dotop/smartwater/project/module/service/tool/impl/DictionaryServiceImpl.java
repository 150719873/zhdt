package com.dotop.smartwater.project.module.service.tool.impl;

import com.dotop.smartwater.dependence.core.common.RootModel;
import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.BeanUtils;
import com.dotop.smartwater.project.module.core.water.bo.DictionaryBo;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.module.core.water.dto.DictionaryChildDto;
import com.dotop.smartwater.project.module.core.water.dto.DictionaryDto;
import com.dotop.smartwater.project.module.core.water.vo.DictionaryVo;
import com.dotop.smartwater.project.module.dao.tool.IDictionaryChildDao;
import com.dotop.smartwater.project.module.dao.tool.IDictionaryDao;
import com.dotop.smartwater.project.module.service.tool.IDictionaryService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * 字典配置
 *

 * @date 2019年3月4日
 */
@Service
public class DictionaryServiceImpl implements IDictionaryService {

	private static final Logger LOGGER = LogManager.getLogger(DictionaryServiceImpl.class);

	@Resource
	private IDictionaryDao iDictionaryDao;

	@Resource
	private IDictionaryChildDao iDictionaryChildDao;

	@Override
	public Pagination<DictionaryVo> page(DictionaryBo dictionaryBo) {
		try {
			Page<Object> pageHelper = PageHelper.startPage(dictionaryBo.getPage(), dictionaryBo.getPageCount());
			List<DictionaryVo> list = iDictionaryDao.listByPage(BeanUtils.copy(dictionaryBo, DictionaryDto.class));
			Pagination<DictionaryVo> pagination = new Pagination<>(dictionaryBo.getPageCount(), dictionaryBo.getPage());
			pagination.setData(list);
			pagination.setTotalPageSize(pageHelper.getTotal());
			return pagination;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public DictionaryVo get(DictionaryBo dictionaryBo) {
		try {
			DictionaryDto dictionaryDto = new DictionaryDto();
			dictionaryDto.setDictionaryId(dictionaryBo.getDictionaryId());
			return iDictionaryDao.get(dictionaryDto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public DictionaryVo add(DictionaryBo dictionaryBo) {
		try {
			DictionaryDto dictionaryDto = BeanUtils.copy(dictionaryBo, DictionaryDto.class);

			dictionaryDto.setDictionaryId(getDictionaryUUID(dictionaryBo));
			dictionaryDto.setIsDel(RootModel.NOT_DEL);

			iDictionaryDao.add(dictionaryDto);
			return BeanUtils.copy(dictionaryDto, DictionaryVo.class);

		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public DictionaryVo edit(DictionaryBo dictionaryBo) {
		try {
			DictionaryDto dictionaryDto = BeanUtils.copy(dictionaryBo, DictionaryDto.class);
			iDictionaryDao.edit(dictionaryDto);
			return BeanUtils.copy(dictionaryDto, DictionaryVo.class);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public String del(DictionaryBo dictionaryBo) {
		try {
			DictionaryDto dictionaryDto = BeanUtils.copy(dictionaryBo, DictionaryDto.class);

			if (StringUtils.isNotBlank(dictionaryBo.getDictionaryId())) {
				DictionaryChildDto dictionaryChildDto = new DictionaryChildDto();
				dictionaryChildDto.setDictionaryId(dictionaryBo.getDictionaryId());
				iDictionaryChildDao.deleteByDictionaryId(dictionaryChildDto);
			}
			iDictionaryDao.del(dictionaryDto);

			return ResultCode.Success;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public boolean isExist(DictionaryBo dictionaryBo) {
		try {
			int i = iDictionaryDao.isExistDictionaryCode(BeanUtils.copy(dictionaryBo, DictionaryDto.class));
			return i > 0;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public List<DictionaryVo> getByDictionaryCode(DictionaryBo dictionaryBo) {
		try {
			return iDictionaryDao.getByDictionaryCode(BeanUtils.copy(dictionaryBo, DictionaryDto.class));
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public DictionaryVo getByDictionaryId(String dictionaryId) {
		try {
			DictionaryDto dictionaryDto = new DictionaryDto();
			dictionaryDto.setDictionaryId(dictionaryId);
			DictionaryVo dictionaryVo = iDictionaryDao.get(dictionaryDto);
			if (dictionaryVo != null) {
				DictionaryChildDto dictionaryChildDto = new DictionaryChildDto();
				dictionaryChildDto.setDictionaryId(dictionaryId);
				dictionaryVo.setChildren(iDictionaryChildDao.list(dictionaryChildDto));
			}

			return dictionaryVo;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public List<DictionaryVo> list(DictionaryBo dictionaryBo) {
		try {
			return iDictionaryDao.list(BeanUtils.copy(dictionaryBo, DictionaryDto.class));
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	private static String getDictionaryUUID(DictionaryBo dictionaryBo) {
		if (StringUtils.isBlank(dictionaryBo.getEnterpriseid())
				|| StringUtils.isBlank(dictionaryBo.getDictionaryCode())) {
			throw new FrameworkRuntimeException(ResultCode.Fail, "主键参数不能为空");
		}
		return dictionaryBo.getEnterpriseid() + "," + dictionaryBo.getDictionaryCode();
	}
}
