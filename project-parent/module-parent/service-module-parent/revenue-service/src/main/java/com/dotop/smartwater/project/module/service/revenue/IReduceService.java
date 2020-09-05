package com.dotop.smartwater.project.module.service.revenue;

import java.util.List;

import com.dotop.smartwater.dependence.core.common.BaseService;
import com.dotop.smartwater.project.module.core.water.bo.ReduceBo;
import com.dotop.smartwater.project.module.core.water.vo.ReduceVo;

/**

 * @date 2019/2/26.
 */
public interface IReduceService extends BaseService<ReduceBo, ReduceVo> {
	/**
	 * 获取列表
	 *
	 * @param reduceBo
	 * @return @
	 */
	List<ReduceVo> getReduces(ReduceBo reduceBo);

	/**
	 * 添加
	 *
	 * @param reduceBo
	 * @return @
	 */
	int addReduce(ReduceBo reduceBo);

	/**
	 * 删除
	 *
	 * @param reduceBo
	 * @return @
	 */
	int delReduce(ReduceBo reduceBo);

	/**
	 * 编辑
	 *
	 * @param reduceBo
	 * @return @
	 */
	int editReduce(ReduceBo reduceBo);

	/**
	 * 根据id查找
	 *
	 * @param reduceBo
	 * @return @
	 */
	ReduceVo findById(ReduceBo reduceBo);
}
