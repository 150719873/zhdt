package com.dotop.pipe.auth.api.service.enterprise;

import java.util.Date;

import com.dotop.pipe.auth.core.vo.enterprise.EnterpriseVo;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;

// 企业
public interface IEnterpriseService {

	// 通过认证中心企业eid获取企业信息
	public EnterpriseVo get(String eid) throws FrameworkRuntimeException;

	// 绑定认证中心企业eid与管道系统eid关联
	public String add(String eid, String enterpriseName, Date curr, String userBy) throws FrameworkRuntimeException;

	// 通过认证中心企业eid获取企业信息
	public EnterpriseVo getDb(String eid) throws FrameworkRuntimeException;

}
