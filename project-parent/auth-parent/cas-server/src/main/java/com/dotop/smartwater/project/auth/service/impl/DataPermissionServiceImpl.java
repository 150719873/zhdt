package com.dotop.smartwater.project.auth.service.impl;

import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.project.auth.dao.IDataPermissionDao;
import com.dotop.smartwater.project.auth.service.IDataPermissionService;
import com.dotop.smartwater.project.module.core.auth.form.DataPermissionPerForm;
import com.dotop.smartwater.project.module.core.auth.vo.CheckboxOptionVo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**

 * @date 2019/8/7.
 */
@Service
public class DataPermissionServiceImpl implements IDataPermissionService {

	private static final Logger LOGGER = LogManager.getLogger(DataPermissionServiceImpl.class);

	@Autowired
	private IDataPermissionDao iDataPermissionDao;

	@Override
	public List<CheckboxOptionVo> loadPermissionById(String id) {
		try {
			return iDataPermissionDao.loadPermissionByRoleId(id);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public void updateDataPermission(DataPermissionPerForm dataPermissionPerForm) {
		try {
			iDataPermissionDao.deletePermissionByRoleId(dataPermissionPerForm.getId());
			if(!CollectionUtils.isEmpty(dataPermissionPerForm.getPermissionids())){
				iDataPermissionDao.addPermission(dataPermissionPerForm.getId(),dataPermissionPerForm.getPermissionids());
			}
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}
}
