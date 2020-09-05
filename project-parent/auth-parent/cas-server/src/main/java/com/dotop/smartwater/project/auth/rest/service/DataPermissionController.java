package com.dotop.smartwater.project.auth.rest.service;

import com.dotop.smartwater.dependence.core.common.BaseController;
import com.dotop.smartwater.dependence.core.global.GlobalContext;
import com.dotop.smartwater.dependence.core.utils.VerificationUtils;
import com.dotop.smartwater.project.auth.cache.CBaseDao;
import com.dotop.smartwater.project.auth.common.FoundationController;
import com.dotop.smartwater.project.auth.api.IDataPermissionFactory;
import com.dotop.smartwater.project.module.core.auth.constants.AuthResultCode;
import com.dotop.smartwater.project.module.core.auth.form.DataPermissionPerForm;
import com.dotop.smartwater.project.module.core.auth.form.DataTypeForm;
import com.dotop.smartwater.project.module.core.auth.vo.UserVo;
import com.dotop.smartwater.project.module.core.water.enums.OperateTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**

 * @date 2019/8/7.
 */
@RestController

@RequestMapping("/auth/dataPermission")
public class DataPermissionController extends FoundationController implements BaseController<DataTypeForm> {

	@Autowired
	private CBaseDao baseDao;

	@Autowired
	private IDataPermissionFactory iDataPermissionFactory;


	@PostMapping(value = "/loadDataPermission", produces = GlobalContext.PRODUCES)
	public String loadPermissionData(@RequestHeader("userid") String userid, @RequestHeader("ticket") String ticket,
	                                 @RequestBody DataTypeForm dataTypeForm) {
		UserVo user = baseDao.getRedisUser(userid);
		if (!baseDao.webAuth(userid, ticket) || user == null) {
			return resp(AuthResultCode.TimeOut, "登录超时,请重新登录", null);
		}

		VerificationUtils.string("id", dataTypeForm.getId());

		return resp(AuthResultCode.Success, "SUCCESS", iDataPermissionFactory.loadPermissionById(dataTypeForm));
	}

	@PostMapping(value = "/updateDataPermission", produces = GlobalContext.PRODUCES)
	public String updatePermissionData(@RequestHeader("userid") String userid, @RequestHeader("ticket") String ticket,
	                                   @RequestBody DataPermissionPerForm dataPermissionPerForm) {
		UserVo user = baseDao.getRedisUser(userid);
		if (!baseDao.webAuth(userid, ticket) || user == null) {
			return resp(AuthResultCode.TimeOut, "登录超时,请重新登录", null);
		}

		VerificationUtils.string("id", dataPermissionPerForm.getId());

		iDataPermissionFactory.updateDataPermission(dataPermissionPerForm, user);
		auditLog(OperateTypeEnum.PLATFORM_ROLE,"编辑业务权限","角色ID",dataPermissionPerForm.getId());
		return resp(AuthResultCode.Success, "SUCCESS", null);
	}
}
