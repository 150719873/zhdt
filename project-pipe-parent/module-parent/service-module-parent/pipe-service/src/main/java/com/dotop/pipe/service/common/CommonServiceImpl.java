package com.dotop.pipe.service.common;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.dotop.pipe.api.client.IWaterDictClient;
import com.dotop.pipe.api.dao.common.ICommonDao;
import com.dotop.pipe.api.service.common.ICommonService;
import com.dotop.pipe.core.dto.common.DictionaryDto;
import com.dotop.pipe.core.vo.alarm.AlarmNoticeRuleVo;
import com.dotop.pipe.core.vo.common.DictionaryVo;
import com.dotop.pipe.core.vo.common.NumRuleVo;
import com.dotop.smartwater.dependence.core.common.RootModel;
import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.UuidUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

@Service
public class CommonServiceImpl implements ICommonService {

	private final static Logger logger = LogManager.getLogger(CommonServiceImpl.class);

	@Autowired
	private IWaterDictClient iWaterDictClient;

	@Autowired
	private ICommonDao iCommonDao;

	@Override
	public DictionaryVo getByType(String type) throws FrameworkRuntimeException {
		try {
			return iWaterDictClient.get(type);
			// Integer isDel = RootModel.NOT_DEL;
			// return iCommonDao.getVo(type, isDel);
		} catch (FrameworkRuntimeException e) {
			logger.error(LogMsg.to(e));
			throw e;
		} catch (DataAccessException e) {
			logger.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		} catch (Throwable e) {
			logger.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.BASE_ERROR, e.getMessage(), e);
		}
	}

	@Override
	public DictionaryVo get(String id) throws FrameworkRuntimeException {
		try {
			Integer isDel = RootModel.NOT_DEL;
			return iCommonDao.get(id, isDel);
		} catch (FrameworkRuntimeException e) {
			logger.error(LogMsg.to(e));
			throw e;
		} catch (DataAccessException e) {
			logger.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		} catch (Throwable e) {
			logger.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.BASE_ERROR, e.getMessage(), e);
		}
	}

	@Override
	public Pagination<DictionaryVo> page(String type, Integer page, Integer pageSize) throws FrameworkRuntimeException {
		try {
			Integer isDel = RootModel.NOT_DEL;
			// 参数转换
			DictionaryDto dictionaryDto = new DictionaryDto();
			dictionaryDto.setType(type);
			dictionaryDto.setIsDel(isDel);
			Page<Object> pageHelper = PageHelper.startPage(page, pageSize);
			List<DictionaryVo> list = iCommonDao.list(dictionaryDto);
			Pagination<DictionaryVo> pagination = new Pagination<>(pageSize, page);
			pagination.setData(list);
			pagination.setTotalPageSize(pageHelper.getTotal());
			return pagination;
		} catch (FrameworkRuntimeException e) {
			logger.error(LogMsg.to(e));
			throw e;
		} catch (DataAccessException e) {
			logger.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		} catch (Throwable e) {
			logger.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.BASE_ERROR, e.getMessage(), e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public String del(String id) throws FrameworkRuntimeException {
		try {
			Integer isDel = RootModel.NOT_DEL;
			Integer newIsDel = RootModel.DEL;
			// 参数转换
			DictionaryDto dictionaryDto = new DictionaryDto();
			dictionaryDto.setIsDel(isDel);
			dictionaryDto.setId(id);
			dictionaryDto.setNewIsDel(newIsDel);
			iCommonDao.del(dictionaryDto);
			return null;
		} catch (FrameworkRuntimeException e) {
			logger.error(LogMsg.to(e));
			throw e;
		} catch (DataAccessException e) {
			logger.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		} catch (Throwable e) {
			logger.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.BASE_ERROR, e.getMessage(), e);
		}
	}

	@Override
	public DictionaryVo getByTypeAndName(String type, String name) throws FrameworkRuntimeException {
		try {
			List<DictionaryVo> result = iCommonDao.getByTypeAndName(type, name);
			if (!CollectionUtils.isEmpty(result) && result.size() == 1) {
				return result.get(0);
			} else {
				return null;
			}
		} catch (FrameworkRuntimeException e) {
			logger.error(LogMsg.to(e));
			throw e;
		} catch (DataAccessException e) {
			logger.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		} catch (Throwable e) {
			logger.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.BASE_ERROR, e.getMessage(), e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public DictionaryVo add(String name, String val, String type, String des, String unit)
			throws FrameworkRuntimeException {
		try {
			String dictId = UuidUtils.getUuid();
			Integer isDel = RootModel.NOT_DEL;
			// 参数转换
			DictionaryDto dictionaryDto = new DictionaryDto();
			dictionaryDto.setId(dictId);
			dictionaryDto.setType(type);
			dictionaryDto.setName(name);
			dictionaryDto.setVal(val);
			dictionaryDto.setUnit(unit);
			dictionaryDto.setDes(des);
			dictionaryDto.setIsDel(isDel);
			iCommonDao.add(dictionaryDto);
			DictionaryVo dictionaryVo = new DictionaryVo();
			dictionaryVo.setId(dictId);
			return dictionaryVo;
		} catch (FrameworkRuntimeException e) {
			logger.error(LogMsg.to(e));
			throw e;
		} catch (DataAccessException e) {
			logger.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		} catch (Throwable e) {
			logger.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.BASE_ERROR, e.getMessage(), e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public void edit(String name, String val, String type, String des, String unit, String id)
			throws FrameworkRuntimeException {
		try {
			Integer isDel = RootModel.NOT_DEL;
			// 参数转换
			DictionaryDto dictionaryDto = new DictionaryDto();
			dictionaryDto.setId(id);
			dictionaryDto.setType(type);
			dictionaryDto.setName(name);
			dictionaryDto.setVal(val);
			dictionaryDto.setUnit(unit);
			dictionaryDto.setDes(des);
			dictionaryDto.setIsDel(isDel);
			iCommonDao.edit(dictionaryDto);
		} catch (FrameworkRuntimeException e) {
			logger.error(LogMsg.to(e));
			throw e;
		} catch (DataAccessException e) {
			logger.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		} catch (Throwable e) {
			logger.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.BASE_ERROR, e.getMessage(), e);
		}
	}

	private static String increaseId(String id) {
		if (id != null && id.length() == 6) {
			String start = id.substring(0, 3);
			String numStr = id.substring(3);
			DecimalFormat countFormat = new DecimalFormat("000");
			String num = countFormat.format(Integer.parseInt(numStr) + 1);
			return start + num;
		}
		return "";
	}

	@Override
	public NumRuleVo getMaxCode(String type, String operEid) {
		return iCommonDao.getMaxCode(type, operEid);
	}

	@Override
	public AlarmNoticeRuleVo getALarmNoticeRule(String operEid) {
		return iCommonDao.getALarmNoticeRule(operEid,"");
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public void editNumRule(String id, Integer maxValue) {
		try {
			Integer isDel = RootModel.NOT_DEL;
			iCommonDao.editNumRule(id, maxValue);
		} catch (FrameworkRuntimeException e) {
			logger.error(LogMsg.to(e));
			throw e;
		} catch (DataAccessException e) {
			logger.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		} catch (Throwable e) {
			logger.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.BASE_ERROR, e.getMessage(), e);
		}

	}

	@Override
	public void addNumRule(String category, String title, Integer maxValue, String userBy, Date cuur, String operEid) {
		try {
			Integer isDel = RootModel.NOT_DEL;
			iCommonDao.addNumRule(category, title,maxValue,isDel,userBy,cuur,operEid);
		} catch (FrameworkRuntimeException e) {
			logger.error(LogMsg.to(e));
			throw e;
		} catch (DataAccessException e) {
			logger.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		} catch (Throwable e) {
			logger.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.BASE_ERROR, e.getMessage(), e);
		}
	}

	@Override
	public List<String> getEnterpriseIdList() {
		try {
			Integer isDel = RootModel.NOT_DEL;
			return iCommonDao.getEnterpriseIdList(isDel);
		} catch (FrameworkRuntimeException e) {
			logger.error(LogMsg.to(e));
			throw e;
		} catch (DataAccessException e) {
			logger.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		} catch (Throwable e) {
			logger.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.BASE_ERROR, e.getMessage(), e);
		}
	}
}
