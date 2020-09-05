package com.dotop.pipe.api.dao.collection;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.dotop.pipe.core.dto.collection.CollectionDeviceDto;
import com.dotop.pipe.core.vo.collection.CollectionDeviceVo;
import com.dotop.smartwater.dependence.core.common.BaseDao;

public interface ICollectionDeviceDao extends BaseDao<CollectionDeviceDto, CollectionDeviceVo> {

	public void add(CollectionDeviceDto collectionDeviceDto) throws DataAccessException;

	public List<CollectionDeviceVo> page(CollectionDeviceDto collectionDeviceDto);

	Integer del(CollectionDeviceDto collectionDeviceDto);
}
