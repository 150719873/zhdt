package com.dotop.smartwater.project.module.api.tool.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.api.revenue.IInformationFactory;
import com.dotop.smartwater.project.module.api.tool.IDeviceBatchFactory;
import com.dotop.smartwater.project.module.core.auth.local.AuthCasClient;
import com.dotop.smartwater.project.module.core.auth.vo.UserVo;
import com.dotop.smartwater.project.module.core.water.bo.DeviceBatchBo;
import com.dotop.smartwater.project.module.core.water.bo.DeviceBatchRelationBo;
import com.dotop.smartwater.project.module.core.water.bo.DeviceParametersBo;
import com.dotop.smartwater.project.module.core.water.bo.StoreProductBo;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.module.core.water.constants.WaterConstants;
import com.dotop.smartwater.project.module.core.water.form.DeviceBatchForm;
import com.dotop.smartwater.project.module.core.water.form.DeviceBatchRelatioForm;
import com.dotop.smartwater.project.module.core.water.form.DeviceForm;
import com.dotop.smartwater.project.module.core.water.vo.DeviceBatchVo;
import com.dotop.smartwater.project.module.core.water.vo.DeviceParametersVo;
import com.dotop.smartwater.project.module.core.water.vo.DeviceVo;
import com.dotop.smartwater.project.module.core.water.vo.StoreProductVo;
import com.dotop.smartwater.project.module.service.store.IStoreProductService;
import com.dotop.smartwater.project.module.service.tool.IDeviceBatchService;
import com.dotop.smartwater.project.module.service.tool.IDeviceParametersService;

/**
 * 设备批次管理

 *
 */

@Component
public class DeviceBatchFactoryImpl implements IDeviceBatchFactory {
	
	@Autowired
	private IDeviceBatchService service;
	
	@Autowired
	private IStoreProductService iStoreProductService;
	
	@Autowired
	private IDeviceParametersService iDeviceParametersService;
	
	@Autowired
	private IInformationFactory iInformationFactory;
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public DeviceBatchVo add(DeviceBatchForm form)  {
		UserVo user = AuthCasClient.getUser();

		DeviceBatchBo bo = new DeviceBatchBo();
		BeanUtils.copyProperties(form, bo);
		bo.setEnterpriseid(user.getEnterpriseid());
		bo.setUserBy(user.getName());
		bo.setCurr(new Date());
		bo.setStatus(WaterConstants.PRODUCTION_NO_START+"");
		
		// 查询产品信息
		StoreProductBo storeProductBo = new StoreProductBo();
		storeProductBo.setEnterpriseid(user.getEnterpriseid());
		storeProductBo.setProductId(form.getProductId());
		StoreProductVo productVo = iStoreProductService.getProductByNo(storeProductBo);
		if(productVo != null) {
			bo.setProductNo(productVo.getProductNo());
			bo.setProductName(productVo.getName());
			bo.setProductModel(productVo.getModel());
			bo.setProductCaliber(productVo.getCaliber()+"");
			bo.setProductVender(productVo.getVender());
		}
		
		// 查询设备参数信息
		DeviceParametersBo deviceParametersBo = new DeviceParametersBo();
		deviceParametersBo.setDeviceParId(form.getDeviceParId());
		deviceParametersBo.setEnterpriseid(user.getEnterpriseid());
		DeviceParametersVo parametersVo =  iDeviceParametersService.get(deviceParametersBo);
		if (parametersVo != null) {
			bo.setDeviceName(parametersVo.getDeviceName());
			bo.setMode(parametersVo.getModeName());
			bo.setTaptype(parametersVo.getValveType());
			bo.setUnit(parametersVo.getUnitName());
			bo.setDeviceName(parametersVo.getDeviceName());
			bo.setSensorType(parametersVo.getSensorTypeName());
		}
		
		if (!service.checkBatchIsExist(bo)) {
			throw new FrameworkRuntimeException(ResultCode.Fail, "批次号已存在");
		}
		return service.add(bo);
	}
	
	@Override
	public DeviceBatchVo edit(DeviceBatchForm form) {
		UserVo user = AuthCasClient.getUser();

		DeviceBatchBo bo = new DeviceBatchBo();
		BeanUtils.copyProperties(form, bo);
		bo.setEnterpriseid(user.getEnterpriseid());
		bo.setUserBy(user.getName());
		bo.setCurr(new Date());
		
		// 查询产品信息
		StoreProductBo storeProductBo = new StoreProductBo();
		storeProductBo.setEnterpriseid(user.getEnterpriseid());
		storeProductBo.setProductId(form.getProductId());
		StoreProductVo productVo = iStoreProductService.getProductByNo(storeProductBo);
		if(productVo != null) {
			bo.setProductNo(productVo.getProductNo());
			bo.setProductName(productVo.getName());
			bo.setProductModel(productVo.getModel());
			bo.setProductCaliber(productVo.getCaliber()+"");
			bo.setProductVender(productVo.getVender());
		}
		
		// 查询设备参数信息
		DeviceParametersBo deviceParametersBo = new DeviceParametersBo();
		deviceParametersBo.setDeviceParId(form.getDeviceParId());
		deviceParametersBo.setEnterpriseid(user.getEnterpriseid());
		DeviceParametersVo parametersVo =  iDeviceParametersService.get(deviceParametersBo);
		if (parametersVo != null) {
			bo.setDeviceName(parametersVo.getDeviceName());
			bo.setMode(parametersVo.getModeName());
			bo.setTaptype(parametersVo.getValveType());
			bo.setUnit(parametersVo.getUnitName());
			bo.setDeviceName(parametersVo.getDeviceName());
			bo.setSensorType(parametersVo.getSensorTypeName());
		}
		return service.edit(bo);
	}
	
	
	
	public Pagination<DeviceBatchVo> page(DeviceBatchForm form) {
		UserVo user = AuthCasClient.getUser();
		DeviceBatchBo bo = new DeviceBatchBo();
		BeanUtils.copyProperties(form, bo);
		bo.setEnterpriseid(user.getEnterpriseid());
		return service.page(bo);
	}
	
	public List<DeviceParametersVo> noEndList(DeviceBatchForm form) {
		UserVo user = AuthCasClient.getUser();
		DeviceBatchBo bo = new DeviceBatchBo();
		BeanUtils.copyProperties(form, bo);
		bo.setEnterpriseid(user.getEnterpriseid());
		return iDeviceParametersService.noEndList(bo);
	}
	
	
	public Pagination<DeviceVo> detailPage(DeviceBatchRelatioForm form) {
		UserVo user = AuthCasClient.getUser();
		DeviceBatchRelationBo bo = new DeviceBatchRelationBo();
		BeanUtils.copyProperties(form, bo);
		bo.setEnterpriseid(user.getEnterpriseid());
		return service.detailPage(bo);
	}
	
	public boolean delete(DeviceBatchForm form) {
		UserVo user = AuthCasClient.getUser();
		DeviceBatchBo bo = new DeviceBatchBo();
		BeanUtils.copyProperties(form, bo);
		bo.setEnterpriseid(user.getEnterpriseid());
		bo.setUserBy(user.getName());
		bo.setCurr(new Date());
		
		DeviceBatchVo vo = service.get(bo);
		if (vo != null) {
			if (vo.getStatus().equals(""+WaterConstants.PRODUCTION_STARTING)) {
				throw new FrameworkRuntimeException(String.valueOf(ResultCode.PRODUCTION_STARTING_NO_DELETE),
						ResultCode.getMessage(ResultCode.PRODUCTION_STARTING_NO_DELETE));
			} else if (vo.getStatus().equals(""+WaterConstants.PRODUCTION_END)) {
				throw new FrameworkRuntimeException(String.valueOf(ResultCode.PRODUCTION_END_NO_DELETE),
						ResultCode.getMessage(ResultCode.PRODUCTION_END_NO_DELETE));
			}
		} else {
			throw new FrameworkRuntimeException(String.valueOf(ResultCode.NO_GET_DATA),
					ResultCode.getMessage(ResultCode.NO_GET_DATA));
		}
		
		return service.delete(bo);
	}
	
	
	public boolean deleteDevice(DeviceBatchRelatioForm form) {
		UserVo user = AuthCasClient.getUser();
		DeviceBatchRelationBo bo = new DeviceBatchRelationBo();
		BeanUtils.copyProperties(form, bo);
		bo.setEnterpriseid(user.getEnterpriseid());
		bo.setUserBy(user.getName());
		bo.setCurr(new Date());
		
		// 删除设备
		DeviceForm deviceForm = new DeviceForm();
		deviceForm.setDeveui(form.getDeveui());
		iInformationFactory.del(deviceForm);
		//删除关系
		service.deleteDevice(bo);
		return true;
	}
	
	
	public boolean end(DeviceBatchForm form) {
		UserVo user = AuthCasClient.getUser();
		DeviceBatchBo bo = new DeviceBatchBo();
		BeanUtils.copyProperties(form, bo);
		bo.setEnterpriseid(user.getEnterpriseid());
		bo.setUserBy(user.getName());
		bo.setCurr(new Date());
		bo.setStatus(""+WaterConstants.PRODUCTION_END);
		DeviceBatchVo vo = service.get(bo);
		if (vo != null) {
			if (vo.getStatus().equals(""+WaterConstants.PRODUCTION_END)) {
				throw new FrameworkRuntimeException(String.valueOf(ResultCode.BATCH_END),
						ResultCode.getMessage(ResultCode.BATCH_END));
			}
		} else {
			throw new FrameworkRuntimeException(String.valueOf(ResultCode.NO_GET_DATA),
					ResultCode.getMessage(ResultCode.NO_GET_DATA));
		}
		
		return service.end(bo);
	}
	
	
	public DeviceBatchVo get(DeviceBatchForm form) {
		UserVo user = AuthCasClient.getUser();
		DeviceBatchBo bo = new DeviceBatchBo();
		BeanUtils.copyProperties(form, bo);
		bo.setEnterpriseid(user.getEnterpriseid());
		return service.get(bo);
	}
	
}
