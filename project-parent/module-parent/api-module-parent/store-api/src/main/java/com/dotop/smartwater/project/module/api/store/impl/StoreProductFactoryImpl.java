package com.dotop.smartwater.project.module.api.store.impl;

import java.util.Date;
import java.util.List;

import com.dotop.smartwater.project.module.api.store.IStoreProductFactory;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.UuidUtils;
import com.dotop.smartwater.project.module.client.third.fegin.pipe.IPipeFeginClient;
import com.dotop.smartwater.project.module.core.auth.local.AuthCasClient;
import com.dotop.smartwater.project.module.core.auth.vo.UserVo;
import com.dotop.smartwater.project.module.core.third.bo.pipe.PipeProductBo;
import com.dotop.smartwater.project.module.core.third.vo.pipe.PipeProductVo;
import com.dotop.smartwater.project.module.core.water.bo.StoreProductBo;
import com.dotop.smartwater.project.module.core.water.form.StoreProductForm;
import com.dotop.smartwater.project.module.core.water.vo.StoreProductVo;
import com.dotop.smartwater.project.module.service.store.IStoreProductService;

@Component
public class StoreProductFactoryImpl implements IStoreProductFactory {

	@Autowired
	private IStoreProductService iStoreProductService;
	
	@Autowired
	private IPipeFeginClient iPipeFeginClient;

	@Override
	public Pagination<StoreProductVo> getProByCon(StoreProductForm storeProductForm) throws FrameworkRuntimeException {
		// TODO Auto-generated method stub
		// 获取用户信息
		UserVo user = AuthCasClient.getUser();
		String userBy = user.getAccount();
		Date curr = new Date();
		// 业务逻辑
		StoreProductBo storeProductBo = new StoreProductBo();
		BeanUtils.copyProperties(storeProductForm, storeProductBo);
		storeProductBo.setEnterpriseid(user.getEnterpriseid());
		storeProductBo.setUserBy(userBy);
		storeProductBo.setCurr(curr);

		// 数据处理
		Pagination<StoreProductVo> pagination = iStoreProductService.getProByCon(storeProductBo);
		return pagination;
	}

	@Override
	public StoreProductVo getProductByNo(StoreProductForm storeProductForm) throws FrameworkRuntimeException {
		// TODO Auto-generated method stub
		// 获取用户信息
		UserVo user = AuthCasClient.getUser();
		String userBy = user.getAccount();
		Date curr = new Date();
		// 业务逻辑
		StoreProductBo storeProductBo = new StoreProductBo();
		BeanUtils.copyProperties(storeProductForm, storeProductBo);
		storeProductBo.setEnterpriseid(user.getEnterpriseid());
		storeProductBo.setUserBy(userBy);
		storeProductBo.setCurr(curr);

		// 数据处理
		StoreProductVo storePro = iStoreProductService.getProductByNo(storeProductBo);
		return storePro;
	}

	@Override
	public Integer addProduct(StoreProductForm storeProductForm) throws FrameworkRuntimeException {
		// TODO Auto-generated method stub
		// 获取用户信息
		UserVo user = AuthCasClient.getUser();
		String userBy = user.getAccount();
		Date curr = new Date();
		// 业务逻辑
		StoreProductBo storeProductBo = new StoreProductBo();
		BeanUtils.copyProperties(storeProductForm, storeProductBo);
		storeProductBo.setEnterpriseid(user.getEnterpriseid());
		storeProductBo.setUserBy(userBy);
		storeProductBo.setCurr(curr);
		
		if(user.getEnterprise().getName() != null) {
			storeProductBo.setEnterprisename(user.getEnterprise().getName());
		}
		
		storeProductBo.setStatus(StoreProductVo.PRODUCT_STATUS_UP);			
		storeProductBo.setCreateTime(curr);
		storeProductBo.setCreateUserId(user.getUserid());//获取创建人ID
		storeProductBo.setCreateUsername(user.getName());
		storeProductBo.setUpdateTime(curr);
		storeProductBo.setUpdateUserId(user.getUserid());
		storeProductBo.setUpdateUsername(user.getName());
		storeProductBo.setProductId(UuidUtils.getUuid());
		
		// 数据处理
		return iStoreProductService.addProduct(storeProductBo);
	}

	@Override
	public Integer editProduct(StoreProductForm storeProductForm) throws FrameworkRuntimeException {
		// TODO Auto-generated method stub
		// 获取用户信息
		UserVo user = AuthCasClient.getUser();
		String userBy = user.getAccount();
		Date curr = new Date();
		// 业务逻辑
		StoreProductBo storeProductBo = new StoreProductBo();
		BeanUtils.copyProperties(storeProductForm, storeProductBo);
		storeProductBo.setEnterpriseid(user.getEnterpriseid());
		storeProductBo.setUserBy(userBy);
		storeProductBo.setCurr(curr);

		storeProductBo.setUpdateTime(curr);
		storeProductBo.setUpdateUserId(user.getUserid());
		storeProductBo.setUpdateUsername(user.getName());

		// 数据处理
		return iStoreProductService.editProduct(storeProductBo);
	}

	@Override
	public Integer changeStatus(StoreProductForm storeProductForm) throws FrameworkRuntimeException {
		// TODO Auto-generated method stub
		// 获取用户信息
		UserVo user = AuthCasClient.getUser();
		String userBy = user.getAccount();
		Date curr = new Date();
		// 业务逻辑
		StoreProductBo storeProductBo = new StoreProductBo();
		BeanUtils.copyProperties(storeProductForm, storeProductBo);
		storeProductBo.setEnterpriseid(user.getEnterpriseid());
		storeProductBo.setUserBy(userBy);
		storeProductBo.setCurr(curr);

		Integer status = storeProductForm.getStatus();
		Integer change_status = -2;
		if (status == 1) { // -1-删除 0-下线 1-保存 2-上线
			change_status = 2;
		} else if (status == 2) {
			change_status = 0;
		} else if (status == 0) {
			change_status = 1;
		}
		storeProductBo.setUpdateTime(curr);
		storeProductBo.setUpdateUserId(user.getUserid());
		storeProductBo.setUpdateUsername(user.getName());
		storeProductBo.setStatus(change_status);

		return iStoreProductService.changeStatus(storeProductBo);
	}

	@Override
	public Integer checkProductNo(StoreProductForm storeProductForm) throws FrameworkRuntimeException {
		// TODO Auto-generated method stub
		// 获取用户信息
		UserVo user = AuthCasClient.getUser();
		// 业务逻辑
		StoreProductBo storeProductBo = new StoreProductBo();
		BeanUtils.copyProperties(storeProductForm, storeProductBo);
		storeProductBo.setEnterpriseid(user.getEnterpriseid());

		// 数据处理
		return iStoreProductService.checkProductNo(storeProductBo);
	}
	
	@Override
	public Integer checkProductName(StoreProductForm storeProductForm) {
		// TODO Auto-generated method stub
		// 获取用户信息
		UserVo user = AuthCasClient.getUser();
		// 业务逻辑
		StoreProductBo storeProductBo = new StoreProductBo();
		BeanUtils.copyProperties(storeProductForm, storeProductBo);
		storeProductBo.setEnterpriseid(user.getEnterpriseid());

		// 数据处理
		return iStoreProductService.checkProductName(storeProductBo);
	}


	@Override
	public Integer deleteProduct(StoreProductForm storeProductForm) throws FrameworkRuntimeException {
		// TODO Auto-generated method stub
		// 获取用户信息
		UserVo user = AuthCasClient.getUser();
		String userBy = user.getAccount();
		Date curr = new Date();
		// 业务逻辑
		StoreProductBo storeProductBo = new StoreProductBo();
		BeanUtils.copyProperties(storeProductForm, storeProductBo);
		storeProductBo.setEnterpriseid(user.getEnterpriseid());
		storeProductBo.setUserBy(userBy);
		storeProductBo.setCurr(curr);

		// 数据处理
		return iStoreProductService.deleteProduct(storeProductBo);
	}

	@Override
	public List<StoreProductVo> getLinePro(StoreProductForm storeProductForm) throws FrameworkRuntimeException {
		// TODO Auto-generated method stub
		// 获取用户信息
		UserVo user = AuthCasClient.getUser();
		String userBy = user.getAccount();
		Date curr = new Date();
		// 业务逻辑
		StoreProductBo storeProductBo = new StoreProductBo();
		BeanUtils.copyProperties(storeProductForm, storeProductBo);
		storeProductBo.setEnterpriseid(user.getEnterpriseid());
		storeProductBo.setUserBy(userBy);
		storeProductBo.setCurr(curr);
		storeProductBo.setStatus(2);

		// 数据处理
		List<StoreProductVo> list_pro = iStoreProductService.getLinePro(storeProductBo);
		return list_pro;
	}
	
	@Override
	public List<StoreProductVo> list(StoreProductForm storeProductForm) throws FrameworkRuntimeException {
		// TODO Auto-generated method stub
		// 获取用户信息
		UserVo user = AuthCasClient.getUser();
		// 业务逻辑
		StoreProductBo storeProductBo = new StoreProductBo();
		BeanUtils.copyProperties(storeProductForm, storeProductBo);
		storeProductBo.setEnterpriseid(user.getEnterpriseid());

		// 数据处理
		List<StoreProductVo> list = iStoreProductService.list(storeProductBo);
		return list;
	}

	@Override
	public Pagination<PipeProductVo> getPipeProduct(StoreProductForm storeProductForm)
			throws FrameworkRuntimeException {
		// TODO Auto-generated method stub
		UserVo user = AuthCasClient.getUser();
		
		PipeProductBo pipeProductBo = new PipeProductBo();
		if(!StringUtils.isBlank(storeProductForm.getName())) {
			pipeProductBo.setName(storeProductForm.getName());
		}
		if(!StringUtils.isBlank(storeProductForm.getProductNo())) {
			pipeProductBo.setCode(storeProductForm.getProductNo());
		}
		pipeProductBo.setEnterpriseId(user.getEnterpriseid());
		pipeProductBo.setPage(storeProductForm.getPage());
		pipeProductBo.setPageSize(storeProductForm.getPageCount());

		return iPipeFeginClient.productPage(pipeProductBo);
	}
}
