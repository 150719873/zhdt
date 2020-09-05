package com.dotop.smartwater.project.auth.api.impl;

import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.BeanUtils;
import com.dotop.smartwater.dependence.lock.IDistributedLock;
import com.dotop.smartwater.project.auth.api.IRoleFactory;
import com.dotop.smartwater.project.auth.service.IRoleService;
import com.dotop.smartwater.project.module.core.auth.bo.RoleBo;
import com.dotop.smartwater.project.module.core.auth.bo.UserBo;
import com.dotop.smartwater.project.module.core.auth.form.RoleForm;
import com.dotop.smartwater.project.module.core.auth.form.UserForm;
import com.dotop.smartwater.project.module.core.auth.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Component
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
public class RoleFactoryImpl implements IRoleFactory {

	@Resource
	private IRoleService iRoleService;

	@Autowired
	private IDistributedLock iDistributedLock;

	@Override
	public void updateRolePermission(UserForm userLogin, String proleid) {

		iRoleService.updateRolePermission(BeanUtils.copy(userLogin, UserBo.class), proleid);
	}

	@Override
	public RoleVo findRoleByName(RoleForm role) {
		return iRoleService.findRoleByName(BeanUtils.copy(role, RoleBo.class));
	}

	@Override
	public Integer addRole(RoleForm role) {
		return iRoleService.addRole(BeanUtils.copy(role, RoleBo.class));
	}

	@Override
	public Integer editRole(RoleForm role) {
		return iRoleService.editRole(BeanUtils.copy(role, RoleBo.class));
	}

	@Override
	public RoleVo findRoleByNameAndId(RoleForm role) {
		return iRoleService.findRoleByNameAndId(BeanUtils.copy(role, RoleBo.class));
	}

	@Override
	public Integer getCount(String roleid) {
		return iRoleService.getCount(roleid);
	}

	@Override
	public RoleVo findRoleById(String roleid) {
		return iRoleService.findRoleById(roleid);
	}

	@Override
	public Integer delRole(String roleid) {
		return iRoleService.delRole(roleid);
	}

	@Override
	public Pagination<RoleVo> getRoleList(RoleForm role) {
		return iRoleService.getRoleList(BeanUtils.copy(role, RoleBo.class));
	}

	@Override
	public List<MenuVo> getAdminMenuByParentid(String parentid) {
		return iRoleService.getAdminMenuByParentid(parentid);
	}

	@Override
	public List<MenuVo> getMenuByParentidAndProleid(String parentid, String proleid) {
		return iRoleService.getMenuByParentidAndProleid(parentid, proleid);
	}

	@Override
	public List<MenuVo> getMenuByRoleid(String roleid, String parentid) {
		return iRoleService.getMenuByRoleid(roleid, parentid);
	}

	@Override
	public List<PermissionVo> getPermissionByRoleidAndProleid(String roleid, String proleid) {
		return iRoleService.getPermissionByRoleidAndProleid(roleid, proleid);
	}

	@Override
	public List<MenuVo> getMenus(List<String> ids) {
		return iRoleService.getMenus(ids);
	}

	@Override
	public Integer addRolePermission(String roleid, List<String> ids) {
		return iRoleService.addRolePermission(roleid, ids);
	}

	@Override
	public List<RoleVo> list(RoleForm roleForm) {
		return iRoleService.list(BeanUtils.copy(roleForm, RoleBo.class));
	}

	@Override
	public List<AreaNodeVo> loadPermissionTree(RoleParamVo roleParam, String proleId) {
		List<AreaNodeVo> list = iRoleService.findPermissionByRoleid(roleParam.getRoleid(), proleId);
		return checkIsLeaf(listToTree(list));
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

	private List<AreaNodeVo> checkIsLeaf(List<AreaNodeVo> list) {
		if (!CollectionUtils.isEmpty(list)) {
			for (AreaNodeVo node : list) {
				if (CollectionUtils.isEmpty(node.getChildren())) {
					node.setIsLeaf(true);
				} else {
					node.setIsLeaf(false);
					node.setChildren(checkIsLeaf(node.getChildren()));
				}
			}
		}
		return list;
	}

}
