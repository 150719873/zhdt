package com.dotop.smartwater.project.auth.service.impl;

import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.UuidUtils;
import com.dotop.smartwater.project.auth.cache.EnterpriseRoleCacheDao;
import com.dotop.smartwater.project.auth.cache.PlatformRoleCacheDao;
import com.dotop.smartwater.project.auth.dao.IPlatformRoleDao;
import com.dotop.smartwater.project.auth.dao.IRoleDao;
import com.dotop.smartwater.project.auth.service.IRoleService;
import com.dotop.smartwater.project.module.core.auth.bo.RoleBo;
import com.dotop.smartwater.project.module.core.auth.bo.UserBo;
import com.dotop.smartwater.project.module.core.auth.dto.RoleDto;
import com.dotop.smartwater.project.module.core.auth.dto.UserDto;
import com.dotop.smartwater.project.module.core.auth.vo.*;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;

@Service
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
public class RoleServiceImpl implements IRoleService {

	private static final Logger LOGGER = LogManager.getLogger(RoleServiceImpl.class);

	@Resource
	private IRoleDao iRoleDao;

	@Resource
	private IPlatformRoleDao iPlatformRoleDao;

	@Resource
	private EnterpriseRoleCacheDao enterpriseRoleCacheDao;

	@Resource
	private PlatformRoleCacheDao platformRoleCacheDao;

	@Override
	public int addRole(RoleBo role) throws FrameworkRuntimeException {
		try {
			// 参数转换
			RoleDto roleDto = new RoleDto();
			BeanUtils.copyProperties(role, roleDto);
			roleDto.setRoleid(UuidUtils.getUuid());
			roleDto.setCreatetime(new Date());
			return iRoleDao.addRole(roleDto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public int editRole(RoleBo role) throws FrameworkRuntimeException {
		try {
			// 参数转换
			RoleDto roleDto = new RoleDto();
			BeanUtils.copyProperties(role, roleDto);
			return iRoleDao.editRole(roleDto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public int delRole(String roleid) throws FrameworkRuntimeException {
		try {
			return iRoleDao.delRole(roleid);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public int getCount(String roleid) throws FrameworkRuntimeException {
		try {
			return iRoleDao.getCount(roleid);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public RoleVo findRoleById(String roleid) throws FrameworkRuntimeException {
		try {
			return iRoleDao.findRoleById(roleid);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public Pagination<RoleVo> getRoleList(RoleBo role) throws FrameworkRuntimeException {
		try {
			// 参数转换
			RoleBo roleBo = new RoleBo();
			BeanUtils.copyProperties(role, roleBo);

			RoleDto roleDto = new RoleDto();
			BeanUtils.copyProperties(role, roleDto);

			// 操作数据
			Page<Object> pageHelper = PageHelper.startPage(roleBo.getPage(), roleBo.getPageCount());
			List<RoleVo> list = iRoleDao.getRoleList(roleDto);
			// 拼接数据返回
			Pagination<RoleVo> pagination = new Pagination<>(roleBo.getPage(), roleBo.getPageCount(), list,
					pageHelper.getTotal());
			return pagination;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public RoleVo findRoleByName(RoleBo role) throws FrameworkRuntimeException {
		try {
			// 参数转换
			RoleDto roleDto = new RoleDto();
			BeanUtils.copyProperties(role, roleDto);
			return iRoleDao.findRoleByName(roleDto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public RoleVo findRoleByNameAndId(RoleBo role) throws FrameworkRuntimeException {
		try {
			// 参数转换
			RoleDto roleDto = new RoleDto();
			BeanUtils.copyProperties(role, roleDto);
			return iRoleDao.findRoleByNameAndId(roleDto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public List<MenuVo> getMenuByParentidAndProleid(String parentid, String proleid) throws FrameworkRuntimeException {
		try {
			return iRoleDao.getMenuByParentid(parentid, proleid);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public List<MenuVo> getMenuByModelidAndProleid(String modelid, String proleid) throws FrameworkRuntimeException {
		try {
			return iRoleDao.getMenuByModelidAndProleid(modelid, proleid);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public List<MenuVo> getAdminMenuByParentid(String parentid) throws FrameworkRuntimeException {
		try {
			return iRoleDao.getAdminMenuByParentid(parentid);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public List<MenuVo> getAdminTreeByModelid(String modelid) throws FrameworkRuntimeException {
		try {
			return iRoleDao.getAdminTreeByModelid(modelid);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public List<MenuVo> getMenuByRoleid(String roleid, String parentid) throws FrameworkRuntimeException {
		try {
			return iRoleDao.getMenuByRoleid(roleid, parentid);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public List<MenuVo> getMenuByRoleidAndModelid(String roleid, String modelid) throws FrameworkRuntimeException {
		try {
			return iRoleDao.getMenuByRoleidAndModelid(roleid, modelid);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}


	@Override
	public List<MenuVo> getMenus(List<String> ids) {
		return iRoleDao.getMenus(ids);
	}


	@Override
	public void updateRolePermission(UserBo user, String proleid) throws FrameworkRuntimeException {
		try {
			// 参数转换
			UserDto userDto = new UserDto();
			BeanUtils.copyProperties(user, userDto);

			Set<String> menuIds = null;
			RolePermissionVo rolePermission = new RolePermissionVo();
			switch (user.getType()) {
			case UserVo.USER_TYPE_ADMIN:
				break;
			case UserVo.USER_TYPE_ADMIN_ENTERPRISE:
				// 获取水司拥有的平台权限
				menuIds = iPlatformRoleDao.findMenuidsByProleId(proleid);
				if (menuIds != null && menuIds.size() > 0) {
					rolePermission.setRoleid(proleid);
					rolePermission.setPermissions(menuIds);
					platformRoleCacheDao.setPlatformRole(rolePermission);
				}
				break;
			case UserVo.USER_TYPE_ENTERPRISE_NORMAL:
				// 获取用户角色拥有的权限
				menuIds = iRoleDao.getPsidByRoleId(user.getRoleid());
				if (menuIds != null && menuIds.size() > 0) {
					rolePermission.setRoleid(user.getRoleid());
					rolePermission.setPermissions(menuIds);
					enterpriseRoleCacheDao.setEnterpriseRole(rolePermission);
				}
				break;
			}
			user.setPermissions(menuIds);

		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public int addRolePermission(String roleid, List<String> ids) throws FrameworkRuntimeException {
		// TODO Auto-generated method stub
		try {
			int result = 0;
			result += iRoleDao.delRolePermission(roleid);
			if(!CollectionUtils.isEmpty(ids)){
				result += iRoleDao.addRolePermission(roleid, ids);
			}
			return result;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public List<PermissionVo> getPermissionByRoleidAndProleid(String roleid, String proleid)
			throws FrameworkRuntimeException {
		// TODO Auto-generated method stub
		try {
			return iRoleDao.getPermissionByRoleidAndProleid(roleid, proleid);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public List<RoleVo> list(RoleBo role) throws FrameworkRuntimeException {
		try {
			// 参数转换
			RoleDto roleDto = new RoleDto();
			BeanUtils.copyProperties(role, roleDto);

			List<RoleVo> list = iRoleDao.list(roleDto);

			return list;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public List<AreaNodeVo> findPermissionByRoleid(String roleid, String proleId) {
		try {
			return iRoleDao.findPermissionByRoleid(roleid, proleId);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}
}
