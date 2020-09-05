package com.dotop.smartwater.project.auth.api.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dotop.smartwater.project.auth.api.IAccountFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.UuidUtils;
import com.dotop.smartwater.project.auth.service.IAccountService;
import com.dotop.smartwater.project.auth.util.GenerateDoMainUtil;
import com.dotop.smartwater.project.module.core.auth.bo.EnterpriseBo;
import com.dotop.smartwater.project.module.core.auth.bo.UserAreaBo;
import com.dotop.smartwater.project.module.core.auth.bo.UserBo;
import com.dotop.smartwater.project.module.core.auth.form.EnterpriseForm;
import com.dotop.smartwater.project.module.core.auth.form.UserAreaForm;
import com.dotop.smartwater.project.module.core.auth.form.UserForm;
import com.dotop.smartwater.project.module.core.auth.vo.EnterpriseVo;
import com.dotop.smartwater.project.module.core.auth.vo.UserVo;

@Component
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
public class AccountFactoryImpl implements IAccountFactory {

	@Autowired
	private IAccountService iAccountService;
	
	// 阿里DNS解析
	@Value("${aliyundns.accessKeyId}")
    private String ACCESSKEYID;
	@Value("${aliyundns.accessSecret}")
    private String ACCESSSECRET;
	@Value("${aliyundns.regionid}")
    private String REGIONID;
	@Value("${aliyundns.type}")
    private String TYPE;
	@Value("${aliyundns.lang}")
    private String LANG;
	@Value("${aliyundns.defaultIp}")
    private String DEFAULTIP;
	@Value("${aliyundns.defaultDoMain}")
    private String DEFAULTDOMAIN;
	
	private static final String SUCCESS = "success";
	
	private static final String ERROR = "error";
	

	@Override
	public UserVo login(String eid, String account, String pwd) throws FrameworkRuntimeException {
		UserForm user = new UserForm();
		user.setEnterpriseid(eid);
		user.setAccount(account);
		user.setPassword(pwd);

		// 业务逻辑
		UserBo userBo = new UserBo();
		BeanUtils.copyProperties(user, userBo);

		return iAccountService.login(userBo);
	}

	@Override
	public EnterpriseVo findEnterpriseById(UserForm user) throws FrameworkRuntimeException {
		// 业务逻辑
		UserBo userBo = new UserBo();
		BeanUtils.copyProperties(user, userBo);

		return iAccountService.findEnterpriseById(userBo.getEnterpriseid());
	}

	@Override
	public EnterpriseVo findEnterpriseByWebsite(String website) throws FrameworkRuntimeException {
		return iAccountService.findEnterpriseByWebsite(website);
	}

	@Override
	public UserVo findUserByAccount(UserForm user) throws FrameworkRuntimeException {
		// 业务逻辑
		UserBo userBo = new UserBo();
		BeanUtils.copyProperties(user, userBo);
		return iAccountService.findUserByAccount(userBo);
	}

	@Override
	public UserVo findUserByWorknum(UserForm user) throws FrameworkRuntimeException {
		// 业务逻辑
		UserBo userBo = new UserBo();
		BeanUtils.copyProperties(user, userBo);
		return iAccountService.findUserByWorknum(userBo);
	}

	@Override
	public Integer addUser(UserForm user) throws FrameworkRuntimeException {
		// 业务逻辑
		UserBo userBo = new UserBo();
		BeanUtils.copyProperties(user, userBo);
		return iAccountService.addUser(userBo);
	}

	@Override
	public UserVo findUserById(String userid) throws FrameworkRuntimeException {
		return iAccountService.findUserById(userid);
	}

	@Override
	public UserVo findUserByAccountAndId(UserForm user) throws FrameworkRuntimeException {
		// 业务逻辑
		UserBo userBo = new UserBo();
		BeanUtils.copyProperties(user, userBo);
		return iAccountService.findUserByAccountAndId(userBo);
	}

	@Override
	public UserVo findUserByWorknumAndId(UserForm user) throws FrameworkRuntimeException {
		// 业务逻辑
		UserBo userBo = new UserBo();
		BeanUtils.copyProperties(user, userBo);
		return iAccountService.findUserByWorknumAndId(userBo);
	}

	@Override
	public Integer editUser(UserForm user) throws FrameworkRuntimeException {
		// 业务逻辑
		UserBo userBo = new UserBo();
		BeanUtils.copyProperties(user, userBo);
		return iAccountService.editUser(userBo);
	}
	
	@Override
	public Integer editUserState(UserForm user) throws FrameworkRuntimeException {
		// 业务逻辑
		UserBo userBo = new UserBo();
		BeanUtils.copyProperties(user, userBo);
		return iAccountService.editUserState(userBo);
	}

	@Override
	public Pagination<UserVo> getUserList(UserForm user) throws FrameworkRuntimeException {
		// 业务逻辑
		UserBo userBo = new UserBo();
		BeanUtils.copyProperties(user, userBo);

		return iAccountService.getUserList(userBo);
	}

	@Override
	public EnterpriseVo findEnterpriseByName(EnterpriseForm enterprise) throws FrameworkRuntimeException {
		// 业务逻辑
		EnterpriseBo enterpriseBo = new EnterpriseBo();
		BeanUtils.copyProperties(enterprise, enterpriseBo);

		return iAccountService.findEnterpriseByName(enterpriseBo);
	}

	@Override
	public Integer addEnterprise(EnterpriseForm enterprise) throws FrameworkRuntimeException {
		// 业务逻辑
		enterprise.setEnterpriseid(UuidUtils.getUuid());
		EnterpriseBo enterpriseBo = new EnterpriseBo();
		BeanUtils.copyProperties(enterprise, enterpriseBo);

		return iAccountService.addEnterprise(enterpriseBo);
	}
	
	@Override
	public void openDoMain(EnterpriseForm enterprise) throws FrameworkRuntimeException {
		try {
			Map<String,String> params = new HashMap<String , String>();
			params.put("accessKeyId", ACCESSKEYID);
			params.put("accessSecret", ACCESSSECRET);
			params.put("regionid", REGIONID);
			params.put("type", TYPE);
			params.put("lang", LANG);
			params.put("websitePrefix", enterprise.getWebsitePrefix());
			params.put("websiteSuffix", DEFAULTDOMAIN);
			params.put("defaultIp", DEFAULTIP);
			GenerateDoMainUtil.generate(params);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	@Override
	public EnterpriseVo findEnterpriseByNameAndId(EnterpriseForm enterprise) throws FrameworkRuntimeException {
		// 业务逻辑
		EnterpriseBo enterpriseBo = new EnterpriseBo();
		BeanUtils.copyProperties(enterprise, enterpriseBo);

		return iAccountService.findEnterpriseByNameAndId(enterpriseBo);
	}

	@Override
	public Integer editEnterprise(EnterpriseForm enterprise) throws FrameworkRuntimeException {
		// 业务逻辑
		EnterpriseBo enterpriseBo = new EnterpriseBo();
		BeanUtils.copyProperties(enterprise, enterpriseBo);

		return iAccountService.editEnterprise(enterpriseBo);
	}

	@Override
	public EnterpriseVo findEnterpriseById(String enterpriseid) throws FrameworkRuntimeException {
		return iAccountService.findEnterpriseById(enterpriseid);
	}

	@Override
	public Integer delEnterprise(String enterpriseid) throws FrameworkRuntimeException {
		return iAccountService.delEnterprise(enterpriseid);
	}
	
	public void deleteDoMain(EnterpriseForm enterprise) throws FrameworkRuntimeException {
		try {
			Map<String,String> params = new HashMap<String , String>();
			
			params.put("accessKeyId", ACCESSKEYID);
			params.put("accessSecret", ACCESSSECRET);
			params.put("regionid", REGIONID);
			params.put("type", TYPE);
			params.put("lang", LANG);
			params.put("websitePrefix", enterprise.getWebsitePrefix());
			params.put("websiteSuffix", DEFAULTDOMAIN);
			params.put("defaultIp", DEFAULTIP);
			params.put("webSite", enterprise.getWebsite());
			// 根据域名查询详情
			Map<String, String> result = GenerateDoMainUtil.getDoMain(params);
			if (result != null && result.get("msg") != null && result.get("msg").equals(SUCCESS)) {
				params.put("recordId", result.get("recordId"));
				GenerateDoMainUtil.deleteDoMain(params);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void editDoMain(EnterpriseVo oldEn , EnterpriseForm newEn) throws FrameworkRuntimeException {
		try {
			Map<String,String> params = new HashMap<String , String>();
			params.put("accessKeyId", ACCESSKEYID);
			params.put("accessSecret", ACCESSSECRET);
			params.put("regionid", REGIONID);
			params.put("type", TYPE);
			params.put("lang", LANG);
			params.put("websitePrefix", oldEn.getWebsitePrefix());
			params.put("websiteSuffix", DEFAULTDOMAIN);
			params.put("defaultIp", DEFAULTIP);
			params.put("webSite", oldEn.getWebsite());
			// 根据域名查询详情
			Map<String, String> result = GenerateDoMainUtil.getDoMain(params);
			if (result != null && result.get("msg") != null && result.get("msg").equals(SUCCESS)) {
				params.put("recordId", result.get("recordId"));
				// 删除原域名
				GenerateDoMainUtil.deleteDoMain(params);
				
				//新增新域名
				params.put("accessKeyId", ACCESSKEYID);
				params.put("accessSecret", ACCESSSECRET);
				params.put("regionid", REGIONID);
				params.put("type", TYPE);
				params.put("lang", LANG);
				params.put("websitePrefix", newEn.getWebsitePrefix());
				params.put("websiteSuffix", DEFAULTDOMAIN);
				params.put("defaultIp", DEFAULTIP);
				GenerateDoMainUtil.generate(params);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	@Override
	public Pagination<EnterpriseVo> getEnterpriseList(EnterpriseForm enterprise) throws FrameworkRuntimeException {
		// 业务逻辑
		EnterpriseBo enterpriseBo = new EnterpriseBo();
		BeanUtils.copyProperties(enterprise, enterpriseBo);

		return iAccountService.getEnterpriseList(enterpriseBo);
	}

	@Override
	public List<UserVo> getUsers(String userIds) throws FrameworkRuntimeException {
		return iAccountService.getUsers(userIds);
	}

	@Override
	public Integer addUserArea(UserAreaForm userArea) throws FrameworkRuntimeException {
		// 业务逻辑
		UserAreaBo userAreaBo = new UserAreaBo();
		BeanUtils.copyProperties(userArea, userAreaBo);

		return iAccountService.addUserArea(userAreaBo);
	}

	@Override
	public List<UserVo> list(UserForm user) throws FrameworkRuntimeException {
		// 业务逻辑
		UserBo userBo = new UserBo();
		BeanUtils.copyProperties(user, userBo);
		return iAccountService.list(userBo);
	}

}
