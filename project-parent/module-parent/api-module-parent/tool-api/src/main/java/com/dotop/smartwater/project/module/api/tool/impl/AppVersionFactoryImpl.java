package com.dotop.smartwater.project.module.api.tool.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.JSONUtils;
import com.dotop.smartwater.dependence.core.utils.UuidUtils;
import com.dotop.smartwater.project.module.api.tool.IAppVersionFactory;
import com.dotop.smartwater.project.module.core.auth.local.AuthCasClient;
import com.dotop.smartwater.project.module.core.auth.vo.UserVo;
import com.dotop.smartwater.project.module.core.water.bo.AppVersionBo;
import com.dotop.smartwater.project.module.core.water.form.AppVersionForm;
import com.dotop.smartwater.project.module.core.water.vo.AppVersionVo;
import com.dotop.smartwater.project.module.service.tool.IAppVersionService;

/**
 * App版本控制
 * 

 * @date 2019年3月5日 15:45
 *
 */
@Component
public class AppVersionFactoryImpl implements IAppVersionFactory {

	@Autowired
	private IAppVersionService iAppVersionService;

	@Override
	public Pagination<AppVersionVo> page(AppVersionForm appVersionForm) {
		AuthCasClient.getUser();
		AppVersionBo appVersionBo = new AppVersionBo();
		BeanUtils.copyProperties(appVersionForm, appVersionBo);
		return iAppVersionService.page(appVersionBo);
	}

	@Override
	public List<AppVersionVo> getVersions(AppVersionForm appVersionForm) {
		UserVo user = AuthCasClient.getUser();
		String operEid = user.getEnterpriseid();

		AppVersionBo appVersionBo = new AppVersionBo();
		BeanUtils.copyProperties(appVersionForm, appVersionBo);
		appVersionBo.setEnterpriseid(operEid);

		return iAppVersionService.getVersions(appVersionBo);
	}

	@Override
	public String addApp(AppVersionForm appVersionForm) {
		UserVo user = AuthCasClient.getUser();
		Date curr = new Date();
		String operEid = user.getEnterpriseid();

		AppVersionBo appVersionBo = new AppVersionBo();
		BeanUtils.copyProperties(appVersionForm, appVersionBo);
		String access = JSONUtils.toJSONString(appVersionForm.getAccessMap());
		appVersionBo.setId(UuidUtils.getUuid());
		appVersionBo.setCreateUserId(user.getUserid());
		appVersionBo.setCreateTime(curr);
		appVersionBo.setAccess(access);
		appVersionBo.setEnterpriseid(operEid);
		if (appVersionBo.getStatus().equals(AppVersionVo.APPVERSION_PUBLISH)) {
			appVersionBo.setPublishUserId(user.getUserid());
			appVersionBo.setPublishTime(curr);
		}

		return iAppVersionService.addApp(appVersionBo);
	}

	@Override
	public Integer revise(AppVersionForm appVersionForm) {
		UserVo user = AuthCasClient.getUser();

		AppVersionBo appVersionBo = new AppVersionBo();
		BeanUtils.copyProperties(appVersionForm, appVersionBo);
		if(appVersionForm.getAccessMap() != null) {
			String access = JSONUtils.toJSONString(appVersionForm.getAccessMap());
			appVersionBo.setAccess(access);
		}
		if(appVersionForm.getSign()) {
			appVersionBo.setPublishTime(new Date());
			appVersionBo.setPublishUserId(user.getUserid());
		}

		return iAppVersionService.revise(appVersionBo);
	}

	@Override
	public String del(AppVersionForm appVersionForm) {
		UserVo user = AuthCasClient.getUser();
		String operEid = user.getEnterpriseid();

		AppVersionBo appVersionBo = new AppVersionBo();
		BeanUtils.copyProperties(appVersionForm, appVersionBo);
		appVersionBo.setEnterpriseid(operEid);

		iAppVersionService.del(appVersionBo);
		return appVersionForm.getId();
	}

	@Override
	public List<AppVersionVo> getAppName() {
		// 用户校验
		AuthCasClient.getUser();
		return iAppVersionService.getAppName();
	}

	@Override
	public AppVersionVo getLatest(AppVersionForm appVersionForm) {
		AppVersionBo appVersionBo = new AppVersionBo();
		BeanUtils.copyProperties(appVersionForm, appVersionBo);
		List<AppVersionVo> list = iAppVersionService.getVersions(appVersionBo);
		AppVersionVo appVersionVo = null;
		if (!list.isEmpty()) {
			appVersionVo = list.get(0);
			for (AppVersionVo item : list) {
				if (item.getVersionCode() > appVersionVo.getVersionCode()) {
					appVersionVo = item;
				}
			}
			return appVersionVo;
		} else {
			return null;
		}
	}

	@Override
	public AppVersionVo getApp(AppVersionForm appVersionForm) {
		// TODO Auto-generated method stub
		AuthCasClient.getUser();
		AppVersionBo appVersionBo = new AppVersionBo();
		BeanUtils.copyProperties(appVersionForm, appVersionBo);
		return iAppVersionService.getApp(appVersionBo);
	}

}
