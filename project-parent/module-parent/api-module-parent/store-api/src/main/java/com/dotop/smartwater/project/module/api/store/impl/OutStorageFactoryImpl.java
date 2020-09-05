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
import com.dotop.smartwater.project.module.api.store.IOutStorageFactory;
import com.dotop.smartwater.project.module.core.auth.local.AuthCasClient;
import com.dotop.smartwater.project.module.core.auth.vo.UserVo;
import com.dotop.smartwater.project.module.core.water.bo.OutStorageBo;
import com.dotop.smartwater.project.module.core.water.bo.StorageBo;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.module.core.water.form.OutStorageForm;
import com.dotop.smartwater.project.module.core.water.form.StorageForm;
import com.dotop.smartwater.project.module.core.water.utils.file.CellStyleFactory;
import com.dotop.smartwater.project.module.core.water.utils.file.ExcelCreator;
import com.dotop.smartwater.project.module.core.water.utils.file.ExcellException;
import com.dotop.smartwater.project.module.core.water.vo.OutStorageVo;
import com.dotop.smartwater.project.module.service.store.IOutStorageService;

import jxl.write.WritableCellFormat;

/**
 * 
 * 仓库管理-出库管理
 * 

 * @date 2018-11-30 下午 15:49
 *
 */

@Component
public class OutStorageFactoryImpl implements IOutStorageFactory {

	@Autowired
	private IOutStorageService iOutStorageService;

	@Override
	// 查询不需要添加事务注解
	// @Transactional(propagation = Propagation.REQUIRED, rollbackFor =
	// FrameworkRuntimeException.class)
	public Pagination<OutStorageVo> getOutStorByCon(OutStorageForm outStorageForm) {
		// 获取用户信息
		UserVo user = AuthCasClient.getUser();
		String userBy = user.getAccount();
		Date curr = new Date();
		// 业务逻辑
		OutStorageBo outStorageBo = new OutStorageBo();
		BeanUtils.copyProperties(outStorageForm, outStorageBo);
		outStorageBo.setEnterpriseid(user.getEnterpriseid());
		outStorageBo.setUserBy(userBy);
		outStorageBo.setCurr(curr);
		// 数据处理
		Pagination<OutStorageVo> pagination = iOutStorageService.getOutStorByCon(outStorageBo);
		return pagination;
	}

	@Override
	public OutStorageVo getOutStorByNo(OutStorageForm outStorageForm) {
		// 获取用户信息
		UserVo user = AuthCasClient.getUser();
		String userBy = user.getAccount();
		Date curr = new Date();

		// 业务逻辑
		OutStorageBo outStorageBo = new OutStorageBo();
		BeanUtils.copyProperties(outStorageForm, outStorageBo);
		outStorageBo.setEnterpriseid(user.getEnterpriseid());
		outStorageBo.setUserBy(userBy);
		outStorageBo.setCurr(curr);

		// 数据处理
		OutStorageVo outStore = iOutStorageService.getOutStorByNo(outStorageBo);
		return outStore;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public String addOutStor(OutStorageForm outStorageForm) {
		// 获取用户信息
		UserVo user = AuthCasClient.getUser();
		String userBy = user.getAccount();
		Date curr = new Date();
		// 业务逻辑
		OutStorageBo outStorageBo = new OutStorageBo();
		BeanUtils.copyProperties(outStorageForm, outStorageBo);
		outStorageBo.setEnterpriseid(user.getEnterpriseid());
		outStorageBo.setUserBy(userBy);
		outStorageBo.setCurr(curr);
		String recordNo = UuidUtils.getUuid();
		outStorageBo.setRecordNo(recordNo);

		if (outStorageBo.getStatus() != null && outStorageBo.getStatus() == 0) {
			outStorageBo.setOutstorageDate(curr);
		}
		outStorageBo.setOutUserId(user.getUserid());
		outStorageBo.setOutUsername(user.getName());

		// 数据处理
		Integer num = iOutStorageService.addOutStor(outStorageBo);
		if (num != 1) {
			throw new FrameworkRuntimeException(ResultCode.Fail, "新增出库失败！");
		}
		return recordNo;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public Integer editOutStor(OutStorageForm outStorageForm) {
		// 获取用户信息
		UserVo user = AuthCasClient.getUser();
		String userBy = user.getAccount();
		Date curr = new Date();
		// 业务逻辑
		OutStorageBo outStorageBo = new OutStorageBo();
		BeanUtils.copyProperties(outStorageForm, outStorageBo);
		outStorageBo.setEnterpriseid(user.getEnterpriseid());
		outStorageBo.setUserBy(userBy);
		outStorageBo.setCurr(curr);

		if (outStorageBo.getStatus() != null && outStorageBo.getStatus() == 0) {
			outStorageBo.setOutstorageDate(curr);
			outStorageBo.setOutUserId(user.getUserid());
			outStorageBo.setOutUsername(user.getName());
		}

		// 数据处理
		return iOutStorageService.editOutStor(outStorageBo);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public Integer confirmOutStor(OutStorageForm outStorageForm) {
		// 获取用户信息
		UserVo user = AuthCasClient.getUser();
		String userBy = user.getAccount();
		Date curr = new Date();
		// 业务逻辑
		OutStorageBo outStorageBo = new OutStorageBo();
		BeanUtils.copyProperties(outStorageForm, outStorageBo);
		outStorageBo.setEnterpriseid(user.getEnterpriseid());
		outStorageBo.setUserBy(userBy);
		outStorageBo.setCurr(curr);
		outStorageBo.setOutstorageDate(curr);
		outStorageBo.setOutUserId(user.getUserid());
		outStorageBo.setOutUsername(user.getName());
		outStorageBo.setStatus(0);
		// 数据处理
		return iOutStorageService.confirmOutStor(outStorageBo);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = FrameworkRuntimeException.class)
	public Integer deleteOutStor(OutStorageForm outStorageForm) {
		// 获取用户信息
		UserVo user = AuthCasClient.getUser();
		String userBy = user.getAccount();
		Date curr = new Date();
		// 业务逻辑
		OutStorageBo outStorageBo = new OutStorageBo();
		BeanUtils.copyProperties(outStorageForm, outStorageBo);
		outStorageBo.setEnterpriseid(user.getEnterpriseid());
		outStorageBo.setUserBy(userBy);
		outStorageBo.setCurr(curr);

		// 数据处理
		return iOutStorageService.deleteOutStor(outStorageBo);
	}

	@Override
	public Integer getProCount(StorageForm storageForm) {
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
		return iOutStorageService.getProCount(storageBo);
	}

	@Override
	public void exportOutStor(String tempFileName, OutStorageForm outStorageForm) throws ExcellException, IOException {
		// 获取用户信息
		UserVo user = AuthCasClient.getUser();

		OutStorageBo outStorageBo = new OutStorageBo();
		BeanUtils.copyProperties(outStorageForm, outStorageBo);
		outStorageBo.setEnterpriseid(user.getEnterpriseid());
		outStorageBo.setPage(1);
		outStorageBo.setPageCount(2000);// 最多打印2000条，太多excel内存炸掉

		int line = 0;
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH-MM-SS"); // 日期格式转换

		Pagination<OutStorageVo> pagination = iOutStorageService.getOutStorByCon(outStorageBo);
		List<OutStorageVo> out_list = pagination.getData();

		for (OutStorageVo item : out_list) {
			if (item.getOutstorageDate() != null) {
				item.setOutDateStr(formatter.format(item.getOutstorageDate()));
			}
		}

		ExcelCreator creator = new ExcelCreator(tempFileName);
		creator.CreateSheet("出库数据查询");
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
		creator.writeExcel("状 态", 9, line, wcf);
		creator.writeExcel("出库时间", 10, line, wcf);
		creator.writeExcel("出库人", 11, line, wcf);

		creator.getSheet().setColumnView(0, 10);
		creator.getSheet().setColumnView(1, 10);
		creator.getSheet().setColumnView(2, 15);
		creator.getSheet().setColumnView(3, 20);
		creator.getSheet().setColumnView(4, 20);
		creator.getSheet().setColumnView(5, 10);
		creator.getSheet().setColumnView(6, 10);
		creator.getSheet().setColumnView(7, 20);
		creator.getSheet().setColumnView(8, 20);
		creator.getSheet().setColumnView(9, 10);
		creator.getSheet().setColumnView(10, 30);
		creator.getSheet().setColumnView(11, 15);

		if (out_list != null && out_list.size() > 0) {
			for (OutStorageVo stor : out_list) {
				line++;
				creator.writeExcel(stor.getRecordNo(), 0, line, wcfItem);
				creator.writeExcel(stor.getRepoNo(), 1, line, wcfItem);
				creator.writeExcel(stor.getRepoName(), 2, line, wcfItem);
				creator.writeExcel(stor.getProductNo(), 3, line, wcfItem);
				creator.writeExcel(stor.getName(), 4, line, wcfItem);
				creator.writeExcel(stor.getQuantity(), 5, line, wcfItem);
				creator.writeExcel(stor.getUnit(), 6, line, wcfItem);
				creator.writeExcel(stor.getPrice(), 7, line, wcfItem);
				creator.writeExcel(stor.getTotal(), 8, line, wcfItem);
				if (stor.getStatus() == 0) {
					creator.writeExcel("已出库", 9, line, wcfItem);
				} else if (stor.getStatus() == 1) {
					creator.writeExcel("已保存", 9, line, wcfItem);
				} else if (stor.getStatus() == -1) {
					creator.writeExcel("申请中", 9, line, wcfItem);
				} else if (stor.getStatus() == -2) {
					creator.writeExcel("审核中", 9, line, wcfItem);
				} else if (stor.getStatus() == -3) {
					creator.writeExcel("审核失败", 9, line, wcfItem);
				}
				creator.writeExcel(stor.getOutDateStr(), 10, line, wcfItem);
				creator.writeExcel(stor.getOutUsername(), 11, line, wcfItem);
			}
		}
		creator.close();
	}

}
