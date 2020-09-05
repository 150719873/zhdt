package com.dotop.smartwater.project.module.api.wechat.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.dotop.smartwater.project.module.api.wechat.IWechatInstallFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.dotop.smartwater.dependence.cache.StringValueCache;
import com.dotop.smartwater.project.module.core.water.bo.WechatUserBo;
import com.dotop.smartwater.project.module.core.water.constants.CacheKey;
import com.dotop.smartwater.project.module.core.water.constants.WechatConstants;
import com.dotop.smartwater.project.module.core.water.form.WechatUserForm;
import com.dotop.smartwater.project.module.core.water.vo.WechatUserVo;
import com.dotop.smartwater.project.module.service.wechat.IWechatInstallService;

@Component
public class WechatInstallFactoryImpl implements IWechatInstallFactory {

	@Autowired
	private IWechatInstallService service;

	@Autowired
	protected StringValueCache svc;

	@Override
	public boolean update(WechatUserForm form) {
		WechatUserBo bo = new WechatUserBo();
		BeanUtils.copyProperties(form, bo);
		bo.setCurr(new Date());
		bo.setSource(WechatConstants.WECHAT_INSTALL_USER);
		return service.update(bo);
	}

	public Map<String, String> login(WechatUserForm form) {
		Map<String, String> map = new HashMap<>();
		WechatUserBo bo = new WechatUserBo();
		BeanUtils.copyProperties(form, bo);

		// 获取用户信息
		WechatUserVo vo = service.get(bo);
		if (vo == null) {
			vo = service.save(bo);
		}

		String token = UUID.randomUUID().toString();
		svc.set(CacheKey.WATERWECHATINSTALLUSERID + token, JSON.toJSONString(vo));
		map.put("token", token);
		map.put("userid", vo.getId());
		map.put("name", vo.getName());
		map.put("headUrl", vo.getHeadUrl());
		map.put("phone", vo.getPhone());
		map.put("cardid", vo.getCardid());
		map.put("enterpriseid", vo.getEnterpriseid());
		return map;
	}

	public WechatUserVo save(WechatUserForm form) {
		WechatUserBo bo = new WechatUserBo();
		BeanUtils.copyProperties(form, bo);
		bo.setCurr(new Date());
		return service.save(bo);
	}

}
