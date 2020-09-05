package com.dotop.pipe.web.api.factory.collection;

import com.dotop.pipe.core.form.CollectionDeviceForm;
import com.dotop.pipe.core.vo.collection.CollectionDeviceVo;
import com.dotop.smartwater.dependence.core.common.BaseFactory;
import com.dotop.smartwater.dependence.core.pagination.Pagination;

public interface ICollectionDeviceFactory extends BaseFactory<CollectionDeviceForm, CollectionDeviceVo> {

	CollectionDeviceVo add(CollectionDeviceForm collectionDeviceForm);

	Pagination<CollectionDeviceVo> page(CollectionDeviceForm collectionDeviceForm);

	String del(CollectionDeviceForm collectionDeviceForm);

}
