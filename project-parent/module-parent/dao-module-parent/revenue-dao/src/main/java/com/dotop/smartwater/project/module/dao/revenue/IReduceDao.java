package com.dotop.smartwater.project.module.dao.revenue;

import com.dotop.smartwater.dependence.core.common.BaseDao;
import com.dotop.smartwater.project.module.core.water.dto.ReduceDto;
import com.dotop.smartwater.project.module.core.water.vo.ReduceVo;

import java.util.List;

/**

 * @date 2019/2/26.
 */
public interface IReduceDao extends BaseDao<ReduceDto, ReduceVo> {

	/**
	 * 获取列表
	 * @param reduceDto
	 * @return
	 * @
	 */
	List<ReduceVo> getReduces(ReduceDto reduceDto) ;

	/**
	 * 添加
	 * @param reduceDto
	 * @return
	 * @
	 */
	int addReduce(ReduceDto reduceDto) ;

	/**
	 * 删除
	 * @param reduceDto
	 * @return
	 * @
	 */
	int delReduce(ReduceDto reduceDto) ;

	/**
	 * 编辑
	 * @param reduceDto
	 * @return
	 * @
	 */
	int editReduce(ReduceDto reduceDto) ;

	/**
	 * 根据id查找
	 * @param reduceDto
	 * @return
	 * @
	 */
	ReduceVo findById(ReduceDto reduceDto) ;

	/**
	 * 根据减免类型是否已经被使用
	 * @param reduceDto
	 * @return
	 * @
	 */
    int checkReduceIsUse(ReduceDto reduceDto) ;
}
