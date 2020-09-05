package com.dotop.pipe.api.dao.model;

import org.springframework.dao.DataAccessException;

import com.dotop.pipe.core.dto.model.ModelMapDto;
import com.dotop.pipe.core.vo.model.ModelMapVo;
import com.dotop.smartwater.dependence.core.common.BaseDao;

// 模型与设备关系
public interface IModelMapDao extends BaseDao<ModelMapDto, ModelMapVo> {

	// 获取模型与设备关系
	public ModelMapVo get(ModelMapDto modelMapDto) throws DataAccessException;

}
