package com.dotop.smartwater.project.auth.rest.service;

import com.dotop.smartwater.dependence.core.common.BaseController;
import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.VerificationUtils;
import com.dotop.smartwater.dependence.lock.IDistributedLock;
import com.dotop.smartwater.project.auth.api.IAccountFactory;
import com.dotop.smartwater.project.auth.api.IRoleFactory;
import com.dotop.smartwater.project.auth.cache.CBaseDao;
import com.dotop.smartwater.project.auth.common.FoundationController;
import com.dotop.smartwater.project.module.core.auth.constants.AuthResultCode;
import com.dotop.smartwater.project.module.core.auth.form.RoleForm;
import com.dotop.smartwater.project.module.core.auth.form.UserForm;
import com.dotop.smartwater.project.module.core.auth.vo.*;
import com.dotop.smartwater.project.module.core.auth.vo.select.Obj;
import com.dotop.smartwater.project.module.core.auth.vo.select.UserAndRoleObj;
import com.dotop.smartwater.project.module.core.auth.vo.select.UserAndRoleSelect;
import com.dotop.smartwater.project.module.core.water.constants.WaterConstants;
import com.dotop.smartwater.project.module.core.water.enums.OperateTypeEnum;
import com.dotop.smartwater.project.module.core.water.form.MenuForm;
import com.dotop.smartwater.project.module.core.water.utils.MenuUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 

 * @date 2019年5月9日
 * @description
 */
@RestController

@RequestMapping("/auth/RoleController")
public class RoleController extends FoundationController implements BaseController<RoleForm> {

	private static final Logger LOGGER = LogManager.getLogger(RoleController.class);

	@Autowired
	private CBaseDao baseDao;

	@Resource
	private IRoleFactory iRoleFactory;

	@Resource
	private IAccountFactory iUserFactory;

	@Autowired
	private IDistributedLock iDistributedLock;

	@PostMapping(value = "/add_role", produces = GlobalContext.PRODUCES)
	public String addRole(@RequestHeader("userid") String userid, @RequestHeader("ticket") String ticket,
			@RequestBody RoleForm role) {

		try {
			UserVo user = baseDao.getRedisUser(userid);
			if (!baseDao.webAuth(userid, ticket) || user == null) {
				return resp(AuthResultCode.TimeOut, "登录超时,请重新登录", null);
			}
			if (StringUtils.isBlank(role.getName()) || StringUtils.isBlank(role.getDescription())) {
				return resp(AuthResultCode.ParamIllegal, "必填参数不能为空", null);
			}

			role.setEnterpriseid(user.getEnterpriseid());

			RoleVo tempRole = iRoleFactory.findRoleByName(role);

			if (tempRole != null) {
				return resp(AuthResultCode.RoleExist, "账户类型已经存在", null);
			}

			role.setCreateuser(user.getUserid());

			auditLog(OperateTypeEnum.ROLE_MANAGEMENT,"新增","角色名称",role.getName());
			if (iRoleFactory.addRole(role) > 0) {
				return resp(AuthResultCode.Success, AuthResultCode.SUCCESS, null);
			}

			return resp(AuthResultCode.Fail, "add_role fail", null);
		} catch (Exception e) {
			LOGGER.error("add_role", e);
			return resp(AuthResultCode.Fail, e.getMessage(), null);
		}
	}

	@PostMapping(value = "/edit_role", produces = GlobalContext.PRODUCES)
	public String editRole(@RequestHeader("userid") String userid, @RequestHeader("ticket") String ticket,
			@RequestBody RoleForm role) {
		try {
			UserVo user = baseDao.getRedisUser(userid);
			if (!baseDao.webAuth(userid, ticket) || user == null) {
				return resp(AuthResultCode.TimeOut, "登录超时,请重新登录", null);
			}
			if (StringUtils.isBlank(role.getName()) || StringUtils.isBlank(role.getDescription())) {
				return resp(AuthResultCode.ParamIllegal, "必填参数不能为空", null);
			}

			role.setEnterpriseid(user.getEnterpriseid());

			RoleVo tempRole = iRoleFactory.findRoleByNameAndId(role);

			if (tempRole != null) {
				return resp(AuthResultCode.RoleExist, "账户类型已经存在", null);
			}

			// TODO role.setCreatetime(new Date());
			role.setCreateuser(user.getUserid());

			auditLog(OperateTypeEnum.ROLE_MANAGEMENT,"编辑","角色名称",role.getName());
			if (iRoleFactory.editRole(role) > 0) {
				return resp(AuthResultCode.Success, AuthResultCode.SUCCESS, null);
			}

			return resp(AuthResultCode.Fail, "edit_role error", null);
		} catch (Exception e) {
			LOGGER.error("edit_role", e);
			return resp(AuthResultCode.Fail, e.getMessage(), null);
		}
	}

	@PostMapping(value = "/del_role", produces = GlobalContext.PRODUCES)
	public String delRole(@RequestHeader("userid") String userid, @RequestHeader("ticket") String ticket,
			@RequestBody RoleForm role) {
		try {
			UserVo user = baseDao.getRedisUser(userid);
			if (!baseDao.webAuth(userid, ticket) || user == null) {
				return resp(AuthResultCode.TimeOut, "登录超时,请重新登录", null);
			}
			if (StringUtils.isBlank(role.getRoleid())) {
				return resp(AuthResultCode.ParamIllegal, "必填参数不能为空", null);
			}

			if (iRoleFactory.getCount(role.getRoleid()) > 0) {
				return resp(AuthResultCode.CanNotDeleteUserRole, "角色已经被使用,不能删除", null);
			}

			RoleVo r = iRoleFactory.findRoleById(role.getRoleid());

			auditLog(OperateTypeEnum.ROLE_MANAGEMENT,"删除","角色名称",r.getName());
			if (iRoleFactory.delRole(role.getRoleid()) > 0) {
				return resp(AuthResultCode.Success, AuthResultCode.SUCCESS, null);
			}

			return resp(AuthResultCode.Fail, "unknow error", null);
		} catch (Exception e) {
			LOGGER.error("del_role", e);
			return resp(AuthResultCode.Fail, e.getMessage(), null);
		}
	}

	@PostMapping(value = "/find_role", produces = GlobalContext.PRODUCES)
	public String findRole(@RequestHeader("userid") String userid, @RequestHeader("ticket") String ticket,
			@RequestBody RoleVo role) {
		try {
			UserVo user = baseDao.getRedisUser(userid);
			if (!baseDao.webAuth(userid, ticket) || user == null) {
				return resp(AuthResultCode.TimeOut, "登录超时,请重新登录", null);
			}
			if (StringUtils.isBlank(role.getRoleid())) {
				return resp(AuthResultCode.ParamIllegal, "必填参数不能为空", null);
			}

			return resp(AuthResultCode.Success, AuthResultCode.SUCCESS, iRoleFactory.findRoleById(role.getRoleid()));
		} catch (Exception e) {
			LOGGER.error("find_role", e);
			return resp(AuthResultCode.Fail, e.getMessage(), null);
		}
	}

	@PostMapping(value = "/get_role", produces = GlobalContext.PRODUCES)
	public String getRole(@RequestHeader("userid") String userid, @RequestHeader("ticket") String ticket,
			@RequestBody RoleForm role) {
		if (!baseDao.webAuth(userid, ticket)) {
			return resp(AuthResultCode.AccountInvalid, "用户已失效,请重新登录", null);
		}

		try {
			UserVo user = baseDao.getRedisUser(userid);
			if (user == null) {
				return resp(AuthResultCode.UserNotLogin, "用户没登录", null);
			}
			Pagination<RoleVo> pagination = new Pagination<>();
			if (user.getType().equals(0)) {
				RoleVo roleVo = new RoleVo();
				roleVo.setRoleid("-1");
				roleVo.setName("水司最高级管理员");
				List<RoleVo> list = new ArrayList<>();
				list.add(roleVo);
				pagination.setPageNo(1L);
				pagination.setData(list);
			} else {
				role.setEnterpriseid(user.getEnterpriseid());
				pagination = iRoleFactory.getRoleList(role);
			}
			return resp(AuthResultCode.Success, "SUCCESS", pagination);
		} catch (Exception e) {
			LOGGER.error("get_role", e);
			return resp(AuthResultCode.Fail, e.getMessage(), null);
		}
	}

	@PostMapping(value = "/get_menu", produces = GlobalContext.PRODUCES)
	public String getMenu(@RequestHeader("userid") String userid, @RequestHeader("ticket") String ticket) {
		try {
			UserVo user = baseDao.getRedisUser(userid);
			if (!baseDao.webAuth(userid, ticket) || user == null) {
				return resp(AuthResultCode.TimeOut, "登录超时,请重新登录", null);
			}

			// 系统管理员
			List<MenuVo> list = new ArrayList<>();
			if (user.getType() == 0) {
				list = iRoleFactory.getAdminMenuByParentid("0");
			}

			// 水司管理员
			if (user.getType() == 1) {
				// 只获取一级
				String proleId = null;
				if (user.getEnterprise() != null) {
					if (user.getEnterprise().getProleid() != null) {
						proleId = user.getEnterprise().getProleid();
					}
				}
				if (proleId == null) {
					return resp(AuthResultCode.ParamIllegal, "该水司没有开通服务权限", null);
				}
				list = MenuUtils.filterChild(getMenus(iRoleFactory.getMenuByParentidAndProleid(null, proleId)));
			}

			// 普通系统用户,跟权限挂钩
			if (user.getType() == 2) {
				list = MenuUtils.filterChild(getMenus(iRoleFactory.getMenuByRoleid(user.getRoleid(), null)));
			}

			return resp(AuthResultCode.Success, AuthResultCode.SUCCESS, list);
		} catch (Exception e) {
			LOGGER.error("get_menu", e);
			return resp(AuthResultCode.Fail, e.getMessage(), null);
		}
	}


	@PostMapping(value = "/get_menu_child", produces = GlobalContext.PRODUCES)
	public String getMenuChild(@RequestHeader("userid") String userid, @RequestHeader("ticket") String ticket,
			@RequestBody MenuForm params) {
		UserVo user = baseDao.getRedisUser(userid);
		if (!baseDao.webAuth(userid, ticket) || user == null) {
			return resp(AuthResultCode.TimeOut, "登录超时,请重新登录", null);
		}
		try {
			if (StringUtils.isBlank(params.getParentid())) {
				return resp(AuthResultCode.ParamIllegal, "parentid 为空", null);
			}

			List<MenuVo> list = new ArrayList<>();

			// 最高管理员
			if (UserVo.USER_TYPE_ADMIN == user.getType()) {
				// 二级
				list = iRoleFactory.getAdminMenuByParentid(params.getParentid());
				for (MenuVo menu : list) {
					// 三级
					menu.setChild(iRoleFactory.getAdminMenuByParentid(menu.getMenuid()));
				}
			}

			// 水司管理员
			if (UserVo.USER_TYPE_ADMIN_ENTERPRISE == user.getType()) {
				String proleId = null;
				if (user.getEnterprise() != null) {
					if (user.getEnterprise().getProleid() != null) {
						proleId = user.getEnterprise().getProleid();
					}
				}
				if (proleId == null) {
					return resp(AuthResultCode.ParamIllegal, "该水司没有开通服务权限", null);
				}
				// 二级
				list = iRoleFactory.getMenuByParentidAndProleid(params.getParentid(), proleId);
				for (MenuVo menu : list) {
					// 三级
					menu.setChild(iRoleFactory.getMenuByParentidAndProleid(menu.getMenuid(), proleId));
				}
			}

			// 普通系统用户,跟权限挂钩
			if (UserVo.USER_TYPE_ENTERPRISE_NORMAL == user.getType()) {
				// 二级
				list = iRoleFactory.getMenuByRoleid(user.getRoleid(), params.getParentid());
				for (MenuVo menu : list) {
					// 三级
					menu.setChild(iRoleFactory.getMenuByRoleid(user.getRoleid(), menu.getMenuid()));
				}
			}

			return resp(AuthResultCode.Success, AuthResultCode.SUCCESS, list);
		} catch (Exception e) {
			LOGGER.error("get_menu_child", e);
			return resp(AuthResultCode.Fail, e.getMessage(), null);
		}
	}

	private List<MenuVo> getMenus(List<MenuVo> menus){
		if(CollectionUtils.isEmpty(menus)){
			return Collections.emptyList();
		}
		List<String> mids = new ArrayList<>();
		for(MenuVo m:menus){
			mids.add(m.getMenuid());
		}

		List<String> ids = new ArrayList<>();
		for(MenuVo m:menus){
			if(!"0".equals(m.getParentid()) && !mids.contains(m.getParentid())){
				ids.add(m.getParentid());
			}
		}

		if(!CollectionUtils.isEmpty(ids)){
			List<MenuVo> menuVoList =  iRoleFactory.getMenus(ids);
			menuVoList.addAll(menus);
			return getMenus(menuVoList);
		}else{
			return menus;
		}
	}

	@PostMapping(value = "/get_permissions", produces = GlobalContext.PRODUCES)
	public String getPermissions(@RequestHeader("userid") String userid, @RequestHeader("ticket") String ticket,
			@RequestBody RoleVo role) {
		UserVo user = baseDao.getRedisUser(userid);
		if (!baseDao.webAuth(userid, ticket) || user == null) {
			return resp(AuthResultCode.TimeOut, "登录超时,请重新登录", null);
		}

		try {
			if (role.getRoleid() == null) {
				return resp(AuthResultCode.ParamIllegal, "roleid 为空", null);
			}

			String proleId = null;
			if (user.getEnterprise() != null) {
				if (user.getEnterprise().getProleid() != null) {
					proleId = user.getEnterprise().getProleid();
				}
			}

			if (proleId == null) {
				return resp(AuthResultCode.ParamIllegal, "该水司没有开通服务权限", null);
			}

			List<PermissionVo> rolePermission = iRoleFactory.getPermissionByRoleidAndProleid(role.getRoleid(), proleId);

			return resp(AuthResultCode.Success, AuthResultCode.SUCCESS, rolePermission);
		} catch (Exception e) {
			LOGGER.error("get_permission", e);
			return resp(AuthResultCode.Fail, e.getMessage(), null);
		}
	}

	@PostMapping(value = "/load_permission_tree", produces = GlobalContext.PRODUCES)
	public String loadPermissionTree(@RequestHeader("userid") String userid, @RequestHeader("ticket") String ticket,
	                                 @RequestBody RoleParamVo roleParam) {
		UserVo user = baseDao.getRedisUser(userid);
		if (!baseDao.webAuth(userid, ticket) || user == null) {
			return resp(AuthResultCode.TimeOut, "登录超时,请重新登录", null);
		}

		VerificationUtils.string("roleid", roleParam.getRoleid());

		String proleId = null;
		if (user.getEnterprise() != null) {
			if (user.getEnterprise().getProleid() != null) {
				proleId = user.getEnterprise().getProleid();
			}
		}

		if (proleId == null) {
			return resp(AuthResultCode.ParamIllegal, "该水司没有开通服务权限", null);
		}


		return resp(AuthResultCode.Success, "SUCCESS", iRoleFactory.loadPermissionTree(roleParam, proleId));
	}

	@PostMapping(value = "/edit_permission", produces = GlobalContext.PRODUCES)
	public String editPermission(@RequestHeader("userid") String userid, @RequestHeader("ticket") String ticket,
			@RequestBody RoleParamVo roleParam) {
		UserVo user = baseDao.getRedisUser(userid);
		if (!baseDao.webAuth(userid, ticket) || user == null) {
			return resp(AuthResultCode.TimeOut, "登录超时,请重新登录", null);
		}
		String roleid = roleParam.getRoleid();
		if (StringUtils.isBlank(roleid)) {
			return resp(AuthResultCode.ParamIllegal, "roleid 为空", null);
		}
		List<String> permissionids = roleParam.getPermissionids();
		String key = "edit_permission_" + user.getEnterpriseid() + "_" + roleid;
		boolean flag = iDistributedLock.lock(key, 2);
		try {
			// 编辑权限要加锁,避免冲突
			if (flag) {
				RoleVo role = iRoleFactory.findRoleById(roleid);
				if (role == null) {
					return resp(AuthResultCode.ParamIllegal, "该roleid的账号类型不存在 ", null);
				}

				auditLog(OperateTypeEnum.ROLE_MANAGEMENT,"分配权限","角色名称",role.getName());
				iRoleFactory.addRolePermission(roleid, permissionids);
				return resp(AuthResultCode.Success, AuthResultCode.SUCCESS, null);
			} else {
				return resp(AuthResultCode.Fail, "Locking", null);
			}
		} catch (Exception e) {
			LOGGER.error("edit_permission", e);
			return resp(AuthResultCode.Fail, e.getMessage(), null);
		} finally {
			if (flag) {
				iDistributedLock.releaseLock(key);
			}
		}
	}

	@PostMapping(value = "/userSelect", produces = GlobalContext.PRODUCES)
	public String userSelect(@RequestHeader("userid") String userid, @RequestHeader("ticket") String ticket) {
		UserVo user = baseDao.getRedisUser(userid);
		if (!baseDao.webAuth(userid, ticket) || user == null) {
			return resp(AuthResultCode.TimeOut, "登录超时,请重新登录", null);
		}

		RoleForm roleForm = new RoleForm();
		roleForm.setEnterpriseid(user.getEnterpriseid());
		List<RoleVo> roles = iRoleFactory.list(roleForm);

		UserForm userForm = new UserForm();
		userForm.setEnterpriseid(user.getEnterpriseid());
		List<UserVo> users = iUserFactory.list(userForm);

		UserAndRoleSelect select = new UserAndRoleSelect();
		UserAndRoleObj userObj = new UserAndRoleObj();
		userObj.setType(WaterConstants.SELECT_TYPE_USER);
		userObj.setName("用户列表");

		UserAndRoleObj roleObj = new UserAndRoleObj();
		roleObj.setType(WaterConstants.SELECT_TYPE_ROLE);
		roleObj.setName("角色列表");

		List<Obj> r = new ArrayList<>();
		Obj o = new Obj();
		o.setId(WaterConstants.SELECT_TYPE_DEFAULT_ROLE_ID);
		o.setName(WaterConstants.SELECT_TYPE_DEFAULT_ROLE_NAME);
		r.add(o);
		if (roles != null) {
			for (RoleVo ro : roles) {
				Obj obj = new Obj();
				obj.setId(ro.getRoleid());
				obj.setName(ro.getName());
				r.add(obj);
			}
		}

		roleObj.setList(r);

		if (users != null) {
			List<Obj> u = new ArrayList<>(users.size());
			for (UserVo uo : users) {
				Obj obj = new Obj();
				obj.setId(uo.getUserid());
				obj.setName(uo.getName());
				u.add(obj);
			}
			userObj.setList(u);
		}

		select.setRoles(roleObj);
		select.setUsers(userObj);
		return resp(AuthResultCode.Success, AuthResultCode.SUCCESS, select);

	}
}
