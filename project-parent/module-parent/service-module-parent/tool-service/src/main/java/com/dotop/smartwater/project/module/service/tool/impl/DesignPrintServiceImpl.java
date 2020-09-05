package com.dotop.smartwater.project.module.service.tool.impl;

import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.BeanUtils;
import com.dotop.smartwater.dependence.core.utils.UuidUtils;
import com.dotop.smartwater.project.module.core.water.bo.DesignPrintBo;
import com.dotop.smartwater.project.module.core.water.bo.PrintBindBo;
import com.dotop.smartwater.project.module.core.water.dto.DesignPrintDto;
import com.dotop.smartwater.project.module.core.water.dto.PrintBindDto;
import com.dotop.smartwater.project.module.core.water.vo.DesignPrintVo;
import com.dotop.smartwater.project.module.core.water.vo.PrintBindVo;
import com.dotop.smartwater.project.module.core.water.vo.customize.DesignFieldVo;
import com.dotop.smartwater.project.module.dao.tool.IDesignPrintDao;
import com.dotop.smartwater.project.module.service.tool.IDesignPrintService;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 字典配置
 *

 * @date 2019年3月4日
 */
@Service
public class DesignPrintServiceImpl implements IDesignPrintService {

	private static final Logger LOGGER = LogManager.getLogger(DesignPrintServiceImpl.class);

	@Resource
	private IDesignPrintDao iDesignPrintDao;

	@Override
	public List<PrintBindVo> templateList(PrintBindBo printBindBo) {
		try {
			// 参数转换
			PrintBindDto printBindDto = BeanUtils.copy(printBindBo, PrintBindDto.class);
			return iDesignPrintDao.templateList(printBindDto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	public Pagination<DesignPrintVo> getDesignPrintList(DesignPrintBo designPrintBo) {
		try {
			// 参数转换
			DesignPrintDto designPrintDto = BeanUtils.copyProperties(designPrintBo, DesignPrintDto.class);
			Page<Object> pageHelper = PageHelper.startPage(designPrintBo.getPage(), designPrintBo.getPageCount());
			List<DesignPrintVo> list = iDesignPrintDao.getDesignPrintList(designPrintDto);
			Pagination<DesignPrintVo> pagination = new Pagination<>(designPrintBo.getPageCount(),
					designPrintBo.getPage());
			pagination.setData(list);
			pagination.setTotalPageSize(pageHelper.getTotal());
			return pagination;

		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public DesignPrintVo get(String id) {
		try {
			return iDesignPrintDao.get(id);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public int deleteDesignPrint(String id) {
		try {
			return iDesignPrintDao.deleteDesignPrint(id);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public List<DesignFieldVo> getFields(String id) {
		try {
			return iDesignPrintDao.getFields(id);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public int deleteDesignFields(String id) {
		try {
			return iDesignPrintDao.deleteDesignFields(id);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public Boolean updateDesignPrint(DesignPrintBo designPrint) {
		try {
			DesignPrintDto designPrintDto = BeanUtils.copy(designPrint, DesignPrintDto.class);
			// 先更新 design_print 表
			iDesignPrintDao.updateDesignPrint(designPrintDto);
			// 删掉design_field 字段
			iDesignPrintDao.deleteDesignFields(designPrint.getId());
			// 插入design_field 字段
			List<DesignFieldVo> list = new ArrayList<>();
			if (!StringUtils.isBlank(designPrint.getFields())) {
				String[] fields = designPrint.getFields().split(",");
				for (String field : fields) {
					DesignFieldVo df = new DesignFieldVo();
					df.setId(UuidUtils.getUuid());
					df.setDesignid(designPrint.getId());
					df.setField(field);
					list.add(df);
				}
			}
			iDesignPrintDao.saveField(list);
			return true;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public Boolean saveDesignPrint(DesignPrintBo designPrint) {
		try {
			// 处理保存 和字段保存
			String id = UuidUtils.getUuid();
			DesignPrintDto designPrintDto = BeanUtils.copy(designPrint, DesignPrintDto.class);
			designPrintDto.setId(id);
			// 先保存design_print 表
			iDesignPrintDao.saveDesignPrint(designPrintDto);
			// 再保存 design_field 表
			List<DesignFieldVo> list = new ArrayList<>();
			if (!StringUtils.isBlank(designPrint.getFields())) {
				String[] fields = designPrint.getFields().split(",");
				for (String field : fields) {
					DesignFieldVo df = new DesignFieldVo();
					df.setId(UuidUtils.getUuid());
					df.setDesignid(id);
					df.setField(field);
					list.add(df);
				}
			}
			iDesignPrintDao.saveField(list);
			return true;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public Map<String, String> getView(String sql) {
		try {
			return iDesignPrintDao.getView(sql);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public List<Map<String, String>> getSqlView(String sql) {
		try {
			return iDesignPrintDao.getSqlView(sql);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public void updateTemplate(DesignPrintBo designPrintbo) {
		try {
			DesignPrintDto designPrintDto = BeanUtils.copy(designPrintbo, DesignPrintDto.class);
			iDesignPrintDao.updateTemplate(designPrintDto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}

	}

	@Override
	public void addTemplate(DesignPrintBo designPrintbo) {
		try {
			DesignPrintDto designPrintDto = BeanUtils.copy(designPrintbo, DesignPrintDto.class);
			designPrintDto.setId(UuidUtils.getUuid());
			iDesignPrintDao.addTemplate(designPrintDto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public DesignPrintVo getPrintStatus(String enterpriseid, Integer type) {
		try {
			return iDesignPrintDao.getPrintStatus(enterpriseid, type);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}
}
