package com.dotop.pipe.web.factory.collection;

import com.dotop.pipe.web.api.factory.collection.ICollectionDeviceFactory;
import com.dotop.pipe.api.service.collection.ICollectionDeviceService;
import com.dotop.pipe.auth.cas.web.IAuthCasWeb;
import com.dotop.pipe.auth.core.vo.auth.LoginCas;
import com.dotop.pipe.core.bo.collection.CollectionDeviceBo;
import com.dotop.pipe.core.form.CollectionDeviceForm;
import com.dotop.pipe.core.vo.collection.CollectionDeviceVo;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.BeanUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 *
 * @date 2019年2月15日
 */
@Component
public class CollectionDeviceFactoryImpl implements ICollectionDeviceFactory {

	private final static Logger logger = LogManager.getLogger(CollectionDeviceFactoryImpl.class);

	@Autowired
	private IAuthCasWeb iAuthCasApi;

	@Autowired
	private ICollectionDeviceService iCollectionDeviceService;

	@Override
	public Pagination<CollectionDeviceVo> page(CollectionDeviceForm collectionDeviceForm)
			throws FrameworkRuntimeException {
		LoginCas loginCas = iAuthCasApi.get();
		String operEid = loginCas.getOperEid();
		String userId = loginCas.getUserId();
		CollectionDeviceBo collectionDeviceBo = BeanUtils.copyProperties(collectionDeviceForm,
				CollectionDeviceBo.class);
		collectionDeviceBo.setUserId(userId);
		collectionDeviceBo.setEnterpriseId(operEid);
		Pagination<CollectionDeviceVo> pagination = iCollectionDeviceService.page(collectionDeviceBo);
		return pagination;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public CollectionDeviceVo add(CollectionDeviceForm collectionDeviceForm) {
		LoginCas loginCas = iAuthCasApi.get();
		String operEid = loginCas.getOperEid();
		String userId = loginCas.getUserId();
		String userBy = loginCas.getUserName();
		CollectionDeviceBo collectionDeviceBo = BeanUtils.copyProperties(collectionDeviceForm,
				CollectionDeviceBo.class);
		collectionDeviceBo.setCurr(new Date());
		collectionDeviceBo.setUserBy(userBy);
		collectionDeviceBo.setUserId(userId);
		collectionDeviceBo.setEnterpriseId(operEid);
		CollectionDeviceVo collectionDeviceVo = iCollectionDeviceService.add(collectionDeviceBo);
		return collectionDeviceVo;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public String del(CollectionDeviceForm collectionDeviceForm) {
		LoginCas loginCas = iAuthCasApi.get();
		String operEid = loginCas.getOperEid();
		String userId = loginCas.getUserId();
		String userBy = loginCas.getUserName();
		CollectionDeviceBo collectionDeviceBo = BeanUtils.copyProperties(collectionDeviceForm,
				CollectionDeviceBo.class);
		collectionDeviceBo.setCurr(new Date());
		collectionDeviceBo.setUserBy(userBy);
		collectionDeviceBo.setUserId(userId);
		collectionDeviceBo.setEnterpriseId(operEid);
		return iCollectionDeviceService.del(collectionDeviceBo);
	}

}
