package com.dotop.smartwater.project.module.api.store;

import java.io.IOException;

import com.dotop.smartwater.dependence.core.common.BaseFactory;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.water.form.OutStorageForm;
import com.dotop.smartwater.project.module.core.water.form.StorageForm;
import com.dotop.smartwater.project.module.core.water.utils.file.ExcellException;
import com.dotop.smartwater.project.module.core.water.vo.OutStorageVo;

/* 
 * 仓库管理 

 * @date 2018-11-30 下午 15:46
 *
 */

public interface IOutStorageFactory extends BaseFactory<OutStorageForm, OutStorageVo> {

	/**
	 * 获取出库信息并分页
	 * 
	 * @param outStor
	 * @return
	 */
	Pagination<OutStorageVo> getOutStorByCon(OutStorageForm outStorageForm);
	/**
	 * 根据产品编号获取单条产品信息
	 * @param outStorageForm
	 * @return
	 * @throws FrameworkRuntimeException
	 */
	OutStorageVo getOutStorByNo(OutStorageForm outStorageForm);
	/**
	 * 新增出库
	 * @param outStorageForm
	 * @return
	 * @throws FrameworkRuntimeException
	 */
	String addOutStor(OutStorageForm outStorageForm);
	/**
	 * 编辑出库信息
	 * @param outStorageForm
	 * @return
	 * @throws FrameworkRuntimeException
	 */
	Integer editOutStor(OutStorageForm outStorageForm);
	/**
	 * 确认出库
	 * @param outStorageForm
	 * @return
	 * @throws FrameworkRuntimeException
	 */
	Integer confirmOutStor(OutStorageForm outStorageForm);
	/**
	 * 删除出库记录
	 * @param outStorageForm
	 * @return
	 * @throws FrameworkRuntimeException
	 */
	Integer deleteOutStor(OutStorageForm outStorageForm);
	/**
	 * 获取库存数量
	 * @param outStorageForm
	 * @return
	 * @throws FrameworkRuntimeException
	 */
	Integer getProCount(StorageForm storageForm);
	/**
	 * 导出出库表信息
	 * @param tempFileName
	 * @param outStorageForm
	 * @throws FrameworkRuntimeException
	 */
	void exportOutStor(String tempFileName, OutStorageForm outStorageForm) throws ExcellException, IOException;
}
