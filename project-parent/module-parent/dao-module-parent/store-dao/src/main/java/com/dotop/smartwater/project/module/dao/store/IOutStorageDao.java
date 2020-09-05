package com.dotop.smartwater.project.module.dao.store;

import com.dotop.smartwater.dependence.core.common.BaseDao;
import com.dotop.smartwater.project.module.core.water.dto.OutStorageDto;
import com.dotop.smartwater.project.module.core.water.dto.StorageDto;
import com.dotop.smartwater.project.module.core.water.vo.OutStorageVo;

import java.util.List;

/**
 * 仓库管理-出库管理Mapper
 *

 * @date 2018-11-30 下午 15:55
 */

public interface IOutStorageDao extends BaseDao<OutStorageDto, OutStorageVo> {

	List<OutStorageVo> getOutStorByCon(OutStorageDto outStorageDto);

	OutStorageVo getOutStorByNo(OutStorageDto outStorageDto);

	Integer addOutStor(OutStorageDto outStorageDto);

	Integer editOutStor(OutStorageDto outStorageDto);

	Integer deleteOutStor(OutStorageDto outStorageDto);

	Integer confirmOutStor(OutStorageDto outStorageDto);

	Integer getProCount(StorageDto storageDto);
}
