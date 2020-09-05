package com.dotop.smartwater.project.auth.rest.service;

import com.dotop.smartwater.dependence.core.common.BaseController;
import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.VerificationUtils;
import com.dotop.smartwater.project.auth.api.IPlatformFactory;
import com.dotop.smartwater.project.auth.cache.CBaseDao;
import com.dotop.smartwater.project.auth.common.FoundationController;
import com.dotop.smartwater.project.module.core.auth.constants.AuthResultCode;
import com.dotop.smartwater.project.module.core.auth.form.PlatFormPerForm;
import com.dotop.smartwater.project.module.core.auth.form.PlatformRoleForm;
import com.dotop.smartwater.project.module.core.auth.form.RoleAreaForm;
import com.dotop.smartwater.project.module.core.auth.vo.AreaNodeVo;
import com.dotop.smartwater.project.module.core.auth.vo.PlatformRoleVo;
import com.dotop.smartwater.project.module.core.auth.vo.UserVo;
import com.dotop.smartwater.project.module.core.water.enums.OperateTypeEnum;
import com.dotop.smartwater.project.module.core.water.form.MenuForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 

 * @date 2019年5月9日
 * @description
 */
@RestController

@RequestMapping("/auth/platform")
public class PlatFormController extends FoundationController implements BaseController<PlatformRoleForm> {

	@Autowired
	private CBaseDao baseDao;

	@Autowired
	private IPlatformFactory iPlatformFactory;

	@PostMapping(value = "/get_platform_role_list", produces = GlobalContext.PRODUCES)
	public String getPlatformRoleList(@RequestHeader("userid") String userid, @RequestHeader("ticket") String ticket,
			@RequestBody PlatformRoleForm platformRoleForm) {
		UserVo user = baseDao.getRedisUser(userid);
		if (!baseDao.webAuth(userid, ticket) || user == null) {
			return resp(AuthResultCode.TimeOut, "登录超时,请重新登录", null);
		}
		Pagination<PlatformRoleVo> pagination = iPlatformFactory.getPlatformRoleList(platformRoleForm);
		return resp(AuthResultCode.Success, "SUCCESS", pagination);
	}

	@PostMapping(value = "/add_platform_role", produces = GlobalContext.PRODUCES)
	public String addPlatformRole(@RequestHeader("userid") String userid, @RequestHeader("ticket") String ticket,
			@RequestBody PlatformRoleForm platformRoleForm) {
		UserVo user = baseDao.getRedisUser(userid);
		if (!baseDao.webAuth(userid, ticket) || user == null) {
			return resp(AuthResultCode.TimeOut, "登录超时,请重新登录", null);
		}

		VerificationUtils.string("name", platformRoleForm.getName());

		platformRoleForm.setCreatetime(new Date());
		platformRoleForm.setCreateuser(userid);
		iPlatformFactory.add(platformRoleForm);
		auditLog(OperateTypeEnum.PLATFORM_ROLE,"新增","名称",platformRoleForm.getName());
		return resp(AuthResultCode.Success, "SUCCESS", null);
	}

	@PostMapping(value = "/del_platform_role", produces = GlobalContext.PRODUCES)
	public String delPlatformRole(@RequestHeader("userid") String userid, @RequestHeader("ticket") String ticket,
			@RequestBody PlatformRoleForm platformRoleForm) {
		UserVo user = baseDao.getRedisUser(userid);
		if (!baseDao.webAuth(userid, ticket) || user == null) {
			return resp(AuthResultCode.TimeOut, "登录超时,请重新登录", null);
		}

		VerificationUtils.string("proleId", platformRoleForm.getProleid());

		iPlatformFactory.del(platformRoleForm);
		auditLog(OperateTypeEnum.PLATFORM_ROLE,"删除","角色ID",platformRoleForm.getProleid());
		return resp(AuthResultCode.Success, "SUCCESS", null);
	}

	@PostMapping(value = "/edit_platform_role", produces = GlobalContext.PRODUCES)
	public String editPlatformRole(@RequestHeader("userid") String userid, @RequestHeader("ticket") String ticket,
			@RequestBody PlatformRoleForm platformRoleForm) {
		UserVo user = baseDao.getRedisUser(userid);
		if (!baseDao.webAuth(userid, ticket) || user == null) {
			return resp(AuthResultCode.TimeOut, "登录超时,请重新登录", null);
		}

		VerificationUtils.string("proleId", platformRoleForm.getProleid());

		iPlatformFactory.edit(platformRoleForm);
		auditLog(OperateTypeEnum.PLATFORM_ROLE,"编辑","角色ID",platformRoleForm.getProleid());
		return resp(AuthResultCode.Success, "SUCCESS", null);
	}

	@PostMapping(value = "/load_permission_data", produces = GlobalContext.PRODUCES)
	public String loadPermissionData(@RequestHeader("userid") String userid, @RequestHeader("ticket") String ticket,
			@RequestBody PlatformRoleForm platformRoleForm) {
		UserVo user = baseDao.getRedisUser(userid);
		if (!baseDao.webAuth(userid, ticket) || user == null) {
			return resp(AuthResultCode.TimeOut, "登录超时,请重新登录", null);
		}

		VerificationUtils.string("proleId", platformRoleForm.getProleid());

		return resp(AuthResultCode.Success, "SUCCESS", iPlatformFactory.loadPermissionData(platformRoleForm));
	}

	@PostMapping(value = "/load_permission_tree", produces = GlobalContext.PRODUCES)
	public String loadPermissionTree(@RequestHeader("userid") String userid, @RequestHeader("ticket") String ticket,
	                                 @RequestBody PlatformRoleForm platformRoleForm) {
		UserVo user = baseDao.getRedisUser(userid);
		if (!baseDao.webAuth(userid, ticket) || user == null) {
			return resp(AuthResultCode.TimeOut, "登录超时,请重新登录", null);
		}

		VerificationUtils.string("proleId", platformRoleForm.getProleid());

		return resp(AuthResultCode.Success, "SUCCESS", iPlatformFactory.loadPermissionTree(platformRoleForm));
	}

	@PostMapping(value = "/update_permission_data", produces = GlobalContext.PRODUCES)
	public String updatePermissionData(@RequestHeader("userid") String userid, @RequestHeader("ticket") String ticket,
			@RequestBody PlatFormPerForm platFormPerForm) {
		UserVo user = baseDao.getRedisUser(userid);
		if (!baseDao.webAuth(userid, ticket) || user == null) {
			return resp(AuthResultCode.TimeOut, "登录超时,请重新登录", null);
		}

		VerificationUtils.string("proleId", platFormPerForm.getProleid());

		iPlatformFactory.updatePermissionData(platFormPerForm, user);
		auditLog(OperateTypeEnum.PLATFORM_ROLE,"编辑权限","角色ID",platFormPerForm.getProleid());
		return resp(AuthResultCode.Success, "SUCCESS", null);
	}

	@PostMapping(value = "/loadRoleArea", produces = GlobalContext.PRODUCES)
	public String loadRoleArea(@RequestHeader("userid") String userid, @RequestHeader("ticket") String ticket,
	                                 @RequestBody RoleAreaForm roleAreaForm) {
		UserVo user = baseDao.getRedisUser(userid);
		if (!baseDao.webAuth(userid, ticket) || user == null) {
			return resp(AuthResultCode.TimeOut, "登录超时,请重新登录", null);
		}

		VerificationUtils.string("roleId", roleAreaForm.getRoleId());
		if(UserVo.USER_TYPE_ADMIN != user.getType()){
			roleAreaForm.setEnterpriseid(user.getEnterpriseid());
		}
		return resp(AuthResultCode.Success, "SUCCESS", iPlatformFactory.loadRoleAreaTree(roleAreaForm));
	}

	@PostMapping(value = "/updateRoleArea", produces = GlobalContext.PRODUCES)
	public String updateRoleArea(@RequestHeader("userid") String userid, @RequestHeader("ticket") String ticket,
	                                   @RequestBody RoleAreaForm roleAreaForm) {
		UserVo user = baseDao.getRedisUser(userid);
		if (!baseDao.webAuth(userid, ticket) || user == null) {
			return resp(AuthResultCode.TimeOut, "登录超时,请重新登录", null);
		}

		VerificationUtils.string("roleId", roleAreaForm.getRoleId());

		iPlatformFactory.updateRoleArea(roleAreaForm, user);
		auditLog(OperateTypeEnum.PLATFORM_ROLE,"编辑区域权限","角色ID",roleAreaForm.getRoleId());
		return resp(AuthResultCode.Success, "SUCCESS", null);
	}


	@PostMapping(value = "/loadMenuDataByType", produces = GlobalContext.PRODUCES)
	public String loadMenuDataByType(@RequestHeader("userid") String userid, @RequestHeader("ticket") String ticket,
			@RequestBody MenuForm menuForm) {
		UserVo user = baseDao.getRedisUser(userid);
		if (!baseDao.webAuth(userid, ticket) || user == null) {
			return resp(AuthResultCode.TimeOut, "登录超时,请重新登录", null);
		}

		VerificationUtils.integer("type", menuForm.getType());

		Map<String, AreaNodeVo> map = iPlatformFactory.loadMenuDataByType(menuForm.getType());
		List<AreaNodeVo> list = InterfaceController.mapToListTree(map);
		List<AreaNodeVo> newList = new ArrayList<>();
		for (AreaNodeVo areaNodeVo : list) {
			if ("sys".equals(areaNodeVo.getSystype())) {
				newList.add(areaNodeVo);
			}
		}
		for (AreaNodeVo areaNodeVo : list) {
			if ("tool".equals(areaNodeVo.getSystype())) {
				newList.add(areaNodeVo);
			}
		}
		return resp(AuthResultCode.Success, "SUCCESS", newList);
	}
}
