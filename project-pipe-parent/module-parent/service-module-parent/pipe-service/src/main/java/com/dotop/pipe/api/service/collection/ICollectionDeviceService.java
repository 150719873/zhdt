package com.dotop.pipe.api.service.collection;

import com.dotop.pipe.core.bo.collection.CollectionDeviceBo;
import com.dotop.pipe.core.vo.collection.CollectionDeviceVo;
import com.dotop.smartwater.dependence.core.common.BaseService;
import com.dotop.smartwater.dependence.core.pagination.Pagination;

// 收藏夹
public interface ICollectionDeviceService extends BaseService<CollectionDeviceBo, CollectionDeviceVo> {

	CollectionDeviceVo add(CollectionDeviceBo collectionDeviceBo);

	Pagination<CollectionDeviceVo> page(CollectionDeviceBo collectionDeviceBo);

	String del(CollectionDeviceBo collectionDeviceBo);
}
