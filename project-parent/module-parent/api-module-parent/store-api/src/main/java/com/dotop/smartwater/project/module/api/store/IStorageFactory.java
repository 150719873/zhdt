package com.dotop.smartwater.project.module.api.store;

import com.dotop.smartwater.dependence.core.common.BaseFactory;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.water.form.StorageForm;
import com.dotop.smartwater.project.module.core.water.utils.file.ExcellException;
import com.dotop.smartwater.project.module.core.water.vo.StorageVo;
import com.dotop.smartwater.project.module.core.water.vo.StoreProductVo;

import java.io.IOException;
import java.util.List;

public interface IStorageFactory extends BaseFactory<StorageForm, StorageVo> {
	/**
	 * 获取入库信息并分页
	 * @param storageForm
	 * @return
	 * @throws FrameworkRuntimeException
	 */
	Pagination<StorageVo> getStorageByCon(StorageForm storageForm);
	/**
	 * 根据记录号获取入库信息
	 * @param storageForm
	 * @return
	 * @throws FrameworkRuntimeException
	 */
	StorageVo getStorageByNo(StorageForm storageForm);
	/**
	 * 新增入库
	 * @param storageForm
	 * @return
	 * @throws FrameworkRuntimeException
	 */
	Integer addStorage(StorageForm storageForm);
	/**
	 * 编辑入库信息
	 * @param storageForm
	 * @return
	 * @throws FrameworkRuntimeException
	 */
	Integer editStorage(StorageForm storageForm);
	/**
	 * 删除入库记录
	 * @param storageForm
	 * @return
	 * @throws FrameworkRuntimeException
	 */
	Integer deleteStorage(StorageForm storageForm);
	/**
	 * 确认入库
	 * @param storageForm
	 * @return
	 * @throws FrameworkRuntimeException
	 */
	Integer confirmStorage(StorageForm storageForm);
	/**
	 * 获取已入库产品信息
	 * @param storageForm
	 * @return
	 * @throws FrameworkRuntimeException
	 */
	List<StoreProductVo> getProIn(StorageForm storageForm);
	/**
	 * 导出
	 * @param tempFileName
	 * @param storageForm
	 * @throws ExcellException
	 * @throws IOException
	 */
	void exportStorage(String tempFileName, StorageForm storageForm) throws ExcellException, IOException;
}
