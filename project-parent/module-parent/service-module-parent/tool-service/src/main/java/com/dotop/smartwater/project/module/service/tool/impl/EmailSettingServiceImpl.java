package com.dotop.smartwater.project.module.service.tool.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.UuidUtils;
import com.dotop.smartwater.project.module.core.water.bo.EmailSettingBo;
import com.dotop.smartwater.project.module.core.water.dto.EmailSettingDto;
import com.dotop.smartwater.project.module.core.water.vo.EmailSettingVo;
import com.dotop.smartwater.project.module.dao.tool.IEmailSettingDao;
import com.dotop.smartwater.project.module.service.tool.IEmailSettingService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

@Service
public class EmailSettingServiceImpl implements IEmailSettingService {

	private static final Logger LOGGER = LogManager.getLogger(EmailSettingServiceImpl.class);

	@Resource
	private IEmailSettingDao iEmailSettingDao;

	@Override
	public Pagination<EmailSettingVo> page(EmailSettingBo emailSettingBo) {
		try {
			// 参数转换
			EmailSettingDto emailSettingDto = new EmailSettingDto();
			BeanUtils.copyProperties(emailSettingBo, emailSettingDto);

			// 操作数据
			Page<Object> pageHelper = PageHelper.startPage(emailSettingBo.getPage(), emailSettingBo.getPageCount());
			List<EmailSettingVo> list = iEmailSettingDao.getPage(emailSettingDto);
			// 拼接数据返回
			return new Pagination<>(emailSettingBo.getPage(), emailSettingBo.getPageCount(), list,
					pageHelper.getTotal());
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public int insertOrUpdate(EmailSettingBo emailSettingBo) {
		try {
			// 参数转换
			EmailSettingDto emailSettingDto = new EmailSettingDto();
			BeanUtils.copyProperties(emailSettingBo, emailSettingDto);
			String id = emailSettingDto.getId();
			if (id != null) {
				return iEmailSettingDao.edit(emailSettingDto);
			} else {
				id = UuidUtils.getUuid();
				emailSettingDto.setId(id);
				// 1 启用 0 禁用
				emailSettingDto.setStatus(1);
				// 1 正常 0 删除
				emailSettingDto.setDelflag(1);
				return iEmailSettingDao.add(emailSettingDto);
			}
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

}
