package com.dotop.smartwater.project.module.api.store.impl;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.dependence.core.utils.UuidUtils;
import com.dotop.smartwater.project.module.api.store.IStorageFactory;
import com.dotop.smartwater.project.module.core.auth.local.AuthCasClient;
import com.dotop.smartwater.project.module.core.auth.vo.UserVo;
import com.dotop.smartwater.project.module.core.water.bo.StorageBo;
import com.dotop.smartwater.project.module.core.water.form.StorageForm;
import com.dotop.smartwater.project.module.core.water.utils.file.CellStyleFactory;
import com.dotop.smartwater.project.module.core.water.utils.file.ExcelCreator;
import com.dotop.smartwater.project.module.core.water.utils.file.ExcellException;
import com.dotop.smartwater.project.module.core.water.vo.StorageVo;
import com.dotop.smartwater.project.module.core.water.vo.StoreProductVo;
import com.dotop.smartwater.project.module.service.store.IStorageService;

import jxl.write.WritableCellFormat;

@Component
public class StorageFactoryImpl implements IStorageFactory {

	@Autowired
	private IStorageService iStorageService;

	@Override
	public Pagination<StorageVo> getStorageByCon(StorageForm storageForm) {
		// 获取用户信息
		UserVo user = AuthCasClient.getUser();
		String userBy = user.getAccount();
		Date curr = new Date();
		// 业务逻辑
		StorageBo storageBo = new StorageBo();
		BeanUtils.copyProperties(storageForm, storageBo);
		storageBo.setEnterpriseid(user.getEnterpriseid());
		storageBo.setUserBy(userBy);
		storageBo.setCurr(curr);

		// 数据处理
		return iStorageService.getStorageByCon(storageBo);
	}

	@Override
	public StorageVo getStorageByNo(StorageForm storageForm) {
		// 获取用户信息
		UserVo user = AuthCasClient.getUser();
		String userBy = user.getAccount();
		Date curr = new Date();
		// 业务逻辑
		StorageBo storageBo = new StorageBo();
		BeanUtils.copyProperties(storageForm, storageBo);
		storageBo.setEnterpriseid(user.getEnterpriseid());
		storageBo.setUserBy(userBy);
		storageBo.setCurr(curr);
		// 数据处理
		return iStorageService.getStorageByNo(storageBo);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public Integer editStorage(StorageForm storageForm) {
		// 获取用户信息
		UserVo user = AuthCasClient.getUser();
		String userBy = user.getAccount();
		Date curr = new Date();
		// 业务逻辑
		StorageBo storageBo = new StorageBo();
		BeanUtils.copyProperties(storageForm, storageBo);
		storageBo.setEnterpriseid(user.getEnterpriseid());
		storageBo.setUserBy(userBy);
		storageBo.setCurr(curr);
		storageBo.setStock(storageBo.getQuantity());

		if (storageBo.getStatus() == 2) {
			storageBo.setStorageDate(new Date());
			storageBo.setStorageUserId(user.getUserid());
			storageBo.setStorageUsername(user.getName());
		}

		// 数据处理
		return iStorageService.editStorage(storageBo);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public Integer addStorage(StorageForm storageForm) {
		// 获取用户信息
		UserVo user = AuthCasClient.getUser();
		String userBy = user.getAccount();
		Date curr = new Date();
		// 业务逻辑
		StorageBo storageBo = new StorageBo();
		BeanUtils.copyProperties(storageForm, storageBo);
		storageBo.setEnterpriseid(user.getEnterpriseid());
		storageBo.setUserBy(userBy);
		storageBo.setCurr(curr);
		storageBo.setStock(storageBo.getQuantity());
		if (!storageBo.getStatus().equals(0) && storageBo.getRecordNo() == null) {
			storageBo.setRecordNo(UuidUtils.getUuid());
		}

		if (storageBo.getStatus() != null && storageBo.getStatus() == 2) {
			storageBo.setStorageDate(new Date());
			storageBo.setStorageUserId(user.getUserid());
			storageBo.setStorageUsername(user.getName());
		}
		// 数据处理
		return iStorageService.addStorage(storageBo);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public Integer deleteStorage(StorageForm storageForm) {
		// 获取用户信息
		UserVo user = AuthCasClient.getUser();
		String userBy = user.getAccount();
		Date curr = new Date();
		// 业务逻辑
		StorageBo storageBo = new StorageBo();
		BeanUtils.copyProperties(storageForm, storageBo);
		storageBo.setEnterpriseid(user.getEnterpriseid());
		storageBo.setUserBy(userBy);
		storageBo.setCurr(curr);

		// 数据处理
		return iStorageService.deleteStorage(storageBo);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public Integer confirmStorage(StorageForm storageForm) {
		// 获取用户信息
		UserVo user = AuthCasClient.getUser();
		String userBy = user.getAccount();
		Date curr = new Date();
		// 业务逻辑
		StorageBo storageBo = new StorageBo();
		BeanUtils.copyProperties(storageForm, storageBo);
		storageBo.setEnterpriseid(user.getEnterpriseid());
		storageBo.setUserBy(userBy);
		storageBo.setCurr(curr);

		storageBo.setStorageDate(curr);
		storageBo.setStorageUserId(user.getUserid());
		storageBo.setStorageUsername(user.getName());

		// 数据处理
		return iStorageService.confirmStorage(storageBo);
	}

	@Override
	public List<StoreProductVo> getProIn(StorageForm storageForm) {
		// 获取用户信息
		UserVo user = AuthCasClient.getUser();
		String userBy = user.getAccount();
		Date curr = new Date();
		// 业务逻辑
		StorageBo storageBo = new StorageBo();
		BeanUtils.copyProperties(storageForm, storageBo);
		storageBo.setEnterpriseid(user.getEnterpriseid());
		storageBo.setUserBy(userBy);
		storageBo.setCurr(curr);
		storageBo.setStatus(2);

		// 数据处理
		return iStorageService.getProIn(storageBo);
	}

	@Override
	public void exportStorage(String tempFileName, StorageForm storageForm) throws ExcellException, IOException {
		UserVo user = AuthCasClient.getUser();

		StorageBo storageBo = new StorageBo();
		BeanUtils.copyProperties(storageForm, storageBo);
		storageBo.setEnterpriseid(user.getEnterpriseid());
		storageBo.setPage(1);
		storageBo.setPageCount(2000);// 最多打印2000条，太多excel内存炸掉
		int line = 0;
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH-MM-SS"); // 日期格式转换

		Pagination<StorageVo> pagination = iStorageService.getStorageByCon(storageBo);
		List<StorageVo> stor_list = pagination.getData();

		for (StorageVo item : stor_list) {
			if (item.getProductionDate() != null) {
				item.setProDateStr(formatter.format(item.getProductionDate()));
			}
			if (item.getStorageDate() != null) {
				item.setStorDateStr(formatter.format(item.getStorageDate()));
			}
			if (item.getEffectiveDate() != null) {
				item.setEffDateStr(formatter.format(item.getEffectiveDate()));
			}
		}
		ExcelCreator creator = new ExcelCreator(tempFileName);
		creator.CreateSheet("入库数据查询");
		WritableCellFormat wcf = CellStyleFactory.getMenuTopStyle();
		WritableCellFormat wcfItem = CellStyleFactory.getSchemeStyle();

		creator.writeExcel("记录号", 0, line, wcf);
		creator.writeExcel("仓库编号", 1, line, wcf);
		creator.writeExcel("仓库名称", 2, line, wcf);
		creator.writeExcel("产品编号", 3, line, wcf);
		creator.writeExcel("产品名称", 4, line, wcf);
		creator.writeExcel("数 量", 5, line, wcf);
		creator.writeExcel("单 位", 6, line, wcf);
		creator.writeExcel("单 价/元", 7, line, wcf);
		creator.writeExcel("总 价/元", 8, line, wcf);
		creator.writeExcel("生产日期", 9, line, wcf);
		creator.writeExcel("入库时间", 10, line, wcf);
		creator.writeExcel("有效日期", 11, line, wcf);
		creator.writeExcel("厂 家", 12, line, wcf);
		creator.writeExcel("入库人", 13, line, wcf);
		creator.writeExcel("状 态", 14, line, wcf);

		creator.getSheet().setColumnView(0, 10);
		creator.getSheet().setColumnView(1, 10);
		creator.getSheet().setColumnView(2, 15);
		creator.getSheet().setColumnView(3, 20);
		creator.getSheet().setColumnView(4, 20);
		creator.getSheet().setColumnView(5, 10);
		creator.getSheet().setColumnView(6, 10);
		creator.getSheet().setColumnView(7, 20);
		creator.getSheet().setColumnView(8, 20);
		creator.getSheet().setColumnView(9, 30);
		creator.getSheet().setColumnView(10, 30);
		creator.getSheet().setColumnView(11, 30);
		creator.getSheet().setColumnView(12, 15);
		creator.getSheet().setColumnView(13, 15);
		creator.getSheet().setColumnView(14, 10);

		if (stor_list != null && stor_list.size() > 0) {
			for (StorageVo stor : stor_list) {
				line++;
				creator.writeExcel(stor.getRecordNo(), 0, line, wcfItem);
				creator.writeExcel(stor.getRepoNo(), 1, line, wcfItem);
				creator.writeExcel(stor.getRepoName(), 2, line, wcfItem);
				creator.writeExcel(stor.getProductNo(), 3, line, wcfItem);
				creator.writeExcel(stor.getName(), 4, line, wcfItem);
				creator.writeExcel(stor.getStock(), 5, line, wcfItem);
				creator.writeExcel(stor.getUnit(), 6, line, wcfItem);
				creator.writeExcel(stor.getPrice(), 7, line, wcfItem);
				creator.writeExcel(stor.getTotal(), 8, line, wcfItem);
				creator.writeExcel(stor.getProDateStr(), 9, line, wcfItem);
				creator.writeExcel(stor.getStorDateStr(), 10, line, wcfItem);
				creator.writeExcel(stor.getEffDateStr(), 11, line, wcfItem);
				creator.writeExcel(stor.getVender(), 12, line, wcfItem);
				creator.writeExcel(stor.getStorageUsername(), 13, line, wcfItem);
				if (stor.getStatus() == 2) {
					creator.writeExcel("已入库", 14, line, wcfItem);
				} else if (stor.getStatus() == 1) {
					creator.writeExcel("已保存", 14, line, wcfItem);
				} else if (stor.getStatus() == 0) {
					creator.writeExcel("申请中", 14, line, wcfItem);
				} else if (stor.getStatus() == -1) {
					creator.writeExcel("审核中", 14, line, wcfItem);
				} else if (stor.getStatus() == -2) {
					creator.writeExcel("审核失败", 14, line, wcfItem);
				}
			}
		}
		creator.close();
	}

}
