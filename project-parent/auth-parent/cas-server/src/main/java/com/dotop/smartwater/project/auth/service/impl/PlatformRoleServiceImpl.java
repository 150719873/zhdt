package com.dotop.smartwater.project.auth.service.impl;

import java.util.List;
import java.util.Map;

import com.dotop.smartwater.project.auth.cache.AreaNodeMapCacheDao;
import com.dotop.smartwater.project.auth.dao.IPlatformRoleDao;
import com.dotop.smartwater.project.auth.dao.IUserDao;
import com.dotop.smartwater.project.auth.service.IPlatformRoleService;
import com.dotop.smartwater.project.module.core.auth.dto.UserDto;
import com.dotop.smartwater.project.module.core.auth.form.RoleAreaForm;
import com.dotop.smartwater.project.module.core.auth.vo.UserVo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.BeanUtils;
import com.dotop.smartwater.dependence.core.utils.UuidUtils;
import com.dotop.smartwater.project.module.core.auth.bo.PlatformRoleBo;
import com.dotop.smartwater.project.module.core.auth.dto.PlatformRoleDto;
import com.dotop.smartwater.project.module.core.auth.form.PlatFormPerForm;
import com.dotop.smartwater.project.module.core.auth.vo.AreaNodeVo;
import com.dotop.smartwater.project.module.core.auth.vo.PermissionVo;
import com.dotop.smartwater.project.module.core.auth.vo.PlatformRoleVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;

/**

 */
@Component
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
public class PlatformRoleServiceImpl implements IPlatformRoleService {

	private static final Logger LOGGER = LogManager.getLogger(PlatformRoleServiceImpl.class);

	@Resource
	private IPlatformRoleDao iPlatformRoleDao;

	@Resource
	private IUserDao iUserDao;

	@Resource
	private AreaNodeMapCacheDao areaNodeMapCacheDao;

	@Override
	public Pagination<PlatformRoleVo> getPlatformRoleList(PlatformRoleBo platformRoleBo){
		try {
			// 操作数据
			Page<Object> pageHelper = PageHelper.startPage(platformRoleBo.getPage(), platformRoleBo.getPageCount());
			List<PlatformRoleVo> list = iPlatformRoleDao
					.getPlatformRoleList(BeanUtils.copy(platformRoleBo, PlatformRoleDto.class));
			// 拼接数据返回
			return new Pagination<>(platformRoleBo.getPage(), platformRoleBo.getPageCount(), list,
					pageHelper.getTotal());
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public PlatformRoleVo findPlatformRoleByName(String name)  {
		try {
			return iPlatformRoleDao.findPlatformRoleByName(name);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public PlatformRoleVo findPlatformRoleById(String proleid)  {
		try {
			return iPlatformRoleDao.selectById(proleid);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public int getCount(String proleid)  {
		try {
			return iPlatformRoleDao.getCount(proleid);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public void delPlatformRole(String proleid) {
		try {
			iPlatformRoleDao.delPlatformRolePermissionByProleid(proleid);
			iPlatformRoleDao.deleteById(proleid);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public PlatformRoleVo findPlatformRoleByNameAndId(PlatformRoleBo platformRoleBo) {
		try {
			return iPlatformRoleDao
					.findPlatformRoleByNameAndProleid(BeanUtils.copy(platformRoleBo, PlatformRoleDto.class));
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public PlatformRoleVo editPlatformRole(PlatformRoleBo platformRoleBo)  {
		try {
			iPlatformRoleDao.updateById(BeanUtils.copy(platformRoleBo, PlatformRoleDto.class));
			return BeanUtils.copy(platformRoleBo, PlatformRoleVo.class);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public List<PermissionVo> getPermissionByRoleId(String roleid) {
		try {
			return iPlatformRoleDao.getPermissionByRoleId(roleid);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public List<AreaNodeVo> findPermissionByProleid(String proleid) {
		try {
			return iPlatformRoleDao.findPermissionByProleid(proleid);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public void updatePermissionData(PlatFormPerForm platFormPerForm)  {
		try {
			iPlatformRoleDao.delPlatformRolePermissionByProleid(platFormPerForm.getProleid());
			if(!CollectionUtils.isEmpty(platFormPerForm.getPermissionids())){
				iPlatformRoleDao.addPlatformRolePermission(platFormPerForm.getProleid(),
						platFormPerForm.getPermissionids());
			}
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public List<AreaNodeVo> loadRoleArea(RoleAreaForm roleAreaForm) {
		try {
			return iPlatformRoleDao.loadRoleArea(roleAreaForm);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public void updateRoleArea(RoleAreaForm roleAreaForm) {
		try {
			iPlatformRoleDao.deleteRoleArea(roleAreaForm.getRoleId());

			//先清空用户区域缓存
			UserDto user = new UserDto();
			user.setRoleid(roleAreaForm.getRoleId());
			user.setEnterpriseid(roleAreaForm.getEnterpriseid());
			List<UserVo> userVos = iUserDao.getUserList(user);
			if(userVos != null && userVos.size() > 0){
				for(UserVo vo : userVos){
					areaNodeMapCacheDao.delAreaNodeMap(vo.getUserid());
				}
			}
			if(!CollectionUtils.isEmpty(roleAreaForm.getAreaIds())){
				iPlatformRoleDao.addRoleArea(roleAreaForm.getRoleId(),roleAreaForm.getAreaIds());
			}
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public Map<String, AreaNodeVo> loadMenuDataByType(String type)  {
		try {
			return iPlatformRoleDao.loadMenuDataByType(type);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public PlatformRoleVo add(PlatformRoleBo platformRoleBo) {
		try {
			PlatformRoleDto platformRoleDto = BeanUtils.copy(platformRoleBo, PlatformRoleDto.class);
			platformRoleDto.setProleid(UuidUtils.getUuid());
			iPlatformRoleDao.insert(platformRoleDto);
			return BeanUtils.copy(platformRoleDto, PlatformRoleVo.class);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}
}
