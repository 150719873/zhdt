package com.dotop.smartwater.project.module.api.revenue;

import java.util.List;

import com.dotop.smartwater.dependence.core.common.BaseFactory;
import com.dotop.smartwater.dependence.core.pagination.Pagination;
import com.dotop.smartwater.project.module.core.water.form.OwnerChangeForm;
import com.dotop.smartwater.project.module.core.water.form.OwnerForm;
import com.dotop.smartwater.project.module.core.water.vo.OwnerRecordVo;
import com.dotop.smartwater.project.module.core.water.vo.OwnerVo;

/**

 */
public interface IOwnerFactory extends BaseFactory<OwnerForm, OwnerVo> {

	/**
	 * 新增业主
	 * 
	 * @param ownerForm
	 * @return @
	 */
	@Override
	OwnerVo add(OwnerForm ownerForm);

	/**
	 * 第三方接口批量新增
	 * @param owners
	 * @return
	 */
	boolean batchAdd(List<OwnerForm> owners);
	
	/**
	 * 第三方接口批量修改
	 * @param owners
	 * @return
	 */
	boolean batchEdit(List<OwnerForm> owners);
	
	/**
	 * 第三方接口批量换表
	 * @param changes
	 * @return
	 */
	boolean batchChange(List<OwnerChangeForm> changes);
	
	/**
	 * 开户
	 * 
	 * @param ownerForm @
	 */
	void openOwner(OwnerForm ownerForm);
	
	/**
	 * 检查业主是否存在
	 * 
	 * @param ownerForm
	 * @return @
	 */
	OwnerVo checkOwnerIsExist(OwnerForm ownerForm);

	/**
	 * 根据用户编号获取用户信息
	 * 
	 * @param ownerForm
	 * @return @
	 */
	OwnerVo getUserNoOwner(OwnerForm ownerForm);

	/**
	 * 通过关键字检索用户信息
	 * 
	 * @param ownerForm
	 * @return @
	 */
	OwnerVo getkeyWordOwner(OwnerForm ownerForm);

	/**
	 * 修改业主信息
	 * 
	 * @param ownerForm @
	 */
	void updateOwner(OwnerForm ownerForm);

	/**
	 * 删除业主
	 * 
	 * @param ownerForm @
	 */
	void delOwner(OwnerForm ownerForm);

	/**
	 * 检测业主开户状态
	 * 
	 * @param ownerForm @
	 */
	void checkOpen(OwnerForm ownerForm);

	/**
	 * 销户
	 * 
	 * @param ownerForm @
	 */
	void cancelOwner(OwnerForm ownerForm);

	/**
	 * 换水表
	 * 
	 * @param ownerForm @
	 */
	void changeDevice(OwnerForm ownerForm);

	/**
	 * 报装流程-创建用户并开户
	 * 
	 * @param ownerForm
	 * @return @
	 */
	int createOwner(OwnerForm ownerForm);

	/**
	 * 过户
	 * 
	 * @param ownerForm @
	 */
	void changeOwner(OwnerForm ownerForm);

	/**
	 * 获取业主信息（仅提供给水务后台App使用）
	 * 
	 * @param ownerForm @
	 */
	Pagination<OwnerVo> getOwners(OwnerForm ownerForm);

	/**
	 * 根据区域ID获取业主信息
	 * 
	 * @param ownerForm
	 * @return @
	 */
	List<OwnerVo> getCommunityOwner(OwnerForm ownerForm);

	/**
	 * 获取业主列表
	 * 
	 * @param ownerForm @
	 */
	Pagination<OwnerVo> getOwnerList(OwnerForm ownerForm);

	OwnerVo getDetailOwner(OwnerForm ownerForm);

	OwnerVo getOwner(OwnerForm ownerForm);

	void genNewOrder(OwnerForm ownerForm);

	Pagination<OwnerRecordVo> getRecordList(OwnerForm ownerForm);

	void batchUpdateOwner(OwnerForm ownerForm);

	void getCheckSearch(OwnerForm ownerForm);

	OwnerVo findByOwnerId(OwnerForm ownerForm);

	List<OwnerVo> findBusinessHallOwners(OwnerForm ownerForm);
}
