package com.dotop.smartwater.project.module.service.device.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.UuidUtils;
import com.dotop.smartwater.project.module.core.water.bo.DeviceMigrationBo;
import com.dotop.smartwater.project.module.core.water.bo.DeviceMigrationHistoryBo;
import com.dotop.smartwater.project.module.core.water.dto.DeviceMigrationDto;
import com.dotop.smartwater.project.module.core.water.dto.DeviceMigrationHistoryDto;
import com.dotop.smartwater.project.module.core.water.dto.StoreProductDto;
import com.dotop.smartwater.project.module.core.water.vo.DeviceMigrationHistoryVo;
import com.dotop.smartwater.project.module.core.water.vo.DeviceMigrationVo;
import com.dotop.smartwater.project.module.core.water.vo.StoreProductVo;
import com.dotop.smartwater.project.module.dao.device.IDeviceMigrationDao;
import com.dotop.smartwater.project.module.dao.store.IStoreProductDao;
import com.dotop.smartwater.project.module.service.device.IDeviceMigrationService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

/**
 * 设备迁移

 * @date 2019-08-09
 *
 */
@Service
public class DeviceMigrationServiceImpl implements IDeviceMigrationService {

	private static final Logger LOGGER = LogManager.getLogger(DeviceMigrationServiceImpl.class);
	
	@Autowired
	private IDeviceMigrationDao iDeviceMigrationDao;
	
	@Autowired
	private IStoreProductDao iStoreProductDao;
	
	@Override
	public List<DeviceMigrationVo> tempPage(DeviceMigrationBo deviceMigrationBo) {
		// TODO Auto-generated method stub
		try {
			// 参数转换
			DeviceMigrationDto deviceMigrationDto = new DeviceMigrationDto();
			BeanUtils.copyProperties(deviceMigrationBo, deviceMigrationDto);
			List<DeviceMigrationVo> list = iDeviceMigrationDao.tempPage(deviceMigrationDto);
			return list;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public List<DeviceMigrationVo> pageMigration(DeviceMigrationBo deviceMigrationBo) {
		// TODO Auto-generated method stub
		try {
			// 参数转换
			DeviceMigrationDto deviceMigrationDto = new DeviceMigrationDto();
			BeanUtils.copyProperties(deviceMigrationBo, deviceMigrationDto);
			List<DeviceMigrationVo> list = iDeviceMigrationDao.page(deviceMigrationDto);
			
			iDeviceMigrationDao.clearTemp();
			//将查询到的设备信息放入缓存表
			iDeviceMigrationDao.insertTempSelect(deviceMigrationDto);

			return list;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public Pagination<DeviceMigrationVo> detail(DeviceMigrationBo deviceMigrationBo) {
		// TODO Auto-generated method stub
		try {
			// 参数转换
			DeviceMigrationDto deviceMigrationDto = new DeviceMigrationDto();
			BeanUtils.copyProperties(deviceMigrationBo, deviceMigrationDto);

			Page<Object> pageHelper = PageHelper.startPage(deviceMigrationBo.getPage(), deviceMigrationBo.getPageCount());
			List<DeviceMigrationVo> list = iDeviceMigrationDao.detail(deviceMigrationDto);
			Pagination<DeviceMigrationVo> pagination = new Pagination<>(deviceMigrationBo.getPageCount(), deviceMigrationBo.getPage());
			pagination.setData(list);
			pagination.setTotalPageSize(pageHelper.getTotal());
			
			return pagination;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public Integer clearTemp(DeviceMigrationBo deviceMigrationBo) {
		// TODO Auto-generated method stub
		try {
			
			return iDeviceMigrationDao.clearTemp();
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		} 
	}

	@Override
	public Pagination<DeviceMigrationHistoryVo> pageHistory(DeviceMigrationHistoryBo deviceMigrationHistoryBo) {
		// TODO Auto-generated method stub
		try {
			// 参数转换
			DeviceMigrationHistoryDto deviceMigrationHistoryDto = new DeviceMigrationHistoryDto();
			BeanUtils.copyProperties(deviceMigrationHistoryBo, deviceMigrationHistoryDto);

			Page<Object> pageHelper = PageHelper.startPage(deviceMigrationHistoryBo.getPage(), deviceMigrationHistoryBo.getPageCount());
			List<DeviceMigrationHistoryVo> list = iDeviceMigrationDao.pageHistory(deviceMigrationHistoryDto);
			Pagination<DeviceMigrationHistoryVo> pagination = new Pagination<>(deviceMigrationHistoryBo.getPageCount(), deviceMigrationHistoryBo.getPage());
			pagination.setData(list);
			pagination.setTotalPageSize(pageHelper.getTotal());
			return pagination;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}
	
	public String migrationDevice(DeviceMigrationHistoryBo deviceMigrationHistoryBo) {
		// TODO Auto-generated method stub
		try {
			//从缓存表中读取查询出来的设备列表
			List<DeviceMigrationVo> list = iDeviceMigrationDao.tempList(new DeviceMigrationDto());
			if(list == null || list.isEmpty()) {
				return "";
			}
			// 参数转换
			DeviceMigrationHistoryDto deviceMigrationHistoryDto = new DeviceMigrationHistoryDto();
			BeanUtils.copyProperties(deviceMigrationHistoryBo, deviceMigrationHistoryDto);
			List<DeviceMigrationDto> list_migration = new ArrayList<DeviceMigrationDto>();
			List<DeviceMigrationDto> list_temp = new ArrayList<DeviceMigrationDto>();
			//实际迁移个数
			Integer count_practical = 0;
			//失败迁移个数
			Integer count_unsuccess = 0;
			for(int i = 0;i < list.size();i++) {
				DeviceMigrationVo item = list.get(i);
				if(item.getStatus() == DeviceMigrationVo.MIGRATION_STATUS_WAIT) {
					count_practical++;
					DeviceMigrationDto deviceMigrationDto = new DeviceMigrationDto();
					BeanUtils.copyProperties(item, deviceMigrationDto);
//					Integer count = iDeviceMigrationDao.judgeDevice(deviceMigrationDto);
					//no表示未开户，yes表示开户
					if("no".equals(item.getIsOpen())) {
						list_migration.add(deviceMigrationDto);
						list.remove(item);
						i--;
					}else {
						item.setStatus(DeviceMigrationVo.MIGRATION_STATUS_FAIL);
						item.setDescription(DeviceMigrationVo.MIGRATION_DESCRIPTION_FAIL_OPEN);
						count_unsuccess++;
					}
				}
			}
			for(DeviceMigrationVo item: list) {
				DeviceMigrationDto deviceMigrationDto = new DeviceMigrationDto();
				BeanUtils.copyProperties(item, deviceMigrationDto);
				deviceMigrationDto.setId(UuidUtils.getUuid());
				list_temp.add(deviceMigrationDto);
			}
			if(list_migration != null && !list_migration.isEmpty()) {
				StoreProductVo storeProductVo = new StoreProductVo();
				List<StoreProductVo> list_product1 = new ArrayList<StoreProductVo>();
				List<StoreProductVo> list_product2 = new ArrayList<StoreProductVo>();
				for(DeviceMigrationDto item: list_migration) {
					if(StringUtils.isNotBlank(item.getProductId())) {
						//在已查询出的原水司的产品列表中查找该产品
						storeProductVo = ergodicProductList(item.getProductId(), list_product1, "id");
						if(storeProductVo == null) {
							StoreProductDto storeProductDto = new StoreProductDto();
							storeProductDto.setProductId(item.getProductId());
							storeProductDto.setEnterpriseid(deviceMigrationHistoryBo.getInitialId());
							//获取原水司中产品信息
							storeProductVo = iStoreProductDao.getProductByNo(storeProductDto);
							if(storeProductVo != null) {
								//插入到原水司产品列表信息
								list_product1.add(storeProductVo);
							}
						}
						
						if(storeProductVo != null) {
							StoreProductVo storeProductVo2 = ergodicProductList(storeProductVo.getProductNo(), list_product2, "no");
							if(storeProductVo2 == null) {
								//检验目标水司中是否存在该产品
								StoreProductDto storeProductDto1 = new StoreProductDto();
								storeProductDto1.setProductNo(storeProductVo.getProductNo());
								storeProductDto1.setEnterpriseid(deviceMigrationHistoryBo.getTargetId());
								storeProductVo2 = iStoreProductDao.getProductByNo(storeProductDto1);
								if(storeProductVo2 != null) {
									item.setProductId(storeProductVo2.getProductId());
									storeProductVo2.setUpdateUserId("update");
									list_product2.add(storeProductVo2);
								}else {
									//新增产品
									StoreProductDto storeProductDto3 = new StoreProductDto();
									BeanUtils.copyProperties(storeProductVo, storeProductDto3);
									storeProductDto3.setProductId(UuidUtils.getUuid());
									storeProductDto3.setEnterpriseid(deviceMigrationHistoryBo.getTargetId());
									storeProductDto3.setStatus(StoreProductVo.PRODUCT_STATUS_UP);
									storeProductDto3.setCreateUserId("1");
									storeProductDto3.setCreateUsername("迁移新增");
									storeProductDto3.setCreateTime(new Date());
//									iStoreProductDao.addProduct(storeProductDto3);
									item.setProductId(storeProductDto3.getProductId());
									storeProductVo2 = new StoreProductVo();
									BeanUtils.copyProperties(storeProductDto3, storeProductVo2);
									storeProductVo2.setUpdateUserId("add");
									list_product2.add(storeProductVo2);
								}
							}else {
								item.setProductId(storeProductVo2.getProductId());
								/*if("update".equals(storeProductVo2.getUpdateUserId())) {
									item.setProductId(storeProductVo2.getProductId());
								}else if("add".equals(storeProductVo2.getUpdateUserId())) {
									//新增产品
									StoreProductDto storeProductDto3 = new StoreProductDto();
									BeanUtils.copyProperties(storeProductVo, storeProductDto3);
									storeProductDto3.setProductId(UuidUtils.getUuid());
									storeProductDto3.setEnterpriseid(deviceMigrationHistoryBo.getTargetId());
									storeProductDto3.setStatus(StoreProductVo.PRODUCT_STATUS_UP);
									storeProductDto3.setCreateUserId("1");
									storeProductDto3.setCreateUsername("迁移新增");
									storeProductDto3.setCreateTime(new Date());
//									iStoreProductDao.addProduct(storeProductDto3);
									item.setProductId(storeProductDto3.getProductId());
								}*/
							}
						}else {
							item.setProductId(null);
						}
					}
				}
				//批量迁移设备
				if(list_migration != null && !list_migration.isEmpty()) {
					iDeviceMigrationDao.migrateDevice(list_migration, deviceMigrationHistoryDto);
				}
				//将迁移成功的设备已插入迁移表中
				if(!list_migration.isEmpty()) {
					for(DeviceMigrationDto item: list_migration) {
						item.setId(UuidUtils.getUuid());
						item.setMigrationHistoryId(deviceMigrationHistoryDto.getId());
						item.setStatus(DeviceMigrationVo.MIGRATION_STATUS_FINISH);
						item.setDescription(DeviceMigrationVo.MIGRATION_DESCRIPTION_SUCCESS);
					}
					iDeviceMigrationDao.batchInsertMigration(list_migration);
				}
//				System.out.println("***********list_product2: " + JSONUtils.toJSONString(list_product2));
				if(!list_product2.isEmpty()) {
					//批量新增产品
					for(int i=0;i < list_product2.size();i++) {
						//将目标水司中已经存在的产品从需要新增产品的列表中移除
						if("update".equals(list_product2.get(i).getUpdateUserId())) {
							list_product2.remove(i);
							i--;
						}
					}
					if(!list_product2.isEmpty()) {
						iStoreProductDao.batchAddProduct(list_product2);
					}
				}
			}
			deviceMigrationHistoryDto.setPracticalNum(count_practical);
			deviceMigrationHistoryDto.setSuccessfulNum(list_migration.size());
			deviceMigrationHistoryDto.setUnsuccessfulNum(count_unsuccess);
			//创建迁移历史
			iDeviceMigrationDao.insertHistory(deviceMigrationHistoryDto);
			//清空缓存设备迁移表
			iDeviceMigrationDao.clearTemp();
			//重新插入缓存设备迁移表
			if(!list_temp.isEmpty()) {
				iDeviceMigrationDao.batchInsertTemp(list_temp);
			}
			return null;
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}
	/**
	 * 遍历产品列表并返回找到的产品对象
	 * @return
	 */
	protected StoreProductVo ergodicProductList(String id, List<StoreProductVo> list, String type) {
		if(StringUtils.isBlank(id) || list == null || list.isEmpty() || StringUtils.isBlank(type)) {
			return null;
		}
		for(StoreProductVo item: list) {
			if("id".equals(type)) {
				if(id.equals(item.getProductId())) {
					return item;
				}
			}else if("no".equals(type)) {
				if(id.equals(item.getProductNo())) {
					return item;
				}
			}
		}
		return null;
	}

	@Override
	public String deleteHistory(DeviceMigrationHistoryBo deviceMigrationHistoryBo) {
		// TODO Auto-generated method stub
		try {
			// 参数转换
			DeviceMigrationHistoryDto deviceMigrationHistoryDto = new DeviceMigrationHistoryDto();
			BeanUtils.copyProperties(deviceMigrationHistoryBo, deviceMigrationHistoryDto);

			Integer count = iDeviceMigrationDao.deleteHistory(deviceMigrationHistoryDto);
			if(count == 1) {
				iDeviceMigrationDao.deleteMigration(deviceMigrationHistoryDto);
				return "success";
			}else {
				return "fail";
			}
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

	@Override
	public Integer updateTemp(DeviceMigrationBo deviceMigrationBo) {
		// TODO Auto-generated method stub
		try {
			// 参数转换
			DeviceMigrationDto deviceMigrationDto = new DeviceMigrationDto();
			BeanUtils.copyProperties(deviceMigrationBo, deviceMigrationDto);

			return iDeviceMigrationDao.updateTemp(deviceMigrationDto);
		} catch (DataAccessException e) {
			LOGGER.error(LogMsg.to(e));
			throw new FrameworkRuntimeException(BaseExceptionConstants.DATABASE_EXCEPTION, e.getMessage(), e);
		}
	}

}
