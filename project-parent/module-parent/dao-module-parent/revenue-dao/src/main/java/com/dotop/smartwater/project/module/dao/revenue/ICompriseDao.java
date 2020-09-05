package com.dotop.smartwater.project.module.dao.revenue;


import com.dotop.smartwater.dependence.core.common.BaseDao;
import com.dotop.smartwater.project.module.core.water.dto.CompriseDto;
import com.dotop.smartwater.project.module.core.water.vo.CompriseVo;

import java.util.List;

/**

 */
public interface ICompriseDao extends BaseDao<CompriseDto, CompriseVo> {

	void update(CompriseDto compriseDto);

	void insert(CompriseDto compriseDto);

	List<CompriseVo> findComprise(CompriseDto compriseDto);

	void deleteComprises(String typeid);

	void delete(String id);
}