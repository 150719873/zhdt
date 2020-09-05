package com.dotop.smartwater.project.auth.api.impl;

import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.BeanUtils;
import com.dotop.smartwater.dependence.lock.IDistributedLock;
import com.dotop.smartwater.project.auth.api.IPlatformFactory;
import com.dotop.smartwater.project.auth.service.IPlatformRoleService;
import com.dotop.smartwater.project.module.core.auth.bo.PlatformRoleBo;
import com.dotop.smartwater.project.module.core.auth.form.PlatFormPerForm;
import com.dotop.smartwater.project.module.core.auth.form.PlatformRoleForm;
import com.dotop.smartwater.project.module.core.auth.form.RoleAreaForm;
import com.dotop.smartwater.project.module.core.auth.vo.AreaNodeVo;
import com.dotop.smartwater.project.module.core.auth.vo.PermissionVo;
import com.dotop.smartwater.project.module.core.auth.vo.PlatformRoleVo;
import com.dotop.smartwater.project.module.core.auth.vo.UserVo;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**

 */
@Component
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
public class PlatformFactoryImpl implements IPlatformFactory {

	@Autowired
	private IPlatformRoleService iPlatformRoleService;

	@Autowired
	private IDistributedLock iDistributedLock;

	@Override
	public Pagination<PlatformRoleVo> getPlatformRoleList(PlatformRoleForm platformRoleForm) {
		return iPlatformRoleService.getPlatformRoleList(BeanUtils.copy(platformRoleForm, PlatformRoleBo.class));
	}

	@Override
	public List<PermissionVo> loadPermissionData(PlatformRoleForm platformRoleForm) {
		return iPlatformRoleService.getPermissionByRoleId(platformRoleForm.getProleid());
	}

	@Override
	public List<AreaNodeVo> loadPermissionTree(PlatformRoleForm platformRoleForm) {
		List<AreaNodeVo> permissionVos = iPlatformRoleService.findPermissionByProleid(platformRoleForm.getProleid());
		return checkIsLeaf(listToTree(permissionVos));
	}


	private List<AreaNodeVo> listToTree(List<AreaNodeVo> list) {
		List<AreaNodeVo> results = new ArrayList<>();
		if (CollectionUtils.isEmpty(list)) {
			return results;
		}
		for (AreaNodeVo areaNodeVo : list) {
			if (!areaNodeVo.getIsLeaf()) {
				results.add(areaNodeVo);
			}

			for (AreaNodeVo subAreaNodeVo : list) {
				if (subAreaNodeVo.getPId().equals(areaNodeVo.getKey())) {
					if (CollectionUtils.isEmpty(areaNodeVo.getChildren())) {
						areaNodeVo.setChildren(new ArrayList<>());
					}
					areaNodeVo.getChildren().add(subAreaNodeVo);
				}
			}

		}
		return results;
	}

	private List<AreaNodeVo> checkIsLeaf(List<AreaNodeVo> list){
		if(!CollectionUtils.isEmpty(list)){
			for(AreaNodeVo node : list){
				if(CollectionUtils.isEmpty(node.getChildren())){
					node.setIsLeaf(true);
				}else{
					node.setIsLeaf(false);
					node.setChildren(checkIsLeaf(node.getChildren()));
				}
			}
		}
		return list;
	}

	@Override
	public List<AreaNodeVo> loadRoleAreaTree(RoleAreaForm roleAreaForm) {
		return checkIsLeaf(listToTree(iPlatformRoleService.loadRoleArea(roleAreaForm)));
	}

	@Override
	public void updateRoleArea(RoleAreaForm roleAreaForm, UserVo user) {
		String key = "update_role_area_" + user.getEnterpriseid() + "_" + roleAreaForm.getRoleId();
		boolean flag = iDistributedLock.lock(key, 2);
		try {
			// 编辑权限要加锁,避免冲突
			if (flag) {
				roleAreaForm.setEnterpriseid(user.getEnterpriseid());
				iPlatformRoleService.updateRoleArea(roleAreaForm);
			} else {
				throw new FrameworkRuntimeException(ResultCode.Fail, "Locking");
			}
		} catch (Exception e) {
			throw new FrameworkRuntimeException(ResultCode.Fail, e.getMessage(), e);
		} finally {
			if (flag) {
				iDistributedLock.releaseLock(key);
			}
		}
	}

	@Override
	public void updatePermissionData(PlatFormPerForm platFormPerForm, UserVo user) {
		String key = "update_permission_data_" + user.getEnterpriseid() + "_" + platFormPerForm.getProleid();
		boolean flag = iDistributedLock.lock(key, 2);
		try {
			// 编辑权限要加锁,避免冲突
			if (flag) {
				iPlatformRoleService.updatePermissionData(platFormPerForm);
			} else {
				throw new FrameworkRuntimeException(ResultCode.Fail, "Locking");
			}
		} catch (Exception e) {
			throw new FrameworkRuntimeException(ResultCode.Fail, e.getMessage(), e);
		} finally {
			if (flag) {
				iDistributedLock.releaseLock(key);
			}
		}
	}

	@Override
	public Map<String, AreaNodeVo> loadMenuDataByType(Integer type) {
		return iPlatformRoleService.loadMenuDataByType(String.valueOf(type));
	}

	@Override
	public PlatformRoleVo add(PlatformRoleForm platformRoleForm) {
		PlatformRoleVo tempRole = iPlatformRoleService.findPlatformRoleByName(platformRoleForm.getName());

		if (tempRole != null) {
			throw new FrameworkRuntimeException(ResultCode.RoleExist, "平台角色已经存在");
		}

		return iPlatformRoleService.add(BeanUtils.copy(platformRoleForm, PlatformRoleBo.class));
	}

	@Override
	public PlatformRoleVo edit(PlatformRoleForm platformRoleForm) {
		PlatformRoleBo platformRoleBo = BeanUtils.copy(platformRoleForm, PlatformRoleBo.class);
		PlatformRoleVo r = iPlatformRoleService.findPlatformRoleByNameAndId(platformRoleBo);
		if (r != null) {
			throw new FrameworkRuntimeException(ResultCode.RoleExist, ResultCode.getMessage(ResultCode.RoleExist));
		}

		return iPlatformRoleService.editPlatformRole(platformRoleBo);
	}

	@Override
	public String del(PlatformRoleForm platformRoleForm) {
		PlatformRoleVo r = iPlatformRoleService.findPlatformRoleById(platformRoleForm.getProleid());
		if (r == null) {
			throw new FrameworkRuntimeException(ResultCode.ParamIllegal, "找不到该ID对应的平台角色");
		}

		if (iPlatformRoleService.getCount(platformRoleForm.getProleid()) > 0) {
			throw new FrameworkRuntimeException(ResultCode.CanNotDeleteUserRole,
					ResultCode.getMessage(ResultCode.CanNotDeleteUserRole));
		}

		iPlatformRoleService.delPlatformRole(platformRoleForm.getProleid());

		return "";
	}
}
