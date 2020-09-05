package com.dotop.pipe.auth.web.factory.enterprise;

import com.dotop.pipe.auth.core.vo.auth.LoginCas;
import com.dotop.pipe.auth.core.vo.enterprise.EnterpriseVo;
import com.dotop.pipe.auth.api.service.enterprise.IEnterpriseService;
import com.dotop.pipe.auth.web.api.factory.area.IAreaFactory;
import com.dotop.pipe.auth.web.api.factory.enterprise.IEnterpriseFactory;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.lock.IDistributedLock;
import com.dotop.smartwater.dependence.lock.LockKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Component
public class EnterpriseFactoryImpl implements IEnterpriseFactory {

	@Autowired
	private IDistributedLock iDistributedLock;

	@Autowired
	private IEnterpriseService iEnterpriseService;

	// 这里设计问题，逻辑上是auth-web应该在pipe-web之上
	@Autowired
	private IAreaFactory iAreaFactory;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public void init(LoginCas loginCas) throws FrameworkRuntimeException {
		String eid = loginCas.getEid();
		EnterpriseVo enterprise = iEnterpriseService.get(eid);
		if (enterprise == null) {
			// 锁
			String dk = null;
			try {
				dk = LockKey.addEnterprise(eid);
				if (iDistributedLock.lock(dk)) {
					// 再次查询数据库
					enterprise = iEnterpriseService.get(eid);
					if (enterprise == null) {
						Date curr = new Date();
						String userBy = loginCas.getUserName();
						String enterpriseName = loginCas.getEnterpriseName();
						// 创建
						String enterpriseId = iEnterpriseService.add(eid, enterpriseName, curr, userBy);
						iAreaFactory.addAreaRoot(enterpriseName, enterpriseName, enterpriseId);
					}
				}
			} finally {
				iDistributedLock.releaseLock(dk);
			}
		}
	}
}
